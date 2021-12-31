package com.itemshare.ui;

import com.itemshare.model.ItemSharePlayer;
import com.itemshare.service.ItemSharePanelService;
import java.awt.Component;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class ItemShareUpdateMessagePanel extends JPanel
{
	private final JLabel message = new JLabel();
	private ItemSharePlayer player;
	private Timer timer;

	protected ItemShareUpdateMessagePanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		message.setAlignmentX(Component.CENTER_ALIGNMENT);
		message.setHorizontalAlignment(SwingConstants.CENTER);
		ItemSharePanelService.setHeight(message, 20);
		add(message);

		timer = new Timer((int) TimeUnit.SECONDS.toMillis(6), event -> updateMessage());
		timer.start();
	}

	public void updatePanel(ItemSharePlayer player)
	{
		this.player = player;
		updateMessage();
	}

	private void updateMessage()
	{
		message.setText(getMessageText());
	}

	private String getMessageText()
	{
		if (this.player == null || this.player.getUpdatedDate() == null)
		{
			return "Last Saved: N/A";
		}
		else
		{
			long ms = Math.abs(new Date().getTime() - this.player.getUpdatedDate().getTime());
			long seconds = TimeUnit.SECONDS.convert(ms, TimeUnit.MILLISECONDS);
			long minutes = TimeUnit.MINUTES.convert(ms, TimeUnit.MILLISECONDS);
			long hours = TimeUnit.HOURS.convert(ms, TimeUnit.MILLISECONDS);
			long days = TimeUnit.DAYS.convert(ms, TimeUnit.MILLISECONDS);

			if (days > 0)
			{
				return getSavedMessage(days, "day");
			}
			else if (hours > 0)
			{
				return getSavedMessage(hours, "hour");
			}
			else if (minutes > 0)
			{
				return getSavedMessage(minutes, "minute");
			}
			else if (seconds >= 5)
			{
				int rounded = getRoundedIncrement(seconds, 5);
				return getSavedMessage(rounded, "second");
			}
			else
			{
				return "Last Saved: moments ago";
			}
		}
	}

	private int getRoundedIncrement(long seconds, int increment)
	{
		int a = (int) ((float) seconds / increment) * increment;
		int b = a + 10;
		return (seconds - a > b - seconds) ? b : a;
	}

	private String getSavedMessage(long amount, String unit)
	{
		String plural = amount > 1 ? "s" : "";
		return "Last Saved: " + amount + " " + unit + plural + " ago";
	}
}