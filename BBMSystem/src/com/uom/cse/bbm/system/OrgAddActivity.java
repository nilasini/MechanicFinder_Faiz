package com.uom.cse.bbm.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.uom.cse.bbm.system.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author T.Nila allow the admin to add the authorized users
 *
 */
public class OrgAddActivity extends Activity {
	private String URL_NEW_PREDICTION = BbmConstants.HOSTNAME
			+ BbmConstants.CONTEXT_PATH + "authorized_org.php";
	Button b1, b2, logout_but, exit_but;
	EditText edtOrgName, edtOrgLoct;
	String org_name, location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_addingpage);
		b1 = (Button) findViewById(R.id.orgadd);

		b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				edtOrgName = (EditText) findViewById(R.id.org_name);
				edtOrgLoct = (EditText) findViewById(R.id.org_address);
				org_name = edtOrgName.getText().toString();
				location = edtOrgLoct.getText().toString();
				new AddData().execute(org_name, location);
			}
		});
		// allow the user to logout the session

		logout_but = (Button) findViewById(R.id.org_logout);
		logout_but.setOnClickListener(new OnClickListener() {

			// logout the session
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences sharedpreferences = getSharedPreferences(
						AdminLoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.clear();
				editor.commit();
				Intent intent = new Intent(v.getContext(),
						AdminLoginActivity.class);
				startActivityForResult(intent, 0);
			}
		});

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

	/**
	 * 
	 * asyntask for save the organization details
	 *
	 */
	private class AddData extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... arg) {
			// TODO Auto-generated method stub
			// Preparing post params
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("org_name", arg[0]));
			params.add(new BasicNameValuePair("location", arg[1]));
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
}