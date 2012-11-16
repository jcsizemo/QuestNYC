package com.columbia.quest;

import java.util.ArrayList;

import com.columbia.questnyc.R;
import com.columbia.server.QuestionQuery;
import com.columbia.server.ServerQuery;
import com.columbia.server.XMLHelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class QuestionActivity extends ListActivity {
	
	Intent intent;
	Bundle pointData;
	private ArrayList<Question> questions;
	private ArrayList<String> questionLabels = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		pointData = intent.getExtras();
		int id = intent.getIntExtra("id", 0);
		Intent intent = new Intent(this, QuestionQuery.class);
		intent.putExtra("id", id);
		intent.putExtra("interactionType", ServerQuery.GET);
		startActivityForResult(intent, 3);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 3 && resultCode == 0) {
			String sQuestions = data.getStringExtra("questions");
			XMLHelper xHelper = new XMLHelper();
			questions = xHelper.XMLtoQuestion(sQuestions);
			for (Question q : questions) {
				questionLabels.add(q.getSentence() + "\nSolved: " + q.getSolved());
			}
			this.setListAdapter(new ArrayAdapter<Object>(this,
					android.R.layout.simple_list_item_1, questionLabels.toArray()));
		}
		if (requestCode == 5 && resultCode == 0) {
			
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Question q = questions.get(position);
		Intent intent = new Intent(this,AnswerQuestionActivity.class);
		intent.putExtra("question", q.getSentence());
		intent.putExtra("id", q.getId());
		intent.putExtras(pointData);
		startActivityForResult(intent,5);
	}
}