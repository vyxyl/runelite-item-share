package com.itemshare.service;

import com.itemshare.model.ItemShareContainer;
import com.itemshare.model.ItemShareItem;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class ItemShareDataService
{
	@Inject
	private ItemManager itemManager;

	@Inject
	private Client client;

	public ItemShareContainer getItemContainer(ItemContainer container)
	{
		return ItemShareContainer.builder()
			.items(getItems(container))
			.updatedDate(new Date())
			.build();
	}

	private ArrayList<ItemShareItem> getItems(ItemContainer container)
	{
		return (ArrayList<ItemShareItem>) Arrays.stream(container.getItems())
			.filter(this::isRealItem)
			.map(item ->
				ItemShareItem.builder()
					.id(item.getId())
					.quantity(item.getQuantity())
					.name(getItemName(item))
					.build()
			).collect(Collectors.toList());
	}

	private boolean isRealItem(Item item)
	{
		return item.getId() == itemManager.canonicalize(item.getId());
	}

	private String getItemName(Item item)
	{
		ItemComposition composition = itemManager.getItemComposition(item.getId());
		return composition.getName();
	}
}
