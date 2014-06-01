/**
 * 
 */
package com.hortashorchatas.foodcrumbs;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

import org.json.JSONException;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
//import org.json.JSONTokener;






import android.location.Location;
import android.util.Log;

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
			place = server_response.getJSONObject("places");
			results = place.getJSONArray("results");
		}
		
		for (int i = 0; i < results.length(); ++i) {
			JSONObject jobj = results.getJSONObject(i);
			
			JSONObject geom = jobj.getJSONObject("geometry");
			JSONObject location = geom.getJSONObject("location");
			
			double latitude = location.getDouble("lat");
			double longitude = location.getDouble("lng");
			
			Log.v("Latitude", String.valueOf(latitude));
			Log.v("longitude", String.valueOf(longitude));
			
			LatLng rest_location = new LatLng(latitude, longitude);
			
			String rest_id = jobj.getString("id");
			
			String rest_name = jobj.getString("name");
			
			String rest_reference = jobj.getString("reference");
			
			String rest_rating;
			
			try {
				 rest_rating = String.valueOf(jobj.getDouble("rating"));//jobj.getString("rating");/*"";*/
			} catch (JSONException e) {
				rest_rating = "No Rating";
			}
			
			String rest_addr;
			
			try {
				rest_addr = jobj.getString("vicinity");
			} catch (JSONException e) {
				rest_addr = "No Address";
			}
			
			Restaurant restaurant = new Restaurant(rest_reference, rest_id, rest_name, rest_addr, rest_location, rest_rating);
			
			restaurants.add(restaurant);
		}
		
		return restaurants;
	}
	
	
	@SuppressWarnings("unused")
	public TotalRouteInfo getTotalDirections() throws JSONException {
		JSONObject routes = null;
		JSONArray routes_2 = null;
		JSONArray legs = null;
		
		if (server_response != null) {
			routes = server_response.getJSONObject("routes");
			routes_2 = routes.getJSONArray("routes");
		}
				
		for (int i = 0; i < routes_2.length(); ++i) {
			JSONObject temp = routes_2.getJSONObject(i);
			legs = temp.getJSONArray("legs");
		}
		
		for (int i = 0; i < 1; ++i) {
			JSONObject jobj = legs.getJSONObject(i);
			
			JSONObject temp = jobj.getJSONObject("distance");
			String distance = temp.getString("text");
			
			temp = jobj.getJSONObject("duration");
			String duration = temp.getString("text");
			
			String start_location = jobj.getString("start_address");
			String end_location = jobj.getString("end_address");
			
			TotalRouteInfo totalDirection = new TotalRouteInfo(distance, duration, start_location, end_location);
			
			return totalDirection;
		}
		
		return null;
	}
	
	public ArrayList<DirectionSteps> getDirectionSteps() throws JSONException {
		ArrayList<DirectionSteps> steps_arr = new ArrayList<DirectionSteps>();
		
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
		
		for (int i = 0; i < 1; ++i) {
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
			
			temp = jobj.getJSONObject("distance");
			String distance = temp.getString("text");
			
			temp = jobj.getJSONObject("duration");
			String duration = temp.getString("text");
			
			String instruction = jobj.getString("html_instructions");
			
			DirectionSteps d_step = new DirectionSteps(distance, duration, instruction, strt_loc, end_loc);
			steps_arr.add(d_step);
		}
		return steps_arr;
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
		
		for (int i = 0; i < 1; ++i) {
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

	public HashMap<String, String> getDetails() throws JSONException {
		HashMap<String, String> details = new HashMap<String, String>();
		JSONObject result = server_response.getJSONObject("result");
		
		if (result.getString("formatted_address") != null) {
			details.put("address", result.getString("formatted_address"));
		} else {
			details.put("address", "");
		}
		
		if (result.getString("international_phone_number") != null) {
			details.put("phone number", result.getString("international_phone_number"));
		} else {
			details.put("phone number", "");
		}
		
		JSONObject geometry = result.getJSONObject("geometry");
		JSONObject location = geometry.getJSONObject("location");

		double latitude = location.getDouble("lat");
		double longitude = location.getDouble("lng");
		
		details.put("latitude", String.valueOf(latitude));
		details.put("longitude", String.valueOf(longitude));
				
		if (result.getString("id") != null) {
			details.put("id", result.getString("id"));
		} else {
			details.put("id", "");
		}
		
		if (result.getString("name") != null) {
			details.put("name", result.getString("name"));
		} else {
			details.put("name", "");
		}
		
		if (result.getString("rating") != null) {
			details.put("rating", result.getString("rating"));
		} else {
			details.put("rating", "");
		}
		
		if (result.getString("website") != null) {
			details.put("website", result.getString("website"));
		} else {
			details.put("website", "");
		}
		
		JSONArray photos = result.getJSONArray("photos");
		JSONObject photos_sub = photos.getJSONObject(0);
		
		if (photos_sub != null) {
			details.put("photo", photos_sub.getString("photo_reference"));
			details.put("height", photos_sub.getString("height"));
			details.put("width", photos_sub.getString("width"));
		}
		
		return details;
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
