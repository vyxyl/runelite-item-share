package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.ICON_SETTINGS_BUTTON;
import static com.itemshare.constant.ItemShareConstants.OPTION_NO_PLAYER;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.service.ItemShareAPIService;
import com.itemshare.service.ItemSharePanelService;
import com.itemshare.service.ItemSharePlayerService;
import com.itemshare.state.ItemShareState;
import java.awt.event.ItemEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.Border;
import net.runelite.client.ui.ColorScheme;
import org.apache.commons.lang3.StringUtils;

public class ItemShareNavigationPanel extends JPanel
{
	private final ItemSharePlayer emptyPlayer = ItemSharePlayerService.getUnselectedPlayer();
	private final ItemSharePlayerDropdownPanel playerDropdown = new ItemSharePlayerDropdownPanel();
	private final ItemSharePlayerPanel playerPanel = new ItemSharePlayerPanel();
	private final ItemShareUpdateMessagePanel updateMessagePanel = new ItemShareUpdateMessagePanel();

	protected ItemShareNavigationPanel(Runnable runnable)
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		ImageIcon settingsIcon = ItemSharePanelService.loadIcon(ICON_SETTINGS_BUTTON);
		ItemShareTitlePanel titlePanel = new ItemShareTitlePanel("Item Share", settingsIcon, runnable);

		Border border = BorderFactory.createEmptyBorder(0, 0, 10, 0);

		playerDropdown.setBorder(border);
		updateMessagePanel.setBorder(border);
		playerPanel.setBorder(border);

		add(titlePanel);
		add(playerDropdown);
		add(updateMessagePanel);
		add(playerPanel);
	}

	public void update()
	{
		renderPlayerData();

		playerDropdown.update();
		playerDropdown.setItemListener(this::onPlayerSelected);
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

		if (StringUtils.equals(playerName, OPTION_NO_PLAYER))
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
		if (player.getName().equals(ItemShareState.playerName))
		{
			updateMessagePanel.clearMessage();
		}
		else
		{
			updateMessagePanel.updatePanel(player.getUpdatedDate());
		}

		playerPanel.update(player);
	}

	private void loadEmptyPlayer(String playerName)
	{
		updateMessagePanel.updatePanel(null);
		playerPanel.update(ItemSharePlayerService.getEmptyPlayer(playerName));
	}
}