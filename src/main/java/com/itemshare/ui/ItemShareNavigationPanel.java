package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.ICON_SETTINGS_BUTTON;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.service.ItemSharePanelService;
import com.itemshare.state.ItemShareState;
import java.awt.event.ItemEvent;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.Border;
import net.runelite.client.ui.ColorScheme;
import org.apache.commons.lang3.StringUtils;

public class ItemShareNavigationPanel extends JPanel
{
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
		updateData();

		playerDropdown.update();
		playerDropdown.setItemListener(this::onPlayerSelected);
	}

	private void onPlayerSelected(ItemEvent event)
	{
		if (event.getStateChange() == ItemEvent.SELECTED)
		{
			updateData();
		}
	}

	private void updateData()
	{
		ItemSharePlayer player = findPlayer();
		updateMessagePanel.update(player.getUpdatedDate());
		playerPanel.update(player);
	}

	private ItemSharePlayer findPlayer()
	{
		ItemSharePlayer player = playerDropdown.getSelectedPlayer();

		if (ItemShareState.data == null || ItemShareState.data.getPlayers() == null)
		{
			return player;
		}
		else
		{
			return findPlayerByName(player.getName()).orElse(player);
		}
	}

	private Optional<ItemSharePlayer> findPlayerByName(String name)
	{
		return ItemShareState.data.getPlayers().stream().filter(p -> StringUtils.equals(p.getName(), name)).findFirst();
	}
}
