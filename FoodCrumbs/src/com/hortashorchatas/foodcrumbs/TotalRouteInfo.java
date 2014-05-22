package com.hortashorchatas.foodcrumbs;

public class TotalRouteInfo {
	private String distance;
	private String duration;
	private String start_address;
	private String end_address;
	
	public TotalRouteInfo(String distance, String duration, String start_address, String end_address) {
		this.distance = distance;
		this.duration = duration;
		this.start_address = start_address;
		this.end_address = end_address;
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
	public String getStart_address() {
		return start_address;
	}
	public void setStart_address(String start_address) {
		this.start_address = start_address;
	}
	public String getEnd_address() {
		return end_address;
	}
	public void setEnd_address(String end_address) {
		this.end_address = end_address;
	}
	
	
}
