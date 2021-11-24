package com.itemshare.ui;

import com.itemshare.model.ItemSharePlayer;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.runelite.client.game.ItemManager;

public class ItemSharePlayerPanel extends JPanel
{
	JTabbedPane tabs = new JTabbedPane();

	ItemShareEquipmentPanel equipment;
	ItemShareInventoryPanel inventory;
	ItemShareItemListPanel bank;

	protected ItemSharePlayerPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		equipment = new ItemShareEquipmentPanel();
		inventory = new ItemShareInventoryPanel();
		bank = new ItemShareItemListPanel();

		tabs.addTab("Equipment", equipment);
		tabs.addTab("Inventory", inventory);
		tabs.addTab("Bank", bank);
		tabs.setAlignmentX(CENTER_ALIGNMENT);

		add(tabs, BorderLayout.NORTH);
	}

	public void update(ItemManager itemManager, ItemSharePlayer player)
	{
		equipment.update(itemManager, player);
		inventory.update(itemManager, player);
		bank.update(itemManager, player.getBank());
	}
}
