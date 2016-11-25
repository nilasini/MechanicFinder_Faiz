package com.uom.cse.bbm.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uom.cse.bbm.system.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class OrganizationRegistration extends Activity {
	private String URL_NEW_PREDICTION = BbmConstants.HOSTNAME
			+ BbmConstants.CONTEXT_PATH + "save_org.php";
	private String URL_ITEMS = BbmConstants.HOSTNAME
			+ BbmConstants.CONTEXT_PATH + "get_authorizedorg.php";
	private static final String Details = "jresponse";
	private static final String ORG_NAME = "org_name";
	private static final String LOCATION = "location";
	private Button btnRegister, btnVarify;
	private String username, password, code, phone_num, email, org_name;
	JSONArray matchFixture = null;
	private boolean isAuthorizedname = false, isFinished = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_registration);

		btnVarify = (Button) findViewById(R.id.varify);
		btnVarify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (org_name.equals("")) {
					new AlertDialog.Builder(OrganizationRegistration.this)
							.setTitle("warning")
							.setMessage("please enter an organization name")
							.setPositiveButton(android.R.string.yes,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();

				} else {
					String message = "please add our organization " + org_name
							+ " as an authorized organization";
					String number = "5556";
					SmsManager manager = SmsManager.getDefault();
					manager.sendTextMessage(number, null, message, null, null);
					Toast.makeText(getApplicationContext(),
							"send successfully", Toast.LENGTH_LONG).show();
				}

			}
		});

		btnRegister = (Button) findViewById(R.id.register1);
		btnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText eTun = (EditText) findViewById(R.id.username1);
				EditText eTpwd = (EditText) findViewById(R.id.pwd1);
				EditText eTmail = (EditText) findViewById(R.id.mail_id1);
				EditText eTcode = (EditText) findViewById(R.id.code1);
				EditText eTorgname = (EditText) findViewById(R.id.org_name1);
				EditText eTphone = (EditText) findViewById(R.id.phonenumber1);

				username = eTun.getText().toString();
				password = eTpwd.getText().toString();
				email = eTmail.getText().toString();
				code = eTcode.getText().toString();
				org_name = eTorgname.getText().toString();
				phone_num = eTphone.getText().toString();
				isFinished = false;
				if (username.trim().equals("")) {
					eTun.setError("User name is required!");
					// You can Toast a message here that the Username is Empty
				}

				if (password.trim().equals("")) {
					Toast.makeText(getApplicationContext(),
							"password is required!", Toast.LENGTH_SHORT).show();
					eTpwd.setError("password is required!");
					// You can Toast a message here that the Username is Empty
				}

				if (code.trim().equals("")) {
					Toast.makeText(getApplicationContext(),
							"code is required!", Toast.LENGTH_SHORT).show();
					eTcode.setError("code is required!");
					// You can Toast a message here that the Username is Empty
				}
				if (org_name.trim().equals("")) {
					Toast.makeText(getApplicationContext(),
							"org_name is required!", Toast.LENGTH_SHORT).show();
					eTorgname.setError("org_name is required!");
					// You can Toast a message here that the Username is Empty
				}
				if (phone_num.trim().equals("")) {
					Toast.makeText(getApplicationContext(),
							"phone number is required!", Toast.LENGTH_SHORT)
							.show();
					eTphone.setError("phone number is required!");
					// You can Toast a message here that the Username is Empty
				} else {

					new GetAuthorizedOrg().execute();
					while (!isFinished) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if (isAuthorizedname) {

						new AddData().execute(phone_num, email, code, org_name,
								password, username);
					} else {
						new AlertDialog.Builder(OrganizationRegistration.this)
								.setTitle("error")
								.setMessage(
										"please click on the verify button to validate ur organization")
								.setPositiveButton(android.R.string.yes,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();
											}
										})
								.setIcon(android.R.drawable.ic_dialog_alert)
								.show();

					}

				}
			}
		});

	}

	public void exit(View v) {
		Intent intent = new Intent(v.getContext(), UsersActivity.class);
		startActivityForResult(intent, 0);
	}

	private class GetAuthorizedOrg extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg) {

			String orgname, loct;

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
					matchFixture = jsonObj.getJSONArray(Details);
					Log.d("json aray", "user point array");
					int len = matchFixture.length();
					Log.d("len", "get array length");
					for (int i = 0; i < matchFixture.length(); i++) {
						JSONObject c = matchFixture.getJSONObject(i);
						orgname = c.getString(ORG_NAME);
						Log.d("orgname", orgname);
						if (org_name.trim().equalsIgnoreCase(orgname)) {
							isAuthorizedname = true;
						}

					}
				} catch (JSONException e) {
					Log.d("catch", "in the catch");
					e.printStackTrace();
				}
			} else {
				Log.e("JSON Data", "Didn't receive any data from server!");
			}
			isFinished = true;
			return null;
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

		}
	}

	private class AddData extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(String... arg) {
			// TODO Auto-generated method stub
			// Preparing post params
			String cn, mail, code, orgname, pwd, un;
			cn = arg[0];
			mail = arg[1];
			code = arg[2];
			orgname = arg[3];
			pwd = arg[4];
			un = arg[5];
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("contactnumber", cn));
			params.add(new BasicNameValuePair("email_id", mail));
			params.add(new BasicNameValuePair("organizationbbcode", code));
			params.add(new BasicNameValuePair("organization_name", orgname));
			params.add(new BasicNameValuePair("password", pwd));
			params.add(new BasicNameValuePair("username", un));

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
