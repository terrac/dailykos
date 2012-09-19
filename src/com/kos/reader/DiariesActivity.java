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
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuCompat;
import android.text.Editable;
import android.text.Html;
import android.text.Html.TagHandler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class DiariesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_content_no_bar);
		WebView tv = (WebView) findViewById(R.id.textView1);

		String ignoreExtra = "<script type=\\\"text/javascript\\\">" +
				"$(\".collapsable\").hide()" +
				"</script>";
		String data = get(Uri.parse("http://www.dailykos.com/diaries"));

		data = ignoreExtra+data;
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
				if (count > 2) {

					if (line.contains("<li id=\"c2\">")) {
						count = 4;
					}
					if (count == 4) {
						// Log.d("content", line);
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
		} catch (Throwable e) {
			error = "Unable to load";
		}
		return null;
	}

}
