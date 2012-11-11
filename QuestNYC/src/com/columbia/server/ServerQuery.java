package com.columbia.server;

public abstract class ServerQuery implements Runnable {
	
	public static final String address = "54.234.134.140";
	
	public abstract void get();
	
	public abstract void post();

}
