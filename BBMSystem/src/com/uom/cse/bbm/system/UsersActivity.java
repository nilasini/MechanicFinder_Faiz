package com.uom.cse.bbm.system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.uom.cse.bbm.system.R;
import com.uom.cse.bbm.system.DonorRegistration;

/**
 * 
 * allow the users to do their particular activities
 *
 */
public class UsersActivity extends Activity {

	public void onCreate(Bundle savedInstenceState) {
		super.onCreate(savedInstenceState);
		// load the donor pade
		setContentView(R.layout.userspage);
		Button btn = (Button) findViewById(R.id.donor);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DonorActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		// load the oragnization page
		Button btn2 = (Button) findViewById(R.id.b_makerequest);
		btn2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						OrganizationLoginActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		// load the admin page
		Button btn3 = (Button) findViewById(R.id.admin);
		btn3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						AdminLoginActivity.class);
				startActivityForResult(intent, 0);
			}
		});

	}
}
