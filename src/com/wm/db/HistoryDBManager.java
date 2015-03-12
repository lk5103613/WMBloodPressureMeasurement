package com.wm.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wm.db.DeviceDataContract.BPDataEntry;
import com.wm.db.DeviceDataContract.BSDataEntry;
import com.wm.db.DeviceDataContract.DeviceEntry;
import com.wm.db.DeviceDataContract.FHDataEntry;
import com.wm.entity.BPResult;
import com.wm.entity.BSResult;
import com.wm.entity.DeviceInfo;
import com.wm.entity.FHResult;

public class HistoryDBManager {

	private Context mContext;
	private DBHelper mDBHelper;
	private static HistoryDBManager mHistoryDBManager;
	private final String SEPARATOR = ",";

	private HistoryDBManager(Context context) {
		this.mContext = context;
		mDBHelper = new DBHelper(mContext);
	}
	
	public static HistoryDBManager getInstance(Context context) {
		if(mHistoryDBManager == null) {
			mHistoryDBManager = new HistoryDBManager(context);
		}
		return mHistoryDBManager;
	}

	public long addDevice(DeviceInfo deviceInfo) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DeviceEntry.COLUMN_NAME_NAME, deviceInfo.name);
		values.put(DeviceEntry.COLUMN_NAME_ADDRESS, deviceInfo.address);
		values.put(DeviceEntry.COLUMN_NAME_TYPE, deviceInfo.type);
		long newRowId;
		newRowId = db.insert(DeviceEntry.TABLE_NAME,
				DeviceEntry.COLUMN_NAME_NULLABLE, values);
		return newRowId;
	}
	
	public void updateDevice(DeviceInfo deviceInfo) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DeviceEntry.COLUMN_NAME_NAME, deviceInfo.name);
		values.put(DeviceEntry.COLUMN_NAME_ADDRESS, deviceInfo.address);
		values.put(DeviceEntry.COLUMN_NAME_TYPE, deviceInfo.type);
		String where = DeviceEntry.COLUMN_NAME_ID + " = ?";
		String[] whereArgs = new  String[]{String.valueOf(deviceInfo.id)};
		db.update(DeviceEntry.TABLE_NAME, values, where, whereArgs);
	}

	public DeviceInfo getDeviceByAddress(String address) {
		DeviceInfo deviceInfo = null;
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		String[] projection = { DeviceEntry.COLUMN_NAME_ID, DeviceEntry.COLUMN_NAME_NAME, DeviceEntry.COLUMN_NAME_ADDRESS, DeviceEntry.COLUMN_NAME_TYPE };
		String selection = DeviceEntry.COLUMN_NAME_ADDRESS + "=?";
		String selectionArgs[] = new String[]{address};
		Cursor c = db.query(
				DeviceEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null );
		if(c.moveToFirst()) {
			int id = c.getInt(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_ID));
			String name = c.getString(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_NAME));
			String type = c.getString(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_TYPE));
			deviceInfo = new DeviceInfo(id, type, name, address);
		}
		return deviceInfo;
	}
	
	public List<DeviceInfo> getAllDevices() {
		List<DeviceInfo> devices = new ArrayList<DeviceInfo>();
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		String[] projection = { DeviceEntry.COLUMN_NAME_ID, DeviceEntry.COLUMN_NAME_NAME, DeviceEntry.COLUMN_NAME_ADDRESS, DeviceEntry.COLUMN_NAME_TYPE };
		Cursor c = db.query(
				DeviceEntry.TABLE_NAME, projection, null, null, null, null, null);
		while(c.moveToNext()) {
			int id = c.getInt(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_ID));
			String name = c.getString(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_NAME));
			String type = c.getString(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_TYPE));
			String address = c.getString(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_ADDRESS));
			devices.add(new DeviceInfo(id, type, name, address));
		}
		return devices;
	}
	
	public List<DeviceInfo> getDeviceByType(String type) {
		List<DeviceInfo> devices = new ArrayList<DeviceInfo>();
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		String[] projection = { DeviceEntry.COLUMN_NAME_ID, DeviceEntry.COLUMN_NAME_NAME, DeviceEntry.COLUMN_NAME_ADDRESS, DeviceEntry.COLUMN_NAME_TYPE };
		String where = DeviceEntry.COLUMN_NAME_TYPE + " = ?";
		String[] whereArgs = new String[]{type};
		Cursor c = db.query(DeviceEntry.TABLE_NAME, projection, where, whereArgs, null, null, null);
		while(c.moveToNext()) {
			int id = c.getInt(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_ID));
			String name = c.getString(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_NAME));
			String address = c.getString(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_ADDRESS));
			devices.add(new DeviceInfo(id, type, name, address));
		}
		return devices;
	}
	
	public void removeDeviceById(int id) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		String selection = DeviceEntry.COLUMN_NAME_ID + " = ?";
		String[] selectionArgs = { String.valueOf(id) };
		db.delete(DeviceEntry.TABLE_NAME, selection, selectionArgs);
	}
	
	public void removeDeviceByAddress(String address) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		String selection = DeviceEntry.COLUMN_NAME_ADDRESS + " = ?";
		String[] selectionArgs = {address};
		db.delete(DeviceEntry.TABLE_NAME, selection, selectionArgs);
		db.close();
	}
	
	/**
	 * ������е�Ѫѹ������
	 * @return
	 */
	public List<BPResult> getAllBpResults(){
		List<BPResult> bpResults = new ArrayList<>();
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		String[] projection = { BPDataEntry.COLUMN_NAME_ID, BPDataEntry.COLUMN_NAME_SZVALUE, 
				BPDataEntry.COLUMN_NAME_SSVALUE, BPDataEntry.COLUMN_NAME_DATE};
		Cursor c = db.query(
				BPDataEntry.TABLE_NAME, projection, null, null, null, null, null );
		
		while(c.moveToNext()) {
			int id = c.getInt(c.getColumnIndexOrThrow(BPDataEntry.COLUMN_NAME_ID));
			float szValue = c.getFloat(c.getColumnIndexOrThrow(BPDataEntry.COLUMN_NAME_SZVALUE));
			float ssValue = c.getFloat(c.getColumnIndexOrThrow(BPDataEntry.COLUMN_NAME_SSVALUE));
			long date = c.getLong(c.getColumnIndexOrThrow(BPDataEntry.COLUMN_NAME_DATE)); 
			bpResults.add(new BPResult(id, szValue, ssValue, date));
		}
		return bpResults;
	}
	
	/**
	 * ���Ѫѹ����ʷ
	 * 
	 * @param bpResult
	 * @return
	 */
	public long addBpResult(BPResult bpResult){
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BPDataEntry.COLUMN_NAME_SZVALUE, bpResult.szValue);
		values.put(BPDataEntry.COLUMN_NAME_SZVALUE, bpResult.ssValue);
		values.put(BPDataEntry.COLUMN_NAME_HEART_RATE, bpResult.heartRate);
		values.put(BPDataEntry.COLUMN_NAME_HEART_RATE_STATE, bpResult.heartRateState);
		values.put(BPDataEntry.COLUMN_NAME_DATE, bpResult.date);
		values.put(BPDataEntry.COLUMN_NAME_STATUS, bpResult.status);
		long newRowId = db.insert(BPDataEntry.TABLE_NAME,
				BPDataEntry.COLUMN_NAME_NULLABLE, values);
		return newRowId;
	}
	
	/**
	 * �������Ѫ������
	 * 
	 * @return
	 */
	public List<BSResult> getAllBsResults(){
		List<BSResult> bsResults = new ArrayList<>();
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		String[] projection = {BSDataEntry.COLUMN_NAME_ID, BSDataEntry.COLUMN_NAME_BSVALUE, 
				BSDataEntry.COLUMN_NAME_DATE};
		Cursor c = db.query(BSDataEntry.TABLE_NAME, projection, null, null, null, null, null);
		while (c.moveToNext()) {
			int id = c.getInt(c.getColumnIndexOrThrow(BSDataEntry.COLUMN_NAME_ID));
			int bsValue = c.getInt(c.getColumnIndexOrThrow(BSDataEntry.COLUMN_NAME_BSVALUE));
			long date = c.getLong(c.getColumnIndexOrThrow(BSDataEntry.COLUMN_NAME_DATE));
			bsResults.add(new BSResult(id, bsValue, date));
		}
		return bsResults;
	}
	
	/**
	 * ���Ѫ����ʷ
	 * 
	 * @param bsResult
	 * @return
	 */
	public long addBsResult(BSResult bsResult){
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BSDataEntry.COLUMN_NAME_BSVALUE, bsResult.bsValue);
		values.put(BSDataEntry.COLUMN_NAME_STATUS, bsResult.status);
		values.put(BSDataEntry.COLUMN_NAME_DATE, bsResult.date);
		long newRowId = db.insert(BSDataEntry.TABLE_NAME, BSDataEntry.COLUMN_NAME_NULLABLE, values);
		return newRowId;
		
	}
	
	/**
	 * �������̥��ֵ
	 * 
	 * @return
	 */
	public List<FHResult> getAllFhResults(){
		List<FHResult> fhResults = new ArrayList<>();
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		String[] projection = {FHDataEntry.COLUMN_NAME_ID, FHDataEntry.COLUMN_NAME_FHVALUES, 
				FHDataEntry.COLUMN_NAME_DATE};
		Cursor c = db.query(FHDataEntry.TABLE_NAME, projection, null, null, null, null, null);
		while (c.moveToNext()) {
			int id = c.getInt(c.getColumnIndexOrThrow(FHDataEntry.COLUMN_NAME_ID));
			String fhValues = c.getString(c.getColumnIndexOrThrow(FHDataEntry.COLUMN_NAME_FHVALUES));
			long date = c.getLong(c.getColumnIndexOrThrow(FHDataEntry.COLUMN_NAME_DATE));
			List<Float> fhList = splitFhValues(fhValues);
			fhResults.add(new FHResult(id,fhList, date));
		}
		return fhResults;
	}
	
	
	/**
	 * ���̥����ʷ
	 * 
	 * @param fhResult
	 * @return
	 */
	public long addFhResult(FHResult fhResult) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FHDataEntry.COLUMN_NAME_FHVALUES, listToStr(fhResult.fhValues, SEPARATOR));
		values.put(FHDataEntry.COLUMN_NAME_STATUS, fhResult.status);
		values.put(FHDataEntry.COLUMN_NAME_DATE, fhResult.date);
		long newRowId = db.insert(FHDataEntry.TABLE_NAME, FHDataEntry.COLUMN_NAME_NULLABLE, values);
		return newRowId;
	}
	/**
	 * ̥���ַ����ŷ�Ϊlist
	 * 
	 * @param valueStr
	 * @return List
	 */
	private List<Float> splitFhValues(String valueStr){
		List<Float> fhList = new ArrayList<>();
		String[] fhArray = valueStr.split(SEPARATOR);
		for (int i = 0; i < fhArray.length; i++) {
			fhList.add(Float.parseFloat(fhArray[i]));
		}
		return fhList;
	}
	
	
	/**
	 * ��list ���ָ���ƴ�ӳ��ַ����
	 * @param list
	 * @param separator
	 * @return String 
	 */
	private <T> String listToStr(List<T> list, String separator) {
		StringBuffer sb = new StringBuffer();  
		for (int i = 0, size = list.size(); i < size; i++) {
			sb.append(list.get(i));
			sb.append(separator);
		}
		
		return sb.toString();
		
	}
	
}
