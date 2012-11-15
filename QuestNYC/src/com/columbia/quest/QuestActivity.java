package com.columbia.quest;

import com.columbia.questnyc.R;
import android.app.ListActivity;
import android.os.Bundle;

public class QuestActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quest_layout); // set layout
	}
}