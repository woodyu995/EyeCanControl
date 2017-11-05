package com.example.eyecancontrol;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;


public class BulbActivity extends Activity{
	
	ImageView originalImg;	//ImageView 선언
	BitmapDrawable changedImg;	//BitmapDrawable선언
	BitmapDrawable originBitImg; //BitmapDrawable선언
	VariableSet variable;	//VariableSet변수 선언
	Handler mHandler;	//Handler선언
	ToggleButton bulbTBtn;	//ToggleButton선언
	protected void onCreate(Bundle savedInstanceState) 	
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bulb);		
		
		originalImg = (ImageView) findViewById(R.id.bulbImg);	//ImageView ID할당
		bulbTBtn = (ToggleButton) findViewById(R.id.bulbBtn);	//ImageView ID할당
		changedImg = (BitmapDrawable) getResources().getDrawable(R.drawable.bulb_on_256x256);	//Image resource할당
		originBitImg = (BitmapDrawable) getResources().getDrawable(R.drawable.bulb_256x256);	//Image resource할당
		
		bulbTBtn.setOnCheckedChangeListener(BTOGGLEBUTTON);	//ToggleButton 의 리스너 추가
		originalImg.setImageResource(R.drawable.bulb_256x256);	//Image resource할당
		variable = (VariableSet)getIntent().getExtras().get("variable");	//이전 Activity에서 VariableSet변수 받아오기
		mHandler = EyeCanControl.mHandler;	//mHandler 할당
		initial_status();	//초기 Image변경을 위한 메소드 호출
		
	}

	public ToggleButton.OnCheckedChangeListener BTOGGLEBUTTON = new ToggleButton.OnCheckedChangeListener()
	{

		public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
		{
			if(arg1)	//led꺼진 상태
			{
				sendData("11", true);	//server에 data전송
				Message msg = Message.obtain(mHandler, Integer.parseInt("11"));	//Handler message 생성
				mHandler.sendMessage(msg);	//Message 전송
				originalImg.setImageDrawable(changedImg);	//Image변경
			}
			else
			{
				sendData("10", false);	//server에 data전송
				Message msg = Message.obtain(mHandler, Integer.parseInt("10"));	//Handler message 생성
				mHandler.sendMessage(msg);	//Message 전송
				originalImg.setImageDrawable(originBitImg);	//Image변경
			}
		}

	};
	public void sendData(String data,boolean status)
	{
		TCPclient tp;	//TCPclient 선언
		tp = new TCPclient(data, EyeCanControl.socket, variable.getServerIP(),
				variable.getServerPort()); // TCPclient 객체 생성
		tp.run(); // run 메소드 호출
		variable.setLedStatus(status);;	//led 상태 변경
	}
	
	public void initial_status()
	{
		if(variable.getLedStatus() == true)	//LED On
		{
			bulbTBtn.setChecked(true);		//bulbTBtn true변경
			originalImg.setImageDrawable(changedImg);	//Image변경(LED ON이미지)
		}
		else
		{
			bulbTBtn.setChecked(false);	//bulbTBtn true변경
			originalImg.setImageDrawable(originBitImg);	//Imgae변경(LED OFF이미지, 기본이미지)
		}
	}
}
