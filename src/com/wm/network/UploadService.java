package com.wm.network;

import retrofit.http.Body;
import retrofit.http.POST;

import com.wm.entity.BPResult;
import com.wm.entity.BSResult;
import com.wm.entity.FHResult;
import com.wm.entity.ResponseData;
import com.wm.entity.UploadEntity;


public interface UploadService {
	
	@POST("/upload/bloodPressure/2J.do")
	ResponseData uploadBloodpressure(@Body UploadEntity<BPResult> bpResults);
	
	@POST("/upload/fetalHeart/2J.do")
	ResponseData uploadFetalHeart(@Body UploadEntity<FHResult> fhResults);
	
	@POST("/upload/bloodGlucose/2J.do")
	ResponseData uploadBloodGlucose(@Body UploadEntity<BSResult> bsResults);
	
}
