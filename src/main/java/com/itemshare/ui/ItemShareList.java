package com.itemshare.ui;

import com.itemshare.model.ItemShareContainer;
import com.itemshare.model.ItemShareRenderItem;
import com.itemshare.model.ItemShareItem;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.AsyncBufferedImage;

public class ItemShareList extends JPanel
{
	private JPanel panel;
	private List<JPanel> panelItems;
	private List<ItemShareRenderItem> currentRenderItems = new ArrayList<>();

	protected ItemShareList()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	public void reset()
	{
		removeAll();
		repaint();
	}

	public void update(ItemManager itemManager, ItemShareContainer data)
	{
		removeAll();
		updateItems(itemManager, data.getItems());
		revalidate();
		repaint();
	}

	private void updateItems(ItemManager itemManager, List<ItemShareItem> items)
	{
		if (!items.isEmpty())
		{
			displayItems(itemManager, items);
		}
	}

	private void displayItems(ItemManager itemManager, List<ItemShareItem> items)
	{
		panel = new JPanel(new GridLayout(11, 2, 10, 5));
		currentRenderItems = getUpdatedItems(itemManager, items);
		panelItems = currentRenderItems.stream().map(this::getPanelItem).collect(Collectors.toList());

		panelItems.forEach(panelItem -> {
			panel.add(panelItem);
		});

		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH - 16, 32 * 11));

		add(scrollPane);
	}

	private List<ItemShareRenderItem> getUpdatedItems(ItemManager itemManager, List<ItemShareItem> items)
	{
		List<ItemShareItem> validItems = items.stream()
			.filter(item -> !isOsrsNull(item))
			.collect(Collectors.toList());

		List<ItemShareRenderItem> renderItems = validItems.stream()
			.map(item -> ItemShareRenderItem.builder().item(item).build())
			.collect(Collectors.toList());

		List<ItemShareRenderItem> existingRenderItems = currentRenderItems.stream()
			.filter(renderItems::contains)
			.collect(Collectors.toList());

		List<ItemShareRenderItem> newRenderItems = renderItems.stream()
			.filter(item -> !isOsrsNull(item) && !currentRenderItems.contains(item))
			.collect(Collectors.toList());

		newRenderItems.forEach(renderItem -> {
			AsyncBufferedImage icon = itemManager.getImage(
				renderItem.getItem().getId(),
				renderItem.getItem().getQuantity(),
				renderItem.getItem().getQuantity() > 1
			);

			renderItem.setImage(icon);
			renderItem.setIcon(new ImageIcon(icon));
		});

		existingRenderItems.addAll(newRenderItems);

		return existingRenderItems;
	}

	private JPanel getPanelItem(ItemShareRenderItem item)
	{
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		JPanel panelItem = new JPanel(new GridLayout(1, 2, 10, 5));
		JLabel itemIcon = new JLabel(item.getIcon());
		panelItem.add(itemIcon, constraints);

		JLabel itemName = new JLabel(item.getItem().getName());
		panelItem.add(itemName, constraints);

		item.getImage().onLoaded(panelItem::repaint);

		return panelItem;
	}

	private boolean isOsrsNull(ItemShareItem item)
	{
		return item == null || item.getName().equals("null") || item.getId() == -1;
	}

	private boolean isOsrsNull(ItemShareRenderItem renderItem)
	{
		return isOsrsNull(renderItem.getItem());
	}
}
