package com.itemshare.ui;

import com.itemshare.model.ItemShareContainer;
import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemShareRenderItem;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.util.AsyncBufferedImage;

public class ItemShareListPanel extends JPanel
{
	private final IconTextField searchBox = new IconTextField();
	private List<ItemShareRenderItem> currentItems = new ArrayList<>();
	private final JList<ItemShareRenderItem> list;
	private final ItemShareListModel model = new ItemShareListModel();
	private final JScrollPane scrollPane;

	protected ItemShareListPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		createSearchBox();

		list = new JList<>();
		list.setCellRenderer(new ItemShareListRenderer());
		list.setFixedCellWidth(PluginPanel.PANEL_WIDTH);

		scrollPane = new JScrollPane(list);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		add(searchBox);
		add(scrollPane);
	}

	private void createSearchBox()
	{
		searchBox.setIcon(IconTextField.Icon.SEARCH);
		searchBox.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		searchBox.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		searchBox.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		searchBox.setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		searchBox.setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		searchBox.getDocument().addDocumentListener(getSearchBoxListener());
	}

	private DocumentListener getSearchBoxListener()
	{
		return new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				searchItem();
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				searchItem();
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				searchItem();
			}
		};
	}

	public void update(ItemManager itemManager, ItemShareContainer data)
	{
		updateItems(itemManager, data.getItems());
		revalidate();
		repaint();
	}

	private void updateItems(ItemManager itemManager, List<ItemShareItem> items)
	{
		if (items.isEmpty())
		{
			currentItems.clear();
			model.removeAll();
		}
		else
		{
			currentItems = getUpdatedItems(itemManager, items);
			model.replaceAll(currentItems);
		}

		list.setModel(model);
	}

	private void searchItem()
	{
		String text = searchBox.getText();

		model.filterItems(text);
		list.setModel(model);

		list.repaint();
		scrollPane.repaint();

		revalidate();
		repaint();
	}

	private List<ItemShareRenderItem> getUpdatedItems(ItemManager itemManager, List<ItemShareItem> items)
	{
		List<ItemShareRenderItem> newItems = items.stream()
			.filter(item -> !isNullItem(item))
			.map(item -> ItemShareRenderItem.builder().item(item).build())
			.collect(Collectors.toList());

		newItems.forEach(item -> {
			AsyncBufferedImage icon = getIcon(itemManager, item);
			item.setImage(icon);
			item.setIcon(new ImageIcon(icon));
			icon.onLoaded(() -> repaintItem(item));
		});

		return newItems;
	}

	private AsyncBufferedImage getIcon(ItemManager itemManager, ItemShareRenderItem renderItem)
	{
		ItemShareItem item = renderItem.getItem();
		int quantity = item.getQuantity();
		return itemManager.getImage(item.getId(), quantity, quantity > 1);
	}

	private void repaintItem(ItemShareRenderItem item)
	{
		int index = model.getIndex(item);

		if (index >= 0)
		{
			list.repaint(list.getCellBounds(index, index));
		}
	}

	private boolean isNullItem(ItemShareItem item)
	{
		return item == null || item.getName().equals("null") || item.getId() == -1;
	}
}
