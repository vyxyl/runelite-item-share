package com.itemshare.ui;

import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemSharePlayer;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.AsyncBufferedImage;

public class ItemShareInventoryPanel extends JPanel
{
	List<JPanel> itemPanels = new ArrayList<>();
	JPanel panel = new JPanel(new GridBagLayout());

	public ItemShareInventoryPanel()
	{
		super(false);

		addItems();
		add(panel);
	}

	private void addItems()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(0, 0, 5, 5);

		IntStream.range(0, 7).forEach(y -> IntStream.range(0, 4).forEach(x -> addItem(c, x, y)));
	}

	private void addItem(GridBagConstraints c, int x, int y)
	{
		c.gridx = x;
		c.gridy = y;

		JLabel label = new JLabel();
		label.setVerticalAlignment(JLabel.CENTER);
		label.setHorizontalAlignment(JLabel.CENTER);

		JPanel itemPanel = new JPanel();
		itemPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		itemPanel.setPreferredSize(new Dimension(36, 36));
		itemPanel.add(label);

		itemPanels.add(itemPanel);
		panel.add(itemPanel, c);
	}

	public void update(ItemManager itemManager, ItemSharePlayer player)
	{
		List<ItemShareItem> items = player.getInventory().getItems();
		updateItems(itemManager, items);
		repaint();
	}

	private void updateItems(ItemManager itemManager, List<ItemShareItem> items)
	{
		IntStream.range(0, items.size()).forEach(index -> updateItem(itemManager, items, index));
	}

	private void updateItem(ItemManager itemManager, List<ItemShareItem> items, int index)
	{
		JPanel itemPanel = itemPanels.get(index);
		ItemShareItem item = items.get(index);

		if (item == null)
		{
			removeIcon(itemPanel);
		}
		else if (item.getId() >= 0)
		{
			addIcon(itemManager, itemPanel, item);
		}
		else
		{
			removeIcon(itemPanel);
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
		label.removeAll();
		label.repaint();
		itemPanel.repaint();
	}

	private AsyncBufferedImage getIcon(ItemManager itemManager, ItemShareItem item)
	{
		return itemManager.getImage(item.getId(), item.getQuantity(), item.getQuantity() > 1);
	}
}
