package com.itemshare;

import com.google.inject.Provides;
import static com.itemshare.constant.ItemShareConstants.ICON_NAV_BUTTON;
import static com.itemshare.constant.ItemShareConstants.MONGODB_SYNC_FREQUENCY_MS;
import com.itemshare.db.ItemShareMongoDB;
import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemShareItems;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.model.ItemShareSlots;
import com.itemshare.service.ItemShareConfigService;
import com.itemshare.service.ItemShareDataService;
import com.itemshare.ui.ItemSharePanel;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.WorldType;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.banktags.BankTagsPlugin;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@PluginDescriptor(
	name = "Item Share"
)
@PluginDependency(BankTagsPlugin.class)
public class ItemSharePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ItemShareConfig config;

	@Inject
	private ItemShareDataService dataService;

	@Inject
	private ItemShareConfigService configService;

	@Inject
	private ItemShareMongoDB db;

	@Inject
	private ClientToolbar toolbar;

	@Inject
	private ConfigManager configManager;

	@Inject
	private ItemManager itemManager;

	private String playerName;
	private ItemShareData data;
	private ItemSharePlayer player;
	private ItemSharePanel panel;
	private Instant lastSync;

	@Provides
	ItemShareConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ItemShareConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		loadUI();
		loadLocalData();
		updateUI();

		db.connect(this::onConnectionSuccess, this::onConnectionFailure);
	}

	@Override
	protected void shutDown() throws Exception
	{
		saveData();
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		loadPlayer();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged e)
	{
		loadPlayer();

		boolean isNotLoggedIn = e.getGameState().equals(GameState.LOGGED_IN);

		if (isValidState() && !isNotLoggedIn)
		{
			saveData();
		}

		updateUI();
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (isValidState())
		{
			loadItems(event);
		}
	}

	private void onConnectionSuccess()
	{
		syncData();
		updateUI();
	}

	private void onConnectionFailure()
	{
		updateUI();
	}

	private void syncData()
	{
		if (lastSync == null || db.isConnected() && isSyncExpired())
		{
			lastSync = Instant.now();
			savePlayer();
			loadPlayers();
			updateUI();
		}
	}

	private boolean isSyncExpired()
	{
		return Duration.between(lastSync, Instant.now()).toMillis() > MONGODB_SYNC_FREQUENCY_MS;
	}

	private boolean isSupportedWorld()
	{
		return client.getWorldType().stream().noneMatch(EnumSet.of(
			WorldType.NOSAVE_MODE,
			WorldType.TOURNAMENT_WORLD,
			WorldType.DEADMAN,
			WorldType.SEASONAL
		)::contains);
	}

	private void loadLocalData()
	{
		data = configService.getLocalData();
	}

	private void loadPlayers()
	{
		List<ItemSharePlayer> dbPlayers = db.getPlayers();
		data.setPlayers(dbPlayers);
	}

	private void loadItems(ItemContainerChanged event)
	{
		ItemContainer container = event.getItemContainer();

		if (container == client.getItemContainer(InventoryID.BANK))
		{
			loadBank(container);
		}
		else if (container == client.getItemContainer(InventoryID.INVENTORY))
		{
			loadInventory(container);
		}
		else if (container == client.getItemContainer(InventoryID.EQUIPMENT))
		{
			loadEquipment(container);
		}
	}

	private void loadBank(ItemContainer container)
	{
		player.setBank(dataService.getBankContainer(container));
		player.setUpdatedDate(new Date());

		updateUI();
	}

	private void loadInventory(ItemContainer container)
	{
		player.setInventory(dataService.getInventoryContainer(container));
		player.setUpdatedDate(new Date());

		updateUI();
	}

	private void loadEquipment(ItemContainer container)
	{
		player.setEquipment(dataService.getEquipmentContainer(container));
		player.setUpdatedDate(new Date());

		updateUI();
	}

	private void loadPlayer()
	{
		if (StringUtils.isEmpty(playerName))
		{
			playerName = getPlayerName();
		}
		else if (player == null)
		{
			player = getPlayer();
			updateUI();
		}
	}

	private String getPlayerName()
	{
		Player player = client.getLocalPlayer();
		return player == null ? null : player.getName();
	}

	private ItemSharePlayer getPlayer()
	{
		return findPlayer().orElseGet(this::addNewPlayer);
	}

	private Optional<ItemSharePlayer> findPlayer()
	{
		return data.getPlayers().stream()
			.filter(player -> StringUtils.equals(player.getName(), playerName))
			.findFirst();
	}

	private ItemSharePlayer addNewPlayer()
	{
		ItemSharePlayer player = ItemSharePlayer.builder()
			.name(playerName)
			.updatedDate(new Date())
			.bank(ItemShareItems.builder().items(new ArrayList<>()).build())
			.equipment(ItemShareSlots.builder().slots(new HashMap<>()).build())
			.inventory(ItemShareItems.builder().items(new ArrayList<>()).build())
			.build();

		data.getPlayers().add(player);
		return player;
	}

	private boolean isValidState()
	{
		return isSupportedWorld()
			&& player != null
			&& !StringUtils.isEmpty(player.getName())
			&& data != null;
	}

	private void loadUI()
	{
		assert SwingUtilities.isEventDispatchThread();
		panel = new ItemSharePanel(configManager, db);

		BufferedImage icon = ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_NAV_BUTTON);
		NavigationButton button = NavigationButton.builder()
			.tooltip("View shared items")
			.icon(icon)
			.priority(7)
			.panel(panel)
			.build();

		toolbar.addNavigation(button);
	}

	private void updateUI()
	{
		SwingUtilities.invokeLater(() -> panel.update(itemManager, data, db.getStatus()));
	}

	private void saveData()
	{
		configService.saveLocalData(data);
		savePlayer();
	}

	private void savePlayer()
	{
		if (db.isConnected() && player != null && player.getName() != null)
		{
			db.savePlayer(player);
		}
	}
}
