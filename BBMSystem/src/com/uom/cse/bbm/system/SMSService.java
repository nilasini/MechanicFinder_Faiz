package com.uom.cse.bbm.system;

import java.util.List;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;

/*
 * This class do the sms notification 
 */
public class SMSService {
	/*
	 * send sms to the list of given phone numbers
	 */
	public void sendSMS(List<String> phone_numbers, String message) {

		for (int i = 0; i < phone_numbers.size(); i++) {
			String number = phone_numbers.get(i);
			SmsManager manager = SmsManager.getDefault();
			manager.sendTextMessage(number, null, message, null, null);

		}

	}

}
