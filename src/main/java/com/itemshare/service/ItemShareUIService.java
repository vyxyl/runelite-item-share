package com.itemshare.service;

import static com.itemshare.constant.ItemShareConstants.ICON_PLUGIN;
import com.itemshare.ui.ItemSharePanel;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import net.runelite.client.ui.NavigationButton;

public class ItemShareUIService
{
	private static ItemSharePanel panel;

	public static void update()
	{
		if (panel != null)
		{
			SwingUtilities.invokeLater(() -> panel.update());
		}
	}

	public static NavigationButton getNavButton()
	{
		assert SwingUtilities.isEventDispatchThread();
		panel = new ItemSharePanel();

		BufferedImage image = ItemSharePanelService.loadImage(ICON_PLUGIN);

		return getNavigationButton(image);
	}

	private static NavigationButton getNavigationButton(BufferedImage icon)
	{
		return NavigationButton.builder()
			.tooltip("View shared items")
			.icon(icon)
			.priority(100)
			.panel(panel)
			.build();
	}
}