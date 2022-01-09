package com.itemshare.ui;

import com.itemshare.service.ItemSharePanelService;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class ItemShareTitlePanel extends PluginPanel
{
	public ItemShareTitlePanel(String label, Icon icon, Runnable onClick)
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		JLabel titleLabel = new JLabel();
		titleLabel.setText(label);
		titleLabel.setForeground(Color.WHITE);

		JButton button = ItemSharePanelService.getButton(icon, null, onClick);

		ItemSharePanelService.setSize(titleLabel, 0.90, 30);
		ItemSharePanelService.setSize(button, 20, 20);

		titleLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		button.setAlignmentY(Component.CENTER_ALIGNMENT);

		add(titleLabel);
		add(button);
	}
}
