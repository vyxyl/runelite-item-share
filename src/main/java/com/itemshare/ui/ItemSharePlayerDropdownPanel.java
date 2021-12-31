package com.itemshare.ui;

import static com.itemshare.constant.ItemShareConstants.ICON_RELOAD;
import static com.itemshare.constant.ItemShareConstants.SELECT_A_PLAYER;
import com.itemshare.service.ItemShareAPIService;
import com.itemshare.service.ItemSharePanelService;
import com.itemshare.state.ItemShareState;
import java.awt.Component;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ItemSharePlayerDropdownPanel extends JPanel
{
	private final ItemSharePlayerDropdownModel model;
	private final JComboBox<String> dropdown;
	private JButton button = new JButton();
	private Timer timer;

	protected ItemSharePlayerDropdownPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		model = new ItemSharePlayerDropdownModel();

		dropdown = new JComboBox<>(model);
		dropdown.setRenderer(new ItemSharePlayerDropdownRenderer());
		dropdown.setFocusable(false);

		ImageIcon icon = ItemSharePanelService.loadIcon(ICON_RELOAD);
		button = ItemSharePanelService.getButton(icon, null, () -> {
			if (button.isEnabled())
			{
				ItemShareAPIService.savePlayer(() -> {
					;
					ItemShareAPIService.getPlayerNames(names -> {
						ItemShareState.playerNames = names;
						reselectPLayer();
					});
				});

				temporarilyDisableButton();
			}
		});

		JPanel padding = new JPanel();

		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		padding.setAlignmentX(Component.LEFT_ALIGNMENT);
		dropdown.setAlignmentX(Component.LEFT_ALIGNMENT);

		ItemSharePanelService.setSize(button, 30, 30);
		ItemSharePanelService.setSize(padding, 10, 30);
		ItemSharePanelService.setSize(dropdown, 185, 30);

		add(button);
		add(padding);
		add(dropdown);
	}

	private void reselectPLayer()
	{
		String selectedName = (String) dropdown.getSelectedItem();
		model.setNames(ItemShareState.playerNames);

		dropdown.setSelectedItem(SELECT_A_PLAYER);

		if (ItemShareState.playerNames.contains(selectedName))
		{
			dropdown.setSelectedItem(selectedName);
		}
	}

	private void temporarilyDisableButton()
	{
		int delayMs = 5 * 1000;
		button.setEnabled(false);
		timer = new Timer(delayMs, event -> {
			button.setEnabled(true);
			timer.stop();
		});
		timer.start();
	}

	public void setItemListener(ItemListener listener)
	{
		dropdown.addItemListener(listener);
	}

	public String getSelectedPlayerName()
	{
		return model.getSelectedItem();
	}

	public void update()
	{
		reselectPLayer();
		repaint();
	}
}