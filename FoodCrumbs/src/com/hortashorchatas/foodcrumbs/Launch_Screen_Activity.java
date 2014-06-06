package com.hortashorchatas.foodcrumbs;

import android.app.Activity;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class Launch_Screen_Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch__screen_);
		
		if (!isNetworkAvailable()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("No Network Available");
			builder.setMessage("Please connect to the internet.");
			builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
			builder.show();
		} else if (!hasGLES20()){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("OpenGL ES 2.0 needed.");
			builder.setMessage("Your phone physically cannot run this application. \n To run this app you need to upgrade...");
			builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
			builder.show();
		} else {
			Thread welcomeThread = new Thread() {
				@Override
				public void run() {
					try{
	                super.run();
	                sleep(3000);  //Delay of 3 seconds
					} catch (Exception e) {
						// Do nothing
					} finally {
						Intent i = new Intent(getApplicationContext(), Main_Menu_Activity.class);
						startActivity(i);
						finish();
					}
	            }
			};
			welcomeThread.start();
		}
	}
	
	private boolean hasGLES20() {
	    ActivityManager am = (ActivityManager)
                getSystemService(ACTIVITY_SERVICE);
	    ConfigurationInfo info = am.getDeviceConfigurationInfo();
	    return info.reqGlEsVersion >= 0x20000;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launch__screen_, menu);
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
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
