package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.NO_PLAYER;
import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class ItemSharePlayerDropdown extends JPanel
{
	private final ItemShareDrodown dropdown;

	protected ItemSharePlayerDropdown(Consumer<String> callback)
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		dropdown = new ItemShareDrodown(callback);
		add(dropdown, BorderLayout.PAGE_START);
	}

	public void update(ItemShareData data, String selectedName)
	{
		List<ItemSharePlayer> players = data.getPlayers();

		ArrayList<String> options = (ArrayList<String>) players.stream()
			.map(ItemSharePlayer::getUserName)
			.collect(Collectors.toList());

		String name = options.stream()
			.filter(n -> n.equals(selectedName))
			.findFirst()
			.orElse(NO_PLAYER);

		options.add(0, NO_PLAYER);
		dropdown.update(options);
		dropdown.select(name);

		repaint();
	}
}
