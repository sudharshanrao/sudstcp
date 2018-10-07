package com.clearmarkets.object;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.clearmarkets.cleartcp.domain.Constants;
import com.clearmarkets.cleartcp.domain.message.GUIMessage;
import com.clearmarkets.cleartcp.tcpclient.SimpleTCPClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TCPClient2 extends SimpleTCPClient<GUIMessage> {
	private static Gson gson;
	public TCPClient2(String hostname, int port, String serviceName)
			throws UnknownHostException, IOException {
		super(hostname, port, serviceName, Constants.DEFAULT_WEIGHT);
		gson = new GsonBuilder().create();
	}

	public static void main(String argv[]) throws Exception  {
		TCPClient2 client = new TCPClient2("localhost", 10000, "TCPClient2");
		GUIMessage messageRead = null;
		while(!Thread.currentThread().isInterrupted()){
			try{
				GUIMessage message = new GUIMessage();
				Map<String, Object> map = new HashMap<>(1);
				map.put("legalEntityId", 3);
				String mapJsonStr = gson.toJson(map, Map.class);
				message.setRequestMethodParam(mapJsonStr);
				message.setSourceServiceName(client.getClass().getName());
				message.setDestinationServiceName("TCPClient");
				System.out.println("message written to server is: "+message.toString());
				client.write(message);
				Thread.sleep(2000);
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