package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class ItemSharePanel extends PluginPanel
{
	private final JPanel titlePanel = getTitlePanel();
	private final ItemSharePlayerPanel playerInfoPanel = new ItemSharePlayerPanel();

	public ItemSharePanel()
	{
		super(false);
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	public void reset()
	{
		playerInfoPanel.reset();
	}

	public void update(ItemManager itemManager, ItemShareData data)
	{
		removeAll();
		add(titlePanel);
		add(playerInfoPanel);

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
