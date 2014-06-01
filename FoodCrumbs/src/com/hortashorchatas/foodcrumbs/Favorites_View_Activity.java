package com.hortashorchatas.foodcrumbs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.os.Build;

public class Favorites_View_Activity extends Activity {
	
	private DatabaseHandler db;
	private SimpleAdapter simpleAdpt;
	private List<Map<String, String>> favoriteList = new ArrayList<Map<String, String>>();
	ListView faveList;
	private final String faveKey = "newFavorite";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites__view);
		db = new DatabaseHandler(this);

		makeList();

		faveList = (ListView) findViewById(R.id.listView);
		simpleAdpt = new SimpleAdapter(this, favoriteList, android.R.layout.simple_list_item_1, new String[] {faveKey}, new int[] {android.R.id.text1});
		faveList.setAdapter(simpleAdpt);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
        faveList.setOnItemLongClickListener(new OnItemLongClickListener() {
            // setting onItemLongClickListener and passing the position to the function
                      public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int position, long arg3) {
                removeItemFromList(position);   
                
                return true;
            }
        });
		
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
		db.addFavorite(new Restaurant("abcdefg", "1", "Burger King", "3500 Gillman Dr.", new LatLng(33,16), "3"));
		db.addFavorite(new Restaurant("abcdefg", "2", "Santorini's", "3500 Gillman Dr.", new LatLng(15,16), "4"));
		db.addFavorite(new Restaurant("abcdefg", "3", "Subway", "3500 Gillman Dr.", new LatLng(66,66), "4"));
		db.addFavorite(new Restaurant("abcdefg", "4", "Shogun", "3500 Gillman Dr.", new LatLng(65,66), "5"));
		db.addFavorite(new Restaurant("abcdefg", "5", "Panda Express", "3500 Gillman Dr.", new LatLng(66,67), "3"));
		db.addFavorite(new Restaurant("abcdefg", "6", "Rubio's", "3500 Gillman Dr.", new LatLng(64,66), "4"));
		db.addFavorite(new Restaurant("abcdefg", "7", "Foodworx", "3500 Gillman Dr.", new LatLng(66,65), "3"));
		db.addFavorite(new Restaurant("abcdefg", "8", "Canyon Vista", "3500 Gillman Dr.", new LatLng(63,66), "4"));
		db.addFavorite(new Restaurant("abcdefg", "9", "Pines", "3500 Gillman Dr.", new LatLng(65,65), "5"));
		db.addFavorite(new Restaurant("abcdefg", "10", "D'lush", "3500 Gillman Dr.", new LatLng(66,63), "3"));
		db.addFavorite(new Restaurant("abcdefg", "13", "Subway", "3600 Gillman Dr.", new LatLng(65,65), "4"));

		
		ArrayList<Restaurant> myFavorites = db.getAllFavorites();
		
		favoriteList = new ArrayList<Map<String, String>>();
		
		for(int i=0 ;i < myFavorites.size(); i++){

			Map<String, String> myMap = new HashMap<String, String>();
			
			myMap.put(faveKey, myFavorites.get(i).name);
			
			favoriteList.add(myMap);
		}
		
	}
	
	// method to remove list item
    protected void removeItemFromList(int position) {
        final int deletePosition = position;
		ArrayList<Restaurant> tempFavorites = db.getAllFavorites();
        String temp = (tempFavorites.get(position)).business_id;
        final int restid = Integer.valueOf(temp);
  


 
        AlertDialog.Builder alert = new AlertDialog.Builder(
                Favorites_View_Activity.this);
    
        alert.setTitle("Delete");
        alert.setMessage("Do you want delete this item?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                    
                    // main code on after clicking yes
                    favoriteList.remove(deletePosition);
                    simpleAdpt.notifyDataSetChanged();
                    simpleAdpt.notifyDataSetInvalidated();
                    db.deleteFavorite(db.getFavorite(restid));
      
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
      
        alert.show();
      
    }
}
