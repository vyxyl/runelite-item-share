package com.itemshare.state;

import com.itemshare.db.ItemShareAPI;
import com.itemshare.model.ItemSharePlayer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;

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
	public static Date autoSaveDate = null;
	public static List<String> playerNames = new ArrayList<>();
}