package com.itemshare.service;

import static com.itemshare.constant.ItemShareConstants.SELECT_A_PLAYER;
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

public class ItemSharePlayerService
{
	private static boolean isLoadingPlayer = false;
	private static boolean hasLoadedItems = false;

	public static void loadPlayerData()
	{
		if (StringUtils.isEmpty(ItemShareState.playerName))
		{
			ItemShareState.playerName = getClientPlayerName();
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
		else if (ItemShareState.player != null && !hasLoadedItems)
		{
			try
			{
				hasLoadedItems = true;
				ItemShareContainerService.loadContainer(InventoryID.INVENTORY);
				ItemShareContainerService.loadContainer(InventoryID.EQUIPMENT);
			}
			catch (Exception e)
			{
				hasLoadedItems = false;
				e.printStackTrace();
			}
		}
	}

	private static void addToPlayerNames(String name)
	{
		if (!ItemShareState.playerNames.contains(name))
		{
			ItemShareState.playerNames.add(name);
		}
	}

	public static ItemSharePlayer getUnselectedPlayer()
	{
		return ItemSharePlayer.builder()
			.name(SELECT_A_PLAYER)
			.updatedDate(null)
			.bank(getEmptyItems())
			.inventory(getEmptyItems())
			.equipment(getEmptySlots())
			.build();
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

	private static String getClientPlayerName()
	{
		Player player = ItemShareState.client.getLocalPlayer();
		return player == null ? null : player.getName();
	}
}