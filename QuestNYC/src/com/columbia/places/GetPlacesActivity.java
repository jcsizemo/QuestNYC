package com.columbia.places;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;

import com.columbia.quest.create.CreateQuestActivity;

/**
 * Background Async Task to Load Google places
 * */
public class GetPlacesActivity extends Activity implements Runnable{
	
	PlacesList nearPlaces;
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();
	ProgressDialog pDialog;
	Context mContext;
	AlertDialogManager alert = new AlertDialogManager();
	Intent returnIntent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		returnIntent = getIntent();
        new Thread(this).start();
	}
	
	public void run() {
		GooglePlaces googlePlaces = new GooglePlaces();
		GPSTracker gps = new GPSTracker(this);
		try {
			String types = null;
			double radius = 1000;
			nearPlaces = googlePlaces.search(gps.getLatitude(),
                    gps.getLongitude(), radius, types);
			pDialog.dismiss();
			String status = nearPlaces.status;
			// Check for all possible status
            if(status.equals("OK")){
                // Successfully got places details
                if (nearPlaces.results != null) {
                    // loop through each place
                    for (Place p : nearPlaces.results) {
                        HashMap<String, String> map = new HashMap<String, String>();

                        // Place reference won't display in listview - it will be hidden
                        // Place reference is used to get "place full details"
                        map.put(CreateQuestActivity.KEY_REFERENCE, p.reference);

                        // Place name
                        map.put(CreateQuestActivity.KEY_NAME, p.name);

                        // adding HashMap to ArrayList
                        placesListItems.add(map);
                    }
                }
                returnIntent.putExtra("nearPlaces", nearPlaces);
                returnIntent.putExtra("placesListItems",placesListItems);
                setResult(0,returnIntent);
            }
            else if(status.equals("ZERO_RESULTS")){
                returnIntent.putExtra("errorType", "Near Places");
                returnIntent.putExtra("errorMsg", "No places nearby found. Make the quest elsewhere or add your own questions.");
                setResult(1,returnIntent);
            }
            else if(status.equals("UNKNOWN_ERROR"))
            {
                returnIntent.putExtra("errorType", "Places Error");
                returnIntent.putExtra("errorMsg", "Unknown error occurred for place search.");
                setResult(1,returnIntent);
            }
            else if(status.equals("OVER_QUERY_LIMIT"))
            {
                returnIntent.putExtra("errorType", "Places Error");
                returnIntent.putExtra("errorMsg", "Query limit for places reached.");
                setResult(1,returnIntent);
            }
            else if(status.equals("REQUEST_DENIED"))
            {
                returnIntent.putExtra("errorType", "Places Error");
                returnIntent.putExtra("errorMsg", "Request denied for place search.");
                setResult(1,returnIntent);
            }
            else if(status.equals("INVALID_REQUEST"))
            {
                returnIntent.putExtra("errorType", "Places Error");
                returnIntent.putExtra("errorMsg", "Invalid request in place search.");
                setResult(1,returnIntent);
            }
            else
            {
                returnIntent.putExtra("errorType", "Places Error");
                returnIntent.putExtra("errorMsg", "Unknown error");
                setResult(1,returnIntent);
            }
            finish();
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		
	}
}