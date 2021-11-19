package com.itemshare.ui;

import com.itemshare.ItemSharePlugin;
import static com.itemshare.constant.ItemShareConstants.ICON_CLOSE_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_SETTINGS_BUTTON;
import com.itemshare.db.ItemShareMongoDB;
import com.itemshare.model.ItemShareData;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicButtonUI;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;

public class ItemSharePanel extends PluginPanel
{
	private final ItemShareMongoDBPanel settingsPanel;
	private final ItemShareNavigationPanel playerPanel = new ItemShareNavigationPanel();
	private final ImageIcon settingsIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_SETTINGS_BUTTON));
	private final ImageIcon closeIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_CLOSE_BUTTON));

	public ItemSharePanel(ConfigManager configManager, ItemShareMongoDB db)
	{
		super(false);

		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BorderLayout());

		settingsPanel = new ItemShareMongoDBPanel(configManager, db);

		JPanel controls = getControls();

		ItemShareTitlePanel titlePanel = new ItemShareTitlePanel();
		titlePanel.add(controls);

		add(titlePanel, BorderLayout.NORTH);
		add(settingsPanel, BorderLayout.CENTER);
		add(playerPanel, BorderLayout.CENTER);

		showSettings();
	}

	public void update(ItemManager itemManager, ItemShareData data, boolean isConnected)
	{
		settingsPanel.update(isConnected);
		playerPanel.update(itemManager, data);

		if (isConnected)
		{
			showData();
		}
		else
		{
			showSettings();
		}
	}

	private void showData()
	{
		playerPanel.show();
		settingsPanel.hide();

		repaintAll();
	}

	private void showSettings()
	{
		playerPanel.hide();
		settingsPanel.show();

		repaintAll();
	}

	private void repaintAll()
	{
		playerPanel.revalidate();
		playerPanel.repaint();

		settingsPanel.revalidate();
		settingsPanel.repaint();

		revalidate();
		repaint();
	}

	private JPanel getControls()
	{
		JButton settingsButton = createSettingsButton();
		JPanel controls = new JPanel(new GridLayout(1, 3, 10, 0));
		controls.setBackground(ColorScheme.DARK_GRAY_COLOR);
		controls.add(settingsButton);
		return controls;
	}

	private JButton createSettingsButton()
	{
		JButton button = new JButton();
		SwingUtil.removeButtonDecorations(button);
		button.setIcon(settingsIcon);
		button.setToolTipText("Database Settings");
		button.setBackground(ColorScheme.DARK_GRAY_COLOR);
		button.setUI(new BasicButtonUI());

		button.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseEntered(java.awt.event.MouseEvent event)
			{
				button.setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);
			}

			public void mouseExited(java.awt.event.MouseEvent event)
			{
				button.setBackground(ColorScheme.DARK_GRAY_COLOR);
			}

			public void mousePressed(java.awt.event.MouseEvent event)
			{
				toggle(button);
			}
		});

		return button;
	}

	private void toggle(JButton button)
	{
		if (button.getIcon() == settingsIcon)
		{
			button.setIcon(closeIcon);
			showSettings();
		}
		else
		{
			button.setIcon(settingsIcon);
			showData();
		}
	}
}
