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
	private final JLabel updateMessageLabel = new JLabel();
	private final ItemSharePlayerDropdownPanel playerDropdown = new ItemSharePlayerDropdownPanel();
	private final ItemSharePlayerPanel playerPanel = new ItemSharePlayerPanel();

	protected ItemShareNavigationPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel updateMessage = new JPanel();
		updateMessage.setLayout(new BoxLayout(updateMessage, BoxLayout.LINE_AXIS));
		updateMessage.add(updateMessageLabel);

		add(updateMessage);
		add(playerDropdown);
		add(playerPanel);
	}

	public void update(ItemManager itemManager, ItemShareData data)
	{
		updateData(itemManager);

		playerDropdown.update(data);
		playerDropdown.setItemListener(e -> onPlayerSelected(itemManager, e));
	}

	private void onPlayerSelected(ItemManager itemManager, ItemEvent e)
	{
		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			updateData(itemManager);
			updateMessageLabel.setText(getUpdateMessage());
		}
	}

	private String getUpdateMessage()
	{
		if (player.getUpdatedDate() == null)
		{
			return "Last Updated: N/A";
		}
		else
		{
			return new SimpleDateFormat("EEEEE, MMM. dd yyyy, h:mm a z").format(player.getUpdatedDate());
		}
	}

	private void updateData(ItemManager itemManager)
	{
		player = playerDropdown.getSelectedPlayer();
		playerPanel.update(itemManager, player);
	}
}
