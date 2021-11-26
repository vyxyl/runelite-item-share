package com.itemshare;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_GROUP_ID;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_CLUSTER_DOMAIN;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_COLLECTION_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_DATABASE_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_ENABLED;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_PASSWORD;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_USERNAME;
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
		keyName = CONFIG_MONGODB_ENABLED,
		name = "Mongo DB Enabled",
		description = "Is the user using a self-hosted mongodb database"
	)
	default boolean mongoDbDatabaseEnabled()
	{
		return false;
	}

	@ConfigItem(
		hidden = true,
		keyName = CONFIG_MONGODB_DATABASE_NAME,
		name = "Mongo DB Cluster Database Name",
		description = "The name of the MongoDB database"

	)
	default String mongoDbDatabaseName()
	{
		return "";
	}

	@ConfigItem(
		hidden = true,
		keyName = CONFIG_MONGODB_COLLECTION_NAME,
		name = "Mongo DB Cluster Database Name",
		description = "The name of the MongoDB database"

	)
	default String mongoDbCollectionName()
	{
		return "";
	}

	@ConfigItem(
		hidden = true,
		keyName = CONFIG_MONGODB_CLUSTER_DOMAIN,
		name = "Mongo DB Cluster Domain",
		description = "The domain of the MongoDB cluster"

	)
	default String mongoDbClusterDomain()
	{
		return "";
	}

	@ConfigItem(
		hidden = true,
		keyName = CONFIG_MONGODB_USERNAME,
		name = "Mongo DB Username",
		description = "The username for the MongoDB database"
	)
	default String mongoDbUsername()
	{
		return "";
	}

	@ConfigItem(
		hidden = true,
		keyName = CONFIG_MONGODB_PASSWORD,
		name = "Mongo DB Password",
		description = "The password for the MongoDB database"
	)
	default String mongoDbPassword()
	{
		return "";
	}
}
