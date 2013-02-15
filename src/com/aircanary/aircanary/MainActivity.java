package com.aircanary.aircanary;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.content.Context;
import android.util.Log;

public class MainActivity extends Activity
{
	
    /**
     * Site data
     */
    ArrayList<HashMap<String,String>> siteData = new ArrayList<HashMap<String,String>>();
	
    /**
     * Context
     */
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_table);
        
        mContext = this;
        //SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.action_list,
        //        android.R.layout.simple_spinner_dropdown_item);
        
        //reloadFeatured();
        //reloadSites();
        
        //ActionBar actionBar = getActionBar();
        
        //ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
        	  // Get the same strings provided for the drop-down's ArrayAdapter
        	  //String[] strings = getResources().getStringArray(R.array.action_list);

        	  //@Override
        	  //public boolean onNavigationItemSelected(int position, long itemId) {
        		  /*
        	    // Create new fragment from our own Fragment class
        	    ListContentFragment newFragment = new ListContentFragment();
        	    FragmentTransaction ft = openFragmentTransaction();
        	    // Replace whatever is in the fragment container with this fragment
        	    //  and give the fragment a tag name equal to the string at the position selected
        	    ft.replace(R.id.fragment_container, newFragment, strings[position]);
        	    // Apply changes
        	    ft.commit();
        	    */
        	    //return true;
        	    
        	  //}
        	//};
        
    }
    
    private void reloadCurrent()
    {
        //TextView tv = (TextView) findViewById(R.id.CurrentPm);
        //tv.setText("--");
        new GetCurrentJsonTask().execute("slc");
    }
    
    private void reloadSites()
    {

    	TableLayout tl = (TableLayout) findViewById(R.id.SiteTable);
    	//tl.removeViews(1, tl.getChildCount() - 1);
    	//tl.getChildCount();
    	tl.removeAllViews();
        new GetSitesJsonTask().execute("all");
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
        
        reloadCurrent();
        //reloadSites();
    }
    
    /**
     * Asynchronous get JSON task
     */
    private class GetCurrentJsonTask extends AsyncTask<String, Void, String> {
        
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
            loadCurrent(result);
        }
    }
    
    /**
     * Load data from JSON string result
     */
    public void loadCurrent(String result) {
    
        try {
          
            // Convert result into JSONArray
        	JSONObject resultObject = new JSONObject(result);
        	
        	//TextView tvLocation = (TextView) findViewById(R.id.Location);
        	//tvLocation.setText(resultObject.getString("name"));
        	
            JSONArray featuredData = resultObject.getJSONArray("data");
            
            JSONObject sampleObject = featuredData.getJSONObject(0);
            
            TextView tvCurrentPm = (TextView) findViewById(R.id.PmValue);
            tvCurrentPm.setText(sampleObject.getString("pm25"));
            
            TextView tvCurrentOzone = (TextView) findViewById(R.id.OzValue);
            tvCurrentOzone.setText(sampleObject.getString("ozone"));
            
            TextView tvCurrentTemp = (TextView) findViewById(R.id.TempValue);
            String temperature = sampleObject.getString("temperature");
            if (temperature.equalsIgnoreCase("None")) {
            	temperature = "n/a";
            }
            tvCurrentTemp.setText(temperature);
            
            TextView tvCurrentWind = (TextView) findViewById(R.id.WindValue);
            tvCurrentWind.setText(sampleObject.getString("wind_speed"));
            
            TextView tvObserved = (TextView) findViewById(R.id.ObservedValue);
            tvObserved.setText(sampleObject.getString("observed"));
          
        } catch (JSONException e) {
          
        	Log.w("AC", e.getMessage());
        	Toast.makeText(mContext, "An error occurred while retrieving area data", Toast.LENGTH_SHORT).show();
          
        }
    
    }
    
    /**
     * Asynchronous get JSON task
     */
    private class GetSitesJsonTask extends AsyncTask<String, Void, String> {
        
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
            loadSites(result);
        }
    }
    
    /**
     * Load data from JSON string result
     */
    public void loadSites(String result) {
    
        try {
          
            // Convert result into JSONArray
            JSONArray json = new JSONArray(result);
          
            TableLayout siteTable = (TableLayout) findViewById(R.id.SiteTable);
            siteTable.removeAllViews();
            
            TableRow siteHeaderRow = (TableRow) View.inflate(mContext, R.layout.site_header_row, null);
            siteTable.addView(siteHeaderRow);
            
            // Loop over JSONarray
            for (int i = 0; i < json.length(); i++) {
            
                // Get JSONObject from current array element
                JSONObject siteObject = json.getJSONObject(i);
                
                String code = siteObject.getString("code");
                String name = siteObject.getString("name");
                
                JSONArray airData = siteObject.getJSONArray("data");
                String pm25 = airData.getJSONObject(0).getString("pm25");
                String ozone = airData.getJSONObject(0).getString("ozone");
                
                Log.w("AC", code);
                
                TableRow siteRow = (TableRow) View.inflate(mContext, R.layout.site_row, null);
                TextView tv = (TextView) siteRow.findViewById(R.id.name);
                tv.setText(name);
                
                TextView tv2 = (TextView) siteRow.findViewById(R.id.pm25);
                tv2.setText(pm25);
                
                TextView tvOzone = (TextView) siteRow.findViewById(R.id.Ozone);
                tvOzone.setText(ozone);
                
                siteTable.addView(siteRow);
                
            }
          
        } catch (JSONException e) {
          
        	Log.w("AC", e.getMessage());
        	Toast.makeText(mContext, "An error occurred while retrieving area data", Toast.LENGTH_SHORT).show();
          
        }
    
        // Create simple adapter using hashmap
        /*
        SimpleAdapter areas = new SimpleAdapter(
            this,
            siteData,
            R.layout.site_row,
            new String[] { "name"},
            new int[] { R.id.name }
        );
        */
        /*
        TableLayout siteTable = (TableLayout) findViewById(R.id.SiteTable);
        siteTable.removeAllViews();
        
        TableRow siteRow = (TableRow) View.inflate(mContext, R.layout.site_row, null);
        
        siteTable.addView(siteRow);
        */
        
      
        //setListAdapter(areas);
    
      //ListView lv = getListView();
      //lv.setTextFilterEnabled(true);
      
      // Populate empty row in case we didn't find any areas
      //emptyView.setText(noneText);
      
      // Set on item click listener for states
      //lv.setOnItemClickListener(new OnItemClickListener() {
      
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
