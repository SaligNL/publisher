/*
 * Copyright: Almende B.V. (2014), Rotterdam, The Netherlands
 * License: The Apache Software License, Version 2.0
 */
package com.almende.salig;

import com.almende.eve.agent.Agent;
import com.almende.eve.protocol.jsonrpc.annotation.Access;
import com.almende.eve.protocol.jsonrpc.annotation.AccessType;
import com.almende.eve.protocol.jsonrpc.annotation.Name;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * The Class Publisher.
 */
@Access(AccessType.PUBLIC)
public class Publisher extends Agent {
	MediaSenseApp app = null;
	
	/* (non-Javadoc)
	 * @see com.almende.eve.agent.Agent#onInit()
	 */
	protected void onBoot(){
		app = new MediaSenseApp(getConfig().get("my_uci").asText());
		schedule("start",null,100);
		schedule("resolve",null,1100);
	}
	
	/**
	 * Start.
	 */
	public void start(){
		app.start();
	}
	
	/**
	 * Start.
	 */
	public void resolve(){
		app.resolve();
	}
	/**
	 * Send message.
	 *
	 * @param message
	 *            the message
	 * @throws JsonProcessingException
	 *             the json processing exception
	 */
	public void sendMessage(@Name("message") final Message message) throws JsonProcessingException {
		app.sendMessage(message);
	}
}
