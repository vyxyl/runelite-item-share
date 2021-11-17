package com.itemshare.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itemshare.model.ItemShareContainer;
import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import static com.itemshare.constant.ItemShareConfigKeys.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConfigKeys.CONFIG_DATA;
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
		this.gson = new GsonBuilder().disableHtmlEscaping().create();
	}

	public ItemShareData getLocalData(String playerName)
	{
		if (playerName == null)
		{
			return getDefaultData(null);
		}
		else
		{
			String json = getDataJson(playerName);

			return json == null
				? getDefaultData(playerName)
				: getItemShareData(playerName, json);
		}
	}

	private String getDataJson(String playerName)
	{
		return configManager.getConfiguration(CONFIG_BASE, CONFIG_DATA + "_" + playerName);
	}

	private ItemShareData getItemShareData(String playerName, String json)
	{
		ItemShareData data = gson.fromJson(json, ItemShareData.class);
		data.getLocalPlayer().setUserName(playerName);
		return data;
	}

	private ItemShareData getDefaultData(String playerName)
	{
		return ItemShareData.builder()
			.localPlayer(getLocalPlayerData(playerName))
			.otherPlayers(new ArrayList<>())
			.build();
	}

	public void saveLocalData(String playerName, ItemShareData data)
	{
		configManager.setConfiguration(CONFIG_BASE, CONFIG_DATA + "_" + playerName, gson.toJson(data));
	}

	private ItemSharePlayer getLocalPlayerData(String playerName)
	{
		return ItemSharePlayer.builder()
			.userName(playerName)
			.bank(ItemShareContainer.builder()
				.items(new ArrayList<>())
				.build())
			.equipment(ItemShareContainer.builder()
				.items(new ArrayList<>())
				.build())
			.inventory(ItemShareContainer.builder()
				.items(new ArrayList<>())
				.build())
			.build();
	}
}
