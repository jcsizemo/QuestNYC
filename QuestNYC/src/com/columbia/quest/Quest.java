package com.columbia.quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.columbia.location.BoundaryPoint;
import com.columbia.location.CenterPoint;
import com.google.android.maps.GeoPoint;
import android.content.Intent;

public class Quest implements Serializable {
	
	private int id;
	private String name;
	private String description;
	private List<GeoPoint> boundaries = new ArrayList<GeoPoint>();
	private CenterPoint center;
	private float rating;
	private float solvedRate;
	Map<String,String> questions = new HashMap<String,String>();
	
	public Quest(int id, String name, String description, int longitude, int latitude, ArrayList<GeoPoint> boundaries, float rating, float solvedRate){
		this.id = id;
		this.name = name;
		this.description = description;
		this.center = new CenterPoint(latitude, longitude);
		this.boundaries = boundaries;
		this.rating = rating;
		this.solvedRate = solvedRate;
	}
	
	public Quest() {
		
	}
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public CenterPoint getCenter(){
		return center;
	}
	
	public List<GeoPoint> getBoundaries(){
		return boundaries;
	}
	
	public float rating(){
		return rating;
	}
	
	public float solvedRate(){
		return solvedRate;
	}
	
	public void addQuestion(String question, String answer) {
		questions.put(question, answer);
	}
	
	public Map<String,String> getQuestions() {
		return questions;
	}
	
	public void addBoundary(GeoPoint gp) {
		boundaries.add(gp);
	}
	
	public void setCenter(GeoPoint c) {
		center = new CenterPoint(c.getLatitudeE6(),c.getLongitudeE6());
	}
	
	public static Intent addQuestPoints(Quest q, Intent intent) {
		intent.putExtra("centerLat", q.getCenter().getLatitudeE6());
		intent.putExtra("centerLong", q.getCenter().getLongitudeE6());
		List<GeoPoint> bounds = q.getBoundaries();
		for (int i = 0; i < bounds.size(); i++) {
			intent.putExtra("bound" + i + "Lat", bounds.get(i).getLatitudeE6());
			intent.putExtra("bound" + i + "Long", bounds.get(i).getLongitudeE6());
		}
		return intent;
	}
	
	public static List<BoundaryPoint> getBoundaryPoints(Intent intent) {
		List<BoundaryPoint> returnList = new ArrayList<BoundaryPoint>();
		for (int i = 0; i < 4; i++) {
			int latitude = intent.getIntExtra("bound" + i + "Lat", 0);
			int longitude = intent.getIntExtra("bound" + i + "Long", 0);
			returnList.add(new BoundaryPoint((int) (latitude * 1E6),(int) (longitude * 1E6)));
		}
		return returnList;
	}
	
	public static CenterPoint getCenterPoint(Intent intent) {
		int latitude = intent.getIntExtra("centerLat", 0);
		int longitude = intent.getIntExtra("centerLong", 0);
		return new CenterPoint((int) (latitude * 1E6),(int) (longitude * 1E6));
	}

}
