package com.hortashorchatas.foodcrumbs;

import android.animation.TimeAnimator;
import android.annotation.TargetApi;
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
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class Random_Generator_Activity extends Activity {
	
	private Button spin_button;
	private TextView random_text;
	private TimeAnimator mTime_anim;
	private String[] types = {"Fast Food", "Cafe", "Bistro", "Coffee", "Pub", "East African", "Horn African", "North African",
			"South African", "West African", "Chinese", "Japanese", "Korean", "Mongolian BBQ", "Vietnamese", "Ramen", "Pho",
			"Thai", "Indian", "Turkish", "Polish", "German", "Mediterranean", "Russian", "Danish", "Belgian", "French",
			"Mexican", "American", "Burgers", "Steak", "Burritos", "Pizza", "Tacos", "Horchatas"};

	private int random_type_index;
	private boolean canTouchTextView = false;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_random__generator);
		
		random_text = (TextView) findViewById(R.id.random_text);
		random_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (canTouchTextView) {
					Intent i = new Intent(getApplicationContext(), Map_View_Activity.class);
					i.putExtra("Random Filter", types[random_type_index]);
					startActivity(i);
					finish();
				}
			}
		});
		
		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("OS Jelly Bean needed.");
			builder.setMessage("Your phone physically cannot run this application. \n To run this portion of the app you need to upgrade your OS...");
			builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
			builder.show();
		}
		
		if (mTime_anim != null) {
			mTime_anim.cancel();
		}
		
		spin_button = (Button) findViewById(R.id.spin_button);
		spin_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				random_type_index = randomGenerator();
				random_text.setText("GO!");
				
				spin_button.setEnabled(false);
				canTouchTextView = false;
				
				mTime_anim = new TimeAnimator();
				mTime_anim.setTimeListener(new TimeAnimator.TimeListener() {
					int text_count = 0;
					int arr_count = 0;
					@Override
					public void onTimeUpdate(TimeAnimator animation, long totalTime,
							long deltaTime) {
						// TODO Auto-generated method stub
						if ((totalTime % 5) == 0) {
							if (text_count == 2) {
								spin_button.setEnabled(true);
								spin_button.setText("Spin Again?");
								canTouchTextView = true;
								mTime_anim.cancel();
							} else {
								if (arr_count == random_type_index) {
									++text_count;
								} else if (arr_count > 34) {
									arr_count = 0;
								}
								random_text.setText(types[arr_count]);
								++arr_count;
							}
						}
					}
				});
				
				mTime_anim.start();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.random__generator_, menu);
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
	
	private int randomGenerator() {
		int random_num = (int) (Math.random()*300000+1);
		int index = random_num % 35;
		return index;
	}

}
