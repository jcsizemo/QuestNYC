package com.columbia.server;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.content.Intent;
import android.os.Bundle;

public class RatingQuery extends ServerQuery {
	
	Intent intent;
	String query;
	int interactionType;
	int questId;
	int rating;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		questId = intent.getIntExtra("questId", 0);
		interactionType = intent.getIntExtra("interactionType",0);
		rating = intent.getIntExtra("rating", 0);
		this.execute();
	}
	
	public void run() {
		query = address + "/ratequest/?questid=" + questId + "&rate=" + rating + "&username=" + ServerHelper.email + "&password=" + ServerHelper.password;
		
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

		
		intent.putExtra("xml", sResponse);
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
	public HttpResponse post() { // check answer
		return null;
	}

}
