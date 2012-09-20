package com.kos.reader;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;
import org.mcsoxford.rss.RSSReaderException;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TitlesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titles);
        

        
    }
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
        new AsyncTask<String, Void, String>() {
        	RSSFeed feed;
        	boolean error;
        	@Override
        	protected String doInBackground(String... params) {
        		RSSReader reader = new RSSReader();
                String uri = getIntent().getStringExtra("url");
                try {
					feed = reader.load(uri);
				} catch (RSSReaderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Throwable t){
					error=true;
					
				}
        					
        		return null;
        	}
        	
        	@Override
        	protected void onPostExecute(String result) {
        		if(error){
        			String uri = getIntent().getStringExtra("url");
                    String text = "Cannot currently access "+uri+" Please Check your internet connection or the tag if you have set it";
					String title = "Error";
					new AlertDialog.Builder(TitlesActivity.this).setTitle(title).setMessage(text).setNeutralButton("Close", null).show();
					//
					error=false;
					return;
        		}
        		final ListView listView = (ListView) findViewById(R.id.listView1);

        	        listView.setAdapter(new ArrayAdapter<RSSItem>(TitlesActivity.this,  android.R.layout.simple_list_item_1, feed.getItems().toArray(new RSSItem[0])));
        			OnItemClickListener onClickListener = new OnItemClickListener() {
        				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
        						long arg3) {
        					
        					RSSItem o=(RSSItem) arg0.getItemAtPosition(position);
        	            	Intent myIntent = new Intent(TitlesActivity.this, ContentActivity.class);
        	    			myIntent.putExtra("content",o.getDescription());
        	    			myIntent.putExtra("uri", o.getLink());
        	    			TitlesActivity.this.startActivity(myIntent);
        				}
        	        };
        	        listView.setOnItemClickListener(onClickListener);
        		
        			
                }
		}.execute("");

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.activity_titles, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (R.id.menu_preferences == item.getItemId()) {
			Intent myIntent = new Intent(TitlesActivity.this, PrefsFragment.class);
			TitlesActivity.this.startActivity(myIntent);
		}
		return super.onOptionsItemSelected(item);
	}
    
}
