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
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/*
 * find the nearby donors and list down the donors
 */
public class AvailableNearbyDonors extends ListActivity {
	private String distanceArray[];
	private List<String> pNumber = new ArrayList<String>();
	private ArrayList<HashMap<String, String>> nearbyDonorsList = new ArrayList<HashMap<String, String>>();
	RequestActivity sa = new RequestActivity();

	/*
	 *  show the nearby donor list
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_list);

		distanceArray = new String[RequestActivity.distanceList.size()];
		distanceArray = RequestActivity.distanceList.toArray(distanceArray);
		addNearbyDonors(distanceArray);
		Button bSend = (Button) findViewById(R.id.sendsms);
		bSend.setOnClickListener(new OnClickListener() {

			/**
			 * send sms to the nearby donors
			 */
			@Override
			public void onClick(View v) {
				SMSService sms = new SMSService();
				String message = "your blood is need for us,if you available to donate please contact us";
				sms.sendSMS(pNumber, message);
				Toast.makeText(getApplicationContext(), "send successfully",
						Toast.LENGTH_LONG).show();
			}
		});

	}

	/**
	 * 
	 * @param view
	 *    exit the current screen and go to the front screen
	 */
	public void exit(View v) {
		Intent intent = new Intent(v.getContext(), UsersActivity.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * 
	 * @param distance
	 *            -distance between the organization and donors Add the nearby
	 *            donors details to a list
	 */
	public void addNearbyDonors(String[] distance) {
		NearbyDonor near = new NearbyDonor();
		String minDis = null;
		List<Long> distanceInLong = new ArrayList<Long>();
		distanceInLong = near.findNearbyDonors(distance);
		int x = 0;
		//check whether there are more then one available places to compare
		if (distanceInLong.size() > 1) {
			
			if (distanceInLong.get(0).equals(distanceInLong.get(1))) {
				minDis = distanceInLong.get(0).toString();
				HashMap<String, String> availableDonors = new HashMap<String, String>();
				for (int y = 0; y < distance.length; y++) {
					availableDonors = sa.getAvailableDonorsList().get(y);
					if ((availableDonors.containsValue(minDis))) {
						String s = availableDonors.get("username");
						String loct = availableDonors.get("location");
						String phone_num = availableDonors.get("contact");
						HashMap<String, String> nearDonor = new HashMap<String, String>();
						nearDonor.put("username", s);
						nearDonor.put("location", loct);
						nearDonor.put("contact", phone_num);
						pNumber.add(phone_num);
						//add the nearby donors to to a list
						nearbyDonorsList.add(nearDonor);
					}

				}
			}

		}

		else {
			minDis = distanceInLong.get(x).toString();
			HashMap<String, String> availableDonors = new HashMap<String, String>();
			for (int y = 0; y < distance.length; y++) {
				availableDonors = sa.getAvailableDonorsList().get(y);
				if ((availableDonors.containsValue(minDis))) {
					String s = availableDonors.get("username");
					String loct = availableDonors.get("location");
					String phone_num = availableDonors.get("contact");
					HashMap<String, String> nearDonor = new HashMap<String, String>();
					nearDonor.put("username", s);
					nearDonor.put("location", loct);
					nearDonor.put("contact", phone_num);
					pNumber.add(phone_num);
					nearbyDonorsList.add(nearDonor);
				}

			}

		}

		// show the nearby donors details as a table
		ListAdapter adapter1 = new SimpleAdapter(AvailableNearbyDonors.this,
				nearbyDonorsList, R.layout.nearby_donors, new String[] {
						"location", "username", "contact" }, new int[] {
						R.id.nearby_location, R.id.nearby_name,
						R.id.nearby_phone

				});
		setListAdapter(adapter1);

	}

}
