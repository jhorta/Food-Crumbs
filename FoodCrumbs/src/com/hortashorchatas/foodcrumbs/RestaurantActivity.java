package com.hortashorchatas.foodcrumbs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;

import org.json.JSONException;

import com.google.android.gms.maps.model.LatLng;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	private ImageView favorite_star;
	
	private TextView restaurant_name;
	private TextView restaurant_address;
	
	private Spinner restaurant_hours_spinner;
	
	private TextView restaurant_phone_number;
	private TextView restaurant_rating;
	private TextView restaurant_website;
	private TextView review_text;
	
	private String_Parser json_response_parser;
	
	private HashMap<String, String> details;
	
	private ArrayAdapter<String> adapter_state;
	
	private boolean isFavorite;
	
	private String restaurant_id_reference;
	private String restaurant_name_reference;
	private String restaurant_address_reference;
	private double restaurant_latitude_reference;
	private double restaurant_longitude_reference;
	
	private int day;
	private String restaurant_rating_reference;
	private String restaurant_reference;
	
	private Button dishes_button;
	
	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant);
		
		restaurant_id_reference = "";
		restaurant_rating_reference = "";
		restaurant_address_reference = "";
		restaurant_latitude_reference = 0;
		restaurant_latitude_reference = 0;
		restaurant_name_reference = "";
		
		json_response_parser = new String_Parser();
        
        details = new HashMap<String, String>();
		
		Intent i = getIntent();
		restaurant_reference = i.getStringExtra("restaurant_reference_id");
		
		Log.i("RefID", restaurant_reference);
		
		db = new DatabaseHandler(this);
		
		try {
			URL url = new URL(new String("http://192.241.180.205:9292/restaurantInfo?reference=" + restaurant_reference));
//			URL url = new URL("https://maps.googleapis.com/maps/api/place/details/json?reference="+restaurant_reference+"&sensor=true&key="+Globals.GOOGLE_PLACES_API_KEY);
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
		restaurant_name.setText("Restaurant Name Placeholder");
		restaurant_name.setTypeface(null, Typeface.BOLD);
		
		restaurant_address = (TextView) findViewById(R.id.restaurant_address);
		restaurant_address.setText("Restaurant Address, Placeholder");
		restaurant_address.setTypeface(null, Typeface.BOLD);
		restaurant_address.setTextColor(Color.BLUE);
		restaurant_address.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final AlertDialog.Builder dialog = new AlertDialog.Builder(RestaurantActivity.this);
				dialog.setTitle("Go to Google Map");
				dialog.setMessage("Would you like to find this address on your Google Map Application?");
				dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+restaurant_address.getText().toString()));
						startActivity(intent);
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
		
		restaurant_hours_spinner = (Spinner) findViewById(R.id.restaurant_hours);
		
		restaurant_hours[0] = "Sun 9am-5pm";
		restaurant_hours[1] = "Mon 9am-5pm";
		restaurant_hours[2] = "Tue 9am-5pm";
		restaurant_hours[3] = "Wed 9am-5pm";
		restaurant_hours[4] = "Thu 9am-5pm";
		restaurant_hours[5] = "Fri 9am-5pm";
		restaurant_hours[6] = "Sat 9am-5pm";
		
		Calendar calendar = Calendar.getInstance();
		day = calendar.get(Calendar.DAY_OF_WEEK); 
		
		adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, restaurant_hours);
		adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		restaurant_hours_spinner.setAdapter(adapter_state);
		restaurant_hours_spinner.setSelection(day-1);
		
		restaurant_phone_number = (TextView) findViewById(R.id.restaurant_phone_number);
		restaurant_phone_number.setText("+PhoneNumberPlaceholder");
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
						Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+restaurant_phone_number.getText().toString()));
						startActivity(i);
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
		restaurant_rating.setText("Rating/5.0");
		restaurant_rating.setTypeface(null, Typeface.BOLD);
		
		restaurant_website = (TextView) findViewById(R.id.restaurant_website);
		restaurant_website.setText(Html.fromHtml(
	            "<a href=\"about:blank\">Website Placeholder</a> "));
		restaurant_website.setMovementMethod(LinkMovementMethod.getInstance());
		restaurant_website.setTypeface(null, Typeface.BOLD);
		
		favorite_star = (ImageView) findViewById(R.id.restaurant_favorite);
		
		favorite_star.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isFavorite) {
					isFavorite = false;
					favorite_star.setImageResource(R.drawable.ic_not_favorite);
					db.deleteFavorite(restaurant_id_reference);
				} else {
					isFavorite = true;
					favorite_star.setImageResource(R.drawable.ic_favorite);
					Restaurant restaurant = new Restaurant(restaurant_reference, restaurant_id_reference, restaurant_name_reference,
							restaurant_address_reference, new LatLng(restaurant_latitude_reference, restaurant_longitude_reference),
							restaurant_rating_reference);
					db.addFavorite(restaurant);
				}
			}
		});
		
		review_text = (TextView) findViewById(R.id.best_review_text);
		review_text.setTypeface(null, Typeface.BOLD);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.restaurant_menu, menu);
		return true;
	}
	
	private void getDetails() {
		try {
			details = json_response_parser.getDetails();
			
			restaurant_hours = json_response_parser.getRestaurantHours();
			
			String review = json_response_parser.getReview();
			if (review.equals("")) {
				review_text.setText("No reviews for this retaurant.");
			} else {
				review_text.setText(review);
			}
			
			adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, restaurant_hours);
			adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			restaurant_hours_spinner.setAdapter(adapter_state);
			restaurant_hours_spinner.setSelection(day-1);
			
			if (!details.get("photo").equals("")) {
				ImageDownloadTask[] imageDownloadTask = new ImageDownloadTask[1];
				if (!details.get("photo").equals("")) {
					String url = new String("https://maps.googleapis.com/maps/api/place/photo?photoreference="+details.get("photo")+"&sensor=false&maxheight="+details.get("height")+"&maxwidth="+details.get("width")+"&key="+Globals.GOOGLE_PLACES_API_KEY);
					imageDownloadTask[0] = new ImageDownloadTask();
					imageDownloadTask[0].execute(url);
				}
			}
			
			restaurant_id_reference = details.get("id");
			restaurant_rating_reference = details.get("rating");
			restaurant_address_reference = details.get("address");
			restaurant_latitude_reference = Double.parseDouble(details.get("latitude"));
			restaurant_latitude_reference = Double.parseDouble(details.get("longitude"));
			restaurant_name_reference = details.get("name");
			
			restaurant_name.setText(restaurant_name_reference);
			restaurant_address.setText(restaurant_address_reference);
			restaurant_phone_number.setText(details.get("phone number"));
			
			if (details.get("rating").equals("")) {
				restaurant_rating.setText("No Rating.");
			} else {
				restaurant_rating.setText(details.get("rating")+"/"+"5");
			}
			if (details.get("website").equals("")) {
				restaurant_website.setText("No Website.");
			} else {
				restaurant_website.setText(Html.fromHtml(
			            "<a href=\""+details.get("website")+"\">"+"Website"+"</a> "));
			}
			
			isFavorite = db.getIsFavorite(restaurant_id_reference);
			
			if (isFavorite) {
				Log.i("RESTAURANT ACTIVITY", "IS FAVORITE");
				favorite_star.setImageResource(R.drawable.ic_favorite);
			} else {
				Log.i("RESTAURANT ACTIVITY", "IS NOT FAVORITE");
			}
			
			dishes_button = (Button) findViewById(R.id.dishes_button);
			dishes_button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), Dishes_Activity.class);
					i.putExtra("business_id", restaurant_id_reference);
					startActivity(i);
				}
				
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void setImage(Bitmap result) {
		if (result != null) {
			restaurant_image.setImageBitmap(result);
		}
	}
	
    private Bitmap downloadImage(String strUrl) throws IOException{
        Bitmap bitmap=null;
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);
 
            /** Creating an http connection to communcate with url */
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
 
            /** Connecting to url */
            urlConnection.connect();
 
            /** Reading data from url */
            iStream = urlConnection.getInputStream();
 
            /** Creating a bitmap from the stream returned from the url */
            bitmap = BitmapFactory.decodeStream(iStream);
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
        }
        return bitmap;
    }
    
	private class getDetailsFromServer extends AsyncTask<URL, Void, String> {
	    private ProgressDialog dialog;

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
		protected void onPreExecute() {
			dialog = ProgressDialog.show(RestaurantActivity.this, "Please wait ...", "Loading Restaurant...", true);
		}

		@Override
		protected void onPostExecute(String result) {
	        if (dialog.isShowing()) {
	            dialog.dismiss();
	        }
	        json_response_parser.setNewQueryReponse(result);
			
			getDetails();
			Log.i("Ohs yarhh", result);
		}
	}
	
	 private class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap>{
	        Bitmap bitmap = null;
	        @Override
	        protected Bitmap doInBackground(String... url) {
	            try{
	                // Starting image download
	                bitmap = downloadImage(url[0]);
	            }catch(Exception e){
	                Log.d("Background Task",e.toString());
	            }
	            return bitmap;
	        }
	 
	        @Override
	        protected void onPostExecute(Bitmap result) {
	            setImage(result);
	        }
	        
	        
	    }
}
