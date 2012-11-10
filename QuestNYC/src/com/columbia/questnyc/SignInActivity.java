package com.columbia.questnyc;

import com.columbia.server.ServerHelper;

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
    	if (R.id.signInButton == v.getId()) {
    		Intent intent = new Intent(this, ServerHelper.class);
    		startActivity(intent);
    	}
    	else if (R.id.signUpButton == v.getId()) {
    		
    	}
    	else if (R.id.helpButton == v.getId()) {
    		Intent intent = new Intent(this, HelpActivity.class);
    		startActivity(intent);
    	}
    }
}
