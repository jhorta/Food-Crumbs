package com.hortashorchatas.foodcrumbs;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class Main_Menu_Activity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main__menu);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main__menu_, menu);
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
	 * This method should put the Favorites View Activity on to the stack as the Activity view on top
	 * of this current view. If the Favorites View Activity is not started something is wrong.
	 * @param view
	 */
	public void toFavoritesView(View view) {
		Intent i = new Intent(getApplicationContext(), Favorites_View_Activity.class);
		startActivity(i);
	}
	
	/**
	 * This method should put the Map View Activity on to the stack as the Activity view on top
	 * of this current view. If the Map View Activity is not started something is wrong.
	 * @param view
	 */
	public void toMapView(View view) {
		Intent i = new Intent(getApplicationContext(), Map_View_Activity.class);
		startActivity(i);
	}
	
	/**
	 * This method should put the Random Gen View Activity on to the stack as the Activity view on top
	 * of this current view. If the Random Gen View Activity is not started something is wrong.
	 * @param view
	 */
	public void toRandomGenView(View view) {
		Intent i = new Intent(getApplicationContext(), Random_Generator_Activity.class);
		startActivity(i);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		/**
		 * This method creates the placeholder fragment view that sits on top of the main activity view
		 * in the Main_Menu_Activity class. A couple things to test here. Check to see if the profile picture
		 * is indeed the one in the resources. Also check if the profile picture image view gets changed
		 * to the correct size.
		 * Check if text view sets text to "Hello, Michael!"
		 * Check if all of the image buttons change to the given sizes.
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main__menu,
					container, false);
			
			// Should test --> check if image is set to the default pic in the resources
			final ImageView prof_pic = (ImageView) rootView.findViewById(R.id.profile_pic);
			prof_pic.getLayoutParams().height = 150;
			prof_pic.setImageResource(R.drawable.defaultprof);
			
			// Should test --> check if text is set to "Hello, Michael!"
			final TextView name = (TextView) rootView.findViewById(R.id.name_text);
			name.setText("Hello, Michael!");
			
			// Should test --> check if correct size.
			final ImageButton favorites_button = (ImageButton) rootView.findViewById(R.id.favorites_button);
			favorites_button.getLayoutParams().height = 200;
			favorites_button.getLayoutParams().width = 200;
			
			// Should test --> check if correct size.
			final ImageButton directions_button = (ImageButton) rootView.findViewById(R.id.directions_button);
			directions_button.getLayoutParams().height = 200;
			directions_button.getLayoutParams().width = 200;
			
			// Should test --> check if correct size.
			final ImageButton random_button = (ImageButton) rootView.findViewById(R.id.random_button);
			random_button.getLayoutParams().height = 200;
			random_button.getLayoutParams().width = 200;
			
			return rootView;
		}
	}

}
