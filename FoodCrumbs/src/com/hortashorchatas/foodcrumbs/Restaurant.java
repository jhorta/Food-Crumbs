package com.hortashorchatas.foodcrumbs;

import android.location.Location;

public class Restaurant implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7686178610670313168L;
	public String business_Id;		// Google Places API id
	public String name;				// Google Places API name
	public String address;			// Google Places API address
	public Location location;		// Google Places API location
	public String rating;			// Google Places API rating

	/*
	//public String img_url;		// Google Places API image_url
	//public String phone_number;	// Google Places API display_phone
	//public String gPlaces_url;		// Google Places API url
	//public String website; 			// Google Places API website
	
	// TODO: add more attributes
	 * 
	 */
}
