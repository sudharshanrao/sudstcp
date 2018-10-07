package com.clearmarkets.object;

import java.io.IOException;
import java.net.UnknownHostException;

import com.clearmarkets.cleartcp.domain.Constants;
import com.clearmarkets.cleartcp.domain.message.GUIMessage;
import com.clearmarkets.cleartcp.tcpclient.SimpleTCPClient;


class TCPClient extends SimpleTCPClient<GUIMessage> {
	public TCPClient(String hostname, int port, String serviceName)
			throws UnknownHostException, IOException {
		super(hostname, port, serviceName, Constants.DEFAULT_WEIGHT);
	}

	public static void main(String argv[]) throws Exception  {
		TCPClient client = new TCPClient("localhost", 10000, "TCPClient");
		GUIMessage messageRead = null;
		while(!Thread.currentThread().isInterrupted()){
			try{
				GUIMessage message = new GUIMessage();
				message.setSourceServiceName(client.getClass().getName());
				message.setDestinationServiceName("TCPClient2");
				System.out.println("message written to server is: "+message.toString());
				client.write(message);
				Thread.sleep(1000);
				if((messageRead = client.read()) != null){
					System.out.println("message read in client is: "+messageRead);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}