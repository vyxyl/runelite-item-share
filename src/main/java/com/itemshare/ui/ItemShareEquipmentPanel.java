package com.itemshare.ui;

import com.itemshare.model.ItemSharePlayer;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;

public class ItemShareEquipmentPanel extends JPanel
{
	protected ItemShareEquipmentPanel()
	{
		super(false);
		setLayout(new BorderLayout(0, 8));
		setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
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
