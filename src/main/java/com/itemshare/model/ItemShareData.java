package com.itemshare.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemShareData
{
    private List<ItemSharePlayer> players;
}
