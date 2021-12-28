package com.itemshare.state;

import com.itemshare.db.ItemShareDedicatedDB;
import com.itemshare.db.ItemShareMongoDB;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.model.ItemSharePlayerLite;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
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
	public static Runnable onSelfHostSuccess = () -> {
	};

	// plugin
	public static List<ItemSharePlayer> players = new ArrayList<>();
	public static List<String> playerNames = new ArrayList<>();

	// async data
	public static ClientThread clientThread;

	public static ItemSharePlayer player;
	public static String playerName = "";
	public static String groupId;
}
