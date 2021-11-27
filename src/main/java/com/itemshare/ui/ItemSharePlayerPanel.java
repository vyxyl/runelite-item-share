package com.itemshare.ui;

import com.itemshare.ItemSharePlugin;
import static com.itemshare.constant.ItemShareConstants.ICON_BANK;
import static com.itemshare.constant.ItemShareConstants.ICON_CLOSE_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_EQUIPMENT;
import static com.itemshare.constant.ItemShareConstants.ICON_INVENTORY;
import static com.itemshare.constant.ItemShareConstants.ICON_SETTINGS_BUTTON;
import com.itemshare.model.ItemSharePlayer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import static java.awt.Image.SCALE_SMOOTH;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;

public class ItemSharePlayerPanel extends JPanel
{
	JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

	ItemShareEquipmentPanel equipment;
	ItemShareInventoryPanel inventory;
	ItemShareItemListPanel bank;

	protected ItemSharePlayerPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		equipment = new ItemShareEquipmentPanel();
		inventory = new ItemShareInventoryPanel();
		bank = new ItemShareItemListPanel();

		tabs.addTab("Gear", getIcon(ICON_EQUIPMENT), equipment, "Equipment");
		tabs.addTab("Inventory", getIcon(ICON_INVENTORY), inventory, "Inventory");
		tabs.addTab("Bank", getIcon(ICON_BANK), bank, "Bank");
		tabs.setAlignmentX(CENTER_ALIGNMENT);

		add(tabs, BorderLayout.NORTH);
	}

	private ImageIcon getIcon(String path)
	{
		Image image = ImageUtil.loadImageResource(ItemSharePlugin.class, path);
		return new ImageIcon(image.getScaledInstance(16, 16, SCALE_SMOOTH));
	}

	public void update(ItemSharePlayer player)
	{
		equipment.update(player);
		inventory.update(player);
		bank.update(player.getBank());

		revalidate();
		repaint();
	}
}
