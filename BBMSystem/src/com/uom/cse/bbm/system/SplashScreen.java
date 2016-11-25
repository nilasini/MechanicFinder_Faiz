package com.uom.cse.bbm.system;

import com.uom.cse.bbm.system.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 
 * @author T.Nila load the splash screen and wait for the main activity start
 *
 */
public class SplashScreen extends Activity {
	public void onCreate(Bundle savedInstenceState) {
		super.onCreate(savedInstenceState);
		setContentView(R.layout.splash);
		Thread splash_screen = new Thread() {
			public void run() {
				try {
					sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					startActivity(new Intent(getApplicationContext(),
							MainActivity.class));
					finish();
				}
			}
		};
		splash_screen.start();

	}

}
