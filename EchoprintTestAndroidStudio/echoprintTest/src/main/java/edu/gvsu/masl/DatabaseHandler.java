package edu.gvsu.masl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehulsmritiraje on 12/12/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "songFingerprints";

    // Contacts table name
    private static final String TABLE_FINGERPRINTS = "fingerprints";

    // Contacts Table Columns names
    private static final String KEY_ID = "song_id";
    private static final String KEY_NAME = "song_name";
    private static final String KEY_PRINT = "song_fingerprint";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FINGERPRINTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PRINT + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINGERPRINTS);

        // Create tables again
        onCreate(db);
    }

    //public void addFingerprint(Integer id, String name, String fingerprint, Song song) {
    public void addFingerprint(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,Integer.parseInt(song.getID())); //Song ID
        values.put(KEY_NAME, song.getTitle()); // Song Name
        values.put(KEY_PRINT, song.getFingerprint()); // Song fingerprint

        // Inserting Row
        db.insert(TABLE_FINGERPRINTS, null, values);
        db.close(); // Closing database connection
    }

    public List<String> getAllFingerprintedIDs() {
        List<String> IDList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT " +KEY_NAME+ " FROM " + TABLE_FINGERPRINTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                IDList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // return contact list
        return IDList;
    }
}
