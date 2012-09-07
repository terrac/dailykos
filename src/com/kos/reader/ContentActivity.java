package com.kos.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Html.TagHandler;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ContentActivity extends Activity {

	String value;
	String initial;
	boolean ignoreImages = true;
	boolean plainText = false;

	boolean tutorialMessage = false;
	private int fontSize;
	final public static String css = "		<script type=\"text/javascript\"><!--\n" + 
			"		  document.write('<link href=\"/c/enhanced.css\" rel=\"stylesheet\" media=\"screen, projection\" type=\"text/css\" />');\n" +
			"	<link href=\"/c/unified.css?rev=46\" media=\"all\" rel=\"stylesheet\" type=\"text/css\" />\n" + 
			"	<!-- can this be unified? -->\n" + 
			"	<link href=\"/c/print.css\" rel=\"stylesheet\" media=\"print\" type=\"text/css\" />\n" + 
			"		" + 
			"		//--></script>";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		String stringExtra = getIntent().getStringExtra("content");

		Uri parcelableExtra = getIntent().getParcelableExtra("uri");
		// String second=get(parcelableExtra,
		// stringExtra.substring(stringExtra.length()-10));
		value= initial = stringExtra;
		stringExtra += "<br> loading ....";
		loadData(stringExtra);

		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				String stringExtra = getIntent().getStringExtra("content");

				Uri parcelableExtra = getIntent().getParcelableExtra("uri");
				stringExtra = ContentActivity.this.get(parcelableExtra);
				value = stringExtra + "<a href=" + parcelableExtra
						+ "> go to kos page</a>";
				return null;
			}

			@Override
			protected void onPostExecute(String result) {

				Context context = getApplicationContext();
				CharSequence text = "Loaded.";
				int duration = Toast.LENGTH_SHORT;

				if (initial.length() > value.length()) {
					text = "Already Loaded";
					Uri parcelableExtra = getIntent().getParcelableExtra("uri");

					value = initial + "<a href=" + parcelableExtra
							+ "> go to kos page</a>";
				}
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				loadData(value);
			}
		}.execute("");
		
		
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		if(!preferences.getBoolean("tutorial", false)){
			Editor edit = preferences.edit();
			edit.putBoolean("tutorial", true);
			edit.commit();
			Toast.makeText(getApplicationContext(), "Use settings to format the diaries", Toast.LENGTH_SHORT).show();			
			
		}
		
		loadData(value);
	}

	public void loadData(String stringExtra) {
		if (plainText) {
			stringExtra = stripHtml(stringExtra);
		}
		
		stringExtra = css + stringExtra;
	
		
		if(ignoreImages){
			String ignore = 
					"<style type=\"text/css\">\n" + 
					"img\n" + 
					"{ display: none; }\n"+ 
					"</style> ";
			stringExtra = ignore + stringExtra;
		}
		String ignoreIFrame = 
				"<style type=\"text/css\">\n" + 
				"iframe\n" + 
				"{ display: none; }\n"+ 
				"</style> ";
		stringExtra = ignoreIFrame + stringExtra;
	
//		if (fontSize != 0) {
//			String size = "<style type=\"text/css\">\n" + "body\n"
//					+ "{ font-size:"+fontSize+"em; }\n" + "</style> ";
//			stringExtra = size + stringExtra;
//		}

		WebView tv = (WebView) findViewById(R.id.textView1);

		tv.loadDataWithBaseURL("http://www.dailykos.com", stringExtra,
				"text/html", "UTF-8", "about:blank");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_content, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (R.id.menu_comments == item.getItemId()) {
			Intent myIntent = new Intent(ContentActivity.this,
					CommentsActivity.class);
			String stringExtra = getIntent().getStringExtra("comments");
			if (stringExtra != null) {
				myIntent.putExtra("comments", stringExtra);
				ContentActivity.this.startActivity(myIntent);
			}

		}
//		if (R.id.menu_plain == item.getItemId()) {
//
//			item.setChecked(!item.isChecked());
//			plainText = item.isChecked();
//			if (item.isChecked()) {
//				item.setTitle("Complex Html");
//			} else {
//				item.setTitle("Simple Html");
//
//			}
//			loadData(value);
//
//		}
		if (R.id.menu_images == item.getItemId()) {
			
			item.setChecked(!item.isChecked());
			ignoreImages = item.isChecked();
			if (item.isChecked()) {
				item.setTitle("Not Showing Images");
			} else {
				item.setTitle("Showing Images");

			}
			loadData(value);

		}
		
//		if (R.id.menu_fontsize == item.getItemId()) {
//			final EditText et=new EditText(getApplicationContext());
//			et.setInputType(InputType.TYPE_CLASS_NUMBER);
//			
//			final AlertDialog dialog = new AlertDialog.Builder(this)
//		    .setTitle("Font Size")
//		    .setView(et).create();
//			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//			dialog.show();
//			
//			et.setOnEditorActionListener(new OnEditorActionListener() {
//				
//				@Override
//				public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
//					if(arg2 == null){
//						fontSize=Integer.getInteger(et.getText().toString());
//						
//						dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//						
//						dialog.dismiss();
//						
//											}
//					return false;
//				}
//			});
//	
//
//
//		}

		return super.onOptionsItemSelected(item);
	}

	public String get(Uri uri) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet("" + uri);
			HttpResponse response = httpClient.execute(httpGet, localContext);
			StringBuffer result = new StringBuffer();
			StringBuffer comments = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = null;
			int count = 0;
			while ((line = reader.readLine()) != null) {
				if (count > 2) {

					if (line.contains("<li id=\"c2\">")) {
						count = 4;
					}
					if (count == 4) {
						//Log.d("content", line);
						comments.append(line + "\n");
					}
					if (line.contains("<li id=\"eP\">")) {
						this.getIntent().putExtra("comments",
								comments.toString());
						break;
					}
				}

				if (count > 0 && count < 3) {
					// Log.d("content", line);
					result.append(line + "\n");
					if (line.contains("</div>")) {
						count++;

					}
				} else {
					if (line.contains("<div id=\"intro\">")) {
						count = 1;
					}
				}
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String stripHtml(String a) {
		Html.fromHtml(a);
		return null;
		//return a.replaceAll("<(^a|^p|^\\s|^blockquote)+?>", "");
	}
}
