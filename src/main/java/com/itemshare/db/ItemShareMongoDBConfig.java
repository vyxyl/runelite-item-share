package com.itemshare.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemShareMongoDBConfig
{
	String clusterDomain;
	String databaseName;
	String collectionName;
	String username;
	String password;

	public boolean isComplete()
	{
		return !StringUtils.isEmpty(clusterDomain)
			&& !StringUtils.isEmpty(databaseName)
			&& !StringUtils.isEmpty(collectionName)
			&& !StringUtils.isEmpty(username)
			&& !StringUtils.isEmpty(password);
	}
}