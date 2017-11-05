package com.example.eyecancontrol;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class Urine_dialog extends Dialog implements OnTouchListener 
{

	private Button ok;
	private Button no;
	
	public Urine_dialog(Context context) 
	{
		super(context); // context 객체를 받는 생성자가 반드시 필요!
		setTitle("소변통");
		setCancelable(false);
		setCanceledOnTouchOutside(false);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_urine); // 커스텀 다이얼로그 레이아웃
		ok = (Button) findViewById(R.id.OK);
		no = (Button) findViewById(R.id.NO);
		ok.setOnTouchListener(this);
		no.setOnTouchListener(this);
	}
	@Override // 터치 리스너
	public boolean onTouch(View v, MotionEvent event) 
	{
	// 확인 버튼을 클릭하면 입력한 값을 적절히 설정한 후 다이얼로그를 닫음
		if (v == ok) 
		{
			dismiss(); // 이후 MainActivity에서 구현해준 Dismiss 리스너가 작동함

		} 
		else if (v == no)
		{
			cancel();

		}
		return false;
	}
}