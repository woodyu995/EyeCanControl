package com.example.eyecancontrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;


public class BedActivity extends Activity{
	
	ImageView oriImg;	//기본 이미지 저장 변수
	VariableSet variable;	//variableSet변수 선언
	Button up;	//Button변수 선언
	Button down;	//Button변수 선언
	protected void onCreate(Bundle savedInstanceState) 	
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bed);
		oriImg =(ImageView) findViewById(R.id.bedImg);	//이미지 ID 할당	
		up = (Button)findViewById(R.id.upBtn);	//UP버튼 할당
		down = (Button)findViewById(R.id.Downbtn);	//Down버튼 할당
		oriImg.setImageResource(R.drawable.bed_256x256);	//이미지 resource할당
		variable = (VariableSet)getIntent().getExtras().get("variable");//이전 activity에서 variableSet가져오기
		
		up.setOnClickListener(new OnClickListener()
		{	//UP버튼에 리스너 부착
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(variable.isBedUp() == false)	//isBedUp이 false인 경우
				{
					oriImg.setImageResource(R.drawable.bedup_256x256);	//이미지 변경
					variable.setBedUp(true);	//isBedUp true로 변경
					sendData("71");	//서버에 71전송
				}
				else
				{
					oriImg.setImageResource(R.drawable.bed_256x256);	//이미지 변경
					variable.setBedUp(false);	//isBedUp false로 변경
					sendData("70");	//서버에 70전송
				}
			}			
		});
		down.setOnClickListener(new OnClickListener()	//down버튼에 리스너 부착
		{

			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(variable.isBedDown() == false)	//isBedDown이 false인 경우
				{
					oriImg.setImageResource(R.drawable.beddown_256x256);	//Down이미지로 변경
					variable.setBedDown(true);	//BedDown변수 true로 변경
					sendData("72");	//서버에 72전송
				}
				else
				{
					oriImg.setImageResource(R.drawable.bed_256x256);	//bed기본 이미지로 변경
					variable.setBedDown(false);	//BedDown false로 변경
					sendData("70");	//서버에 70전송
				}	
			}
		});
	}
	/*
	 * 서버에 입력받은 parameter값을 전송한다.
	 */
	public void sendData(String data)
	{
		TCPclient tp;	//TCPclient객체 선언
		tp = new TCPclient(data, EyeCanControl.socket, variable.getServerIP(),
				variable.getServerPort()); // TCPclient생성
		tp.run(); // run 메소드 호출
	}

}
