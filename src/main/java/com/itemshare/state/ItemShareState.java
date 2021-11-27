package com.itemshare.state;

import com.itemshare.db.ItemShareCentralDB;
import com.itemshare.db.ItemShareDB;
import com.itemshare.db.ItemShareMongoDB;
import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ClientToolbar;

public class ItemShareState
{
	// runelite
	public static Client client;
	public static ClientToolbar toolbar;
	public static ItemManager itemManager;
	public static ConfigManager configManager;

	// database
	public static ItemShareDB db;
	public static ItemShareMongoDB mongoDB;
	public static ItemShareCentralDB centralDB;

	// plugin
	public static ItemShareData data;
	public static ItemSharePlayer player;
	public static String playerName;
	public static String groupId;
}
