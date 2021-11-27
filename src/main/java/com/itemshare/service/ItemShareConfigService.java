package com.itemshare.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_DATA;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_ENABLED;
import com.itemshare.model.ItemShareData;
import com.itemshare.state.ItemShareState;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

public class ItemShareConfigService
{
	private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	public static ItemShareData getLocalData()
	{
		String json = getDataJson();

		return json == null
			? getDefaultData()
			: getData(json);
	}

	private static String getDataJson()
	{
		return ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_DATA);
	}

	private static ItemShareData getData(String json)
	{
		return gson.fromJson(json, ItemShareData.class);
	}

	private static ItemShareData getDefaultData()
	{
		return ItemShareData.builder()
			.players(new ArrayList<>())
			.build();
	}

	public static void saveLocalData(ItemShareData data)
	{
		ItemShareState.configManager.setConfiguration(CONFIG_BASE, CONFIG_DATA, gson.toJson(data));
	}

	public static boolean isSelfHost()
	{
		String value = ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_MONGODB_ENABLED);

		return StringUtils.equals(value, "true");
	}
}
