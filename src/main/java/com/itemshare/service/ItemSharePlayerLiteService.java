package com.itemshare.service;

import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemShareItems;
import com.itemshare.model.ItemShareItemsLite;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.model.ItemSharePlayerLite;
import com.itemshare.model.ItemShareSlots;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import net.runelite.api.EquipmentInventorySlot;
import org.apache.commons.lang3.ArrayUtils;

public class ItemSharePlayerLiteService
{
	private static final EquipmentInventorySlot[] SLOTS = EquipmentInventorySlot.values();

	public static ItemSharePlayer toPlayer(ItemSharePlayerLite lite)
	{
		String name = lite.getName();
		Date updatedDate = ItemSharePlayerLiteService.toDate(lite.getCurrentTimeMs());
		ItemShareItems bank = ItemSharePlayerLiteService.toItems(lite.getBank());
		ItemShareItems inventory = ItemSharePlayerLiteService.toItems(lite.getInventory());
		ItemShareSlots equipment = ItemSharePlayerLiteService.toSlots(lite.getEquipment());

		return ItemSharePlayer.builder()
			.name(name)
			.updatedDate(updatedDate)
			.bank(bank)
			.inventory(inventory)
			.equipment(equipment)
			.build();
	}

	public static ItemSharePlayerLite toPlayerLite(ItemSharePlayer player)
	{
		ItemShareItems bank = player.getBank();
		ItemShareItems inventory = player.getInventory();
		ItemShareSlots equipment = player.getEquipment();
		Date updateDate = player.getUpdatedDate();

		return ItemSharePlayerLite.builder()
			.name(player.getName())
			.currentTimeMs(updateDate == null ? 0 : updateDate.getTime())
			.bank(toItemsLite(bank))
			.inventory(toItemsLite(inventory))
			.equipment(toItemsLite(equipment))
			.build();
	}

	public static ItemShareItemsLite toItemsLite(ItemShareItems items)
	{
		Date updateDate = items.getUpdatedDate();
		List<ItemShareItem> itemList = items.getItems();
		Integer[] raw = itemList.isEmpty() ? new Integer[]{} : toRaw(itemList.stream());

		return ItemShareItemsLite.builder()
			.currentTimeMs(updateDate == null ? 0 : updateDate.getTime())
			.items(ArrayUtils.toPrimitive(raw))
			.build();
	}

	public static ItemShareItemsLite toItemsLite(ItemShareSlots slots)
	{
		Date updateDate = slots.getUpdatedDate();
		Collection<ItemShareItem> items = slots.getSlots().values();
		Integer[] raw = items.isEmpty() ? new Integer[]{} : toRaw(items.stream());

		return ItemShareItemsLite.builder()
			.currentTimeMs(updateDate == null ? 0 : updateDate.getTime())
			.items(ArrayUtils.toPrimitive(raw))
			.build();
	}

	private static Integer[] toRaw(Stream<ItemShareItem> stream)
	{
		return stream.filter(Objects::nonNull)
			.flatMap(item -> Stream.of(item.getId(), item.getQuantity()))
			.toArray(Integer[]::new);
	}

	public static ItemShareItems toItems(ItemShareItemsLite lite)
	{
		int[] raw = lite.getItems();
		ArrayList<ItemShareItem> items = new ArrayList<>();

		for (int i = 0; i < raw.length / 2; i += 2)
		{
			int id = raw[i];
			int quantity = raw[i + 1];

			ItemShareItem item = ItemShareItemService.getItem(id, quantity);
			items.add(item);
		}

		return ItemShareItems.builder()
			.items(items)
			.updatedDate(toDate(lite.getCurrentTimeMs()))
			.build();
	}

	public static ItemShareSlots toSlots(ItemShareItemsLite lite)
	{
		int[] raw = lite.getItems();
		Map<EquipmentInventorySlot, ItemShareItem> slots = new HashMap<>();

		for (int i = 0; i < raw.length / 2; i += 2)
		{
			int id = raw[i];
			int quantity = raw[i + 1];

			ItemShareItem item = ItemShareItemService.getItem(id, quantity);
			slots.put(SLOTS[i / 2], item);
		}

		return ItemShareSlots.builder()
			.slots(slots)
			.updatedDate(toDate(lite.getCurrentTimeMs()))
			.build();
	}

	public static Date toDate(long ms)
	{
		return ms == 0 ? null : new Date(ms);
	}
}
