package com.hortashorchatas.foodcrumbs;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class RestaurantActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant);
		
		Intent i = getIntent();
		Restaurant restaurant = (Restaurant) i.getSerializableExtra("Restaurant_Class");
		String name = restaurant.name;
		String rating = restaurant.rating;
		String address = restaurant.address;
		String id = restaurant.business_id;

		TextView name_text = (TextView) findViewById(R.id.restaurant_name);
		name_text.setText(name);
		TextView address_text = (TextView) findViewById(R.id.address);
		address_text.setText(address);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.restaurant_menu, menu);
		return true;
	}

}
