package com.kos.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kos.utils.JSONSharedPreferences;

public class MainActivity extends Activity {

	String[] arr = new String[] { "http://www.dailykos.com/rss/tag:",
			"http://www.dailykos.com/user/",
			"http://rss.dailykos.com/dailykos/", "http://www.dailykos.com/rss/" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		checkVersion();

		try {
			final ListView listView = setListView();
			OnItemClickListener onClickListener = new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					String url = null;

					try {

						JSONObject jo = JSONSharedPreferences.loadJSONObject(
								getApplicationContext(), "main", "context");

						JSONArray ja = JSONSharedPreferences.loadJSONArray(
								getApplicationContext(), "main", "tags");

						String name = ja.getString(position).toLowerCase();
						url = jo.getString(name);
						if (url == null) {
							jo.put(name, arr[1] + name + ".xml");
							JSONSharedPreferences.saveJSONObject(
									getApplicationContext(), "main", "context",
									jo);
							url = jo.getString(name);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (removeNext) {
						removeif(listView, position);
						//
						removeNext = false;
						return;
					}

					// if (position == 0) {
					// url = "http://www.dailykos.com/rss/tag:Recommended.xml";
					// }
					// if (position == 1) {
					// url = "http://www.dailykos.com/rss/tag:community.xml";
					// }
					//
					// if (position == 2) {
					// url = "http://rss.dailykos.com/dailykos/index.xml";
					// }

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

	private void checkVersion() {
		// TODO Auto-generated method stub
		try {

			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);

			String strVersionCode = "Version Code: "
					+ String.valueOf(packageInfo.versionCode);

			String strVersionName = packageInfo.versionName;

			SharedPreferences preferences = getPreferences(MODE_PRIVATE);
			// if(preferences.getInt("versionNumber", packageInfo.versionCode)
			// != packageInfo.versionCode){
			// if(packageInfo.versionCode )
			// }
			// Editor e=preferences.edit();
			// e.putInt("versionNumber", packageInfo.versionCode);
			// e.commit();
			// if (!preferences.getBoolean("versionUpdate", false)) {
			// new AlertDialog.Builder(
			// MainActivity.this)
			// .setTitle(strVersionCode + "updates").setNeutralButton("Okay",
			// null).setMessage(R.string.);
			// Editor edit = preferences.edit();
			// edit.putBoolean("versionUpdate", true);
			// edit.commit();
			//
			// }

			if (packageInfo.versionCode == 9) {

				preferences = getPreferences(MODE_PRIVATE);
				if (!preferences.getBoolean("resetTags", false)) {
					JSONObject jo = JSONSharedPreferences.loadJSONObject(
							getApplicationContext(), "main", "context");
					jo = new JSONObject();
					JSONSharedPreferences.saveJSONObject(
							getApplicationContext(), "main", "context", jo);
					Editor edit = preferences.edit();
					edit.putBoolean("resetTags", true);
					edit.commit();

				}
			}

		} catch (Throwable e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

	public ListView setListView() throws JSONException {
		final ListView listView = (ListView) findViewById(R.id.listView1);
		JSONArray ja = JSONSharedPreferences.loadJSONArray(
				getApplicationContext(), "main", "tags");
		if (ja.length() == 0) {
			ja.put("Recommended");
			ja.put("Community");
			ja.put("Frontpage");
			ja.put("Rescued");
			ja.put("Recent");
			JSONSharedPreferences.saveJSONArray(getApplicationContext(),
					"main", "tags", ja);
			JSONObject jo = JSONSharedPreferences.loadJSONObject(
					getApplicationContext(), "main", "context");

			jo.put("recommended", arr[0] + "recommended.xml");
			jo.put("community", arr[0] + "community.xml");
			jo.put("rescued", arr[0] + "rescued.xml");
			jo.put("recent", arr[3] + "diary.xml");
			jo.put("frontpage", arr[2] + "index.xml");
			JSONSharedPreferences.saveJSONObject(getApplicationContext(),
					"main", "context", jo);

		}
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ja.join(" ")
						.replace("\"", "").split(" ")));
		return listView;
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

		}

		if (item.getItemId() == R.id.tag_add) {

			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(
					R.layout.alert_dialog_textentry, null);
			Spinner spinner = (Spinner) textEntryView
					.findViewById(R.id.rss_choices);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this, R.array.rss_types,
							android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);

			final Dialog alertDialog = new AlertDialog.Builder(
					MainActivity.this)
					.setTitle("Add Tag")
					.setView(textEntryView)
					.setPositiveButton("ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									JSONArray ja;
									try {

										ja = JSONSharedPreferences
												.loadJSONArray(
														getApplicationContext(),
														"main", "tags");
										CharSequence text = ((TextView) textEntryView
												.findViewById(R.id.alert_text_add_tag))
												.getText();
										ja.put(text);
										JSONSharedPreferences.saveJSONArray(
												getApplicationContext(),
												"main", "tags", ja);

										JSONObject jo = JSONSharedPreferences
												.loadJSONObject(
														getApplicationContext(),
														"main", "context");
										Spinner spinner = (Spinner) textEntryView
												.findViewById(R.id.rss_choices);
										if (spinner.getSelectedItemPosition() == 2) {
											jo.put(text.toString(),
													arr[spinner
															.getSelectedItemPosition()]
															+ text.toString()
															+ ".xml");

										} else {
											jo.put(text.toString(),
													arr[spinner
															.getSelectedItemPosition()]
															+ text.toString()
															+ "/rss.xml");

										}
										JSONSharedPreferences.saveJSONObject(
												getApplicationContext(),
												"main", "context", jo);
										setListView();
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
		if (item.getItemId() == R.id.reset) {

			JSONArray ja;
			try {
				ja = JSONSharedPreferences.loadJSONArray(
						getApplicationContext(), "main", "tags");

				ja = new JSONArray();
				JSONSharedPreferences.saveJSONArray(getApplicationContext(),
						"main", "tags", ja);

				setListView();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (item.getItemId() == R.id.browser) {
			runMini(null);
		}
		return super.onOptionsItemSelected(item);

	}

	private void removeif(ListView arg1, int position) {

		try {
			JSONArray ja = JSONSharedPreferences.loadJSONArray(
					getApplicationContext(), "main", "tags");
			String[] sA = ja.join(" ").replace("\"", "").split(" ");
			List<String> l = new ArrayList(Arrays.asList(sA));
			String removed = l.remove(position).toLowerCase();
			ja = new JSONArray(l);
			JSONSharedPreferences.saveJSONArray(getApplicationContext(),
					"main", "tags", ja);
			arg1.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, l
							.toArray(new String[0])));

			if (!(removed.equals("frontpage") || (removed.equals("recent")))) {
				JSONObject jo = JSONSharedPreferences.loadJSONObject(
						getApplicationContext(), "main", "context");
				jo.remove(removed);
				JSONSharedPreferences.saveJSONObject(getApplicationContext(),
						"main", "context", jo);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void runMini(View view) {
		Intent myIntent = new Intent(MainActivity.this, DiariesActivity.class);
		MainActivity.this.startActivity(myIntent);

	}
}
