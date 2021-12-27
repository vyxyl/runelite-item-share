package com.itemshare.service;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_ENABLED;
import com.itemshare.state.ItemShareState;
import org.apache.commons.lang3.StringUtils;

public class ItemShareConfigService
{
	public static boolean isSelfHost()
	{
		String value = ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_MONGODB_ENABLED);

		return StringUtils.equals(value, "true");
	}
}
