package com.uom.cse.bbm.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class UpdateDonor extends ActionBarActivity {
	private String URL_NEW_PREDICTION = "http://10.0.2.2/bbmsystem/updatedb.php";
	private String URL_ITEMS = "http://10.0.2.2/bbmsystem/get_data.php";
	private String contactNum, fname, bloodgroup, available, location;
	private boolean taskFinished = false;
	private static final String DONOR_DETAILS = "jresponse";
	private static final String B_GROUP = "blood_group";
	private static final String LOCATION = "location";
	private static final String STATUS = "status";
	private static final String NAME = "username";
	private static final String CONTACT = "contact";
	HashMap<String, String> donorDetail = new HashMap<String, String>();
	private ArrayList<HashMap<String, String>> donorDetailsList = new ArrayList<HashMap<String, String>>();
	private JSONArray bloodArray = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donor_update);

		EditText edittext1 = (EditText) findViewById(R.id.blgr);
		EditText edittext2 = (EditText) findViewById(R.id.avln);
		EditText edittext3 = (EditText) findViewById(R.id.status);
		EditText edittext4 = (EditText) findViewById(R.id.d_firstnameu);

		new GetData().execute();
		while (!taskFinished) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		edittext1.setText(donorDetail.get(B_GROUP));
		edittext2.setText(donorDetail.get(LOCATION));
		edittext3.setText(donorDetail.get(STATUS));
		edittext4.setText(donorDetail.get(NAME));

	}

	public void update(View v) {
		EditText edittext1 = (EditText) findViewById(R.id.blgr);
		EditText edittext2 = (EditText) findViewById(R.id.avln);
		EditText edittext3 = (EditText) findViewById(R.id.status);
		EditText edittext4 = (EditText) findViewById(R.id.d_firstnameu);

		bloodgroup = edittext1.getText().toString();
		location = edittext2.getText().toString();
		available = edittext3.getText().toString();
		fname = edittext4.getText().toString();
		new Donorupdate().execute(bloodgroup, location, available, fname,
				DonorUpdate.contactNum);
	}

	private class Donorupdate extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(String... arg) {
			// TODO Auto-generated method stub
			String blood_Group = arg[0];
			String loca_tion = arg[1];
			String avai_lable = arg[2];
			String username = arg[3];
			String contact = arg[4];
			// Preparing post params
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Blood_group", bloodgroup));
			params.add(new BasicNameValuePair("Location", location));
			params.add(new BasicNameValuePair("Available", available));
			params.add(new BasicNameValuePair("name", username));
			params.add(new BasicNameValuePair("Contact_number", contact));
			Servicehandler serviceClient = new Servicehandler();

			String json = serviceClient.makeServiceCall(URL_NEW_PREDICTION,
					Servicehandler.POST, params);

			Log.d("Create Prediction Request: ", "> " + json);

			if (json != null) {
				try {
					JSONObject jsonObj = new JSONObject(json);
					boolean error = jsonObj.getBoolean("error");
					// checking for error node in json
					if (!error) {
						// new category created successfully
						Log.e("Prediction added successfully ",
								"> " + jsonObj.getString("message"));
					} else {
						Log.e("Add Prediction Error: ",
								"> " + jsonObj.getString("message"));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				Log.e("JSON Data", "JSON data error!");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}

	private class GetData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg) {
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
					bloodArray = jsonObj.getJSONArray(DONOR_DETAILS);
					Log.d("json aray", "user point array");
					int len = bloodArray.length();
					Log.d("len", "get array length");
					for (int i = 0; i < bloodArray.length(); i++) {
						JSONObject c = bloodArray.getJSONObject(i);
						String bloodGroup = c.getString(B_GROUP);
						Log.d("Blood_group", bloodGroup);
						String location = c.getString(LOCATION);
						Log.d("Location", location);
						String donorName = c.getString(NAME);
						Log.d("name", donorName);
						String availableStatus = c.getString(STATUS);
						String contact = c.getString(CONTACT);
						// hashmap for single match
						if (contact.equalsIgnoreCase(DonorUpdate.contactNum)) {

							// adding each child node to HashMap key => value
							donorDetail.put(B_GROUP, bloodGroup);
							donorDetail.put(LOCATION, location);
							donorDetail.put(NAME, donorName);
							donorDetail.put(CONTACT, contact);
							donorDetail.put(STATUS, availableStatus);
							// donorDetailsList.add(donorDetail);
						}
					}
				} catch (JSONException e) {
					Log.d("catch", "in the catch");
					e.printStackTrace();
				}
			} else {
				Log.e("JSON Data", "Didn't receive any data from server!");
			}
			taskFinished = true;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

		}

	}

	public void close(View v) {
		Intent intent = new Intent(v.getContext(), UsersActivity.class);
		startActivityForResult(intent, 0);
	}

}
