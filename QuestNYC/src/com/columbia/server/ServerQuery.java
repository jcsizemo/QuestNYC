package com.columbia.server;

import java.net.CookieStore;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public abstract class ServerQuery extends Activity implements Runnable {
	
	public static final String address = "http://54.243.134.140";
	public static final int GET = 0;
	public static final int POST = 1;
	public HttpClient httpClient = new DefaultHttpClient();
	public int interactionType;
	public Intent intent;
	
	@TargetApi(12)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
	}
	
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
