package com.itemshare.service;

import com.itemshare.model.ItemShareItem;
import com.itemshare.state.ItemShareState;
import java.awt.Dimension;
import javax.swing.JComponent;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.AsyncBufferedImage;

public class ItemSharePanelService
{
	public static AsyncBufferedImage getIcon(ItemShareItem item)
	{
		int id = item.getId();
		int quantity = item.getQuantity();

		return ItemShareState.itemManager.getImage(id, quantity, quantity > 1);
	}

	public static void setSize(JComponent component, int widthPercent, int height)
	{
		int width = PluginPanel.PANEL_WIDTH * widthPercent / 100;

		component.setPreferredSize(new Dimension(width, height));
		component.setMinimumSize(new Dimension(width, height));
		component.setMaximumSize(new Dimension(width, height));
	}
}
