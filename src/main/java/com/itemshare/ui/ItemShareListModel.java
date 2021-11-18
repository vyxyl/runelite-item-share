package com.itemshare.ui;

import com.itemshare.model.ItemShareRenderItem;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import org.apache.commons.lang3.StringUtils;

public class ItemShareListModel implements ListModel<ItemShareRenderItem>
{
	private final List<ListDataListener> listeners = new ArrayList<>();

	private final List<ItemShareRenderItem> items = new ArrayList<>();
	private List<ItemShareRenderItem> filteredItems = new ArrayList<>();

	public int getIndex(ItemShareRenderItem item)
	{
		return filteredItems.indexOf(item);
	}

	public void replaceAll(List<ItemShareRenderItem> itemsToReplace)
	{
		removeAll();
		addAll(itemsToReplace);
	}

	public void addAll(List<ItemShareRenderItem> itemsToAdd)
	{
		items.addAll(itemsToAdd);
		filteredItems.addAll(itemsToAdd);
	}

	public void removeAll()
	{
		items.clear();
		filteredItems.clear();
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

	private void applyFilter(String text)
	{
		filteredItems = items.stream()
			.filter(item -> isMatchingItem(text, item))
			.collect(Collectors.toList());
	}

	private boolean isMatchingItem(String text, ItemShareRenderItem item)
	{
		return item.getItem().getName().toLowerCase().contains(text.toLowerCase());
	}

	private void clearFilter()
	{
		filteredItems.clear();
		filteredItems.addAll(this.items);
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
