package com.itemshare.ui;

import java.awt.Component;
import javax.swing.BoxLayout;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class ItemSharePanel extends PluginPanel
{
	private final ItemShareSettingsPanel settingsPanel;
	private final ItemShareNavigationPanel navPanel;

	public ItemSharePanel()
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		navPanel = new ItemShareNavigationPanel(this::showSettings);
		settingsPanel = new ItemShareSettingsPanel(this::showItems);

		add(settingsPanel);
		add(navPanel);

		showItems();
	}

	public void update()
	{
		settingsPanel.update();
		navPanel.update();
	}

	private void showItems()
	{
		removeAll();
		add(navPanel);

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
