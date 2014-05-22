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
import com.nineoldandroids.view.animation.AnimatorProxy;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
	private ArrayList<DirectionSteps> direction_steps_array;
	private ArrayList<Restaurant> restaurant_array;
	private Polyline direction_line;
	private PolylineOptions pOpt; 
	private SlidingUpPanelLayout sPanel;
	private TotalRouteInfo totalDirection;
	private TextView directions_overview;
	private ListView directions_list;
    public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "saved_state_action_bar_hidden";

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
        
        directions_overview = (TextView) findViewById(R.id.directions_overview_description);
                
        findLocation("string");
        
        sPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        sPanel.setPanelHeight(0);
        sPanel.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                setActionBarTranslation(sPanel.getCurrentParalaxOffset());
            }

            @Override
            public void onPanelExpanded(View panel) {
            	sPanel.setSlidingEnabled(false);
            }

            @Override
            public void onPanelCollapsed(View panel) {
            	
            }

            @Override
            public void onPanelAnchored(View panel) {}
        });
        
        directions_list = (ListView) findViewById(R.id.directions_list);
                
        directions_list.setOnScrollListener(new OnScrollListener() {
        	private int mLastFirstVisibleItem = 0;
        	private boolean mIsScrollingUp = false;
        	
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            	final int currentFirstVisibleItem = directions_list.getFirstVisiblePosition();

                if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                    mIsScrollingUp = false;
                } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                    mIsScrollingUp = true;
                }
                
                if (mIsScrollingUp) {
                	if (currentFirstVisibleItem == 0) {
                		sPanel.setSlidingEnabled(true);
                	}
                } else {
                	sPanel.setSlidingEnabled(false);
                }

                mLastFirstVisibleItem = currentFirstVisibleItem;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                int visibleItemCount, int totalItemCount) {}
        });
        
        boolean actionBarHidden = savedInstanceState != null && savedInstanceState.getBoolean(SAVED_STATE_ACTION_BAR_HIDDEN, false);
        if (actionBarHidden) {
            int actionBarHeight = getActionBarHeight();
            setActionBarTranslation(-actionBarHeight);//will "hide" an ActionBar
        }
	}
	
    private int getActionBarHeight(){
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
	
	public void setActionBarTranslation(float y) {
        // Figure out the actionbar height
        int actionBarHeight = getActionBarHeight();
        // A hack to add the translation to the action bar
        ViewGroup content = ((ViewGroup) findViewById(android.R.id.content).getParent());
        int children = content.getChildCount();
        for (int i = 0; i < children; i++) {
            View child = content.getChildAt(i);
            if (child.getId() != android.R.id.content) {
                if (y <= -actionBarHeight) {
                    child.setVisibility(View.GONE);
                } else {
                    child.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        child.setTranslationY(y);
                    } else {
                        AnimatorProxy.wrap(child).setTranslationY(y);
                    }
                }
            }
        }
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
			totalDirection = json_reponse_parser.getTotalDirections();
			direction_steps_array = json_reponse_parser.getDirectionSteps();
			
			for (int i = 0; i < direction_steps_array.size(); ++i) {
				DirectionSteps d_step = direction_steps_array.get(i);
				Log.i("Step distance", d_step.getDistance());
				Log.i("Step duration", d_step.getDuration());
				Log.i("Step instruction", d_step.getInstruction());
			}
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
        						.title(totalDirection.getStart_address())
        						.position(directions_array.get(0).getStartLocation()));
        
        endMarker = gMaps.addMarker(new MarkerOptions()
        						.title(totalDirection.getEnd_address())
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
        
        sPanel.setPanelHeight(136);
        
        directions_overview.setText(totalDirection.getDuration() + "(" + totalDirection.getDistance() + ")");
        
        String[] instructions = new String[direction_steps_array.size()];
        Integer[] imageIds = new Integer[direction_steps_array.size()];
        for (int i = 0; i < direction_steps_array.size(); ++i) {
        	String instruction = direction_steps_array.get(i).getInstruction();
        	instruction = instruction.replace("</b>", "");
        	String formated_instruction = instruction.replace("<b>", "");
        	if (instruction.contains("right")) {
        		imageIds[i] = R.drawable.ic_action_right_turn;
        	} else if (instruction.contains("left")) {
        		imageIds[i] = R.drawable.ic_action_left_turn;
        	} else {
        		imageIds[i] = R.drawable.ic_action_straight_on;
        	}
        	instructions[i] = formated_instruction + " " + direction_steps_array.get(i).getDuration() + "(" + direction_steps_array.get(i).getDistance() + ")";
        }
        
        CustomListDirectionSteps directions_steps_adapter = new CustomListDirectionSteps(this, instructions, imageIds);
        
        directions_list.setAdapter(directions_steps_adapter);
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
					
					if (restaurant == null) {
						return;
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
	
	class CustomListDirectionSteps extends ArrayAdapter<String> {
		
		Activity context;
		String[] instructions;
		Integer[] imageIds;
		
		public CustomListDirectionSteps(Activity context, String[] instructions, Integer[] imageIds) {
			super(context, R.layout.app_custom_list, instructions);
			
			this.context = context;
			this.instructions = instructions;
			this.imageIds = imageIds;
		}
		
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView= inflater.inflate(R.layout.app_custom_list, null, true);
			
			TextView txtTitle = (TextView) rowView.findViewById(R.id.dir_instruction);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.dir_icon);
			
			txtTitle.setText(instructions[position]);
			imageView.setImageResource(imageIds[position]);
			
			return rowView;
		}
	}

}
