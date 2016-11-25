package com.uom.cse.bbm.system;

import android.R.integer;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.uom.cse.bbm.system.R;

/**
 * 
 * @author T.Nila ask the donor details and save it to the database
 *
 */
public class DonorRegistration extends ActionBarActivity {
	private String contactNum, fname, bloodgroup, available, location;
	private int donorage;
	private String URL_NEW_PREDICTION = BbmConstants.HOSTNAME
			+ BbmConstants.CONTEXT_PATH + "new_predict.php";
	private Button btnAddPrediction, btnReadData;
	static String DBNAME = "php";
	static String TABLE = "donor_details";
	private boolean taskFinished = false;

	/**
	 * load the registration page of the donor
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donor_registration);
		btnAddPrediction = (Button) findViewById(R.id.donor_register);
		btnAddPrediction.setOnClickListener(new View.OnClickListener() {
			/*
			 * take the details from the donor
			 */
			public void onClick(View v) {
				Spinner edittext1 = (Spinner) findViewById(R.id.d_bloodgroup);
				Spinner edittext2 = (Spinner) findViewById(R.id.d_location);
				Spinner edittext3 = (Spinner) findViewById(R.id.d_Available);
				EditText edittext4 = (EditText) findViewById(R.id.d_firstname);
				EditText edittext5 = (EditText) findViewById(R.id.d_age);
				EditText edittext7 = (EditText) findViewById(R.id.phone_num);
				// check whether the required details given or not
				if (edittext4.getText().toString().trim().equals("")) {
					Toast.makeText(getApplicationContext(),
							"first name required!", Toast.LENGTH_SHORT).show();
					edittext4.setError("First name is required!");
					// You can Toast a message here that the Username is Empty
				}

				if (edittext7.getText().toString().trim().equals("")) {
					Toast t = Toast.makeText(getApplicationContext(),
							"contact number is required!", Toast.LENGTH_SHORT);
					edittext4.setError("First name is required!");
					// You can Toast a message here that the contact is Empty
				}

				if (edittext2.getSelectedItem().toString().trim().equals("")) {
					Toast t = Toast.makeText(getApplicationContext(),
							"location is requred!", Toast.LENGTH_SHORT);
					edittext4.setError("First name is required!");
					// You can Toast a message here that the location is Empty
				}

				if (edittext1.getSelectedItem().toString().trim().equals("")) {
					Toast t = Toast.makeText(getApplicationContext(),
							"blood group is required!", Toast.LENGTH_SHORT);
					edittext4.setError("First name is required!");
					// You can Toast a message here that the blood group is
					// Empty
				} else {

					bloodgroup = edittext1.getSelectedItem().toString();
					location = edittext2.getSelectedItem().toString();
					available = edittext3.getSelectedItem().toString();
					fname = edittext4.getText().toString();
					contactNum = edittext7.getText().toString();
					// TODO Auto-generated method stub

					new AddNewPrediction().execute(bloodgroup, location,
							available, fname, contactNum);
					while (!taskFinished) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					edittext4.setText("");
					edittext5.setText("");
					edittext1.setSelection(0);
					edittext2.setSelection(0);
					edittext3.setSelection(0);
					edittext7.setText("");

				}
			}
		});

	}

	/**
	 * 
	 * @param view
	 *            exit the current screen and go to the front screen
	 */
	public void close(View v) {
		Intent intent = new Intent(v.getContext(), UsersActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * 
	 * Asyntask for save the donor details in the database
	 *
	 */
	private class AddNewPrediction extends AsyncTask<String, Void, Void> {

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
			taskFinished = true;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}

}
