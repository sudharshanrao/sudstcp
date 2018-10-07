package com.clearmarkets.cleartcp.tcpserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clearmarkets.cleartcp.domain.Constants;
import com.clearmarkets.cleartcp.domain.Identification;
import com.clearmarkets.cleartcp.domain.message.MessageType;
import com.clearmarkets.cleartcp.scheduler.CustomRoundRobinScheduler;
import com.clearmarkets.cleartcp.scheduler.IScheduler;
import com.clearmarkets.cleartcp.stream.StreamReaderWriter;

/**
 * Framework thread in the tcp server for the client worker which services the client socket.
 * @author Sudharshan
 *
 * @param <T>
 */

public abstract class ClientWorkerRunnable<T extends Serializable> extends StreamReaderWriter implements Runnable{

    private Socket clientSocket;
    private static final Logger logger = LoggerFactory.getLogger(ClientWorkerRunnable.class);
    private String serviceName;
    private Map<String, IScheduler> mapOfClientWorkerThreads;
	private AtomicInteger heartBeatCounter = new AtomicInteger(0);
	private ScheduledExecutorService scheduler;
	
    public ClientWorkerRunnable(Socket clientSocket, Map<String, IScheduler> mapOfClientWorkerThreads) throws IOException, ClassNotFoundException {
        this.clientSocket = clientSocket;
        inputStreamFromTCPServer = new DataInputStream(this.clientSocket.getInputStream());
        outputStreamToTCPServer = new DataOutputStream(this.clientSocket.getOutputStream());
        this.mapOfClientWorkerThreads = mapOfClientWorkerThreads;
        Identification identification = (Identification) readObject();
		setIdentification(identification);
		monitorClientHeartBeats();
	}

	public abstract void run();
	
	private void resetHeartbeatCounter() {
		heartBeatCounter.set(0);
	}
	
	private void incrementHeartbeatCounter(){
		heartBeatCounter.getAndIncrement();
	}
	
	/**
	 * method to read from client socket.
	 * @return object of type T. May return null on invalid message type, heartbeats or if socket is closed from client end.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public T readFromClient() throws ClassNotFoundException, IOException{
		T t = null;
		try {
            t = (T) readObject();
				if(t instanceof Long){
					logger.trace("Heartbeat received with timestamp {} from {}",t, getServiceName());
					incrementHeartbeatCounter();
					sendHeartbeatToClient();
					return null;
				}
		} catch (SocketException | EOFException e) {
			interruptThreadAndStopHeartbeatScheduler(e);
		}
		return t;
	}
	
	public byte[] readBytesFromClient() throws IOException{
		byte[] buf = null;
		try {
			 buf = readBytes();
			 if (buf != null && buf.length == 0) {
				incrementHeartbeatCounter();
				sendHeartbeatToClient();
				return null;
			 }
		} catch (SocketException | EOFException e) {
			interruptThreadAndStopHeartbeatScheduler(e);
		}
		return buf;
	}
	
	public void writeBytesToClient(byte[] buf) throws IOException {
		try {
            writeBytes(buf);
		}
		catch (SocketException e) {
			interruptThreadAndStopHeartbeatScheduler(e);
		}
	}
	
	private void sendHeartbeatToClient() {
		try {
            writeObject(System.currentTimeMillis(), MessageType.HEARTBEAT);
		} catch (IOException e) {
			logger.error("Exception in sending heartbeat to client {}", e);
		}
	}

	/**
	 * method to write to the client socket.
	 * @param t
	 * @throws IOException
	 */
	public void sendToClient(T t) throws IOException{
		try {
            writeObject(t, MessageType.APPLICATION);
		} catch (SocketException e) {
			interruptThreadAndStopHeartbeatScheduler(e);
		}
	}

	/**
	 * @param e
	 */
	protected void interruptThreadAndStopHeartbeatScheduler(Exception e) {
		logError(e);
		cleanup();
		IScheduler scheduler = this.mapOfClientWorkerThreads.get(serviceName);
		if(scheduler != null){
			scheduler.remove(this);
		}
		Thread.currentThread().interrupt();
	}
	
	private void monitorClientHeartBeats() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable(){
			public void run() {
				if(heartBeatCounter.get() == 0){
					logger.error("We have a problem. Client has not sent heartbeat in {} milliseconds. Will disconnect now.", Constants.MISSED_HEARTBEAT_INTERVAL);
					try {
						//disconnect the client connection and wait for the client to connect.
						clientSocket.close();
					} catch (IOException e) {
						logger.error("Exception in closing client socket connection",e);
					}
				}else{
					logger.trace("Heartbeat received from {}",getServiceName());
					resetHeartbeatCounter();
				}
			}
		},
		Constants.MISSED_HEARTBEAT_INTERVAL,
		Constants.MISSED_HEARTBEAT_INTERVAL,
		TimeUnit.MILLISECONDS);
	}
	
	private void logError(Exception e){
		logger.error("{} from {} @ {}", e, serviceName, this.clientSocket.getInetAddress().getHostName());
		logger.error(e.getMessage(), e);
	}
	
	public String getServiceName() {
		return serviceName;
	}

	private void setIdentification(Identification identification) {
		logger.info("Client {} with {} registered at {} from {}", identification.getServiceName(), identification.getWeight(),
										new Date(), this.clientSocket.getInetAddress().getHostName());
		this.serviceName = identification.getServiceName();
		IScheduler scheduler = this.mapOfClientWorkerThreads.get(this.serviceName);
		if(scheduler == null){
			scheduler = new CustomRoundRobinScheduler();
			this.mapOfClientWorkerThreads.put(serviceName, scheduler);
		}
		scheduler.add(this, identification.getWeight());
		incrementHeartbeatCounter();
	}
	
	private void cleanup(){
		clientSocket = null;
        inputStreamFromTCPServer = null;
        outputStreamToTCPServer = null;
		if(scheduler != null){
			scheduler.shutdown();
		}
	}
}