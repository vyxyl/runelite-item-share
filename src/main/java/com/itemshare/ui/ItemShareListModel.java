package com.itemshare.ui;

import com.itemshare.model.ItemShareRenderItem;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class ItemShareListModel implements ListModel<ItemShareRenderItem>
{
	private final List<ItemShareRenderItem> items = new ArrayList<>();
	private final List<ListDataListener> listeners = new ArrayList<>();
	private List<ItemShareRenderItem> filteredItems = new ArrayList<>();

	public int getIndex(ItemShareRenderItem item)
	{
		return this.filteredItems.indexOf(item);
	}

	public void setAllItems(List<ItemShareRenderItem> items)
	{
		removeAllItems();
		this.items.addAll(items);
		this.filteredItems.addAll(items);
	}

	public void removeAllItems()
	{
		this.items.clear();
		this.filteredItems.clear();
	}

	public void filterItems(String text)
	{
		if (text == null || text.isEmpty())
		{
			clearFilter();
		}
		else
		{
			applyFilter(text);
		}
	}

	private void applyFilter(String text)
	{
		this.filteredItems = this.items.stream()
			.filter(item -> isMatchingItem(text, item))
			.collect(Collectors.toList());
	}

	private boolean isMatchingItem(String text, ItemShareRenderItem item)
	{
		return item.getItem().getName().toLowerCase().contains(text.toLowerCase());
	}

	private void clearFilter()
	{
		this.filteredItems = this.items;
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
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l)
	{
		listeners.remove(l);
	}
}
