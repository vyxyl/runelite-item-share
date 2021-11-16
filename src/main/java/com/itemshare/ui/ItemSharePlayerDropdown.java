package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ItemSharePlayerDropdown extends JPanel
{
	protected ItemSharePlayerDropdown()
	{
		super(false);
		setLayout(new BorderLayout(0, 8));
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
	}

	public void update(ItemShareData data)
	{
		removeAll();

		ItemShareDrodown dropdown = new ItemShareDrodown();

		add(dropdown);
		repaint();

		ArrayList<ItemSharePlayer> players = getPlayers(data);
		ArrayList<String> names = (ArrayList<String>) players.stream().map(ItemSharePlayer::getUserName).collect(Collectors.toList());

		dropdown.update("Player", names, name ->
		{
			if (!name.equals(data.getLocalPlayer().getUserName()))
			{
				System.out.println("Selected Different Player");
			}
		});
	}

	private ArrayList<ItemSharePlayer> getPlayers(ItemShareData data)
	{
		ArrayList<ItemSharePlayer> players = new ArrayList<>();
		players.add(data.getLocalPlayer());
		players.addAll(data.getOtherPlayers());
		return players;
	}
}
