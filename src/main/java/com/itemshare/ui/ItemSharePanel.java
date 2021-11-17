package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import javax.swing.BoxLayout;
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
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	public void update(ItemManager itemManager, ItemShareData data)
	{
		if (playerPanel == null)
		{
			playerPanel = new ItemSharePlayerPanel(itemManager, data);
			add(playerPanel);
		}

		playerPanel.update(data);
		repaint();
	}
}
