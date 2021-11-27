package com.itemshare.ui;

import com.itemshare.model.ItemSharePlayer;
import com.itemshare.state.ItemShareState;
import java.awt.event.ItemEvent;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.commons.lang3.StringUtils;

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

	public void update()
	{
		updateData();

		playerDropdown.update();
		playerDropdown.setItemListener(this::onPlayerSelected);
	}

	private void onPlayerSelected(ItemEvent event)
	{
		if (event.getStateChange() == ItemEvent.SELECTED)
		{
			updateData();
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

		}, 0, 1000);

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

	private void updateData()
	{
		ItemSharePlayer selectedPlayer = playerDropdown.getSelectedPlayer();
		player = findPlayer(selectedPlayer);
		playerPanel.update(ItemShareState.itemManager, player);
	}

	private ItemSharePlayer findPlayer(ItemSharePlayer defaultPlayer)
	{
		if (ItemShareState.data == null || ItemShareState.data.getPlayers() == null)
		{
			return defaultPlayer;
		}
		else
		{
			return ItemShareState.data.getPlayers().stream().filter(p -> StringUtils.equals(p.getName(), defaultPlayer.getName())).findFirst().orElse(defaultPlayer);
		}
	}
}
