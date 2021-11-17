package com.itemshare.ui;

import com.itemshare.model.ItemSharePlayer;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.runelite.client.game.ItemManager;

public class ItemShareItemContainerTabPanel extends JPanel
{
	ItemSharePlayer player;

	ItemShareContainerPanel equipment;
	ItemShareContainerPanel inventory;
	ItemShareContainerPanel bank;

	protected ItemShareItemContainerTabPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));


		equipment = new ItemShareContainerPanel("equipment");
		inventory = new ItemShareContainerPanel("inventory");
		bank = new ItemShareContainerPanel("bank");

		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Equipment", equipment);
		tabs.addTab("Inventory", inventory);
		tabs.addTab("Bank", bank);
		tabs.setAlignmentX(CENTER_ALIGNMENT);

		add(tabs, BorderLayout.NORTH);
	}

	public void update(ItemManager itemManager, ItemSharePlayer player)
	{
		this.player = player;
		update(itemManager);
		repaint();
	}

	public void update(ItemManager itemManager)
	{
		if (this.player != null)
		{
			equipment.update(itemManager, this.player.getEquipment());
			inventory.update(itemManager, this.player.getInventory());
			bank.update(itemManager, this.player.getBank());
		}

		repaint();
	}
}
