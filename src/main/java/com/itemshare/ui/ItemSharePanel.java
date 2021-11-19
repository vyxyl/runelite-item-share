package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import java.awt.BorderLayout;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class ItemSharePanel extends PluginPanel
{
	private final ItemShareNavigationPanel playerPanel = new ItemShareNavigationPanel();

	public ItemSharePanel()
	{
		super(false);
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BorderLayout());

		ItemShareTitlePanel titlePanel = new ItemShareTitlePanel();

		add(titlePanel, BorderLayout.NORTH);
		add(playerPanel, BorderLayout.CENTER);
	}

	public void update(ItemManager itemManager, ItemShareData data)
	{
		playerPanel.update(itemManager, data);
		repaint();
	}
}
