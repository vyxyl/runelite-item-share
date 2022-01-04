package com.itemshare.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static com.itemshare.constant.ItemShareConstants.AWS_GIM_STORAGE_API;
import static com.itemshare.constant.ItemShareConstants.AWS_PLAYER_API;
import static com.itemshare.constant.ItemShareConstants.AWS_PLAYER_NAMES_API;
import static com.itemshare.constant.ItemShareConstants.AWS_X_API_KEY;
import com.itemshare.model.ItemShareGIMStorage;
import com.itemshare.model.ItemShareGIMStorageLite;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.model.ItemSharePlayerLite;
import com.itemshare.service.ItemShareDataService;
import com.itemshare.service.ItemShareRestService;
import com.itemshare.state.ItemShareState;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemShareAPI
{
	private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	private final Logger logger = LoggerFactory.getLogger(ItemShareAPI.class);

	@Inject
	private ItemShareRestService httpService;

	public void savePlayer(String groupId, ItemSharePlayer player, Runnable result, Runnable onFailure)
	{
		try
		{
			if (!StringUtils.isEmpty(groupId) && player != null && !StringUtils.isEmpty(player.getName()))
			{
				Request request = buildSavePlayerRequest(groupId, player);

				httpService.call(request,
					json -> result.run(),
					error -> {
						logger.error("Failed to save player: " + error);
						onFailure.run();
					});
			}
			else
			{
				logger.error("Failed to save player: invalid request");
				onFailure.run();
			}
		}
		catch (Exception e)
		{
			logger.error("Failed to save player: " + e.getMessage());
			e.printStackTrace();
			onFailure.run();
		}
	}

	public void getPlayer(String groupId, String playerName, Consumer<ItemSharePlayer> result)
	{
		try
		{
			if (!StringUtils.isEmpty(groupId) && !StringUtils.isEmpty(playerName))
			{
				Request request = buildGetPlayerRequest(groupId, playerName);

				httpService.call(request,
					json -> toGetPlayerResponse(json, playerName, result),
					error -> {
						logger.error("Failed to get player: " + error);
					});
			}
			else
			{
				logger.error("Failed to get player: invalid request");
			}
		}
		catch (Exception e)
		{
			logger.error("Failed to get player: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void getPlayerNames(String groupId, Consumer<List<String>> result, Runnable onFailure)
	{
		try
		{
			if (!StringUtils.isEmpty(groupId))
			{
				Request request = buildGetPlayerNamesRequest(groupId);

				httpService.call(request,
					json -> result.accept(toGetPlayerNamesResponse(json)),
					error -> {
						logger.error("Failed to get player names: " + error);
						onFailure.run();
					});
			}
			else
			{
				logger.error("Failed to get player names: invalid request");
				onFailure.run();
			}
		}
		catch (Exception e)
		{
			logger.error("Failed to get player names: " + e.getMessage());
			e.printStackTrace();
			onFailure.run();
		}
	}

	public void getGIMStorage(String groupId, Consumer<ItemShareGIMStorage> result, Runnable onFailure)
	{
		try
		{
			if (!StringUtils.isEmpty(groupId))
			{
				Request request = buildGetGIMStorageRequest(groupId);

				httpService.call(request,
					json -> toGetGIMStorageResponse(json, result),
					error -> {
						logger.error("Failed to get GIM storage: " + error);
						onFailure.run();
					});
			}
			else
			{
				logger.error("Failed to get GIM storage: invalid request");
				onFailure.run();
			}
		}
		catch (Exception e)
		{
			logger.error("Failed to get GIM storage: " + e.getMessage());
			onFailure.run();
			e.printStackTrace();
		}
	}

	public void saveGIMStorage(String groupId, ItemShareGIMStorage storage, Runnable result, Runnable onFailure)
	{
		try
		{
			if (!StringUtils.isEmpty(groupId) && storage != null)
			{
				Request request = buildSaveGIMStorageRequest(groupId, storage);

				httpService.call(request,
					json -> result.run(),
					error -> {
						logger.error("Failed to save GIM storage: " + error);
						onFailure.run();
					});
			}
			else
			{
				logger.error("Failed to save GIM storage: invalid request");
				onFailure.run();
			}
		}
		catch (Exception e)
		{
			logger.error("Failed to save GIM storage: " + e.getMessage());
			e.printStackTrace();
			onFailure.run();
		}
	}

	private Request buildSavePlayerRequest(String groupId, ItemSharePlayer player)
	{
		return new Request.Builder()
			.url(AWS_PLAYER_API + "?groupId=" + groupId + "&playerName=" + player.getName())
			.addHeader("x-api-key", AWS_X_API_KEY)
			.post(toSavePlayerRequest(player))
			.build();
	}

	private Request buildGetPlayerRequest(String groupId, String playerName)
	{
		return new Request.Builder()
			.url(AWS_PLAYER_API + "?groupId=" + groupId + "&playerName=" + playerName)
			.addHeader("x-api-key", AWS_X_API_KEY)
			.get()
			.build();
	}

	private Request buildGetPlayerNamesRequest(String groupId)
	{
		return new Request.Builder()
			.url(AWS_PLAYER_NAMES_API + "?groupId=" + groupId)
			.addHeader("x-api-key", AWS_X_API_KEY)
			.get()
			.build();
	}

	private Request buildSaveGIMStorageRequest(String groupId, ItemShareGIMStorage storage)
	{
		return new Request.Builder()
			.url(AWS_GIM_STORAGE_API + "?groupId=" + groupId)
			.addHeader("x-api-key", AWS_X_API_KEY)
			.post(toSaveGIMStorageRequest(storage))
			.build();
	}

	private Request buildGetGIMStorageRequest(String groupId)
	{
		return new Request.Builder()
			.url(AWS_GIM_STORAGE_API + "?groupId=" + groupId)
			.addHeader("x-api-key", AWS_X_API_KEY)
			.get()
			.build();
	}

	private RequestBody toSavePlayerRequest(ItemSharePlayer player)
	{
		ItemSharePlayerLite lite = ItemShareDataService.toPlayerLite((player));

		return RequestBody.create(
			MediaType.parse("application/json; charset=utf-8"),
			gson.toJson(lite));
	}

	private RequestBody toSaveGIMStorageRequest(ItemShareGIMStorage storage)
	{
		ItemShareGIMStorageLite lite = ItemShareDataService.toGimStorageLite((storage));

		return RequestBody.create(
			MediaType.parse("application/json; charset=utf-8"),
			gson.toJson(lite));
	}

	private void toGetPlayerResponse(String json, String playerName, Consumer<ItemSharePlayer> result)
	{
		ItemSharePlayerLite lite = gson.fromJson(json, ItemSharePlayerLite.class);

		ItemShareState.clientThread.invokeLater(() -> {
			ItemSharePlayer player = ItemShareDataService.toPlayer(lite, playerName);
			result.accept(player);
		});
	}

	private void toGetGIMStorageResponse(String json, Consumer<ItemShareGIMStorage> result)
	{
		ItemShareGIMStorageLite lite = gson.fromJson(json, ItemShareGIMStorageLite.class);

		ItemShareState.clientThread.invokeLater(() -> {
			ItemShareGIMStorage storage = ItemShareDataService.toGimStorage(lite);
			result.accept(storage);
		});
	}

	private List<String> toGetPlayerNamesResponse(String json)
	{
		String[] names = gson.fromJson(json, String[].class);
		return Arrays.stream(names).collect(Collectors.toList());
	}
}