/*
 * Copyright: Almende B.V. (2014), Rotterdam, The Netherlands
 * License: The Apache Software License, Version 2.0
 */
package com.almende.salig;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class Message.
 */
public class Message implements Serializable {
	private static final long	serialVersionUID	= -5731354252574031680L;
	private Long				dateTime			= null;
	private String				value				= null;
	private String				license				= null;
	private String				serial				= null;
	private String				type				= null;

	/**
	 * Gets the date time.
	 *
	 * @return the date time
	 */
	public Long getDateTime() {
		return dateTime;
	}

	/**
	 * Sets the date time.
	 *
	 * @param dateTime
	 *            the new date time
	 */
	public void setDateTime(Long dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the license.
	 *
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * Sets the license.
	 *
	 * @param license
	 *            the new license
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * Gets the serial.
	 *
	 * @return the serial
	 */
	public String getSerial() {
		return serial;
	}

	/**
	 * Sets the serial.
	 *
	 * @param serial
	 *            the new serial
	 */
	public void setSerial(String serial) {
		this.serial = serial;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	@JsonIgnore
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
	}
}
