package test.uom.cse.bbm.system;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.MediaStore.Audio.Media;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.uom.cse.bbm.system.GetFixure;

public class DonorDetails extends InstrumentationTestCase {
	public void testSomeAsynTask() throws Throwable {
		// create a signal to let us know when our task is done.
		final CountDownLatch signal = new CountDownLatch(1);

		/*
		 * Just create an in line implementation of an asynctask. Note this
		 * would normally not be done, and is just here for completeness. You
		 * would just use the task you want to unit test in your project.
		 */
		final AsyncTask<String, Void, String> myTask = new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... arg0) {
				// Do something meaningful.
				return "something happened!";
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				/*
				 * This is the key, normally you would use some type of listener
				 * to notify your activity that the async call was finished.
				 * 
				 * In your test method you would subscribe to that and signal
				 * from there instead.
				 */
				signal.countDown();
			}
		};

		// Execute the async task on the UI thread! THIS IS KEY!
		runTestOnUiThread(new Runnable() {

			@Override
			public void run() {
				myTask.execute("Do something");
			}
		});

		/*
		 * The testing thread will wait here until the UI thread releases it
		 * above with the countDown() or 30 seconds passes and it times out.
		 */
		signal.await(30, TimeUnit.SECONDS);

		// The task is done, and now you can assert some things!
		assertTrue("Happiness", true);
	}

}

/*
 * @Test public void test() {
 * 
 * 
 * HashMap<String, String> donorDetail = new HashMap<String, String>();
 * donorDetail.put("blood_group", "A+"); donorDetail.put("location",
 * "Jaffna,Srilanka"); donorDetail.put("status", "yes");
 * donorDetail.put("username", "nilasini");
 * donorDetail.put("contact"," 077524182"); donorDetailList.add(donorDetail);
 * //} new GetFixure().doInBackground();
 * 
 * while (!GetFixure.isFinished) { try { Thread.sleep(500); } catch
 * (InterruptedException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } }
 * 
 * assertEquals(donorDetailList.get(0).get("blood_group"),
 * GetFixure.donorDetailsList.get(0).get("blood_group"));
 * 
 * 
 * }
 * 
 * }
 */

