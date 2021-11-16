package com.itemshare.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import static com.itemshare.ui.ItemShareConfigKeys.CONFIG_BASE;
import static com.itemshare.ui.ItemShareConfigKeys.CONFIG_DATA;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.config.ConfigManager;

@Singleton
public class ItemShareConfigService
{
	private final Gson gson;
	private final ConfigManager configManager;

	@Inject
	private ItemShareConfigService(final ConfigManager configManager)
	{
		this.configManager = configManager;

		GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
		this.gson = builder.create();
	}

	public ItemShareData getLocalData(String playerName)
	{
		String json = configManager.getConfiguration(CONFIG_BASE, CONFIG_DATA + "_" + playerName);

		if (json == null)
		{
			return ItemShareData.builder()
				.localPlayer(getLocalPlayerData(playerName))
				.otherPlayers(new ArrayList<>())
				.build();
		}
		else
		{
			ItemShareData data = gson.fromJson(json, ItemShareData.class);
			data.getLocalPlayer().setUserName(playerName);
			return data;
		}
	}

	public void saveLocalData(ItemShareData data)
	{
		configManager.setConfiguration(CONFIG_BASE, CONFIG_DATA, gson.toJson(data));
	}

	private ItemSharePlayer getLocalPlayerData(String playerName)
	{
		return ItemSharePlayer.builder().userName(playerName).build();
	}
}
