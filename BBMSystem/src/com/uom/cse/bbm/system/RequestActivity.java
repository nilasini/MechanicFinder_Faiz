package com.uom.cse.bbm.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import android.R.integer;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.uom.cse.bbm.system.R;
import com.uom.cse.bbm.system.DonorRegistration;

public class RequestActivity extends ActionBarActivity {
	private String location, bloodgroup;
	private String[] directionurlArray;
	private String[] downloadedurlArray;
	private static String URL_ITEMS = BbmConstants.HOSTNAME
			+ BbmConstants.CONTEXT_PATH + "get_data.php";
	private static final String DONOR_DETAILS = "jresponse";
	private static String DONOR_NAME = "username";
	private static String CONTACT_NUM = "contact";
	private static String B_GROUP = "blood_group";
	private static String LOCATION = "location";
	private static String STATUS = "status";
	private static String DISTANCE = "distance";
	private boolean downloadurlFinished = false, getFixureFinished = false,
			downloadUrlTaskFinished = false;
	private JSONArray bloodArray = null;
	private static List<String> directionsurlList = new ArrayList<String>();
	private List<String> downloadedurlList = new ArrayList<String>();
	public static List<String> distanceList = new ArrayList<String>();
	private static ArrayList<HashMap<String, String>> availableDonorsList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchpage);

		Button btn1 = (Button) findViewById(R.id.req_donordetail);
		btn1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DonorDetail.class);
				startActivityForResult(intent, 0);
			}
		});
	}

	public void logout(View v) {
		SharedPreferences sharedpreferences = getSharedPreferences(
				OrganizationLoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.clear();
		editor.commit();
		Intent intent = new Intent(v.getContext(),
				OrganizationLoginActivity.class);
		startActivityForResult(intent, 0);
	}

	public void nearbyDonor(View v) {
		Intent intent = new Intent(v.getContext(), AvailableNearbyDonors.class);
		startActivityForResult(intent, 0);
	}

	public void onClick(View view) {

		// get the details which was given by the organization and assign it
		Spinner edittext1 = (Spinner) findViewById(R.id.searchd_location);
		Spinner edittext2 = (Spinner) findViewById(R.id.searchb_group);
		location = edittext1.getSelectedItem().toString();
		bloodgroup = edittext2.getSelectedItem().toString();

		//
		new GetFixture().execute();
		while (!getFixureFinished) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		directionurlArray = new String[directionsurlList.size()];
		directionurlArray = directionsurlList.toArray(directionurlArray);

		new DownloadURL().execute(directionurlArray);

		while (!downloadurlFinished) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		downloadedurlArray = new String[downloadedurlList.size()];
		downloadedurlArray = downloadedurlList.toArray(downloadedurlArray);
		new DownloadURLTask().execute(downloadedurlArray);

		while (!downloadUrlTaskFinished) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Intent intent = new Intent(view.getContext(), AvailableDonorTable.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * Get the direction URL
	 * 
	 * @param origin
	 *            starting point
	 * @param destination
	 *            the ending point
	 * @return url to get the directions between two locations
	 */
	private String getDirectionsUrl(String origin, String destination) {
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="
				+ origin
				+ "&destinations="
				+ destination
				+ "&mode=driving&language=English&key=AIzaSyAdmyjZoP2uTdD9stXlJPOjD4qAWDGmN74";

		return url;
	}

	/**
	 * A method to download json data from url
	 */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);
			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();
			// Connecting to url
			urlConnection.connect();
			// Reading data from url
			iStream = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			data = sb.toString();
			br.close();
		} catch (Exception e) {
			// Log.d("Exception while downloading url", e.toString());
			e.printStackTrace();
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	/**
	 * parse the json data to string
	 *
	 */
	private class DownloadURLTask extends AsyncTask<String, Void, String> {
		Object obj1, obj2, obj3, obj4, obj5;
		String dis;
		float distance;

		@Override
		protected String doInBackground(String... urls) {

			try {
				for (int i = 0; i < urls.length; i++) {
					// split the json array and get the first element
					JSONParser parser = new JSONParser();
					obj1 = parser.parse(urls[i]);
					org.json.simple.JSONArray jarray1 = (org.json.simple.JSONArray) obj1;
					org.json.simple.JSONObject jobj1 = (org.json.simple.JSONObject) jarray1
							.get(1);

					// split the json array element to get the value of distance
					obj2 = parser.parse((jobj1.get("rows")).toString());
					org.json.simple.JSONArray jarray2 = (org.json.simple.JSONArray) obj2;
					org.json.simple.JSONObject jobj2 = (org.json.simple.JSONObject) jarray2
							.get(0);

					// split the json array element to get the value of distance
					obj3 = parser.parse((jobj2.get("elements")).toString());
					org.json.simple.JSONArray jarray3 = (org.json.simple.JSONArray) obj3;
					org.json.simple.JSONObject jobj3 = (org.json.simple.JSONObject) jarray3
							.get(0);
					// split the json array element to get the value of distance
					obj4 = parser.parse((jobj3.get("distance")).toString());
					String[] sarr1 = obj4.toString().split("value");
					String[] sarr2 = sarr1[1].split("\\}");
					String[] sarr3 = sarr2[0].split(":");
					String[] sarr4 = sarr3[1].split(",");
					dis = sarr4[0];
					distance=(Float.parseFloat(dis))/1000;
					dis=String.valueOf(distance);
					HashMap<String, String> distancemap = new HashMap<String, String>();
					distancemap = availableDonorsList.remove(0);
					distancemap.put(DISTANCE, dis);
					availableDonorsList.add(distancemap);
					distanceList.add(dis);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			downloadUrlTaskFinished = true;
			return dis;
		}

		@Override
		protected void onPostExecute(String result) {

		}
	}

	/**
	 * get data from database and make available donors directions list
	 *
	 */
	private class GetFixture extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg) {
			availableDonorsList.clear();
			String directionUrl;
			// make a service call to the specified php and get the database
			// details as a json string
			Servicehandler serviceClient = new Servicehandler();
			Log.d("url: ", "> " + URL_ITEMS);
			String json = serviceClient.makeServiceCall(URL_ITEMS,
					Servicehandler.GET);
			// print the json response in the log
			Log.d("Get match fixture response: ", "> " + json);
			if (json != null) {
				try {

					JSONObject jsonObj = new JSONObject(json);
					Log.d("jsonObject", "new json Object");
					// Getting JSON Array node
					bloodArray = jsonObj.getJSONArray(DONOR_DETAILS);
					Log.d("json aray", "user point array");
					// get the number of rows for the donors
					int len = bloodArray.length();
					String[] distance1 = new String[len];
					Log.d("len", "get array length");
					// get the blood group and location details using string
					// array and assign it to a local variable
					for (int i = 0; i < bloodArray.length(); i++) {
						JSONObject c = bloodArray.getJSONObject(i);
						// get the blood group from database and assign it
						String blood_group = c.getString(B_GROUP);
						Log.d("Blood_group", blood_group);
						if (blood_group.equals(bloodgroup)) {
							// get the location from database and assign it to a
							// local variable
							String loctn = c.getString(LOCATION);
							Log.d("Location", loctn);
							String dusername = c.getString(DONOR_NAME);
							String dcontact = c.getString(CONTACT_NUM);
							String dstatus = c.getString(STATUS);
							if (!dstatus.equalsIgnoreCase("no")) {
								directionUrl = getDirectionsUrl(loctn, location);
								directionsurlList.add(directionUrl);
								HashMap<String, String> availableDonors = new HashMap<String, String>();
								availableDonors.put(B_GROUP, blood_group);
								availableDonors.put(LOCATION, loctn);
								availableDonors.put(DONOR_NAME, dusername);
								availableDonors.put(CONTACT_NUM, dcontact);
								availableDonorsList.add(availableDonors);

							}
						}

					}

				} catch (JSONException e) {
					Log.d("catch", "in the catch");
					e.printStackTrace();
				}
			} else {
				Log.e("JSON Data", "Didn't receive any data from server!");
			}
			getFixureFinished = true;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

		}

	}

	/**
	 * 
	 * download the specified url and return it
	 *
	 */
	private class DownloadURL extends AsyncTask<String, Void, String> {
		String ur = null;
		String downloadedUrl;

		@Override
		protected String doInBackground(String... urls) {
			for (int i = 0; i < urls.length; i++) {

				try {
					// download the specified url
					ur = downloadUrl(urls[i]);
					downloadedUrl = "[0," + ur + "]";
					downloadedurlList.add(downloadedUrl);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// assign as true to determine that the particular asyntask finished
			downloadurlFinished = true;
			return downloadedUrl;
		}

		@Override
		protected void onPostExecute(String result) {

		}
	}

	/**
	 * @return list of distances between two locations as string
	 */
	public List<String> getDistanceList() {
		return distanceList;
	}

	/**
	 * @param distanceList
	 *            list of distance between two locations as string
	 */
	public void setDistanceList(List<String> distanceList) {
		this.distanceList = distanceList;
	}

	/**
	 * @return available donor list of the specied blood group
	 */
	public ArrayList<HashMap<String, String>> getAvailableDonorsList() {
		return availableDonorsList;
	}

	/**
	 * 
	 * @param availableDonorsList
	 *            available donor list of the specied blood group
	 */

	public void setAvailableDonorsList(
			ArrayList<HashMap<String, String>> availableDonorsList) {
		this.availableDonorsList = availableDonorsList;
	}

}
