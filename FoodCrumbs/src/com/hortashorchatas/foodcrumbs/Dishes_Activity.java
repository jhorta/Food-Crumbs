package com.hortashorchatas.foodcrumbs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.os.Build;

public class Dishes_Activity extends Activity {
	
	private ListView dish_list;
	private ArrayList<Dish> dishes = new ArrayList<Dish>();
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<String> ratings = new ArrayList<String>();
	private ArrayList<String> prices = new ArrayList<String>();
	
	private String business_id;
	private CustomListView adapter;
	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dishes_);
		
		Intent i = getIntent();
		business_id = i.getStringExtra("business_id");
		
		Log.i("Dishes", business_id);
		
		db = new DatabaseHandler(this);
		
		boolean hasDishes = db.hasDishes(business_id);
		
		if (hasDishes) {
			dishes = db.getDishes(business_id);
			
			for (int index = 0; index < dishes.size(); ++index) {
				Dish dish = dishes.get(index);
				names.add(dish.getName());
				ratings.add(dish.getRating());
				prices.add(dish.getPrice());
			}
		}
		
		adapter = new CustomListView(this, names, ratings, prices);
		
		dish_list = (ListView) findViewById(R.id.dish_list);
		
		dish_list.setAdapter(adapter);
		
		dish_list.setOnItemLongClickListener(new OnItemLongClickListener() {
            // setting onItemLongClickListener and passing the position to the function
                      public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int position, long arg3) {
                    	  final int item_pos = position;
                          AlertDialog.Builder alert = new AlertDialog.Builder(
                                  Dishes_Activity.this);
                      
                          alert.setTitle("Delete");
                          alert.setMessage("Do you want delete this item?");
                          alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int which) {
                            	  Dish dish = dishes.get(item_pos);
                            	  db.deleteDishes(dish.getBusiness_id(), dish.getName(), dish.getRating(), dish.getPrice());  
                            	  refreshList();
                              }
                          });
                          alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int which) {
                                  dialog.dismiss();
                              }
                          });
                          alert.show();
                return true;
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dishes_, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add_dish) {
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.add_dish_layout);
			dialog.setTitle("Please Add Dish Information");
 
			// set the custom dialog components - text, image and button
			final EditText dish_name = (EditText) dialog.findViewById(R.id.dish_name_text);
			final EditText dish_rating = (EditText) dialog.findViewById(R.id.dish_rating);
			final EditText dish_price = (EditText) dialog.findViewById(R.id.dish_price);

			
			Button doneButton = (Button) dialog.findViewById(R.id.done_button);
			
			doneButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					db.addDish(business_id, dish_name.getText().toString(), dish_rating.getText().toString(), dish_price.getText().toString());
					refreshList();
					dialog.dismiss();
				}
			});
			dialog.show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void refreshList() {
		adapter.clear();
		dishes.clear();
		names.clear();
		ratings.clear();
		prices.clear();
		
		dishes = db.getDishes(business_id);
		
		for (int index = 0; index < dishes.size(); ++index) {
			Dish dish = dishes.get(index);
			names.add(dish.getName());
			ratings.add(dish.getRating());
			prices.add(dish.getPrice());
		}
		
		CustomListView adapter = new CustomListView(this, names, ratings, prices);
		
		dish_list = (ListView) findViewById(R.id.dish_list);
		
		dish_list.setAdapter(adapter);
	}
	
	private class CustomListView extends ArrayAdapter<String> {
		
		Activity context;
		ArrayList<String> names;
		ArrayList<String> ratings;
		ArrayList<String> prices;
		
		public CustomListView(Activity context, ArrayList<String> names, ArrayList<String> ratings, ArrayList<String> prices) {
			super(context, R.layout.dish_row_custom, names);
			
			this.context = context;
			this.names = names;
			this.ratings = ratings;
			this.prices = prices;
		}
		
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView= inflater.inflate(R.layout.dish_row_custom, null, true);
			
			TextView dish_name = (TextView) rowView.findViewById(R.id.dish_name);
			TextView rating =  (TextView) rowView.findViewById(R.id.dish_rating);
			TextView price =  (TextView) rowView.findViewById(R.id.price);

			dish_name.setText(names.get(position));
			rating.setText(ratings.get(position)+"/5");
			price.setText("$"+prices.get(position));
			
			return rowView;
		}
	}
}

