package com.itemshare;

import com.google.inject.Provides;
import com.itemshare.model.ItemShareContainer;
import com.itemshare.model.ItemShareData;
import com.itemshare.service.ItemShareCloudService;
import com.itemshare.service.ItemShareConfigService;
import com.itemshare.service.ItemShareDataService;
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
	}

	@Override
	protected void shutDown() throws Exception
	{
		saveData();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		retrieveLocalState();
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		retrieveLocalState();
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (isSupportedWorld() && data != null)
		{
			loadItems(event);
		}
	}

	private void retrieveLocalState()
	{
		if (playerName == null)
		{
			playerName = getPlayerName();
		}
		else if (data == null)
		{
			retrieveLocalPlayer();
			retrieveOtherPLayers();
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
		data.getLocalPlayer().setEquipment(itemShareContainer);
		data.getLocalPlayer().setUpdatedDate(new Date());
		updatePanel();
	}

	private void loadInventory(ItemShareContainer itemShareContainer)
	{
		data.getLocalPlayer().setInventory(itemShareContainer);
		data.getLocalPlayer().setUpdatedDate(new Date());
		updatePanel();
	}

	private void loadBank(ItemShareContainer itemShareContainer)
	{
		data.getLocalPlayer().setBank(itemShareContainer);
		data.getLocalPlayer().setUpdatedDate(new Date());
		updatePanel();
	}

	private void createNavigationButton(ItemSharePanel panel)
	{
		BufferedImage icon = ImageUtil.loadImageResource(ItemSharePlugin.class, "/icon.png");
		NavigationButton button = NavigationButton.builder()
			.tooltip("Show shared items")
			.icon(icon)
			.priority(7)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(button);
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

	private void retrieveLocalPlayer()
	{
		data = configService.getLocalData(playerName);
	}

	private void retrieveOtherPLayers()
	{
		data.setOtherPlayers(cloudService.getOtherPlayers());
	}

	private void saveData()
	{
		configService.saveLocalData(playerName, data);
		cloudService.uploadLocalData(data.getLocalPlayer());
	}
}
