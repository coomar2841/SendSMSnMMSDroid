package com.beanie.sendmms;

import java.util.Set;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendSMSActivity extends Activity {
	private final static String TAG = "SendSMSActivity";

	private final static String INTENT_ACTION_SENT = "com.beanie.sendmms.INTENT_ACTION_SENT";
	private final static String INTENT_ACTION_DELIVERY = "com.beanie.sendmms.INTENT_ACTION_DELIVERY";
	private final static int REQUEST_CODE_ACTION_SENT = 1;
	private static final int REQUEST_CODE_ACTION_DELIVERY = 2;

	private BroadcastReceiver smsSentDeliveredReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_sms_activity);

		final EditText editTextNumber = (EditText) findViewById(R.id.editTextTo);

		final EditText editTextMessage = (EditText) findViewById(R.id.editTextContent);

		Button buttonSend = (Button) findViewById(R.id.buttonSend);
		buttonSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String number = editTextNumber.getText().toString();
				String message = editTextMessage.getText().toString();
				sendSMS(number, message);
			}
		});

		initializeReceivers();

	}

	private void sendSMS(String number, String message) {

		Intent sentIntent = new Intent(INTENT_ACTION_SENT);
		PendingIntent pendingSentIntent = PendingIntent.getBroadcast(this,
				REQUEST_CODE_ACTION_SENT, sentIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Intent deliveryIntent = new Intent(INTENT_ACTION_DELIVERY);
		PendingIntent pendingDeliveryIntent = PendingIntent.getBroadcast(this,
				REQUEST_CODE_ACTION_DELIVERY, deliveryIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		SmsManager smsManager = SmsManager.getDefault();

		// Second parameter is the service center number. Use null if you want
		// to use the default number
		smsManager.sendTextMessage(number, null, message, pendingSentIntent,
				pendingDeliveryIntent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(smsSentDeliveredReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(INTENT_ACTION_SENT);
		filter.addAction(INTENT_ACTION_DELIVERY);

		registerReceiver(smsSentDeliveredReceiver, filter);
	}

	private void initializeReceivers() {
		smsSentDeliveredReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				processBroadcasts(intent);
			}
		};
	}

	private void processBroadcasts(Intent intent) {
		String action = intent.getAction();
		Log.i(TAG, "Received: " + action);

		if (action.equals(INTENT_ACTION_SENT)) {
			Bundle bundle = intent.getExtras();
			// Need to check for error messages
			Log.i(TAG, "Message: Sent");
			Toast.makeText(this, "Message sent", Toast.LENGTH_LONG).show();
		} else if (action.equals(INTENT_ACTION_DELIVERY)) {
			Bundle bundle = intent.getExtras();
			Set<String> keys = bundle.keySet();
			// Need to check for error messages
			Log.i(TAG, "Message: Delivered");
			Toast.makeText(this, "Message delivered", Toast.LENGTH_LONG).show();
		}
	}
}
