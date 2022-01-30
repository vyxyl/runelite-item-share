package com.itemshare.service;

import com.itemshare.model.ItemShareItems;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.model.ItemShareSlots;
import com.itemshare.state.ItemShareState;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import net.runelite.api.InventoryID;
import net.runelite.api.Player;
import org.apache.commons.lang3.StringUtils;

public class ItemShareLoadService
{
	private static boolean isLoadingPlayer = false;
	private static boolean hasLoadedPlayerItems = false;

	public static void loadPlayerData()
	{
		if (StringUtils.isEmpty(ItemShareState.playerName))
		{
			ItemShareState.clientThread.invokeLater(() -> {
				Player player = ItemShareState.client.getLocalPlayer();
				ItemShareState.playerName = player == null ? null : player.getName();
			});
		}
		else if (ItemShareState.player == null && !isLoadingPlayer)
		{
			isLoadingPlayer = true;
			try
			{
				ItemShareAPIService.getPlayer(ItemShareState.playerName, player -> {
					ItemShareState.player = player;
					addToPlayerNames(ItemShareState.playerName);

					isLoadingPlayer = false;
					ItemShareUIService.update();
				});
			}
			catch (Exception e)
			{
				isLoadingPlayer = false;
				e.printStackTrace();
			}
		}
		else if (ItemShareState.player != null && !hasLoadedPlayerItems)
		{
			try
			{
				hasLoadedPlayerItems = true;
				ItemShareContainerService.loadContainer(InventoryID.INVENTORY);
				ItemShareContainerService.loadContainer(InventoryID.EQUIPMENT);
			}
			catch (Exception e)
			{
				hasLoadedPlayerItems = false;
				e.printStackTrace();
			}
		}
	}

	public static void loadGIMStorage()
	{
		ItemShareAPIService.getGIMStorage(gimStorage -> {
			ItemShareState.gimStorage = gimStorage;
			ItemShareUIService.update();
		});
	}

	private static void addToPlayerNames(String name)
	{
		if (!ItemShareState.playerNames.contains(name))
		{
			ItemShareState.playerNames.add(name);
		}
	}

	public static ItemSharePlayer getEmptyPlayer(String name)
	{
		return ItemSharePlayer.builder()
			.name(name)
			.updatedDate(new Date())
			.bank(getEmptyItems())
			.inventory(getEmptyItems())
			.equipment(getEmptySlots())
			.build();
	}

	private static ItemShareSlots getEmptySlots()
	{
		return ItemShareSlots.builder().slots(new HashMap<>()).build();
	}

	private static ItemShareItems getEmptyItems()
	{
		return ItemShareItems.builder().items(new ArrayList<>()).build();
	}
}