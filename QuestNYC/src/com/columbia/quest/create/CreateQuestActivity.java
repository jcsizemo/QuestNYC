package com.columbia.quest.create;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.columbia.location.BoundaryPoint;
import com.columbia.location.CenterPoint;
import com.columbia.places.AddItemizedOverlay;
import com.columbia.places.AlertDialogManager;
import com.columbia.places.ConnectionDetector;
import com.columbia.places.GPSTracker;
import com.columbia.places.GetPlacesActivity;
import com.columbia.places.GooglePlaces;
import com.columbia.places.Place;
import com.columbia.places.PlacesList;
import com.columbia.quest.Quest;
import com.columbia.quest.QuestActivity;
import com.columbia.questnyc.R;
import com.columbia.server.CreateQuestQuery;
import com.columbia.server.CreateQuestionQuery;
import com.columbia.server.ServerQuery;
import com.columbia.server.XMLHelper;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateQuestActivity extends MapActivity implements OnTouchListener {
	
	MapView mapView;
	MapController mc;
	GeoPoint geoPoint;
	Projection projection;
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
	// Google Places
	GooglePlaces googlePlaces;
	// Places List
	PlacesList nearPlaces;
	// GPS Location
	GPSTracker gps;
	// Progress dialog
	ProgressDialog pDialog;
	// ListItems data
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();
    // Map overlay items
    List<Overlay> mapOverlays;
    AddItemizedOverlay itemizedOverlay;
    OverlayItem overlayitem;
    double latitude;
    double longitude;
    Drawable dCenter;
    Drawable dUser;
    Drawable dBoundary;
    int centerLat;
    int centerLong;
    
    TextView instructions;
    Button placeButton;
    Button startOverButton;
    TextView questionLabel;
    EditText questionField;
    TextView answerLabel;
    EditText answerField;
    TextView numberOfQuestionsLabel;
    Button addButton;
    Button submitButton;
    
    Quest quest = new Quest();
    List<OverlayItem> questPoints = new ArrayList<OverlayItem>();
	
	@SuppressWarnings("deprecation")
	@TargetApi(13)
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_quest_layout);
        
        placeButton = (Button) findViewById(R.id.placeButton);
        instructions = (TextView) findViewById(R.id.instructionsLabel);
        startOverButton = (Button) findViewById(R.id.startOverButton);
        questionLabel = (TextView) findViewById(R.id.questionLabel);
        questionField = (EditText) findViewById(R.id.questionField);
        answerLabel = (TextView) findViewById(R.id.answerLabel);
        answerField = (EditText) findViewById(R.id.answerField);
        numberOfQuestionsLabel = (TextView) findViewById(R.id.numberOfQuestionsLabel);
        addButton = (Button) findViewById(R.id.addButton);
        submitButton = (Button) findViewById(R.id.submitButton);
        
        
        questionLabel.setVisibility(View.INVISIBLE);
        questionField.setVisibility(View.INVISIBLE);
        answerLabel.setVisibility(View.INVISIBLE);
        answerField.setVisibility(View.INVISIBLE);
        numberOfQuestionsLabel.setVisibility(View.INVISIBLE);
        addButton.setVisibility(View.INVISIBLE);
        submitButton.setVisibility(View.INVISIBLE);
        
        cd = new ConnectionDetector(getApplicationContext());
        
        dUser = this.getResources().getDrawable(R.drawable.user);
        dCenter = this.getResources().getDrawable(R.drawable.center);
        dBoundary = this.getResources().getDrawable(R.drawable.boundary);

		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			// Internet Connection is not present
			alert.showAlertDialog(CreateQuestActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// creating GPS Class object
		gps = new GPSTracker(this);
		latitude = gps.getLatitude();
		longitude = gps.getLongitude();

		// check if GPS location can get
		if (gps.canGetLocation()) {
			Log.d("Your Location", "latitude:" + gps.getLatitude()
					+ ", longitude: " + gps.getLongitude());
		} else {
			// Can't get user's current location
			alert.showAlertDialog(CreateQuestActivity.this, "GPS Status",
					"Couldn't get location information. Please enable GPS",
					false);
			// stop executing code by return
			return;
		}
 
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setOnTouchListener(this);
 
        mapOverlays = mapView.getOverlays();
 
        // Geopoint to user on map
        geoPoint = new GeoPoint((int) (latitude * 1E6),
                (int) (longitude * 1E6));
 
        // Drawable marker icon
        Drawable drawable_user = this.getResources()
                .getDrawable(R.drawable.user);
 
        itemizedOverlay = new AddItemizedOverlay(drawable_user, this);
 
        // Map overlay item
        overlayitem = new OverlayItem(geoPoint, "Your Location",
                "That is you!");
 
        itemizedOverlay.addOverlay(overlayitem);
        itemizedOverlay.populateNow();
 
        // Drawable marker icon
        Drawable drawable = this.getResources()
                .getDrawable(R.drawable.center);
 
        itemizedOverlay = new AddItemizedOverlay(drawable, this);
 
        mc = mapView.getController();
        mc.animateTo(geoPoint);
        mc.setZoom(15);
        
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);	//get display height dimension
        Display display = wm.getDefaultDisplay();
        int currentApiVersion = android.os.Build.VERSION.SDK_INT; 
        int height = 0;
        if (currentApiVersion >= 13) {	// different ways of getting height depending on Android version
        	Point size = new Point();
            display.getSize(size);
            height = size.y;
        }
        else {
        	height = display.getHeight();
        }
        ViewGroup.LayoutParams params = mapView.getLayoutParams();
        params.height = height/2;		// set map view to be 1/3 of the screen height
        mapView.setLayoutParams(params);
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public void updateCenter(MapView mapView) {
        mapOverlays = mapView.getOverlays();
        mapOverlays.clear();
        // Geopoint to place on map
        geoPoint = mapView.getMapCenter();
 
        // Drawable marker icon
        Drawable drawable = dBoundary;
 
        itemizedOverlay = new AddItemizedOverlay(drawable, this);
 
        // Map overlay item
        overlayitem = new OverlayItem(geoPoint, "Point",
                "This is one of your points.");
 
        itemizedOverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedOverlay);
        for (OverlayItem oi : questPoints) {
        	itemizedOverlay.addOverlay(oi);
        	mapOverlays.add(itemizedOverlay);
        }
        itemizedOverlay.populateNow();
	}
	
	public boolean onTouch(View arg0, MotionEvent arg1) {
		if (questPoints.size() < 5) {
			updateCenter(mapView);
		}
		return false;
	}
	
	public void onClick(View v) {
		if (v.getId() == R.id.placeButton) {
			if (placeButton.getText().equals("Save Boundaries")) {
				Intent getPlacesIntent = new Intent(this,GetPlacesActivity.class);
				getPlacesIntent.putExtra("centerLat",centerLat);
				getPlacesIntent.putExtra("centerLong",centerLong);
				startActivityForResult(getPlacesIntent,3);
			}
			else {
				if (questPoints.size() < 5) {
			        GeoPoint gp = mapView.getMapCenter();
			        OverlayItem oi = null;
			        if (questPoints.size() == 0) {
			        	gp = new CenterPoint(gp.getLatitudeE6(),gp.getLongitudeE6());
			        	centerLat = gp.getLatitudeE6();
			        	centerLong = gp.getLongitudeE6();
			        	oi = new OverlayItem(gp, "Center", "This is the center.");
			        }
			        else {
			        	oi = new OverlayItem(gp, "Boundary", "This is a boundary");
			        }
			        questPoints.add(oi);
				}
				if (questPoints.size() == 5) {
					placeButton.setText("Save Boundaries");
				}
			}
		}
		else if (v.getId() == R.id.startOverButton) {
			placeButton.setText("Place");
			mapOverlays.clear();
			questPoints.clear();
			mapView.invalidate();
		}
		else if (v.getId() == R.id.addButton) {
			String question = questionField.getText().toString();
			String answer = answerField.getText().toString();
			if ("".equals(question) || "".equals(answer)) {
				Toast.makeText(this, "Question or answer blank", Toast.LENGTH_SHORT).show();
			}
			quest.addQuestion(question, answer);
			numberOfQuestionsLabel.setText(quest.getQuestions().size() + " questions");
		}
		else if (v.getId() == R.id.submitButton) {
			Intent intent = new Intent(this, NameActivity.class);
			startActivityForResult(intent,4);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 3 && resultCode == 0) {
			nearPlaces = (PlacesList) data.getSerializableExtra("nearPlaces");
			placesListItems = (ArrayList<HashMap<String, String>>) data.getSerializableExtra("placesListItems");
			populateMap(nearPlaces, placesListItems);
		}
//		if (requestCode == 3 && resultCode == 1) {
//		}
		if (requestCode == 4 && resultCode == 0) {
			String name = data.getStringExtra("name");
			String description = data.getStringExtra("description");
			List<GeoPoint> boundaries = quest.getBoundaries();
			CenterPoint c = quest.getCenter();
			Intent initIntent = new Intent(this,CreateQuestQuery.class);
			initIntent.putExtra("name", name);
			initIntent.putExtra("description", description);
			initIntent.putExtra("interactionType", ServerQuery.GET);
			initIntent = Quest.addQuestPoints(quest, initIntent);
			int cLat = initIntent.getIntExtra("centerLat", 0);
			int cLong = initIntent.getIntExtra("centerLong", 0);
			startActivityForResult(initIntent,5);
		}
		if (requestCode == 5 && resultCode == 0) {
			String xml = data.getStringExtra("xml");
			XMLHelper xH = new XMLHelper();
			String questId = xH.XMLgetQuestId(xml);
			Map<String,String> questions = quest.getQuestions();
			for (String question : questions.keySet()) {
				Intent qIntent = new Intent(this,CreateQuestionQuery.class);
				qIntent.putExtra("questId", questId);
				qIntent.putExtra("question",question);
				qIntent.putExtra("answer", questions.get(question));
				startActivityForResult(qIntent,6);
			}
		}
		if (requestCode == 6 && resultCode == 0) {
			Toast.makeText(this, "Quest created!", Toast.LENGTH_SHORT);
			finish();
		}
	}

	public void populateMap(PlacesList nearPlaces,
			ArrayList<HashMap<String, String>> placeListItems) {
		// These values are used to get map boundary area
		// The area where you can see all the markers on screen
	      int minLat = Integer.MAX_VALUE;
	      int minLong = Integer.MAX_VALUE;
	      int maxLat = Integer.MIN_VALUE;
	      int maxLong = Integer.MIN_VALUE;

		// check for null in case it is null
		if (nearPlaces.results != null) {
			AddItemizedOverlay aio = new AddItemizedOverlay(dUser,this);
			// loop through all the places
			for (Place place : nearPlaces.results) {
				latitude = place.geometry.location.lat; // latitude
				longitude = place.geometry.location.lng; // longitude

				// Geopoint to place on map
				GeoPoint gp = new GeoPoint((int) (latitude * 1E6),
						(int) (longitude * 1E6));

				// Map overlay item
				OverlayItem oi = new OverlayItem(gp, place.name + ", " + place.vicinity,
						latitude + ", " + longitude);

				aio.addOverlay(oi);

				// calculating map boundary area
				minLat = (int) Math.min(gp.getLatitudeE6(), minLat);
				minLong = (int) Math.min(gp.getLongitudeE6(), minLong);
				maxLat = (int) Math.max(gp.getLatitudeE6(), maxLat);
				maxLong = (int) Math.max(gp.getLongitudeE6(), maxLong);
			}
			mapOverlays.add(aio);

			// showing all overlay items
			aio.populateNow();
		}

		// Adjusting the zoom level so that you can see all the markers on map
		mapView.getController().zoomToSpan(Math.abs(minLat - maxLat),
				Math.abs(minLong - maxLong));

		// Showing the center of the map
		mc.animateTo(new GeoPoint((maxLat + minLat) / 2,
				(maxLong + minLong) / 2));
		mapView.postInvalidate();
		
		placeButton.setVisibility(View.INVISIBLE);
        instructions.setVisibility(View.INVISIBLE);
        startOverButton.setVisibility(View.INVISIBLE);
		questionLabel.setVisibility(View.VISIBLE);
        questionField.setVisibility(View.VISIBLE);
        answerLabel.setVisibility(View.VISIBLE);
        answerField.setVisibility(View.VISIBLE);
        numberOfQuestionsLabel.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);
        
        for (OverlayItem oi : questPoints) {
        	if (questPoints.indexOf(oi) == 0) {
        		quest.setCenter(oi.getPoint());
        	}
        	else {
        		quest.addBoundary(oi.getPoint());
        	}
        }
	}
}
