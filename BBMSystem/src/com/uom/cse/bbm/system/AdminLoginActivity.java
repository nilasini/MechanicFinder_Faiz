package com.uom.cse.bbm.system;

import com.uom.cse.bbm.system.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author T.Nilasini Login class for admin
 *
 */
public class AdminLoginActivity extends Activity {
	SharedPreferences sharedpreferences;
	public static final String Name = "nameKey";
	public static final String Password = "passwordkey";
	public static final String MyPREFERENCES = null;
	Button b1, b2;
	EditText edu, edp;

	TextView tx1;
	int counter = 3;

	/**
	 * load the login page
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_login_page);

		b1 = (Button) findViewById(R.id.ad_login);
		edu = (EditText) findViewById(R.id.ad_username);
		edp = (EditText) findViewById(R.id.ad_password);
		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		/**
		 * check the username and password and give warning messages
		 */
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isWorked = false;
				if ((edu.getText().toString().equals(""))
						&& (edp.getText().toString().equals(""))) {
					new AlertDialog.Builder(AdminLoginActivity.this)
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
					if (edu.getText().toString().equals("admin")) {
						if (edp.getText().toString().equals("admin")) {
							isWorked = true;
							Intent intent = new Intent(v.getContext(),
									AdminActivity.class);
							startActivityForResult(intent, 0);
							SharedPreferences.Editor editor = sharedpreferences
									.edit();

							editor.putString(Name, edu.getText().toString());
							editor.putString(Password, edp.getText().toString());
							editor.commit();

						} else if (edp.getText().toString().trim().equals("")) {
							isWorked = true;
							new AlertDialog.Builder(AdminLoginActivity.this)
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
							new AlertDialog.Builder(AdminLoginActivity.this)
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
							edp.setText("");

						}
					} else if (!isWorked) {
						isWorked = true;
						new AlertDialog.Builder(AdminLoginActivity.this)
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
						edu.setText("");
						edp.setText("");

					}
				}

			}
		});

	}

	/**
	 * 
	 * @param view
	 *            exit from the current screen and go to the backward screen
	 */

	public void back(View v) {
		Intent intent = new Intent(v.getContext(), UsersActivity.class);
		startActivityForResult(intent, 0);
	}

}