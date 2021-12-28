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
			ItemShareState.playerName = getName();
		}
		else if (ItemShareState.player == null && !isLoading)
		{
			isLoading = true;

			ItemShareDBService.getPlayer(ItemShareState.playerName, lite -> {
				ItemShareState.clientThread.invokeLater(() -> {
					ItemShareState.player = ItemSharePlayerLiteService.toPlayer(lite);
				});
				isLoading = false;
			}, () -> {
				ItemShareState.player = getNewPlayer();
				String name = ItemShareState.player.getName();

				if (!ItemShareState.playerNames.contains(name))
				{
					ItemShareState.playerNames.add(name);
				}

				ItemShareUIService.update();
				isLoading = false;
			});
		}
	}

	private static String getName()
	{
		Player player = ItemShareState.client.getLocalPlayer();
		return player == null ? null : player.getName();
	}

	public static ItemSharePlayer getEmptyPlayer()
	{
		return ItemSharePlayer.builder()
			.name(OPTION_NO_PLAYER)
			.updatedDate(null)
			.bank(ItemShareItems.builder().items(new ArrayList<>()).build())
			.equipment(ItemShareSlots.builder().slots(new HashMap<>()).build())
			.inventory(ItemShareItems.builder().items(new ArrayList<>()).build())
			.build();
	}

	private static ItemSharePlayer getNewPlayer()
	{
		return ItemSharePlayer.builder()
			.name(ItemShareState.playerName)
			.updatedDate(new Date())
			.bank(ItemShareItems.builder().items(new ArrayList<>()).build())
			.equipment(ItemShareSlots.builder().slots(new HashMap<>()).build())
			.inventory(ItemShareItems.builder().items(new ArrayList<>()).build())
			.build();
	}
}
