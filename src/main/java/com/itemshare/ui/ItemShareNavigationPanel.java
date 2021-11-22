package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;

public class ItemShareNavigationPanel extends JPanel
{
	ItemSharePlayer player;
	private final JLabel lastUpdated = new JLabel();
	private final ItemSharePlayerDropdownPanel playerDropdown = new ItemSharePlayerDropdownPanel();
	private final ItemShareItemPlayerItemsPanel playerItems = new ItemShareItemPlayerItemsPanel();

	protected ItemShareNavigationPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel lastUpdatedWrapper = new JPanel();
		lastUpdatedWrapper.setLayout(new BoxLayout(lastUpdatedWrapper, BoxLayout.LINE_AXIS));
		lastUpdatedWrapper.add(lastUpdated);

		add(lastUpdatedWrapper);
		add(playerDropdown);
		add(playerItems);
	}

	public void update(ItemManager itemManager, ItemShareData data)
	{
		updateData(itemManager, data);

		playerDropdown.update(data);
		playerDropdown.setItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				updateData(itemManager, data);
				lastUpdated.setText(getUpdatedDateString());
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
			return new SimpleDateFormat("EEEEE, MMM. dd yyyy, h:mm a z").format(player.getUpdatedDate());
		}
	}

	private void updateData(ItemManager itemManager, ItemShareData data)
	{
		updateTabs(itemManager, data);
		repaint();
	}

	private void updateTabs(ItemManager itemManager, ItemShareData data)
	{
		ItemSharePlayer selectedPlayer = playerDropdown.getSelectedPlayer(data);

		if (selectedPlayer != player)
		{
			player = selectedPlayer;
			playerItems.clearFilters();
		}

		playerItems.update(itemManager, selectedPlayer);
	}
}
