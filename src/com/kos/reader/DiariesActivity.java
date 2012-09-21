package com.kos.reader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

public class DiariesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_content_no_bar);
		WebView tv = (WebView) findViewById(R.id.textView1);


		String data = get(Uri.parse("http://www.dailykos.com/diaries"));

		data = data+R.string.strip_script;
		tv.loadDataWithBaseURL("http://www.dailykos.com", data, "text/html",
				"UTF-8", "about:blank");

		super.onCreate(savedInstanceState);
	}

	String error;

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
					result.append(line + "\n");
			}
			return result.toString();
		} catch (Throwable e) {
			error = "Unable to load";
		}
		return null;
	}

}
