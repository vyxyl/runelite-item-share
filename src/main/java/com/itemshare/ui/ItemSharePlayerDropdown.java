package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class ItemSharePlayerDropdown extends JPanel
{
	Consumer<String> callback;

	protected ItemSharePlayerDropdown(Consumer<String> callback)
	{
		super(false);
		this.callback = callback;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	public void update(ItemShareData data)
	{
		removeAll();

		ItemShareDrodown dropdown = new ItemShareDrodown();

		add(dropdown, BorderLayout.PAGE_START);
		repaint();

		ArrayList<ItemSharePlayer> players = getPlayers(data);
		ArrayList<String> names = (ArrayList<String>) players.stream().map(ItemSharePlayer::getUserName).collect(Collectors.toList());

		dropdown.update("Player", names, callback);
	}

	private ArrayList<ItemSharePlayer> getPlayers(ItemShareData data)
	{
		ArrayList<ItemSharePlayer> players = new ArrayList<>();
		players.add(data.getLocalPlayer());
		players.addAll(data.getOtherPlayers());
		return players;
	}
}
