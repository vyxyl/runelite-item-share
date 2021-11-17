package com.itemshare.ui;

import com.itemshare.ItemSharePlugin;
import static com.itemshare.constant.ItemShareConstants.ICON;
import static com.itemshare.constant.ItemShareConstants.SETTINGS_ICON;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;

public class ItemShareTitlePanel extends PluginPanel
{
	public ItemShareTitlePanel()
	{
		super(false);
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel titleLabel = new JLabel();
		titleLabel.setText("Item Share");
		titleLabel.setForeground(Color.WHITE);

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.add(titleLabel, BorderLayout.WEST);

		JButton settingsButton = createSettingsButton();
		JPanel controls = new JPanel(new GridLayout(1, 3, 10, 0));
		controls.setBackground(ColorScheme.DARK_GRAY_COLOR);
		controls.add(settingsButton);

		add(titlePanel);
		add(controls);
	}

	private JButton createSettingsButton()
	{
		BufferedImage icon = ImageUtil.loadImageResource(ItemSharePlugin.class, SETTINGS_ICON);
		ImageIcon imageIcon = new ImageIcon(icon);

		JButton button = new JButton();
		SwingUtil.removeButtonDecorations(button);
		button.setIcon(imageIcon);
		button.setToolTipText("Change your settings");
		button.setBackground(ColorScheme.DARK_GRAY_COLOR);
		button.setUI(new BasicButtonUI());

		button.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseEntered(java.awt.event.MouseEvent evt)
			{
				button.setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);
			}

			public void mouseExited(java.awt.event.MouseEvent evt)
			{
				button.setBackground(ColorScheme.DARK_GRAY_COLOR);
			}
		});

		return button;
	}
}
