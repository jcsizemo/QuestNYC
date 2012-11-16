package com.columbia.quest;

import com.columbia.location.GPSHelper;
import com.columbia.server.QuestQuery;
import com.columbia.server.ServerQuery;
import com.google.android.maps.GeoPoint;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class QuestActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.quest_layout); // set layout
		GeoPoint gp = GPSHelper.getLocation(this);
		int latitude = (int) (gp.getLatitudeE6() / 1E6);
		int longitude = (int) (gp.getLongitudeE6() / 1E6);
		Intent intent = new Intent(this, QuestQuery.class);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longitude", longitude);
		intent.putExtra("interactionType", ServerQuery.GET);
		String[] test = {"Columbia Quest"};
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, test));
		startActivityForResult(intent, 3);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 3 && resultCode == 0) {
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
	}
}