package com.itemshare.ui;

import com.itemshare.model.ItemSharePlayer;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;

public final class ItemSharePlayerDropdownRenderer extends DefaultListCellRenderer
{
	@Override
	public Component getListCellRendererComponent(JList<?> players, Object o, int index, boolean isSelected, boolean b1)
	{
		setForeground(Color.WHITE);
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setBorder(new EmptyBorder(0, 0, 0, 0));

		if (isSelected)
		{
			setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		}
		else
		{
			setForeground(Color.WHITE);
		}

		String name = getPlayerName(o);
		setText(name);

		return this;
	}

	private String getPlayerName(Object o)
	{
		if (o instanceof String)
		{
			return (String) o;
		}
		else
		{
			return "N/A";
		}
	}
}