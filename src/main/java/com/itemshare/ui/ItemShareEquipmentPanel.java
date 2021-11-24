package com.itemshare.ui;

import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemSharePlayer;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.AsyncBufferedImage;

public class ItemShareEquipmentPanel extends JPanel
{
	JPanel panel = new JPanel(new GridBagLayout());
	Map<EquipmentInventorySlot, JPanel> slotPanels = new HashMap<>();

	public ItemShareEquipmentPanel()
	{
		super(false);

		addSlots();
		add(panel);
	}

	private void addSlots()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(0, 0, 5, 5);

		addSpacer(c, 0, 0);
		addSlot(c, 1, 0, EquipmentInventorySlot.HEAD);
		addSpacer(c, 2, 0);

		addSlot(c, 0, 1, EquipmentInventorySlot.CAPE);
		addSlot(c, 1, 1, EquipmentInventorySlot.AMULET);
		addSlot(c, 2, 1, EquipmentInventorySlot.AMMO);

		addSlot(c, 0, 2, EquipmentInventorySlot.WEAPON);
		addSlot(c, 1, 2, EquipmentInventorySlot.BODY);
		addSlot(c, 2, 2, EquipmentInventorySlot.SHIELD);

		addSpacer(c, 0, 3);
		addSlot(c, 1, 3, EquipmentInventorySlot.LEGS);
		addSpacer(c, 2, 3);

		addSlot(c, 0, 4, EquipmentInventorySlot.GLOVES);
		addSlot(c, 1, 4, EquipmentInventorySlot.BOOTS);
		addSlot(c, 2, 4, EquipmentInventorySlot.RING);
	}

	private void addSlot(GridBagConstraints c, int x, int y, EquipmentInventorySlot slot)
	{
		c.gridx = x;
		c.gridy = y;

		JPanel slotPanel = getBlackSlotPanel();
		slotPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		slotPanels.put(slot, slotPanel);
		panel.add(slotPanel, c);
	}

	private void addSpacer(GridBagConstraints c, int x, int y)
	{
		c.gridx = x;
		c.gridy = y;

		JPanel slotPanel = getBlackSlotPanel();
		panel.add(slotPanel, c);
	}

	private JLabel getCenteredLabel()
	{
		JLabel label = new JLabel();
		label.setVerticalAlignment(JLabel.CENTER);
		label.setHorizontalAlignment(JLabel.CENTER);

		return label;
	}

	private JPanel getBlackSlotPanel()
	{
		JLabel label = getCenteredLabel();

		JPanel item = new JPanel();
		item.setPreferredSize(new Dimension(36, 36));
		item.add(label);

		return item;
	}

	public void update(ItemManager itemManager, ItemSharePlayer player)
	{
		Map<EquipmentInventorySlot, ItemShareItem> slots = player.getEquipment().getSlots();
		updateItems(itemManager, slots);
		repaint();
	}

	private void updateItems(ItemManager itemManager, Map<EquipmentInventorySlot, ItemShareItem> slots)
	{
		Arrays.stream(EquipmentInventorySlot.values()).forEach(slot -> updateItem(itemManager, slots, slot));
	}

	private void updateItem(ItemManager itemManager, Map<EquipmentInventorySlot, ItemShareItem> slots, EquipmentInventorySlot slot)
	{
		JPanel slotPanel = slotPanels.get(slot);
		ItemShareItem item = slots.get(slot);

		if (item == null)
		{
			removeIcon(slotPanel);
		}
		else if (item.getId() >= 0)
		{
			addIcon(itemManager, slotPanel, item);
		}
		else
		{
			removeIcon(slotPanel);
		}
	}

	private void addIcon(ItemManager itemManager, JPanel itemPanel, ItemShareItem item)
	{
		JLabel label = (JLabel) itemPanel.getComponent(0);
		AsyncBufferedImage icon = getIcon(itemManager, item);
		label.setToolTipText(item.getName());
		icon.addTo(label);
	}

	private void removeIcon(JPanel itemPanel)
	{
		JLabel label = (JLabel) itemPanel.getComponent(0);
		label.setToolTipText(null);
		label.setIcon(null);
		label.repaint();
		itemPanel.repaint();
	}

	private AsyncBufferedImage getIcon(ItemManager itemManager, ItemShareItem item)
	{
		return itemManager.getImage(item.getId(), item.getQuantity(), item.getQuantity() > 1);
	}
}