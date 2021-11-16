package com.itemshare.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.function.Consumer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ItemShareDrodown extends JPanel
{
    protected ItemShareDrodown()
    {
        super(false);
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

        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(225, 0));

        removeAll();
        add(label, BorderLayout.CENTER);
        add(dropdown, BorderLayout.EAST);
        repaint();
    }
}
