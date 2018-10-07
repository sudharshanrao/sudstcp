package com.clearmarkets.object;

import java.util.Date;

import org.junit.Test;

import com.clearmarkets.cleartcp.domain.message.GUIMessage;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SandboxTest {
	private transient final static ObjectMapper MAPPER = new ObjectMapper();
	{
		{
			System.out.println("in static block");
			MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			MAPPER.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
		}

	}
	private transient JsonNode mapNode;
	private static final String INPUT1 = "{\"stubType\":\"SHORT_FRONT\",\"effectiveDate\":\"2016-11-06\",\"endDate\":\"2017-11-23\",\"paymentFrequency\":\"6M\",\"roll\":\"6\"}";
	private static final String INPUT2 = "{\"stubType\":\"SHORT_FRONT\",\"effectiveDate\":\"2016-11-06\",\"endDate\":\"2017-11-23\",\"paymentFrequency\":\"SEMI\",\"roll\":\"6\"}";

	@Test
	public void test() {
		try {

			if (mapNode == null) {
				mapNode = MAPPER.readTree(INPUT1);
			}
			JsonNode value = mapNode.get("paymentFrequency");
			System.out.println(value);
			System.out.println(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES.name() + ":" + MAPPER.getDeserializationConfig().isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
			System.out.println(DeserializationFeature.READ_ENUMS_USING_TO_STRING.name() + ":" + MAPPER.getDeserializationConfig().isEnabled(DeserializationFeature.READ_ENUMS_USING_TO_STRING));
			JsonParseFrequency object = MAPPER.readValue(value.toString(), JsonParseFrequency.class);
			System.out.println(object);

		} catch (Exception e) {
			throw new IllegalStateException("Failed processing the json for key:paymentFrequency", e);
		}

	}

	@Test
	public void test2() {
		GUIMessage gui = new GUIMessage();
		gui.setRequestMethodParam(INPUT1);
		System.out.println(gui.parseParamUsingKey("endDate", Date.class));
		System.out.println(gui.parseParamUsingKey("paymentFrequency", JsonParseFrequency.class));
	}
	
	@Test(expected=IllegalStateException.class)
	public void test3() {
		GUIMessage gui = new GUIMessage();
		gui.setRequestMethodParam(INPUT2);
		System.out.println(gui.parseParamUsingKey("endDate", Date.class));
		System.out.println(gui.parseParamUsingKey("paymentFrequency", JsonParseFrequency.class));
	}
}