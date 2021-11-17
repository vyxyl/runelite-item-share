package com.itemshare.ui;

import com.itemshare.model.ItemShareContainer;
import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemShareRenderItem;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.util.AsyncBufferedImage;

public class ItemShareList extends JPanel
{
	private final IconTextField searchBox = new IconTextField();
	private List<ItemShareRenderItem> currentRenderItems = new ArrayList<>();
	private final JList<ItemShareRenderItem> itemList;
	private final ItemShareListModel model = new ItemShareListModel();

	protected ItemShareList()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		createSearchBox();
		itemList = new JList<>();
	}

	private void createSearchBox()
	{
		searchBox.setIcon(IconTextField.Icon.SEARCH);
		searchBox.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		searchBox.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		searchBox.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		searchBox.setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		searchBox.setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		searchBox.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				searchItem();
				itemList.repaint();
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				searchItem();
				itemList.repaint();
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
			}

			public void searchItem()
			{
				model.searchItem(searchBox.getText());
			}
		});
	}

	public void update(ItemManager itemManager, ItemShareContainer data)
	{
		removeAll();
		add(searchBox);
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

		model.clearItems();
		model.addItems(currentRenderItems);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		itemList.setModel(model);
		itemList.setCellRenderer(new ItemShareListRenderer());
		itemList.setFixedCellWidth(PluginPanel.PANEL_WIDTH);

		panel.add(itemList);

		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		add(scrollPane);
	}

	private List<ItemShareRenderItem> getUpdatedItems(ItemManager itemManager, List<ItemShareItem> items)
	{
		List<ItemShareItem> validItems = items.stream()
			.filter(item -> !isNullItem(item))
			.collect(Collectors.toList());

		List<ItemShareRenderItem> renderItems = validItems.stream()
			.map(item -> ItemShareRenderItem.builder().item(item).build())
			.collect(Collectors.toList());

		List<ItemShareRenderItem> existingRenderItems = currentRenderItems.stream()
			.filter(renderItems::contains)
			.collect(Collectors.toList());

		List<ItemShareRenderItem> newRenderItems = renderItems.stream()
			.filter(item -> !isNullItem(item) && !currentRenderItems.contains(item))
			.sorted(Comparator.comparing((ItemShareRenderItem a) -> a.getItem().getName()))
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

	private boolean isNullItem(ItemShareRenderItem renderItem)
	{
		return isNullItem(renderItem.getItem());
	}

	private boolean isNullItem(ItemShareItem item)
	{
		return item == null || item.getName().equals("null") || item.getId() == -1;
	}
}
