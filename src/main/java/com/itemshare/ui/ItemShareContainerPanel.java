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
	private final JTextPane noItemsMessage;
	private final ItemShareListPanel list = new ItemShareListPanel();

	protected ItemShareContainerPanel(String containerName)
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		noItemsMessage = new JTextPane();
		noItemsMessage.setText("The player needs to change their " + containerName + " to update this list");
		noItemsMessage.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 10));

		setMessageSettings(noItemsMessage);
		setCenterStyle(noItemsMessage);

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
		removeAll();

		if (container.getItems().isEmpty())
		{
			add(noItemsMessage);
		}
		else
		{
			list.update(itemManager, container);
			add(list);
		}

		repaint();
	}
}
