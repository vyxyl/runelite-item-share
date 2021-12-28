package com.itemshare.service;

import com.itemshare.model.ItemSharePlayer;
import com.itemshare.model.ItemSharePlayerLite;
import com.itemshare.state.ItemShareState;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class ItemShareDBService
{
	private static boolean isSelfHostLoaded = false;

	public static void load()
	{
		if (ItemShareConfigService.isSelfHost())
		{
			ItemShareState.onSelfHostSuccess = () -> {
				if (!isSelfHostLoaded)
				{
					isSelfHostLoaded = true;
					loadPlayerNames();
				}
			};

			ItemShareState.selfHostDb.connect();
		}
		else
		{
			isSelfHostLoaded = false;
			ItemShareState.selfHostDb.disconnect();
			loadPlayerNames();
		}
	}

	public static void getPlayer(String playerName, Consumer<ItemSharePlayerLite> onSuccess, Runnable onFailure)
	{
		boolean isSelfHost = ItemShareConfigService.isSelfHost();

		if (isSelfHost && ItemShareState.selfHostDb.isConnected())
		{
//			ItemShareState.selfHostDb.getPlayer(
//				ItemShareState.groupId,
//				playerName,
//				onSuccess,
//				onFailure);
		}
		else if (!isSelfHost)
		{
			ItemShareState.dedicatedDB.getPlayer(
				ItemShareState.groupId,
				playerName,
				onSuccess,
				onFailure);
		}
		else
		{
			onFailure.run();
		}
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

		boolean isSelfHost = ItemShareConfigService.isSelfHost();

		if (isSelfHost && ItemShareState.selfHostDb.isConnected())
		{
			ItemShareState.selfHostDb.getPlayerNames(
				ItemShareState.groupId,
				loadNames,
				ItemShareDBService::onGetNamesFail);
		}
		else if (!isSelfHost)
		{
			ItemShareState.dedicatedDB.getPlayerNames(
				ItemShareState.groupId,
				loadNames,
				ItemShareDBService::onGetNamesFail);
		}
		else
		{
			onGetNamesFail();
		}
	}

	public static void save()
	{
		if (isSavingAllowed())
		{
			boolean isSelfHost = ItemShareConfigService.isSelfHost();

			if (isSelfHost && ItemShareState.selfHostDb.isConnected())
			{
				ItemShareState.selfHostDb.savePlayer(
					ItemShareState.groupId,
					ItemShareState.player,
					ItemShareDBService::onSaveSuccess,
					ItemShareDBService::onSaveFail);
			}
			else if (!isSelfHost)
			{
				ItemShareState.dedicatedDB.savePlayer(
					ItemShareState.groupId,
					ItemShareState.player,
					ItemShareDBService::onSaveSuccess,
					ItemShareDBService::onSaveFail);
			}
			else
			{
				onSaveFail();
			}
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
