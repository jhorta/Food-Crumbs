/**
 * 
 */
package com.hortashorchatas.foodcrumbs;

import java.util.ArrayList;

import org.json.JSONException;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
//import org.json.JSONTokener;


import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class String_Parser {
			
//	JSONParser parser = new JSONParser();
	
	JSONObject server_response;
	
	public String_Parser() {
		server_response = new JSONObject();
	}
	
	public String_Parser(String json_response) {				
		try {
			server_response = new JSONObject(json_response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Restaurant> getRestaurants() throws JSONException {
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		
		JSONObject place = null;
		JSONArray results = null;
		
		if (server_response != null) {
			place = server_response.getJSONObject("place1");
			results = place.getJSONArray("results");
		}
		
		for (int i = 0; i < results.length(); ++i) {
			JSONObject jobj = results.getJSONObject(i);
			
			JSONObject geom = jobj.getJSONObject("geometry");
			JSONObject location = geom.getJSONObject("location");
			
			double latitude = location.getDouble("lat");
			double longitude = location.getDouble("lng");
			
			LatLng rest_location = new LatLng(latitude, longitude);
			
			String rest_id = jobj.getString("id");
			
			String rest_name = jobj.getString("name");
			
			String rest_rating = "";//jobj.getString("rating");
			
			String rest_addr = jobj.getString("vicinity");
			
			Restaurant rest = new Restaurant(rest_id, rest_name, rest_addr, rest_location, rest_rating);
			
			restaurants.add(rest);
		}
		
		return restaurants;
	}
	
	public ArrayList<DirLine> getDirections() throws JSONException{
		ArrayList<DirLine> dirList = new ArrayList<DirLine>();
		
		JSONObject routes = null;
		JSONArray routes_2 = null;
		JSONArray legs = null;
		JSONArray steps = null;
		
		if (server_response != null) {
			routes = server_response.getJSONObject("routes");
			routes_2 = routes.getJSONArray("routes");
		}
				
		for (int i = 0; i < routes_2.length(); ++i) {
			JSONObject temp = routes_2.getJSONObject(i);
			legs = temp.getJSONArray("legs");
		}
		
		for (int i = 0; i < legs.length(); ++i) {
			JSONObject temp = legs.getJSONObject(i);
			steps = temp.getJSONArray("steps");
		}
		
		for (int i = 0; i < steps.length(); ++i) {
			JSONObject jobj = steps.getJSONObject(i);
						
			// gets end locations
			JSONObject temp = jobj.getJSONObject("end_location");
			double lat = temp.getDouble("lat");
			double lng = temp.getDouble("lng");
			LatLng end_loc = new LatLng(lat, lng);
			
			// gets start locations
			temp = jobj.getJSONObject("start_location");
			lat = temp.getDouble("lat");
			lng = temp.getDouble("lng");
			LatLng strt_loc = new LatLng(lat, lng);
			
			temp = jobj.getJSONObject("polyline");
			String poly_points = temp.getString("points");
			ArrayList<LatLng> poly_points_arr = decodePoly(poly_points);
			
			DirLine d_line = new DirLine(strt_loc, end_loc, poly_points_arr);
			dirList.add(d_line);
		}
		
		return dirList;
	}
	
    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
   
            LatLng position = new LatLng((double)lat / 1E5, (double)lng / 1E5);
            poly.add(position);
        }
        return poly;
    }
	
	public void setNewQueryReponse(String json_response) {
		try {
			server_response = new JSONObject(json_response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
