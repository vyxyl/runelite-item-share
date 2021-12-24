package com.itemshare.service;

import com.itemshare.ItemSharePlugin;
import com.itemshare.model.ItemShareItem;
import com.itemshare.state.ItemShareState;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.FlatTextField;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ImageUtil;

public class ItemSharePanelService
{
	public static AsyncBufferedImage getIcon(ItemShareItem item)
	{
		int id = item.getId();
		int quantity = item.getQuantity();

		return ItemShareState.itemManager.getImage(id, quantity, quantity > 1);
	}

	public static void addButtonInteractionStyling(JButton button)
	{
		button.getModel().addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent event)
			{
				ButtonModel model = (ButtonModel) event.getSource();

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

	public static JButton getButton(Icon icon, String label, Runnable onClick)
	{
		JButton button = new JButton();
		button.setIcon(icon);
		button.setText(label);
		button.setFocusPainted(false);
		button.setBackground(ColorScheme.DARK_GRAY_COLOR);
		button.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent event)
			{
				onClick.run();

				ButtonModel model = button.getModel();
				model.setPressed(false);
				model.setRollover(false);
				model.setSelected(false);

				button.setBackground(ColorScheme.DARK_GRAY_COLOR);
			}
		});

		addButtonInteractionStyling(button);

		return button;
	}

	public static int getWidth(double percent)
	{
		return (int) (PluginPanel.PANEL_WIDTH * percent);
	}

	public static void setSize(JComponent component, double widthPercent, int height)
	{
		setSize(component, getWidth(widthPercent), height);
	}

	public static void setHeight(JComponent component, int height)
	{
		setSize(component, PluginPanel.PANEL_WIDTH, height);
	}

	public static void setSize(JComponent component, int width, int height)
	{
		Dimension dimension = new Dimension(width, height);

		component.setPreferredSize(dimension);
		component.setMinimumSize(dimension);
		component.setMaximumSize(dimension);
	}

	public static JPanel getScrollableTextField(FlatTextField textField)
	{
		BoundedRangeModel brm = textField.getTextField().getHorizontalVisibility();

		JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
		scrollBar.setModel(brm);

		JPanel scrollPanel = new JPanel();
		scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
		scrollPanel.add(textField);
		scrollPanel.add(scrollBar);

		return scrollPanel;
	}

	public static ImageIcon loadIcon(String path)
	{
		return new ImageIcon(loadImage(path));
	}

	public static BufferedImage loadImage(String path)
	{
		return ImageUtil.loadImageResource(ItemSharePlugin.class, path);
	}

	public static JPanel getPadding(int height)
	{
		JPanel panel = new JPanel();
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		setSize(panel, PluginPanel.PANEL_WIDTH, height);

		return panel;
	}

	public static JTextPane getCenteredTextPane(String text)
	{
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setFocusable(false);
		textPane.setText(text);

		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		return textPane;
	}
}
