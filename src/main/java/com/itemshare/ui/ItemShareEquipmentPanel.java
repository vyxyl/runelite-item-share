package com.itemshare.ui;

import com.itemshare.model.ItemSharePlayer;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;

public class ItemShareEquipmentPanel extends JPanel
{
	protected ItemShareEquipmentPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	public void reset()
	{
		removeAll();
		repaint();
	}

	public void update(ItemManager itemManager, ItemSharePlayer data)
	{
		ItemShareList list = new ItemShareList();
		removeAll();
		add(list);
		repaint();

		list.update(itemManager, data.getEquipment());
	}
}
