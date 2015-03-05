package com.wm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wm.db.DeviceDataContract.DeviceEntry;

public class DBHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "device_data.db";
    
    private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_DEVICE =
	    "CREATE TABLE " + DeviceEntry.TABLE_NAME + " (" +
	    DeviceEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTO INCREMENT," +
	    DeviceEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
	    DeviceEntry.COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP +
	    DeviceEntry.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
	    " )";

	private static final String SQL_DELETE_DEVICE =
	    "DROP TABLE IF EXISTS " + DeviceEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_DEVICE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_DEVICE);
		onCreate(db);
	}

}
