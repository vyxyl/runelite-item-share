package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class ItemSharePanel extends PluginPanel
{
	private final ItemSharePlayerPanel playerInfoPanel;

	public ItemSharePanel()
	{
		super(false);
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		JPanel titlePanel = getTitlePanel();
		add(titlePanel);

		playerInfoPanel = new ItemSharePlayerPanel();
		add(playerInfoPanel);
	}

	public void reset()
	{
		playerInfoPanel.reset();
	}

	public void update(ItemManager itemManager, ItemShareData data)
	{
		playerInfoPanel.update(itemManager, data);
	}

	private JPanel getTitlePanel()
	{
		JLabel title = new JLabel();
		title.setText("Item Share");
		title.setForeground(Color.WHITE);

		JPanel titlePanel = new JPanel();
		titlePanel.add(title, BorderLayout.WEST);

		return titlePanel;
	}
}
