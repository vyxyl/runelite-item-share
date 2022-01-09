package com.itemshare.state;

import com.itemshare.db.ItemShareAPI;
import com.itemshare.model.ItemShareGIMStorage;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.service.ItemShareAPIService;
import com.itemshare.service.ItemShareUIService;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import org.apache.commons.lang3.StringUtils;

public class ItemShareState
{
	public static Client client;
	public static ClientThread clientThread;
	public static ItemManager itemManager;
	public static ConfigManager configManager;

	public static ItemShareAPI api;

	public static String groupId = null;
	public static String playerName = "";
	public static ItemSharePlayer player = null;
	public static List<String> playerNames = new ArrayList<>();
	public static ItemShareGIMStorage tempStorage = getEmptyGIMStorage();
	public static ItemShareGIMStorage gimStorage = getEmptyGIMStorage();

	private static ItemShareGIMStorage getEmptyGIMStorage()
	{
		return ItemShareGIMStorage.builder()
			.items(new ArrayList<>())
			.updatedDate(null)
			.build();
	}

	public static void reset()
	{
		playerNames = new ArrayList<>();

		if (player != null && !StringUtils.isEmpty(player.getName()))
		{
			playerName = player.getName();
			playerNames.add(player.getName());
		}
		else
		{
			playerName = "";
			player = null;
		}

		tempStorage = getEmptyGIMStorage();
		gimStorage = getEmptyGIMStorage();

		ItemShareAPIService.getPlayerNames(names -> {
			ItemShareState.playerNames = names;
			ItemShareAPIService.getGIMStorage(gimStorage -> {
				ItemShareState.gimStorage = gimStorage;
				ItemShareUIService.update();
			}, ItemShareUIService::update);
		}, ItemShareUIService::update);
	}
}