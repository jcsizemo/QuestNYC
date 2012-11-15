package com.columbia.questnyc;

import com.columbia.quest.create.CreateQuestActivity;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_layout);
    }
    
    public void onClick(View v) {
    	if (R.id.helpButton == v.getId()) {
    		Intent intent = new Intent(this, CreateQuestActivity.class);
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
    			SignInQuery sq = new SignInQuery(this, email,password,ServerQuery.POST,null);
    			sq.execute();
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
		}
		SignInQuery sq = new SignInQuery(this,email,password,ServerQuery.POST,nickname);
		sq.execute();
	}
	
	public void process(boolean success, boolean isAdmin) {
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
