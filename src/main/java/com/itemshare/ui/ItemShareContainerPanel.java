package com.itemshare.ui;

import com.itemshare.model.ItemShareContainer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.runelite.client.game.ItemManager;

public class ItemShareContainerPanel extends JPanel
{
	private final ItemShareListPanel list;
	private final JTextPane noItemsMessage;

	protected ItemShareContainerPanel(String containerName)
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		noItemsMessage = new JTextPane();
		noItemsMessage.setText("No " + containerName + " items are saved \n\n The player needs to make a change to their " + containerName + " to update this list");
		noItemsMessage.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

		setMessageSettings(noItemsMessage);
		setCenterStyle(noItemsMessage);

		list = new ItemShareListPanel();
		list.setVisible(false);
		noItemsMessage.setVisible(true);

		add(list);
		add(noItemsMessage);
	}

	private void setMessageSettings(JTextPane actionMessage)
	{
		actionMessage.setOpaque(false);
		actionMessage.setEditable(false);
		actionMessage.setFocusable(false);
	}

	private void setCenterStyle(JTextPane textPane)
	{
		StyledDocument style = textPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		style.setParagraphAttributes(0, style.getLength(), center, false);
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
