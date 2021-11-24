package com.itemshare.ui;

import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import java.awt.event.ItemEvent;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;

public class ItemShareNavigationPanel extends JPanel
{
	private final JLabel updateMessageLabel = new JLabel();
	private final ItemSharePlayerDropdownPanel playerDropdown = new ItemSharePlayerDropdownPanel();
	private final ItemSharePlayerPanel playerPanel = new ItemSharePlayerPanel();

	Timer timer;
	ItemSharePlayer player;

	protected ItemShareNavigationPanel()
	{
		super(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel updateMessage = new JPanel();
		updateMessage.setLayout(new BoxLayout(updateMessage, BoxLayout.LINE_AXIS));
		updateMessage.add(updateMessageLabel);

		playerDropdown.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		updateMessage.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		playerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		add(playerDropdown);
		add(updateMessage);
		add(playerPanel);

		timer = createUpdateTimer();
	}

	public void update(ItemManager itemManager, ItemShareData data)
	{
		updateData(itemManager);

		playerDropdown.update(data);
		playerDropdown.setItemListener(event -> onPlayerSelected(itemManager, event));
	}

	private void onPlayerSelected(ItemManager itemManager, ItemEvent event)
	{
		if (event.getStateChange() == ItemEvent.SELECTED)
		{
			updateData(itemManager);
			updateMessage();
		}
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

		}, 1000, 1000);

		return timer;
	}

	private void updateMessage()
	{
		updateMessageLabel.setText(getMessageText());
	}

	private String getMessageText()
	{
		if (player == null || player.getUpdatedDate() == null)
		{
			return "Last Saved: N/A";
		}
		else
		{
			Date now = new Date();
			Date saveDate = player.getUpdatedDate();

			long ms = Math.abs(now.getTime() - saveDate.getTime());
			long seconds = TimeUnit.SECONDS.convert(ms, TimeUnit.MILLISECONDS);
			long minutes = TimeUnit.MINUTES.convert(ms, TimeUnit.MILLISECONDS);
			long hours = TimeUnit.HOURS.convert(ms, TimeUnit.MILLISECONDS);
			long days = TimeUnit.DAYS.convert(ms, TimeUnit.MILLISECONDS);

			if (days > 0)
			{
				return getLastSavedMessage(days, "day");
			}
			else if (hours > 0)
			{
				return getLastSavedMessage(hours, "hour");
			}
			else if (minutes > 0)
			{
				return getLastSavedMessage(minutes, "minute");
			}
			else
			{
				if (seconds == 0)
				{
					return "Last Saved: just now";
				}
				else
				{
					return getLastSavedMessage(seconds, "second");
				}
			}
		}
	}

	private String getLastSavedMessage(long amount, String unit)
	{
		String plural = amount > 1 ? "s" : "";
		return "Last Saved: " + amount + " " + unit + plural + " ago";
	}

	private void updateData(ItemManager itemManager)
	{
		player = playerDropdown.getSelectedPlayer();
		playerPanel.update(itemManager, player);
	}
}
