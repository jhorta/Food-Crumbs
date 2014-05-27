package com.hortashorchatas.foodcrumbs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class RestaurantActivity extends Activity {
	private String[] restaurant_hours = new String[7];
	private ImageView restaurant_image;
	private TextView restaurant_name;
	private TextView restaurant_address;
	private Spinner restaurant_hours_spinner;
	private TextView restaurant_phone_number;
	private TextView restaurant_rating;
	private TextView restaurant_website;

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
		
		restaurant_image = (ImageView) findViewById(R.id.restaurant_picture);
		restaurant_image.getLayoutParams().height = 160;
		restaurant_image.getLayoutParams().width = 160;
		
		restaurant_name = (TextView) findViewById(R.id.restaurant_name);
		restaurant_name.setText("Subway");
		restaurant_name.setTypeface(null, Typeface.BOLD);
		
		restaurant_address = (TextView) findViewById(R.id.restaurant_address);
		restaurant_address.setText("221B Baker St, London NW1 6XE, United Kingdom");
		restaurant_address.setTypeface(null, Typeface.BOLD);
		
		restaurant_hours_spinner = (Spinner) findViewById(R.id.restaurant_hours);
		
		restaurant_hours[0] = "Sun 11am-12am";
		restaurant_hours[1] = "Mon 11am-12am";
		restaurant_hours[2] = "Tue 11am-12am";
		restaurant_hours[3] = "Wed 11am-12am";
		restaurant_hours[4] = "Thu 11am-12am";
		restaurant_hours[5] = "Fri 11am-12am";
		restaurant_hours[6] = "Sat 11am-12am";
		
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK); 
		
		ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, restaurant_hours);
		adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		restaurant_hours_spinner.setAdapter(adapter_state);
		restaurant_hours_spinner.setSelection(day-1);
		
		restaurant_phone_number = (TextView) findViewById(R.id.restaurant_phone_number);
		restaurant_phone_number.setText("+44 20 7224 3688");
		restaurant_phone_number.setTypeface(null, Typeface.BOLD);
		restaurant_phone_number.setTextColor(Color.BLUE);
		restaurant_phone_number.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final AlertDialog.Builder dialog = new AlertDialog.Builder(RestaurantActivity.this);
				dialog.setTitle("Call...");
				dialog.setMessage(restaurant_phone_number.getText().toString());
				dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// DO NOTHING FOR NOW
					}
				});
				dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
		
		restaurant_rating = (TextView) findViewById(R.id.restaurant_rating);
		restaurant_rating.setText("3.5/5.0");
		restaurant_rating.setTypeface(null, Typeface.BOLD);
		
		restaurant_website = (TextView) findViewById(R.id.restaurant_website);
		restaurant_website.setText(Html.fromHtml(
	            "<a href=\"http://subway.com\">http://subway.com</a> "));
		restaurant_website.setMovementMethod(LinkMovementMethod.getInstance());
		restaurant_website.setTypeface(null, Typeface.BOLD);
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
