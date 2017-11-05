package com.example.eyecancontrol;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

@SuppressLint("HandlerLeak") public class MainLogoActivity extends Activity
{
	protected void onCreate(Bundle savedInstanceState) 	
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mainlogo);
		
		Handler h = new Handler() 
		{
			public void handleMessage(Message msg) 
			{
				finish();
			}
		};
		
		h.sendEmptyMessageDelayed(0, 3000);	//딜레이 시간 설정
	}

}
