package com.itemshare.service;

import com.itemshare.model.ItemSharePlayerLite;
import com.itemshare.state.ItemShareState;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemShareAPIService
{
	public static void load()
	{
		loadPlayerNames(() -> {
		});
	}

	public static void getPlayer(String playerName, Consumer<ItemSharePlayerLite> onSuccess, Runnable onFailure)
	{
		ItemShareState.api.getPlayer(
			ItemShareState.groupId,
			playerName,
			onSuccess,
			onFailure);
	}

	public static void loadPlayerNames(Runnable runnable)
	{
		Consumer<List<String>> loadNames = names -> {
			ItemShareState.playerNames = names;
			ItemShareUIService.update();
			runnable.run();
		};

		ItemShareState.api.getPlayerNames(
			ItemShareState.groupId,
			loadNames,
			() -> ItemShareState.playerNames = new ArrayList<>());
	}

	public static void save()
	{
		if (ItemShareSupportedService.isSupported())
		{
			ItemShareState.api.savePlayer(
				ItemShareState.groupId,
				ItemShareState.player,
				ItemShareUIService::update,
				() -> {
				});
		}
	}
}
