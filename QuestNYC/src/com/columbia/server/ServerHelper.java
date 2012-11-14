package com.columbia.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.*;

import com.columbia.location.GPSHelper;
import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ServerHelper extends Activity implements Runnable {

	String latLong;
	Double lati;
	Double longi;
	String address = "54.243.134.140";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new Thread(this).start();
	}

	public String photoChecker() {
		StringBuilder sb = new StringBuilder("<Photo>\n");
		sb.append("<Location>" + latLong + "</Location>\n");
		sb.append("</Photo>");
		return sb.toString();
	}

	public HttpResponse getResponse(String message) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"http://54.243.134.140:7000/loadquest/?longitude=45&latitude=45");
		// HttpPost httpPost = new HttpPost("http://54.243.134.140/");
		try {
			// List<NameValuePair> nameValuePairs= new
			// ArrayList<NameValuePair>(2);
			// nameValuePairs.add(new BasicNameValuePair("latitude",
			// lati.toString()));
			// nameValuePairs.add(new BasicNameValuePair("longitude",
			// longi.toString()));
			return httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			return null;
		} catch (IOException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			return null;
		}
	}

	public void run() {
		GeoPoint location = GPSHelper.getLocation(this);

		lati = Double.valueOf(45);
		longi = Double.valueOf(45);
		latLong = "Latitude: " + lati + " Longitude: " + longi;
		String photoToServer = photoChecker();
		String sResponse;
		HttpResponse response = getResponse(photoToServer);
		HttpEntity entity = response.getEntity();
		try {
			sResponse = getResponseBody(entity);
			int stop = 1;
		} catch (ParseException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (IOException ioe) {
			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (IOException e) {
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}

	public static String getResponseBody(final HttpEntity entity) throws IOException,
			ParseException {
		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}
		InputStream instream = entity.getContent();
		if (instream == null) {
			return "";
		}
		if (entity.getContentLength() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
			"HTTP entity too large to be buffered in memory");
		}
		String charset = getContentCharSet(entity);
		if (charset == null) {
			charset = HTTP.DEFAULT_CONTENT_CHARSET;
		}
		Reader reader = new InputStreamReader(instream, charset);
		StringBuilder buffer = new StringBuilder();
		try {
			char[] tmp = new char[1024];
			int l;
			while ((l = reader.read(tmp)) != -1) {
				buffer.append(tmp, 0, l);
			}
		} finally {
			reader.close();
		}
		return buffer.toString();
	}

	private static String getContentCharSet(final HttpEntity entity)
			throws ParseException {
		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}
		String charset = null;
		if (entity.getContentType() != null) {
			HeaderElement values[] = entity.getContentType().getElements();
			if (values.length > 0) {
				NameValuePair param = values[0].getParameterByName("charset");
				if (param != null) {
					charset = param.getValue();
				}
			}
		}
		return charset;
	}

}
