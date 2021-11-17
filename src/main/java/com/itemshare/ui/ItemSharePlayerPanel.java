package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.NO_PLAYER;
import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.service.ItemShareDefaultDataService;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;

public class ItemSharePlayerPanel extends JPanel
{
	ItemSharePlayerDropdown playerDropdown;
	String selectedPlayerName;

	protected ItemSharePlayerPanel(ItemManager itemManager, ItemShareData data)
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		ItemShareItemTabPanel tabPanel = new ItemShareItemTabPanel();

		playerDropdown = new ItemSharePlayerDropdown(name -> tabPanel.update(itemManager, getSelectedPlayer(data, name)));
		playerDropdown.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

		add(playerDropdown);
		add(tabPanel);
	}

	public void update(ItemShareData data)
	{
		playerDropdown.update(data, selectedPlayerName);
		repaint();
	}

	private ItemSharePlayer getSelectedPlayer(ItemShareData data, String name)
	{
		selectedPlayerName = name;
		return NO_PLAYER.equals(name)
			? ItemShareDefaultDataService.getDefaultPlayerData(NO_PLAYER)
			: getExistingPlayer(data, name);
	}

	private ItemSharePlayer getExistingPlayer(ItemShareData data, String name)
	{
		return data.getPlayers().stream().filter(p -> p.getUserName().equals(name)).findFirst().get();
	}
}
