package com.columbia.quest.create;

import com.columbia.location.CenterPoint;
import com.columbia.location.GPSHelper;
import com.columbia.location.UserPoint;
import com.columbia.questnyc.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

public class CreateQuestActivity extends MapActivity implements OnClickListener, OnFocusChangeListener {
	
	MapView mapView;
	MapController mapController;
	Projection projection;
	
	@SuppressWarnings("deprecation")
	@TargetApi(13)
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_quest_layout);
        
        mapView = (MapView) findViewById(R.id.mapview); // get Map view
        mapView.setClickable(true);
        mapView.setOnClickListener(this);
        mapView.setOnFocusChangeListener(this);
        mapController = mapView.getController();
        projection = mapView.getProjection();
        mapView.setBuiltInZoomControls(true); // zoomable
        UserPoint user = GPSHelper.getLocation(this);
        mapController.animateTo(user);
        mapController.setZoom(16);
        mapView.invalidate();
        
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
        params.height = height/3;		// set map view to be 1/3 of the screen height
        mapView.setLayoutParams(params);
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void onClick(View v) {
		Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
		
	}

	public void onFocusChange(View arg0, boolean arg1) {
		Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
		
	}

}
