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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

public class ContentActivity extends Activity {

	String value;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		WebView tv = (WebView) findViewById(R.id.textView1);
		String stringExtra = getIntent().getStringExtra("content");

		Uri parcelableExtra = getIntent().getParcelableExtra("uri");
		// String second=get(parcelableExtra,
		// stringExtra.substring(stringExtra.length()-10));
		stringExtra += "<br> loading ....";
		tv.loadData(stringExtra, "text/html; charset=UTF-8", null);

		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				String stringExtra = getIntent().getStringExtra("content");

				Uri parcelableExtra = getIntent().getParcelableExtra("uri");
				stringExtra = ContentActivity.this.get(parcelableExtra);
				value = stringExtra +"<a href=" + parcelableExtra +   "> go to kos page</a>";
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				
				Context context = getApplicationContext();
				CharSequence text = "Loaded.";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				WebView tv = (WebView) findViewById(R.id.textView1);

				//tv.loadData(value, "text/html; charset=UTF-8", null);
				tv.loadDataWithBaseURL("http://www.dailykos.com", value,
						"text/html", "UTF-8", "about:blank");

			}
		}.execute("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_content, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(R.id.menu_comments == item.getItemId()){
			Intent myIntent = new Intent(ContentActivity.this, CommentsActivity.class);
			String stringExtra = getIntent().getStringExtra("comments");
			if(stringExtra != null){
				myIntent.putExtra("comments",stringExtra);			
				ContentActivity.this.startActivity(myIntent);			
			}
		
		}
		if(R.id.menu_plain == item.getItemId()){

			WebView tv = (WebView) findViewById(R.id.textView1);
			//tv.loadData(stripHtml(value), "text/plain; charset=UTF-8", null);
			tv.loadDataWithBaseURL("http://www.dailykos.com", stripHtml(value),
					"text/html", "UTF-8", "about:blank");

		}

		
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
				if(count > 2){
					
					if(line.contains("<li id=\"c2\">")) {
						count = 4;
					}
					if(count == 4){
						Log.d("content", line);
						comments.append(line + "\n");						
					}
					if(line.contains("<li id=\"eP\">")) {
						this.getIntent().putExtra("comments", comments.toString());
						break;
					}
				}
				
				
				if (count > 0 && count < 3) {
					//Log.d("content", line);
					result.append(line + "\n");
					if(line.contains("</div>")) {
						count++;
						
					}
				} else {
					if(line.contains("<div id=\"intro\">")) {
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

	public String stripHtml(String a){
		return a.replaceAll("<([^a]|\\n)+?>", "");
	}
}
