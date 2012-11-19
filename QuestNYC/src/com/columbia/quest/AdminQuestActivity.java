package com.columbia.quest;

import java.util.ArrayList;

import com.columbia.location.GPSHelper;
import com.columbia.server.DeleteQuery;
import com.columbia.server.QuestQuery;
import com.columbia.server.ServerQuery;
import com.columbia.server.XMLHelper;
import com.google.android.maps.GeoPoint;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AdminQuestActivity extends ListActivity {
	
	private ArrayList<Quest> quests;
	private ArrayList<String> questLabels = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GeoPoint gp = GPSHelper.getLocation(this);
		int latitude = (int) (gp.getLatitudeE6() / 1E6);
		int longitude = (int) (gp.getLongitudeE6() / 1E6);
		Intent intent = new Intent(this, QuestQuery.class);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longitude", longitude);
		intent.putExtra("interactionType", ServerQuery.GET);
		startActivityForResult(intent, 3);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 3 && resultCode == 0) {
			String sQuests = data.getStringExtra("quests");
			XMLHelper xHelper = new XMLHelper();
			quests = xHelper.XMLtoQuest(sQuests);
			for (Quest q : quests) {
				questLabels.add("Name: " + q.getName() + "\nDescription: " + q.getDescription() + "\nRating: " + q.rating());
			}
			this.setListAdapter(new ArrayAdapter<Object>(this,
					android.R.layout.simple_list_item_1, questLabels.toArray()));
		}
		if (requestCode == 5 && resultCode == 0) {
			int id = data.getIntExtra("idToDelete", 0);
			Intent intent = new Intent(this,DeleteQuery.class);
			intent.putExtra("interactionType", ServerQuery.GET);
			intent.putExtra("id", id);
			startActivityForResult(intent,6);
		}
		if (requestCode == 6 && resultCode == 0) {
			
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Quest q = quests.get(position);
		Intent intent = new Intent(this,AdminQuestDeleteActivity.class);
		intent.putExtra("id", q.getId());
		startActivityForResult(intent,5);
	}
}