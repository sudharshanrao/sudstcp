package com.clearmarkets.object;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.clearmarkets.cleartcp.domain.message.GUIMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonParseTest {

	@Test
	public void testGson() throws JsonProcessingException, IOException {
		// GSON
		Gson gson = new GsonBuilder().create();
		
		// REQUEST PARAM
		Map<String, Object> map = new HashMap<>();
		map.put("key", new TestDummyObject("fun", 1));
		map.put("endDate", new Date());

		// GUI MESSAGE
		GUIMessage inputMsg = new GUIMessage();
		inputMsg.setDestinationServiceName("affirmationService");
		inputMsg.setRequestMethodParam(gson.toJson(map));

		String input = gson.toJson(inputMsg);
		System.out.println("test: " + input);

		GUIMessage parsedMsg = gson.fromJson(input, GUIMessage.class);
		System.out.println(parsedMsg);
		System.out.println(parsedMsg.getRequestMethodParam());

		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("MMM dd,yyyy HH:mm:ss aa"));
		JsonNode actualObj = mapper.readTree(parsedMsg.getRequestMethodParam());

		System.out.println("END_DATE");
		JsonNode value = actualObj.get("endDate");
		System.out.println("JSON: " + value);
		System.out.println("JAVA[GSON]: " + gson.fromJson(value.toString(), Date.class));
		System.out.println("JAVA[JACK]: " + mapper.readValue(value.toString(), Date.class));

		System.out.println("TEST_DUMMY_OBJECT");
		JsonNode value2 = actualObj.get("key");
		System.out.println("JSON: " + value2.toString());
		System.out.println("JAVA[GSON]: " + gson.fromJson(value2.toString(), TestDummyObject.class));
		System.out.println("JAVA[JACK]: " + mapper.readValue(value2.toString(), TestDummyObject.class));
	}
	
	@Test
	public void testJackson() throws JsonProcessingException, IOException {
		// JACKSON
		ObjectMapper mapper = new ObjectMapper();
		
		// REQUEST PARAM
		Map<String, Object> map = new HashMap<>();
		map.put("key", new TestDummyObject("fun", 1));
		map.put("endDate", new Date());

		// GUI MESSAGE
		GUIMessage inputMsg = new GUIMessage();
		inputMsg.setDestinationServiceName("affirmationService");
		inputMsg.setRequestMethodParam(mapper.writeValueAsString(map));

		String input = mapper.writeValueAsString(inputMsg);
		System.out.println("test: " + input);

		GUIMessage parsedMsg = mapper.readValue(input, GUIMessage.class);
		System.out.println(parsedMsg);
		System.out.println(parsedMsg.getRequestMethodParam());

		JsonNode actualObj = mapper.readTree(parsedMsg.getRequestMethodParam());

		System.out.println("END_DATE");
		JsonNode value = actualObj.get("endDate");
		System.out.println("JSON: " + value);
		System.out.println("JAVA[JACK]: " + mapper.readValue(value.toString(), Date.class));

		System.out.println("TEST_DUMMY_OBJECT");
		JsonNode value2 = actualObj.get("key");
		System.out.println("JSON: " + value2.toString());
		System.out.println("JAVA[JACK]: " + mapper.readValue(value2.toString(), TestDummyObject.class));
	}

	@Test
	public void testToJsonEnumStringLabelGson() {
		Gson gson = new GsonBuilder().create();
		String value = gson.toJson(TestEnumWithLabel._1M);
		Assert.assertEquals(value, "\"_1M\"");
	}

	@Test
	public void testToJsonEnumStringLabel() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String value = mapper.writeValueAsString(TestEnumWithLabel._1M);
		Assert.assertEquals(value, "\"1M\"");
	}

	@Test
	public void testToJsonDate() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		// DateFormat df = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a");
		// mapper.setDateFormat(df);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		String value = mapper.writeValueAsString(new Date(116, 0, 1));
		Assert.assertEquals(value, "\"2016-01-01T05:00:00.000+0000\"");
		Date date = mapper.readValue(value, Date.class);
		Assert.assertEquals(date, new Date(116, 0, 1));

	}

	@Test
	public void testFromJsonDate() throws IOException {
		String input = "\"Jan 1, 2016 12:00:00 AM\"";
		ObjectMapper mapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a");
		mapper.setDateFormat(df);
		Date value = mapper.readValue(input, Date.class);
		Assert.assertEquals(value, new Date(116, 0, 1));
	}

	@Test
	public void testFromDefaultJsonDate() throws IOException {
		String input = "\"2016-01-01T05:00:00.000+0000\"";
		ObjectMapper mapper = new ObjectMapper();
		// DateFormat df = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a");
		// mapper.setDateFormat(df);
		Date value = mapper.readValue(input, Date.class);
		Assert.assertEquals(value, new Date(116, 0, 1));
	}

	@Test
	public void testToJsonDateGson() {
		Gson gson = new GsonBuilder().create();
		String value = gson.toJson(new Date(116, 0, 1));
		Assert.assertEquals(value, "Jan 1, 2016 12:00:00 AM");
	}
}