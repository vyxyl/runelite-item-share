package com.itemshare.service;

import com.itemshare.state.ItemShareState;

public class ItemShareDBService
{
	private static boolean connected;

	public static void connect()
	{
		connected = false;
		ItemShareState.db.setCallbacks(ItemShareDBService::onSuccess, ItemShareDBService::onFailure);
		ItemShareState.db.connect();
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
