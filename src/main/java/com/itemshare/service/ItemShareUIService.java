package com.itemshare.service;

import static com.itemshare.constant.ItemShareConstants.ICON_NAV_BUTTON;
import com.itemshare.state.ItemShareState;
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

	public static void load()
	{
		assert SwingUtilities.isEventDispatchThread();
		panel = new ItemSharePanel();

		BufferedImage image = ItemSharePanelService.loadImage(ICON_NAV_BUTTON);
		NavigationButton button = getNavigationButton(image);

		ItemShareState.toolbar.addNavigation(button);
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
