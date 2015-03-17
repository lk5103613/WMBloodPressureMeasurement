package com.wm.blecore;

public interface IHandleConnect {

	void handleConnect();
	
	void handleDisconnect();
	
	void handleGetData(String data);
	
	void handleServiceDiscover();
	
}
