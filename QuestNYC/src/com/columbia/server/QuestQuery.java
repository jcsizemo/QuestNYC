package com.columbia.server;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class QuestQuery extends ServerQuery {
	
	int latitude;
	int longitude;
	String query;
	int interactionType;
	
	@TargetApi(12)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		latitude = intent.getIntExtra("latitude", 0);
		longitude = intent.getIntExtra("longitude", 0);
		interactionType = intent.getIntExtra("interactionType",0);
		this.execute();
	}

	public void run() {
		
		query = address + "/loadquest/?latitude=" + 40 + "&longitude=" + -73;
		
		DefaultHttpClient dHttpClient = new DefaultHttpClient();
		BasicClientCookie cookie = new BasicClientCookie("sessionID",ServerHelper.sessionID);
		cookie.setVersion(0);
		cookie.setDomain(address);
		cookie.setPath("/");
		
		dHttpClient.setCookieStore(new BasicCookieStore());
		dHttpClient.getCookieStore().addCookie(cookie);
		
		HttpResponse response = null;
		if (ServerQuery.GET == interactionType) {
			response = get();
		}
		else if (ServerQuery.POST == interactionType) {
			response = post();
		}
		HttpEntity entity = response.getEntity();
		String sResponse;
		try {
			sResponse = ServerHelper.getResponseBody(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int stop = 1;
	}

	@Override
	public HttpResponse get() {		// get quests
		HttpGet httpGet = new HttpGet(query);
		try {
			return httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public HttpResponse post() {	// creating a quest, posting to server
		return null;
	}

}
