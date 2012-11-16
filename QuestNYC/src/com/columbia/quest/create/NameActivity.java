package com.columbia.quest.create;

import com.columbia.questnyc.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NameActivity extends Activity {
	
	String name;
	String description;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_layout);
    }
	
	public void onClick(View v) {
    	EditText nameField = (EditText) findViewById(R.id.questNameField);
    	EditText descriptionField = (EditText) findViewById(R.id.questDescriptionField);
    	name = nameField.getText().toString();
    	description = descriptionField.getText().toString();
    	if ("".equals(name) || "".equals(description)) {
    		Toast.makeText(this, "No name or description entered!", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	Intent intent = getIntent();
    	intent.putExtra("name", name);
    	intent.putExtra("description",description);
    	setResult(0,intent);
    	finish();
    }
	
}
