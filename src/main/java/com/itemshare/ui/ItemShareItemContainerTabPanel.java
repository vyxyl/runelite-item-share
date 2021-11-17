package com.itemshare.ui;

import com.itemshare.model.ItemSharePlayer;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;

public class ItemShareItemContainerTabPanel extends JPanel
{
	private final JPanel panel = new JPanel();
	private final MaterialTabGroup tabGroup = new MaterialTabGroup(panel);

	private final ItemShareContainerPanel equipment;
	private final ItemShareContainerPanel inventory;
	private final ItemShareContainerPanel bank;

	private final MaterialTab equipmentTab;
	private final MaterialTab inventoryTab;
	private final MaterialTab bankTab;

	private ItemSharePlayer player;

	protected ItemShareItemContainerTabPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		equipment = new ItemShareContainerPanel("equipment");
		inventory = new ItemShareContainerPanel("inventory");
		bank = new ItemShareContainerPanel("bank");

		equipmentTab = new MaterialTab("Equipment", tabGroup, equipment);
		inventoryTab = new MaterialTab("Inventory", tabGroup, inventory);
		bankTab = new MaterialTab("Bank", tabGroup, bank);

		tabGroup.addTab(equipmentTab);
		tabGroup.addTab(inventoryTab);
		tabGroup.addTab(bankTab);

		tabGroup.select(equipmentTab);

		add(tabGroup, BorderLayout.NORTH);
		add(panel, BorderLayout.NORTH);
	}

	public void update(ItemManager itemManager, ItemSharePlayer player)
	{
		this.player = player;
		tabGroup.select(equipmentTab);

		update(itemManager);
	}

	public void update(ItemManager itemManager)
	{
		if (this.player != null)
		{
			equipment.update(itemManager, this.player.getEquipment());
			inventory.update(itemManager, this.player.getInventory());
			bank.update(itemManager, this.player.getBank());
		}

		equipmentTab.repaint();
		inventoryTab.repaint();
		bankTab.repaint();

		tabGroup.repaint();

		repaint();
	}
}
