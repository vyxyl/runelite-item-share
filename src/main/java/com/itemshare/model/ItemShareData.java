package com.itemshare.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemShareData
{
    private ItemSharePlayer localPlayer;
    private List<ItemSharePlayer> otherPlayers;
}
