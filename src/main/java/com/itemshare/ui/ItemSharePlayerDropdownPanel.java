package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.OPTION_NO_PLAYER;
import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.service.ItemShareDefaultDataService;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import net.runelite.client.ui.PluginPanel;
import org.apache.commons.lang3.StringUtils;

public class ItemSharePlayerDropdownPanel extends JPanel
{
	private ItemListener listener;
	private final JComboBox<String> dropdown;

	protected ItemSharePlayerDropdownPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH, 40));
		setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, 40));
		setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, 40));
		setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

		dropdown = new JComboBox<>();
		dropdown.setFocusable(false);
		dropdown.setForeground(Color.WHITE);
		dropdown.setRenderer(new ItemShareDropdownRenderer());

		add(dropdown);
	}

	public void setItemListener(ItemListener listener)
	{
		if (this.listener != null)
		{
			dropdown.removeItemListener(this.listener);
		}

		dropdown.addItemListener(listener);

		this.listener = listener;
	}

	public ItemSharePlayer getSelectedPlayer(ItemShareData data)
	{
		String selected = getSelected();

		return data.getPlayers().stream()
			.filter(p -> StringUtils.equals(p.getName(), selected))
			.findFirst()
			.orElse(ItemShareDefaultDataService.getDefaultPlayerData(selected));
	}

	public void update(ItemShareData data)
	{
		String selected = getSelected();

		dropdown.removeAllItems();

		List<String> options = getOptions(data);
		options.add(0, OPTION_NO_PLAYER);
		options.forEach(dropdown::addItem);

		dropdown.setSelectedItem(options.stream()
			.filter(option -> StringUtils.equals(option, selected))
			.findFirst()
			.orElse(OPTION_NO_PLAYER));

		repaint();
	}

	private String getSelected()
	{
		return (String) dropdown.getSelectedItem();
	}

	private List<String> getOptions(ItemShareData data)
	{
		return data.getPlayers().stream()
			.map(ItemSharePlayer::getName)
			.collect(Collectors.toList());
	}
}
