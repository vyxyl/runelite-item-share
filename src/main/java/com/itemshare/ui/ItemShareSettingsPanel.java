package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.ICON_CLOSE_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_JOIN;
import static com.itemshare.constant.ItemShareConstants.ICON_SHARE;
import com.itemshare.service.ItemSharePanelService;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.runelite.client.ui.ColorScheme;

public class ItemShareSettingsPanel extends JPanel
{
	private enum ItemShareSetting
	{
		MAIN,
		SHARE_GROUP,
		JOIN_GROUP,
	}

	private final ItemShareCopyGroupPanel shareGroupPanel;
	private final ItemShareJoinGroupPanel joinGroupPanel;

	private final ItemShareTitlePanel titlePanel;
	private final JButton shareGroupButton;
	private final JButton joinGroupButton;

	private ItemShareSetting currentSetting = ItemShareSetting.MAIN;

	public ItemShareSettingsPanel(Runnable onClose)
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

		ImageIcon closeIcon = ItemSharePanelService.loadIcon(ICON_CLOSE_BUTTON);
		titlePanel = new ItemShareTitlePanel("Item Share / Settings", closeIcon, onClose);

		shareGroupPanel = new ItemShareCopyGroupPanel(() -> onSettingClick(ItemShareSetting.MAIN));
		joinGroupPanel = new ItemShareJoinGroupPanel(() -> onSettingClick(ItemShareSetting.MAIN));

		ImageIcon joinIcon = ItemSharePanelService.loadIcon(ICON_JOIN);
		ImageIcon shareIcon = ItemSharePanelService.loadIcon(ICON_SHARE);

		joinGroupButton = ItemSharePanelService.getButton(joinIcon, "Join Group", () -> onSettingClick(ItemShareSetting.JOIN_GROUP));
		shareGroupButton = ItemSharePanelService.getButton(shareIcon, "Share Group", () -> onSettingClick(ItemShareSetting.SHARE_GROUP));

		titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		joinGroupButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		shareGroupButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		ItemSharePanelService.setHeight(joinGroupButton, 30);
		ItemSharePanelService.setHeight(shareGroupButton, 30);

		rebuild();
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
		add(titlePanel);
		add(ItemSharePanelService.getPadding(10));
		add(shareGroupButton);
		add(ItemSharePanelService.getPadding(10));
		add(joinGroupButton);
	}
}
