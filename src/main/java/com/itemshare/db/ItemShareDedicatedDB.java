package com.itemshare.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static com.itemshare.constant.ItemShareConstants.AWS_PLAYER_API;
import static com.itemshare.constant.ItemShareConstants.AWS_PLAYER_NAMES_API;
import static com.itemshare.constant.ItemShareConstants.AWS_X_API_KEY;
import com.itemshare.model.ItemShareItems;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.model.ItemSharePlayerLite;
import com.itemshare.model.ItemShareSlots;
import com.itemshare.service.ItemSharePlayerLiteService;
import com.itemshare.service.ItemShareRestService;
import java.util.Arrays;
import java.util.Date;
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

public class ItemShareDedicatedDB
{
	private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	private final Logger logger = LoggerFactory.getLogger(ItemShareDedicatedDB.class);

	@Inject
	private ItemShareRestService httpService;

	public void savePlayer(String groupId, ItemSharePlayer player, Runnable onSuccess, Runnable onFailure)
	{
		if (!StringUtils.isEmpty(groupId))
		{
			Request request = buildSavePlayerRequest(groupId, player);

			httpService.call(request,
				json -> onSuccess.run(),
				error -> {
					logger.error("Failed to save player: " + error);
					onFailure.run();
				});
		}
		else
		{
			onFailure.run();
		}
	}

	public void getPlayer(String groupId, String playerName, Consumer<ItemSharePlayerLite> onSuccess, Runnable onFailure)
	{
		if (!StringUtils.isEmpty(groupId))
		{
			Request request = buildGetPlayerRequest(groupId, playerName);

			httpService.call(request,
				json -> onSuccess.accept(toGetPlayerResponse(json)),
				error -> {
					logger.error("Failed to get player: " + error);
					onFailure.run();
				});
		}
		else
		{
			onFailure.run();
		}
	}

	public void getPlayerNames(String groupId, Consumer<List<String>> onSuccess, Runnable onFailure)
	{
		if (!StringUtils.isEmpty(groupId))
		{
			Request request = buildGetPlayerNamesRequest(groupId);

			httpService.call(request,
				json -> onSuccess.accept(toGetPlayerNamesResponse(json)),
				error -> {
					logger.error("Failed to get player names: " + error);
					onFailure.run();
				});
		}
		else
		{
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

	private RequestBody toSavePlayerRequest(ItemSharePlayer player)
	{
		ItemSharePlayerLite lite = ItemSharePlayerLiteService.toPlayerLite((player));

		return RequestBody.create(
			MediaType.parse("application/json; charset=utf-8"),
			gson.toJson(lite));
	}

	private ItemSharePlayerLite toGetPlayerResponse(String json)
	{
		return gson.fromJson(json, ItemSharePlayerLite.class);


	}

	private List<String> toGetPlayerNamesResponse(String json)
	{
		String[] names = gson.fromJson(json, String[].class);
		return Arrays.stream(names).collect(Collectors.toList());
	}
}
