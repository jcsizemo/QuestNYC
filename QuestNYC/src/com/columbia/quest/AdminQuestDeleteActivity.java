package com.columbia.quest;

import com.columbia.questnyc.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminQuestDeleteActivity extends Activity {
	
	Intent intent;
	int id;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		id = intent.getIntExtra("id", 0);
		setContentView(R.layout.admin_quest_delete_layout);
	}
	
	public void onClick(View v) {
		if (v.getId() == R.id.yesButton) {
			intent.putExtra("idToDelete", id);
			setResult(0,intent);
		}
		else if (v.getId() == R.id.noButton) {
			setResult(1,intent);
		}
		finish();
	}

}
