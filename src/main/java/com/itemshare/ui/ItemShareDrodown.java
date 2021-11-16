package com.itemshare.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.function.Consumer;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.client.ui.PluginPanel;

public class ItemShareDrodown extends JPanel
{
	protected ItemShareDrodown()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH, 25));
		setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, 25));
	}

	void update(String name, ArrayList<String> options, Consumer<String> callback)
	{
		JLabel label = new JLabel(name);
		label.setForeground(Color.WHITE);

		String[] optionsArray = options.toArray(new String[0]);

		JComboBox<String> dropdown = new JComboBox<>(optionsArray);
		dropdown.setFocusable(false);
		dropdown.setForeground(Color.WHITE);
		dropdown.setRenderer(new DropdownRenderer());
		dropdown.addItemListener(e ->
		{
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				String value = (String) e.getItem();
				callback.accept(value);
			}
		});

		removeAll();
		add(label, BorderLayout.WEST);
		add(dropdown, BorderLayout.EAST);
		repaint();
	}
}
