package com.uom.cse.bbm.system;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

/**
 * 
 * @author T.Nila update the donor details in the databse
 *
 */
public class DonorUpdate extends ActionBarActivity {
	public static String contactNum;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_donor);

	}

	// allow the user to give the contact number to update the database
	public void click(View v) {
		EditText edittext7 = (EditText) findViewById(R.id.d_contactdetail);
		contactNum = edittext7.getText().toString();
		Intent intent = new Intent(v.getContext(), UpdateDonor.class);
		startActivityForResult(intent, 0);
	}
}
