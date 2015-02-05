/*
 * Copyright: Almende B.V. (2014), Rotterdam, The Netherlands
 * License: The Apache Software License, Version 2.0
 */
package com.almende.salig;

import se.mediasense.addinlayer.AddInManager;
import se.mediasense.addinlayer.extensions.publishsubscribe.NotifySubscribersMessage;
import se.mediasense.addinlayer.extensions.publishsubscribe.PublishSubscribeExtension;
import se.mediasense.addinlayer.extensions.publishsubscribe.SubscriptionResponseListener;
import se.mediasense.distribution.FuturePrimitive;
import se.mediasense.distribution.PrimitiveListener;
import se.mediasense.distribution.Registrator;
import se.mediasense.distribution.Resolver;
import se.mediasense.interfacelayer.MediaSenseApplication;
import se.mediasense.interfacelayer.MediaSensePlatform;
import se.mediasense.messages.MediaSenseMessage;

import com.almende.util.jackson.JOM;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * The Class MediaSenseApp.
 */
public class MediaSenseApp implements MediaSenseApplication,
		SubscriptionResponseListener {
	private MediaSensePlatform			mp		= null;
	private PublishSubscribeExtension	p		= null;
	private Boolean						started	= false;
	private final String				MY_UCI;

	/**
	 * Instantiates a new media sense app.
	 *
	 * @param uci
	 *            the uci
	 * @param host
	 *            the host
	 * @param remoteport
	 *            the remoteport
	 * @param localport
	 *            the localport
	 */
	public MediaSenseApp(final String uci, final String host, final int remoteport, final int localport) {
		MY_UCI = uci;

		// Instantiate platform with the MediaSenseListener
		mp = new MediaSensePlatform(this);
		mp.init(host, remoteport, localport);

		p = new PublishSubscribeExtension(this);
		AddInManager am = new AddInManager(mp);
		am.loadAddIn(p);
		p.startAddIn();
	}

	/**
	 * Start.
	 */
	public void start() {
		System.out.println("working with:" + MY_UCI);
		Registrator r = mp.registerUCI(MY_UCI);
		r.startSynchronousRegistration();
	}

	/**
	 * Resolve.
	 */
	public void resolve() {
		final Resolver rs = mp.resolveUCI(MY_UCI);
		rs.startAsynchronousResolve(new PrimitiveListener() {
			@Override
			public void handleCompleted(FuturePrimitive futureprimtive) {
				System.out.println("resolution completed!");
				started = true;
				try {
					p.startSubscription(rs.getUci());
				} catch (Exception e) {
					System.out.println("Oops, failed to start subscription!");
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Send message.
	 *
	 * @param message
	 *            the message
	 * @throws JsonProcessingException
	 *             the json processing exception
	 */
	public void sendMessage(Message message) throws JsonProcessingException {
		System.out.println("Sending message : "
				+ JOM.getInstance().writeValueAsString(message));
		if (started) {
			System.out.println("Notifying subscribers!");
			p.notifySubscribers(MY_UCI,
					JOM.getInstance().writeValueAsString(message));
		} else {
			System.out
					.println("Trying to send message before start, ignoring.");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * se.mediasense.messages.MediaSenseListener#handleMessage(se.mediasense
	 * .messages.MediaSenseMessage)
	 */
	@Override
	public void handleMessage(MediaSenseMessage msm) {
		System.out.println("Received message:" + msm.getMsgID() + "("
				+ msm.getMsgType() + ") from:" + msm.getSource() + " to:"
				+ msm.getDestination() + " : " + msm.toString());
	}

	@Override
	public void subscriptionResponse(NotifySubscribersMessage _msg) {
		System.out.println("Received response:" + _msg.getMsgID() + "("
				+ _msg.getMsgType() + ") from:" + _msg.getSource() + " to:"
				+ _msg.getDestination() + " : " + _msg.toString() + " val:"
				+ _msg.getValue());
	}
}