package com.clearmarkets.cleartcp.tcpserver;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clearmarkets.cleartcp.stream.StreamReaderWriter;

/**
 * Framework thread for the tcp server
 * @author Sudharshan
 *
 * @param <T>
 */

public abstract class TCPServerRunnable<T extends Serializable> extends StreamReaderWriter implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(TCPServerRunnable.class);
	private int serverPort;
	private String serverName;
    private ServerSocket serverSocket;
    protected ExecutorService executorService = null;
	private Socket clientSocket;
    
    public TCPServerRunnable(String serverName, int serverPort) throws IOException{
    	this(serverName, serverPort, Runtime.getRuntime().availableProcessors() * 2);
    }
    
    public TCPServerRunnable(String serverName, int serverPort, int numOfThreads) throws IOException{
    	this.serverPort = serverPort;
    	this.serverName = serverName;
    	this.executorService = Executors.newFixedThreadPool(numOfThreads);
    	try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
        	logger.error("Cannot open port {}", this.serverPort, e);
        	System.exit(1);
        }
        logger.info("TCP server {} started on {}",this.serverName, this.serverPort,new Date());
    }
    
    public ExecutorService getExecutorService(){
    	return this.executorService;
    }
    
    public abstract void run();
    
    public Socket getClientSocket(){
    	try {
            clientSocket = this.serverSocket.accept();
        } catch (IOException e) {
            throw new RuntimeException("Error accepting client connection", e);
        }
    	return clientSocket;
    }
    
}