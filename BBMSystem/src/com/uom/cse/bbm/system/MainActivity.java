package com.uom.cse.bbm.system;

import java.util.ArrayList;

import java.util.List;

import com.uom.cse.bbm.system.R;
import com.uom.cse.bbm.system.*;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 
 * @author T.Nila load the starting page
 *
 */
public class MainActivity extends ActionBarActivity {
	ImageButton imageButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addListenerOnButton();
	}

	public void addListenerOnButton() {

		imageButton = (ImageButton) findViewById(R.id.imageButton1);
		// allow the user to go the users page
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(), UsersActivity.class);
				startActivityForResult(intent, 0);

			}

		});

	}

}
