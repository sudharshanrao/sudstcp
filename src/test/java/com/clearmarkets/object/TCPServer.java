package com.clearmarkets.object;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.clearmarkets.cleartcp.domain.message.GUIMessage;
import com.clearmarkets.cleartcp.scheduler.IScheduler;
import com.clearmarkets.cleartcp.tcpserver.TCPServerRunnable;

class TCPServer extends TCPServerRunnable<GUIMessage> {
	private final Map<String, IScheduler> mapOfClientWorkerThreads = new ConcurrentHashMap<>();
	public TCPServer(String serverName, int serverPort) throws IOException {
		super(serverName, serverPort);
	}

	public static void main(String argv[]) throws Exception  {
		TCPServer server = new TCPServer("tcpServer",6789);
		new Thread(server).start();
	}
	
	public void run(){
        
        while (!Thread.currentThread().isInterrupted()) {
        	try{
                Runnable clientHandler = new ClientHandler(getClientSocket(), mapOfClientWorkerThreads);
                new Thread(clientHandler).start();
        	}
        	catch (Exception e) {
        		e.printStackTrace();
			}
        }
        System.out.println("Server Stopped.");
    }
	
	
}