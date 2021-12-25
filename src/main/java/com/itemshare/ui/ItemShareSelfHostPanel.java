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
import java.util.concurrent.Callable;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import lombok.SneakyThrows;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.FlatTextField;

public class ItemShareSelfHostPanel extends JPanel
{
	private final JLabel statusLabel = new JLabel();
	private final JButton connectButton;

	public ItemShareSelfHostPanel()
	{
		super(false);

		statusLabel.setForeground(Color.WHITE);
		connectButton = getConnectButton();
		JPanel footer = getFooter();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(getSetting("MongoDB Database Name", CONFIG_MONGODB_DATABASE_NAME));
		add(getSetting("MongoDB Collection Name", CONFIG_MONGODB_COLLECTION_NAME));
		add(getSetting("MongoDB Cluster Domain", CONFIG_MONGODB_CLUSTER_DOMAIN));
		add(getSetting("MongoDB Username", CONFIG_MONGODB_USERNAME));
		add(getPasswordSetting("MongoDB Password", CONFIG_MONGODB_PASSWORD));
		add(footer);
	}

	public void updateStatus()
	{
		ItemShareDBStatus status = ItemShareState.db.getStatus();

		connectButton.setEnabled(true);

		switch (status)
		{
			case UNINITIALIZED:
				updateStatus("Uninitialized");
				break;
			case LOADING:
				connectButton.setEnabled(false);
				updateStatus("Loading...");
				break;
			case CONNECTED:
				updateStatus("Connected");
				break;
			case DISCONNECTED:
				updateStatus("Disconnected");
				break;
			default:
				updateStatus("Unknown");
				break;
		}

		repaint();
	}

	private void updateStatus(String text)
	{
		statusLabel.setText("Connection Status: " + text);
	}

	private JButton getConnectButton()
	{
		JButton button = ItemSharePanelService.getButton(null, "Connect", ItemShareState.mongoDB::reconnect);
		ItemSharePanelService.setHeight(button, 30);
		return button;
	}

	private JPanel getFooter()
	{
		JPanel spacer = ItemSharePanelService.getSpacer(15);
		JPanel footer = new JPanel();

		footer.add(spacer);
		footer.add(connectButton);
		footer.add(statusLabel);

		ItemSharePanelService.setHeight(footer, 100);

		return footer;
	}

	private JPanel getPasswordSetting(String name, String key)
	{
		return getStyledSetting(getSettingLabel(name), getPasswordValue(key));
	}

	private JPanel getSetting(String name, String key)
	{
		return getStyledSetting(getSettingLabel(name), getSettingValue(key));
	}

	private JLabel getSettingLabel(String name)
	{
		JLabel label = new JLabel(name);
		label.setForeground(Color.WHITE);

		return label;
	}

	private FlatTextField getSettingValue(String key)
	{
		FlatTextField field = new FlatTextField();
		field.setText(ItemShareState.configManager.getConfiguration(CONFIG_BASE, key));
		field.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		field.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		field.getDocument().addDocumentListener(getListener(key, field::getText));

		return field;
	}

	private JPasswordField getPasswordValue(String key)
	{
		JPasswordField value = new JPasswordField();
		value.setText(ItemShareState.configManager.getConfiguration(CONFIG_BASE, key));
		value.getDocument().addDocumentListener(getListener(key, () -> new String(value.getPassword())));

		return value;
	}

	private JPanel getStyledSetting(JComponent label, JComponent value)
	{
		JPanel setting = new JPanel();
		setting.setLayout(new BorderLayout());
		setting.add(label, BorderLayout.PAGE_START);
		setting.add(value, BorderLayout.CENTER);
		setting.add(Box.createVerticalGlue(), BorderLayout.PAGE_END);

		ItemSharePanelService.setHeight(label, 30);
		ItemSharePanelService.setHeight(value, 30);
		ItemSharePanelService.setHeight(setting, 60);

		return setting;
	}

	private DocumentListener getListener(String key, Callable<String> getValue)
	{
		return new DocumentListener()
		{
			@SneakyThrows
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				updateSetting(key, getValue.call());
			}

			@SneakyThrows
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateSetting(key, getValue.call());
			}

			@SneakyThrows
			@Override
			public void changedUpdate(DocumentEvent e)
			{
				updateSetting(key, getValue.call());
			}
		};
	}

	private void updateSetting(String key, String value)
	{
		ItemShareState.configManager.setConfiguration(CONFIG_BASE, key, value);
	}
}
