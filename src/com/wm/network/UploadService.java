package com.wm.network;

import retrofit.http.Body;
import retrofit.http.POST;

import com.wm.entity.BPResult;
import com.wm.entity.BSResult;
import com.wm.entity.FHResult;
import com.wm.entity.Response;
import com.wm.entity.RequestEntity;

public interface UploadService {

	@POST("/upload/bloodPressure/2J.do")
	Response uploadBloodpressure(@Body RequestEntity<BPResult> bpResults);

	@POST("/upload/fetalHeart/2J.do")
	Response uploadFetalHeart(@Body RequestEntity<FHResult> fhResults);

	@POST("/upload/bloodGlucose/2J.do")
	Response uploadBloodGlucose(@Body RequestEntity<BSResult> bsResults);

}
