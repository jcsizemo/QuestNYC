package com.columbia.quest;

import com.google.android.maps.GeoPoint;

public class Question {

	private int id;
	private boolean solved;
	private String type;
	private GeoPoint point;
	private String sentence;
	
	public int getId() {
		return id;
	}

	public boolean getSolved() {
		return solved;
	}

	public String getType() {
		return type;
	}

	public GeoPoint getPoint() {
		return point;
	}

	public String getSentence() {
		return sentence;
	}

	public Question(int id, boolean solved, String type, int longitude, int latitude, String sentence){
		this.id = id;
		this.solved = solved;
		this.type = type;
		this.point = new GeoPoint(longitude, latitude);
		this.sentence = sentence;
	}
}
