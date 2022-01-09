package com.itemshare.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.runelite.api.EquipmentInventorySlot;

@Getter
@Setter
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemShareSlots
{
	private Map<EquipmentInventorySlot, ItemShareItem> slots;
}