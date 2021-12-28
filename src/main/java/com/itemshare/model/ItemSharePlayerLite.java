package com.itemshare.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemSharePlayerLite
{
	private String name;
	private long currentTimeMs;
	private ItemShareItemsLite bank;
	private ItemShareItemsLite inventory;
	private ItemShareItemsLite equipment;
}
