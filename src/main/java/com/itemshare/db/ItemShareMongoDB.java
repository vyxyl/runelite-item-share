package com.itemshare.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static com.itemshare.constant.ItemShareConstants.CONFIG_BASE;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_CLUSTER_DOMAIN;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_COLLECTION_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_DATABASE_NAME;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_PASSWORD;
import static com.itemshare.constant.ItemShareConstants.CONFIG_MONGODB_USERNAME;
import com.itemshare.model.ItemSharePlayer;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.event.ServerMonitorListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

public class ItemShareMongoDB implements ServerMonitorListener
{
	@Inject
	private ConfigManager configManager;

	private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	private Runnable onReady;
	private Runnable onClosed;
	private MongoCollection<Document> collection;

	public boolean hasEnvironmentVariables()
	{
		return !StringUtils.isEmpty(getClusterDomain())
			&& !StringUtils.isEmpty(getDatabaseName())
			&& !StringUtils.isEmpty(getCollectionName())
			&& !StringUtils.isEmpty(getUsername())
			&& !StringUtils.isEmpty(getPassword());
	}

	public void connect(Runnable onReady, Runnable onClosed)
	{
		this.onReady = onReady;
		this.onClosed = onClosed;

		if (this.hasEnvironmentVariables())
		{
			MongoClient client = createClient(onReady, onClosed);
			MongoDatabase database = client.getDatabase(getDatabaseName());
			collection = database.getCollection(getCollectionName());
		}
		else
		{
			onClosed.run();
		}
	}

	public void reconnect()
	{
		connect(onReady, onClosed);
	}

	public void savePlayer(ItemSharePlayer player)
	{
		String json = gson.toJson(player);
		Document data = Document.parse(json);

		collection.updateOne(
			Filters.eq("name", player.getName()),
			new Document("$set", data),
			new UpdateOptions().upsert(true));
	}

	public List<ItemSharePlayer> getPlayers()
	{
		FindIterable<Document> iterable = collection.find();
		List<Document> documents = new ArrayList<>();
		iterable.forEach(documents::add);

		return documents.stream()
			.map(document -> gson.fromJson(document.toJson(), ItemSharePlayer.class))
			.collect(Collectors.toList());
	}

	private MongoClient createClient(Runnable onReady, Runnable onClosed)
	{
		ConnectionString connection = new ConnectionString(getConnectionString());
		MongoClientSettings settings = MongoClientSettings.builder()
			.applyConnectionString(connection)
			.applyToConnectionPoolSettings(builder -> builder.addConnectionPoolListener(new ItemShareMongoDBListener(onReady, onClosed)))
			.build();
		return MongoClients.create(settings);
	}

	private String getConnectionString()
	{
		return "mongodb+srv://" + getUsername() + ":" + getPassword() + "@" + getClusterDomain() + "/" + getDatabaseName() + "?retryWrites=true&w=majority";
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
