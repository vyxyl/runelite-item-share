package com.itemshare.ui;

import com.itemshare.ItemSharePlugin;
import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import static com.itemshare.constant.ItemShareConstants.ICON_COPY_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_EDIT_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_SAVE_BUTTON;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicButtonUI;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.FlatTextField;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;

public class ItemShareGroupIDPanel extends JPanel
{
	private final ConfigManager configManager;
	ImageIcon editIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_EDIT_BUTTON));
	ImageIcon saveIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_SAVE_BUTTON));
	ImageIcon copyIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_COPY_BUTTON));

	protected ItemShareGroupIDPanel(ConfigManager configManager)
	{
		super(false);
		this.configManager = configManager;

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel groupIdInput = getGroupIdInput();
		add(groupIdInput);
	}

	private JPanel getGroupIdInput()
	{
		JLabel label = getLabel();
		JPanel field = getField();
		JPanel setting = createStyledSetting(label, field);
		setDimension(setting, getWidth(1), 40);

		JPanel groupIdInput = new JPanel();
		groupIdInput.setLayout(new BoxLayout(groupIdInput, BoxLayout.LINE_AXIS));
		groupIdInput.add(setting);

		return groupIdInput;
	}

	private int getWidth(double v)
	{
		return (int) (PluginPanel.PANEL_WIDTH * v);
	}

	private JButton getCopyGroupIDButton()
	{
		JButton button = new JButton();
		SwingUtil.removeButtonDecorations(button);

		button.setIcon(copyIcon);
		button.setToolTipText("Copy Group ID");
		button.setBackground(ColorScheme.DARK_GRAY_COLOR);
		button.setUI(new BasicButtonUI());
		addInteractionStyling(button);

		return button;
	}

	private JButton getEditGroupIDButton()
	{
		JButton button = new JButton();
		SwingUtil.removeButtonDecorations(button);

		button.setIcon(editIcon);
		button.setToolTipText("Edit Group ID");
		button.setBackground(ColorScheme.DARK_GRAY_COLOR);
		button.setUI(new BasicButtonUI());

		addInteractionStyling(button);

		return button;
	}

	private void addToggleEditListener(FlatTextField field, JButton button)
	{
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (field.getTextField().isEditable())
				{
					disableEditing(field, button);
					updateGroupId(field.getText());
				}
				else
				{
					enableEditing(field, button);
				}
			}
		});
	}

	private void enableEditing(FlatTextField field, JButton button)
	{
		field.getTextField().setEditable(true);
		button.setIcon(saveIcon);
		button.setToolTipText("Save Group ID");
	}

	private void disableEditing(FlatTextField field, JButton button)
	{
		field.getTextField().setEditable(false);
		button.setIcon(editIcon);
		button.setToolTipText("Edit Group ID");
	}

	private void addCopyTextListener(FlatTextField field, JButton copyButton)
	{
		copyButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				StringSelection stringSelection = new StringSelection(field.getTextField().getText());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
	}

	private void addInteractionStyling(JButton button)
	{
		button.getModel().addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				ButtonModel model = (ButtonModel) e.getSource();

				if (model.isPressed())
				{
					button.setBackground(ColorScheme.DARKER_GRAY_COLOR);
				}
				else if (model.isRollover())
				{
					button.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
				}
				else
				{
					button.setBackground(ColorScheme.DARK_GRAY_COLOR);
				}
			}
		});
	}

	private JLabel getLabel()
	{
		JLabel label = new JLabel("Group ID");
		label.setForeground(Color.WHITE);

		return label;
	}

	private JPanel getField()
	{
		FlatTextField textField = new FlatTextField();
		textField.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		textField.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		textField.setText(configManager.getConfiguration(CONFIG_BASE, CONFIG_GROUP_ID));
		setDimension(textField, getWidth(0.80), 40);

		JButton copyButton = getCopyGroupIDButton();
		addCopyTextListener(textField, copyButton);
		setDimension(copyButton, getWidth(0.10), 30);

		JButton editButton = getEditGroupIDButton();
		addToggleEditListener(textField, editButton);
		disableEditing(textField, editButton);
		setDimension(editButton, getWidth(0.10), 30);

		JPanel field = new JPanel();
		field.setLayout(new BoxLayout(field, BoxLayout.LINE_AXIS));
		field.add(textField);
		field.add(copyButton);
		field.add(editButton);

		return field;
	}

	private JPanel createStyledSetting(JComponent label, JComponent value)
	{
		JPanel setting = new JPanel();
		setting.setLayout(new BorderLayout());
		setting.add(label, BorderLayout.PAGE_START);
		setting.add(value, BorderLayout.CENTER);
		setting.add(Box.createVerticalGlue(), BorderLayout.PAGE_END);

		return setting;
	}

	private void setDimension(JComponent component, int width, int height)
	{
		component.setPreferredSize(new Dimension(width, height));
		component.setMinimumSize(new Dimension(width, height));
		component.setMaximumSize(new Dimension(width, height));
	}

	private void updateGroupId(String value)
	{
		configManager.setConfiguration(CONFIG_BASE, CONFIG_GROUP_ID, value);
	}
}
