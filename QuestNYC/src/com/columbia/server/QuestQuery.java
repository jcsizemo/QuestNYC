package com.columbia.server;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

public class QuestQuery extends ServerQuery {
	
	int latitude;
	int longitude;
	String query;
	int interactionType;
	
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
		
		query = address + ":7000/loadquest/?latitude=" + 40 + "&longtide=" + -73;
		
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
