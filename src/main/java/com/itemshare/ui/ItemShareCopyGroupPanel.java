package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import static com.itemshare.constant.ItemShareConstants.ICON_CLOSE_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_COPY_BUTTON;
import com.itemshare.service.ItemSharePanelService;
import com.itemshare.state.ItemShareState;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.FlatTextField;

public class ItemShareCopyGroupPanel extends JPanel
{
	public ItemShareCopyGroupPanel(Runnable onClose)
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);


		ImageIcon closeIcon = ItemSharePanelService.loadIcon(ICON_CLOSE_BUTTON);
		ItemShareTitlePanel titlePanel = new ItemShareTitlePanel("Settings - Share Group", closeIcon, onClose);

		FlatTextField textField = getGroupIdTextField();
		JPanel scrollableTextField = ItemSharePanelService.getScrollableTextField(textField);

		ImageIcon copyIcon = ItemSharePanelService.loadIcon(ICON_COPY_BUTTON);
		JButton copyButton = ItemSharePanelService.getButton(copyIcon, "Copy to Clipboard", () -> copyId(textField));

		titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollableTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
		copyButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		ItemSharePanelService.setHeight(scrollableTextField, 30);
		ItemSharePanelService.setHeight(copyButton, 30);

		add(titlePanel);
		add(scrollableTextField);
		add(copyButton);
	}

	private FlatTextField getGroupIdTextField()
	{
		FlatTextField textField = new FlatTextField();
		textField.setEditable(false);
		textField.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		textField.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		textField.setText(ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_GROUP_ID));

		return textField;
	}

	private void copyId(FlatTextField textField)
	{
		StringSelection stringSelection = new StringSelection(textField.getTextField().getText());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
}
