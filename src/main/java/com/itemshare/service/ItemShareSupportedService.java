package com.itemshare.service;

import com.itemshare.state.ItemShareState;
import java.util.EnumSet;
import net.runelite.api.WorldType;

public class ItemShareSupportedService
{
	public static boolean isSupported()
	{
		return ItemShareSupportedService.isSupportedWorld()
			&& ItemSharePlayerService.isAvailable();
	}

	public static boolean isSupportedWorld()
	{
		return ItemShareState.client.getWorldType().stream().noneMatch(EnumSet.of(
			WorldType.NOSAVE_MODE,
			WorldType.TOURNAMENT_WORLD,
			WorldType.DEADMAN,
			WorldType.SEASONAL
		)::contains);
	}
}