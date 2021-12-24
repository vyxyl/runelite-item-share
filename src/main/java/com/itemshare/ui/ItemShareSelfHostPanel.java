package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_CLUSTER_DOMAIN;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_COLLECTION_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_DATABASE_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_PASSWORD;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_USERNAME;
import com.itemshare.db.ItemShareDBStatus;
import com.itemshare.service.ItemSharePanelService;
import com.itemshare.state.ItemShareState;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.FlatTextField;

public class ItemShareSelfHostPanel extends JPanel
{
	private final JLabel statusLabel = new JLabel();
	private final JButton connectButton;

	public ItemShareSelfHostPanel()
	{
		super(false);

		connectButton = createConnectButton();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(getSetting("MongoDB Database Name", CONFIG_MONGODB_DATABASE_NAME));
		add(getSetting("MongoDB Collection Name", CONFIG_MONGODB_COLLECTION_NAME));
		add(getSetting("MongoDB Cluster Domain", CONFIG_MONGODB_CLUSTER_DOMAIN));
		add(getSetting("MongoDB Username", CONFIG_MONGODB_USERNAME));
		add(getPasswordSetting());

		JPanel footer = createSelfHostFooter();

		add(footer);
	}

	public void updateStatus()
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
				break;
			case DISCONNECTED:
				statusLabel.setText("Connection Status: Disconnected");
				connectButton.setEnabled(true);
				break;
			default:
				statusLabel.setText("Connection Status: Unknown");
				connectButton.setEnabled(true);
		}

		repaint();
	}

	private JButton createConnectButton()
	{
		JButton button = new JButton();
		button.setText("Connect");
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

	private JPanel createSelfHostFooter()
	{
		JPanel spacer = new JPanel();
		ItemSharePanelService.setHeight(spacer, 15);
		statusLabel.setForeground(Color.WHITE);

		JPanel footer = new JPanel();
		footer.add(spacer);
		footer.add(connectButton);
		footer.add(statusLabel);

		ItemSharePanelService.setHeight(footer, 100);

		return footer;
	}

	private JPanel getPasswordSetting()
	{
		JLabel label = getLabel("MongoDB Password");
		ItemSharePanelService.setHeight(label, 30);

		JPasswordField value = new JPasswordField();
		value.setText(ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_MONGODB_PASSWORD));
		value.getDocument().addDocumentListener(getListener(value));
		ItemSharePanelService.setHeight(value, 30);

		return createStyledSetting(label, value);
	}

	private JPanel getSetting(String name, String key)
	{
		JLabel label = getLabel(name);
		ItemSharePanelService.setHeight(label, 30);

		FlatTextField value = getValue(key);
		ItemSharePanelService.setHeight(value, 30);

		return createStyledSetting(label, value);
	}

	private JPanel createStyledSetting(JComponent label, JComponent value)
	{
		JPanel setting = new JPanel();
		setting.setLayout(new BorderLayout());
		setting.add(label, BorderLayout.PAGE_START);
		setting.add(value, BorderLayout.CENTER);
		setting.add(Box.createVerticalGlue(), BorderLayout.PAGE_END);
		ItemSharePanelService.setHeight(setting, 60);

		return setting;
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
