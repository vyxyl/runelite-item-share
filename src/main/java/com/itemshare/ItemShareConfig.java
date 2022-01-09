package com.itemshare;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GIM_ENABLED;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(CONFIG_BASE)
public interface ItemShareConfig extends Config
{
	@ConfigItem(
		hidden = true,
		keyName = CONFIG_GROUP_ID,
		name = "Group ID",
		description = "Identifies which group the player is syncing with"
	)
	default String groupId()
	{
		return "";
	}

	@ConfigItem(
		hidden = true,
		keyName = CONFIG_GIM_ENABLED,
		name = "Is GIM Enabled",
		description = "Determines if the GIM Storage tab is visible"
	)
	default Boolean isGIMEnabled()
	{
		return false;
	}
}