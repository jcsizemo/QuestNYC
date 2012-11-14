package com.columbia.server;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class ServerQuery implements Runnable {
	
	public static final String address = "http://54.243.134.140";
	public static final int GET = 0;
	public static final int POST = 1;
	public HttpClient httpClient = new DefaultHttpClient();
	public int interactionType;
	
	public HttpResponse get() {
		return null;
	}
	
	public HttpResponse post() {
		return null;
	}
	
	public void execute() {
		new Thread(this).start();
	}

}
