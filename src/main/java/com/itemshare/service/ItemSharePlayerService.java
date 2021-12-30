package com.itemshare.service;

import static com.itemshare.constant.ItemShareConstants.OPTION_NO_PLAYER;
import com.itemshare.model.ItemShareItems;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.model.ItemShareSlots;
import com.itemshare.state.ItemShareState;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import net.runelite.api.Player;
import org.apache.commons.lang3.StringUtils;

public class ItemSharePlayerService
{
	private static boolean isLoading = false;

	public static boolean isAvailable()
	{
		return ItemShareState.player != null
			&& !StringUtils.isEmpty(ItemShareState.player.getName());
	}

	public static void load()
	{
		if (StringUtils.isEmpty(ItemShareState.playerName))
		{
			ItemShareState.playerName = getPlayerName();
		}
		else if (ItemShareState.player == null && !isLoading)
		{
			isLoading = true;

			try
			{
				ItemShareAPIService.getPlayer(ItemShareState.playerName, player -> {
					ItemShareState.player = player;
					loadLoggedInPlayerName();
					ItemShareUIService.update();

					isLoading = false;
				});
			}
			catch (Exception e)
			{
				isLoading = false;
				e.printStackTrace();
			}
		}
	}

	public static void loadLoggedInPlayerName()
	{
		if (!ItemShareState.playerNames.contains(ItemShareState.playerName))
		{
			ItemShareState.playerNames.add(ItemShareState.playerName);
		}
	}

	private static String getPlayerName()
	{
		Player player = ItemShareState.client.getLocalPlayer();
		return player == null ? null : player.getName();
	}

	public static ItemSharePlayer getUnselectedPlayer()
	{
		return ItemSharePlayer.builder()
			.name(OPTION_NO_PLAYER)
			.updatedDate(null)
			.bank(ItemShareItems.builder().items(new ArrayList<>()).build())
			.equipment(ItemShareSlots.builder().slots(new HashMap<>()).build())
			.inventory(ItemShareItems.builder().items(new ArrayList<>()).build())
			.build();
	}

	public static ItemSharePlayer getEmptyPlayer(String name)
	{
		return ItemSharePlayer.builder()
			.name(name)
			.updatedDate(new Date())
			.bank(ItemShareItems.builder().items(new ArrayList<>()).build())
			.equipment(ItemShareSlots.builder().slots(new HashMap<>()).build())
			.inventory(ItemShareItems.builder().items(new ArrayList<>()).build())
			.build();
	}
}
