package com.hortashorchatas.foodcrumbs;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class DirLine {

	private LatLng startLocation;
	private LatLng endLocation;
	private ArrayList<LatLng> poly_points;
	
	public DirLine(LatLng startLocation, LatLng endLocation, ArrayList<LatLng> poly_points) {
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.setPoly_points(poly_points);
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

	public ArrayList<LatLng> getPoly_points() {
		return poly_points;
	}

	public void setPoly_points(ArrayList<LatLng> poly_points) {
		this.poly_points = poly_points;
	}
	
	
}
