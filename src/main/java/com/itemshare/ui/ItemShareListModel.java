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
	private List<ItemShareRenderItem> searchedItems = new ArrayList<>();

	public void addItems(List<ItemShareRenderItem> items)
	{
		this.items.addAll(items);
		this.searchedItems.addAll(items);
	}

	public void searchItem(String text)
	{
		if (text == null || text.isEmpty())
		{
			clearSearch();
		}
		else
		{
			this.searchedItems = this.items.stream()
				.filter(item -> isMatchingItem(text, item))
				.collect(Collectors.toList());
		}
	}

	private boolean isMatchingItem(String text, ItemShareRenderItem item)
	{
		return item.getItem().getName().toLowerCase().contains(text.toLowerCase());
	}

	public void clearSearch()
	{
		this.searchedItems = this.items;
	}

	@Override
	public int getSize()
	{
		return searchedItems.size();
	}

	@Override
	public ItemShareRenderItem getElementAt(int index)
	{
		return searchedItems.get(index);
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
