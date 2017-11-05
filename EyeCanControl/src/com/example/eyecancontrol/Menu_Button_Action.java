package com.example.eyecancontrol;


import java.lang.reflect.Array;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;


public class Menu_Button_Action implements OnClickListener 
{
	//각 변수선언
	VariableSet variable;
	EyeCanControl eyeCanControl;
	BulbActivity bulbActivity;
	ArrayList<Intent> intent;
	Handler mHandler;
	public Menu_Button_Action()
	{
		mHandler = EyeCanControl.mHandler;
	}
	
	@Override
	public void onClick(View v) 
	{
		//각버튼의 ID값의 따라 해당 메세지 Handler를 통해 전송
		if(v.getId() == R.id.bulbBtn)
		{
			Message msg = Message.obtain(mHandler,R.id.bulbBtn);
    		mHandler.sendMessage(msg);
		}
		else if(v.getId() == R.id.fanBtn)
		{
			Message msg = Message.obtain(mHandler,R.id.fanBtn);
    		mHandler.sendMessage(msg);
		}
		else if(v.getId() == R.id.windowBtn)
		{
			Message msg = Message.obtain(mHandler,R.id.windowBtn);
    		mHandler.sendMessage(msg);
		}
		else if(v.getId() == R.id.bedBtn)
		{
			Message msg = Message.obtain(mHandler,R.id.bedBtn);
    		mHandler.sendMessage(msg);
		}
		else if(v.getId() == R.id.urineContainerBtn)
		{
			Message msg = Message.obtain(mHandler,R.id.urineContainerBtn);
    		mHandler.sendMessage(msg);
		}
		else if(v.getId() == R.id.nursingDiaryBtn)
		{
			Message msg = Message.obtain(mHandler,R.id.nursingDiaryBtn);
    		mHandler.sendMessage(msg);
		}

		
	}

}
