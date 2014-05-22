package com.hortashorchatas.foodcrumbs;

import com.google.android.gms.maps.model.LatLng;

public class DirectionSteps {
	private String distance;
	private String duration;
	private String instruction;
	private LatLng start_location;
	private LatLng end_location;
	
	public DirectionSteps(String distance, String duration, String instruction, LatLng start_location, LatLng end_location) {
		this.distance = distance;
		this.duration = duration;
		this.instruction = instruction;
		this.start_location = start_location;
		this.end_location = end_location;
	}
	
	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public LatLng getStart_location() {
		return start_location;
	}

	public void setStart_location(LatLng start_location) {
		this.start_location = start_location;
	}

	public LatLng getEnd_location() {
		return end_location;
	}

	public void setEnd_location(LatLng end_location) {
		this.end_location = end_location;
	}
	
}
