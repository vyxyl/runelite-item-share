package com.itemshare.db;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_CLUSTER_DOMAIN;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_COLLECTION_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_DATABASE_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_PASSWORD;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_USERNAME;
import static com.itemshare.constant.ItemShareConstants.MONGODB_SYNC_FREQUENCY_MS;
import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ServerSettings;
import com.mongodb.event.ServerHeartbeatFailedEvent;
import com.mongodb.event.ServerHeartbeatStartedEvent;
import com.mongodb.event.ServerHeartbeatSucceededEvent;
import com.mongodb.event.ServerMonitorListener;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

public class ItemShareMongoDBConnection implements ServerMonitorListener
{
	@Inject
	private ConfigManager configManager;

	private MongoClient client;
	private MongoDatabase database;
	private MongoCollection<Document> collection;

	private boolean isConnected = false;
	private Runnable onSuccess;
	private Runnable onFailure;

	private ItemShareMongoDBStatus status = ItemShareMongoDBStatus.UNINITIALIZED;

	public ItemShareMongoDBStatus getStatus()
	{
		return status;
	}

	public void connect(Runnable onSuccess, Runnable onFailure)
	{
		status = ItemShareMongoDBStatus.LOADING;

		this.onSuccess = onSuccess;
		this.onFailure = onFailure;

		if (this.hasEnvironmentVariables())
		{
			try
			{
				client = createClient();
				database = client.getDatabase(getDatabaseName());
				collection = database.getCollection(getCollectionName());
			}
			catch (Exception ex)
			{
				status = ItemShareMongoDBStatus.DISCONNECTED;
				onFailure.run();
			}
		}
	}

	public void reconnect()
	{
		connect(onSuccess, onFailure);
	}

	public boolean isConnected()
	{
		return isConnected;
	}

	public MongoCollection<Document> getCollection()
	{
		return collection;
	}

	@Override
	public void serverHearbeatStarted(ServerHeartbeatStartedEvent startedEvent)
	{
		// Ping Started
	}

	@Override
	public void serverHeartbeatSucceeded(ServerHeartbeatSucceededEvent succeededEvent)
	{
		this.isConnected = true;

		if (onSuccess != null)
		{
			status = ItemShareMongoDBStatus.CONNECTED;
			onSuccess.run();
		}
	}

	@Override
	public void serverHeartbeatFailed(ServerHeartbeatFailedEvent failedEvent)
	{
		this.isConnected = false;

		if (onFailure != null)
		{
			status = ItemShareMongoDBStatus.DISCONNECTED;
			onFailure.run();
		}
	}

	private MongoClient createClient()
	{
		ServerMonitorListener listener = this;
		ConnectionString connection = new ConnectionString(getConnectionString());

		MongoClientSettings settings = MongoClientSettings.builder()
			.applyConnectionString(connection)
			.applyToServerSettings(new Block<ServerSettings.Builder>()
			{
				@Override
				public void apply(final ServerSettings.Builder builder)
				{
					builder.addServerMonitorListener(listener);
				}
			})
			.build();
		return MongoClients.create(settings);
	}

	private String getConnectionString()
	{
		return "mongodb+srv://" + getUsername() + ":" + getPassword() + "@" + getClusterDomain() + "/" + getDatabaseName()
			+ "?retryWrites=true"
			+ "&w=majority"
			+ "&heartbeatFrequencyMS=" + MONGODB_SYNC_FREQUENCY_MS;
	}

	public boolean hasEnvironmentVariables()
	{
		return !StringUtils.isEmpty(getClusterDomain())
			&& !StringUtils.isEmpty(getDatabaseName())
			&& !StringUtils.isEmpty(getCollectionName())
			&& !StringUtils.isEmpty(getUsername())
			&& !StringUtils.isEmpty(getPassword());
	}

	private String getUsername()
	{
		return configManager.getConfiguration(CONFIG_BASE, CONFIG_MONGODB_USERNAME);
	}

	private String getPassword()
	{
		return configManager.getConfiguration(CONFIG_BASE, CONFIG_MONGODB_PASSWORD);
	}

	private String getClusterDomain()
	{
		return configManager.getConfiguration(CONFIG_BASE, CONFIG_MONGODB_CLUSTER_DOMAIN);
	}

	private String getDatabaseName()
	{
		return configManager.getConfiguration(CONFIG_BASE, CONFIG_MONGODB_DATABASE_NAME);
	}

	private String getCollectionName()
	{
		return configManager.getConfiguration(CONFIG_BASE, CONFIG_MONGODB_COLLECTION_NAME);
	}
}
