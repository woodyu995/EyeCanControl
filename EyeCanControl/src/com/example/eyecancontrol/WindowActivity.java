package com.example.eyecancontrol;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class WindowActivity extends Activity{
	
	//ImageView,BitmapDrawable,VariableSet,Handler,ToggleButton변수 선언
	ImageView originalImg;
	BitmapDrawable changedImg;
	BitmapDrawable originBitImg;
	VariableSet variable;
	Handler mHandler;
	ToggleButton windowTBtn;
	
	protected void onCreate(Bundle savedInstanceState) 	
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_window);
		
		//각변수 ID값 할당
		originalImg = (ImageView) findViewById(R.id.windowImg);
		originalImg.setImageResource(R.drawable.window_256x256);
		changedImg = (BitmapDrawable) getResources().getDrawable(R.drawable.window_open_256x256);
		originBitImg = (BitmapDrawable) getResources().getDrawable(R.drawable.window_256x256);
		windowTBtn = (ToggleButton) findViewById(R.id.windowSwc);
		variable = (VariableSet)getIntent().getExtras().get("variable");
		mHandler = EyeCanControl.mHandler;
		windowTBtn.setOnCheckedChangeListener(WTOGGLEBUTTON);
		initial_status(); 	//초기 상태값 설정
	}
	
	public ToggleButton.OnCheckedChangeListener WTOGGLEBUTTON = new ToggleButton.OnCheckedChangeListener()
	{

		public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
		{
			if(arg1) 
			{
				sendData("31",true);	//서버에 data보내기
				Message msg = Message.obtain(mHandler, Integer.parseInt("31"));
				mHandler.sendMessage(msg);	//handler send message
				originalImg.setImageDrawable(changedImg);		
			}
			else 
			{
				sendData("30",false);	//서버에 data보내기
				Message msg = Message.obtain(mHandler, Integer.parseInt("30"));
				mHandler.sendMessage(msg);	//handler send message
				originalImg.setImageDrawable(originBitImg);
			}
		}
	};
	
	public void initial_status()
	{
		if(variable.getWindowStatus() == true)	//LED On
		{
			windowTBtn.setChecked(true);		//bulbTbn set checked
			originalImg.setImageDrawable(changedImg);
		}
		else
		{
			windowTBtn.setChecked(false);	//bulbTbn set not-checked
			originalImg.setImageDrawable(originBitImg);
		}
	}
	
	public void sendData(String data, Boolean status)
	{
		TCPclient tp;
		tp = new TCPclient(data, EyeCanControl.socket, variable.getServerIP(),
				variable.getServerPort()); // TCPclient생성
		tp.run(); // run 메소드 호출
		variable.setWindowStatus(status);//창문 상태 변경
	}


}
