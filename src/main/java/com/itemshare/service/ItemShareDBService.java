package com.itemshare.service;

import com.itemshare.model.ItemSharePlayerLite;
import com.itemshare.state.ItemShareState;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemShareDBService
{
	public static void load()
	{
		loadPlayerNames();
	}

	public static void getPlayer(String playerName, Consumer<ItemSharePlayerLite> onSuccess, Runnable onFailure)
	{
		ItemShareState.db.getPlayer(
			ItemShareState.groupId,
			playerName,
			onSuccess,
			onFailure);
	}

	public static void loadPlayerNames()
	{
		loadPlayerNames(() -> {
		});
	}

	public static void loadPlayerNames(Runnable runnable)
	{
		Consumer<List<String>> loadNames = names -> {
			ItemShareState.playerNames = names;
			ItemShareUIService.update();
			runnable.run();
		};

		ItemShareState.db.getPlayerNames(
			ItemShareState.groupId,
			loadNames,
			ItemShareDBService::onGetNamesFail);
	}

	public static void save()
	{
		if (isSavingAllowed())
		{
			ItemShareState.db.savePlayer(
				ItemShareState.groupId,
				ItemShareState.player,
				ItemShareDBService::onSaveSuccess,
				ItemShareDBService::onSaveFail);
		}
	}

	private static boolean isSavingAllowed()
	{
		return ItemShareWorldService.isSupportedWorld()
			&& ItemSharePlayerService.isAvailable();
	}

	private static void onSaveSuccess()
	{
		ItemShareUIService.update();
	}

	private static void onSaveFail()
	{
		//
	}

	private static void onGetNamesFail()
	{
		ItemShareState.playerNames = new ArrayList<>();
	}
}
