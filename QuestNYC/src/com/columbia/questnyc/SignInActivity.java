package com.columbia.questnyc;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class SignInActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_layout);
    }
    
    public void onClick(View v) {
    	if (R.id.logInButton == v.getId()) {
    		
    	}
    	if (R.id.helpButton == v.getId()) {
    		Intent intent = new Intent(this, HelpActivity.class);
    		startActivity(intent);
    	}
    }
}
