package com.itemshare.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemSharePlayer
{
	private String name;
	private ItemShareContainer bank;
	private ItemShareContainer inventory;
	private ItemShareContainer equipment;
	private Date updatedDate;
}
