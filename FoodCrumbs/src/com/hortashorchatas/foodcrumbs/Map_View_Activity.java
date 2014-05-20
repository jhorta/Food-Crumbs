package com.hortashorchatas.foodcrumbs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Build;
import android.provider.Settings;

public class Map_View_Activity extends Activity implements SearchView.OnQueryTextListener {
	private GoogleMap gMaps;
	private MarkerOptions markerOptions;
	private Marker startMarker;
	private Marker endMarker;
	private LatLng myCurrCoordinates;
	private Location myLocation;
	private LocationManager locationServices;
	private String provider;
	private MapFragment mMapFragment;
	private String_Parser json_reponse_parser;
	private ArrayList<DirLine> directions_array;
	private ArrayList<Restaurant> restaurant_array;
	private Polyline direction_line;
	private PolylineOptions pOpt; 
	
	/**
	 * This method creates the view at the onset of the Activity. One thing to check here.
	 * Check if the Alert Dialog gets built, and if all the correct settings are changed,
	 * if the dialog will not popup anymore afterwards.
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map__view);
		
		locationServices = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = locationServices.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// check if dialog pops up when Location services is not available and then 
		// the dialog does not appear again if location services is available
		if (!enabled) {
			AlertDialog.Builder enableLocationServices = new AlertDialog.Builder(this);
			enableLocationServices.setTitle("Location Services Not Active.");
			enableLocationServices.setMessage("Please enable Location Services and GPS.");
			enableLocationServices.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				    startActivity(intent);
				  }
				});
			enableLocationServices.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					finish();
				}
			});
			enableLocationServices.show();
		}
		
		mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
				
        gMaps = mMapFragment.getMap();
        
		gMaps.setMyLocationEnabled(true);
		gMaps.getUiSettings().setMyLocationButtonEnabled(true);
				
        zoomToCurrLocation();
        
        json_reponse_parser = new String_Parser();
                
        findLocation("string");
	}

	/**
	 * This method creates the option menu at the top of the android. The search icon is found in the menu
	 * and we give it a SearchView. Therefore, if the search icon is clicked, then a SearchView should replace
	 * everything else on the menu with the query hint "Enter a Location". 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map__view_, menu);
		
		MenuItem searchItem = menu.findItem(R.id.action_search);
		final SearchView searchLocation = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchLocation.setQueryHint("Enter a Location");
		searchLocation.setIconifiedByDefault(true);
		searchLocation.setOnQueryTextListener(this);

		return true;
	}

	/**
	 * This method controls the selection of the items on the menu bar at the very top of the android. 
	 * Check to see if when the directions icon is clicked it starts the Directions Activity. If it 
	 * does not start the directions activity something is wrong.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_directions) {
			Intent i = new Intent(getApplicationContext(), Directions_Activity.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Currently, find location should log the pair "Hehehe" with the query that you put into
	 * the search bar. Eventually it will do more things...
	 * @param query
	 */
	private void findLocation(String query) {
		try {
			URL url = new URL("http://ucsdfoodcrumbs.herokuapp.com/get_restaurant_lists?origin=hahaha&destination=lols&places_filter=restaurant&radius=1.0&time=NULL&distance=34");
			new getDirectionsFromServer().execute(url);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void getLocationArray() {
		try {
			
			directions_array = json_reponse_parser.getDirections();		
//			for (int i = 0; i < directions_array.size(); ++i) {
//				DirLine temp = directions_array.get(i);
//				Log.i("LatLng Start Loc", "latitude: " + temp.getStartLocation().latitude + " longitude: " + temp.getStartLocation().longitude);
//				Log.i("LatLng End Loc", "latitude: " + temp.getEndLocation().latitude + "longitude: " + temp.getEndLocation().longitude);
//			}
			show_directions();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		Log.i("Hehehe", json_string);
	}
	
	private void show_directions() {
        gMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(directions_array.get(0).getStartLocation().latitude,
                		directions_array.get(0).getStartLocation().longitude), 16));
        
        startMarker = gMaps.addMarker(new MarkerOptions()
        						.title("Start")
        						.snippet("Start of your route")
        						.position(directions_array.get(0).getStartLocation()));
        
        endMarker = gMaps.addMarker(new MarkerOptions()
        						.title("End")
        						.snippet("End of your route")
        						.position(directions_array.get(directions_array.size()-1).getEndLocation()));
        
        pOpt = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0 ; i < directions_array.size(); ++i) {
        	LatLng start_point = directions_array.get(i).getStartLocation();
        	pOpt.add(start_point);
        	
        	ArrayList<LatLng> inbtw_points = directions_array.get(i).getPoly_points();
        	for (int j = 0; j < inbtw_points.size(); ++j) {
        		LatLng inbtw_point = inbtw_points.get(j);
        		pOpt.add(inbtw_point);
        	}
        	
        	LatLng end_point = directions_array.get(i).getEndLocation();
        	pOpt.add(end_point);
        }
        
        direction_line = gMaps.addPolyline(pOpt);
	}

	/**
	 * This should zoom to your current location on the map. If it does not go to your 
	 * current location on the map, something is wrong.
	 */
	private void zoomToCurrLocation() {		
		Criteria criteria = new Criteria();
		provider = locationServices.getBestProvider(criteria, false);
		
		myLocation = locationServices.getLastKnownLocation(provider);
        if (myLocation != null)
        {
            gMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 16));
        }
	}
	
	private void getRestaurants() {
		try {
			
			restaurant_array = json_reponse_parser.getRestaurants();
			for (int i = 0; i < restaurant_array.size(); ++i) {
				Restaurant temp = restaurant_array.get(i);
				Log.i("Restaurant name ", "restaurant: " + temp.name);
			}
			
//			show_directions();
			show_restaurants();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void show_restaurants() {
		for (int i = 0; i < restaurant_array.size(); ++i) {
			Restaurant restaurant = restaurant_array.get(i);
	        gMaps.addMarker(new MarkerOptions()
				.title(restaurant.name)
				.snippet(restaurant.address)
				.position(restaurant.location));
	        gMaps.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker marker) {
					// TODO Auto-generated method stub
					String location_of_restaurant = marker.getSnippet();
					Restaurant restaurant = null;
					for (int i =0 ; i < restaurant_array.size(); ++i) {
						Restaurant temp = restaurant_array.get(i);

						if (temp.address.equals(location_of_restaurant)) {
							restaurant = temp;
							Log.v("hahahha", restaurant.name);
						}
					}
					
					Intent i = new Intent(getApplicationContext(), RestaurantActivity.class);
					i.putExtra("restaurant_reference_id", restaurant.reference_id);
					startActivity(i);
				}
	        	
	        });
		}
	}

	@Override
	public boolean onQueryTextChange(String text) {
		return false;
	}

	/**
	 * Handles the submission of the SearchView on the menu bar. It takes the string in the 
	 * SearchView and calls the method findLocation.
	 */
	@Override
	public boolean onQueryTextSubmit(String text) {
		// TODO Auto-generated method stub
		findLocation(text);
		return false;
	}
	
	private class getDirectionsFromServer extends AsyncTask<URL, Void, String> {

		@Override
		protected String doInBackground(URL... urls) {
			// TODO Auto-generated method stub
			StringBuilder sb = new StringBuilder();
			for (URL url: urls) {
				try {
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("GET");
					
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String line = "";
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
					br.close();
					
					Log.i("Harhhhh", sb.toString());
					return sb.toString();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					Log.i("There was an error", e1.toString());
					e1.printStackTrace();
				} 
			}
			return sb.toString();
		}
		
		@Override
		protected void onPostExecute(String result) {
			json_reponse_parser.setNewQueryReponse(result);
			getLocationArray();
			getRestaurants();
		}
		
	}

}
