package com.itemshare.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.runelite.api.EquipmentInventorySlot;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemShareItem
{
	private int id;
	private String name;
	private int quantity;
	private EquipmentInventorySlot slot;
}
