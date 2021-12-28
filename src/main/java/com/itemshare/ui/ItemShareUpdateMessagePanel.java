package com.itemshare.ui;

import com.itemshare.service.ItemSharePanelService;
import java.awt.Component;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ItemShareUpdateMessagePanel extends JPanel
{
	private final JLabel message = new JLabel();
	private Date updatedDate;

	protected ItemShareUpdateMessagePanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		message.setAlignmentX(Component.CENTER_ALIGNMENT);
		message.setHorizontalAlignment(SwingConstants.CENTER);
		ItemSharePanelService.setHeight(message, 20);
		add(message);
	}

	public void update(Date updatedDate)
	{
		this.updatedDate = updatedDate;
		updateMessage();
	}

	private void updateMessage()
	{
		message.setText(getMessageText());
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
