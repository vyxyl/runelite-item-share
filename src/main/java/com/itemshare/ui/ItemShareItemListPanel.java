package com.itemshare.ui;

import com.itemshare.model.ItemShareItems;
import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemShareRenderItem;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.util.AsyncBufferedImage;

public class ItemShareItemListPanel extends JPanel
{
	private final IconTextField searchBox = new IconTextField();
	private final ItemShareItemListModel model = new ItemShareItemListModel();

	private final JList<ItemShareRenderItem> list;
	private final JScrollPane scrollPane;
	private ItemManager itemManager;

	protected ItemShareItemListPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		createSearchBox();

		list = new JList<>();
		list.setCellRenderer(new ItemShareItemListRenderer());
		list.setFixedCellWidth(PluginPanel.PANEL_WIDTH);
		list.setModel(model);

		scrollPane = new JScrollPane(list);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		add(searchBox);
		add(scrollPane);
	}

	public void update(ItemManager itemManager, ItemShareItems data)
	{
		updateItems(itemManager, data.getItems());
		applyFilter();
	}

	private void createSearchBox()
	{
		searchBox.setIcon(IconTextField.Icon.SEARCH);
		searchBox.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		searchBox.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		searchBox.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		searchBox.setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		searchBox.setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		searchBox.addKeyListener(getSearchBoxListener());
	}

	private KeyListener getSearchBoxListener()
	{
		return new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				applyFilter();
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				applyFilter();
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				applyFilter();
			}
		};
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
	}

	private void applyFilter()
	{
		String searchText = getSearchText();
		model.filterItems(searchText);
		list.repaint();
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
