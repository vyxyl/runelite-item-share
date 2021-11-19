package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import java.awt.event.ItemEvent;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;

public class ItemShareNavigationPanel extends JPanel
{
	ItemSharePlayer player;
	private final ItemSharePlayerDropdownPanel dropdown = new ItemSharePlayerDropdownPanel();
	private final ItemShareItemContainerTabsPanel tabs = new ItemShareItemContainerTabsPanel();

	protected ItemShareNavigationPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		add(dropdown);
		add(tabs);
	}

	public void update(ItemManager itemManager, ItemShareData data)
	{
		updateData(itemManager, data);

		dropdown.update(data);
		dropdown.setItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				updateData(itemManager, data);
			}
		});
	}

	private void updateData(ItemManager itemManager, ItemShareData data)
	{
		updateTabs(itemManager, data);
		repaint();
	}

	private void updateTabs(ItemManager itemManager, ItemShareData data)
	{
		ItemSharePlayer selectedPlayer = dropdown.getSelectedPlayer(data);

		if (selectedPlayer != player)
		{
			player = selectedPlayer;
			tabs.clearFilters();
		}

		tabs.update(itemManager, selectedPlayer);
	}
}
