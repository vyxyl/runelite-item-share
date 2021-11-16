package com.itemshare.ui;

import com.itemshare.model.ItemShareRenderItem;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class ItemShareListModel implements ListModel<ItemShareRenderItem>
{
	private final List<ItemShareRenderItem> items = new ArrayList<>();
	private final List<ListDataListener> listeners = new ArrayList<>();

	public void addItems(List<ItemShareRenderItem> items)
	{
		this.items.addAll(items);
	}

	@Override
	public int getSize()
	{
		return items.size();
	}

	@Override
	public ItemShareRenderItem getElementAt(int index)
	{
		return items.get(index);
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
