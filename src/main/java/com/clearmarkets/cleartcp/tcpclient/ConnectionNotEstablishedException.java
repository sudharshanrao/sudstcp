/**
 * 
 */
package com.clearmarkets.cleartcp.tcpclient;

/**
 * @author sunil
 *
 */
public class ConnectionNotEstablishedException extends TcpException {

	private static final long serialVersionUID = 1L;
	
	public ConnectionNotEstablishedException(String message) {
		super(message);
	}

}
