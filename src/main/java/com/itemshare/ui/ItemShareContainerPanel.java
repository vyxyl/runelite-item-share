package com.itemshare.ui;

import com.itemshare.model.ItemShareContainer;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import net.runelite.client.game.ItemManager;

public class ItemShareContainerPanel extends JPanel
{
	private final ItemShareList list = new ItemShareList();
	private final JTextArea noItemsMessage;

	protected ItemShareContainerPanel(String containerName)
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		noItemsMessage = new JTextArea(2, 20);
		noItemsMessage.setText("No " + containerName + " items are saved. The player needs to make a change to their " + containerName + " to update this list");
		noItemsMessage.setWrapStyleWord(true);
		noItemsMessage.setLineWrap(true);
		noItemsMessage.setOpaque(false);
		noItemsMessage.setEditable(false);
		noItemsMessage.setFocusable(false);

		list.setVisible(false);
		noItemsMessage.setVisible(true);

		add(list);
		add(noItemsMessage);
	}

	public void update(ItemManager itemManager, ItemShareContainer container)
	{
		if (container.getItems().isEmpty())
		{
			list.setVisible(false);
			noItemsMessage.setVisible(true);
		}
		else
		{
			list.update(itemManager, container);

			list.setVisible(true);
			noItemsMessage.setVisible(false);
		}

		repaint();
	}
}
