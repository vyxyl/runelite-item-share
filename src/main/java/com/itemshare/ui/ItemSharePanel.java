package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class ItemSharePanel extends PluginPanel
{
    private final ItemSharePlayerInfoPanel playerInfoPanel;

    public ItemSharePanel()
    {
        super(false);

        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel titlePanel = getTitlePanel();
        add(titlePanel);

        playerInfoPanel = new ItemSharePlayerInfoPanel();
        add(playerInfoPanel);
    }

    public void reset()
    {
        playerInfoPanel.reset();
    }

    public void update(ItemShareData data)
    {
        playerInfoPanel.update(data);
    }

    private JPanel getTitlePanel()
    {
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        titlePanel.setLayout(new BorderLayout());

        JLabel title = new JLabel();
        title.setText("Item Share");
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.WEST);

        return titlePanel;
    }
}
