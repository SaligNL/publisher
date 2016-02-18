/*
 * Copyright: Almende B.V. (2014), Rotterdam, The Netherlands
 * License: The Apache Software License, Version 2.0
 */
package com.almende.salig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.joda.time.DateTime;

import com.almende.eve.agent.Agent;
import com.almende.eve.protocol.jsonrpc.annotation.Access;
import com.almende.eve.protocol.jsonrpc.annotation.AccessType;
import com.almende.eve.protocol.jsonrpc.formats.Params;
import com.almende.util.jackson.JOM;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The Class Client.
 */
public class Client extends Agent {

	protected void onReady() {
		schedule("start", null, 1000);
	}

	/**
	 * Start.
	 */
	@Access(AccessType.PUBLIC)
	public void start() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));

			try {
				final ObjectNode input = (ObjectNode) JOM.getInstance()
						.readTree(br);
				br.close();
				
				Params params = new Params();
				//<type>/string
				String licenseString = getConfig().get("licenseUrl").asText()+"/" + input.get("type").asText()
						+ "/string";
				params.add("message", createMessage(input,licenseString));

				call(URI.create(getConfig().get("publisherUrl").asText()),
						"sendMessage", params);
				
				schedule("shutdown",null,1000);
			} catch (JsonParseException e) {
				e.printStackTrace();
			}
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	private static Message createMessage(ObjectNode input, String licenseString) {
		final Message message = new Message();
		message.setDateTime(DateTime.now().getMillis());
		message.setValue(input.get("value").asText());
		message.setLicense(licenseString);
		message.setSerial(input.get("type").asText() + "Detector");
		return message;
	}
	
	/**
	 * Shutdown.
	 */
	@Access(AccessType.PUBLIC)
	public void shutdown(){
		this.destroy(false);
		System.exit(0);
	}
}
