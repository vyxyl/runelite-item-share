package com.itemshare;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("itemshare")
public interface ItemShareConfig extends Config
{
	@ConfigItem(
		hidden = true,
		keyName = "mongoDbDatabaseName",
		name = "Mongo DB Cluster Database Name",
		description = "The name of the MongoDB database"

	)
	default String mongoDbDatabaseName()
	{
		return "";
	}

	@ConfigItem(
		hidden = true,
		keyName = "mongoDbCollectionName",
		name = "Mongo DB Cluster Database Name",
		description = "The name of the MongoDB database"

	)
	default String mongoDbCollectionName()
	{
		return "";
	}

	@ConfigItem(
		hidden = true,
		keyName = "mongoDbClusterDomain",
		name = "Mongo DB Cluster Domain",
		description = "The domain of the MongoDB cluster"

	)
	default String mongoDbClusterDomain()
	{
		return "";
	}

	@ConfigItem(
		hidden = true,
		keyName = "mongoDbUsername",
		name = "Mongo DB Username",
		description = "The username for the MongoDB database"
	)
	default String mongoDbUsername()
	{
		return "";
	}

	@ConfigItem(
		hidden = true,
		keyName = "mongoDbPassword",
		name = "Mongo DB Password",
		description = "The password for the MongoDB database"
	)
	default String mongoDbPassword()
	{
		return "";
	}
}
