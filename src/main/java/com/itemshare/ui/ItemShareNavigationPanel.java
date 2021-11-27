package com.itemshare.ui;

import com.itemshare.model.ItemSharePlayer;
import com.itemshare.state.ItemShareState;
import java.awt.event.ItemEvent;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.apache.commons.lang3.StringUtils;

public class ItemShareNavigationPanel extends JPanel
{
	private final ItemSharePlayerDropdownPanel playerDropdown = new ItemSharePlayerDropdownPanel();
	private final ItemSharePlayerPanel playerPanel = new ItemSharePlayerPanel();
	private final ItemShareUpdateMessagePanel updateMessagePanel = new ItemShareUpdateMessagePanel();

	protected ItemShareNavigationPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		Border border = BorderFactory.createEmptyBorder(10, 0, 0, 0);

		playerDropdown.setBorder(border);
		updateMessagePanel.setBorder(border);
		playerPanel.setBorder(border);

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
		ItemSharePlayer selectedPlayer = playerDropdown.getSelectedPlayer();
		ItemSharePlayer player = findPlayer(selectedPlayer);
		updateMessagePanel.update(player.getUpdatedDate());
		playerPanel.update(player);
	}

	private ItemSharePlayer findPlayer(ItemSharePlayer player)
	{
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
