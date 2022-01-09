package com.itemshare.ui;

import com.itemshare.ItemSharePlugin;
import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GIM_ENABLED;
import static com.itemshare.constant.ItemShareConstants.ICON_BANK;
import static com.itemshare.constant.ItemShareConstants.ICON_EQUIPMENT;
import static com.itemshare.constant.ItemShareConstants.ICON_GIM;
import static com.itemshare.constant.ItemShareConstants.ICON_INVENTORY;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.state.ItemShareState;
import java.awt.BorderLayout;
import java.awt.Image;
import static java.awt.Image.SCALE_SMOOTH;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.runelite.client.util.ImageUtil;

public class ItemSharePlayerItemsPanel extends JPanel
{
	JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

	ItemShareEquipmentPanel equipment;
	ItemShareInventoryPanel inventory;
	ItemShareBankPanel bank;
	ItemShareBankPanel gimStorage;

	protected ItemSharePlayerItemsPanel(Consumer<Boolean> isGIMSelected)
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		equipment = new ItemShareEquipmentPanel();
		inventory = new ItemShareInventoryPanel();
		bank = new ItemShareBankPanel();
		gimStorage = new ItemShareBankPanel();

		tabs.addTab("Gear", getIcon(ICON_EQUIPMENT), equipment, "Equipment");
		tabs.addTab("Inventory", getIcon(ICON_INVENTORY), inventory, "Inventory");
		tabs.addTab("Bank", getIcon(ICON_BANK), bank, "Bank");
		updateGIM();

		tabs.setAlignmentX(CENTER_ALIGNMENT);

		tabs.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				isGIMSelected.accept(Objects.equals(gimStorage, tabs.getSelectedComponent()));
			}
		});

		add(tabs, BorderLayout.NORTH);
	}

	public void update(ItemSharePlayer player)
	{
		equipment.update(player);
		inventory.update(player);
		bank.update(player);
		updateGIM();

		revalidate();
		repaint();
	}

	private void updateGIM()
	{
		boolean isGIMEnabled = isGIMEnabled();
		boolean hasGIMTab = tabs.indexOfComponent(gimStorage) > -1;

		if (isGIMEnabled)
		{
			gimStorage.update(ItemShareState.gimStorage);

			if (!hasGIMTab)
			{
				addGIMTab();
				tabs.setTitleAt(tabs.indexOfComponent(inventory), "Inv");
			}
		}
		else if (hasGIMTab)
		{
			removeGIMTab();
			tabs.setTitleAt(tabs.indexOfComponent(inventory), "Inventory");
		}
	}

	private boolean isGIMEnabled()
	{
		return Boolean.parseBoolean(ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_GIM_ENABLED));
	}

	private void addGIMTab()
	{
		tabs.addTab("GIM", getIcon(ICON_GIM), gimStorage, "Group Ironman");
	}

	private void removeGIMTab()
	{
		tabs.remove(gimStorage);
	}

	private ImageIcon getIcon(String path)
	{
		Image image = ImageUtil.loadImageResource(ItemSharePlugin.class, path);
		return new ImageIcon(image.getScaledInstance(16, 16, SCALE_SMOOTH));
	}
}