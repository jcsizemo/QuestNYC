package com.columbia.server;

import java.util.List;
import java.util.Map;

import com.columbia.location.CenterPoint;
import com.columbia.quest.Quest;
import com.google.android.maps.GeoPoint;

public class XMLHelper {
	
	public static void QuestToXml(Quest quest) {
		CenterPoint c = quest.getCenter();
		List<GeoPoint> points = quest.getBoundaries();
		Map<String,String> questions = quest.getQuestions();
		StringBuilder sb = new StringBuilder("<Quest>");
		sb.append("<Center>" + (c.getLatitudeE6()/1000) + "," + (c.getLongitudeE6()/1000) + "</Center>");
		for (GeoPoint gp : points) {
			sb.append("<Boundary>" + (gp.getLatitudeE6()/1000) + "," + (gp.getLongitudeE6()/1000) + "</Boundary>");
		}
		for (String question : questions.keySet()) {
			sb.append("<Question>" + question + "," + questions.get(question) + "</Question>");
		}
		sb.append("</Quest>");
	}

}
