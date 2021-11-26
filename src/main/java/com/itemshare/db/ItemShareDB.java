package com.itemshare.db;

import com.itemshare.model.ItemSharePlayer;
import java.util.List;
import java.util.function.Consumer;

public interface ItemShareDB
{
	boolean isCentralDB();
	boolean isConnected();
	ItemShareDBStatus getStatus();
	void connect(Runnable onSuccess, Runnable onFailure);
	void reconnect();
	void savePlayer(ItemSharePlayer player, Runnable onSuccess, Runnable onFailure);
	void getPlayers(String groupId, Consumer<List<ItemSharePlayer>> onSuccess, Runnable onFailure);
}
