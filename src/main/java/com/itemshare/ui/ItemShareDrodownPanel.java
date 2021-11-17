package com.itemshare.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.function.Consumer;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import net.runelite.client.ui.PluginPanel;

public class ItemShareDrodownPanel extends JPanel
{
	private JComboBox<String> dropdown = new JComboBox<>();

	protected ItemShareDrodownPanel(Consumer<String> callback)
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));
		setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, 30));

		dropdown = new JComboBox<>();
		dropdown.setFocusable(false);
		dropdown.setForeground(Color.WHITE);
		dropdown.setRenderer(new ItemShareDropdownRenderer());

		dropdown.addItemListener(e ->
		{
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				String value = (String) e.getItem();
				callback.accept(value);
			}
		});

		add(dropdown);
	}

	void select(String value)
	{
		dropdown.setSelectedItem(value);
	}

	void update(ArrayList<String> options)
	{
		dropdown.removeAllItems();
		options.forEach(option -> dropdown.addItem(option));

		repaint();
	}
}
