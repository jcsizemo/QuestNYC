package com.columbia.quest;

import java.util.ArrayList;
import java.util.List;

import com.columbia.location.CenterPoint;
import com.google.android.maps.GeoPoint;

public class Quest {
	
	List<String> questionIDs = new ArrayList<String>();
	List<GeoPoint> boundaries = new ArrayList<GeoPoint>();
	CenterPoint center;

}
