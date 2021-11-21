package com.itemshare.ui;

import com.itemshare.model.ItemShareContainer;
import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemShareRenderItem;
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
	private final ItemShareListModel model = new ItemShareListModel();

	private final JList<ItemShareRenderItem> list;
	private final JScrollPane scrollPane;
	private ItemManager itemManager;

	protected ItemShareListPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		createSearchBox();

		list = new JList<>();
		list.setCellRenderer(new ItemShareListRenderer());
		list.setFixedCellWidth(PluginPanel.PANEL_WIDTH);
		list.setModel(model);

		scrollPane = new JScrollPane(list);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		add(searchBox);
		add(scrollPane);
	}

	public void update(ItemManager itemManager, ItemShareContainer data)
	{
		updateItems(itemManager, data.getItems());
		repaintAll();
	}

	public void clearFilter()
	{
		searchBox.setText("");
		model.clearFilter();;
		repaintAll();
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
				filterItems();
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				filterItems();
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				filterItems();
			}
		};
	}

	private void filterItems()
	{
		applyCurrentFilter();
		repaintAll();
	}

	private void repaintAll()
	{
		list.setModel(model);

		list.repaint();
		scrollPane.repaint();

		revalidate();
		repaint();
	}

	private void updateItems(ItemManager itemManager, List<ItemShareItem> items)
	{
		this.itemManager = itemManager;

		if (items == null || items.isEmpty())
		{
			removeAllItems();
		}
		else
		{
			setAllItems(items);
		}
	}

	private void removeAllItems()
	{
		model.removeAll();
	}

	private void setAllItems(List<ItemShareItem> baseItems)
	{
		List<ItemShareRenderItem> allItems = getRenderItems(baseItems);
		setAllRenderItems(allItems);
	}

	private void setAllRenderItems(List<ItemShareRenderItem> allItems)
	{
		List<ItemShareRenderItem> items = model.getUnfiliteredItems();
		List<ItemShareRenderItem> existingItems = items.stream().filter(allItems::contains).collect(Collectors.toList());
		List<ItemShareRenderItem> newItems = allItems.stream().filter(item -> !items.contains(item)).collect(Collectors.toList());

		existingItems.addAll(newItems);
		existingItems.sort(Comparator.comparing(item -> item.getItem().getName()));

		model.replaceAll(existingItems);
		renderIcons(newItems);

		applyCurrentFilter();
	}

	private void applyCurrentFilter()
	{
		model.filterItems(getSearchText());
	}

	private String getSearchText()
	{
		return searchBox == null ? "" : searchBox.getText();
	}

	private void renderIcons(List<ItemShareRenderItem> items)
	{
		items.forEach(item -> {
			AsyncBufferedImage icon = getIcon(itemManager, item);
			item.setImage(icon);
			item.setIcon(new ImageIcon(icon));
			icon.onLoaded(() -> repaintItem(item));
		});
	}

	private List<ItemShareRenderItem> getRenderItems(List<ItemShareItem> items)
	{
		return items.stream()
			.filter(item -> !isNullItem(item))
			.map(item -> ItemShareRenderItem.builder().item(item).build())
			.collect(Collectors.toList());
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
