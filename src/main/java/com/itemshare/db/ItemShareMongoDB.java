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
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

public class ItemShareMongoDB implements ServerMonitorListener
{
	@Inject
	private ConfigManager configManager;

	private Consumer<List<ItemSharePlayer>> playersCallback;
	private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	private MongoCollection<Document> collection;

	public void setPlayersCallback(Consumer<List<ItemSharePlayer>> callback)
	{
		this.playersCallback = callback;
	}

	public boolean hasEnvironmentVariables()
	{
		return !StringUtils.isEmpty(getClusterDomain())
			&& !StringUtils.isEmpty(getDatabaseName())
			&& !StringUtils.isEmpty(getCollectionName())
			&& !StringUtils.isEmpty(getUsername())
			&& !StringUtils.isEmpty(getPassword());
	}

	public void connect()
	{
		if (this.hasEnvironmentVariables())
		{
			MongoClient client = createClient();
			MongoDatabase database = client.getDatabase(getDatabaseName());
			collection = database.getCollection(getCollectionName());
		}
	}

	public void reconnect()
	{
		if (this.hasEnvironmentVariables() && collection == null)
		{
			connect();
		}
	}

	public void savePlayer(ItemSharePlayer player)
	{
		if (this.hasEnvironmentVariables() && player != null)
		{
			reconnect();

			String json = gson.toJson(player);
			Document data = Document.parse(json);

			collection.updateOne(
				Filters.eq("name", player.getName()),
				new Document("$set", data),
				new UpdateOptions().upsert(true));
		}
	}

	public List<ItemSharePlayer> getPlayers()
	{
		if (this.hasEnvironmentVariables())
		{
			reconnect();

			FindIterable<Document> iterable = collection.find();
			List<Document> documents = new ArrayList<>();
			iterable.forEach(documents::add);

			List<ItemSharePlayer> players = documents.stream()
				.map(document -> gson.fromJson(document.toJson(), ItemSharePlayer.class))
				.collect(Collectors.toList());

			this.playersCallback.accept(players);

			return players;
		}
		else
		{
			return new ArrayList<>();
		}
	}

	private MongoClient createClient()
	{
		ConnectionString connection = new ConnectionString(getConnectionString());
		MongoClientSettings settings = MongoClientSettings.builder()
			.applyConnectionString(connection)
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
