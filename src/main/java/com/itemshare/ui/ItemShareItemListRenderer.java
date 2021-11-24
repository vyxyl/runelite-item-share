package com.itemshare.ui;

import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemShareRenderItem;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import net.runelite.client.util.AsyncBufferedImage;

public class ItemShareItemListRenderer extends JLabel implements ListCellRenderer<ItemShareRenderItem>
{
	@Override
	public Component getListCellRendererComponent(JList<? extends ItemShareRenderItem> list, ItemShareRenderItem value, int index, boolean isSelected, boolean cellHasFocus)
	{
		ItemShareItem item = value.getItem();
		AsyncBufferedImage image = value.getImage();

		String name = item == null ? "N/A" : item.getName();
		ImageIcon icon = image == null ? new ImageIcon() : new ImageIcon(image);

		setText(name);
		setIcon(icon);

		return this;
	}
}