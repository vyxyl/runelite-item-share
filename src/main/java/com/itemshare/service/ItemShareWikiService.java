package com.itemshare.service;

import com.itemshare.model.ItemShareItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import net.runelite.client.util.LinkBrowser;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;

public class ItemShareWikiService
{
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
			e.printStackTrace();
		}
	}
}