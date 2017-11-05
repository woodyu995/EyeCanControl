package com.example.eyecancontrol;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;


public class FanActivity extends Activity
{
	
	//ImageView,BitmapDrawable,VariableSet,SeekBar,Handler 변수 선언
	ImageView originalImg;
	BitmapDrawable changedImg1;
	BitmapDrawable changedImg2;
	BitmapDrawable originBitImg;
	VariableSet variable;
	SeekBar fanSeekBar;
	Handler mHandler;

	protected void onCreate(Bundle savedInstanceState) 	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fan);
		
		//각 변수 ID할당
		originalImg = (ImageView) findViewById(R.id.fanImg);
		originalImg.setImageResource(R.drawable.fan_256x256);
		changedImg1 = (BitmapDrawable) getResources().getDrawable(R.drawable.fan_low_256x256);
		changedImg2 = (BitmapDrawable) getResources().getDrawable(R.drawable.fan_high_256x256);
		originBitImg = (BitmapDrawable) getResources().getDrawable(R.drawable.fan_256x256);
		
		fanSeekBar = (SeekBar) findViewById(R.id.fanSeekBar);
		fanSeekBar.setOnSeekBarChangeListener(FSEEKBAR);
		variable = (VariableSet)getIntent().getExtras().get("variable");	//넘어온 객체 받기
		mHandler = EyeCanControl.mHandler;
		initial_status();
	}
	//Seekbar 객체 생성및 리스너 추가
	public SeekBar.OnSeekBarChangeListener FSEEKBAR = new SeekBar.OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
		{
			//SeekBar체인지 리스너 구현
			if(progress == 0) 
			{
				sendData("20");	//서버에 data보내기
				originalImg.setImageDrawable(originBitImg);
				Message msg = Message.obtain(mHandler, Integer.parseInt("20"));	//set fan status for set message
				mHandler.sendMessage(msg);	//handler send message
			}
			else if(progress == 1) 
			{
				sendData("21");	//서버에 data보내기
				originalImg.setImageDrawable(changedImg1);
				Message msg = Message.obtain(mHandler, Integer.parseInt("21"));
				mHandler.sendMessage(msg);	//handler send message
				
			}
			else if(progress == 2)
			{
				sendData("22");	//서버에 data보내기
				originalImg.setImageDrawable(changedImg2);
				Message msg = Message.obtain(mHandler, Integer.parseInt("22"));
				mHandler.sendMessage(msg);	//handler send message
				
			}
		}
	};
	
	public void sendData(String data)
	{
		TCPclient tp;
		tp = new TCPclient(data, EyeCanControl.socket, variable.getServerIP(),
				variable.getServerPort()); // TCPclient생성(11초기화)
		tp.run(); // run 메소드 호출
	}
	
	public void initial_status()
	{
		if(variable.getFanStatus().equals("20"))
		{
			fanSeekBar.setProgress(0);
			originalImg.setImageDrawable(originBitImg);
			
		}
		else if(variable.getFanStatus().equals("21"))
		{
			fanSeekBar.setProgress(1);
			originalImg.setImageDrawable(changedImg1);
			
		}
		else if(variable.getFanStatus().equals("22"))
		{
			fanSeekBar.setProgress(2);
			originalImg.setImageDrawable(changedImg2);
			
		}
		

	}
}
