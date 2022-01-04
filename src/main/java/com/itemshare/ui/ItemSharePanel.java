package com.itemshare.ui;

import javax.swing.BoxLayout;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class ItemSharePanel extends PluginPanel
{
	private final ItemShareSettingsPanel settingsPanel;
	private final ItemShareContentPanel headerPanel;

	public ItemSharePanel()
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		headerPanel = new ItemShareContentPanel(this::showSettings);
		settingsPanel = new ItemShareSettingsPanel(this::showItems);

		add(settingsPanel);
		add(headerPanel);

		showItems();
	}

	public void update()
	{
		headerPanel.update();
	}

	private void showItems()
	{
		removeAll();
		add(headerPanel);

		repaint();
		revalidate();
	}

	private void showSettings()
	{
		removeAll();
		add(settingsPanel);

		repaint();
		revalidate();
	}
}