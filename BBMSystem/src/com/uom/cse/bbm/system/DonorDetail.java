package com.uom.cse.bbm.system;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uom.cse.bbm.system.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

/**
 * 
 * @author T.Nilasini Give the registered donor details from database
 *
 */
public class DonorDetail extends ListActivity {
	private String URL_ITEMS = BbmConstants.HOSTNAME
			+ BbmConstants.CONTEXT_PATH + "get_data.php";
	private static final String DONOR_DETAILS = "jresponse";
	public static final String B_GROUP = "blood_group";
	public static final String LOCATION = "location";
	public static final String STATUS = "status";
	public static final String NAME = "username";
	public static final String CONTACT = "contact";
	JSONArray matchFixture = null;
	public static boolean isFinished = false;
	public static ArrayList<HashMap<String, String>> donorDetailsList = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donor_details);
		// Call Async task to get the match fixture
		new GetFixture().execute();
	}

	private class GetFixture extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg) {
			donorDetailsList.clear();
			Servicehandler serviceClient = new Servicehandler();
			Log.d("url: ", "> " + URL_ITEMS);
			String json = serviceClient.makeServiceCall(URL_ITEMS,
					Servicehandler.GET);
			// print the json response in the log
			Log.d("Get match fixture response: ", "> " + json);
			if (json != null) {
				try {
					Log.d("try", "in the try");
					JSONObject jsonObj = new JSONObject(json);
					Log.d("jsonObject", "new json Object");
					// Getting JSON Array node
					matchFixture = jsonObj.getJSONArray(DONOR_DETAILS);
					Log.d("json aray", "user point array");
					int len = matchFixture.length();
					Log.d("len", "get array length");
					for (int i = 0; i < matchFixture.length(); i++) {
						JSONObject c = matchFixture.getJSONObject(i);
						String bloodGroup = c.getString(B_GROUP);
						Log.d("Blood_group", bloodGroup);
						String location = c.getString(LOCATION);
						Log.d("Location", location);
						String donorName = c.getString(NAME);
						Log.d("name", donorName);
						String availableStatus = c.getString(STATUS);
						String contact = c.getString(CONTACT);
						// hashmap for single match
						if (!availableStatus.equalsIgnoreCase("no")) {

							HashMap<String, String> donorDetail = new HashMap<String, String>();
							// adding each child node to HashMap key => value
							donorDetail.put(B_GROUP, bloodGroup);
							donorDetail.put(LOCATION, location);
							donorDetail.put(NAME, donorName);
							donorDetail.put(CONTACT, contact);
							donorDetailsList.add(donorDetail);
						}
					}
				} catch (JSONException e) {
					Log.d("catch", "in the catch");
					e.printStackTrace();
				}
			} else {
				Log.e("JSON Data", "Didn't receive any data from server!");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			ListAdapter adapter = new SimpleAdapter(DonorDetail.this,
					donorDetailsList, R.layout.list_item, new String[] {
							B_GROUP, LOCATION, NAME }, new int[] {
							R.id.donor_bg, R.id.donor_l, R.id.donor_n });
			setListAdapter(adapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}