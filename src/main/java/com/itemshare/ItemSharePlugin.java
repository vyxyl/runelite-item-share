package com.itemshare;

import com.google.inject.Provides;
import com.itemshare.model.ItemShareContainer;
import com.itemshare.model.ItemShareData;
import com.itemshare.service.ItemShareCloudService;
import com.itemshare.service.ItemShareConfigService;
import com.itemshare.service.ItemShareDataService;
import com.itemshare.ui.ItemSharePanel;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
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

import javax.inject.Inject;
import java.awt.image.BufferedImage;

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

	private boolean isLoaded = false;
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

//		reset();
		loadLocalData("Frozen Gulf");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Item Share stopped!");
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		String playerName = getPlayerName();

		if (!isLoaded && playerName != null && isSupportedWorld())
		{
			loadLocalData(playerName);
		}
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event)
	{
		final GameState state = event.getGameState();

		if (state == GameState.LOGIN_SCREEN)
		{
			reset();
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (isLoaded && isSupportedWorld())
		{
			saveItems(event);
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

	private void saveItems(ItemContainerChanged event)
	{
		ItemContainer itemContainer = event.getItemContainer();
		ItemShareContainer itemShareContainer = dataService.getItemContainer(itemContainer);

		if (itemContainer == client.getItemContainer(InventoryID.BANK))
		{
			saveBank(itemShareContainer);
		}
		else if (itemContainer == client.getItemContainer(InventoryID.INVENTORY))
		{
			saveInventory(itemShareContainer);
		}
		else if (itemContainer == client.getItemContainer(InventoryID.EQUIPMENT))
		{
			saveEquipment(itemShareContainer);
		}
	}

	private void saveEquipment(ItemShareContainer itemShareContainer)
	{
		data.getLocalPlayer().setEquipment(itemShareContainer);
		data.getLocalPlayer().setUpdatedDate(new Date());
		updatePanel();
	}

	private void saveInventory(ItemShareContainer itemShareContainer)
	{
		data.getLocalPlayer().setInventory(itemShareContainer);
		data.getLocalPlayer().setUpdatedDate(new Date());
		updatePanel();
	}

	private void saveBank(ItemShareContainer itemShareContainer)
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

	private void reset()
	{
		isLoaded = false;
		SwingUtilities.invokeLater(() -> panel.reset());
	}

	private void update(String playerName)
	{
		data.setOtherPlayers(cloudService.getOtherPlayers());
		updatePanel();

		configService.saveLocalData(data);
		cloudService.saveLocalData(data.getLocalPlayer());
	}

	private void loadLocalData(String playerName)
	{
		data = configService.getLocalData(playerName);
		updatePanel();
		isLoaded = true;
	}

	private String getPlayerName()
	{
		return Objects.requireNonNull(client.getLocalPlayer()).getName();
	}

	private void updatePanel()
	{
		SwingUtilities.invokeLater(() -> panel.update(itemManager, data));
	}
}
