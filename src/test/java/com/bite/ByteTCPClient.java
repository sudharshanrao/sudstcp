package com.bite;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.commons.lang.SerializationUtils;

import com.cleartcp.domain.Constants;
import com.cleartcp.domain.message.GUIMessage;
import com.cleartcp.tcpclient.SimpleTCPClient;


class ByteTCPClient extends SimpleTCPClient<GUIMessage> {
	public ByteTCPClient(String hostname, int port, String serviceName)
			throws UnknownHostException, IOException {
		super(hostname, port, serviceName, Constants.DEFAULT_WEIGHT);
	}

	public static void main(String argv[]) throws Exception  {
		ByteTCPClient client = new ByteTCPClient("localhost", 6789, "byteTcpClient");
		byte[] messageRead = null;
		while(!Thread.currentThread().isInterrupted()){
			try{
				GUIMessage message = new GUIMessage();
				message.setSourceServiceName(client.getClass().getName());
				System.out.println("message written to server is: "+message.toString());
				byte[] bufToWrite = SerializationUtils.serialize(message);
				client.writeBytesToServer(bufToWrite);
				Thread.sleep(2000);
				if((messageRead = client.readBytesFromServer()) != null){
					System.out.println("message read in client is: "+(GUIMessage)SerializationUtils.deserialize(messageRead));
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}