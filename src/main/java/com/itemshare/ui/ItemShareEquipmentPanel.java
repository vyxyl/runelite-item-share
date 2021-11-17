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
		add(list);
	}

	public void clear()
	{
		list.clear();
		repaint();
	}

	public void update(ItemManager itemManager, ItemSharePlayer player)
	{
		list.update(itemManager, player.getEquipment());

		repaint();
	}
}
