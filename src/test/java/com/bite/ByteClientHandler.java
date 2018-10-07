package com.clearmarkets.bite;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

import com.clearmarkets.cleartcp.domain.message.GUIMessage;
import com.clearmarkets.cleartcp.scheduler.IScheduler;
import com.clearmarkets.cleartcp.tcpserver.ClientWorkerRunnable;

public class ByteClientHandler extends ClientWorkerRunnable<GUIMessage>{

	public ByteClientHandler(Socket clientSocket,
			Map<String, IScheduler> mapOfClientWorkerThreads)
			throws SocketException, IOException, ClassNotFoundException {
		super(clientSocket, mapOfClientWorkerThreads);
	}

	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()){
        	try {
            	byte[] buf = readBytesFromClient();
            	if(buf == null){
            		continue;
            	}
				System.out.println("message read from client"+buf.length);
				writeBytesToClient(buf);
        	}
        	catch(Exception e){
        		e.printStackTrace();
        	}
        }
	}
}