package com.itemshare.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static com.itemshare.constant.ItemShareConstants.AWS_BASE_URL;
import static com.itemshare.constant.ItemShareConstants.AWS_X_API_KEY;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.service.ItemShareRestService;
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

public class ItemShareCentralDB implements ItemShareDB
{
	private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	private final Logger logger = LoggerFactory.getLogger(ItemShareCentralDB.class);

	@Inject
	private ItemShareRestService httpService;

	private Runnable onSuccess;
	private Runnable onFailure;

	public boolean isConnected()
	{
		return true;
	}

	public ItemShareDBStatus getStatus()
	{
		return ItemShareDBStatus.CONNECTED;
	}

	public void setCallbacks(Runnable onSuccess, Runnable onFailure)
	{
		this.onSuccess = onSuccess;
		this.onFailure = onFailure;
	}

	public void connect()
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
			Request request = buildSaveRequest(player);

			httpService.call(request,
				json -> {
					onSuccess.run();
				},
				error -> {
					logger.error("Failed to Save player: " + error);
					onFailure.run();
				});
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
			Request request = buildGetRequest(groupId);

			httpService.call(request,
				json -> {
					onSuccess.accept(toPlayers(json));
				},
				error -> {
					logger.error("Failed to Get players: " + error);
					onFailure.run();
				});
		}
		else
		{
			onFailure.run();
		}
	}

	private Request buildSaveRequest(ItemSharePlayer player)
	{
		return new Request.Builder()
			.url(AWS_BASE_URL + "?groupId=" + player.getGroupId())
			.addHeader("x-api-key", AWS_X_API_KEY)
			.post(toBody(player))
			.build();
	}

	private Request buildGetRequest(String groupId)
	{
		return new Request.Builder()
			.url(AWS_BASE_URL + "?groupId=" + groupId)
			.addHeader("x-api-key", AWS_X_API_KEY)
			.get()
			.build();
	}

	private RequestBody toBody(ItemSharePlayer player)
	{
		return RequestBody.create(
			MediaType.parse("application/json; charset=utf-8"),
			gson.toJson(player));
	}

	private List<ItemSharePlayer> toPlayers(String json)
	{
		ItemSharePlayer[] players = gson.fromJson(json, ItemSharePlayer[].class);
		return Arrays.stream(players).collect(Collectors.toList());
	}
}
