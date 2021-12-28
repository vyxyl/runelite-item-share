package com.itemshare.service;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import com.itemshare.state.ItemShareState;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class ItemShareGroupIdService
{
	private final static Pattern REGEX_VALID_UUID = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

	public static boolean isValidId(String value)
	{
		return !StringUtils.isEmpty(value) && REGEX_VALID_UUID.matcher(value).matches();
	}

	public static void loadExistingId()
	{
		loadSavedId();

		if (!isValidId(ItemShareState.groupId))
		{
			loadNewId();
		}
	}

	public static void loadNewId()
	{
		String uuid = getNewId();
		ItemShareState.configManager.setConfiguration(CONFIG_BASE, CONFIG_GROUP_ID, uuid);
		ItemShareState.groupId = uuid;
	}

	public static String getNewId()
	{
		return UUID.randomUUID().toString();
	}

	private static void loadSavedId()
	{
		ItemShareState.groupId = ItemShareState.configManager.getConfiguration(CONFIG_BASE, CONFIG_GROUP_ID);
	}
}
