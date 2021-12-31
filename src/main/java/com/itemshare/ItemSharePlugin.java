package com.itemshare;

import com.google.inject.Provides;
import com.itemshare.db.ItemShareAPI;
import com.itemshare.service.ItemShareAPIService;
import com.itemshare.service.ItemShareContainerService;
import com.itemshare.service.ItemShareGroupIdService;
import com.itemshare.service.ItemSharePlayerService;
import com.itemshare.service.ItemShareUIService;
import com.itemshare.state.ItemShareState;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.swing.Timer;
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
	private ClientThread clientThread;

	@Inject
	private ItemShareAPI api;

	@Provides
	ItemShareConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ItemShareConfig.class);
	}

	private Timer saveTimer;

	@Override
	protected void startUp() throws Exception
	{
		ItemShareState.client = client;
		ItemShareState.clientThread = clientThread;
		ItemShareState.itemManager = itemManager;
		ItemShareState.configManager = configManager;
		ItemShareState.api = api;

		toolbar.addNavigation(ItemShareUIService.getNavButton());

		ItemShareState.groupId = ItemShareGroupIdService.getGroupId();

		ItemShareAPIService.getPlayerNames(names -> {
			ItemShareState.playerNames = names;
			ItemShareUIService.update();
		}, ItemShareUIService::update);

		saveTimer = getSaveTimer();
	}

	@Override
	protected void shutDown() throws Exception
	{
		ItemShareAPIService.savePlayer(this::clearPlayer, this::clearPlayer);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		ItemSharePlayerService.loadPlayerData();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		boolean isLoggedIn = event.getGameState().equals(GameState.LOGGED_IN);

		if (isLoggedIn)
		{
			saveTimer.start();
		}
		else
		{
			saveTimer.stop();
			ItemShareAPIService.savePlayer(this::clearPlayer, this::clearPlayer);
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		ItemShareContainerService.loadContainer(event);
	}

	private void clearPlayer()
	{
		ItemShareState.player = null;
		ItemShareState.playerName = null;
	}

	private Timer getSaveTimer()
	{
		int repeatMs = (int) TimeUnit.MINUTES.toMillis(10);
		return new Timer(repeatMs, event -> ItemShareAPIService.savePlayer());
	}
}