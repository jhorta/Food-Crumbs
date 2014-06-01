package com.hortashorchatas.foodcrumbs;

import java.util.Calendar;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class Main_Menu_Activity extends ActionBarActivity {
    private static final int SELECT_PICTURE = 1;
    private DatabaseHandler db;
    private String img_resource;
    private Button select_image;
    private String prof_name;
    
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
					prof_name = text.getText().toString();
					db.addProfileInfo(img_resource, prof_name, 1);
					createPage(img_resource, prof_name);
					dialog.dismiss();
				}
			});
			dialog.show();
		} else {
			img_resource = db.getProfilePic();
			prof_name = db.getProfileName();
			createPage(img_resource, prof_name);
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
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if (hour >= 7 && hour < 12) {
			name_text.setText("Good morning, " + "\n" + name + "!");
		} else if (hour >= 12 && hour < 18) {
			name_text.setText("Good afternoon, " + "\n" + name + "!");
		} else if (hour >= 18 && hour < 24) {
			name_text.setText("Good evening, " + "\n" +  name + "!");
		} else {
			name_text.setText("Good night, " + "\n" + name + "!");
		}
		
		// Should test --> check if correct size.
		final Button favorites_button = (Button) findViewById(R.id.favorites_button);
		favorites_button.setVisibility(View.VISIBLE);
		
		// Should test --> check if correct size.
		final Button directions_button = (Button) findViewById(R.id.directions_button);
		directions_button.setVisibility(View.VISIBLE);
		
		// Should test --> check if correct size.
		final Button random_button = (Button) findViewById(R.id.random_button);
		random_button.setVisibility(View.VISIBLE);
	}
	
	private void hideViews() {
		final ImageView prof_pic = (ImageView) findViewById(R.id.profile_pic);
		prof_pic.setVisibility(View.GONE);
		final TextView name_text = (TextView) findViewById(R.id.name_text);
		name_text.setVisibility(View.GONE);
		final Button favorites_button = (Button) findViewById(R.id.favorites_button);
		favorites_button.setVisibility(View.GONE);
		final Button directions_button = (Button) findViewById(R.id.directions_button);
		directions_button.setVisibility(View.GONE);
		final Button random_button = (Button) findViewById(R.id.random_button);
		random_button.setVisibility(View.GONE);
	}
	
	private final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	
    @SuppressLint("NewApi")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (isKitKat && DocumentsContract.isDocumentUri(this, selectedImageUri)) {
                	if (isExternalStorageDocument(selectedImageUri)) {
                        final String docId = DocumentsContract.getDocumentId(selectedImageUri);
                        final String[] split = docId.split(":");
                        final String type = split[0];

                        if ("primary".equalsIgnoreCase(type)) {
                            img_resource = Environment.getExternalStorageDirectory() + "/" + split[1];
                        }
                    } else if (isDownloadsDocument(selectedImageUri)) {

                        final String id = DocumentsContract.getDocumentId(selectedImageUri);
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                        img_resource = getDataColumn(contentUri, null, null);
                    } else if (isMediaDocument(selectedImageUri)) {
                        final String docId = DocumentsContract.getDocumentId(selectedImageUri);
                        final String[] split = docId.split(":");
                        final String type = split[0];

                        Uri contentUri = null;
                        if ("image".equals(type)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(type)) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(type)) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        final String selection = "_id=?";
                        final String[] selectionArgs = new String[] {
                                split[1]
                        };

                        img_resource = getDataColumn(contentUri, selection, selectionArgs);
                    }
                } else if ("content".equalsIgnoreCase(selectedImageUri.getScheme())) {

                    // Return the remote address
                    if (isGooglePhotosUri(selectedImageUri))
                        img_resource =  selectedImageUri.getLastPathSegment();

                    img_resource = getDataColumn(selectedImageUri, null, null);
                }
                else if ("file".equalsIgnoreCase(selectedImageUri.getScheme())) {
                	img_resource = selectedImageUri.getPath();
                }
                select_image.setEnabled(false);
                select_image.setText("Image selected.");
            }
        }
    }
    
    public String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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
					if (!text.getText().toString().equals("")) {
						prof_name = text.getText().toString();
					}
					db.updateProfileInfo(img_resource, prof_name, 1);
					createPage(img_resource, prof_name);
					dialog.dismiss();
				}
			});
			dialog.show();
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
		i.putExtra("Source", Globals.SOURCE_HOME_PAGE);
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
