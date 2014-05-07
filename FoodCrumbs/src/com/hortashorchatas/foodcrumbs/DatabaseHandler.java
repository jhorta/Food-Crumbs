package com.hortashorchatas.foodcrumbs;

import java.util.ArrayList;
import java.util.List;

import com.hortashorchatas.foodcrumbs.Restaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
 
    // Favorites Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_RATING = "rating";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
 // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT" + KEY_LOCATION + " TEXT" + KEY_RATING + " TEXT" + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }
    
 // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
 
        // Create tables again
        onCreate(db);
    }
    
    //addFavorite()
    // Adding new favorite
    
public void addFavorite(Restaurant favorite) {
    SQLiteDatabase db = this.getWritableDatabase();
 
    ContentValues values = new ContentValues();
    values.put(KEY_ID, favorite.business_id); // Restaurant Name
    values.put(KEY_NAME, favorite.name); // Restaurant Name
    values.put(KEY_ADDRESS, favorite.address); // Restaurant Phone
    //values.put(KEY_LOCATION, favorite.location); // Restaurant Phone
    values.put(KEY_RATING, favorite.rating); // Restaurant Phone

    
    // Inserting Row
    db.insert(TABLE_FAVORITES, null, values);
    db.close(); // Closing database connection
}


//Getting single favorite
Restaurant getFavorite(int id) {
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.query(TABLE_FAVORITES, new String[] { KEY_ID,
            KEY_NAME, KEY_ADDRESS, KEY_LOCATION, KEY_RATING }, KEY_ID + "=?",
            new String[] { String.valueOf(id) }, null, null, null, null);
    if (cursor != null)
        cursor.moveToFirst();

    Restaurant favorite = new Restaurant(cursor.getString(0),
            cursor.getString(1), cursor.getString(2), null, cursor.getString(3));
    // return favorite
    return favorite;
}


//Getting All Favorites
public List<Restaurant> getAllFavorites() {
    List<Restaurant> favoriteList = new ArrayList<Restaurant>();
    // Select All Query
    String selectQuery = "SELECT  * FROM " + TABLE_FAVORITES;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
        do {
        	Restaurant favorite = new Restaurant();
        	favorite.business_id = cursor.getString(0);
        	favorite.name = cursor.getString(1);
        	favorite.address = cursor.getString(2);
//        	favorite.location = cursor.getString(2);
        	favorite.rating = cursor.getString(2);
            // Adding favorite to list
        	favoriteList.add(favorite);
        } while (cursor.moveToNext());
    }

    // return favorite list
    return favoriteList;
}


//Updating single favorite
public int updateFavorite(Restaurant favorite) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_ID, favorite.business_id);
    values.put(KEY_NAME, favorite.name);
    values.put(KEY_ADDRESS, favorite.address);
//    values.put(KEY_LOCATION, favorite.location);
    values.put(KEY_RATING, favorite.rating);

    // updating row
    return db.update(TABLE_FAVORITES, values, KEY_ID + " = ?",
            new String[] { String.valueOf(favorite.business_id) });
}

//Deleting single favorite
public void deleteFavorite(Restaurant favorite) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_FAVORITES, KEY_ID + " = ?",
            new String[] { String.valueOf(favorite.business_id) });
    db.close();
}

//Getting favorites Count
public int getFavoriteCount() {
    String countQuery = "SELECT  * FROM " + TABLE_FAVORITES;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    cursor.close();

    // return count
    return cursor.getCount();
}

 
}
