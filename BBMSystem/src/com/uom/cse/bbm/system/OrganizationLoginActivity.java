package com.uom.cse.bbm.system;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uom.cse.bbm.system.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 
 * @author T.Nilasini allow the organization to login
 *
 */
public class OrganizationLoginActivity extends Activity {
	private String URL_ITEMS = BbmConstants.HOSTNAME
			+ BbmConstants.CONTEXT_PATH + "get_orgpwd.php";
	private static final String Details = "jresponse";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	public static final String MyPREFERENCES = "MyPrefs";
	private static String u_name, passwrd;
	SharedPreferences sharedpreferences;
	public static final String Name = "nameKey";
	public static final String Password = "passwordkey";
	JSONArray matchFixture = null;
	ArrayList<HashMap<String, String>> matchFixtureList = new ArrayList<HashMap<String, String>>();
	Button login_btn, signup_btn;
	EditText ed_acu, ed_acp;
	boolean isFinished = true;
	int counter = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_loginpage);

		login_btn = (Button) findViewById(R.id.org_login);
		signup_btn = (Button) findViewById(R.id.org_signup);
		ed_acu = (EditText) findViewById(R.id.org_username);
		ed_acp = (EditText) findViewById(R.id.org_password);
		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
		// allow to login to the page
		login_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isWorked = false;
				new GetPassword().execute();
				while (isFinished) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				SharedPreferences.Editor editor = sharedpreferences.edit();

				editor.putString(Name, u_name);
				editor.putString(Password, passwrd);
				editor.commit();
				// check for the user name and password and give error warnings

				if ((ed_acu.getText().toString().equals(""))
						&& (ed_acp.getText().toString().equals(""))) {
					isWorked = true;
					new AlertDialog.Builder(OrganizationLoginActivity.this)
							.setTitle("warning")
							.setMessage(
									"please enter the username and password")
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
					if (ed_acu.getText().toString().equals(u_name)) {
						if (ed_acp.getText().toString().equals(passwrd)) {
							isWorked = true;
							Intent intent = new Intent(v.getContext(),
									RequestActivity.class);
							startActivityForResult(intent, 0);

						} else if (ed_acp.getText().toString().trim()
								.equals("")) {
							isWorked = true;
							new AlertDialog.Builder(
									OrganizationLoginActivity.this)
									.setTitle("warning")
									.setMessage("please enter the password")
									.setPositiveButton(
											android.R.string.yes,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.cancel();
												}
											})
									.setIcon(android.R.drawable.ic_dialog_alert)
									.show();

						} else {
							isWorked = true;
							new AlertDialog.Builder(
									OrganizationLoginActivity.this)
									.setTitle("warning")
									.setMessage("wrong password")
									.setPositiveButton(
											android.R.string.yes,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.cancel();
												}
											})
									.setIcon(android.R.drawable.ic_dialog_alert)
									.show();
							ed_acp.setText("");

						}
					} else if (!isWorked) {
						isWorked = true;
						new AlertDialog.Builder(OrganizationLoginActivity.this)
								.setTitle("warning")
								.setMessage("wrong username and pssword")
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
						ed_acu.setText("");
						ed_acp.setText("");

					}
				}

			}
		});

		// allow the organizations to register

		signup_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						OrganizationRegistration.class);
				startActivityForResult(intent, 0);

			}
		});

	}

	/**
	 * 
	 * asyntask for get the particular username's password
	 *
	 */
	private class GetPassword extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg) {
			boolean isValidusername = false;
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
						u_name = c.getString(USERNAME);
						Log.d("Blood_group", u_name);
						if (ed_acu.getText().toString().equals(u_name)) {
							passwrd = c.getString(PASSWORD);

							Log.d("Location", passwrd);
							isValidusername = true;
						}

					}
					// check the username for validate
					if (!isValidusername) {
						Toast.makeText(getApplicationContext(),
								"wrong username", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					Log.d("catch", "in the catch");
					e.printStackTrace();
				}
			} else {
				Log.e("JSON Data", "Didn't receive any data from server!");
			}
			isFinished = false;
			return null;
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

		}
	}

	// getter and setter for private variables

	public static String getU_name() {
		return u_name;
	}

	public static void setU_name(String u_name) {
		OrganizationLoginActivity.u_name = u_name;
	}

}
