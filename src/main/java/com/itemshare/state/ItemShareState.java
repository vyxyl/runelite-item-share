package com.itemshare.state;

import com.itemshare.db.ItemShareDedicatedDB;
import com.itemshare.db.ItemShareMongoDB;
import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import java.util.ArrayList;
import java.util.List;
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
	public static ItemShareMongoDB selfHostDb;
	public static ItemShareDedicatedDB dedicatedDB;
	public static Runnable onSelfHostSuccess = () -> {};

	// plugin
	public static List<ItemSharePlayer> players;
	public static ItemSharePlayer player;
	public static List<String> playerNames = new ArrayList<>();
	public static String playerName;
	public static String groupId;
}
