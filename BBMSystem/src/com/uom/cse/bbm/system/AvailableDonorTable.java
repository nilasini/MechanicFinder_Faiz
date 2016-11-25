package com.uom.cse.bbm.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.view.View.OnClickListener;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uom.cse.bbm.system.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/* 
 * This class will find the available donors for the specified blood group
 *  which was given by the organization
 */
public class AvailableDonorTable extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.available_list);
		availableList();

//		Button nearby = (Button) findViewById(R.id.nearby_donor);
//		nearby.setOnClickListener(new OnClickListener() {
//
//			/*
//			 * this is an onclick method for finding the nearby donors
//			 */
//			@Override
//			public void onClick(View v) {
//
//				Intent intent = new Intent(v.getContext(),
//						AvailableNearbyDonors.class);
//				startActivityForResult(intent, 0);
//
//			}
//		});
	}

	/**
	 * 
	 * @param view
	 *            exit the current screen and go to the front screen
	 */
	public void exit(View v) {
		Intent intent = new Intent(v.getContext(), UsersActivity.class);
		startActivityForResult(intent, 0);
	}

	/*
	 * This will show the list of available donors for the specified blood group
	 */
	public void availableList() {
		RequestActivity sea = new RequestActivity();

		ListAdapter adapter = new SimpleAdapter(AvailableDonorTable.this,
				sea.getAvailableDonorsList(), R.layout.available_donors,
				new String[] { "blood_group", "location", "username",
						"contact", "distance" }, new int[] { R.id.avai_bg,
						R.id.avai_location, R.id.avai_dname, R.id.p_num,
						R.id.avai_distance });
		setListAdapter(adapter);
	}

}
