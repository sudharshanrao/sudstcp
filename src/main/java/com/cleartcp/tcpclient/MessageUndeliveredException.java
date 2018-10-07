/**
 * 
 */
package com.cleartcp.tcpclient;

import com.cleartcp.domain.message.GUIMessage;

/**
 * @author sunil
 *
 */
public class MessageUndeliveredException extends TcpException {

	private static final long serialVersionUID = 1L;
	private final GUIMessage undelieveredMsg;
	
	public MessageUndeliveredException(GUIMessage undelieveredMsg) {
		super("");
		this.undelieveredMsg = undelieveredMsg;
	}
	
	public MessageUndeliveredException(GUIMessage undelieveredMsg, String reasonStr) {
		super(reasonStr);
		this.undelieveredMsg = undelieveredMsg;
	}

	public GUIMessage getUndelieveredMsg() {
		return undelieveredMsg;
	}

}
