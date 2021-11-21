package com.itemshare.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class ItemShareTitlePanel extends PluginPanel
{
	JPanel titlePanel = new JPanel();
	JLabel titleLabel = new JLabel();

	public ItemShareTitlePanel()
	{
		super(false);
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		titleLabel.setForeground(Color.WHITE);
		titlePanel.setLayout(new BorderLayout());
		titlePanel.add(titleLabel, BorderLayout.WEST);

		add(titlePanel);
	}

	public void setTitleName(String name) {
		titleLabel.setText(name);
		repaint();
	}
}
