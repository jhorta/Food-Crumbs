package com.hortashorchatas.foodcrumbs;

import com.google.android.gms.maps.model.LatLng;

import android.location.Location;

public class Restaurant implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7686178610670313168L;
	public String business_id;		// Google Places API id
	public String name;				// Google Places API name
	public String address;			// Google Places API address
	public LatLng location;		// Google Places API location
	public double latitude;
	public double longitude;
	public String rating;			// Google Places API rating

	public Restaurant(String business_id, String name, String address, LatLng location, String rating) {
		this.business_id = business_id;
		this.name = name;
		this.address = address;
		this.location = location;
		this.rating = rating;
		this.latitude = location.latitude;
		this.longitude = location.longitude;
	}
	
	public Restaurant(String business_id, String name) {
		this.business_id = business_id;
		this.name = name;
		this.address = "";
		this.location = null;
		this.rating = "";
	}
	
	public Restaurant() {
		this.business_id = "";
		this.name = "";
		this.address = "";
		this.location = null;
		this.rating = "";
	}
	/*
	//public String img_url;		// Google Places API image_url
	//public String phone_number;	// Google Places API display_phone
	//public String gPlaces_url;		// Google Places API url
	//public String website; 			// Google Places API website
	
	// TODO: add more attributes
	 * 
	 */
}
