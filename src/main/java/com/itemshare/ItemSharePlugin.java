package com.itemshare;

import com.google.inject.Provides;
import static com.itemshare.constant.ItemShareConstants.ICON_NAV_BUTTON;
import com.itemshare.db.ItemShareMongoDB;
import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.service.ItemShareConfigService;
import com.itemshare.service.ItemShareDataService;
import com.itemshare.ui.ItemSharePanel;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
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

	private final long SYNC_MS = 15 * 1000;
	private String playerName;
	private ItemShareData data;
	private ItemSharePlayer player;
	private ItemSharePanel panel;
	private Instant lastSync = Instant.now();
	private boolean isConnected = false;

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

		db.setPlayersCallback(players -> {
			setPlayers(players);
			isConnected = true;
		});
		db.connect();
		loadPlayers();
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
		syncData();
	}

	private void syncData()
	{
		if (isConnected)
		{
			Duration diff = Duration.between(lastSync, Instant.now());
			if (diff.toMillis() > SYNC_MS)
			{
				lastSync = Instant.now();
				savePlayer();
				loadPlayers();
			}
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged e)
	{
		if (isValidState())
		{
			if (e.getGameState().equals(GameState.LOGGED_IN))
			{
				loadPlayer();
				updateUI();
			}
			else
			{
				saveData();
			}
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (isValidState())
		{
			loadItems(event);
		}
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
		List<ItemSharePlayer> players = db.getPlayers();

		setPlayers(players);
	}

	private void setPlayers(List<ItemSharePlayer> players)
	{
		List<String> names = players.stream().map(ItemSharePlayer::getName).collect(Collectors.toList());
		data.getPlayers().removeIf(p -> names.contains(p.getName()));
		data.getPlayers().addAll(players);
		updateUI();
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

	private void loadEquipment(ItemContainer container)
	{
		player.setEquipment(dataService.getItemContainer(container));
		player.setUpdatedDate(new Date());

		updateUI();
	}

	private void loadInventory(ItemContainer container)
	{
		player.setInventory(dataService.getItemContainer(container));
		player.setUpdatedDate(new Date());

		updateUI();
	}

	private void loadBank(ItemContainer container)
	{
		player.setBank(dataService.getItemContainer(container));
		player.setUpdatedDate(new Date());

		updateUI();
	}

	private void loadPlayer()
	{
		if (playerName == null)
		{
			playerName = getPlayerName();
		}
		else if (player == null)
		{
			player = getPlayer();
		}
	}

	private String getPlayerName()
	{
		Player player = client.getLocalPlayer();
		return player == null ? null : player.getName();
	}

	private ItemSharePlayer getPlayer()
	{
		return data.getPlayers().stream()
			.filter(player -> player.getName().equals(playerName))
			.findFirst()
			.orElseGet(() -> addPlayer(data, playerName));
	}

	private ItemSharePlayer addPlayer(ItemShareData data, String name)
	{
		ItemSharePlayer player = ItemSharePlayer.builder()
			.name(playerName)
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
		SwingUtilities.invokeLater(() -> panel.update(itemManager, data, isConnected));
	}

	private void saveData()
	{
		configService.saveLocalData(data);
		savePlayer();
	}

	private void savePlayer()
	{
		if (isConnected)
		{
			db.savePlayer(player);
		}
	}
}
