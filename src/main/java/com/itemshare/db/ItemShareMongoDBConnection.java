package com.itemshare.db;

import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_CLUSTER_DOMAIN;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_COLLECTION_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_DATABASE_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_PASSWORD;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_USERNAME;
import static com.itemshare.constant.ItemShareConstants.DB_SYNC_FREQUENCY_MS;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

	private Runnable onSuccess;
	private Runnable onFailure;

	private ItemShareDBStatus status = ItemShareDBStatus.UNINITIALIZED;

	public ItemShareDBStatus getStatus()
	{
		return status;
	}

	public void disconnect()
	{
		if (client != null)
		{
			client.close();
			client = null;
			database = null;
			collection = null;
		}

		updateStatus(onFailure, ItemShareDBStatus.DISCONNECTED);
	}

	public void connect(Runnable onSuccess, Runnable onFailure)
	{
		this.onSuccess = onSuccess;
		this.onFailure = onFailure;

		disconnect();
		updateStatus(onFailure, ItemShareDBStatus.LOADING);

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
				disconnect();
			}
		}
		else
		{
			disconnect();
		}
	}

	public void reconnect()
	{
		connect(onSuccess, onFailure);
	}

	public boolean isConnected()
	{
		return status == ItemShareDBStatus.CONNECTED;
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
		updateStatus(onSuccess, ItemShareDBStatus.CONNECTED);
	}

	@Override
	public void serverHeartbeatFailed(ServerHeartbeatFailedEvent failedEvent)
	{
		disconnect();
	}

	private void updateStatus(Runnable runnable, ItemShareDBStatus disconnected)
	{
		status = disconnected;

		if (runnable != null)
		{
			runnable.run();
		}
	}

	private MongoClient createClient()
	{
		ServerMonitorListener listener = this;
		ConnectionString connection = new ConnectionString(getConnectionString());

		MongoClientSettings settings = MongoClientSettings.builder()
			.applyConnectionString(connection)
			.applyToServerSettings(builder -> builder.addServerMonitorListener(listener))
			.build();

		return MongoClients.create(settings);
	}

	private String getConnectionString()
	{
		return "mongodb+srv://" + getUsername() + ":" + getPassword() + "@" + getClusterDomain() + "/" + getDatabaseName()
			+ "?retryWrites=true"
			+ "&w=majority"
			+ "&heartbeatFrequencyMS=" + DB_SYNC_FREQUENCY_MS;
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
		return getConfig(CONFIG_MONGODB_USERNAME);
	}

	private String getPassword()
	{
		return getConfig(CONFIG_MONGODB_PASSWORD);
	}

	private String getClusterDomain()
	{
		return getConfig(CONFIG_MONGODB_CLUSTER_DOMAIN);
	}

	private String getDatabaseName()
	{
		return getConfig(CONFIG_MONGODB_DATABASE_NAME);
	}

	private String getCollectionName()
	{
		return getConfig(CONFIG_MONGODB_COLLECTION_NAME);
	}

	private String getConfig(String configMongodbUsername)
	{
		return configManager.getConfiguration(CONFIG_BASE, configMongodbUsername);
	}
}
