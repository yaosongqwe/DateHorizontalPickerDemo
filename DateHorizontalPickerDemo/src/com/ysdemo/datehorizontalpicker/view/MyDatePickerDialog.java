package com.ysdemo.datehorizontalpicker.view;

import android.app.DatePickerDialog;
import android.content.Context;

public class MyDatePickerDialog extends DatePickerDialog {
	public MyDatePickerDialog(Context context, OnDateSetListener callBack,
            int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }
    @Override
    protected void onStop() {
    	// 如果不注释掉，点击返回也会去刷新时间
//        super.onStop();
    	this.dismiss();
    }
}
