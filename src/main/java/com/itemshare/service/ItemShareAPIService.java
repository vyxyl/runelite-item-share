package com.itemshare.service;

import com.itemshare.model.ItemShareGIMStorage;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.state.ItemShareState;
import java.util.List;
import java.util.function.Consumer;

public class ItemShareAPIService
{
	public static void savePlayer()
	{
		savePlayer(() -> {
		});
	}

	public static void savePlayer(Runnable onSuccess)
	{
		savePlayer(onSuccess, () -> {
		});
	}

	public static void savePlayer(Runnable onSuccess, Runnable onFailure)
	{
		boolean isPlayerAvailable = ItemShareState.player != null;
		boolean isSupportedWorld = ItemShareSupportedService.isSupportedWorld();

		if (isSupportedWorld && isPlayerAvailable)
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
		getPlayerNames(onSuccess, () -> {
		});
	}

	public static void getPlayerNames(Consumer<List<String>> onSuccess, Runnable onFailure)
	{
		ItemShareState.api.getPlayerNames(ItemShareState.groupId, onSuccess, onFailure);
	}

	public static void getGIMStorage(Consumer<ItemShareGIMStorage> onSuccess)
	{
		getGIMStorage(onSuccess, () -> {
		});
	}

	public static void getGIMStorage(Consumer<ItemShareGIMStorage> onSuccess, Runnable onFailure)
	{
		ItemShareState.api.getGIMStorage(ItemShareState.groupId, onSuccess, onFailure);
	}

	public static void saveGIMStorage()
	{
		saveGIMStorage(() -> {
		});
	}

	public static void saveGIMStorage(Runnable onSuccess)
	{
		saveGIMStorage(onSuccess, () -> {
		});
	}

	public static void saveGIMStorage(Runnable onSuccess, Runnable onFailure)
	{
		ItemShareState.api.saveGIMStorage(ItemShareState.groupId, ItemShareState.tempStorage, onSuccess, onFailure);
	}
}