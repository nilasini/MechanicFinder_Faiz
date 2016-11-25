package com.uom.cse.bbm.system;

import com.uom.cse.bbm.system.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * @author T.Nila specify the donors activity
 *
 */
public class DonorActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.donor_actions);
		/**
		 * response for the button press of update
		 */
		Button bupdate = (Button) findViewById(R.id.donor_update);
		bupdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(), DonorUpdate.class);
				startActivityForResult(intent, 0);

			}
		});

		/**
		 * response for the button press of signup
		 */

		Button bRegister = (Button) findViewById(R.id.donor_signup);
		bRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),
						DonorRegistration.class);
				startActivityForResult(intent, 0);

			}
		});

		/**
		 * give the registered donor details
		 */

		

	}

	/**
	 * 
	 * @param v
	 *            give the eligible requirements for a blood donor
	 */
	public void eligibleReq(View v) {
		Intent intent = new Intent(v.getContext(), Requirements.class);
		startActivityForResult(intent, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
