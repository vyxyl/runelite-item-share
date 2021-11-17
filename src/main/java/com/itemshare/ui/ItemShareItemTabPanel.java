package com.itemshare.ui;

import com.itemshare.model.ItemSharePlayer;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;

public class ItemShareItemTabPanel extends JPanel
{
	private final JPanel panel = new JPanel();
	private final MaterialTabGroup tabGroup = new MaterialTabGroup(panel);
	private final ItemShareEquipmentPanel equipment = new ItemShareEquipmentPanel();
	private final ItemShareInventoryPanel inventory = new ItemShareInventoryPanel();
	private final ItemShareBankPanel bank = new ItemShareBankPanel();

	protected ItemShareItemTabPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		MaterialTab equipmentTab = new MaterialTab("Equipment", tabGroup, equipment);
		MaterialTab inventoryTab = new MaterialTab("Inventory", tabGroup, inventory);
		MaterialTab bankTab = new MaterialTab("Bank", tabGroup, bank);

		tabGroup.addTab(equipmentTab);
		tabGroup.addTab(inventoryTab);
		tabGroup.addTab(bankTab);

		tabGroup.select(equipmentTab);

		add(tabGroup, BorderLayout.NORTH);
		add(panel, BorderLayout.NORTH);
	}

	public void clear()
	{
		equipment.clear();
		inventory.clear();
		bank.clear();

		repaint();
	}

	public void update(ItemManager itemManager, ItemSharePlayer player)
	{
		equipment.update(itemManager, player);
		inventory.update(itemManager, player);
		bank.update(itemManager, player);

		repaint();
	}
}