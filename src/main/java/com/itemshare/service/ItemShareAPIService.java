package com.itemshare.service;

import com.itemshare.model.ItemSharePlayer;
import com.itemshare.state.ItemShareState;
import java.util.List;
import java.util.function.Consumer;

public class ItemShareAPIService
{
	public static void load()
	{
		loadPlayerNames(names -> ItemShareUIService.update());
	}

	public static void sync()
	{
		loadPlayerNames(names -> save());
	}

	public static void save()
	{
		if (ItemShareSupportedService.isSupported())
		{
			ItemShareState.api.savePlayer(ItemShareState.groupId, ItemShareState.player, ItemShareUIService::update);
		}
	}

	public static void getPlayer(String playerName, Consumer<ItemSharePlayer> onSuccess)
	{
		ItemShareState.api.getPlayer(ItemShareState.groupId, playerName, onSuccess);
	}

	private static void loadPlayerNames(Consumer<List<String>> onSuccess)
	{
		ItemShareState.api.getPlayerNames(ItemShareState.groupId, names -> {
			ItemShareState.playerNames = names;
			ItemSharePlayerService.loadLoggedInPlayerName();

			onSuccess.accept(names);
		});
	}
}
