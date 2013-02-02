package com.aircanary.aircanary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.content.Context;
import android.util.Log;

public class MainActivity extends Activity
{
	
    /**
     * Context
     */
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mContext = this;
        
        reload();
        
    }
    
    private void reload()
    {
        TextView tv = (TextView) findViewById(R.id.CurrentPm);
        tv.setText("--");
        new GetJsonTask().execute("slc");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onResume()
    {
        super.onResume();
        
        // If tempUnit has changed, refresh
        reload();
    }
    
    /**
     * Asynchronous get JSON task
     */
    private class GetJsonTask extends AsyncTask<String, Void, String> {
        
        /**
         * Execute in background
         */
        protected String doInBackground(String... args) {
            
              AcApi api = new AcApi(mContext);
              return api.getJson(args[0]);

        }
        
        /**
         * After execute (in UI thread context)
         */
        protected void onPostExecute(String result)
        {
        	Log.w("AC", result);
            loadData(result);
        }
    }
    
    /**
     * Load data from JSON string result
     */
    public void loadData(String result) {
    
        try {
          
            // Convert result into JSONArray
            JSONArray json = new JSONArray(result);
          
            // Loop over JSONarray
            for (int i = 0; i < json.length(); i++) {
            
                // Get JSONObject from current array element
                JSONObject sampleObject = json.getJSONObject(i);
                
                String pm25 = sampleObject.getString("pm25");
                TextView tv = (TextView) findViewById(R.id.CurrentPm);
                tv.setText(pm25);
                //tv.setBackgroundColor(color);
                
                /*
                // Add state to hashmap
                HashMap<String, String> map = new HashMap<String,String>();
                map.put("areaId", areaId);
                map.put("name", name);
                areaList.add(map);
                */
            }
          
        } catch (JSONException e) {
          
        	Toast.makeText(mContext, "An error occurred while retrieving area data", Toast.LENGTH_SHORT).show();
          
        }
    
        /*
        // Create simple adapter using hashmap
        SimpleAdapter areas = new SimpleAdapter(
            this,
            areaList,
            R.layout.list_row,
            new String[] { "name"},
            new int[] { R.id.name }
        );
      
      setListAdapter(areas);
    
      ListView lv = getListView();
      lv.setTextFilterEnabled(true);
      
      // Populate empty row in case we didn't find any areas
      emptyView.setText(noneText);
      
      // Set on item click listener for states
      lv.setOnItemClickListener(new OnItemClickListener() {
      */
          /**
           * On item click action, open area activity
           */
        /*
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              
              // Get object from item position
              Object item = parent.getItemAtPosition(position);
              HashMap<String, String> hashMap = (HashMap<String, String>) item;
              String str = hashMap.get("areaId"); // id
              String areaName = hashMap.get("name");
        
              Intent i = new Intent(getApplicationContext(), AreaActivity.class);
              i.putExtra("areaId", str);
              i.putExtra("name", areaName);
              startActivity(i);
          }
          */
          
      //});
      
      //dialog.hide();
      
    }
    
}
