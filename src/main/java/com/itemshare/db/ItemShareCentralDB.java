package com.itemshare.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itemshare.model.ItemSharePlayer;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Inject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemShareCentralDB implements ItemShareDB
{
	private final String X_API_KEY = "PXS5OXyQtb22QuJI5PKaL8Gg8MXNi56O4t5BGUoJ";
	private final String BASE_URL = "https://dcc7edfbqc.execute-api.us-east-2.amazonaws.com/prod/item-share";
	private final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

	private final Logger logger = LoggerFactory.getLogger(ItemShareCentralDB.class);
	private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	@Inject
	private OkHttpClient okHttpClient;

	@Inject
	private GsonBuilder gsonBuilder;

	public boolean isCentralDB()
	{
		return true;
	}

	public boolean isConnected()
	{
		return true;
	}

	public ItemShareDBStatus getStatus()
	{
		return ItemShareDBStatus.CONNECTED;
	}

	public void connect(Runnable onSuccess, Runnable onFailure)
	{
		onSuccess.run();
	}

	public void reconnect()
	{
		//
	}

	public void savePlayer(ItemSharePlayer player, Runnable onSuccess, Runnable onFailure)
	{
		if (player != null && player.getGroupId() != null)
		{
			Request request = new Request.Builder()
				.url(BASE_URL + "?groupId=" + player.getGroupId())
				.addHeader("x-api-key", X_API_KEY)
				.post(RequestBody.create(JSON_MEDIA_TYPE, gson.toJson(player)))
				.build();

			call(request, "Save player",
				json -> onSuccess.run(),
				errorString -> onFailure.run());
		}
		else
		{
			onFailure.run();
		}
	}

	public void getPlayers(String groupId, Consumer<List<ItemSharePlayer>> onSuccess, Runnable onFailure)
	{
		if (!StringUtils.isEmpty(groupId))
		{
			Request request = new Request.Builder()
				.url(BASE_URL + "?groupId=" + groupId)
				.addHeader("x-api-key", X_API_KEY)
				.get()
				.build();

			call(request, "Get players",
				json -> onSuccess.accept(toPlayers(json)),
				errorString -> onFailure.run());
		}
		else
		{
			onFailure.run();
		}
	}

	private List<ItemSharePlayer> toPlayers(String json)
	{
		ItemSharePlayer[] players = gson.fromJson(json, ItemSharePlayer[].class);
		return Arrays.stream(players).collect(Collectors.toList());
	}

	private void call(Request request, String message, Consumer<String> onSuccess, Consumer<String> onFailure)
	{
		okHttpClient.newCall(request).enqueue(new Callback()
		{
			@Override
			public void onFailure(Call call, IOException exception)
			{
				logger.error("Failed to " + message, exception);

				onFailure.accept("Failed to" + message);
			}

			@Override
			public void onResponse(Call call, Response response)
			{
				try
				{
					if (response.isSuccessful() && response.body() != null)
					{
						onSuccess.accept(response.body().string());
					}
					else
					{
						if (response.body() != null)
						{
							logger.error("Failed to" + message + response.body().string());
						}
						else
						{
							logger.error("Failed to" + message);
						}

						onFailure.accept("Failed to" + message);
					}
				}
				catch (Exception e)
				{
					onFailure.accept("Failed to" + message);
				}
				finally
				{
					response.close();
				}
			}
		});
	}
}
