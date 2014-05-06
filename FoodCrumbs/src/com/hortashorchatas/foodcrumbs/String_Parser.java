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
	
	public ArrayList<DirLine> getDirections() throws JSONException{
		ArrayList<DirLine> dirList = new ArrayList<DirLine>();
		ArrayList<LatLng> startLocations = new ArrayList<LatLng>();
		ArrayList<LatLng> endLocations = new ArrayList<LatLng>();
		
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
			endLocations.add(end_loc);
			
			// gets start locations
			temp = jobj.getJSONObject("start_location");
			lat = temp.getDouble("lat");
			lng = temp.getDouble("lng");
			LatLng strt_loc = new LatLng(lat, lng);
			startLocations.add(strt_loc);
		}
		
		for (int i = 0; i < startLocations.size(); ++i) {
			DirLine d_line = new DirLine(startLocations.get(i), endLocations.get(i));
			dirList.add(d_line);
		}
		
		return dirList;
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
