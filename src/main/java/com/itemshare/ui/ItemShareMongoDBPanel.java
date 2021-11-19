package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_CLUSTER_DOMAIN;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_COLLECTION_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_DATABASE_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_PASSWORD;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_USERNAME;
import com.itemshare.db.ItemShareMongoDB;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.FlatTextField;

public class ItemShareMongoDBPanel extends JPanel
{
	private ConfigManager configManager;
	private ItemShareMongoDB db;
	private final JLabel status = new JLabel("Connection Status: Unknown");

	public ItemShareMongoDBPanel(ConfigManager configManager, ItemShareMongoDB db)
	{
		super(false);

		this.configManager = configManager;
		this.db = db;

		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		add(getSetting("Database Name", CONFIG_MONGODB_DATABASE_NAME));
		add(getSetting("Collection Name", CONFIG_MONGODB_COLLECTION_NAME));
		add(getSetting("Cluster Domain", CONFIG_MONGODB_CLUSTER_DOMAIN));
		add(getSetting("Username", CONFIG_MONGODB_USERNAME));
		add(getPasswordField());

		JButton button = new JButton();
		button.setText("Connect");
		button.setBackground(ColorScheme.DARK_GRAY_COLOR);
		button.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH / 2, 30));
		button.setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH / 2, 30));
		button.setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH / 2, 30));

		button.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseEntered(java.awt.event.MouseEvent evt)
			{
				db.reconnect();
			}

			public void mouseExited(java.awt.event.MouseEvent evt)
			{
				db.reconnect();
			}
		});

		status.setForeground(Color.WHITE);

		JPanel footer = new JPanel();
		footer.add(button);

		add(footer);
	}

	public void update(boolean isConnected)
	{
		if (isConnected)
		{
			status.setText("Connection Status: Good");
		}
		else
		{
			status.setText("Connection Status: None");
		}

		repaint();
	}

	private JPanel getPasswordField()
	{
		JPanel setting = new JPanel();
		setting.setLayout(new BoxLayout(setting, BoxLayout.PAGE_AXIS));
		setting.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		JLabel label = getLabel("Password");
		JPasswordField value = new JPasswordField();
		value.setText(configManager.getConfiguration(CONFIG_BASE, CONFIG_MONGODB_PASSWORD));
		setAsLine(value);

		setting.add(label);
		setting.add(value);

		return setting;
	}

	private JPanel getSetting(String name, String key)
	{
		JPanel setting = new JPanel();
		setting.setLayout(new BoxLayout(setting, BoxLayout.PAGE_AXIS));
		setting.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

		JLabel label = getLabel(name);
		FlatTextField value = getValue(key);

		setting.add(label);
		setting.add(value);

		return setting;
	}

	private JLabel getLabel(String name)
	{
		JLabel label = new JLabel(name);
		label.setForeground(Color.WHITE);
		setAsLine(label);

		return label;
	}

	private FlatTextField getValue(String key)
	{
		FlatTextField field = new FlatTextField();
		setAsLine(field);

		field.setText(configManager.getConfiguration(CONFIG_BASE, key));
		field.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		field.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		field.getDocument().addDocumentListener(getListener(key, field));

		return field;
	}

	private void setAsLine(JComponent component)
	{
		component.setAlignmentX(Component.LEFT_ALIGNMENT);
		component.setLayout(new BoxLayout(component, BoxLayout.LINE_AXIS));
		component.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		component.setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		component.setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
	}

	private DocumentListener getListener(String key, FlatTextField field)
	{
		return new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				updateSetting(key, field.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateSetting(key, field.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				updateSetting(key, field.getText());
			}
		};
	}

	private void updateSetting(String key, String value)
	{
		configManager.setConfiguration(CONFIG_BASE, key, value);
	}
}
