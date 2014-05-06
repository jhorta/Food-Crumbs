package com.hortashorchatas.foodcrumbs;

import com.google.android.gms.maps.model.LatLng;

public class DirLine {

	private LatLng startLocation;
	private LatLng endLocation;
	
	public DirLine(LatLng startLocation, LatLng endLocation) {
		this.startLocation = startLocation;
		this.endLocation = endLocation;
	}
	
	public LatLng getStartLocation() {
		return startLocation;
	}
	public void setStartLocation(LatLng startLocation) {
		this.startLocation = startLocation;
	}
	public LatLng getEndLocation() {
		return endLocation;
	}
	public void setEndLocation(LatLng endLocation) {
		this.endLocation = endLocation;
	}
	
	
}
