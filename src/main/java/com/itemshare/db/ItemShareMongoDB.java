package com.itemshare.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itemshare.model.ItemSharePlayer;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

public class ItemShareMongoDB
{
	@Inject
	private ItemShareMongoDBConnection connection;

	private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	public boolean isConnected()
	{
		return connection.isConnected();
	}

	public ItemShareDBStatus getStatus()
	{
		return connection.getStatus();
	}

	public void connect()
	{
		connection.connect();
	}

	public void disconnect()
	{
		connection.disconnect();
	}

	public void savePlayer(String groupId, ItemSharePlayer player, Runnable onSuccess, Runnable onFailure)
	{
		try
		{
			if (connection.isConnected() && player != null && !StringUtils.isEmpty(groupId))
			{
				ItemSharePlayer playerToSave = player.toBuilder()
					.updatedDate(new Date())
					.build();

				String json = gson.toJson(playerToSave);
				Document data = Document.parse(json);
				Bson filter = getPlayerFilter(groupId, player.getName());

				connection.getCollection().updateOne(
					filter,
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

	public void getPlayer(String groupId, String playerName, Consumer<ItemSharePlayer> onSuccess, Runnable onFailure)
	{
		try
		{
			if (connection.isConnected())
			{
				Bson filter = getPlayerFilter(groupId, playerName);
				FindIterable<Document> iterable = connection.getCollection().find(filter);
				String json = Objects.requireNonNull(iterable.first()).toJson();
				ItemSharePlayer player = gson.fromJson(json, ItemSharePlayer.class);

				onSuccess.accept(player);
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

	public void getPlayerNames(String groupId, Consumer<List<String>> onSuccess, Runnable onFailure)
	{
		try
		{
			if (connection.isConnected())
			{
				Bson filter = Filters.eq("groupId", groupId);
				Bson projection = Projections.fields(Projections.include("name"));
				FindIterable<Document> iterable = connection.getCollection().find(filter).projection(projection);
				List<Document> documents = new ArrayList<>();
				iterable.forEach(documents::add);

				List<String> names = documents.stream()
					.map(document -> gson.fromJson(document.toJson(), ItemSharePlayer.class))
					.map(ItemSharePlayer::getName)
					.collect(Collectors.toList());

				onSuccess.accept(names);
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

	private Bson getPlayerFilter(String groupId, String playerName)
	{
		return Filters.and(
			Filters.eq("groupId", groupId),
			Filters.eq("name", playerName));
	}
}
