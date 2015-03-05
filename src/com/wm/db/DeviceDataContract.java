package com.wm.db;

import android.provider.BaseColumns;

public class DeviceDataContract {
	
	public DeviceDataContract(){}
	
	public static abstract class DeviceEntry implements BaseColumns {
		public static final String TABLE_NAME = "device";
		public static final String COLUMN_NAME_ID = "device_id";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_ADDRESS = "address";
		public static final String COLUMN_NAME_TYPE = "type";
		public static final String COLUMN_NAME_NULLABLE = "null";
	}
	
	public static abstract class DataEntry implements BaseColumns {
		public static final String TABLE_NAME = "data";
		public static final String COLUMN_NAME_ID = "data_id";
		public static final String COLUMN_NAME_NULLABLE = "null";
	}
	
	
}
