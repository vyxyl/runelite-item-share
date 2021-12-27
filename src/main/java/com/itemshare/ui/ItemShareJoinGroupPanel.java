package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import static com.itemshare.constant.ItemShareConstants.ICON_BACK;
import static com.itemshare.constant.ItemShareConstants.ICON_SAVE_BUTTON;
import com.itemshare.service.ItemShareDBService;
import com.itemshare.service.ItemShareGroupIdService;
import com.itemshare.service.ItemSharePanelService;
import com.itemshare.state.ItemShareState;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

		textField = getGroupIdTextField();
		JPanel scrollableTextField = ItemSharePanelService.getScrollableTextField(textField);

		ImageIcon saveIcon = ItemSharePanelService.loadIcon(ICON_SAVE_BUTTON);
		JButton saveButton = ItemSharePanelService.getButton(saveIcon, "Save", () -> updateGroupId(textField.getText()));
		statusTextPane = ItemSharePanelService.getCenteredTextPane("");
		JTextPane textPane = ItemSharePanelService.getCenteredTextPane("Copy + paste a Group ID here\n\nPlayers with the same Group ID \nwill be able to see each other's items");

		titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollableTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
		saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		statusTextPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		textPane.setAlignmentX(Component.LEFT_ALIGNMENT);

		ItemSharePanelService.setHeight(scrollableTextField, 30);
		ItemSharePanelService.setHeight(saveButton, 30);
		ItemSharePanelService.setHeight(statusTextPane, 30);
		ItemSharePanelService.setHeight(textPane, 90);

		add(titlePanel);
		add(ItemSharePanelService.getPadding(10));
		add(scrollableTextField);
		add(ItemSharePanelService.getPadding(10));
		add(saveButton);
		add(ItemSharePanelService.getPadding(10));
		add(statusTextPane);
		add(ItemSharePanelService.getPadding(10));
		add(textPane);
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
