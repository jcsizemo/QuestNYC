package com.columbia.quest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.columbia.location.BoundaryPoint;
import com.columbia.location.CenterPoint;
import com.columbia.location.GPSHelper;
import com.columbia.location.UserPoint;
import com.columbia.questnyc.R;
import com.columbia.server.QuestionQuery;
import com.columbia.server.ServerQuery;
import com.columbia.server.SignInQuery;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class AnswerQuestionActivity extends MapActivity {
	
	MapView mapView;
	MapController mapController;
	Projection projection;
	Intent intent;
	List<Overlay> mapOverlays;
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_question_layout);	// set layout
        mapView = (MapView) findViewById(R.id.mapview); // get Map view
        mapOverlays = mapView.getOverlays();
        intent = getIntent();
        
        mapController = mapView.getController();
        projection = mapView.getProjection();
        mapView.setBuiltInZoomControls(true); // zoomable
        
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
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        List<BoundaryPoint> boundaries = Quest.getBoundaryPoints(intent);
        Drawable dCenter = this.getResources().getDrawable(R.drawable.center);
        Drawable dUser = this.getResources().getDrawable(R.drawable.user);
        Drawable dBoundary = this.getResources().getDrawable(R.drawable.boundary);
        
        QuestOverlay userOverlay = new QuestOverlay(dUser,this);
        UserPoint user = GPSHelper.getLocation(this);
        OverlayItem o = new OverlayItem(user, null, null);
        userOverlay.addOverlay(o);
        mapOverlays.add(userOverlay);
        
        QuestOverlay centerOverlay = new QuestOverlay(dCenter,this);
        CenterPoint c = Quest.getCenterPoint(intent);
        OverlayItem o2 = new OverlayItem(c, null, null);
        centerOverlay.addOverlay(o2);
        mapOverlays.add(centerOverlay);
        
        QuestOverlay boundaryOverlay = new QuestOverlay(dBoundary,this);
        OverlayItem o3 = new OverlayItem(boundaries.get(0),null,null);
        OverlayItem o4 = new OverlayItem(boundaries.get(1),null,null);
        OverlayItem o5 = new OverlayItem(boundaries.get(2),null,null);
        OverlayItem o6 = new OverlayItem(boundaries.get(3),null,null);
        boundaryOverlay.addOverlay(o3);
        boundaryOverlay.addOverlay(o4);
        boundaryOverlay.addOverlay(o5);
        boundaryOverlay.addOverlay(o6);
        mapOverlays.add(boundaryOverlay);
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public void onClick(View v) {
		if (v.getId() == R.id.answerQuestionButton) {
			EditText questionAnswer = (EditText) findViewById(R.id.answerQuestionField);
			String answer = questionAnswer.getText().toString();
			if ("".equals(answer)) {
				Toast.makeText(this, "No answer", Toast.LENGTH_SHORT).show();
			}
			Intent answerIntent = new Intent(this,QuestionQuery.class);
			answerIntent.putExtra("answer", answer);
			startActivityForResult(answerIntent,5);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 5 && resultCode == 0) {
			
		}
	}
	
	class QuestOverlay extends ItemizedOverlay<OverlayItem> {
		
		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

		public QuestOverlay(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
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
			GeoPoint oldP = null;
			ArrayList<OverlayItem> boundaries = new ArrayList<OverlayItem>();
			
			for (OverlayItem o : mOverlays) {
				if ((o.getPoint() instanceof CenterPoint) || (o.getPoint() instanceof UserPoint)) {
					continue;
				}
				boundaries.add(o);
			}
			
			if (boundaries.size() != 0) {
				for (int i = 0; i < (1 + boundaries.size()); i++) {
					if (0 == i) {
						oldP = boundaries.get(i % (boundaries.size()))
								.getPoint();
						continue;
					}
					GeoPoint newP = boundaries.get(i % (boundaries.size()))
							.getPoint();
					Point p1 = new Point();
					Point p2 = new Point();
					Path path = new Path();
					projection.toPixels(oldP, p1);
					projection.toPixels(newP, p2);
					path.moveTo(p2.x, p2.y);
					path.lineTo(p1.x, p1.y);
					canvas.drawPath(path, mPaint);
					oldP = newP;
				}
			}
			
			
//			GeoPoint gP1 = new GeoPoint(19240000,-99120000);
//	        GeoPoint gP2 = new GeoPoint(37423157, -122085008);
//
//	        Point p1 = new Point();
//	        Point p2 = new Point();
//	        Path path = new Path();
//
//	        projection.toPixels(gP1, p1);
//	        projection.toPixels(gP2, p2);
//
//	        path.moveTo(p2.x, p2.y);
//	        path.lineTo(p1.x,p1.y);
//
//	        canvas.drawPath(path, mPaint);
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