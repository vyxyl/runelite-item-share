package com.itemshare.ui;

import com.itemshare.service.ItemSharePanelService;
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
		searchBox.addKeyListener(ItemSharePanelService.getKeyListener(runnable));
	}

	public String getText()
	{
		return searchBox.getText();
	}


}
