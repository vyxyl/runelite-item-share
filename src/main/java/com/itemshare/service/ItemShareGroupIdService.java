package com.itemshare.service;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import com.itemshare.state.ItemShareState;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class ItemShareGroupIdService
{
	public static String loadExistingId()
	{
		if (StringUtils.isEmpty(ItemShareState.groupId))
		{
			ItemShareState.groupId = ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_GROUP_ID);
		}

		if (StringUtils.isEmpty(ItemShareState.groupId))
		{
			loadNewGroupId();
		}

		return ItemShareState.groupId;
	}

	public static String loadNewGroupId()
	{
		String uuid = UUID.randomUUID().toString();
		ItemShareState.configManager.setConfiguration(CONFIG_BASE, CONFIG_GROUP_ID, uuid);
		ItemShareState.groupId = uuid;

		if (ItemShareState.player != null && StringUtils.isEmpty(ItemShareState.player.getGroupId()))
		{
			ItemShareState.player.setGroupId(ItemShareState.groupId);
		}

		return ItemShareState.groupId;
	}
}
