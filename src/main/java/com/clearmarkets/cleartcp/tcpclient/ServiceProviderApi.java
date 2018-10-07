/**
 * 
 */
package com.clearmarkets.cleartcp.tcpclient;

import com.clearmarkets.cleartcp.domain.CMUser;

/**
 * @author sunil
 *
 */
public interface ServiceProviderApi {
	
	public void logout(CMUser user);
	
	public void login(CMUser user);
	
	public String getName();
	
	public void handleUndeliveredMessage(MessageUndeliveredException ex);
	
	public void setTcpAdapter(ServiceTcpAdapter tcpAdapter);

}
