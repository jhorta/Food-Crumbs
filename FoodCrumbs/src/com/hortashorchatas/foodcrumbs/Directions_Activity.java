	package com.hortashorchatas.foodcrumbs;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Build;

public class Directions_Activity extends Activity implements OnItemSelectedListener {
	
	private EditText start_location;
	private EditText end_location;
	private EditText search_query;
	
	private Spinner radius_filter_spinner;
	
	private RadioButton time_filter;
	private RadioButton distance_filter;
	
	private TextView time_distance_filter_label;
	private EditText time_distance_filter_text;
	
	private Button submit_button;
	
	private boolean isFilterTime;
	
	private String radius_Dist[] = {"0.5 mi", "1 mi", "2 mi", "5 mi", "10 mi", "20 mi"};
	
	/**
	 * Creates the activity. In this method, I connect all of the view objects to the 
	 * corresponding views on the xml files and set up instance variables.
	 * No functionalities to test in this method.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directions_);
		
		final TextView filter_text = (TextView) findViewById(R.id.filter_text);
		filter_text.setText("Filters:");
		
		final TextView radius_label = (TextView) findViewById(R.id.search_radius_label);
		radius_label.setText("Search Radius: ");
		
		start_location = (EditText) findViewById(R.id.start_location);
		end_location = (EditText) findViewById(R.id.end_location);
		search_query = (EditText) findViewById(R.id.yelp_search_query_filter);
				
		radius_filter_spinner = (Spinner) findViewById(R.id.yelp_distance_filter_spinner);
		
		ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, radius_Dist);
		adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		radius_filter_spinner.setAdapter(adapter_state);
		radius_filter_spinner.setOnItemSelectedListener(this);
		
		time_filter = (RadioButton) findViewById(R.id.radio_button_filter_time);
		time_filter.toggle();
		distance_filter = (RadioButton) findViewById(R.id.radio_button_filter_distance);
		
		isFilterTime = true;
		
		time_distance_filter_label = (TextView) findViewById(R.id.label_text_hours_or_miles);
		time_distance_filter_label.setText("Hours");
		
		time_distance_filter_text = (EditText) findViewById(R.id.edit_text_hours_or_miles);
		
		submit_button = (Button) findViewById(R.id.submit_button);
		submit_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (start_location.getText().toString().equals("") || start_location.getText().toString() == null) {
					final AlertDialog.Builder dialog = new AlertDialog.Builder(Directions_Activity.this);
					dialog.setTitle("Error!");
					dialog.setMessage("Please input an origin!");
					dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					dialog.show();
				} else if (end_location.getText().toString().equals("") || end_location.getText().toString() == null) {
					final AlertDialog.Builder dialog = new AlertDialog.Builder(Directions_Activity.this);
					dialog.setTitle("Error!");
					dialog.setMessage("Please input a destination!");
					dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					dialog.show();
				} else {
					Intent i = getIntent();
					i.putExtra("Start Location", start_location.getText().toString());
					i.putExtra("End Location", end_location.getText().toString());
					i.putExtra("Search Query", search_query.getText().toString());
					int radius_position = radius_filter_spinner.getSelectedItemPosition();
					String radius_string = radius_Dist[radius_position].replace(" mi", "");
					i.putExtra("Radius", radius_string);
					if (time_distance_filter_text.getText().toString().equals("") || time_distance_filter_text.getText().toString() == null) {
						i.putExtra("Time", "0");
						i.putExtra("Distance", "0");
					} else if (isFilterTime) {
						i.putExtra("Time", time_distance_filter_text.getText().toString());
						i.putExtra("Distance", "0");
					} else {
						i.putExtra("Time", "0");
						i.putExtra("Distance", time_distance_filter_text.getText().toString());
					}
					setResult(RESULT_OK, i);
					finish();
				}
			}
		});
	}
	
	/**
	 * This method handles what happens when the radio buttons for Time and Distance get clicked.
	 * If radio_button_filter_time (Time button) is clicked, the label time_distance_filter_label
	 * should be changed to "Hours" and the distance_filter (Distance button) should be denoted as
	 * unchecked while the Time button should be denoted as checked. If the Distance button is clicked,
	 * the label should be changed to Miles and Distance button should be chcekd while the Time button
	 * should be unchecked.
	 * @param view
	 */
	public void onRadioButtonClicked(View view) {
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    switch(view.getId()) {
	    	case R.id.radio_button_filter_time:
	    		if (checked) {
	    			isFilterTime = true;
	    			time_distance_filter_label.setText("Hours");
	    			distance_filter.setChecked(false);
	    		}
	    		break;
	    	case R.id.radio_button_filter_distance:
	    		if (checked) {
	    			isFilterTime = false;
	    			time_distance_filter_label.setText("Miles");
	    			time_filter.setChecked(false);
	    		}
	    		break;
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.directions_, menu);
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
	 * Doesn't really do anything yet. It lets the spinner object (drop down list) know that 
	 * the selected item has changed and that the object should set the selection to the 
	 * object in the position that it is.
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		radius_filter_spinner.setSelection(position);
		//String selectedItem = (String) distance_filter_spinner.getSelectedItem();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}

}
