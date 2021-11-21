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
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.bson.Document;

public class ItemShareMongoDB implements ServerMonitorListener
{
	@Inject
	private ItemShareMongoDBConnection connection;

	private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	public boolean isConnected()
	{
		return connection.isConnected();
	}

	public ItemShareMongoDBStatus getStatus()
	{
		return connection.getStatus();
	}

	public void connect(Runnable onSuccess, Runnable onFailure)
	{
		connection.connect(onSuccess, onFailure);
	}

	public void reconnect()
	{
		connection.reconnect();
	}

	public void savePlayer(ItemSharePlayer player)
	{
		try
		{
			if (connection.isConnected() && player != null)
			{
				Date updatedDate = new Date();
				ItemSharePlayer playerToSave = player.toBuilder()
					.updatedDate(updatedDate)
					.build();

				String json = gson.toJson(playerToSave);
				Document data = Document.parse(json);

				connection.getCollection().updateOne(
					Filters.eq("name", playerToSave.getName()),
					new Document("$set", data),
					new UpdateOptions().upsert(true));

				player.setUpdatedDate(updatedDate);
			}
		}
		catch (Exception e)
		{
			//
		}
	}

	public List<ItemSharePlayer> getPlayers()
	{
		try
		{
			if (connection.isConnected())
			{
				FindIterable<Document> iterable = connection.getCollection().find();
				List<Document> documents = new ArrayList<>();
				iterable.forEach(documents::add);

				return documents.stream()
					.map(document -> gson.fromJson(document.toJson(), ItemSharePlayer.class))
					.collect(Collectors.toList());
			}
			else
			{
				return new ArrayList<>();
			}
		}
		catch (Exception e)
		{
			return new ArrayList<>();
		}
	}
}
