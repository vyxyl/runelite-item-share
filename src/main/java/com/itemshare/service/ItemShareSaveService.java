package com.itemshare.service;

import com.itemshare.state.ItemShareState;

public class ItemShareSaveService
{
	public static void save()
	{
		if (shouldSave())
		{
			ItemShareConfigService.saveLocalData(ItemShareState.data);

			if (ItemShareState.db.isConnected() && ItemSharePlayerService.isAvailable())
			{
				ItemShareState.db.savePlayer(ItemShareState.player, () -> {
				}, () -> {
				});
			}
		}
	}

	private static boolean shouldSave()
	{
		return ItemShareWorldService.isSupportedWorld()
			&& ItemSharePlayerService.isAvailable();
	}
}
