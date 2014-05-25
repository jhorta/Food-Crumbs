package com.hortashorchatas.foodcrumbs;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;
import android.provider.MediaStore;

public class Main_Menu_Activity extends ActionBarActivity {
    private static final int SELECT_PICTURE = 1;
    DatabaseHandler db;
    String img_resource;
    Button select_image;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main__menu);
		
		db = new DatabaseHandler(this);
		
		img_resource = "";
		
		int first_install = 0;
		try {
			first_install = db.getIsFirstInstall();
		} catch (Exception e) {
			first_install = 0;
		}
		
		if (first_install == 0) {
			hideViews();
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.initial_dialog_layout);
			dialog.setTitle("Please Choose a Picture and a Username");
 
			// set the custom dialog components - text, image and button
			final EditText text = (EditText) dialog.findViewById(R.id.name_text);
 
			select_image = (Button) dialog.findViewById(R.id.select_image);
			// if button is clicked, close the custom dialog
			select_image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
				}
			});
			
			Button doneButton = (Button) dialog.findViewById(R.id.done_button);
			
			doneButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					db.addProfileInfo(img_resource, text.getText().toString(), 1);
					createPage(img_resource, text.getText().toString());
					dialog.dismiss();
				}
			});
			dialog.show();
		} else {
			img_resource = db.getProfilePic();
			String name = db.getProfileName();
			createPage(img_resource, name);
		}
	}
	
	private void createPage(String img_resource, String name) {
		final ImageView prof_pic = (ImageView) findViewById(R.id.profile_pic);
		prof_pic.setVisibility(View.VISIBLE);
		prof_pic.getLayoutParams().height = 160;
		prof_pic.getLayoutParams().width = 160;
		if (img_resource != "") {
			prof_pic.setImageURI(Uri.parse(img_resource));
		} else {
			prof_pic.setImageResource(R.drawable.defaultprof);
		}
		
		// Should test --> check if text is set to "Hello, 'name input'!"
		final TextView name_text = (TextView) findViewById(R.id.name_text);
		name_text.setVisibility(View.VISIBLE);
		name_text.setText("Hello! " + name);
		
		// Should test --> check if correct size.
		final ImageButton favorites_button = (ImageButton) findViewById(R.id.favorites_button);
		favorites_button.setVisibility(View.VISIBLE);
		favorites_button.getLayoutParams().height = 200;
		favorites_button.getLayoutParams().width = 200;
		
		// Should test --> check if correct size.
		final ImageButton directions_button = (ImageButton) findViewById(R.id.directions_button);
		directions_button.setVisibility(View.VISIBLE);
		directions_button.getLayoutParams().height = 200;
		directions_button.getLayoutParams().width = 200;
		
		// Should test --> check if correct size.
		final ImageButton random_button = (ImageButton) findViewById(R.id.random_button);
		random_button.setVisibility(View.VISIBLE);
		random_button.getLayoutParams().height = 200;
		random_button.getLayoutParams().width = 200;	
	}
	
	private void hideViews() {
		final ImageView prof_pic = (ImageView) findViewById(R.id.profile_pic);
		prof_pic.setVisibility(View.GONE);
		final TextView name_text = (TextView) findViewById(R.id.name_text);
		name_text.setVisibility(View.GONE);
		final ImageButton favorites_button = (ImageButton) findViewById(R.id.favorites_button);
		favorites_button.setVisibility(View.GONE);
		final ImageButton directions_button = (ImageButton) findViewById(R.id.directions_button);
		directions_button.setVisibility(View.GONE);
		final ImageButton random_button = (ImageButton) findViewById(R.id.random_button);
		random_button.setVisibility(View.GONE);
	}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                img_resource = getPath(selectedImageUri);
                select_image.setEnabled(false);
                select_image.setText("Image selected.");
            }
        }
    }
    
    public String getPath(Uri uri) {
        String res = "";
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor.moveToFirst()){
        	int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        	res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
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

}
