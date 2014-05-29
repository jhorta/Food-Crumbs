package com.hortashorchatas.foodcrumbs;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Build;

public class Filters_Activity extends Activity implements OnItemSelectedListener {
	private EditText search_query;
	
	private Spinner radius_filter_spinner;
	
	private RadioButton time_filter;
	private RadioButton distance_filter;
	
	private TextView time_distance_filter_label;
	private EditText time_distance_filter_text;
	
	private Button submit_button;
	
	private boolean isFilterTime;
	
	private String radius_Dist[] = {"0.5 mi", "1 mi", "2 mi", "5 mi", "10 mi", "20 mi"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filters_);
		
		
		search_query = (EditText) findViewById(R.id.search_filter);
		
		radius_filter_spinner = (Spinner) findViewById(R.id.radius_filter_spinner);
		
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
				Intent i = getIntent();
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
		});
	}
	
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
		getMenuInflater().inflate(R.menu.filters_, menu);
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		radius_filter_spinner.setSelection(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

}
