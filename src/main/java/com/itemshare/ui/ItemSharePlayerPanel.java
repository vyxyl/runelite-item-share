package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.NO_PLAYER;
import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;

public class ItemSharePlayerPanel extends JPanel
{
	private final ItemSharePlayerDropdownPanel playerDropdown;
	private String selectedPlayerName;
	private ItemManager itemManager;
	private ItemShareItemContainerTabPanel tabPanel;
	private final JPanel noPlayersMessage;
	private final JPanel noPlayerSelectedMessage;

	protected ItemSharePlayerPanel(ItemManager itemManager, ItemShareData data)
	{
		super(false);
		this.itemManager = itemManager;

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		tabPanel = new ItemShareItemContainerTabPanel();

		playerDropdown = new ItemSharePlayerDropdownPanel(name -> onPlayerSelect(data, name));
		playerDropdown.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

		noPlayersMessage = new JPanel();
		noPlayersMessage.add(new JLabel("Login to create a new save"));

		noPlayerSelectedMessage = new JPanel();
		noPlayerSelectedMessage.add(new JLabel("Select a player to view their items"));

		tabPanel.setVisible(true);
		noPlayersMessage.setVisible(false);
		noPlayerSelectedMessage.setVisible(false);

		add(playerDropdown);
		add(tabPanel);
		add(noPlayersMessage);
		add(noPlayerSelectedMessage);
	}

	public void update(ItemShareData data)
	{
		playerDropdown.update(data, selectedPlayerName);
		tabPanel.update(this.itemManager);
		repaint();
	}

	private void onPlayerSelect(ItemShareData data, String name)
	{
		if (isNoPlayerSelected(name))
		{
			tabPanel.setVisible(false);

			if (hasPlayers(data))
			{
				noPlayerSelectedMessage.setVisible(true);
			}
			else
			{
				noPlayersMessage.setVisible(true);
			}
		}
		else if (!Objects.equals(selectedPlayerName, name))
		{
			selectedPlayerName = name;

			ItemSharePlayer player = getExistingPlayer(data, name);

			tabPanel.clearFilters();
			tabPanel.update(this.itemManager, player);
			tabPanel.setVisible(true);

			noPlayersMessage.setVisible(false);
			noPlayerSelectedMessage.setVisible(false);

			tabPanel.repaintAll();
		}

		repaint();
	}

	private boolean hasPlayers(ItemShareData data)
	{
		return data.getPlayers().size() > 0;
	}

	private boolean isNoPlayerSelected(String name)
	{
		return NO_PLAYER.equals(name) || name == null;
	}

	private ItemSharePlayer getExistingPlayer(ItemShareData data, String name)
	{
		return data.getPlayers().stream().filter(p -> p.getUserName().equals(name)).findFirst().get();
	}
}
