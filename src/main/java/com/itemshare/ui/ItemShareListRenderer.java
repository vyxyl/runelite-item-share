package com.itemshare.ui;

import com.itemshare.model.ItemShareRenderItem;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ItemShareListRenderer extends JLabel implements ListCellRenderer<ItemShareRenderItem>
{
	@Override
	public Component getListCellRendererComponent(JList<? extends ItemShareRenderItem> list, ItemShareRenderItem value, int index, boolean isSelected, boolean cellHasFocus)
	{
		setText(value.getItem().getName());
		setIcon(new ImageIcon(value.getImage()));
		return this;
	}
}