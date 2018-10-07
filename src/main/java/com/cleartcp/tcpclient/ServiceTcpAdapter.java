package com.cleartcp.tcpclient;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.cleartcp.domain.CMUser;
import com.cleartcp.domain.Constants;
import com.cleartcp.domain.GUIException;
import com.cleartcp.domain.LocalCacheEvent;
import com.cleartcp.domain.message.GUIMessage;
import com.cleartcp.enumeration.CMException;

public class ServiceTcpAdapter {
	private final static Logger LOGGER = LoggerFactory.getLogger(ServiceTcpAdapter.class);

	//private static final boolean DEBUG = logger.isDebugEnabled();
	
	protected volatile SimpleTCPClient<GUIMessage> server;
	private Thread readerThread;
	
	private final String host;
	private final int port;
	private final String serviceName;
	private final Map<Object, ServiceInfo> serviceInfoList = new ConcurrentHashMap<>();
	private final Map<String, ServiceCall> serviceCalls = new ConcurrentHashMap<>();

	
	public ServiceTcpAdapter(String serviceName, String host, int port,
			List<ServiceInfo> serviceInfos) {
		this.host = host;
		this.port = port;
		this.serviceName = serviceName;
		for (ServiceInfo s : serviceInfos) {
			addService(s);
		}
	}
	
	public ServiceTcpAdapter(String serviceName, String host, int port) {
		this.host = host;
		this.port = port;
		this.serviceName = serviceName;
	}
	
	private SimpleTCPClient<GUIMessage> connectToServer(String serviceName, String host , int port) {
		while (true) {
			try {
				LOGGER.info("Connecting to routing serevr at-" + host + ":" + port );
				return new SimpleTCPClient<GUIMessage>(host, port, serviceName, Constants.DEFAULT_WEIGHT) {

					protected void onCacheEvent(LocalCacheEvent localCacheEvent, GUIMessage m){
						switch (localCacheEvent.getUserCacheEventEnum()) {
						case DELETE_USER_FROM_CACHE:
							for (CMUser u : localCacheEvent.getMapOfUsers().values()) {
								for (ServiceInfo info : serviceInfoList.values()) {
									info.service.logout(u);
								}
							}
							break;
						case REMOVE_ALL_USERS_FROM_CACHE:
							for (CMUser u : localCacheEvent.getMapOfUsers().values()) {
								for (ServiceInfo info : serviceInfoList.values()) {
									info.service.logout(u);
								}
							}
							break;
						case ADD_USER_TO_CACHE:
							for (CMUser u : localCacheEvent.getMapOfUsers().values()) {
								for (ServiceInfo info : serviceInfoList.values()) {
									info.service.login(u);	
								}
							}
							break;
						case ADD_ALL_USERS_TO_CACHE:
							for (CMUser u : localCacheEvent.getMapOfUsers().values()) {
								for (ServiceInfo info : serviceInfoList.values()) {
									info.service.login(u);
								}
							}
							break;
							default:
								//do nothing
						}
						
					};
					
				};
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					Thread.currentThread().interrupt();
					return null;
				}
			}
		}
	}
	
	private Map<String, ServiceCall> initMethodsMap(ServiceProviderApi service, Class<?>[] interfaces) {
		String key;
		String keyCapsStr;
		Class<?> paramType;
		Map<String, ServiceCall> serviceMethods = new HashMap<>();
		for (Class<?> clazz : interfaces) {
			for (Method m : clazz.getMethods()) {
				key = m.getName();
				keyCapsStr = getCapsStr(key);
				if (m.getParameterTypes().length > 0) {
					if (m.getParameterTypes().length > 3) {
						throw new IllegalStateException("Not more than 3 argument method should be declared");
					}
					
					if (m.getParameterTypes().length == 3) {
						if ( !GUIMessage.class.isAssignableFrom(m.getParameterTypes()[2])) {
							throw new IllegalStateException("3 argument method must have third argument of type " + GUIMessage.class.getSimpleName()
									+ " : " + Arrays.toString(m.getParameterTypes()));
						}
					} 
					
					if (m.getParameterTypes().length >= 2) {
						if ( !CMUser.class.isAssignableFrom(m.getParameterTypes()[1])) {
							throw new IllegalStateException("2 or more argument method must have second argument of type " + CMUser.class.getSimpleName()
									+ " : " + Arrays.toString(m.getParameterTypes()));
						}
					}
					paramType = m.getParameterTypes()[0];//expecting only one parameter apart from CMUser and GUIMessage
					if (!CMUser.class.isAssignableFrom(paramType)) {//if only 1 parameter it could be CMUser or request parameter type coming in GUIMessage 
						key = key + ":" + paramType.getSimpleName();
						keyCapsStr = keyCapsStr + ":" + paramType.getSimpleName();
					}
				}
				if (serviceMethods.get(key) != null) {
					throw new IllegalStateException("Multiple calls for: " + key + " in given services");
				}
				ServiceCall call = new ServiceCall(service, m);
				serviceMethods.put(key, call);
				serviceMethods.put(keyCapsStr, call);
			}
		}
		return serviceMethods;
	}

	private String getCapsStr(String key) {
		char[] data = key.toCharArray();
		StringBuilder sb= new StringBuilder(data.length+5);
		boolean first = true;
		final int diff = 'A' - 'a';
		for (char c : data) {
			if (first) {
				first= false;
			} else if (c >= 'A' && c <= 'Z') {
				sb.append('_');
			}
			
			if (c >= 'a' && c <= 'z') {
				c+= diff; 
			}
			sb.append(c);
		}
		
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	protected <T> void runUntilInteruppted(String serviceName) {
		String key;
		ServiceCall call;
		Object[] noArg = new Object[0];
		Object response;
		GUIMessage messageRead;
		Object requestParam;
		//int uSeq = -1;
		CMUser user;
		Map<String, ?> paramMap;
		while (!Thread.currentThread().isInterrupted()) {
			try {
				messageRead = server.read();
			} catch (Throwable e) {
				LOGGER.error("Stacktrace", e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					return;
				}
				continue;
			}
			if (messageRead == null) {
				continue;
			}
			try {
				
				user = server.getUserByToken(messageRead.getToken());
				if (user != null) {
					MDC.put("userId", user.getLoginId());
				}
				LOGGER.debug("Received: {}", messageRead);
				response = null;
				key = messageRead.getRequestMethod();
				requestParam = messageRead.getRequestMethodParam();
				if (requestParam instanceof Map) {
					paramMap = (Map<String, ?>) requestParam;
					if (paramMap.size() == 1) {
						requestParam = paramMap.entrySet().iterator().next().getValue();
						if (requestParam instanceof MessageUndeliveredException) {
							LOGGER.error("TcpMessage not delivered--{}", ((MessageUndeliveredException)requestParam).
									getUndelieveredMsg());
							continue;
						}
					} else if (paramMap.size() == 0) {
						requestParam = null;
					}
				}
				
				if (requestParam != null) {
					call = serviceCalls.get(key + ":" + requestParam.getClass().getSimpleName());
					if (call != null) {
						key = key + ":" + requestParam.getClass().getSimpleName();
					} else {
						call = serviceCalls.get(key + ":" + String.class.getSimpleName());
						if (call != null) {
							LOGGER.info("Received request param as non-string. Expecting string for given method name..using toString to convert to string");
							key = key + ":" + String.class.getSimpleName();
							requestParam = requestParam.toString();
						}
					}
				} 
				call = serviceCalls.get(key);
				
				
				if (call != null) {
					if (requestParam != null) {
						switch (call.m.getParameterTypes().length) {
						case 1:
							response = call.m.invoke(call.service, requestParam);
							break;
						case 2:
							response = call.m.invoke(call.service, requestParam, user);
							break;
						case 3:
							response = call.m.invoke(call.service, requestParam, user, messageRead);
							break;
						}
					} else {
						if (call.m.getParameterTypes().length == 0) {
							response = call.m.invoke(call.service, noArg);
						} else if (call.m.getParameterTypes().length == 1) {
							response = call.m.invoke(call.service, user);
						} else if (call.m.getParameterTypes().length == 2) {
							response = call.m.invoke(call.service, user, messageRead);
						} else {
							LOGGER.error("A method with parameter is called without sending request parameter");
						}
					}
					if (response != null) {
						messageRead.setJSONFormattedResponse(response);
						server.write(messageRead);
					}
				} else {
					LOGGER.error("Incorrect method {} - {}" , messageRead.getRequestMethod(), key);
				}
				
				if (response != null) {
					messageRead.setJSONFormattedResponse(response);
					server.write(messageRead);
				}
				
				
			} catch (Throwable e) {
                LOGGER.error("Exception encountered during message processing - " + messageRead, e);
				messageRead.setStatus(false);
				messageRead.setGuiException(new GUIException(CMException.EXCEPTION_WHILE_PROCESSING_REQUEST));
			}
		}
	}
	
	public synchronized void addService(ServiceInfo serviceInfo) {
		if (!this.serviceInfoList.containsKey(serviceInfo.service)) {
			LOGGER.info("{}: Service provider instance registered {}", serviceName, serviceInfo);
			this.serviceInfoList.put(serviceInfo.service, serviceInfo);
			this.serviceCalls.putAll(initMethodsMap(serviceInfo.service, serviceInfo.interfaces));
			serviceInfo.service.setTcpAdapter(this);
		}
	}
	
	public synchronized void startService() {
		if (isConnected()) {
			LOGGER.info("Already connected to Routing Server");
			return;
		}
		LOGGER.info("Connecting to Routing Server...");
		final String serviceName = this.serviceName;
		final String host = this.host;
		final int port = this.port;
		readerThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				server = connectToServer(serviceName, host , port);
				if (server != null) {
					LOGGER.info("Connected to Routing Server");
					runUntilInteruppted(serviceName);
				} else {
					LOGGER.error("Could not connect to routing service for name " + serviceName + " check all configuration is right and server is up at- " + host+ ":" + port);
					throw new IllegalStateException("Could not connect to server");
				}
				
			}
		}, serviceName);
		readerThread.start();
	}
	
	public synchronized void stopService() {
		if (server != null) {
			try {
				server.disconnect();
				server = null;
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		
		if (readerThread != null) {
			LOGGER.info("Sending interrupt signal to - " + readerThread.getName());
			readerThread.interrupt();
			readerThread = null;
		}
	}

	public boolean isConnected() {
		return server != null && readerThread != null && readerThread.isAlive();
	}

	public void write(GUIMessage guiMessage) throws ConnectionNotEstablishedException {
		if (isConnected()) {
			if (guiMessage == null) {
				return;
			}
			try {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Sending GUIMessage-{}", guiMessage);
				}
				server.write(guiMessage);
			} catch (IOException e) {
				LOGGER.error("Error while writing message-" + guiMessage, e);
			} catch (Throwable e) {
				LOGGER.error("Error while writing message-" + guiMessage, e);
			}
		} else {
			throw new ConnectionNotEstablishedException(serviceName + "-" + host+":"+port);
		}
		
	}
	
	public static class ServiceInfo {
		final ServiceProviderApi service;
		//final String serviceName;
		Class<?>[] interfaces;
		
		public ServiceInfo(final ServiceProviderApi service, Class<?>... interfaces) {
			this.service = service;
			//this.serviceName = serviceName;
			this.interfaces = interfaces;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(service.getClass().getSimpleName());
			sb.append("-{");
			for (Class<?> c : interfaces) {
				sb.append(c.getSimpleName());
			}
			sb.append('}');
			return sb.toString();
		}
	}
	
	public static class ServiceCall {
		final ServiceProviderApi service;
		final Method m;
		
		public ServiceCall(ServiceProviderApi service,	final Method m) {
			this.service = service;
			this.m = m;
		}
	}
}
