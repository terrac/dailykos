package com.kos.reader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class CommentsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content_no_bar);
		doInitialLoad();

	}

	@Override
	protected void onRestart() {
		doInitialLoad();
		super.onRestart();
	}

	public void doInitialLoad() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		WebView tv = (WebView) findViewById(R.id.textView1);
		tv.setWebChromeClient(new WebChromeClient());
		tv.getSettings().setJavaScriptEnabled(true);
		
		String stringExtra = "";

		ContentActivity.ignoreImages(this, preferences);
		stringExtra = ContentActivity.fontSize(stringExtra, preferences);

		stringExtra += getIntent().getStringExtra("comments");
		stringExtra = showRecomended(stringExtra, preferences);

		Log.d("com", stringExtra);
		tv.loadDataWithBaseURL("http://www.dailykos.com", stringExtra,
				"text/html", "UTF-8", "about:blank");

	}

	private String showRecomended(String stringExtra,
			SharedPreferences preferences) {

		// String string = getResources().getString(R.string.comment_script);
		String a = "<script src=https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js></script>"
				+ addScript(scr);
		stringExtra = a.replace("numberOfRecommends",
				preferences.getString("recommendsHide", "0"))
				+ stringExtra+addScript(hideRecs);

		return stringExtra;
	}

	String scr = "var i = numberOfRecommends;\r\n"
			+ "if(i != 0){\r\n"
			+ "\r\n"
			+ "document.write(\"<input type=button value='togglecomments below numberOfRecommends recommends' onclick=toggleComments()></input>\")\r\n"
			+ "\r\n" + "}\r\n" + "function toggleComments(){\r\n"
			+ "	$(\".cx\").each(function() {\r\n"
			+ "		 if(i >$(this).find(\".crs\").find(\"a\").length){\r\n"
			+ "	    	$(this).toggle();\r\n" + "	    }\r\n" + "	    \r\n"
			+ "	});\r\n" + "	}\r\n" ;

	
	String hideRecs = "$('.crs').hide();";

	public static String addScript(String string) {
		return "<script>" + string + "</script>";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_titles, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (R.id.menu_preferences == item.getItemId()) {
			preferences(null);
		}
		return super.onOptionsItemSelected(item);
	}

	public void preferences(View v) {
		Intent myIntent = new Intent(CommentsActivity.this, PrefsFragment.class);
		CommentsActivity.this.startActivity(myIntent);

	}
}
