package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;

public class ItemShareNavigationPanel extends JPanel
{
	ItemSharePlayer player;
	private final JLabel lastUpdated = new JLabel();
	private final ItemSharePlayerDropdownPanel dropdown = new ItemSharePlayerDropdownPanel();
	private final ItemShareItemContainerTabsPanel tabs = new ItemShareItemContainerTabsPanel();

	protected ItemShareNavigationPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel lastUpdatedPanel = new JPanel();
		lastUpdatedPanel.setLayout(new BoxLayout(lastUpdatedPanel, BoxLayout.LINE_AXIS));
		lastUpdatedPanel.add(lastUpdated);
		lastUpdatedPanel.add(Box.createHorizontalBox());

		add(lastUpdatedPanel);
		add(dropdown);
		add(tabs);
	}

	public void update(ItemManager itemManager, ItemShareData data)
	{
		updateData(itemManager, data);

		lastUpdated.setText(getUpdatedDateString());

		dropdown.update(data);
		dropdown.setItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				updateData(itemManager, data);
			}
		});

		repaint();
	}

	private String getUpdatedDateString()
	{
		if (player == null || player.getUpdatedDate() == null)
		{
			return "Last Updated: N/A";
		}
		else
		{
			String date = new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss z").format(player.getUpdatedDate());
			return "Last Updated: " + date;
		}
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
