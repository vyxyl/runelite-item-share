package com.itemshare.service;

import static com.itemshare.constant.ItemShareConstants.DB_SYNC_FREQUENCY_MS;
import com.itemshare.state.ItemShareState;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

public class ItemShareSyncService
{
	private static Timer timer;
	private static Instant syncTime;

	public static void start()
	{
		if (timer == null)
		{
			createTimer();
		}
	}

	private static void createTimer()
	{
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				sync();
			}
		}, 0, DB_SYNC_FREQUENCY_MS);
	}

	private static void sync()
	{
		if (shouldSync())
		{
			ItemShareGroupIdService.load();

			ItemShareState.db.savePlayer(ItemShareState.player, () -> {
				ItemShareState.db.getPlayers(ItemShareState.player.getGroupId(), players -> {
					ItemShareState.data.setPlayers(players);
					syncTime = Instant.now();
					ItemShareUIService.update();
				}, () -> {
				});
			}, () -> {
			});
		}
	}

	private static boolean shouldSync()
	{
		return isSyncExpired() && ItemShareState.db.isConnected() && ItemSharePlayerService.isAvailable();
	}

	private static boolean isSyncExpired()
	{
		return syncTime == null || Duration.between(syncTime, Instant.now()).toMillis() > DB_SYNC_FREQUENCY_MS;
	}
}
