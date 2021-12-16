package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_CLUSTER_DOMAIN;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_COLLECTION_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_DATABASE_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_ENABLED;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_PASSWORD;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_USERNAME;
import com.itemshare.db.ItemShareDBStatus;
import com.itemshare.state.ItemShareState;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.FlatTextField;

public class ItemShareDBPanel extends JPanel
{
	private final JLabel statusLabel = new JLabel();
	private final JButton connectButton;
	private final Runnable onConnected;
	private ItemShareDBStatus lastStatus;
	private final JCheckBox selfHostCheckbox;
	private final JPanel selfHostPanel;

	public ItemShareDBPanel(Runnable onConnected)
	{
		super(false);

		this.onConnected = onConnected;

		setBackground(ColorScheme.DARK_GRAY_COLOR);

		connectButton = createConnectButton();
		selfHostPanel = createSelfHostPanel();

		boolean isSelfHostEnabled = Boolean.parseBoolean(ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_MONGODB_ENABLED));

		selfHostCheckbox = new JCheckBox("Self-host your own database", isSelfHostEnabled);
		selfHostCheckbox.addItemListener(event -> onSelfHostSelection());
		setFullWidthStyling(selfHostCheckbox, 30);

		ItemShareGroupIDPanel groupId = new ItemShareGroupIDPanel();

		add(groupId);
		add(selfHostCheckbox);

		if (isSelfHostEnabled)
		{
			add(selfHostPanel);
		}
	}

	private void onSelfHostSelection()
	{
		boolean isSelected = selfHostCheckbox.isSelected();
		ItemShareState.configManager.setConfiguration(CONFIG_BASE, CONFIG_MONGODB_ENABLED, isSelected);

		if (isSelected)
		{
			ItemShareState.db = ItemShareState.mongoDB;
			add(selfHostPanel);
		}
		else
		{
			ItemShareState.db = ItemShareState.dedicatedDB;
			remove(selfHostPanel);
		}

		selfHostPanel.repaint();
		selfHostPanel.revalidate();

		repaint();
		revalidate();
	}

	private JPanel createSelfHostPanel()
	{
		JPanel footer = createSelfHostFooter();
		setFullWidthStyling(footer, 100);

		JPanel selfHostDb = new JPanel();
		selfHostDb.setLayout(new BoxLayout(selfHostDb, BoxLayout.PAGE_AXIS));

		selfHostDb.add(getSetting("MongoDB Database Name", CONFIG_MONGODB_DATABASE_NAME));
		selfHostDb.add(getSetting("MongoDB Collection Name", CONFIG_MONGODB_COLLECTION_NAME));
		selfHostDb.add(getSetting("MongoDB Cluster Domain", CONFIG_MONGODB_CLUSTER_DOMAIN));
		selfHostDb.add(getSetting("MongoDB Username", CONFIG_MONGODB_USERNAME));
		selfHostDb.add(getPasswordSetting());
		selfHostDb.add(footer);

		return selfHostDb;
	}

	private JPanel createSelfHostFooter()
	{
		JPanel spacer = new JPanel();
		setFullWidthStyling(spacer, 15);
		statusLabel.setForeground(Color.WHITE);

		JPanel footer = new JPanel();
		footer.add(spacer);
		footer.add(connectButton);
		footer.add(statusLabel);
		return footer;
	}

	private JButton createConnectButton()
	{
		JButton button = new JButton();
		button.setText("Re-Connect");
		button.setBackground(ColorScheme.DARK_GRAY_COLOR);
		button.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mousePressed(java.awt.event.MouseEvent evt)
			{
				statusLabel.setText("Connection Status: Loading...");
				ItemShareState.db.reconnect();
			}
		});

		return button;
	}

	public void update()
	{
		ItemShareDBStatus status = ItemShareState.db.getStatus();

		switch (status)
		{
			case UNINITIALIZED:
				statusLabel.setText("Connection Status: Uninitialized");
				connectButton.setEnabled(true);
				break;
			case LOADING:
				statusLabel.setText("Connection Status: Loading...");
				connectButton.setEnabled(false);
				break;
			case CONNECTED:
				statusLabel.setText("Connection Status: Connected");
				connectButton.setEnabled(true);
				if (lastStatus != ItemShareDBStatus.CONNECTED)
				{
					onConnected.run();
				}
				break;
			case DISCONNECTED:
				statusLabel.setText("Connection Status: Disconnected");
				connectButton.setEnabled(true);
				break;
			default:
				statusLabel.setText("Connection Status: Unknown");
				connectButton.setEnabled(true);
		}

		lastStatus = status;

		repaint();
	}

	private JPanel getPasswordSetting()
	{
		JLabel label = getLabel("MongoDB Password");
		setFullWidthStyling(label, 30);

		JPasswordField value = new JPasswordField();
		value.setText(ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_MONGODB_PASSWORD));
		value.getDocument().addDocumentListener(getListener(value));
		setFullWidthStyling(value, 30);

		return createStyledSetting(label, value);
	}

	private JPanel getSetting(String name, String key)
	{
		JLabel label = getLabel(name);
		setFullWidthStyling(label, 30);

		FlatTextField value = getValue(key);
		setFullWidthStyling(value, 30);

		return createStyledSetting(label, value);
	}

	private JPanel createStyledSetting(JComponent label, JComponent value)
	{
		JPanel setting = new JPanel();
		setting.setLayout(new BorderLayout());
		setting.add(label, BorderLayout.PAGE_START);
		setting.add(value, BorderLayout.CENTER);
		setting.add(Box.createVerticalGlue(), BorderLayout.PAGE_END);
		setFullWidthStyling(setting, 60);

		return setting;
	}

	private void setFullWidthStyling(JComponent component, int height)
	{
		component.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH, height));
		component.setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, height));
		component.setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, height));
	}

	private JLabel getFullLabel(String name)
	{
		JLabel label = getLabel(name);
		setFullWidthStyling(label, 30);

		return label;
	}

	private JLabel getLabel(String name)
	{
		JLabel label = new JLabel(name);
		label.setForeground(Color.WHITE);

		return label;
	}

	private FlatTextField getValue(String key)
	{
		FlatTextField field = new FlatTextField();
		field.setText(ItemShareState.configManager.getConfiguration(CONFIG_BASE, key));
		field.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		field.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		field.getDocument().addDocumentListener(getListener(key, field));

		return field;
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

	private DocumentListener getListener(JPasswordField field)
	{
		return new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				updateSetting(CONFIG_MONGODB_PASSWORD, new String(field.getPassword()));
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateSetting(CONFIG_MONGODB_PASSWORD, new String(field.getPassword()));
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				updateSetting(CONFIG_MONGODB_PASSWORD, new String(field.getPassword()));
			}
		};
	}

	private void updateSetting(String key, String value)
	{
		ItemShareState.configManager.setConfiguration(CONFIG_BASE, key, value);
	}
}
