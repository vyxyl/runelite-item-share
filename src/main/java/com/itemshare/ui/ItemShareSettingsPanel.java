package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GIM_ENABLED;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import static com.itemshare.constant.ItemShareConstants.ICON_CLOSE;
import static com.itemshare.constant.ItemShareConstants.ICON_EXIT;
import static com.itemshare.constant.ItemShareConstants.ICON_HELP;
import static com.itemshare.constant.ItemShareConstants.ICON_JOIN;
import static com.itemshare.constant.ItemShareConstants.ICON_SHARE;
import com.itemshare.service.ItemShareGroupIdService;
import com.itemshare.service.ItemShareLinkService;
import com.itemshare.service.ItemSharePanelService;
import com.itemshare.service.ItemShareUIService;
import com.itemshare.state.ItemShareState;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import net.runelite.client.ui.ColorScheme;

public class ItemShareSettingsPanel extends JPanel
{
	private enum ItemShareSetting
	{
		MAIN,
		SHARE_GROUP,
		JOIN_GROUP
	}

	private final ItemShareCopyGroupPanel shareGroupPanel;
	private final ItemShareJoinGroupPanel joinGroupPanel;

	private final ItemShareTitlePanel titlePanel;
	private final JButton shareGroupButton;
	private final JButton joinGroupButton;
	private final JButton leaveGroupButton;
	private final JButton helpButton;

	private final JCheckBox gimCheckbox;
	private final JTextPane gimNote;

	private ItemShareSetting currentSetting = ItemShareSetting.MAIN;

	public ItemShareSettingsPanel(Runnable onClose)
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

		ImageIcon closeIcon = ItemSharePanelService.loadIcon(ICON_CLOSE);
		titlePanel = new ItemShareTitlePanel("Item Share / Settings", closeIcon, onClose);

		shareGroupPanel = new ItemShareCopyGroupPanel(() -> onSettingClick(ItemShareSetting.MAIN));
		joinGroupPanel = new ItemShareJoinGroupPanel(() -> onSettingClick(ItemShareSetting.MAIN));

		ImageIcon joinIcon = ItemSharePanelService.loadIcon(ICON_JOIN);
		ImageIcon shareIcon = ItemSharePanelService.loadIcon(ICON_SHARE);
		ImageIcon leaveIcon = ItemSharePanelService.loadIcon(ICON_EXIT);
		ImageIcon helpIcon = ItemSharePanelService.loadIcon(ICON_HELP);

		joinGroupButton = ItemSharePanelService.getButton(joinIcon, "Join Group", () -> onSettingClick(ItemShareSetting.JOIN_GROUP));
		shareGroupButton = ItemSharePanelService.getButton(shareIcon, "Share Group", () -> onSettingClick(ItemShareSetting.SHARE_GROUP));
		leaveGroupButton = ItemSharePanelService.getButton(leaveIcon, "Leave Group", () -> showLeaveGroupPopup(onClose));
		helpButton = ItemSharePanelService.getButton(helpIcon, "Help", ItemShareLinkService::goToReadme);

		gimCheckbox = getGIMStorageCheckbox();
		gimNote = ItemSharePanelService.getCenteredTextPane("\n*** Only one GIM Storage is saved and viewable at a time");

		titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		joinGroupButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		shareGroupButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		leaveGroupButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		helpButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		gimCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
		gimNote.setAlignmentX(Component.LEFT_ALIGNMENT);

		ItemSharePanelService.setHeight(joinGroupButton, 30);
		ItemSharePanelService.setHeight(shareGroupButton, 30);
		ItemSharePanelService.setHeight(leaveGroupButton, 30);
		ItemSharePanelService.setHeight(helpButton, 30);
		ItemSharePanelService.setHeight(gimCheckbox, 30);
		ItemSharePanelService.setHeight(gimNote, 60);

		rebuild();
	}

	private JCheckBox getGIMStorageCheckbox()
	{
		boolean isGIMEnabled = Boolean.parseBoolean(ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_GIM_ENABLED));

		JCheckBox checkbox = new JCheckBox("Enable GIM Storage", isGIMEnabled);
		checkbox.setSelected(isGIMEnabled);
		checkbox.addItemListener(event -> {
			ItemShareState.configManager.setConfiguration(CONFIG_BASE, CONFIG_GIM_ENABLED, checkbox.isSelected());
			rebuild();
			ItemShareUIService.update();
		});

		return checkbox;
	}

	private void showLeaveGroupPopup(Runnable onClose)
	{
		JLabel line1 = new JLabel("This will assign you to a new group");
		JLabel line2 = new JLabel("Are you sure?");

		line1.setAlignmentX(Component.CENTER_ALIGNMENT);
		line2.setAlignmentX(Component.CENTER_ALIGNMENT);

		line1.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		line2.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		JPanel message = new JPanel();
		message.setLayout(new BoxLayout(message, BoxLayout.Y_AXIS));
		message.add(line1);
		message.add(line2);

		int result = JOptionPane.showConfirmDialog(
			this, message, "Leave my current group",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE);

		if (result == JOptionPane.OK_OPTION)
		{
			String id = ItemShareGroupIdService.getNewId();
			ItemShareState.configManager.setConfiguration(CONFIG_BASE, CONFIG_GROUP_ID, id);
			ItemShareState.groupId = id;

			ItemShareState.reset();

			onClose.run();
		}
	}

	private void onSettingClick(ItemShareSetting setting)
	{
		currentSetting = setting;
		rebuild();
	}

	private void rebuild()
	{
		removeAll();

		switch (currentSetting)
		{
			default:
			case MAIN:
				rebuildMain();
				break;
			case SHARE_GROUP:
				shareGroupPanel.reset();
				add(shareGroupPanel);
				shareGroupPanel.repaint();
				shareGroupPanel.revalidate();
				break;
			case JOIN_GROUP:
				joinGroupPanel.reset();
				add(joinGroupPanel);
				joinGroupPanel.repaint();
				joinGroupPanel.revalidate();
				break;
		}

		repaint();
		revalidate();
	}

	private void rebuildMain()
	{
		boolean isGIMEnabled = Boolean.parseBoolean(ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_GIM_ENABLED));

		add(titlePanel);
		add(ItemSharePanelService.getPadding(10));
		add(shareGroupButton);
		add(ItemSharePanelService.getPadding(10));
		add(joinGroupButton);
		add(ItemSharePanelService.getPadding(10));
		add(leaveGroupButton);
		add(ItemSharePanelService.getPadding(10));
		add(helpButton);
		add(ItemSharePanelService.getPadding(20));
		add(gimCheckbox);

		if (isGIMEnabled)
		{
			add(gimNote);
		}
	}
}