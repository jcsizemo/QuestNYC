package com.columbia.quest;

import com.columbia.questnyc.R;
import com.columbia.server.RatingQuery;
import com.columbia.server.ServerQuery;
import com.columbia.server.XMLHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Toast;

public class RatingActivity extends Activity {
	
	Intent intent;
	Bundle extras;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        extras = intent.getExtras();
        setContentView(R.layout.rating_layout);
    }
	
    public void onClick(View v) {
    	RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
    	float rating = rb.getRating();
    	Intent intent = new Intent(this, RatingQuery.class);
    	intent.putExtra("rating", rating);
    	intent.putExtra("interactionType", ServerQuery.GET);
    	intent.putExtras(extras);
    	startActivityForResult(intent,2);
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == 0) {
			finish();
		}
	}

}
