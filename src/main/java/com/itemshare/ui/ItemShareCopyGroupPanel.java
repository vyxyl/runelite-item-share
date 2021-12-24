package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import static com.itemshare.constant.ItemShareConstants.ICON_BACK;
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
	public ItemShareCopyGroupPanel(Runnable onBack)
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		ImageIcon backIcon = ItemSharePanelService.loadIcon(ICON_BACK);
		ItemShareTitlePanel titlePanel = new ItemShareTitlePanel("Item Share / Settings / Share", backIcon, onBack);

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
		add(ItemSharePanelService.getPadding(10));
		add(scrollableTextField);
		add(ItemSharePanelService.getPadding(10));
		add(copyButton);
		add(ItemSharePanelService.getPadding(10));
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
