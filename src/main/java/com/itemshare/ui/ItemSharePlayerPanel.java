package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.components.PluginErrorPanel;

public class ItemSharePlayerPanel extends JPanel
{
	private final ItemShareItemTabPanel tabPanel;

	protected ItemSharePlayerPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		tabPanel = new ItemShareItemTabPanel();
	}

	public void reset()
	{
		PluginErrorPanel panel = new PluginErrorPanel();
		panel.setContent("No player data", "Log in to see player data.");

		removeAll();
		add(panel, BorderLayout.NORTH);
		repaint();
	}

	public void update(ItemManager itemManager, ItemShareData data)
	{
		ItemSharePlayerDropdown playerDropdown = new ItemSharePlayerDropdown(name ->
		{
			if (!name.equals(data.getLocalPlayer().getUserName()))
			{
				System.out.println("Selected Different Player");
			}
		});

		removeAll();
		add(playerDropdown);
		add(tabPanel);

		playerDropdown.update(data);
		tabPanel.update(itemManager, data.getLocalPlayer());

		repaint();
	}
}
