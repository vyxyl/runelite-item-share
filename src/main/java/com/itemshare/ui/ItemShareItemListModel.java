package com.itemshare.ui;

import com.itemshare.model.ItemShareRenderItem;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import org.apache.commons.lang3.StringUtils;

public class ItemShareItemListModel implements ListModel<ItemShareRenderItem>
{
	private final List<ItemShareRenderItem> unfilteredItems = new ArrayList<>();
	private final List<ItemShareRenderItem> filteredItems = new ArrayList<>();

	public int getIndex(ItemShareRenderItem item)
	{
		return filteredItems.indexOf(item);
	}

	public void setItems(List<ItemShareRenderItem> items)
	{
		removeAllItems();

		unfilteredItems.addAll(items);
		filteredItems.addAll(unfilteredItems);
	}

	public void removeAllItems()
	{
		unfilteredItems.clear();
		filteredItems.clear();
	}

	public List<ItemShareRenderItem> getUnfilteredItems()
	{
		return unfilteredItems;
	}

	public List<ItemShareRenderItem> getFilteredItems()
	{
		return filteredItems;
	}

	public void filterItems(String text)
	{
		if (StringUtils.isEmpty(text))
		{
			clearFilter();
		}
		else
		{
			applyFilter(text);
		}
	}

	public void clearFilter()
	{
		filteredItems.clear();
		filteredItems.addAll(unfilteredItems);
	}

	private void applyFilter(String text)
	{
		filteredItems.clear();
		filteredItems.addAll(unfilteredItems.stream()
			.filter(item -> isMatchingItem(text, item))
			.collect(Collectors.toList()));
	}

	private boolean isMatchingItem(String text, ItemShareRenderItem item)
	{
		return item.getItem().getName().toLowerCase().contains(text.toLowerCase());
	}

	@Override
	public int getSize()
	{
		return filteredItems.size();
	}

	@Override
	public ItemShareRenderItem getElementAt(int index)
	{
		return filteredItems.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l)
	{
	}

	@Override
	public void removeListDataListener(ListDataListener l)
	{
	}
}
