package com.itemshare.service;

import com.itemshare.state.ItemShareState;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;

public class ItemShareContainerService
{
	public static void load(ItemContainerChanged event)
	{
		if (shouldLoad())
		{
			ItemContainer container = event.getItemContainer();

			if (container == ItemShareState.client.getItemContainer(InventoryID.BANK))
			{
				loadBank(container);
			}
			else if (container == ItemShareState.client.getItemContainer(InventoryID.INVENTORY))
			{
				loadInventory(container);
			}
			else if (container == ItemShareState.client.getItemContainer(InventoryID.EQUIPMENT))
			{
				loadEquipment(container);
			}
		}
	}

	private static void loadBank(ItemContainer container)
	{
		ItemShareState.player.setBank(ItemShareItemService.getBankContainer(container));
	}

	private static void loadInventory(ItemContainer container)
	{
		ItemShareState.player.setInventory(ItemShareItemService.getInventoryContainer(container));
	}

	private static void loadEquipment(ItemContainer container)
	{
		ItemShareState.player.setEquipment(ItemShareItemService.getEquipmentContainer(container));
	}

	private static boolean shouldLoad()
	{
		return ItemShareWorldService.isSupportedWorld()
			&& ItemSharePlayerService.isAvailable();
	}
}
