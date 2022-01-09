package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.SELECT_A_PLAYER;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import org.apache.commons.lang3.StringUtils;

public class ItemSharePlayerDropdownModel implements ComboBoxModel<String>
{
	private final List<String> names;

	private String selected;

	ItemSharePlayerDropdownModel()
	{
		selected = SELECT_A_PLAYER;

		names = new ArrayList<>();
		names.add(SELECT_A_PLAYER);
	}

	public void setNames(List<String> names)
	{
		this.names.clear();
		this.names.add(SELECT_A_PLAYER);
		this.names.addAll(names);
	}

	@Override
	public int getSize()
	{
		return names.size();
	}

	@Override
	public String getElementAt(int index)
	{
		return names.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l)
	{

	}

	@Override
	public void removeListDataListener(ListDataListener l)
	{

	}

	@Override
	public void setSelectedItem(Object item)
	{
		if (item instanceof String)
		{
			this.selected = (String) item;
		}
		else
		{
			this.selected = SELECT_A_PLAYER;
		}
	}

	@Override
	public String getSelectedItem()
	{
		if (Objects.equals(selected, SELECT_A_PLAYER))
		{
			return SELECT_A_PLAYER;
		}
		else
		{
			return names.stream()
				.filter(option -> StringUtils.equals(option, selected))
				.findFirst()
				.orElse(SELECT_A_PLAYER);
		}
	}
}