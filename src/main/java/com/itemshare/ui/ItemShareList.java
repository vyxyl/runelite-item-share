package com.itemshare.ui;

import com.itemshare.model.ItemShareContainer;
import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemShareRenderItem;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.AsyncBufferedImage;

public class ItemShareList extends JPanel
{
	private List<ItemShareRenderItem> currentRenderItems = new ArrayList<>();
	private final JList<ItemShareRenderItem> itemList;

	protected ItemShareList()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		setAlignmentX(Component.LEFT_ALIGNMENT);
		itemList = new JList<>();
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
		currentRenderItems = getUpdatedItems(itemManager, items);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		ItemShareListModel model = new ItemShareListModel();
		model.addItems(currentRenderItems);

		itemList.setModel(model);
		itemList.setCellRenderer(new ItemShareListRenderer());

		panel.add(itemList);

		JScrollPane scrollPane = new JScrollPane(panel);
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
		JPanel panelItem = new JPanel();
		JLabel itemIcon = new JLabel(item.getIcon());
		JLabel itemName = new JLabel(item.getItem().getName());

		panelItem.setLayout(new BoxLayout(panelItem, BoxLayout.LINE_AXIS));
		panelItem.setAlignmentX(Component.LEFT_ALIGNMENT);

		panelItem.add(itemIcon);
		panelItem.add(Box.createRigidArea(new Dimension(10, 0)));
		panelItem.add(itemName);

		item.getImage().onLoaded(panelItem::repaint);

		return panelItem;
	}

	private boolean isOsrsNull(ItemShareRenderItem renderItem)
	{
		return isOsrsNull(renderItem.getItem());
	}

	private boolean isOsrsNull(ItemShareItem item)
	{
		return item == null || item.getName().equals("null") || item.getId() == -1;
	}
}
