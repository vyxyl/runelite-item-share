package com.itemshare.db;

import com.mongodb.event.ConnectionClosedEvent;
import com.mongodb.event.ConnectionPoolListener;
import com.mongodb.event.ConnectionReadyEvent;

public class ItemShareMongoDBListener implements ConnectionPoolListener
{
	private final Runnable onReady;
	private final Runnable onClosed;

	ItemShareMongoDBListener(Runnable onReady, Runnable onClosed)
	{
		this.onReady = onReady;
		this.onClosed = onClosed;
	}

	@Override
	public void connectionReady(ConnectionReadyEvent event)
	{
		onReady.run();
	}

	@Override
	public void connectionClosed(ConnectionClosedEvent event)
	{
		onClosed.run();
	}
}