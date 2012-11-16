package com.columbia.questnyc;

import com.columbia.quest.AdminQuestActivity;
import com.columbia.quest.QuestActivity;
import com.columbia.quest.create.CreateQuestActivity;
import com.columbia.server.ServerHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayOrCreateActivity extends Activity {
	
	boolean isAdmin = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        isAdmin = intent.getBooleanExtra("isAdmin", isAdmin);
        setContentView(R.layout.play_or_create_layout);
        Button deleteQuestButton = (Button) findViewById(R.id.deleteQuestButton);
        if (!isAdmin) {
        	deleteQuestButton.setVisibility(View.INVISIBLE);
        }
    }
	
    public void onClick(View v) {
    	if (R.id.createQuestButton == v.getId()) {
    		Intent intent = new Intent(this, CreateQuestActivity.class);
    		startActivity(intent);
    	}
    	else if (R.id.playQuestButton == v.getId()) {
    		Intent intent = new Intent(this, QuestActivity.class);
    		startActivity(intent);
    	}
    	else if (R.id.deleteQuestButton == v.getId()) {
    		Intent intent = new Intent(this, AdminQuestActivity.class);
    		startActivity(intent);
    	}
    }
}
