package com.clearmarkets.cleartcp.domain.message;

import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clearmarkets.cleartcp.domain.CMUser;
import com.clearmarkets.cleartcp.domain.Constants;
import com.clearmarkets.cleartcp.domain.GUIException;
import com.clearmarkets.cleartcp.enumeration.Application;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class GUIMessage extends BaseTcpMessage {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(GUIMessage.class);
	private static final long serialVersionUID = -8618026128628241322L;
	private String response;

	private String requestMethodParam;
	private CMUser authenticationParam;
	private boolean status = true; // true for ok, false for exception/error
	private GUIException guiException;
	private String[] tokenForSelectPublish;
	private Application[] publishToAllUsersForGivenApplications;
	
	private transient final static ObjectMapper MAPPER = new ObjectMapper();
	static {
		{
			MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			MAPPER.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
			MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			MAPPER.setTimeZone(TimeZone.getTimeZone(Constants.DEFAULT_TIME_ZONE));
		}
	}
	private transient JsonNode mapNode;

	public String getRequestMethodParam() {
		return requestMethodParam;
	}
	public void setRequestMethodParam(String requestMethodParam) {
		this.requestMethodParam = requestMethodParam;
	}

	public String getResponse() {
		return response;
	}


	/**
	 * This should be avoided - prefer the jsonFormatting version
	 * 
	 * @param response
	 */
	@Deprecated
	public void setResponse(String response) {
		this.response = response;
	}
	public void setJSONFormattedResponse(Object response) {
		try {
			this.response = MAPPER.writeValueAsString(response);
		} catch (Exception e) {
			throw new IllegalStateException("Failed processing the json for response: " + response, e);
		}
	}

	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public GUIException getGuiException() {
		return guiException;
	}
	public void setGuiException(GUIException guiException) {
		this.guiException = guiException;
	}
	public String[] getTokenForSelectPublish() {
		return tokenForSelectPublish;
	}
	public Application[] getPublishToAllUsersForGivenApplications() {
		return publishToAllUsersForGivenApplications;
	}
	public void setTokenForSelectPublish(String[] tokenForSelectPublish) {
		this.tokenForSelectPublish = tokenForSelectPublish;
	}
	public void setPublishToAllUsersForGivenApplications(Application[] publishToAllUsersForGivenApplications) {
		this.publishToAllUsersForGivenApplications = publishToAllUsersForGivenApplications;
	}


	public CMUser getAuthenticationParam() {
		return authenticationParam;
	}

	public void setAuthenticationParam(CMUser authenticationParam) {
		this.authenticationParam = authenticationParam;
	}

	public <T> T parseParamUsingKey(String key, Class<T> clazz) {
		JsonNode value = null;
		try {

			if (mapNode == null) {
				mapNode = MAPPER.readTree(getRequestMethodParam());
			}
			value = mapNode.get(key);
			if (value == null) {
				return null;
			}
			return MAPPER.readValue(value.toString(), clazz);

		} catch (Exception e) {
			throw new IllegalStateException("Failed processing the json for key:" + key + ", value:"+value, e);
		}

	}

	public String parseParamUsingKey(String key) {
		return parseParamUsingKey(key, String.class);
	}

	public <T> T parseResponseAs(Class<T> clazz) {
		try {
			return MAPPER.readValue(response, clazz);
		} catch (Exception e) {
			throw new IllegalStateException("Failed processing the json response:" + response + ", class:" + clazz, e);
		}
	}

	@Deprecated
	/**
	 * This should be removed in favor of putting data on in a keyed map
	 * 
	 * @param messageRead
	 * @param clazz
	 * @return object of type class from the requestMethodParam
	 */
	public <T> T parseDirectlyFromRequestMethodParam(GUIMessage messageRead, Class<T> clazz) {
		try {
			return MAPPER.readValue(requestMethodParam, clazz);
		} catch (Exception e) {
			throw new IllegalStateException("Failed processing the json response:" + response + ", class:" + clazz, e);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GUIMessage [super=");
		builder.append(super.toString());
		builder.append(", response=");
		builder.append(response);
		builder.append(", requestMethodParam=");
		builder.append(requestMethodParam);
		builder.append(", authenticationParam=");
		builder.append("REDACTED");
		builder.append(", status=");
		builder.append(status);
		builder.append(", guiException=");
		builder.append(guiException);
		builder.append(", tokenForSelectPublish=");
		builder.append(tokenForSelectPublish);
		builder.append(", publishToAllUsersForGivenApplications=");
		builder.append(publishToAllUsersForGivenApplications);
		builder.append("]");
		return builder.toString();
	}


}
