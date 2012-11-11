package com.columbia.quest;

import java.util.ArrayList;
import java.util.List;

import com.columbia.questnyc.R;
import com.columbia.server.ServerHelper;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestActivity extends MapActivity {
	
	MapView mapView;
	MapController mapController;
	Projection projection;
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_layout);	// set layout
        
        mapView = (MapView) findViewById(R.id.mapview); // get Map view
        mapController = mapView.getController();
        projection = mapView.getProjection();
        mapView.setBuiltInZoomControls(true); // zoomable

        GeoPoint gp = ServerHelper.getLocation(this);
        mapController.animateTo(gp);
        mapController.setZoom(12);
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
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.post);
        QuestOverlay qOverlay = new QuestOverlay(drawable,this);
        
        GeoPoint point = new GeoPoint(19240000,-99120000);
        GeoPoint point2 = new GeoPoint(19240000,-98120000);
        GeoPoint point3 = new GeoPoint(19340000,-99120000);
        GeoPoint point4 = new GeoPoint(19340000,-98120000);
        OverlayItem item = new OverlayItem(point, null, null);
        OverlayItem item2 = new OverlayItem(point2, null, null);
        OverlayItem item3 = new OverlayItem(point3, null, null);
        OverlayItem item4 = new OverlayItem(point4, null, null);
        qOverlay.addOverlay(item);
        qOverlay.addOverlay(item2);
        qOverlay.addOverlay(item3);
        qOverlay.addOverlay(item4);
        mapOverlays.add(qOverlay);
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	class QuestOverlay extends ItemizedOverlay {
		
		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		private Context mContext;

		public QuestOverlay(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			mContext = context;
		}

		@Override
		public void draw(Canvas canvas, MapView mapv, boolean shadow) {
			super.draw(canvas, mapv, shadow);
			
			Paint mPaint = new Paint();
			mPaint.setDither(true);
			mPaint.setColor(Color.RED);
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStrokeWidth(2);
			
			GeoPoint gP1 = new GeoPoint(19240000,-99120000);
	        GeoPoint gP2 = new GeoPoint(37423157, -122085008);

	        Point p1 = new Point();
	        Point p2 = new Point();
	        Path path = new Path();

	        projection.toPixels(gP1, p1);
	        projection.toPixels(gP2, p2);

	        path.moveTo(p2.x, p2.y);
	        path.lineTo(p1.x,p1.y);

	        canvas.drawPath(path, mPaint);
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			return mOverlays.size();
		}
		
		public void addOverlay(OverlayItem overlay) {
			mOverlays.add(overlay);
			populate();
		}
		
	}
}