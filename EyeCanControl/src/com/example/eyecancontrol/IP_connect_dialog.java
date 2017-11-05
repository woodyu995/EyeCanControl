package com.example.eyecancontrol;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class IP_connect_dialog extends Dialog implements OnTouchListener {

	private EditText ip, port;
	private Button addOK;
	private Button quit;
	private String ip_Address, port_Number;
	public IP_connect_dialog(Context context) {
		super(context); // context 객체를 받는 생성자가 반드시 필요!
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_ip); // 커스텀 다이얼로그 레이아웃

	ip = (EditText) findViewById(R.id.ip_address);
	port = (EditText) findViewById(R.id.port_number);
	addOK = (Button) findViewById(R.id.addOK);
	quit = (Button) findViewById(R.id.quit);
	
	addOK.setOnTouchListener(this);
	quit.setOnTouchListener(this);
	}

	public String getIp_Address() {
	return ip_Address;// 사이트 이름을 반환하는 메소드
	}

	public String getPort_Number() {
	return	port_Number; // 사이트 주소를 반환하는 메소드
	}
	@Override // 터치 리스너
	public boolean onTouch(View v, MotionEvent event) {
	// 확인 버튼을 클릭하면 입력한 값을 적절히 설정한 후 다이얼로그를 닫음
	if (v == addOK) 
	{
		ip_Address = ip.getText().toString(); // 입력된 IP주소를 얻어옴
		port_Number = port.getText().toString(); // 입력된 Port번호를 얻어옴
		dismiss(); // 이후 MainActivity에서 구현해준 Dismiss 리스너가 작동함
		
		
	}
	else if (v == quit) 
	{
		android.os.Process.killProcess(android.os.Process.myPid());
		
	}
	return false;
}
}