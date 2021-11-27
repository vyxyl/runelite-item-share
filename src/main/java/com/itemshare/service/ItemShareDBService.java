package com.itemshare.service;

import com.itemshare.state.ItemShareState;

public class ItemShareDBService
{
	private static boolean connected;

	public static void connect()
	{
		connected = false;
		ItemShareState.db.connect(ItemShareDBService::onSuccess, ItemShareDBService::onFailure);
	}

	private static void onSuccess()
	{
		if (!connected)
		{
			connected = true;
			ItemShareSyncService.start();
		}
	}

	private static void onFailure()
	{
		connected = false;
		ItemShareUIService.update();
	}
}
