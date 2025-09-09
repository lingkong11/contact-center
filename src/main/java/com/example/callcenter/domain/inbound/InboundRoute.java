package com.example.callcenter.domain.inbound;

import java.util.Objects;

public class InboundRoute {
	private final String id;
	private final String didNumber;
	private final String destinationType; // QUEUE | USER | URI
	private final String destinationValue; // queue name | user id | full URI

	public InboundRoute(String id, String didNumber, String destinationType, String destinationValue) {
		this.id = Objects.requireNonNull(id);
		this.didNumber = Objects.requireNonNull(didNumber);
		this.destinationType = Objects.requireNonNull(destinationType);
		this.destinationValue = Objects.requireNonNull(destinationValue);
	}

	public String getId() { return id; }
	public String getDidNumber() { return didNumber; }
	public String getDestinationType() { return destinationType; }
	public String getDestinationValue() { return destinationValue; }
}

