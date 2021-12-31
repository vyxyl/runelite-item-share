package com.itemshare.service;

import com.itemshare.state.ItemShareState;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;

public class ItemShareContainerService
{
	public static void loadContainer(ItemContainerChanged event)
	{
		ItemContainer container = event.getItemContainer();
		loadContainer(container);
	}

	public static void loadContainer(InventoryID inventoryId)
	{
		ItemContainer container = ItemShareState.client.getItemContainer(inventoryId);
		loadContainer(container);
	}

	private static void loadContainer(ItemContainer container)
	{
		if (isLoadingAllowed())
		{
			if (container.getId() == InventoryID.BANK.getId())
			{
				loadBank(container);
			}
			else if (container.getId() == InventoryID.INVENTORY.getId())
			{
				loadInventory(container);
			}
			else if (container.getId() == InventoryID.EQUIPMENT.getId())
			{
				loadEquipment(container);
			}
		}
	}

	private static boolean isLoadingAllowed()
	{
		boolean isPlayerAvailable = ItemShareState.player != null;
		boolean isSupportedWorld = ItemShareSupportedService.isSupportedWorld();

		return isPlayerAvailable && isSupportedWorld;
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
}