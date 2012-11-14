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

public class SignInQuery extends ServerQuery {
	
	private String email;
	private String password;
	private String nickname;
	private String query;
	
	public SignInQuery(String email, String password, int interactionType, String nickname) {
		this.email = email;
		this.password = password;
		this.interactionType = interactionType;
		this.nickname = nickname;
	}

	@Override
	public HttpResponse get() {	// get user info, completed quest IDs
		return null;
	}

	@Override
	public HttpResponse post() {
		HttpPost httpPost = new HttpPost(query);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			if (nickname != null) {
				nameValuePairs.add(new BasicNameValuePair("nn", nickname));
			}
	        nameValuePairs.add(new BasicNameValuePair("id", email));
	        nameValuePairs.add(new BasicNameValuePair("pw", password));
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
			query = address + "/signup/?nn=" + nickname + "&username=" + email + "&password=" + password;
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
		String sResponse = null;
		try {
			sResponse = ServerHelper.getResponseBody(entity);
		} catch (ParseException e) {
		} catch (IOException ioe) {
			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (IOException e) {
				}
			}
		}
	}

}
