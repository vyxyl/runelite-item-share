package com.itemshare.service;

import com.itemshare.model.ItemSharePlayer;
import com.itemshare.state.ItemShareState;
import java.util.List;
import java.util.function.Consumer;

public class ItemShareAPIService
{
	public static void savePlayer(Runnable onSuccess)
	{
		savePlayer(onSuccess, () -> {});
	}

	public static void savePlayer(Runnable onSuccess, Runnable onFailure)
	{
		boolean isSupportedWorld = ItemShareSupportedService.isSupportedWorld();

		if (isSupportedWorld)
		{
			ItemShareState.api.savePlayer(ItemShareState.groupId, ItemShareState.player, onSuccess, onFailure);
		}
	}

	public static void getPlayer(String playerName, Consumer<ItemSharePlayer> onSuccess)
	{
		ItemShareState.api.getPlayer(ItemShareState.groupId, playerName, onSuccess);
	}

	public static void getPlayerNames(Consumer<List<String>> onSuccess)
	{
		getPlayerNames(onSuccess, () -> {});
	}

	public static void getPlayerNames(Consumer<List<String>> onSuccess, Runnable onFailure)
	{
		ItemShareState.api.getPlayerNames(ItemShareState.groupId, onSuccess, onFailure);
	}
}