package com.itemshare.service;

import com.itemshare.model.ItemSharePlayer;
import com.itemshare.state.ItemShareState;
import java.util.List;
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
					loadCommon();
				}
			};

			ItemShareState.selfHostDb.connect();
		}
		else
		{
			isSelfHostLoaded = false;
			ItemShareState.selfHostDb.disconnect();
			loadCommon();
		}
	}

	private static void loadCommon()
	{
		ItemShareDBService.loadPlayerNames(names -> {
			ItemShareState.playerNames = names;
			ItemShareUIService.update();
		});
	}

	public static void getPlayer(String playerName, Consumer<ItemSharePlayer> onSuccess, Runnable onFailure)
	{
		boolean isSelfHost = ItemShareConfigService.isSelfHost();

		if (isSelfHost && ItemShareState.selfHostDb.isConnected())
		{
			ItemShareState.selfHostDb.getPlayer(
				ItemShareState.groupId,
				playerName,
				onSuccess,
				onFailure);
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

	public static void loadPlayerNames(Consumer<List<String>> onSuccess)
	{
		boolean isSelfHost = ItemShareConfigService.isSelfHost();

		if (isSelfHost && ItemShareState.selfHostDb.isConnected())
		{
			ItemShareState.selfHostDb.getPlayerNames(
				ItemShareState.groupId,
				onSuccess,
				ItemShareDBService::onGetNamesFail);
		}
		else if (!isSelfHost)
		{
			ItemShareState.dedicatedDB.getPlayerNames(
				ItemShareState.groupId,
				onSuccess,
				ItemShareDBService::onGetNamesFail);
		}
		else
		{
			onSaveFail();
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
		//
	}

	private static void onSaveFail()
	{
		//
	}

	private static void onGetNamesFail()
	{
		//
	}

	private static void onGetPlayerFail()
	{
		//
	}
}
