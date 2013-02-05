package com.aircanary.aircanary;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SiteGrid extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_site_grid);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_site_grid, menu);
		return true;
	}

}
