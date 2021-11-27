package com.itemshare.ui;

import com.itemshare.model.ItemSharePlayer;
import com.itemshare.service.ItemSharePanelService;
import com.itemshare.state.ItemShareState;
import java.awt.Dimension;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import net.runelite.client.ui.PluginPanel;

public class ItemSharePlayerDropdownPanel extends JPanel
{
	private final ItemSharePlayerDropdownModel model;
	private final JComboBox<ItemSharePlayer> dropdown;

	protected ItemSharePlayerDropdownPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH, 40));
		setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, 40));
		setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, 40));

		model = new ItemSharePlayerDropdownModel();

		dropdown = new JComboBox<>(model);
		dropdown.setRenderer(new ItemSharePlayerDropdownRenderer());
		dropdown.setFocusable(false);

		add(dropdown);
	}

	public void setItemListener(ItemListener listener)
	{
		dropdown.addItemListener(listener);
	}

	public ItemSharePlayer getSelectedPlayer()
	{
		return model.getSelectedItem();
	}

	public void update()
	{
		model.setItems(ItemShareState.data.getPlayers());
	}
}
