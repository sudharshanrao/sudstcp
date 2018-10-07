package com.clearmarkets.cleartcp.domain.message;

import java.util.Map;

import com.clearmarkets.cleartcp.domain.CMUser;

public class CacheRefreshMessage extends BaseTcpMessage {
	private static final long serialVersionUID = -8618026128628241322L;

	private Map<String, CMUser> userMap;
	// private String[] tokenForSelectPublish;
	// private Application[] publishToAllUsersForGivenApplications;

	public Map<String, CMUser> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, CMUser> userMap) {
		this.userMap = userMap;
	}

	// public String[] getTokenForSelectPublish() {
	// return tokenForSelectPublish;
	// }
	//
	// public void setTokenForSelectPublish(String[] tokenForSelectPublish) {
	// this.tokenForSelectPublish = tokenForSelectPublish;
	// }
	// public Application[] getPublishToAllUsersForGivenApplications() {
	// return publishToAllUsersForGivenApplications;
	// }
	// public void setPublishToAllUsersForGivenApplications(Application[]
	// publishToAllUsersForGivenApplications) {
	// this.publishToAllUsersForGivenApplications =
	// publishToAllUsersForGivenApplications;
	// }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CacheRefreshMessage [super=");
		builder.append(super.toString());
		builder.append(", userMap=");
		builder.append(userMap);
		// builder.append(", tokenForSelectPublish=");
		// builder.append(tokenForSelectPublish);
		// builder.append(", publishToAllUsersForGivenApplications=");
		// builder.append(publishToAllUsersForGivenApplications);
		// builder.append("]");
		return builder.toString();
	}

}
