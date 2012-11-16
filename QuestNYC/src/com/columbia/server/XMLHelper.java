package com.columbia.server;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.columbia.quest.Quest;
import com.columbia.quest.Question;
import com.google.android.maps.GeoPoint;

public class XMLHelper {

	public ArrayList<Quest> XMLtoQuest(String xml) {
		ArrayList<Quest> questList = new ArrayList<Quest>();

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nlst1 = doc.getElementsByTagName("questList");
			Element e = (Element) nlst1.item(0);
			String exists = e.getAttribute("result");
			if (exists.equals("True")) {
				NodeList nodeLst = doc.getElementsByTagName("quest");

				for (int s = 0; s < nodeLst.getLength(); s++) {
					Node fstNode = nodeLst.item(s);
					if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
						Element fstElmnt = (Element) fstNode;
						int id = Integer.parseInt(fstElmnt.getAttribute("id"));
						String name = fstElmnt.getAttribute("name");
						double longitude = Double.parseDouble(fstElmnt
								.getAttribute("longitude"));
						double latitude = Double.parseDouble(fstElmnt
								.getAttribute("latitude"));
						longitude *= 1E6;
						latitude *= 1E6;
						int lat = (int) latitude;
						int lon = (int) longitude;
						float rating = Float.parseFloat(fstElmnt
								.getAttribute("rating"));
						float solvedRate = Float.parseFloat(fstElmnt
								.getAttribute("solvedRate"));

						NodeList fstNmElmntLst = fstElmnt
								.getElementsByTagName("description");
						Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
						NodeList fstNm = fstNmElmnt.getChildNodes();
						String description = ((Node) fstNm.item(0))
								.getNodeValue();

						ArrayList<GeoPoint> boundaries = new ArrayList<GeoPoint>();
						NodeList nlst = fstElmnt
								.getElementsByTagName("coordinate");
						for (int i = 0; i < nlst.getLength(); i++) {
							Element myElmnt = (Element) nlst.item(i);
							int long1 = (int) Double.parseDouble(myElmnt
									.getAttribute("longitude"));
							int lat1 = (int) Double.parseDouble(myElmnt
									.getAttribute("latitude"));

							GeoPoint gp = new GeoPoint(lat1, long1);
							boundaries.add(gp);
						}

						Quest q = new Quest(id, name, description, lon,
								lat, boundaries, rating, solvedRate);
						questList.add(q);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return questList;
	}

	public ArrayList<Question> XMLtoQuestion(String xml) {
		ArrayList<Question> questionList = new ArrayList<Question>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nlst1 = doc.getElementsByTagName("questionList");
			Element elmt = (Element) nlst1.item(0);
			String exists = elmt.getAttribute("result");

			if (exists.equals("True")) {
				NodeList nodeLst = doc.getElementsByTagName("question");

				for (int s = 0; s < nodeLst.getLength(); s++) {
					Node fstNode = nodeLst.item(s);
					if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
						Element fstElmnt = (Element) fstNode;
						int id = Integer.parseInt(fstElmnt.getAttribute("id"));
						int solved = Integer.parseInt(fstElmnt
								.getAttribute("solved"));
						boolean bSolved = false;
						if (solved == 2) {
							bSolved = true;
						}
						String type = fstElmnt.getAttribute("type");
						int longitude = (int) Double.parseDouble(fstElmnt
								.getAttribute("longitude"));
						int latitude = (int) Double.parseDouble(fstElmnt
								.getAttribute("latitude"));

						NodeList fstNmElmntLst = fstElmnt
								.getElementsByTagName("sentence");
						Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
						NodeList fstNm = fstNmElmnt.getChildNodes();
						String sentence = ((Node) fstNm.item(0)).getNodeValue();

						Question q = new Question(id, bSolved, type, longitude,
								latitude, sentence);
						questionList.add(q);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return questionList;
	}

	public boolean XMLCheckAnswer(String xml) {
		boolean isCorrect = false;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nlst1 = doc.getElementsByTagName("answerquestion");
			Element elmt = (Element) nlst1.item(0);
			String exists = elmt.getAttribute("result");

			if (exists.equals("True")) {
				String correct = elmt.getAttribute("correct");
				if (correct.equals("True"))
					isCorrect = true;
				else
					isCorrect = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isCorrect;
	}
	
	public String XMLgetQuestId(String xml){
		String questId = null;
	    try {
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(xml));
	        Document doc = db.parse(is);
	        doc.getDocumentElement().normalize();
	          
	        NodeList nlst1 = doc.getElementsByTagName("addquest");
	        Element elmt = (Element) nlst1.item(0);
	        String exists = elmt.getAttribute("result");
	        
	        questId = "";
	        if(exists.equals("True"))
	            questId = elmt.getAttribute("questid");
	    }catch (Exception e) {
	        e.printStackTrace();
	    }
	    return questId;
	}
}
