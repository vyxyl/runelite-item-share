package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.OPTION_NO_PLAYER;
import com.itemshare.model.ItemShareItems;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.model.ItemShareSlots;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import org.apache.commons.lang3.StringUtils;

public class ItemSharePlayerDropdownModel implements ComboBoxModel<ItemSharePlayer>
{
	private final List<ItemSharePlayer> items;
	private final ItemSharePlayer unselected;

	private ItemSharePlayer selected;

	ItemSharePlayerDropdownModel()
	{
		unselected = ItemSharePlayer.builder()
			.name(OPTION_NO_PLAYER)
			.bank(getEmptyList())
			.equipment(getEmptySlots())
			.inventory(getEmptyList())
			.build();

		selected = unselected.toBuilder().build();

		items = new ArrayList<>();
		items.add(unselected);
	}

	public void setItems(List<ItemSharePlayer> players)
	{
		if (!items.containsAll(players))
		{
			items.clear();
			items.add(unselected);
			items.addAll(players);
		}
	}

	@Override
	public int getSize()
	{
		return items.size();
	}

	@Override
	public ItemSharePlayer getElementAt(int index)
	{
		return items.get(index);
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
		if (item instanceof ItemSharePlayer)
		{
			this.selected = (ItemSharePlayer) item;
		}
		else
		{
			this.selected = null;
		}
	}

	@Override
	public ItemSharePlayer getSelectedItem()
	{
		if (selected == null)
		{
			return unselected;
		}
		else
		{
			return items.stream()
				.filter(option -> StringUtils.equals(option.getName(), selected.getName()))
				.findFirst()
				.orElse(unselected);
		}
	}

	private ItemShareItems getEmptyList()
	{
		return ItemShareItems.builder().items(new ArrayList<>()).build();
	}

	private ItemShareSlots getEmptySlots()
	{
		return ItemShareSlots.builder().slots(new HashMap<>()).build();
	}
}
