package com.itemshare.ui;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ItemShareUpdateMessagePanel extends JPanel
{
	private final JLabel updateMessageLabel = new JLabel();

	Timer timer;
	Date updatedDate;

	protected ItemShareUpdateMessagePanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel message = new JPanel();
		message.setLayout(new BoxLayout(message, BoxLayout.LINE_AXIS));
		message.add(updateMessageLabel);

		add(message);

		timer = createUpdateTimer();
	}

	public void update(Date updatedDate)
	{
		this.updatedDate = updatedDate;
		updateMessage();
	}

	private Timer createUpdateTimer()
	{
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				updateMessage();
			}

		}, 0, 1000);

		return timer;
	}

	private void updateMessage()
	{
		updateMessageLabel.setText(getMessageText());
	}

	private String getMessageText()
	{
		if (updatedDate == null)
		{
			return "Last Saved: N/A";
		}
		else
		{
			long ms = Math.abs(new Date().getTime() - updatedDate.getTime());
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
			else
			{
				if (seconds > 0)
				{
					return getSavedMessage(seconds, "second");
				}
				else
				{
					return "Last Saved: just now";
				}
			}
		}
	}

	private String getSavedMessage(long amount, String unit)
	{
		String plural = amount > 1 ? "s" : "";
		return "Last Saved: " + amount + " " + unit + plural + " ago";
	}
}
