package com.itemshare.service;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_DATA;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itemshare.model.ItemShareData;
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

	public ItemShareData getLocalData()
	{
		String json = getDataJson();

		return json == null
			? getDefaultData()
			: getData(json);
	}

	private String getDataJson()
	{
		return configManager.getConfiguration(CONFIG_BASE, CONFIG_DATA);
	}

	private ItemShareData getData(String json)
	{
		ItemShareData data = gson.fromJson(json, ItemShareData.class);
		return data;
	}

	private ItemShareData getDefaultData()
	{
		return ItemShareData.builder()
			.players(new ArrayList<>())
			.build();
	}

	public void saveLocalData(ItemShareData data)
	{
		configManager.setConfiguration(CONFIG_BASE, CONFIG_DATA, gson.toJson(data));
	}
}
