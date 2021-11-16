package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import net.runelite.client.ui.components.PluginErrorPanel;

public class ItemSharePlayerInfoPanel extends JPanel
{
	protected ItemSharePlayerInfoPanel()
	{
		super(false);
		setLayout(new BorderLayout(0, 8));
		setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
	}

	public void reset()
	{
		PluginErrorPanel panel = new PluginErrorPanel();
		panel.setContent("No player data", "Log in to see player data.");

		removeAll();
		add(panel, BorderLayout.NORTH);
		repaint();
	}

	public void update(ItemShareData data)
	{
		ItemSharePlayerDropdown playerDropdown = new ItemSharePlayerDropdown();

		removeAll();
		add(playerDropdown);
		playerDropdown.update(data);
		repaint();
	}
}
