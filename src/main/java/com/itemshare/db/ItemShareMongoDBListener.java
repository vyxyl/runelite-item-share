package com.itemshare.db;

import com.mongodb.event.ConnectionCheckOutFailedEvent;
import com.mongodb.event.ConnectionCheckOutStartedEvent;
import com.mongodb.event.ConnectionCheckedInEvent;
import com.mongodb.event.ConnectionCheckedOutEvent;
import com.mongodb.event.ConnectionClosedEvent;
import com.mongodb.event.ConnectionCreatedEvent;
import com.mongodb.event.ConnectionPoolClearedEvent;
import com.mongodb.event.ConnectionPoolClosedEvent;
import com.mongodb.event.ConnectionPoolCreatedEvent;
import com.mongodb.event.ConnectionPoolListener;
import com.mongodb.event.ConnectionReadyEvent;

public class ItemShareMongoDBListener implements ConnectionPoolListener
{
	private final Runnable ready;
	private final Runnable closed;

	ItemShareMongoDBListener(Runnable ready, Runnable closed)
	{
		this.ready = ready;
		this.closed = closed;
	}

	@Override
	public void connectionPoolCleared(ConnectionPoolClearedEvent event)
	{
		closed.run();
	}

	@Override
	public void connectionPoolClosed(ConnectionPoolClosedEvent event)
	{
		closed.run();
	}

	@Override
	public void connectionCheckOutFailed(ConnectionCheckOutFailedEvent event)
	{
		closed.run();
	}

	@Override
	public void connectionClosed(ConnectionClosedEvent event)
	{
		closed.run();
	}

	@Override
	public void connectionPoolCreated(ConnectionPoolCreatedEvent event)
	{
		ready.run();
	}

	@Override
	public void connectionCheckedIn(ConnectionCheckedInEvent event)
	{
		ready.run();
	}

	@Override
	public void connectionCreated(ConnectionCreatedEvent event)
	{
		ready.run();
	}

	@Override
	public void connectionReady(ConnectionReadyEvent event)
	{
		ready.run();
	}

	@Override
	public void connectionCheckOutStarted(ConnectionCheckOutStartedEvent event)
	{
	}

	@Override
	public void connectionCheckedOut(ConnectionCheckedOutEvent event)
	{
	}
}