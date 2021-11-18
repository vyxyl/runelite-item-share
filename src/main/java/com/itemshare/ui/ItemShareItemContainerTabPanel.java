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

	JTabbedPane tabs = new JTabbedPane();

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

		repaintAll();
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

		tabs.repaint();
		repaint();
	}

	public void repaintAll()
	{
		equipment.repaint();
		inventory.repaint();
		bank.repaint();
		tabs.repaint();

		repaint();
	}

}
