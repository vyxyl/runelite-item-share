package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.OPTION_NO_PLAYER;
import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.service.ItemShareDefaultDataService;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
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
	private List<String> previousOptions = new ArrayList<String>();

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
		dropdown.setSelectedItem(OPTION_NO_PLAYER);

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
		List<String> options = getOptions(data);
		options.add(0, OPTION_NO_PLAYER);

		if (!listEqualsIgnoreOrder(options, previousOptions))
		{
			String selected = getSelected();

			dropdown.removeAllItems();
			options.forEach(dropdown::addItem);

			String selectedItem = findSelectedOption(options, selected);
			dropdown.setSelectedItem(selectedItem);

			previousOptions = new ArrayList<>(options);

			repaint();
		}
	}

	private String findSelectedOption(List<String> options, String selected)
	{
		if (selected == null)
		{
			return OPTION_NO_PLAYER;
		}
		else
		{
			return options.stream()
				.filter(option -> StringUtils.equals(option, selected))
				.findFirst()
				.orElse(OPTION_NO_PLAYER);
		}
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

	public static <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2)
	{
		return new HashSet<>(list1).equals(new HashSet<>(list2));
	}
}
