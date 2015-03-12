package com.wm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wm.db.DeviceDataContract.BPDataEntry;
import com.wm.db.DeviceDataContract.BSDataEntry;
import com.wm.db.DeviceDataContract.DeviceEntry;
import com.wm.db.DeviceDataContract.FHDataEntry;

public class DBHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "device_data.db";

	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER ";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_DEVICE = "CREATE TABLE "
			+ DeviceEntry.TABLE_NAME + " (" + DeviceEntry.COLUMN_NAME_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ DeviceEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP
			+ DeviceEntry.COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP
			+ DeviceEntry.COLUMN_NAME_TYPE + TEXT_TYPE + " )";
	private static final String SQL_CREATE_BPDATE = "CREATE TABLE "
			+ BPDataEntry.TABLE_NAME + " (" + BPDataEntry.COLUMN_NAME_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ BPDataEntry.COLUMN_NAME_SSVALUE + TEXT_TYPE + COMMA_SEP
			+ BPDataEntry.COLUMN_NAME_SZVALUE + TEXT_TYPE + COMMA_SEP
			+ BPDataEntry.COLUMN_NAME_HEART_RATE + TEXT_TYPE + COMMA_SEP
			+ BPDataEntry.COLUMN_NAME_HEART_RATE_STATE + TEXT_TYPE + COMMA_SEP
			+ BPDataEntry.COLUMN_NAME_DATE + INTEGER_TYPE + COMMA_SEP
			+ BPDataEntry.COLUMN_NAME_STATUS + INTEGER_TYPE + " )";
	private static final String SQL_CREATE_BSDATE = "CREATE TABLE "
			+ BSDataEntry.TABLE_NAME + " (" + BSDataEntry.COLUMN_NAME_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ BSDataEntry.COLUMN_NAME_BSVALUE + TEXT_TYPE + COMMA_SEP
			+ BSDataEntry.COLUMN_NAME_DATE + INTEGER_TYPE + COMMA_SEP
			+ BSDataEntry.COLUMN_NAME_STATUS + INTEGER_TYPE + " )";
	private static final String SQL_CREATE_FHDATE = "CREATE TABLE "
			+ FHDataEntry.TABLE_NAME + " (" + FHDataEntry.COLUMN_NAME_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ FHDataEntry.COLUMN_NAME_FHVALUES + TEXT_TYPE + COMMA_SEP
			+ FHDataEntry.COLUMN_NAME_DATE + INTEGER_TYPE + COMMA_SEP
			+ FHDataEntry.COLUMN_NAME_STATUS + INTEGER_TYPE + " )";

	private static final String SQL_DELETE_DEVICE = "DROP TABLE IF EXISTS "
			+ DeviceEntry.TABLE_NAME;
	private static final String SQL_DELETE_BPDATA = "DROP TABLE IF EXISTS "
			+ BPDataEntry.TABLE_NAME;
	private static final String SQL_DELETE_BSDATA = "DROP TABLE IF EXISTS "
			+ BSDataEntry.TABLE_NAME;
	private static final String SQL_DELETE_FHDATE = "DROP TABLE IF EXISTS "
			+ FHDataEntry.TABLE_NAME;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_DEVICE);
		db.execSQL(SQL_CREATE_BPDATE);
		db.execSQL(SQL_CREATE_BSDATE);
		db.execSQL(SQL_CREATE_FHDATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_DEVICE);
		db.execSQL(SQL_DELETE_BPDATA);
		db.execSQL(SQL_DELETE_BSDATA);
		db.execSQL(SQL_DELETE_FHDATE);
		onCreate(db);
	}

}
