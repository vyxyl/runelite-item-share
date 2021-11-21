package com.itemshare.ui;

import com.itemshare.ItemSharePlugin;
import static com.itemshare.constant.ItemShareConstants.ICON_CLOSE_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_SETTINGS_BUTTON;
import com.itemshare.db.ItemShareMongoDB;
import com.itemshare.db.ItemShareMongoDBStatus;
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
	private final ItemShareTitlePanel titlePanel = new ItemShareTitlePanel();
	private final JButton settingsButton = createSettingsButton();
	private final ItemShareMongoDBPanel mongoDbPanel;
	private final ItemShareNavigationPanel navPanel = new ItemShareNavigationPanel();
	private final ImageIcon settingsIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_SETTINGS_BUTTON));
	private final ImageIcon closeIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_CLOSE_BUTTON));

	public ItemSharePanel(ConfigManager configManager, ItemShareMongoDB db)
	{
		super(false);

		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BorderLayout());

		titlePanel.add(getControls());
		mongoDbPanel = new ItemShareMongoDBPanel(configManager, db, this::showSharedItems);

		add(titlePanel, BorderLayout.PAGE_START);
		add(mongoDbPanel, BorderLayout.CENTER);
		add(navPanel, BorderLayout.CENTER);

		showMongoDB();
	}

	public void update(ItemManager itemManager, ItemShareData data, ItemShareMongoDBStatus status)
	{
		mongoDbPanel.update(status);
		navPanel.update(itemManager, data);
	}

	private void showSharedItems()
	{
		settingsButton.setIcon(settingsIcon);
		titlePanel.setTitleName("Item Share");
		add(navPanel);
		remove(mongoDbPanel);

		repaintAll();
	}

	private void showMongoDB()
	{
		settingsButton.setIcon(closeIcon);
		titlePanel.setTitleName("MongoDB Settings");
		remove(navPanel);
		add(mongoDbPanel);

		repaintAll();
	}

	private void repaintAll()
	{
		navPanel.revalidate();
		navPanel.repaint();

		mongoDbPanel.revalidate();
		mongoDbPanel.repaint();

		titlePanel.repaint();

		revalidate();
		repaint();
	}

	private JPanel getControls()
	{
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
			showMongoDB();
		}
		else
		{
			showSharedItems();
		}
	}
}
