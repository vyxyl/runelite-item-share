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

	private final ItemShareContainerPanel equipment = new ItemShareContainerPanel("equipment");
	private final ItemShareContainerPanel inventory = new ItemShareContainerPanel("inventory");
	private final ItemShareContainerPanel bank = new ItemShareContainerPanel("bank");

	private final MaterialTab equipmentTab = new MaterialTab("Equipment", tabGroup, equipment);
	private final MaterialTab inventoryTab = new MaterialTab("Inventory", tabGroup, inventory);
	private final MaterialTab bankTab = new MaterialTab("Bank", tabGroup, bank);

	private ItemSharePlayer player;

	protected ItemShareItemTabPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

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
