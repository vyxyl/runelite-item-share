package com.itemshare.ui;

import com.itemshare.ItemSharePlugin;
import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import static com.itemshare.constant.ItemShareConstants.ICON_COPY_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_EDIT_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_GENERATE_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_SAVE_BUTTON;
import com.itemshare.service.ItemShareGroupIdService;
import com.itemshare.state.ItemShareState;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.CLOSED_OPTION;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.FlatTextField;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;

public class ItemShareGroupIDPanel extends JPanel
{
	FlatTextField textField;

	ImageIcon editIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_EDIT_BUTTON));
	ImageIcon saveIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_SAVE_BUTTON));
	ImageIcon copyIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_COPY_BUTTON));
	ImageIcon generateIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_GENERATE_BUTTON));

	protected ItemShareGroupIDPanel()
	{
		super(false);

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

	private JButton getCopyButton()
	{
		return getButton(copyIcon, "Copy Group ID");
	}

	private JButton getEditButton()
	{
		return getButton(editIcon, "Edit Group ID");
	}

	private JButton getGenerateButton()
	{
		return getButton(generateIcon, "Generate Group ID");
	}

	private JButton getButton(ImageIcon editIcon, String s)
	{
		JButton button = new JButton();
		SwingUtil.removeButtonDecorations(button);

		button.setIcon(editIcon);
		button.setToolTipText(s);
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

	private void addCopyTextListener(FlatTextField field, JButton button)
	{
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				StringSelection stringSelection = new StringSelection(field.getTextField().getText());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
	}

	private void addGenerateIdListener(FlatTextField field, JButton button)
	{
		JComponent root = this;

		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel panel = new JPanel();
				panel.setMinimumSize(new Dimension(200, 200));

				JOptionPane.showOptionDialog(root, panel, "Edit Group ID", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{}, null);
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
		textField = new FlatTextField();
		textField.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		textField.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		textField.setText(ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_GROUP_ID));
		setDimension(textField, getWidth(0.70), 40);

		JButton copyButton = getCopyButton();
		addCopyTextListener(textField, copyButton);
		setDimension(copyButton, getWidth(0.10), 30);

		JButton editButton = getEditButton();
		addToggleEditListener(textField, editButton);
		disableEditing(textField, editButton);
		setDimension(editButton, getWidth(0.10), 30);

		JButton generateButton = getGenerateButton();
		addGenerateIdListener(textField, generateButton);
		setDimension(generateButton, getWidth(0.10), 30);

		JPanel field = new JPanel();
		field.setLayout(new BoxLayout(field, BoxLayout.LINE_AXIS));
		field.add(textField);
		field.add(copyButton);
		field.add(editButton);
		field.add(generateButton);

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
		ItemShareState.configManager.setConfiguration(CONFIG_BASE, CONFIG_GROUP_ID, value);
	}
}
