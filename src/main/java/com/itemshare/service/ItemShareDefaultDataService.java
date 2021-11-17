package com.itemshare.service;

import com.itemshare.model.ItemShareContainer;
import com.itemshare.model.ItemSharePlayer;
import java.util.ArrayList;

public class ItemShareDefaultDataService
{
	public static ItemSharePlayer getDefaultPlayerData(String playerName)
	{
		return ItemSharePlayer.builder()
			.userName(playerName)
			.bank(getDefaultItemContainer())
			.equipment(getDefaultItemContainer())
			.inventory(getDefaultItemContainer())
			.build();
	}

	private static ItemShareContainer getDefaultItemContainer()
	{
		return ItemShareContainer.builder()
			.items(new ArrayList<>())
			.build();
	}
}
