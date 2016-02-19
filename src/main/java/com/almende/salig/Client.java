/*
 * Copyright: Almende B.V. (2014), Rotterdam, The Netherlands
 * License: The Apache Software License, Version 2.0
 */
package com.almende.salig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.logging.Logger;

import org.joda.time.DateTime;

import com.almende.eve.agent.Agent;
import com.almende.eve.protocol.jsonrpc.annotation.Access;
import com.almende.eve.protocol.jsonrpc.annotation.AccessType;
import com.almende.eve.protocol.jsonrpc.formats.Params;
import com.almende.util.jackson.JOM;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The Class Client.
 */
public class Client extends Agent {
	private static final Logger	LOG	= Logger.getLogger(Client.class.getName());

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
				final ArrayNode list = JOM.createArrayNode();
				final JsonNode input = JOM.getInstance().readTree(br);
				br.close();

				if (input.isObject()) {
					list.add(input);
				} else if (input.isArray()) {
					list.addAll((ArrayNode) input);
				} else {
					LOG.warning("Incorrect input type:" + input);
				}

				for (JsonNode item : list) {
					final ObjectNode node = (ObjectNode) item;
					Params params = new Params();
					// <type>/string
					String licenseString = getConfig().get("licenseUrl")
							.asText()
							+ "/"
							+ node.get("type").asText()
							+ "/string";
					params.add("message", createMessage(node, licenseString));

					call(URI.create(getConfig().get("publisherUrl").asText()),
							"sendMessage", params);
				}

				schedule("shutdown", null, 1000);
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
		message.setValue(input.get("value").asBoolean());
		message.setLicense(licenseString);
		message.setSerial(input.get("type").asText() + "Detector");
		return message;
	}

	/**
	 * Shutdown.
	 */
	@Access(AccessType.PUBLIC)
	public void shutdown() {
		this.destroy(false);
		System.exit(0);
	}
}
