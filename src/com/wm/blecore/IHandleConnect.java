package com.wm.blecore;

public interface IHandleConnect {

	boolean handleConnect();

	boolean handleDisconnect();

	boolean handleGetData(String data);

	boolean handleServiceDiscover();

}
