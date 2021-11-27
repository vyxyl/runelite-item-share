package com.itemshare.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.IconTextField;

public class ItemShareItemSearchBoxPanel extends JPanel
{
	private final IconTextField searchBox = new IconTextField();
	private final ItemShareItemListModel model = new ItemShareItemListModel();

	protected ItemShareItemSearchBoxPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		searchBox.setIcon(IconTextField.Icon.SEARCH);
		searchBox.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		searchBox.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);

		add(searchBox);
	}

	public void addListener(Runnable runnable)
	{
		searchBox.addClearListener(runnable);
		searchBox.addKeyListener(createListener(runnable));
	}

	public String getText()
	{
		return searchBox.getText();
	}

	private KeyListener createListener(Runnable runnable)
	{
		return new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				runnable.run();
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				runnable.run();
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				runnable.run();
			}
		};
	}
}
