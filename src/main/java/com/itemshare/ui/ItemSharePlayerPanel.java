package com.itemshare.ui;

import com.itemshare.ItemSharePlugin;
import static com.itemshare.constant.ItemShareConstants.ICON_BANK;
import static com.itemshare.constant.ItemShareConstants.ICON_EQUIPMENT;
import static com.itemshare.constant.ItemShareConstants.ICON_GIM;
import static com.itemshare.constant.ItemShareConstants.ICON_INVENTORY;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.state.ItemShareState;
import java.awt.BorderLayout;
import java.awt.Image;
import static java.awt.Image.SCALE_SMOOTH;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.runelite.client.util.ImageUtil;

public class ItemSharePlayerPanel extends JPanel
{
	JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

	ItemShareEquipmentPanel equipment;
	ItemShareInventoryPanel inventory;
	ItemShareBankPanel bank;
	ItemShareBankPanel gimStorage;

	protected ItemSharePlayerPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		equipment = new ItemShareEquipmentPanel();
		inventory = new ItemShareInventoryPanel();
		bank = new ItemShareBankPanel();
		gimStorage = new ItemShareBankPanel();

		tabs.addTab("Gear", getIcon(ICON_EQUIPMENT), equipment, "Equipment");
		tabs.addTab("Inv", getIcon(ICON_INVENTORY), inventory, "Inventory");
		tabs.addTab("Bank", getIcon(ICON_BANK), bank, "Bank");
		tabs.addTab("GIM", getIcon(ICON_GIM), gimStorage, "Group Ironman");
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
		bank.update(player);
		gimStorage.update(ItemShareState.gimStorage);

		revalidate();
		repaint();
	}
}