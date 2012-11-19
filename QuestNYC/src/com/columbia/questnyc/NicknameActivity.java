package com.columbia.questnyc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NicknameActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nickname_layout);
    }
	
	public void onClick(View v) {
    	EditText nameField = (EditText) findViewById(R.id.nicknameField);
    	String nickname = nameField.getText().toString();
    	if ("".equals(nickname)) {
    		Toast.makeText(this, "No nickname entered!", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	Intent intent = getIntent();
    	intent.putExtra("Nickname", nickname);
    	setResult(0,intent);
    	finish();
    }
	
}
