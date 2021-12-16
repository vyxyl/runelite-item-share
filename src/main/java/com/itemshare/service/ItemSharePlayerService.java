package com.itemshare.service;

import com.itemshare.model.ItemShareItems;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.model.ItemShareSlots;
import com.itemshare.state.ItemShareState;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import net.runelite.api.Player;
import org.apache.commons.lang3.StringUtils;

public class ItemSharePlayerService
{
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
		else if (ItemShareState.player == null)
		{
			ItemShareState.player = getPlayer();
		}
	}

	private static String getName()
	{
		Player player = ItemShareState.client.getLocalPlayer();
		return player == null ? null : player.getName();
	}

	private static ItemSharePlayer getPlayer()
	{
		ItemSharePlayer player = findPlayer().orElseGet(ItemSharePlayerService::getNewPlayer);
		player.setGroupId(ItemShareGroupIdService.loadExistingId());
		return getClone(player);
	}

	private static Optional<ItemSharePlayer> findPlayer()
	{
		return ItemShareState.data.getPlayers().stream()
			.filter(player -> StringUtils.equals(player.getName(), ItemShareState.playerName))
			.findFirst();
	}

	private static ItemSharePlayer getClone(ItemSharePlayer player)
	{
		return player.toBuilder()
			.name(player.getName())
			.groupId(player.getGroupId())
			.bank(player.getBank().toBuilder().build())
			.equipment(player.getEquipment().toBuilder().build())
			.inventory(player.getInventory().toBuilder().build())
			.build();
	}

	private static ItemSharePlayer getNewPlayer()
	{
		return ItemSharePlayer.builder()
			.groupId(ItemShareGroupIdService.loadExistingId())
			.name(ItemShareState.playerName)
			.updatedDate(new Date())
			.bank(ItemShareItems.builder().items(new ArrayList<>()).build())
			.equipment(ItemShareSlots.builder().slots(new HashMap<>()).build())
			.inventory(ItemShareItems.builder().items(new ArrayList<>()).build())
			.build();
	}
}
