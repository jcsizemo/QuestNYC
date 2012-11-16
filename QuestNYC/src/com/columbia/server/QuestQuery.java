package com.columbia.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

import com.columbia.quest.Quest;

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
	Intent intent;
	
	@TargetApi(12)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		latitude = intent.getIntExtra("latitude", 0);
		longitude = intent.getIntExtra("longitude", 0);
		interactionType = intent.getIntExtra("interactionType",0);
		this.execute();
	}

	public void run() {
		query = address + "/loadquest/?latitude=" + latitude + "&longitude=" + longitude + "&username=" + ServerHelper.email + "&password=" + ServerHelper.password;
		
		HttpResponse response = null;
		if (ServerQuery.GET == interactionType) {
			response = get();
		}
		else if (ServerQuery.POST == interactionType) {
			response = post();
		}
		HttpEntity entity = response.getEntity();
		String sResponse = null;
		try {
			sResponse = ServerHelper.getResponseBody(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		intent.putExtra("quests", sResponse);
    	setResult(0,intent);
    	finish();
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
