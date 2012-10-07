package com.kos.reader;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;

public class LoginActivity extends Activity {

	String value;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {

				try {
					Document doc = Jsoup.connect("http://www.dailykos.com/login")
							.get();
					value = doc.getElementById("main").toString();
					
				} catch (IOException e) {

				}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				WebView tv = (WebView) findViewById(R.id.textView1);

				tv.loadDataWithBaseURL("http://www.dailykos.com", value,
						"text/html", "UTF-8", "about:blank");

			}

		}.execute("");
	}
}