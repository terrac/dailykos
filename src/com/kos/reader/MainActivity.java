package com.kos.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.mcsoxford.rss.RSSReader;

import com.kos.utils.JSONSharedPreferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			final ListView listView = (ListView) findViewById(R.id.listView1);
			JSONArray ja=JSONSharedPreferences.loadJSONArray(getApplicationContext(), "main", "tags");
			if(ja.length() == 0){
				ja.put("Recommended");
				ja.put("Community");
				ja.put("Index");
				JSONSharedPreferences.saveJSONArray(getApplicationContext(), "main", "tags", ja);
			}
			listView.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, ja.join(" ").replace("\"", "").split(" ")));
			OnItemClickListener onClickListener = new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					String url = null;
					
					try {
						JSONArray ja=JSONSharedPreferences.loadJSONArray(getApplicationContext(), "main", "tags");
						url = "http://www.dailykos.com/rss/tag:"+ja.getString(position)+".xml";
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(!removeNext){
						removeif(listView,position);				
//						
						removeNext = false;
						return;
					}
									
//					if (position == 0) {
//						url = "http://www.dailykos.com/rss/tag:Recommended.xml";
//					}
//					if (position == 1) {
//						url = "http://www.dailykos.com/rss/tag:community.xml";
//					}
//
//					if (position == 2) {
//						url = "http://rss.dailykos.com/dailykos/index.xml";
//					}

					Intent myIntent = new Intent(MainActivity.this,
							TitlesActivity.class);
					myIntent.putExtra("url", url);
					MainActivity.this.startActivity(myIntent);
				}

				
			};
			listView.setOnItemClickListener(onClickListener);

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	boolean removeNext;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.tag_add) {
			
			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(
					R.layout.alert_dialog_textentry, null);
			new AlertDialog.Builder(MainActivity.this)
					.setTitle("Add Tag")
					.setView(textEntryView)
					.setPositiveButton("ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									
									JSONArray ja;
									try {
										ja = JSONSharedPreferences.loadJSONArray(getApplicationContext(), "main", "tags");
										ja.put(((TextView) textEntryView).getText());
										JSONSharedPreferences.saveJSONArray(getApplicationContext(), "main", "tags", ja);
									
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
										
									/* User clicked OK so do some stuff */
								}
							})
					.setNegativeButton("cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked cancel so do some stuff */
								}
							}).show();
		}
		if (item.getItemId() == R.id.tag_remove) {
			removeNext = !removeNext;
			if (removeNext) {
				Toast.makeText(getApplicationContext(),
						"Will remove the next item clicked on",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"No longer in remove mode", Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);

	}
	private void removeif(ListView arg1,int position) {
		
		try {
			JSONArray ja=JSONSharedPreferences.loadJSONArray(getApplicationContext(), "main", "tags");
			String[] sA= ja.join(" ").replace("\"", "").split(" ");
			List<String> l=new ArrayList(Arrays.asList(sA));
			l.remove(position);
			ja=new JSONArray(l);
			JSONSharedPreferences.saveJSONArray(getApplicationContext(), "main", "tags", ja);
			arg1.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, l.toArray(new String[0])));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
