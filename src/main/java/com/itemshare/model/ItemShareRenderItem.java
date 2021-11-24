package com.itemshare.model;

import javax.swing.ImageIcon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.runelite.client.util.AsyncBufferedImage;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemShareRenderItem
{
	private ItemShareItem item;
	private AsyncBufferedImage icon;

	@Override
	public int hashCode()
	{
		return Integer.parseInt(String.valueOf(this.item.getId()) + String.valueOf(this.item.getQuantity()));
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof ItemShareRenderItem)
		{
			ItemShareRenderItem toCompare = (ItemShareRenderItem) o;
			boolean matchingId = this.item.getId() == toCompare.item.getId();
			boolean matchingQuantity = this.item.getQuantity() == toCompare.item.getQuantity();
			return matchingId && matchingQuantity;
		}
		return false;
	}
}
