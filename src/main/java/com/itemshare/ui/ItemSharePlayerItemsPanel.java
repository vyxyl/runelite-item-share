package com.itemshare.ui;

import com.itemshare.model.ItemSharePlayer;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.runelite.client.game.ItemManager;

public class ItemSharePlayerItemsPanel extends JPanel
{
	JTabbedPane tabs = new JTabbedPane();

	ItemShareContainerPanel equipment;
	ItemShareContainerPanel inventory;
	ItemShareContainerPanel bank;

	protected ItemSharePlayerItemsPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		equipment = new ItemShareContainerPanel("equipment");
		inventory = new ItemShareContainerPanel("inventory");
		bank = new ItemShareContainerPanel("bank");

		tabs.addTab("Equipment", equipment);
		tabs.addTab("Inventory", inventory);
		tabs.addTab("Bank", bank);
		tabs.setAlignmentX(CENTER_ALIGNMENT);

		add(tabs, BorderLayout.NORTH);
	}

	public void clearFilters()
	{
		equipment.clearFilter();
		inventory.clearFilter();
		bank.clearFilter();
		repaint();
	}

	public void update(ItemManager itemManager, ItemSharePlayer player)
	{
		equipment.update(itemManager, player.getEquipment());
		inventory.update(itemManager, player.getInventory());
		bank.update(itemManager, player.getBank());
		repaint();
	}
}
