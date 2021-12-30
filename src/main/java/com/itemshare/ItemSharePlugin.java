package com.itemshare;

import com.google.inject.Provides;
import com.itemshare.db.ItemShareAPI;
import com.itemshare.service.ItemShareContainerService;
import com.itemshare.service.ItemShareGroupIdService;
import com.itemshare.service.ItemSharePlayerService;
import com.itemshare.service.ItemShareAPIService;
import com.itemshare.service.ItemShareUIService;
import com.itemshare.state.ItemShareState;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.banktags.BankTagsPlugin;
import net.runelite.client.ui.ClientToolbar;

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
	private ClientToolbar toolbar;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ItemShareConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private ItemShareAPI dedicatedDB;

	@Inject
	private ClientThread clientThread;

	@Provides
	ItemShareConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ItemShareConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		ItemShareState.client = client;
		ItemShareState.toolbar = toolbar;
		ItemShareState.itemManager = itemManager;
		ItemShareState.configManager = configManager;
		ItemShareState.api = dedicatedDB;
		ItemShareState.clientThread = clientThread;

		ItemShareUIService.load();
		ItemShareGroupIdService.loadExistingId();
		ItemShareAPIService.load();
		ItemShareUIService.update();
	}

	@Override
	protected void shutDown() throws Exception
	{
		ItemShareAPIService.save();
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		ItemSharePlayerService.load();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		ItemSharePlayerService.load();

		boolean isLoggedIn = event.getGameState().equals(GameState.LOGGED_IN);

		if (!isLoggedIn)
		{
			ItemShareAPIService.save();
			ItemShareState.player = null;
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		ItemShareContainerService.load(event);
	}
}
