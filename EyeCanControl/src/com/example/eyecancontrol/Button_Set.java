package com.example.eyecancontrol;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.widget.Button;

public class Button_Set
{
	//사용할 Button 선언
	private Button bulbBtn = null;
	private Button fanBtn = null;
	private Button windowBtn = null;
	private Button bedBtn = null;
	private Button urineBtn = null;
	private Button diaryBtn = null;
	
	public Button_Set(Activity activity)
	{
		bulbBtn = ((Button)activity.findViewById(R.id.bulbBtn));	//Button ID할당
		fanBtn = ((Button)activity.findViewById(R.id.fanBtn));	//Button ID할당
		windowBtn = ((Button)activity.findViewById(R.id.windowBtn));	//Button ID할당
		urineBtn = ((Button)activity.findViewById(R.id.urineContainerBtn));		//Button ID할당
		bedBtn = ((Button)activity.findViewById(R.id.bedBtn));		//Button ID할당
		diaryBtn = ((Button)activity.findViewById(R.id.nursingDiaryBtn));		//Button ID할당
	}
	
	//각 버튼 get/set메소드
	public Button getBulbBtn() {
		return bulbBtn;
	}
	public void setBulbBtn(Button bulbBtn) {
		this.bulbBtn = bulbBtn;
	}
	public Button getFanBtn() {
		return fanBtn;
	}
	public void setFanBtn(Button fanBtn) {
		this.fanBtn = fanBtn;
	}
	public Button getWindowBtn() {
		return windowBtn;
	}
	public void setWindowBtn(Button windowBtn) {
		this.windowBtn = windowBtn;
	}
	public Button getBedBtn() {
		return bedBtn;
	}
	public void setBedBtn(Button bedBtn) {
		this.bedBtn = bedBtn;
	}
	public Button getUrineBtn() {
		return urineBtn;
	}
	public void setUrineBtn(Button urineBtn) {
		this.urineBtn = urineBtn;
	}
	public Button getDiaryBtn() {
		return diaryBtn;
	}
	public void setDiaryBtn(Button diaryBtn) {
		this.diaryBtn = diaryBtn;
	}

	public void setAddListener()
	{
		//각 Button의 리스너 추가
		bulbBtn.setOnClickListener(new Menu_Button_Action());
		fanBtn.setOnClickListener(new Menu_Button_Action());
		windowBtn.setOnClickListener(new Menu_Button_Action());
		bedBtn.setOnClickListener(new Menu_Button_Action());
		urineBtn.setOnClickListener(new Menu_Button_Action());
		diaryBtn.setOnClickListener(new Menu_Button_Action());
	}
	
}
