package com.example.eyecancontrol;

import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class UrineActivity extends Activity
{
	//ImageView,Textview,VariableSet,Handler 변수 선언
	ImageView originalImg;
	TextView originalText;
	VariableSet variable;
	Handler mHandler;
	
	protected void onCreate(Bundle savedInstanceState) 	
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_urine);
		variable = (VariableSet)getIntent().getExtras().get("variable");	//get variable
		Urine_dialog urine_dialog;
		
		urine_dialog = new Urine_dialog(UrineActivity.this);
		urine_dialog.setTitle("소변통");	//title설정
		if(variable.getUrineStatus() == 't')
		{
			urine_dialog.show();	//다이얼 로그 보이기
		}

		// 다이얼 로그창 사라지는 경우
		urine_dialog.setOnDismissListener(new OnDismissListener() 
		{
			@Override
			public void onDismiss(DialogInterface arg0) 
			{
				try 
				{
					if(variable.getCancel() == false)
					{
						
						try 
						{
							originalImg.setImageResource(R.drawable.container_256x256);
							originalText.setText(R.string.notfull);
							Message msg = Message.obtain(mHandler, Integer.parseInt("40"));
							mHandler.sendMessage(msg);	//handler send message
							Log.d("Test","OK");
						}
						catch(Exception e)
						{
							
						}
					}
					else
						variable.setCancel(false);
				}
				catch (Exception e) 
				{
					
				}

			}
		});
		// 커스텀 다이얼로그의 취소 버튼을 터치했을 때 취할 행동들
		urine_dialog.setOnCancelListener(new OnCancelListener() 
		{
			@Override
			public void onCancel(DialogInterface arg0) 
			{
				Toast.makeText(getApplicationContext(), "소변통을 비우지 않으셨습니다.",variable.getToastTime()).show();
				variable.setCancel(true);
			}
			
		});
		//각 변수의 ID값 할당
		originalImg = (ImageView) findViewById(R.id.urineContainerImg);
		originalText = (TextView) findViewById(R.id.containerTxt2);
		mHandler = EyeCanControl.mHandler;	//set Handler
		if(variable.getUrineStatus() == 'f')
		{
			originalImg.setImageResource(R.drawable.container_256x256);
			originalText.setText(R.string.notfull);
			Message msg = Message.obtain(mHandler, Integer.parseInt("40"));
			mHandler.sendMessage(msg);	//handler send message
		}
		else
		{
			originalImg.setImageResource(R.drawable.container_full_256x256);
			originalText.setText(R.string.full);
			Message msg = Message.obtain(mHandler, Integer.parseInt("41"));
			mHandler.sendMessage(msg);	//handler send message
		}
	}


}
