package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import static com.itemshare.constant.ItemShareConstants.ICON_BACK;
import static com.itemshare.constant.ItemShareConstants.ICON_COPY;
import com.itemshare.service.ItemSharePanelService;
import com.itemshare.state.ItemShareState;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.FlatTextField;

public class ItemShareCopyGroupPanel extends JPanel
{
	private final FlatTextField textField;

	public ItemShareCopyGroupPanel(Runnable onBack)
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		ImageIcon backIcon = ItemSharePanelService.loadIcon(ICON_BACK);
		ItemShareTitlePanel titlePanel = new ItemShareTitlePanel("Item Share / Settings / Share", backIcon, onBack);

		JLabel textFieldLabel = new JLabel("Group ID");
		textField = getGroupIdTextField();
		JPanel scrollableTextField = ItemSharePanelService.getScrollableTextField(textField);

		ImageIcon copyIcon = ItemSharePanelService.loadIcon(ICON_COPY);
		JButton copyButton = ItemSharePanelService.getButton(copyIcon, "Copy to Clipboard", () -> copyId(textField));
		JTextPane textPane = ItemSharePanelService.getCenteredTextPane("Share your Group ID with other players\n\nPlayers with the same Group ID \nwill be able to see each other's items");

		titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollableTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
		copyButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		textPane.setAlignmentX(Component.LEFT_ALIGNMENT);

		ItemSharePanelService.setHeight(scrollableTextField, 30);
		ItemSharePanelService.setHeight(copyButton, 30);
		ItemSharePanelService.setHeight(textPane, 90);

		add(titlePanel);
		add(ItemSharePanelService.getPadding(10));
		add(textFieldLabel);
		add(scrollableTextField);
		add(ItemSharePanelService.getPadding(10));
		add(copyButton);
		add(ItemSharePanelService.getPadding(10));
		add(textPane);
	}

	public void reset()
	{
		loadId(textField);
	}

	private void loadId(FlatTextField textField)
	{
		textField.setText(ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_GROUP_ID));
	}

	private FlatTextField getGroupIdTextField()
	{
		FlatTextField textField = new FlatTextField();
		textField.setEditable(false);
		textField.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		textField.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		loadId(textField);

		return textField;
	}

	private void copyId(FlatTextField textField)
	{
		StringSelection stringSelection = new StringSelection(textField.getTextField().getText());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
}