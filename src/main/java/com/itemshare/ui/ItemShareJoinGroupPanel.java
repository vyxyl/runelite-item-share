package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import static com.itemshare.constant.ItemShareConstants.ICON_BACK;
import static com.itemshare.constant.ItemShareConstants.ICON_RELOAD;
import static com.itemshare.constant.ItemShareConstants.ICON_SAVE_BUTTON;
import com.itemshare.service.ItemShareDBService;
import com.itemshare.service.ItemShareGroupIdService;
import com.itemshare.service.ItemSharePanelService;
import com.itemshare.state.ItemShareState;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.FlatTextField;

public class ItemShareJoinGroupPanel extends JPanel
{
	JTextPane statusTextPane;
	FlatTextField textField;

	protected ItemShareJoinGroupPanel(Runnable onBack)
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		ImageIcon backIcon = ItemSharePanelService.loadIcon(ICON_BACK);
		ItemShareTitlePanel titlePanel = new ItemShareTitlePanel("Item Share / Settings / Join", backIcon, onBack);

		JPanel buttons = getButtons();
		textField = getGroupIdTextField();
		JPanel scrollableTextField = ItemSharePanelService.getScrollableTextField(textField);
		JLabel textFieldLabel = new JLabel("Group ID");

		statusTextPane = ItemSharePanelService.getCenteredTextPane("");
		JTextPane textPane = ItemSharePanelService.getCenteredTextPane("Copy + paste a Group ID here\n\nPlayers with the same Group ID \nwill be able to see each other's items");

		titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollableTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
		statusTextPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		textPane.setAlignmentX(Component.LEFT_ALIGNMENT);

		ItemSharePanelService.setHeight(buttons, 30);
		ItemSharePanelService.setHeight(scrollableTextField, 30);
		ItemSharePanelService.setHeight(statusTextPane, 30);
		ItemSharePanelService.setHeight(textPane, 90);

		add(titlePanel);
		add(ItemSharePanelService.getPadding(10));
		add(textFieldLabel);
		add(scrollableTextField);
		add(ItemSharePanelService.getPadding(10));
		add(buttons);
		add(ItemSharePanelService.getPadding(10));
		add(statusTextPane);
		add(ItemSharePanelService.getPadding(10));
		add(textPane);
	}

	private JPanel getButtons()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JPanel padding = new JPanel();

		ImageIcon newIdIcon = ItemSharePanelService.loadIcon(ICON_RELOAD);
		JButton newIdButton = ItemSharePanelService.getButton(newIdIcon, null, () -> {
			statusTextPane.setText("");
			textField.setText(ItemShareGroupIdService.getNewId());
		});

		ImageIcon saveIcon = ItemSharePanelService.loadIcon(ICON_SAVE_BUTTON);
		JButton saveButton = ItemSharePanelService.getButton(saveIcon, "Join", () -> updateGroupId(textField.getText()));

		ItemSharePanelService.setSize(newIdButton, 30, 30);
		ItemSharePanelService.setSize(padding, 10, 30);
		ItemSharePanelService.setSize(saveButton, 185, 30);

		panel.add(newIdButton);
		panel.add(padding);
		panel.add(saveButton);

		return panel;
	}

	private FlatTextField getGroupIdTextField()
	{
		FlatTextField textField = new FlatTextField();
		textField.setEditable(true);
		textField.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		textField.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		textField.setText("");
		textField.addKeyListener(ItemSharePanelService.getKeyListener(() -> statusTextPane.setText("")));

		return textField;
	}

	public void reset()
	{
		textField.setText("");
		statusTextPane.setText("");
	}

	private void updateGroupId(String value)
	{
		String trimmedValue = value.trim();

		if (ItemShareGroupIdService.isValidId(trimmedValue))
		{
			ItemShareState.configManager.setConfiguration(CONFIG_BASE, CONFIG_GROUP_ID, trimmedValue);
			ItemShareState.groupId = trimmedValue;
			ItemShareDBService.load();

			statusTextPane.setText("Successfully Joined the Group!");
		}
		else
		{
			statusTextPane.setText("Invalid Group ID, please try again");
		}
	}
}
