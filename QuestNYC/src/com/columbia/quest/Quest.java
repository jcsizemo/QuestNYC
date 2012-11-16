package com.columbia.quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.columbia.location.CenterPoint;
import com.google.android.maps.GeoPoint;

public class Quest implements Serializable {
	
	Map<String,String> questions;
	List<GeoPoint> boundaries;
	CenterPoint center;
	
	public Quest() {
		questions = new HashMap<String,String>();
		boundaries = new ArrayList<GeoPoint>();
	}
	
	public void addBoundary(GeoPoint gp) {
		boundaries.add(gp);
	}
	
	public void setCenter(GeoPoint gp) {
		center = new CenterPoint(gp.getLatitudeE6(), gp.getLongitudeE6());
	}
	
	public void addQuestion(String question, String answer) {
		questions.put(question, answer);
	}
	
	public Map<String,String> getQuestions() {
		return questions;
	}

}
