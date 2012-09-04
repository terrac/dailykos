package com.kos.reader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class CommentsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		WebView tv = (WebView) findViewById(R.id.textView1);
		String stringExtra = 
				"<style type=\"text/css\">\n" + 
				"div.crs\n" + 
				"{ display: none; }\n"+ 
				"</style> ";
		stringExtra += getIntent().getStringExtra("comments");
		Log.d("com", stringExtra);
		tv.loadDataWithBaseURL("http://www.dailykos.com", stringExtra,
				"text/html", "UTF-8", "about:blank");
		
	}

}
