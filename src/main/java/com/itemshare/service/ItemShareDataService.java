package com.itemshare.service;

import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemShareItems;
import com.itemshare.model.ItemShareItemsLite;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.model.ItemSharePlayerLite;
import com.itemshare.model.ItemShareSlots;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import net.runelite.api.EquipmentInventorySlot;
import org.apache.commons.lang3.ArrayUtils;

public class ItemShareDataService
{
	private static final EquipmentInventorySlot[] SLOTS = EquipmentInventorySlot.values();

	public static ItemSharePlayer toPlayer(ItemSharePlayerLite lite)
	{
		String name = lite.getName();
		Date updatedDate = ItemShareDataService.toDate(lite.getCurrentTimeMs());
		ItemShareItems bank = ItemShareDataService.toItems(lite.getBank());
		ItemShareItems inventory = ItemShareDataService.toItems(lite.getInventory());
		ItemShareSlots equipment = ItemShareDataService.toSlots(lite.getEquipment());

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
			.equipment(toSlotsLite(equipment))
			.build();
	}

	public static ItemShareItemsLite toItemsLite(ItemShareItems items)
	{
		Date updateDate = items.getUpdatedDate();
		List<ItemShareItem> itemList = items.getItems();
		return toItemsLite(updateDate, itemList);
	}

	public static ItemShareItemsLite toSlotsLite(ItemShareSlots slots)
	{
		Date updateDate = slots.getUpdatedDate();
		List<ItemShareItem> values = new ArrayList<>(slots.getSlots().values());
		return toSlotsLite(updateDate, values);
	}

	private static ItemShareItemsLite toItemsLite(Date updateDate, List<ItemShareItem> itemList)
	{
		int[] raw = itemList.isEmpty() ? new int[]{} : toItemsRaw(itemList.stream());
		long ms = updateDate == null ? 0 : updateDate.getTime();
		return ItemShareItemsLite.builder().currentTimeMs(ms).items(raw).build();
	}

	private static ItemShareItemsLite toSlotsLite(Date updateDate, List<ItemShareItem> itemList)
	{
		int[] raw = itemList.isEmpty() ? new int[]{} : toSlotsRaw(itemList.stream());
		long ms = updateDate == null ? 0 : updateDate.getTime();
		return ItemShareItemsLite.builder().currentTimeMs(ms).items(raw).build();
	}

	private static int[] toItemsRaw(Stream<ItemShareItem> stream)
	{
		Integer[] values = stream.filter(Objects::nonNull)
			.flatMap(item -> Stream.of(item.getId(), item.getQuantity()))
			.toArray(Integer[]::new);

		return ArrayUtils.toPrimitive(values);
	}

	private static int[] toSlotsRaw(Stream<ItemShareItem> stream)
	{
		Integer[] values = stream.filter(Objects::nonNull).filter(item -> item.getSlot() != null)
			.flatMap(item -> Stream.of(item.getId(), item.getQuantity(), item.getSlot().getSlotIdx()))
			.toArray(Integer[]::new);

		return ArrayUtils.toPrimitive(values);
	}

	public static ItemShareItems toItems(ItemShareItemsLite lite)
	{
		if (lite == null || lite.getItems().length % 2 != 0)
		{
			return ItemShareItems.builder()
				.items(new ArrayList<>())
				.updatedDate(null)
				.build();
		}

		int[] raw = lite.getItems();
		ArrayList<ItemShareItem> items = new ArrayList<>();

		for (int i = 0; i < raw.length; i += 2)
		{
			int id = raw[i];
			int quantity = raw[i + 1];
			items.add(ItemShareItemService.getItem(id, quantity));
		}

		return ItemShareItems.builder()
			.items(items)
			.updatedDate(toDate(lite.getCurrentTimeMs()))
			.build();
	}

	public static ItemShareSlots toSlots(ItemShareItemsLite lite)
	{
		if (lite == null || lite.getItems().length % 3 != 0)
		{
			return ItemShareSlots.builder()
				.slots(new HashMap<>())
				.updatedDate(null)
				.build();
		}

		int[] raw = lite.getItems();
		Map<EquipmentInventorySlot, ItemShareItem> slots = new HashMap<>();

		for (int i = 0; i < raw.length; i += 3)
		{
			int id = raw[i];
			int quantity = raw[i + 1];
			int slotId = raw[i + 2];

			Optional<EquipmentInventorySlot> matchingSlot = Arrays.stream(SLOTS).filter(s -> s.getSlotIdx() == slotId).findFirst();

			if (matchingSlot.isPresent())
			{
				EquipmentInventorySlot slot = matchingSlot.get();
				ItemShareItem item = ItemShareItemService.getItem(id, quantity);

				item.setSlot(slot);
				slots.put(slot, item);
			}
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
