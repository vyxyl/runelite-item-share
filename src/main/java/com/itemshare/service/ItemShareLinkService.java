package com.itemshare.service;

import com.itemshare.db.ItemShareAPI;
import com.itemshare.model.ItemShareItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import net.runelite.client.util.LinkBrowser;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemShareLinkService
{
	private static final Logger logger = LoggerFactory.getLogger(ItemShareAPI.class);

	public static MouseAdapter getWikiMouseListener(ItemShareItem item)
	{
		return new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				goToWiki(item);
			}
		};
	}

	public static void goToWiki(ItemShareItem item)
	{
		try
		{
			if (item != null && !StringUtils.isEmpty(item.getName()))
			{
				HttpUrl.Builder urlBuilder = HttpUrl.parse("https://oldschool.runescape.wiki").newBuilder();
				urlBuilder.addPathSegments("w/Special:Lookup").addQueryParameter("name", item.getName());
				LinkBrowser.browse(urlBuilder.build().toString());
			}
		}
		catch (Exception e)
		{
			logger.warn("Failed to navigate to oldschool runescape wiki: ", e);
		}
	}

	public static void goToReadme()
	{
		try
		{
			LinkBrowser.browse(HttpUrl.parse("https://github.com/vyxyl/item-share#readme").newBuilder().build().toString());
		}
		catch (Exception e)
		{
			logger.warn("Failed to navigate to readme: ", e);
		}
	}
}