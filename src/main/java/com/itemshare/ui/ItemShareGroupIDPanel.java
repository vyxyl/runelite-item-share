package com.itemshare.ui;

import com.itemshare.ItemSharePlugin;
import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import static com.itemshare.constant.ItemShareConstants.ICON_COPY_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_EDIT_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_GENERATE_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_INVALID_ID;
import static com.itemshare.constant.ItemShareConstants.ICON_SAVE_BUTTON;
import static com.itemshare.constant.ItemShareConstants.ICON_VALID_ID;
import com.itemshare.service.ItemShareGroupIdService;
import com.itemshare.state.ItemShareState;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
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
	JPanel titlePanel;
	FlatTextField textField;

	ImageIcon editIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_EDIT_BUTTON));
	ImageIcon saveIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_SAVE_BUTTON));
	ImageIcon copyIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_COPY_BUTTON));
	ImageIcon generateIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_GENERATE_BUTTON));
	ImageIcon validIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_VALID_ID));
	ImageIcon invalidIcon = new ImageIcon(ImageUtil.loadImageResource(ItemSharePlugin.class, ICON_INVALID_ID));

	JLabel invalidMessageLabel;
	JLabel invalidMessageIcon;

	JLabel validMessageLabel;
	JLabel validMessageIcon;

	protected ItemShareGroupIDPanel()
	{
		super(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		titlePanel = getTitlePanel();

		textField = new FlatTextField();
		textField.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		textField.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		textField.setText(ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_GROUP_ID));
		setDimension(textField, getWidth(1), 25);
		textField.setAlignmentX(Component.LEFT_ALIGNMENT);
		textField.addKeyListener(getKeyListener(this::updateValidTitle));

		JPanel copyButton = getCopyButton();
		addCopyTextListener(textField, (JButton) copyButton.getComponent(0));
		setDimension(copyButton, getWidth(1), 30);
		copyButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel editButton = getEditButton();
		addToggleEditListener(textField, (JButton) editButton.getComponent(0));
		disableEditing(textField, (JButton) editButton.getComponent(0));
		setDimension(editButton, getWidth(1), 30);
		editButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel generateButton = getGenerateButton();
		addGenerateIdListener(textField, (JButton) generateButton.getComponent(0));
		setDimension(generateButton, getWidth(1), 30);
		generateButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel scrollableTextField = getScrollableTextField(textField);

		add(titlePanel);
		add(scrollableTextField);
		add(editButton);
		add(copyButton);
		add(generateButton);
	}

	private JPanel getTitlePanel()
	{
		invalidMessageLabel = new JLabel("Invalid ID");
		invalidMessageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		invalidMessageLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		invalidMessageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		setDimension(invalidMessageLabel, getWidth(0.30), 30);

		invalidMessageIcon = new JLabel(invalidIcon);
		invalidMessageIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
		invalidMessageIcon.setAlignmentY(Component.CENTER_ALIGNMENT);
		invalidMessageIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		setDimension(invalidMessageIcon, 15, 30);

		validMessageLabel = new JLabel("Valid ID");
		validMessageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		validMessageLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		validMessageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		setDimension(invalidMessageLabel, getWidth(0.30), 30);

		validMessageIcon = new JLabel(validIcon);
		validMessageIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
		validMessageIcon.setAlignmentY(Component.CENTER_ALIGNMENT);
		validMessageIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		setDimension(validMessageIcon, 15, 30);

		JLabel label = new JLabel("Group ID");
		label.setForeground(Color.WHITE);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		label.setAlignmentY(Component.CENTER_ALIGNMENT);
		setDimension(label, getWidth(0.30), 30);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setAlignmentY(Component.CENTER_ALIGNMENT);
		setDimension(panel, getWidth(1), 30);

		panel.add(label);
		panel.add(Box.createHorizontalGlue(), BorderLayout.LINE_END);
		panel.add(invalidMessageLabel);
		panel.add(invalidMessageIcon);
		panel.add(validMessageLabel);
		panel.add(validMessageIcon);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setAlignmentY(Component.CENTER_ALIGNMENT);

		showValidMessage();

		return panel;
	}

	private void showValidMessage()
	{
		invalidMessageLabel.setVisible(false);
		invalidMessageIcon.setVisible(false);

		validMessageLabel.setVisible(true);
		validMessageIcon.setVisible(true);
	}

	private void showInvalidMessage()
	{
		invalidMessageLabel.setVisible(false);
		invalidMessageIcon.setVisible(false);

		validMessageLabel.setVisible(true);
		validMessageIcon.setVisible(true);

		titlePanel.repaint();
	}

	private void updateValidTitle()
	{
		String value = textField.getTextField().getText();

		if (ItemShareGroupIdService.isValidGroupID(value))
		{
//			titlePanel.add()
		}
	}

	private KeyListener getKeyListener(Runnable runnable)
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

	private JPanel getScrollableTextField(FlatTextField textField)
	{
		JScrollBar textScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);

		JPanel textScrollPanel = new JPanel();
		textScrollPanel.setLayout(new BoxLayout(textScrollPanel, BoxLayout.Y_AXIS));

		BoundedRangeModel brm = textField.getTextField().getHorizontalVisibility();
		textScrollBar.setModel(brm);
		textScrollPanel.add(textField);
		textScrollPanel.add(textScrollBar);

		return textScrollPanel;
	}

	private int getWidth(double v)
	{
		return (int) (PluginPanel.PANEL_WIDTH * v);
	}

	private JPanel getCopyButton()
	{
		return getButton(copyIcon, "Copy ID");
	}

	private JPanel getEditButton()
	{
		return getButton(editIcon, "Edit ID");
	}

	private JPanel getGenerateButton()
	{
		return getButton(generateIcon, "Generate New ID");
	}

	private JPanel getButton(ImageIcon icon, String name)
	{
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		JButton button = new JButton();
		button.setText(name);
		SwingUtil.removeButtonDecorations(button);

		button.setIcon(icon);
		button.setBackground(ColorScheme.DARK_GRAY_COLOR);
		button.setUI(new BasicButtonUI());
		addInteractionStyling(button);

		buttonPanel.add(button);

		return buttonPanel;
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
					button.setText("Edit ID");
				}
				else
				{
					enableEditing(field, button);
					button.setText("Save ID");
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
				String[] options = {"Confirm", "Cancel"};

				int choice = JOptionPane.showOptionDialog(root,
					"This will overwrite your ID with a new one", "Are you sure?",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

				if (choice == 0)
				{
					String groupId = ItemShareGroupIdService.loadNewGroupId();
					field.getTextField().setText(groupId);
				}
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
