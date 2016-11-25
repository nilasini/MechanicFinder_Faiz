package com.uom.cse.bbm.system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 
 * @author T.Nila this defined the admin activities
 *
 */
public class AdminActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_actions);

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
	 * @param vew
	 *            load the oragnization adding page
	 */
	public void addOrg(View v) {
		Intent intent = new Intent(v.getContext(), OrgAddActivity.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * 
	 * @param view
	 *            give the registered donor details
	 */
	public void viewDonor(View v) {
		Intent intent = new Intent(v.getContext(), DonorDetail.class);
		startActivityForResult(intent, 0);
	}

	public void updateDonor(View v) {
		Intent intent = new Intent(v.getContext(), DonorUpdate.class);
		startActivityForResult(intent, 0);
	}

}
