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
	
	public static abstract class BPDataEntry implements BaseColumns {
		public static final String TABLE_NAME = "bp_data";
		public static final String COLUMN_NAME_ID = "id";
		public static final String COLUMN_NAME_SZVALUE = "sz_value";
		public static final String COLUMN_NAME_SSVALUE = "ss_value";
		public static final String COLUMN_NAME_HEART_RATE = "heart_rate";
		public static final String COLUMN_NAME_HEART_RATE_STATE = "heart_rate_state";
		public static final String COLUMN_NAME_DATE = "date";
		public static final String COLUMN_NAME_NULLABLE = "null";
		public static final String COLUMN_NAME_STATUS = "data_status";
	}
	
	public static abstract class BSDataEntry implements BaseColumns {
		public static final String TABLE_NAME = "bs_data";
		public static final String COLUMN_NAME_ID = "id";
		public static final String COLUMN_NAME_BSVALUE = "bs_value";
		public static final String COLUMN_NAME_DATE = "date";
		public static final String COLUMN_NAME_NULLABLE = "null";
		public static final String COLUMN_NAME_STATUS = "status";
	}
	
	public static abstract class FHDataEntry implements BaseColumns {
		public static final String TABLE_NAME = "fh_data";
		public static final String COLUMN_NAME_ID = "id";
		public static final String COLUMN_NAME_FHVALUES = "fh_values";
		public static final String COLUMN_NAME_DATE = "date";
		public static final String COLUMN_NAME_NULLABLE = "null";
		public static final String COLUMN_NAME_STATUS = "status";
	}
	
}
