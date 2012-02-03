package com.beanie.sendmms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);

		Button buttonSendSMS = (Button) findViewById(R.id.buttonSendSMS);
		buttonSendSMS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startSendSMSActivity();
			}
		});
	}

	private void startSendSMSActivity() {
		Intent intent = new Intent(this, SendSMSActivity.class);
		startActivity(intent);
	}
}