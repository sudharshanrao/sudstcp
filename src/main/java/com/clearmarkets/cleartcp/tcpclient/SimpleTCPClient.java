package com.clearmarkets.cleartcp.tcpclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clearmarkets.cleartcp.domain.CMUser;
import com.clearmarkets.cleartcp.domain.Constants;
import com.clearmarkets.cleartcp.domain.Identification;
import com.clearmarkets.cleartcp.domain.LocalCacheEvent;
import com.clearmarkets.cleartcp.domain.message.CacheRefreshMessage;
import com.clearmarkets.cleartcp.domain.message.GUIMessage;
import com.clearmarkets.cleartcp.domain.message.MessageType;
import com.clearmarkets.cleartcp.enumeration.UserCacheEventEnum;
import com.clearmarkets.cleartcp.stream.StreamReaderWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class SimpleTCPClient<T extends Serializable> extends StreamReaderWriter{
	private static final Logger logger = LoggerFactory.getLogger(SimpleTCPClient.class);
	private static final int DEFAULT_WEIGHT = 10;
	private Socket clientSocket;
	private static long SLEEP_TIME_BETWEEN_RECONNECT = 5000;
	private String hostname;
	private int port;
	private String serviceName;
	private ScheduledExecutorService scheduler;
	private ScheduledExecutorService monitoringScheduler;
	private AtomicInteger heartBeatCounter = new AtomicInteger(0);
	// token - CMUser 
	private Map<String, CMUser> tokenUserCache = new ConcurrentHashMap<>();
	// CMUser - token
	private Map<CMUser, String> userTokenCache = new ConcurrentHashMap<>();
 	private static Gson gson;
 	private int weight = DEFAULT_WEIGHT;
 	
 	/**
 	 * constructor which instantiates and tries to connect to the tcp server.
 	 * @param hostname
 	 * @param port
 	 * @param serviceName
 	 * @throws UnknownHostException
 	 * @throws IOException
 	 */
	public SimpleTCPClient(String hostname, int port, String serviceName, int weight) throws UnknownHostException, IOException{
		this(hostname, port, serviceName, true, weight);
	}
	
	/**
	 * constructor which instantiates before connection to tcp server.
	 * Note that call to method initClient is necessary after invoking this constructor.
	 * @param hostname
	 * @param port
	 * @param serviceName
	 * @param initializeBeforeConnect: pass in false for initializing the bean without connecting to tcp server.
	 * @param weight: pass in with a weight
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public SimpleTCPClient(String hostname, int port, String serviceName, boolean initializeBeforeConnect, int weight) throws UnknownHostException, IOException {
		this.hostname = hostname;
		this.port = port;
		this.serviceName = serviceName;
		this.weight = weight;
		gson = new GsonBuilder().create();
		if(initializeBeforeConnect){
			initClient();
		}
	}
	
	/**
	 * @param hostname
	 * @param port
	 * @param serviceName
	 * @throws IOException
	 */
	public void initClient() {
		try {
			initializeClient();
			logger.info("Connected to server {}", this.hostname, this.port, new Date());
		} catch (UnknownHostException e) {
			logger.error("Unknown host configured for the tcp server {}", this.hostname);
		}
		catch(IOException e){
			logger.error("Not able to connect to TCP server {}",this.hostname, this.port);
			tryReconnect();
		}
	}

	/**
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private void initializeClient() throws IOException {
		clientSocket = new Socket(this.hostname, this.port);
		outputStreamToTCPServer = new DataOutputStream(clientSocket.getOutputStream());
		inputStreamFromTCPServer = new DataInputStream(clientSocket.getInputStream());
		writeFirstIdentificationMessage();
		startClientHeartBeatsToServer();
		monitorServerHeartBeats();
	}
	
	private void startClientHeartBeatsToServer() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
	    		try {
                    writeObject(System.currentTimeMillis(), MessageType.HEARTBEAT);
				} catch (IOException e) {
					logger.error("{}", e);
					tryReconnect();
				}
	    	}
		},
		Constants.HEARTBEAT_INTERVAL,
		Constants.HEARTBEAT_INTERVAL,
		TimeUnit.MILLISECONDS);
	}

	private void monitorServerHeartBeats() {
		monitoringScheduler = Executors.newSingleThreadScheduledExecutor();
		monitoringScheduler.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				if(heartBeatCounter.get() == 0){
					logger.error("We have a problem. Server has not sent heartbeat in {} milliseconds. Will disconnect now.", Constants.MISSED_HEARTBEAT_INTERVAL);
					try {
						//disconnect the connection to server
						clientSocket.close();
					} catch (IOException e) {
						logger.error("Exception in closing client socket connection",e);
					}
				}else{
					resetHeartbeatCounter();
				}
			}
			
			private void resetHeartbeatCounter() {
				heartBeatCounter.set(0);
			}
		},
		Constants.MISSED_HEARTBEAT_INTERVAL,
		Constants.MISSED_HEARTBEAT_INTERVAL,
		TimeUnit.MILLISECONDS);
	}
	
	/**
	 * writes object of Type T to this thread's DataOutputStream.
	 * Doesn't interfere with the read from the socket its a separate one-way channel from the InputStream. 
	 * @param t
	 * @throws IOException
	 */
	public void write(T t) throws IOException {
        if (outputStreamToTCPServer == null) {
			return;
		}
		try {
            writeObject(t, MessageType.APPLICATION);
		} 
		catch (SocketException e1) {
			logger.error("{}", e1);
			tryReconnect();
		}
	}
	
	public void writeBytesToServer(byte[] buf) throws IOException {
        if (outputStreamToTCPServer == null) {
			return;
		}
		try {
            writeBytes(buf);
		}
		catch (SocketException e1) {
			logger.error("{}", e1);
			tryReconnect();
		}
	}
	
	/**
	 * blocking call to read from the InputStream. It is the responsibility of the caller method to
	 * constantly read from the socket.  
	 * @returns null on invalid message type and HeartBeats therefore caller should check for null before processing message.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    @SuppressWarnings("unchecked")
    public T read() throws IOException, ClassNotFoundException {
		if(inputStreamFromTCPServer == null){
			return null;
		}
		T t = null;
		try {
            t = (T) readObject();
            if (t instanceof Long) {
                logger.trace("Heartbeat received from server with timestamp {} ", t);
                incrementHeartbeatCounter();
                return null;
            }
			else if (t instanceof CacheRefreshMessage) {
				CacheRefreshMessage message = (CacheRefreshMessage) t;
				updateLocalCache(message);
				return null;
            }
		} catch (EOFException | SocketException e) {
			logger.error("{} from {}", e, this.hostname, this.port);
			tryReconnect();
		}
		return t;
	}
	
    public byte[] readBytesFromServer() throws IOException {
    	if(inputStreamFromTCPServer == null){
			return null;
		}
		byte[] buf = null;
		try {
			 buf = readBytes();
			 if (buf != null && buf.length == 0) {
				incrementHeartbeatCounter();
				return null;
			 }
		} catch (SocketException e) {
			logger.error("{} from {}", e, this.hostname, this.port);
			tryReconnect();
		}
		return buf;
    }
    
	private void incrementHeartbeatCounter() {
		heartBeatCounter.getAndIncrement();
	}

	private void tryReconnect() {
		int counter = 0;
		while(true){
			try {
				counter++;
				cleanup();
				logger.info("{} TCP Connection lost so sleeping for {} milliseconds", serviceName, SLEEP_TIME_BETWEEN_RECONNECT * counter);
				Thread.sleep(SLEEP_TIME_BETWEEN_RECONNECT * counter);
				initializeClient();
				return;
			}
			catch (InterruptedException e) {
				logger.error("Thread interrupted",e);
			}
			catch(ConnectException e){
				logger.error("Not able to connect to TCP server {}",this.hostname, this.port);
			}
			catch(IOException e){
				logger.error("Exception",e);
			}
		}
	}

	protected void writeFirstIdentificationMessage() throws IOException{
		Identification identification = new Identification(this.serviceName, this.weight);
        writeObject(identification, MessageType.IDENTIFICATION);
	}
	
	private void cleanup(){
		clientSocket = null;
		outputStreamToTCPServer = null;
		inputStreamFromTCPServer = null;
		if(scheduler != null){
			scheduler.shutdown();
		}
		if(monitoringScheduler!=null){
			monitoringScheduler.shutdown();
		}
	}
	
	/**
	 * @param guiMessage 
	 * @return null so that client thread does not have to process cache refresh message.
	 */
	private void updateLocalCache(CacheRefreshMessage message) {
		UserCacheEventEnum userCacheEventEnum = UserCacheEventEnum.valueOf(message.getRequestMethod());
		Map<String, CMUser> mapOfUsersForEvent = new HashMap<>();
		switch(userCacheEventEnum){
			case ADD_USER_TO_CACHE:
			if (message.getUserMap().size() != 1) {
				throw new IllegalStateException("Trying to add a single user but more than that are in the map " + message);
			}
			for (CMUser user : message.getUserMap().values()) {
				addUserToCache(message.getToken(), user);
				mapOfUsersForEvent.put(message.getToken(), user);
			}
			break;
			case DELETE_USER_FROM_CACHE:
			CMUser removedUser = removeUserFromCache(message.getToken());
			mapOfUsersForEvent.put(message.getToken(), removedUser);
				break;
			case ADD_ALL_USERS_TO_CACHE:
			Map<String, CMUser> map = message.getUserMap();
				addAllUsersToCache(map);
				mapOfUsersForEvent.putAll(map);
				break;
			case REMOVE_ALL_USERS_FROM_CACHE:
				mapOfUsersForEvent.putAll(removeAllUsersFromCache());
				break;
			default:
			logger.error("Unknown cache refresh event {}", message);
				break;
		}
		LocalCacheEvent localCacheEvent = new LocalCacheEvent(userCacheEventEnum, mapOfUsersForEvent);
		onCacheEvent(localCacheEvent, message);
	}
	
	private Map<String, CMUser> removeAllUsersFromCache() {
		Map<String, CMUser> userMap = new HashMap<>(tokenUserCache);
		tokenUserCache.clear();
		logger.debug("removing {} user(s) from local cache", userTokenCache.size());
		userTokenCache.clear();
		return userMap;
	}

	private void addUserToCache(String token, CMUser newUser){
		tokenUserCache.put(token, newUser);
        userTokenCache.put(newUser, token);
        logger.debug("added user and token to local cache {}, {}", newUser, token);
	}
	
	private CMUser removeUserFromCache(String token){
		CMUser removedUser = tokenUserCache.remove(token);
        if (removedUser == null) {
            logger.debug("Can't find user to remove {}, {}", removedUser, token);
            return null;
        }
        userTokenCache.remove(removedUser);
        logger.debug("removed user and token from local cache {}, {}", removedUser, token);
        return removedUser;
	}
	
	private void addAllUsersToCache(Map<String, CMUser> map){
		for(Entry<String,CMUser> entry: map.entrySet()){
			addUserToCache(entry.getKey(), entry.getValue());
		}
		logger.debug("added {} user(s) into local cache",map.size());
	}
	
	/**
	 * get the CMUser object by authentication token.
	 * @param token
	 * @return
	 */
	public CMUser getUserByToken(String token){
		CMUser user = tokenUserCache.get(token);
		return user;
	}
	
	/**
	 * gives a map of token and users.
	 * @return
	 */
	public Map<String, CMUser> getAllUsers(){
		return new HashMap<String, CMUser>(tokenUserCache);
	}
	
	/**
	 * returns true if user is logged in.
	 */
	public boolean isUserLoggedIn(CMUser cmUser){
		if(cmUser == null){
			return false;
		}
		return userTokenCache.containsKey(cmUser);
	}
	/**
	 * returns the authentication token for the loginId.
	 * @param loginId
	 * @return
	 */
	public String getTokenByUser(CMUser cmUser){
		if(cmUser == null){
			return null;
		}
		return userTokenCache.get(cmUser);
	}
	
	/**
	 * This method writes to the routing server by looking up the token by loginId.
	 * Note that the existing token will be overwritten on the guiMessage therefore this method
	 * should be used only for push notifications to GUI (when service does not know the token).
	 * @param guiMessage
	 * @param loginId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void write(GUIMessage guiMessage, CMUser cmUser) throws Exception {
		if(guiMessage == null){
			throw new Exception("Nothing to write as object is null. User = "+cmUser);
		}
		if(cmUser == null){
			throw new Exception("User unknown. Cannot write to routing server. User = "+cmUser);
		}
		String token = getTokenByUser(cmUser);
		if(token == null){
			throw new Exception("User not logged in. Cannot write to routing server. User = "+cmUser);
		}
		guiMessage.setToken(token);
		write((T) guiMessage);
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void disconnect() throws IOException {
		clientSocket.close();
	}
	
	protected void onCacheEvent(LocalCacheEvent localCacheEvent, CacheRefreshMessage message){};
}