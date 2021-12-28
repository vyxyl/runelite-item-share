package com.itemshare.service;

import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemShareItems;
import com.itemshare.model.ItemShareSlots;
import com.itemshare.state.ItemShareState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;

public class ItemShareItemService
{
	public static ItemShareItems getBankContainer(ItemContainer container)
	{
		return ItemShareItems.builder()
			.items(getBankItems(container))
			.updatedDate(new Date())
			.build();
	}

	public static ItemShareItems getInventoryContainer(ItemContainer container)
	{
		return ItemShareItems.builder()
			.items(getInventoryItems(container))
			.updatedDate(new Date())
			.build();
	}

	public static ItemShareSlots getEquipmentContainer(ItemContainer container)
	{
		return ItemShareSlots.builder()
			.slots(getEquipmentSlots(container))
			.updatedDate(new Date())
			.build();
	}

	public static ItemShareItem getItem(int id, int quantity)
	{
		return ItemShareItem.builder()
			.id(id)
			.quantity(quantity)
			.name(getItemName(id))
			.build();
	}

	private static ArrayList<ItemShareItem> getBankItems(ItemContainer container)
	{
		return (ArrayList<ItemShareItem>) Arrays.stream(container.getItems())
			.filter(ItemShareItemService::isBankItem)
			.map(ItemShareItemService::getItem)
			.collect(Collectors.toList());
	}

	private static ArrayList<ItemShareItem> getInventoryItems(ItemContainer container)
	{
		return (ArrayList<ItemShareItem>) Arrays.stream(container.getItems())
			.map(ItemShareItemService::getItem)
			.collect(Collectors.toList());
	}

	private static Map<EquipmentInventorySlot, ItemShareItem> getEquipmentSlots(ItemContainer container)
	{
		Map<EquipmentInventorySlot, ItemShareItem> slots = new HashMap<>();

		Arrays.stream(EquipmentInventorySlot.values())
			.forEach(slot -> slots.put(slot, getSlotItem(container, slot)));

		return slots;
	}

	private static ItemShareItem getSlotItem(ItemContainer container, EquipmentInventorySlot slot)
	{
		Item item = container.getItem(slot.getSlotIdx());
		return item == null ? null : getEquipmentItem(item, slot);
	}

	private static ItemShareItem getEquipmentItem(Item item, EquipmentInventorySlot slot)
	{
		return ItemShareItem.builder()
			.id(item.getId())
			.quantity(item.getQuantity())
			.name(getItemName(item.getId()))
			.slot(slot)
			.build();
	}

	private static ItemShareItem getItem(Item item)
	{
		return getItem(item.getId(), item.getQuantity());
	}

	private static boolean isBankItem(Item item)
	{
		return item.getId() == ItemShareState.itemManager.canonicalize(item.getId());
	}

	private static String getItemName(int id)
	{
		return getComposition(id).getName();
	}

	private static ItemComposition getComposition(int id)
	{
		return ItemShareState.itemManager.getItemComposition(id);
	}
}
