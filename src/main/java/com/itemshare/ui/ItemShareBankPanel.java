package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.SELECT_A_PLAYER;
import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemShareItems;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.model.ItemShareRenderItem;
import com.itemshare.service.ItemSharePanelService;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.AsyncBufferedImage;

public class ItemShareBankPanel extends JPanel
{
	private final ItemShareItemListRenderer renderer = new ItemShareItemListRenderer();
	private final ItemShareItemSearchBoxPanel searchBox = new ItemShareItemSearchBoxPanel();
	private final ItemShareItemListModel model = new ItemShareItemListModel();

	private final JScrollPane scrollPane;
	private final JList<ItemShareRenderItem> list;

	protected ItemShareBankPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		list = new JList<>();
		list.setModel(model);
		list.setCellRenderer(renderer);
		list.setFixedCellWidth(PluginPanel.PANEL_WIDTH);

		scrollPane = new JScrollPane(list);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		searchBox.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		searchBox.setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		searchBox.setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		searchBox.addListener(this::repaintAll);

		add(searchBox);
		add(scrollPane);
	}

	public void update(ItemSharePlayer player)
	{
		ItemShareItems bank = player.getBank();
		List<ItemShareItem> items = bank == null ? new ArrayList<>() : bank.getItems();
		updateItems(items);
		repaintAll();
	}

	private void updateItems(List<ItemShareItem> items)
	{
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
		model.removeAllItems();
	}

	private void setAllItems(List<ItemShareItem> baseItems)
	{
		List<ItemShareRenderItem> allItems = getRenderItems(baseItems);
		List<ItemShareRenderItem> unfilteredItems = model.getUnfilteredItems();
		List<ItemShareRenderItem> existingItems = unfilteredItems.stream().filter(allItems::contains).collect(Collectors.toList());
		List<ItemShareRenderItem> newItems = allItems.stream().filter(item -> !unfilteredItems.contains(item)).collect(Collectors.toList());

		existingItems.addAll(newItems);
		existingItems.sort(Comparator.comparing(item -> item.getItem().getName()));

		model.setItems(existingItems);
		newItems.forEach(this::addIcon);
	}

	private void repaintAll()
	{
		String searchText = searchBox.getText();
		model.filterItems(searchText);
		list.setModel(model);

		repaintItems(model.getFilteredItems());

		renderer.repaint();
		list.repaint();

		remove(scrollPane);
		add(scrollPane);
		scrollPane.repaint();

		revalidate();
		repaint();
	}

	private void addIcon(ItemShareRenderItem item)
	{
		AsyncBufferedImage icon = getIcon(item);
		item.setIcon(icon);
		icon.onLoaded(() -> repaintItem(item));
	}

	private AsyncBufferedImage getIcon(ItemShareRenderItem renderItem)
	{
		ItemShareItem item = renderItem.getItem();
		return ItemSharePanelService.getIcon(item);
	}

	private void repaintItems(List<ItemShareRenderItem> items)
	{
		items.forEach(this::repaintItem);
	}

	private void repaintItem(ItemShareRenderItem item)
	{
		int index = model.getIndex(item);

		if (index >= 0)
		{
			try
			{
				list.repaint(list.getCellBounds(index, index));
			}
			catch (Exception e)
			{
				//
			}
		}
	}

	private List<ItemShareRenderItem> getRenderItems(List<ItemShareItem> items)
	{
		return items.stream()
			.filter(item -> !isNullItem(item))
			.map(item -> ItemShareRenderItem.builder().item(item).build())
			.collect(Collectors.toList());
	}

	private boolean isNullItem(ItemShareItem item)
	{
		return item == null || item.getName().equals("null") || item.getId() == -1;
	}
}