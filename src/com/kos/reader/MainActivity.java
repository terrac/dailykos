package com.kos.reader;

import org.mcsoxford.rss.RSSReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        RSSReader reader = new RSSReader();
        String uri = "http://www.dailykos.com/rss/tag:Recommended.xml";
        try {
			final ListView listView = (ListView) findViewById(R.id.listView1);

	        listView.setAdapter(new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, new String[]{"Recommended","Community","Main"}));
			OnItemClickListener onClickListener = new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					String url = null;
					if(position == 0){
						url = "http://www.dailykos.com/rss/tag:Recommended.xml";
					}
					if(position == 1){
						url = "http://www.dailykos.com/rss/tag:community.xml";
					}
					
					if(position == 2){
						url = "http://rss.dailykos.com/dailykos/index.xml";
					}
					
					
					Intent myIntent = new Intent(MainActivity.this, TitlesActivity.class);
	    			myIntent.putExtra("url",url);
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

    
}
