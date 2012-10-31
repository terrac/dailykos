package com.kos.reader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		ContentActivity.removeAd(this, preferences);
		
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
		
		StringBuffer stringExtra = new StringBuffer("<style type=\"text/css\">"
			+ "A:link {text-decoration: none; color: orange;}"
			+ "A:visited {text-decoration: none; color: #654B0F;}"
			+ "A:active {text-decoration: none; color: orange;}"
			+ "A:hover {text-decoration: underline; color: #654B0F;}"
			+ "</style>"+ContentActivity.jquery);

		ContentActivity.ignoreImages(this, preferences,stringExtra);
		ContentActivity.fontSize(stringExtra, preferences);

		stringExtra.append(getIntent().getStringExtra("comments"));
		showRecomended(stringExtra, preferences);

		//Log.d("com", stringExtra.to);
		tv.loadDataWithBaseURL("http://www.dailykos.com", stringExtra.toString(),
				"text/html", "UTF-8", "about:blank");

	}

	private void showRecomended(StringBuffer stringExtra,
			SharedPreferences preferences) {

		// String string = getResources().getString(R.string.comment_script);
		String a =  addScript(scr);
		stringExtra.insert(0,a.replace("numberOfRecommends",
				preferences.getString("recommendsHide", "0"))
				);
		stringExtra.append(addScript(hideRecs));
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
