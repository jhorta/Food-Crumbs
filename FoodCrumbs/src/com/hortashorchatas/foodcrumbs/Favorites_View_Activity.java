package com.hortashorchatas.foodcrumbs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.os.Build;

public class Favorites_View_Activity extends Activity {
	
	private DatabaseHandler db;
	private SimpleAdapter simpleAdpt;
	private List<Map<String, String>> favoriteList = new ArrayList<Map<String, String>>();
	
	private final String faveKey = "newFavorite";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites__view);
		db = new DatabaseHandler(this);

		makeList();

		ListView faveList = (ListView) findViewById(R.id.listView);
		simpleAdpt = new SimpleAdapter(this, favoriteList, android.R.layout.simple_list_item_1, new String[] {faveKey}, new int[] {android.R.id.text1});
		faveList.setAdapter(simpleAdpt);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.favorites__view_, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_favorites__view,
					container, false);
			return rootView;
		}
	}
	
	private void makeList()
	{
		db.addFavorite(new Restaurant("1", "Burger King", "3500 Gillman Dr.", new LatLng(33,16), "3"));
		db.addFavorite(new Restaurant("2", "Santorini's", "3500 Gillman Dr.", new LatLng(15,16), "4"));
		db.addFavorite(new Restaurant("3", "Subway", "3500 Gillman Dr.", new LatLng(66,66), "4"));
		db.addFavorite(new Restaurant("4", "Shogun", "3500 Gillman Dr.", new LatLng(65,66), "5"));
		db.addFavorite(new Restaurant("5", "Panda Express", "3500 Gillman Dr.", new LatLng(66,67), "3"));
		db.addFavorite(new Restaurant("6", "Rubio's", "3500 Gillman Dr.", new LatLng(64,66), "4"));
		db.addFavorite(new Restaurant("7", "Foodworx", "3500 Gillman Dr.", new LatLng(66,65), "3"));
		db.addFavorite(new Restaurant("8", "Canyon Vista", "3500 Gillman Dr.", new LatLng(63,66), "4"));
		db.addFavorite(new Restaurant("9", "Pines", "3500 Gillman Dr.", new LatLng(65,65), "5"));
		db.addFavorite(new Restaurant("10", "D'lush", "3500 Gillman Dr.", new LatLng(66,63), "3"));
		
		ArrayList<Restaurant> myFavorites = db.getAllFavorites();
		
		favoriteList = new ArrayList<Map<String, String>>();
		
		for(int i=0 ;i < myFavorites.size(); i++){

			Map<String, String> myMap = new HashMap<String, String>();
			
			myMap.put(faveKey, myFavorites.get(i).name);
			
			favoriteList.add(myMap);
		}
		
	}
	
}
