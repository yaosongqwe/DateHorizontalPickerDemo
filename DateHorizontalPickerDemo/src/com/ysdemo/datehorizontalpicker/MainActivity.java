package com.ysdemo.datehorizontalpicker;

import com.yedemo.datehorizontalpicker.R;
import com.ysdemo.datehorizontalpicker.view.DateHorizontalPickerView;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	private DateHorizontalPickerView dateView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dateView = (DateHorizontalPickerView) findViewById(R.id.layout_date_picker);
		dateView.initDateView("2004-1-2", "2004-2-3",null,null,null);
		dateView.setShowAlphaChange(false);
	}
}
