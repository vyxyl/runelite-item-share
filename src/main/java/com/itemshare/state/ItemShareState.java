package com.itemshare.state;

import com.itemshare.db.ItemShareDB;
import com.itemshare.model.ItemSharePlayer;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ClientToolbar;

public class ItemShareState
{
	// client
	public static Client client;
	public static ClientThread clientThread;
	public static ClientToolbar toolbar;
	public static ItemManager itemManager;
	public static ConfigManager configManager;

	// database
	public static ItemShareDB db;

	// plugin
	public static String groupId;
	public static String playerName = "";
	public static ItemSharePlayer player;
	public static List<String> playerNames = new ArrayList<>();
}
