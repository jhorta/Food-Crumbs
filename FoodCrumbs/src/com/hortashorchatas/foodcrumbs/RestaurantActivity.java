package com.hortashorchatas.foodcrumbs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class RestaurantActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant);
		
		Intent i = getIntent();
		String restaurant_reference = i.getStringExtra("restaurant_reference_id");
		
		try {
			URL url = new URL("http://ucsdfoodcrumbs.herokuapp.com/details?reference=" + restaurant_reference);
			new getDetailsFromServer().execute(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.v("harrhhh", restaurant_reference);
/*		
		String name = restaurant.name;
		String rating = restaurant.rating;
		String address = restaurant.address;
		String id = restaurant.business_id;

		TextView name_text = (TextView) findViewById(R.id.restaurant_name);
		name_text.setText(name);
		TextView address_text = (TextView) findViewById(R.id.address);
		address_text.setText(address);
		*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.restaurant_menu, menu);
		return true;
	}
	
	private class getDetailsFromServer extends AsyncTask<URL, Void, String> {

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
			
		}
	}
}
