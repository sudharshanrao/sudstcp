package com.clearmarkets.object;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.UUID;

import com.clearmarkets.cleartcp.domain.message.GUIMessage;
import com.clearmarkets.cleartcp.scheduler.IScheduler;
import com.clearmarkets.cleartcp.tcpserver.ClientWorkerRunnable;

public class ClientHandler extends ClientWorkerRunnable<GUIMessage>{

	public ClientHandler(Socket clientSocket,
			Map<String, IScheduler> mapOfClientWorkerThreads)
			throws SocketException, IOException, ClassNotFoundException {
		super(clientSocket, mapOfClientWorkerThreads);
	}

	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()){
        	try {
            	GUIMessage guiMessage = readFromClient();
            	if(guiMessage == null){
            		//System.out.println("guiMessage is null");
            		continue;
            	}
				System.out.println("message read from client"+guiMessage.toString());
				String params = guiMessage.getRequestMethodParam();
				System.out.println("params" + params);
				guiMessage.setToken(UUID.randomUUID().toString());
				sendToClient(guiMessage);
				System.out.println("message sent to client"+guiMessage.toString());
        	}
        	catch(Exception e){
        		e.printStackTrace();
        	}
        }
	}
}
