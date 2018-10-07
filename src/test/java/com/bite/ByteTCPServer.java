package com.bite;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cleartcp.domain.message.GUIMessage;
import com.cleartcp.scheduler.IScheduler;
import com.cleartcp.tcpserver.TCPServerRunnable;

class ByteTCPServer extends TCPServerRunnable<GUIMessage> {
	private final Map<String, IScheduler> mapOfClientWorkerThreads = new ConcurrentHashMap<>();
	public ByteTCPServer(String serverName, int serverPort) throws IOException {
		super(serverName, serverPort);
	}

	public static void main(String argv[]) throws Exception  {
		ByteTCPServer server = new ByteTCPServer("byteTcpServer",6789);
		new Thread(server).start();
	}
	
	public void run(){
        
        while (!Thread.currentThread().isInterrupted()) {
        	try{
                Runnable clientHandler = new ByteClientHandler(getClientSocket(), mapOfClientWorkerThreads);
                new Thread(clientHandler).start();
        	}
        	catch (Exception e) {
        		e.printStackTrace();
			}
        }
        System.out.println("Server Stopped.");
    }
	
	
}