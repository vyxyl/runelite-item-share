package com.itemshare.ui;

import com.itemshare.model.ItemSharePlayer;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;

public class ItemShareEquipmentPanel extends JPanel
{
	private final ItemShareList list = new ItemShareList();

	protected ItemShareEquipmentPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	public void update(ItemManager itemManager, ItemSharePlayer data)
	{
		removeAll();
		add(list);

		list.update(itemManager, data.getEquipment());

		repaint();
	}
}
