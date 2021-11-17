package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import java.awt.BorderLayout;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class ItemSharePanel extends PluginPanel
{
	private ItemSharePlayerPanel playerPanel;

	public ItemSharePanel()
	{
		super(false);
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BorderLayout());

		ItemShareTitlePanel titlePanel = new ItemShareTitlePanel();
		add(titlePanel, BorderLayout.NORTH);
	}

	public void update(ItemManager itemManager, ItemShareData data)
	{
		if (playerPanel == null)
		{
			playerPanel = new ItemSharePlayerPanel(itemManager, data);
			add(playerPanel, BorderLayout.CENTER);
		}

		playerPanel.update(data);
		repaint();
	}
}
