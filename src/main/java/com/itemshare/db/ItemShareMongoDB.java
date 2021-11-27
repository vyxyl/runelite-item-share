package com.itemshare.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itemshare.model.ItemSharePlayer;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.event.ServerMonitorListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;

public class ItemShareMongoDB implements ServerMonitorListener, ItemShareDB
{
	@Inject
	private ItemShareMongoDBConnection connection;

	private Runnable onSuccess;
	private Runnable onFailure;

	private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	public boolean isConnected()
	{
		return connection.isConnected();
	}

	public ItemShareDBStatus getStatus()
	{
		return connection.getStatus();
	}

	public void setCallbacks(Runnable onSuccess, Runnable onFailure) {
		this.onSuccess = onSuccess;
		this.onFailure = onFailure;
	}

	public void connect()
	{
		connection.connect(onSuccess, onFailure);
	}

	public void reconnect()
	{
		connection.reconnect();
	}

	public void savePlayer(ItemSharePlayer player, Runnable onSuccess, Runnable onFailure)
	{
		try
		{
			if (connection.isConnected() && player != null && player.getGroupId() != null)
			{
				ItemSharePlayer playerToSave = player.toBuilder()
					.updatedDate(new Date())
					.build();

				String json = gson.toJson(playerToSave);
				Document data = Document.parse(json);

				connection.getCollection().updateOne(
					getUniqueIdFilter(playerToSave),
					new Document("$set", data),
					new UpdateOptions().upsert(true));

				onSuccess.run();
			}
			else
			{
				onFailure.run();
			}
		}
		catch (Exception e)
		{
			onFailure.run();
		}
	}

	public void getPlayers(String groupId, Consumer<List<ItemSharePlayer>> onSuccess, Runnable onFailure)
	{
		try
		{
			if (connection.isConnected())
			{
				FindIterable<Document> iterable = connection.getCollection().find(Filters.eq("groupId", groupId));
				List<Document> documents = new ArrayList<>();
				iterable.forEach(documents::add);

				List<ItemSharePlayer> players = documents.stream()
					.map(document -> gson.fromJson(document.toJson(), ItemSharePlayer.class))
					.collect(Collectors.toList());

				onSuccess.accept(players);
			}
			else
			{
				onFailure.run();
			}
		}
		catch (Exception e)
		{
			onFailure.run();
		}
	}

	private Bson getUniqueIdFilter(ItemSharePlayer player)
	{
		return Filters.and(
			Filters.eq("groupId", player.getGroupId()),
			Filters.eq("name", player.getName()));
	}
}
