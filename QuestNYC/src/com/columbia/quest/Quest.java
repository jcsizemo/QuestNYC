package com.columbia.quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.columbia.location.CenterPoint;
import com.google.android.maps.GeoPoint;

public class Quest implements Serializable {
	
	private int id;
	private String name;
	private String description;
	private List<GeoPoint> boundaries = new ArrayList<GeoPoint>();
	private CenterPoint center;
	private float rating;
	private float solvedRate;
	CenterPoint c;
	Map<String,String> questions = new HashMap<String,String>();
	
	public Quest(int id, String name, String description, int longitude, int latitude, ArrayList<GeoPoint> boundaries, float rating, float solvedRate){
		this.id = id;
		this.name = name;
		this.description = description;
		this.center = new CenterPoint(longitude, latitude);
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

}
