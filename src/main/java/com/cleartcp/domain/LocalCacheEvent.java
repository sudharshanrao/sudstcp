package com.cleartcp.domain;

import java.util.Map;

import com.cleartcp.enumeration.UserCacheEventEnum;

public class LocalCacheEvent {
	private UserCacheEventEnum userCacheEventEnum;
	private Map<String, CMUser> mapOfUsers;
	
	public LocalCacheEvent(UserCacheEventEnum userCacheEventEnum, Map<String, CMUser> mapOfUsers) {
		this.userCacheEventEnum = userCacheEventEnum;
		this.setMapOfUsers(mapOfUsers);
	}
	
	public UserCacheEventEnum getUserCacheEventEnum() {
		return userCacheEventEnum;
	}
	public void setUserCacheEventEnum(UserCacheEventEnum userCacheEventEnum) {
		this.userCacheEventEnum = userCacheEventEnum;
	}

	public Map<String, CMUser> getMapOfUsers() {
		return mapOfUsers;
	}

	public void setMapOfUsers(Map<String, CMUser> mapOfUsers) {
		this.mapOfUsers = mapOfUsers;
	}

	@Override
	public String toString() {
		return "LocalCacheEvent [userCacheEventEnum=" + userCacheEventEnum
				+ ", mapOfUsers=" + mapOfUsers + "]";
	}
}
