package com.hortashorchatas.foodcrumbs;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.hortashorchatas.foodcrumbs.Restaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 */

/**
 * @author cs110xas
 *
 */
public class DatabaseHandler extends SQLiteOpenHelper{

	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "favoritesManager";
 
    // Favorites table name
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_INFO = "info";
 
    // Favorites Table Columns keys
    private static final String KEY_ID = "id";
    private static final String KEY_REFERENCE_ID = "reference";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
//    private static final String KEY_LOCATION = "location";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_RATING = "rating";
    
    // User Info Table Column keys
    private static final String KEY_PROF_PIC_URL = "picture";
    private static final String KEY_PROF_NAME = "name";
    private static final String KEY_IS_FIRST_INSTALL = "first_install";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /**
     * Creates a both a Favorites Table and a User Info Table
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_REFERENCE_ID + " TEXT," + KEY_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT," + KEY_RATING + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT" + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
        
        String CREATE_INFO_TABLE = "CREATE TABLE " + TABLE_INFO + "(" + KEY_IS_FIRST_INSTALL + " INTEGER,"
        		+ KEY_PROF_PIC_URL + " TEXT," + KEY_PROF_NAME + " TEXT" + ")";
        db.execSQL(CREATE_INFO_TABLE);
    }
    
 // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_INFO);
        // Create tables again
        onCreate(db);
    }
    
    /**
     * This function adds profile information such as the profile picture url and the user's name into
     * the User Info Table.
     * @return none
     */
    public void addProfileInfo(String prof_pic_url, String name, int value) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_IS_FIRST_INSTALL, value);
    	values.put(KEY_PROF_PIC_URL, prof_pic_url);
    	values.put(KEY_PROF_NAME, name);
    	
    	db.insert(TABLE_INFO, null, values);
    	db.close();
    }
    
    /**
     * This function gets whether or not the app is opening after the install or subsequently.
     * @return integer 0 or 1
     */
    public int getIsFirstInstall() {
    	int firstInstall = 0;
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	
	    Cursor cursor = db.rawQuery("SELECT "+KEY_IS_FIRST_INSTALL+" FROM "+TABLE_INFO+";", null);
	    cursor.moveToFirst();
	    firstInstall = cursor.getInt(0);
	    
    	return firstInstall;
    }
    
    /**
     * This gets the String representation of a uri that would lead to the directory of the profile
     * picture that the user has chosen in the Main Menu.
     * @return String
     */
    public String getProfilePic() {
    	String prof_pic = "";
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	
	    Cursor cursor = db.rawQuery("SELECT "+KEY_PROF_PIC_URL+" FROM "+TABLE_INFO+";", null);
	    cursor.moveToFirst();
    	prof_pic = cursor.getString(0);
	    
    	return prof_pic;
    }
    
    /**
     * This gets the name of the User that User has input on first install.
     * @return String
     */
    public String getProfileName() {
    	String name = "";
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	
	    Cursor cursor = db.rawQuery("SELECT "+KEY_PROF_NAME+" FROM "+TABLE_INFO+";", null);
	    cursor.moveToFirst();
	    name = cursor.getString(0);
	    
    	return name;
    }
    
    /**
     * When user updates their profile information through the settings tab in the Main Menu, this is called
     * to update the values in the User Info page.
     * @param prof_pic_url
     * @param name
     * @param value
     */
	public void updateProfileInfo(String prof_pic_url, String name, int value)
	{
		ContentValues values = new ContentValues(3);
		values.put(KEY_IS_FIRST_INSTALL, value);
		values.put(KEY_PROF_PIC_URL, prof_pic_url);
		values.put(KEY_PROF_NAME, name);
		
		getWritableDatabase().update(TABLE_INFO, values, null, null);
	}
    
	/**
	 * This method adds a favorite Restaurant to the Database to the Favorites table. It takes in a parameter
	 * Restaurant, and each variable within the Restaurant class will be stored into the table.
	 * @param favorite
	 */
	public void addFavorite(Restaurant favorite) {
	
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_ID, favorite.business_id); // Restaurant Name
	    values.put(KEY_REFERENCE_ID, favorite.reference_id);	// Restaurant reference ID
	    values.put(KEY_NAME, favorite.name); // Restaurant Name
	    values.put(KEY_ADDRESS, favorite.address); // Restaurant Phone
	    values.put(KEY_RATING, favorite.rating); // Restaurant Phone
	    values.put(KEY_LATITUDE, String.valueOf(favorite.latitude));
	    values.put(KEY_LONGITUDE, String.valueOf(favorite.longitude));
	
	    
	    // Inserting Row
	    
	    db.insert(TABLE_FAVORITES, null, values);
	    db.close(); // Closing database connection
	}
	
	
	/**
	 * This method will get a single Favorite Restaurant from the Database. It will return a single Restaurant
	 * class type with all of the variables initiated based on the values from the Table.
	 * @param id
	 * @return Restaurant
	 */
	public Restaurant getFavorite(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	
	    Cursor cursor = db.query(TABLE_FAVORITES, new String[] { KEY_ID, KEY_REFERENCE_ID,
	            KEY_NAME, KEY_ADDRESS, KEY_RATING, KEY_LATITUDE, KEY_LONGITUDE}, KEY_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	
	    Restaurant favorite = new Restaurant(cursor.getString(1), cursor.getString(0),
	            cursor.getString(2), cursor.getString(3), 
	            new LatLng(Double.parseDouble(cursor.getString(5)), Double.parseDouble(cursor.getString(6))), 
	            cursor.getString(4));
	    // return favorite
	    return favorite;
	}
	
	/**
	 * This method will get whether or not a particular Restaurant is a Favorite or not based on the restaurant's
	 * reference id. This method will be used by the Restaurant Activity class to determine whether or not a particular
	 * Restaurant is a favorite.
	 * @param reference_id
	 * @return
	 */
	public boolean getIsFavorite(String reference_id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    
	    Cursor cursor = db.rawQuery("SELECT "+KEY_NAME+" FROM "+TABLE_FAVORITES+" WHERE "+KEY_REFERENCE_ID+"='"+reference_id+"'", null);
	    if (cursor != null && cursor.getCount() > 0) {
	    	return true;
	    } else {
	    	return false;
	    }
	}
	
	
	/**
	 * This method gets an ArrayList of Restaurant types of all the Restaurants that are favorites within
	 * the Database. This method is used by the Favorites Activity class to get all favorites.
	 * @return ArrayList<Restaurant>
	 */
	public ArrayList<Restaurant> getAllFavorites() {
		
		ArrayList<Restaurant> favoriteList = new ArrayList<Restaurant>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_FAVORITES;
	
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	Restaurant favorite = new Restaurant();
	        	favorite.business_id = cursor.getString(0);
	        	
	        	favorite.reference_id = cursor.getString(1);

	        	favorite.name = cursor.getString(2);
	
	        	favorite.address = cursor.getString(3);
	
	        	favorite.rating = cursor.getString(4);
	
	        	favorite.latitude = Double.parseDouble(cursor.getString(5));
	        	
	        	favorite.longitude = Double.parseDouble(cursor.getString(6));
	
	            // Adding favorite to list
	        	favoriteList.add(favorite);
	        } while (cursor.moveToNext());
	    }
	
	    // return favorite list
	    return favoriteList;
	}
	
	
	/**
	 * Updates the values of a single Favorite Restaurant in the Database. Takes a Restaurant favorite.
	 * @param favorite
	 * @return int
	 */
	public int updateFavorite(Restaurant favorite) {
	    SQLiteDatabase db = this.getWritableDatabase();
	
	    ContentValues values = new ContentValues();
	    values.put(KEY_ID, favorite.business_id);
	    values.put(KEY_REFERENCE_ID, favorite.reference_id);
	    values.put(KEY_NAME, favorite.name);
	    values.put(KEY_ADDRESS, favorite.address);
	//    values.put(KEY_LOCATION, favorite.location);
	    values.put(KEY_RATING, favorite.rating);
	    values.put(KEY_LATITUDE, String.valueOf(favorite.latitude));
	    values.put(KEY_LONGITUDE, String.valueOf(favorite.longitude));
	    
	    // updating row
	    return db.update(TABLE_FAVORITES, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(favorite.business_id) });
	}
	
	/**
	 * Deletes a single Favorite from the Database with a Restaurant.
	 * @param favorite
	 */
	public void deleteFavorite(Restaurant favorite) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_FAVORITES, KEY_ID + " = ?",
	            new String[] { String.valueOf(favorite.business_id) });
	    db.close();
	}
	
	/**
	 * Deletes a single Favorite from the Database with a reference id.
	 * @param reference_id
	 */
	public void deleteFavorite(String reference_id) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_FAVORITES, KEY_REFERENCE_ID+"="+reference_id, null);
	}
	
	/**
	 * This method returns the number of favorites within the favorites database.
	 * @return int
	 */
	public int getFavoriteCount() {
	    String countQuery = "SELECT  * FROM " + TABLE_FAVORITES;
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(countQuery, null);
	    cursor.close();
	
	    // return count
	    return cursor.getCount();
	}
}
