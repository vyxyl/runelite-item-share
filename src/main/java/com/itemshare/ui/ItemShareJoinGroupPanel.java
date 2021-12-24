package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import static com.itemshare.constant.ItemShareConstants.ICON_CLOSE_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_INVALID_ID;
import static com.itemshare.constant.ItemShareConstants.ICON_SAVE_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_VALID_ID;
import com.itemshare.service.ItemSharePanelService;
import com.itemshare.state.ItemShareState;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.FlatTextField;

public class ItemShareJoinGroupPanel extends JPanel
{
	FlatTextField textField;

	ImageIcon validIcon = ItemSharePanelService.loadIcon(ICON_VALID_ID);
	ImageIcon invalidIcon = ItemSharePanelService.loadIcon(ICON_INVALID_ID);

	protected ItemShareJoinGroupPanel(Runnable onClose)
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		ImageIcon closeIcon = ItemSharePanelService.loadIcon(ICON_CLOSE_BUTTON);
		ItemShareTitlePanel titlePanel = new ItemShareTitlePanel("Settings - Join Group", closeIcon, onClose);

		textField = getGroupIdTextField();
		JPanel scrollableTextField = ItemSharePanelService.getScrollableTextField(textField);

		ImageIcon saveIcon = ItemSharePanelService.loadIcon(ICON_SAVE_BUTTON);
		JButton saveButton = ItemSharePanelService.getButton(saveIcon, "Join", () -> {});

		titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollableTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
		saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		ItemSharePanelService.setHeight(scrollableTextField, 30);
		ItemSharePanelService.setHeight(saveButton, 30);

		add(titlePanel);
		add(scrollableTextField);
		add(saveButton);
	}

	private FlatTextField getGroupIdTextField()
	{
		FlatTextField textField = new FlatTextField();
		textField.setEditable(true);
		textField.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		textField.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		textField.setText("");

		return textField;
	}

	public void reset()
	{
		textField.setText("");
	}

	private void updateGroupId(String value)
	{
		ItemShareState.configManager.setConfiguration(CONFIG_BASE, CONFIG_GROUP_ID, value);
	}
}
