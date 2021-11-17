package com.itemshare;

import com.google.inject.Provides;
import com.itemshare.model.ItemShareContainer;
import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.service.ItemShareCloudService;
import com.itemshare.service.ItemShareConfigService;
import com.itemshare.service.ItemShareDataService;
import com.itemshare.service.ItemShareDefaultDataService;
import com.itemshare.ui.ItemSharePanel;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
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
	private ItemShareCloudService cloudService;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ConfigManager configManager;

	@Inject
	private ItemManager itemManager;

	private String playerName;
	private ItemShareData data;
	private ItemSharePanel panel;
	private ItemSharePlayer player;

	@Provides
	ItemShareConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ItemShareConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		assert SwingUtilities.isEventDispatchThread();

		panel = injector.getInstance(ItemSharePanel.class);
		createNavigationButton(panel);

		loadData();
	}

	@Override
	protected void shutDown() throws Exception
	{
		saveData();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		loadPlayerData();

		if (data != null && gameStateChanged.getGameState() != GameState.LOGGED_IN)
		{
			saveData();
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		loadPlayerData();
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (isSupportedWorld() && player != null)
		{
			loadItems(event);
		}
	}

	private void loadData()
	{
		data = configService.getLocalData();
		data.setPlayers(cloudService.getPlayers());
		updatePanel();
	}

	private void loadPlayerData()
	{
		if (playerName == null)
		{
			playerName = getPlayerName();
		}
		else if (player == null && data != null)
		{
			player = getLocalPlayer(data, playerName);
			updatePanel();
		}
	}

	private boolean isSupportedWorld()
	{
		List<WorldType> worldTypes = new ArrayList<>(client.getWorldType());

		List<WorldType> unsupportedWorldTypes = new ArrayList<>(EnumSet.of(
			WorldType.NOSAVE_MODE,
			WorldType.TOURNAMENT_WORLD,
			WorldType.DEADMAN,
			WorldType.SEASONAL
		));

		return worldTypes.stream().noneMatch(unsupportedWorldTypes::contains);
	}

	private void loadItems(ItemContainerChanged event)
	{
		ItemContainer itemContainer = event.getItemContainer();
		ItemShareContainer itemShareContainer = dataService.getItemContainer(itemContainer);

		if (itemContainer == client.getItemContainer(InventoryID.BANK))
		{
			loadBank(itemShareContainer);
		}
		else if (itemContainer == client.getItemContainer(InventoryID.INVENTORY))
		{
			loadInventory(itemShareContainer);
		}
		else if (itemContainer == client.getItemContainer(InventoryID.EQUIPMENT))
		{
			loadEquipment(itemShareContainer);
		}
	}

	private void loadEquipment(ItemShareContainer itemShareContainer)
	{
		player.setEquipment(itemShareContainer);
		player.setUpdatedDate(new Date());
		updatePanel();
	}

	private void loadInventory(ItemShareContainer itemShareContainer)
	{
		player.setInventory(itemShareContainer);
		player.setUpdatedDate(new Date());
		updatePanel();
	}

	private void loadBank(ItemShareContainer itemShareContainer)
	{
		player.setBank(itemShareContainer);
		player.setUpdatedDate(new Date());
		updatePanel();
	}

	private void createNavigationButton(ItemSharePanel panel)
	{
		BufferedImage icon = ImageUtil.loadImageResource(ItemSharePlugin.class, "/icon.png");
		NavigationButton button = NavigationButton.builder()
			.tooltip("View shared items")
			.icon(icon)
			.priority(7)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(button);
	}

	private ItemSharePlayer getLocalPlayer(ItemShareData data, String playerName)
	{
		return data.getPlayers().stream()
			.filter(p -> p.getUserName().equals(playerName))
			.findFirst()
			.orElseGet(() -> getNewPlayer(data, playerName));
	}

	private ItemSharePlayer getNewPlayer(ItemShareData data, String playerName)
	{
		ItemSharePlayer player = ItemShareDefaultDataService.getDefaultPlayerData(playerName);
		data.getPlayers().add(player);
		return player;
	}

	private String getPlayerName()
	{
		Player player = client.getLocalPlayer();
		return player == null ? null : player.getName();
	}

	private void updatePanel()
	{
		SwingUtilities.invokeLater(() -> panel.update(itemManager, data));
	}

	private void saveData()
	{
		configService.saveLocalData(data);
		cloudService.savePlayerData(player);
	}
}
