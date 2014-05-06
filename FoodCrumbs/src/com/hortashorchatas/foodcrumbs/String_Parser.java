/**
 * 
 */
package com.hortashorchatas.foodcrumbs;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.android.gms.maps.model.LatLng;

public class String_Parser {
			
	JSONParser parser = new JSONParser();
	
	JSONObject server_response;
	
	public String_Parser() {
		server_response = new JSONObject();
	}
	
	public String_Parser(String json_response) {				
		try {
			server_response = (JSONObject) parser.parse(json_response);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<DirLine> getDirections() throws JSONException{
		ArrayList<DirLine> dirList = new ArrayList<DirLine>();
		ArrayList<LatLng> startLocations = new ArrayList<LatLng>();
		ArrayList<LatLng> endLocations = new ArrayList<LatLng>();
		
		JSONObject routes = new JSONObject();
		JSONArray routes_2 = new JSONArray();
		JSONArray legs = new JSONArray();
		JSONArray steps = new JSONArray();
		
		if (server_response != null) {
			routes = (JSONObject) server_response.get("routes");
			routes_2 = (JSONArray) routes.get("routes");
		}
		
		for (int i = 0; i < routes_2.length(); ++i) {
			if (routes_2.getString(i).equals("legs")) {
				legs = (JSONArray) routes_2.get(i);
				break;
			}
		}
		
		for (int i = 0; i < legs.length(); ++i) {
			if (legs.getString(i).equals("steps")) {
				steps = (JSONArray) legs.get(i);
				break;
			}
		}
		
		for (int i = 0; i < steps.length(); ++i) {
			if (steps.getString(i).equals("end_location")) {
				JSONObject jobj = (JSONObject) steps.get(i);
				double lat = jobj.getDouble("lat");
				double lng = jobj.getDouble("lng");
				LatLng loc = new LatLng(lat, lng);
				endLocations.add(loc);
			}
			if (steps.getString(i).equals("start_location")) {
				JSONObject jobj = (JSONObject) steps.get(i);
				double lat = jobj.getDouble("lat");
				double lng = jobj.getDouble("lng");
				LatLng loc = new LatLng(lat, lng);
				startLocations.add(loc);
			}
		}
		
		for (int i = 0; i < startLocations.size(); ++i) {
			DirLine d_line = new DirLine(startLocations.get(i), endLocations.get(i));
			dirList.add(d_line);
		}
		
		return dirList;
	}
	
	public void setNewQueryReponse(String json_response) {
		try {
			server_response = (JSONObject) parser.parse(json_response);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
