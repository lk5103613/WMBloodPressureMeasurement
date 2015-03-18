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

	/**
	 * 获得所有的血压计数据
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
	
	public List<BPResult> getBpResultsByStatus(int status){
		
		List<BPResult> bpResults = new ArrayList<>();
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		String[] projection = { BPDataEntry.COLUMN_NAME_ID, BPDataEntry.COLUMN_NAME_SZVALUE, 
				BPDataEntry.COLUMN_NAME_SSVALUE, BPDataEntry.COLUMN_NAME_DATE};
		String selection = BPDataEntry.COLUMN_NAME_STATUS + "=?";
		String selectionArgs[] = new String[]{String.valueOf(status)};
		Cursor c = db.query(
				BPDataEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null );
		
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
	 * 删除已上传的数据，只留最近300条
	 * 
	 */
	public void deleteBpDatas(){
		
		List<BPResult> results = getBpResultsByStatus(1);
		if(results.size()<=300) {
			return;
		}
		
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		StringBuilder selection = new StringBuilder();
		List<String> argList = new ArrayList<>();
		selection.append(BPDataEntry.COLUMN_NAME_ID + " in (");
		for (int i = results.size(); i >300; i-- ) {
			selection.append("?,");
			argList.add(results.get(i).id+"");
		}
		selection.deleteCharAt(selection.length()-1);//去掉最后一个逗号
		selection.append(")");
		
		String[] whereArgs = (String[]) argList.toArray(new String[argList.size()]);
		
		db.delete(BPDataEntry.TABLE_NAME, selection.toString(), whereArgs);
		
	}
	
	
	
	/**
	 * 标记血压记录已上传
	 * 
	 * @param bpResults
	 * @return
	 */
	public boolean changeBpStatus(List<BPResult> bpResults){
		if(bpResults.isEmpty()) {
			return true;
		}
		
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BPDataEntry.COLUMN_NAME_STATUS, 1);
		
		List<String> argsList = new ArrayList<>();
		StringBuilder sb = new StringBuilder(BPDataEntry.COLUMN_NAME_ID);
		sb.append(" in (");
		for (int i = 0, size = bpResults.size(); i < size; i++) {
			sb.append("?,");
			argsList.add(String.valueOf(bpResults.get(i).id));
		}
		sb.deleteCharAt(sb.length()-1);//去掉最后一个逗号
		sb.append(")");
		
		String where = sb.toString();
		String[] whereArgs = (String[]) argsList.toArray(new String[bpResults.size()]);
		return db.update(DeviceEntry.TABLE_NAME, values, where, whereArgs) > 0;
	}
	
	/**
	 * 添加血压计历史
	 * 
	 * @param bpResult
	 * @return
	 */
	public long addBpResult(BPResult bpResult){
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BPDataEntry.COLUMN_NAME_SZVALUE, bpResult.sbp);
		values.put(BPDataEntry.COLUMN_NAME_SSVALUE, bpResult.sbp);
		values.put(BPDataEntry.COLUMN_NAME_HEART_RATE, bpResult.pulse);
		values.put(BPDataEntry.COLUMN_NAME_HEART_RATE_STATE, bpResult.heartRateState);
		values.put(BPDataEntry.COLUMN_NAME_DATE, bpResult.date);
		values.put(BPDataEntry.COLUMN_NAME_STATUS, bpResult.status);
		long newRowId = db.insert(BPDataEntry.TABLE_NAME,
				BPDataEntry.COLUMN_NAME_NULLABLE, values);
		return newRowId;
	}
	
	/**
	 * 获得所有血糖数据
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
	
	public List<BSResult> getBsResultsByStatus(int status){
		List<BSResult> bsResults = new ArrayList<>();
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		String[] projection = {BSDataEntry.COLUMN_NAME_ID, BSDataEntry.COLUMN_NAME_BSVALUE, 
				BSDataEntry.COLUMN_NAME_DATE};
		String selection = BSDataEntry.COLUMN_NAME_STATUS + "=?";
		String[] args = {String.valueOf(status)};
		Cursor c = db.query(BSDataEntry.TABLE_NAME, projection, selection, args, null, null, null);
		while (c.moveToNext()) {
			int id = c.getInt(c.getColumnIndexOrThrow(BSDataEntry.COLUMN_NAME_ID));
			int bsValue = c.getInt(c.getColumnIndexOrThrow(BSDataEntry.COLUMN_NAME_BSVALUE));
			long date = c.getLong(c.getColumnIndexOrThrow(BSDataEntry.COLUMN_NAME_DATE));
			bsResults.add(new BSResult(id, bsValue, date));
		}
		return bsResults;
	}
	
	/**
	 * 添加血糖历史
	 * 
	 * @param bsResult
	 * @return
	 */
	public long addBsResult(BSResult bsResult){
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BSDataEntry.COLUMN_NAME_BSVALUE, bsResult.bg);
		values.put(BSDataEntry.COLUMN_NAME_STATUS, bsResult.status);
		values.put(BSDataEntry.COLUMN_NAME_DATE, bsResult.date);
		long newRowId = db.insert(BSDataEntry.TABLE_NAME, BSDataEntry.COLUMN_NAME_NULLABLE, values);
		return newRowId;
		
	}
	
	/**
	 * 获得所有胎心值
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
	
	public List<FHResult> getFhResultsByStatus(int status){
		List<FHResult> fhResults = new ArrayList<>();
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		String[] projection = {FHDataEntry.COLUMN_NAME_ID, FHDataEntry.COLUMN_NAME_FHVALUES, 
				FHDataEntry.COLUMN_NAME_DATE};
		String selections = FHDataEntry.COLUMN_NAME_STATUS + "=?";
		String[] args = {String.valueOf(status)};
		Cursor c = db.query(FHDataEntry.TABLE_NAME, projection, selections, args, null, null, null);
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
	 * 添加胎心历史
	 * 
	 * @param fhResult
	 * @return
	 */
	public long addFhResult(FHResult fhResult) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FHDataEntry.COLUMN_NAME_FHVALUES, listToStr(fhResult.fhValues, SEPARATOR));
		values.put(FHDataEntry.COLUMN_NAME_STATUS, fhResult.status);
		values.put(FHDataEntry.COLUMN_NAME_DATE, fhResult.measureTime);
		long newRowId = db.insert(FHDataEntry.TABLE_NAME, FHDataEntry.COLUMN_NAME_NULLABLE, values);
		return newRowId;
	}
	
	/**
	 * 胎心字符串才分为list
	 * 
	 * @param valueStr
	 * @return List
	 */
	private List<Float> splitFhValues(String valueStr){
		List<Float> fhList = new ArrayList<>();
		if (!"".equals(fhList)){
			String[] fhArray = valueStr.split(SEPARATOR);
			for (int i = 0; i < fhArray.length; i++) {
				fhList.add(Float.parseFloat(fhArray[i]));
			}
		}
		
		return fhList;
	}
	
	
	/**
	 * 将list 安分隔符拼接成字符后窜
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
