package com.columbia.questnyc;

import com.columbia.quest.create.CreateQuestActivity;
import com.columbia.server.ServerHelper;
import com.columbia.server.ServerQuery;
import com.columbia.server.SignInQuery;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends Activity {
	
	String nickname;
	String email;
	String password;
	boolean isAdmin;
	boolean success;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_layout);
    }
    
    public void onClick(View v) {
    	if (R.id.helpButton == v.getId()) {
    		Intent intent = new Intent(this, HelpActivity.class);
    		startActivity(intent);
    	}
    	else {
    		EditText emailField = (EditText) findViewById(R.id.emailField);
    		EditText pwdField = (EditText) findViewById(R.id.passwordField);
    		email = emailField.getText().toString();
    		password = pwdField.getText().toString();
    		
    		if ("".equals(email) || "".equals(password)) {
    			Toast.makeText(this, "Email or password field blank", Toast.LENGTH_SHORT).show();
    			return;
    		}
    		if (R.id.signInButton == v.getId()) {
    			Intent intent = new Intent(this, SignInQuery.class);
    			intent.putExtra("email", email);
    			intent.putExtra("password",password);
    			intent.putExtra("interactionType",ServerQuery.POST);
    			startActivityForResult(intent,3);
    		}
    		else if (R.id.signUpButton == v.getId()) {
    			Intent intent = new Intent(this, NicknameActivity.class);
    	    	startActivityForResult(intent,2);
    		}
    	}
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == 0) {
			nickname = data.getStringExtra("Nickname");
			Intent intent = new Intent(this, SignInQuery.class);
			intent.putExtra("email", email);
			intent.putExtra("password", password);
			intent.putExtra("interactionType", ServerQuery.POST);
			intent.putExtra("nickname", nickname);
			startActivityForResult(intent, 3);
		}
		if (requestCode == 3 && resultCode == 0) {
			process(data);
		}
	}
	
	public void process(Intent data) {
		success = data.getBooleanExtra("success", false);
		isAdmin = data.getBooleanExtra("isAdmin", false);
		ServerHelper.isAdmin = isAdmin;
		if (success == true) {
			Intent intent = new Intent(this, PlayOrCreateActivity.class);
			intent.putExtra("isAdmin", isAdmin);
			startActivity(intent);
		}
		else {
			Toast.makeText(this,"Problems signing in. Please check your input information.", Toast.LENGTH_SHORT).show();
		}
	}
}
