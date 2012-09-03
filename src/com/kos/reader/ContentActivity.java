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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;

public class ContentActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		WebView tv = (WebView) findViewById(R.id.textView1);
		String stringExtra = getIntent().getStringExtra("content");

		Uri parcelableExtra = getIntent().getParcelableExtra("uri");
		// String second=get(parcelableExtra,
		// stringExtra.substring(stringExtra.length()-10));
		stringExtra += "<a href=" + parcelableExtra + "> go to kos page</a>";
		tv.loadData(stringExtra, "text/html; charset=UTF-8", null);

		new AsyncTask<String, Void, String>() {
			String value;

			@Override
			protected String doInBackground(String... params) {
				String stringExtra = getIntent().getStringExtra("content");

				Uri parcelableExtra = getIntent().getParcelableExtra("uri");
				stringExtra = ContentActivity.get(parcelableExtra);
				value = stringExtra + "<a href=" + parcelableExtra
						+ "> go to kos page</a>";
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				WebView tv = (WebView) findViewById(R.id.textView1);

				tv.loadData(value, "text/html; charset=UTF-8", null);
			}
		}.execute("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public static String get(Uri uri) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet("" + uri);
			HttpResponse response = httpClient.execute(httpGet, localContext);
			String result = "";

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = null;
			int count = 0;
			while ((line = reader.readLine()) != null) {
				if (count > 0) {
					Log.d("content", line);
					result += line + "\n";
					if(line.contains("</div>")) {
						count++;
						if(count == 3){
							break;
						}
					}
				} else {
					if(line.contains("<div id=\"intro\">")) {
						count = 1;
					}
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
