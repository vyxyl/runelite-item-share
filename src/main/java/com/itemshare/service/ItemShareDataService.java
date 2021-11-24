package com.itemshare.service;

import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemShareItems;
import com.itemshare.model.ItemShareSlots;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.client.game.ItemManager;

public class ItemShareDataService
{
	@Inject
	private ItemManager itemManager;

	@Inject
	private Client client;

	public ItemShareItems getBankContainer(ItemContainer container)
	{
		return ItemShareItems.builder()
			.items(getBankItems(container))
			.updatedDate(new Date())
			.build();
	}

	public ItemShareItems getInventoryContainer(ItemContainer container)
	{
		return ItemShareItems.builder()
			.items(getInventoryItems(container))
			.updatedDate(new Date())
			.build();
	}

	public ItemShareSlots getEquipmentContainer(ItemContainer container)
	{
		return ItemShareSlots.builder()
			.slots(getEquipmentSlots(container))
			.updatedDate(new Date())
			.build();
	}

	private ArrayList<ItemShareItem> getBankItems(ItemContainer container)
	{
		return (ArrayList<ItemShareItem>) Arrays.stream(container.getItems())
			.filter(this::isBankItem)
			.map(this::getItem)
			.collect(Collectors.toList());
	}

	private ArrayList<ItemShareItem> getInventoryItems(ItemContainer container)
	{
		return (ArrayList<ItemShareItem>) Arrays.stream(container.getItems())
			.map(this::getItem)
			.collect(Collectors.toList());
	}

	private Map<EquipmentInventorySlot, ItemShareItem> getEquipmentSlots(ItemContainer container)
	{
		Map<EquipmentInventorySlot, ItemShareItem> slots = new HashMap<>();

		Arrays.stream(EquipmentInventorySlot.values())
			.forEach(slot -> slots.put(slot, getSlotItem(container, slot)));

		return slots;
	}

	private ItemShareItem getSlotItem(ItemContainer container, EquipmentInventorySlot slot)
	{
		Item item = container.getItem(slot.getSlotIdx());

		if (item == null)
		{
			return null;
		}
		else
		{
			return getEquipmentItem(item, slot);
		}
	}

	private ItemShareItem getEquipmentItem(Item item, EquipmentInventorySlot slot)
	{
		return ItemShareItem.builder()
			.id(item.getId())
			.quantity(item.getQuantity())
			.name(getItemName(item))
			.slot(slot)
			.build();
	}

	private ItemShareItem getItem(Item item)
	{
		return ItemShareItem.builder()
			.id(item.getId())
			.quantity(item.getQuantity())
			.name(getItemName(item))
			.build();
	}

	private boolean isBankItem(Item item)
	{
		return item.getId() == itemManager.canonicalize(item.getId());
	}

	private String getItemName(Item item)
	{
		return getComposition(item).getName();
	}

	private ItemComposition getComposition(Item item)
	{
		return itemManager.getItemComposition(item.getId());
	}

	private ArrayList<ItemShareItem> mergeById(List<ItemShareItem> items)
	{
		Map<Integer, ItemShareItem> map = new HashMap<>();

		items.forEach(item -> map.merge(item.getId(), item.toBuilder().build(), ItemShareItem::merge));

		return new ArrayList<>(map.values());
	}
}
