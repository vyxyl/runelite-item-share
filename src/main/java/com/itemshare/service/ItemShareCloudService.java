package com.itemshare.service;

import com.itemshare.model.ItemShareContainer;
import com.itemshare.model.ItemShareItem;
import com.itemshare.model.ItemSharePlayer;

import java.util.ArrayList;

public class ItemShareCloudService
{
	public void saveLocalData(ItemSharePlayer localPlayer)
	{

	}

	public ArrayList<ItemSharePlayer> getOtherPlayers()
	{
		return new ArrayList<ItemSharePlayer>()
		{{
			add(ItemSharePlayer.builder()
				.userName("Apple")
				.bank(ItemShareContainer.builder()
					.items(new ArrayList<ItemShareItem>()
					{{
						add(ItemShareItem.builder().id(1).name("Rune Scimitar").quantity(4).build());
					}})
					.build())
				.build());
		}};
	}
}
