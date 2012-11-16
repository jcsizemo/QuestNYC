package com.columbia.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class SignInQuery extends ServerQuery {
	
	private String email;
	private String password;
	private String nickname;
	private String query;
	public boolean success = false;
	public boolean isAdmin = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.email = intent.getStringExtra("email");
		this.password = intent.getStringExtra("password");
		this.interactionType = intent.getIntExtra("interactionType",0);
		this.nickname = intent.getStringExtra("nickname");
		this.execute();
	}

	@Override
	public HttpResponse get() {	// get user info, completed quest IDs
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
	public HttpResponse post() {
		HttpPost httpPost = new HttpPost(query);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			if (nickname != null) {
				nameValuePairs.add(new BasicNameValuePair("nn", nickname));
			}
	        nameValuePairs.add(new BasicNameValuePair("username", email));
	        nameValuePairs.add(new BasicNameValuePair("password", password));
	        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			return httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	public void run() {
		
		if (null != nickname) {
			query = address + "/signup/submit/";
		}
		else {
			query = address + "/login/?username=" + email + "&password=" + password;
		}
		
		HttpResponse response = null;
		if (ServerQuery.GET == interactionType) {
			response = get();
		}
		else if (ServerQuery.POST == interactionType) {
			response = post();
		}
		HttpEntity entity = response.getEntity();
		JSONObject jsonResponse = null;
		try {
			jsonResponse = new JSONObject(ServerHelper.getResponseBody(entity));
			success = jsonResponse.getBoolean("success");
			isAdmin = jsonResponse.getBoolean("admin");
			ServerHelper.sessionID = jsonResponse.getString("sessionID");
		} 
		catch (ParseException e) {
			
		} 
		catch (JSONException je) {
			
		}
		catch (IOException ioe) {
			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (IOException e) {
				}
			}
		}
		intent.putExtra("success", success);
		intent.putExtra("isAdmin",isAdmin);
		setResult(0,intent);
		finish();
	}

}
