package com.columbia.questnyc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayOrCreateActivity extends Activity {
	
	Button deleteButton;
	boolean isAdmin = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_or_create_layout);   
    }
	
    public void onClick(View v) {
    	if (R.id.createQuestButton == v.getId()) {
    		Intent intent = new Intent(this, PlayOrCreateActivity.class);
    		startActivity(intent);
    	}
    	else if (R.id.playQuestButton == v.getId()) {
    		Intent intent = new Intent(this, PlayOrCreateActivity.class);
    		startActivity(intent);
    	}
    	else if (R.id.deleteQuestButton == v.getId()) {
    		Intent intent = new Intent(this, HelpActivity.class);
    		startActivity(intent);
    	}
    }
}
