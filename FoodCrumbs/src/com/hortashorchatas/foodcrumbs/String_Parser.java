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
		
		if (result.has("formatted_address")) {
			details.put("address", result.getString("formatted_address"));
		} else {
			details.put("address", "");
		}
		
		if (result.has("international_phone_number")) {
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
				
		if (result.has("id")) {
			details.put("id", result.getString("id"));
		} else {
			details.put("id", "");
		}
		
		if (result.has("name")) {
			details.put("name", result.getString("name"));
		} else {
			details.put("name", "");
		}
		
		if (result.has("rating")) {
			details.put("rating", String.valueOf(result.getDouble("rating")));
		} else {
			details.put("rating", "");
		}
		
		if (result.has("website")) {
			details.put("website", result.getString("website"));
		} else {
			details.put("website", "");
		}
		
		if (result.has("photos")) {
			JSONArray photos = result.getJSONArray("photos");
			JSONObject photos_sub = photos.getJSONObject(0);
			
			if (photos_sub != null) {
				details.put("photo", photos_sub.getString("photo_reference"));
				details.put("height", String.valueOf(photos_sub.getDouble("height")));
				details.put("width", String.valueOf(photos_sub.getDouble("width")));
			}
		} else {
			details.put("photo", "");
			details.put("height", "0");
			details.put("width", "0");
		}
		
		return details;
	}
	
	public String[] getRestaurantHours() {
		String[] hours = new String[7];
		
		for (int i = 0; i < hours.length; ++i) {
			String day = getDayOfWeek(i);
			hours[i] = day + " Closed.";
		}
		
		try {
			JSONObject result = server_response.getJSONObject("result");
			JSONObject opening_hours = result.getJSONObject("opening_hours");
			JSONArray opening_hour_periods = opening_hours.getJSONArray("periods");
			
			for (int i = 0; i < opening_hour_periods.length(); ++i) {
				JSONObject period = opening_hour_periods.getJSONObject(i);
				JSONObject open = period.getJSONObject("open");
				JSONObject close = period.getJSONObject("close");
				
				int day = open.getInt("day");
				
				String open_time = open.getString("time");
				String open_time_hours = open_time.substring(0, 2);
				String open_time_minutes = open_time.substring(2, 4);
				int open_time_int_hrs = Integer.parseInt(open_time_hours);
				
				String close_time = close.getString("time");
				String close_time_hours = close_time.substring(0, 2);
				String close_time_minutes = close_time.substring(2, 4);
				int close_time_int_hrs = Integer.parseInt(close_time_hours);
				
				hours[day] = getDayOfWeek(day) + " " + getTimeString(open_time_int_hrs, open_time_hours, open_time_minutes)
						+ "-" + getTimeString(close_time_int_hrs, close_time_hours, close_time_minutes);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hours;
	}
	
	private String getTimeString(int hours, String hour_string, String minute_string) {
		if (hours > 12 && hours < 24) {
			hours = hours - 12;
			hour_string = String.valueOf(hours);
			return hour_string + ":" + minute_string +"pm";
		} else if (hours == 24) {
			return "12"+":"+minute_string+"am";
		} else if (hours == 12) {
			return "12"+":"+minute_string+"pm";
		} else {
			return hour_string+":"+minute_string+"am";
		}
	}
	
	private String getDayOfWeek(int i) {
		String day = "";
		switch (i) {
			case 0:
				day = "Sun";
				break;
			case 1:
				day = "Mon";
				break;
			case 2:
				day = "Tue";
				break;
			case 3:
				day = "Wed";
				break;
			case 4:
				day = "Thu";
				break;
			case 5:
				day = "Fri";
				break;
			case 6:
				day = "Sat";
				break;
		}
		return day;
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
