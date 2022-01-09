package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.ICON_SETTINGS;
import static com.itemshare.constant.ItemShareConstants.SELECT_A_PLAYER;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.service.ItemShareAPIService;
import com.itemshare.service.ItemShareLoadService;
import com.itemshare.service.ItemSharePanelService;
import java.awt.event.ItemEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.Border;
import net.runelite.client.ui.ColorScheme;
import org.apache.commons.lang3.StringUtils;

public class ItemShareContentPanel extends JPanel
{
	private final ItemSharePlayerDropdownPanel playerDropdown = new ItemSharePlayerDropdownPanel();
	private final ItemShareUpdateMessagePanel updateMessagePanel = new ItemShareUpdateMessagePanel();
	private final ItemSharePlayerItemsPanel playerItemsPanel = new ItemSharePlayerItemsPanel(this::updateGIM);

	protected ItemShareContentPanel(Runnable goToSettings)
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		ImageIcon settingsIcon = ItemSharePanelService.loadIcon(ICON_SETTINGS);
		ItemShareTitlePanel titlePanel = new ItemShareTitlePanel("Item Share", settingsIcon, goToSettings);

		Border border = BorderFactory.createEmptyBorder(0, 0, 10, 0);

		playerDropdown.setBorder(border);
		updateMessagePanel.setBorder(border);
		playerItemsPanel.setBorder(border);

		add(titlePanel);
		add(playerDropdown);
		add(updateMessagePanel);
		add(playerItemsPanel);
	}

	public void update()
	{
		renderPlayerData();

		playerDropdown.update();
		playerDropdown.setItemListener(this::onPlayerSelected);
	}

	public void updateGIM(Boolean isGIMSelected)
	{
		updateMessagePanel.updateGIM(isGIMSelected);
	}

	private void onPlayerSelected(ItemEvent event)
	{
		if (event.getStateChange() == ItemEvent.SELECTED)
		{
			renderPlayerData();
		}
	}

	private void renderPlayerData()
	{
		String playerName = playerDropdown.getSelectedPlayerName();

		if (StringUtils.equals(playerName, SELECT_A_PLAYER))
		{
			loadEmptyPlayer(playerName);
		}
		else
		{
			ItemShareAPIService.getPlayer(playerName, this::loadPlayer);
		}
	}

	private void loadPlayer(ItemSharePlayer player)
	{
		updateMessagePanel.updatePlayer(player);
		playerItemsPanel.update(player);
	}

	private void loadEmptyPlayer(String playerName)
	{
		updateMessagePanel.updatePlayer(null);
		playerItemsPanel.update(ItemShareLoadService.getEmptyPlayer(playerName));
	}
}