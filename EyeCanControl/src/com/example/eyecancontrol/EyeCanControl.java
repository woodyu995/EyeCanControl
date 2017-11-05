package com.example.eyecancontrol;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import com.google.android.gcm.GCMRegistrar;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint({ "NewApi", "HandlerLeak" })

public class EyeCanControl extends Activity 
{
	/** Called when the activity is first created. */
	static public Socket socket = null;	//Socket선언 및 null초기화
	static public Handler mHandler;	//Handler선언
	static public String selectDate = null;	//달력에서 선택한 날짜 저장할 String 변수
	static public SQLiteDatabase db_emergecny;	//DB사용을 위한SQLiteeDatabase 선언
	static public SQLiteDatabase db_urine;	//DB사용을 위한SQLiteeDatabase 선언
	static public ArrayList<String> emergency_date = new ArrayList<String>();	//DB에서 응급메세지 수신 내역 저장할 ArrayList선언
	static public ArrayList<String> urine_date = new ArrayList<String>();	//DB에서 응급메세지 수신 내역 저장할 ArrayList선언
	static String emergencyMsg = null;	//응급 메세지 내용 저장할 String 변수
	VariableSet variable = new VariableSet();	//변수 set생성
	final TextView tv = null;	//TextView생성
	ProgressDialog mProgress;	//Progress다이얼로그 변수 선언
	Button_Set button;	//Button_Set 변수 선언
	Context context = null;	//Context변수 선언
	Urine_dialog urine_dialog = null;	//소변통 다이얼 로그위 한 변수 선언
	DB_Open_Emergency db_open_emergency;	//응급메세지 관련 DB 변수 선언
	DB_Open_Urine db_open_urine;	//소변통 관련 DB변수 선언
	
	Cursor emergency_cursor;	//DB커서 변수 선언
	Cursor urine_cursor;
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		change_policy();	//call policy change
		
		//소변통 다이얼로그 객체 생성
		urine_dialog = new Urine_dialog(EyeCanControl.this);
		context =  getApplicationContext();
		
		//DB관련 메소드
		db_open_emergency = new DB_Open_Emergency(this);
		db_open_urine = new DB_Open_Urine(this);
		db_emergecny = db_open_emergency.getWritableDatabase();
		db_urine = db_open_urine.getWritableDatabase();
		
		
		//Internet연결 체크를 위한 메소드 호출 및 그에 따른 처리
		if(!check_internet())
		{
			AlertDialog.Builder alert = new AlertDialog.Builder(EyeCanControl.this);
			alert.setPositiveButton("확인", new DialogInterface.OnClickListener() 
			{
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    	android.os.Process.killProcess(android.os.Process.myPid());
			    }
			});
			alert.setMessage("인터넷 연결이 되지 않아 어플리케이션이 종료됩니다.");
			alert.show();
		}
		
		//Handler 처리
		mHandler = new Handler()
		{
			@Override
			public void handleMessage(Message meg)
			{
				if(meg.what == 11)
				{
					variable.setLedStatus(true);;	//led 상태 변경
					button.getBulbBtn().setSelected(true);//LED이미지 변경
				}
				else if(meg.what == 10)
				{
					variable.setLedStatus(false);;	//led 상태 변경
					button.getBulbBtn().setSelected(false);//LED이미지 변경
				}
				else if(meg.what == 20)	//선풍기 상태 관련
				{
					variable.setFanStatus("20"); 	//선풍기 상태 변경
					button.getFanBtn().setBackgroundResource(R.drawable.fan_128x128);
					//set fan image
				}
				else if(meg.what == 21)
				{
					variable.setFanStatus("21"); 	//선풍기 상태 변경
					button.getFanBtn().setBackgroundResource(R.drawable.fan_low_128x128);
					//set fan image
				}
				else if(meg.what == 22)
				{
					variable.setFanStatus("22"); 	//선풍기 상태 변경
					button.getFanBtn().setBackgroundResource(R.drawable.fan_high_128x128);
					//set fan image
				}
				else if(meg.what == 30)	//창문 상태 관련
				{
					
					variable.setWindowStatus(false);	//창문 상태 변경 
					button.getWindowBtn().setBackgroundResource(R.drawable.window_128x128);
				}
				else if(meg.what == 31)
				{
					variable.setWindowStatus(true);	 //창문 상태 변경
					button.getWindowBtn().setBackgroundResource(R.drawable.window_open_128x128);
					//set fan image
				}
				else if(meg.what == 40) //소변통 상태 관련
				{
					button.getUrineBtn().setBackgroundResource(R.drawable.container_128x128);
					write_urine(context,variable.getUrineF());	//소변통 비움 상태로 변경
				}
				else if(meg.what == 41)	
				{
					button.getUrineBtn().setBackgroundResource(R.drawable.container_full_128x128);
					write_urine(context,variable.getUrineT());	//소변통 가득찬 상태로 변경
				}
				else if(meg.what == 42)	//DB 쿼리 관련
				{
					//DB쿼리 전달
					db_urine.execSQL("insert into urinecontainer values(datetime('now', 'localtime'))");
					button.getUrineBtn().setBackgroundResource(R.drawable.container_full_128x128);
					write_urine(context,variable.getUrineT());
					urine_dialog.show(); 	//다이얼 로그 창 실행
					
				}
				else if(meg.what == 51)	//DB Insert쿼리 실행
				{
					db_emergecny.execSQL("insert into emergency values(datetime('now', 'localtime'), '" + EyeCanControl.emergencyMsg +"')");
				}
				else if(meg.what == 61)	//DB select문 실행
				{
					call_select();
				}
				else if(meg.what == 70)	//침대 상태 관련
				{
					button.getBedBtn().setBackgroundResource(R.drawable.bed_128x128);	//침대 평상 이미지 적용
				}
				else if(meg.what == 71)	//침대 UP이미지로 변경
				{
					button.getBedBtn().setBackgroundResource(R.drawable.bedup_128x128);
				}
				else if(meg.what == 72)	//침대 Down이미지로 변경
				{
					button.getBedBtn().setBackgroundResource(R.drawable.beddown_128x128);
				}
				//Main Activity 버튼 관련 설정
				else if(meg.what == R.id.bulbBtn)
				{
					show_activity(BulbActivity.class);
				}
				else if(meg.what == R.id.fanBtn)
				{
					show_activity(FanActivity.class);
				}
				else if(meg.what == R.id.windowBtn)
				{
					show_activity(WindowActivity.class);
				}
				else if(meg.what == R.id.bedBtn)
				{
					show_activity(BedActivity.class);
				}
				else if(meg.what == R.id.urineContainerBtn)
				{
					show_activity(UrineActivity.class);	
				}
				else if(meg.what == R.id.nursingDiaryBtn)
				{
					show_activity(DiaryActivity.class);
				}
				
			}
		};
		//초기 로고 화면 실행
		startActivity(new Intent(this, MainLogoActivity.class));	//start loading		
		
		//소변통 상태 읽어오기
		read_urine(context);
	
		
		//Main Activity에서 사용할 Button객체 할당
		button = new Button_Set(this);
		button.setAddListener();	//리스너 추가
		variable.setServerIP("192.168.0.15");	//Server Ip설정
		variable.setServerPort(9000);	//Server Port설정
		variable.setPhoneIp(getLocalIpAddress(2));	//모바일 기기 IP받아 오기

		//Receiver 서버 On
		Receiver receiver = new Receiver(variable,mHandler);
		receiver.setDaemon(true);//다른 스레드가 죽으면 함께 죽는 스레드 설정
		receiver.start();	//receiver start
		
		//소켓 연결
		CreateSocket create = new CreateSocket(variable.getServerIP(), variable.getServerPort());	//소켓 연결 준비 단계
		socket = create.getSocket();	//socket변수 초기화
		try
		{
			connect_Socket(socket, create.getAddress());	//소켓 연결 실행
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		//푸시 알림을 위한 기기 등록
		registerGcm();


		urine_dialog.setTitle("소변통");	//소변통 다이얼 로그 title설정


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
						//파일 내용 바꾸기
						String FileName = "Urine_contatiner.txt";	//파일 이름
						Byte status = 'f';		
						//소변통 관련 파일 생성
						try 
						{
							FileOutputStream fos = context.openFileOutput(FileName,Context.MODE_PRIVATE);	//File output 생성
							fos.write(status);	//파일에 쓰기
							fos.close();	//fileoutput Stream종료
							button.getUrineBtn().setBackgroundResource(R.drawable.container_128x128);
							Log.d("Test","OK");
						}
						catch (IOException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
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
		// 소변통 다이얼로그의 취소 버튼을 터치했을 때 취할 행동들
		urine_dialog.setOnCancelListener(new OnCancelListener() 
		{
			@Override
			public void onCancel(DialogInterface arg0) 
			{
				Toast.makeText(getApplicationContext(), "소변통을 비우지 않으셨습니다.",variable.getToastTime()).show();
				variable.setCancel(true);
			}
			
		});
		
	}
	
	
	//취소버튼 관련 사항
	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
		 switch(keyCode) 
		 {
		 case KeyEvent.KEYCODE_BACK:
			 new AlertDialog.Builder(this)
			 	.setTitle("종료").setMessage("어플리 케이션을 종료하시겠습니다.")
			 			.setPositiveButton("종료", new DialogInterface.OnClickListener() 
			 			{
			 				public void onClick(DialogInterface dialog, int whichButton) 
			 				{
			 					TCPclient tp;
			 					if(EyeCanControl.socket.isConnected() == true)
			 					{
			 						tp = new TCPclient("90", EyeCanControl.socket, variable.getServerIP(),variable.getServerPort());
				 					tp.run();
			 					}
			 					else
			 					{			
			 						////
			 						android.os.Process.killProcess(android.os.Process.myPid());
			 						close_db();	
			 					}
			 					//종료 코드
			 					android.os.Process.killProcess(android.os.Process.myPid());
			 					close_db();
			 					}
			 				})
			 					.setNegativeButton("취소", null).show();
			 				return false;
		 			default:
		 				return false;
		 }
	 }
	 public void call_select()
	 {
		//DB Select 쿼리문
		 emergency_cursor = db_emergecny.rawQuery("select strftime('%Y.%m.%d %H:%M', date) date from emergency  where date(date) = '"+ EyeCanControl.selectDate + "'", null);
		 urine_cursor = db_urine.rawQuery("select strftime('%Y.%m.%d %H:%M', date) date from urinecontainer where date(date) = '" + EyeCanControl.selectDate + "'", null);
		 //array list초기화
		 emergency_date.clear();
		 urine_date.clear();
		while (emergency_cursor.moveToNext()) 
		{
			emergency_date.add(emergency_cursor.getString(0));

		}
		while (urine_cursor.moveToNext()) 
		{
			urine_date.add(urine_cursor.getString(0));

		}
	 }
	 //DB관련 변수 close
	 public void close_db()
	 {
		db_emergecny.close();
		db_urine.close();
		db_open_emergency.close();
		db_open_urine.close();
	 }
	//초기 상태값 받아오기
	public void initial_status()
	{
		// 소켓 연결
		TCPclient tp;
		
		tp = new TCPclient("99",EyeCanControl.socket,variable.getServerIP()
				,variable.getServerPort()); // TCPclient생성(99초기화)
		//run호출
		tp.run();
		variable.setInitialStatus(tp.return_msg());	//초기 설정갓 ㅓ장
		
		if(variable.getInitial_status().substring(0,2).equals("11"))	//11과 같은 경우
		{
			variable.setLedStatus(true);;	//led 상태 변경
			button.getBulbBtn().setSelected(true);
			
		}
		else if(variable.getInitial_status().substring(0,2).equals("10"))	//10과 같은경우
		{
			variable.setLedStatus(false);;	//led 상태 변경
			button.getBulbBtn().setSelected(false);
			
		}
		if(variable.getInitial_status().substring(2,4).equals("20"))	//선풍기 설정관련
		{
			variable.setFanStatus("20");	//선풍기 상태변경
			button.getFanBtn().setBackgroundResource(R.drawable.fan_128x128);	//선풍기 이미지 변경
		}
		else if(variable.getInitial_status().substring(2,4).equals("21"))	//input initial_status equal 21
		{
			variable.setFanStatus("21");	//선풍기 상태변경
			button.getFanBtn().setBackgroundResource(R.drawable.fan_low_128x128);	//set fanButton image	
		}
		else if(variable.getInitial_status().substring(2,4).equals("22"))	//input initial_status equal 22
		{
			variable.setFanStatus("22");	//선풍기 상태변경
			button.getFanBtn().setBackgroundResource(R.drawable.fan_high_128x128);	//set fanButton image
		}
		
		if(variable.getInitial_status().substring(4,6).equals("30"))	//input initial_status equal 30
		{
			variable.setWindowStatus(false);	//창문 상태 관련
			button.getWindowBtn().setBackgroundResource(R.drawable.window_128x128);	//set windowButton image
		}
		else if(variable.getInitial_status().substring(4,6).equals("31"))	//input initial_status equal 31
		{
			variable.setWindowStatus(true);	//창문 상태 관련
			button.getWindowBtn().setBackgroundResource(R.drawable.window_open_128x128);	//set windowButton image	
		}
		if(variable.getUrineStatus() == 'f')	//소변통 관련
		{
			button.getUrineBtn().setBackgroundResource(R.drawable.container_128x128);
		}
		else
		{
			button.getUrineBtn().setBackgroundResource(R.drawable.container_full_128x128);
		}
	}
	
	//GCM register
	public void registerGcm() 
	{

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		final String regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("")) {
			GCMRegistrar.register(this, "1032825485033");
		} else {
			Log.e("id", regId);
		}

	}
	
	//IP가져오기
	public static String getLocalIpAddress(int type) 
	{
		try 
		{
			for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en
					.hasMoreElements();) {
				NetworkInterface intf = (NetworkInterface) en.nextElement();
				for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr
						.hasMoreElements();) {
					InetAddress inetAddress = (InetAddress) enumIpAddr
							.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						switch (type) {
						case 1:	//ip6방식
							if (inetAddress instanceof Inet6Address) {
								return inetAddress.getHostAddress().toString();
							}
							break;

						case 2:	//ip4방식
							if (inetAddress instanceof Inet4Address) {
								return inetAddress.getHostAddress().toString();
							}
							break;
						}

					}
				}
			}
		} 
		catch (SocketException ex) 
		{
			
		}
		return null;
	}
	
	
	public void sendIP(Socket socket,String ip)
	{
		TCPclient tp;
		tp = new TCPclient(ip, socket, variable.getServerIP(), variable.getServerPort());
		tp.run();
		//기기의 IP와 Port관련 Server에정보 전송
	}
	
	public void connect_Socket(Socket socket, SocketAddress address)
	{
		try
		{
			socket.connect(address,variable.getConnectionTime());	//try socket connection	
			if(variable.getUrineStatus() == 't')
			{
				urine_dialog.show();	//다이얼 로그 보이기
			}
			sendIP(EyeCanControl.socket, "IP=" + variable.getPhoneIp());	//send phone IP to server
			initial_status();	//initial_status setting
		}
		catch(SocketTimeoutException e)
		{
			//다이얼로그 호출
			final IP_connect_dialog ip_connect_dialog;
			ip_connect_dialog = new IP_connect_dialog(EyeCanControl.this);
			ip_connect_dialog.setTitle("IP설정");
			ip_connect_dialog.setCancelable(false);	//if cancel button touch, then dialog not close
			ip_connect_dialog.setCanceledOnTouchOutside(false);	//if other activity touch, then dialog not close
			ip_connect_dialog.show();
			ip_connect_dialog.setOnDismissListener(new OnDismissListener() 
			{
				@Override
				public void onDismiss(DialogInterface arg0) 
				{
					// socket = new Socket(serverIP, serverPort);
					try
					{
						
						variable.setServerIP(ip_connect_dialog.getIp_Address());	//IP설정
						variable.setServerPort(Integer.parseInt(ip_connect_dialog.getPort_Number()));	//port설정
						CreateSocket createsocket = new CreateSocket(variable.getServerIP(),
								variable.getServerPort());	//소켓 얻어오기
						Socket socket = createsocket.getSocket();	//소켓 저장
						socket.connect(createsocket.getAddress(),variable.getConnectionTime());	//연결 실행
						if(variable.getUrineStatus() == 't')
						{
							urine_dialog.show();	//다이얼 로그 보이기
						}
						EyeCanControl.socket = socket;
						sendIP(EyeCanControl.socket, "IP=" + variable.getPhoneIp());	//안드로이드 폰 IP전송
						initial_status();	//초기값 설정
						
					}
					catch(SocketTimeoutException e)
					{			
						ip_connect_dialog.show();
						Toast.makeText(getApplicationContext(), "IP주소와 Port번호를 확인해 주세요", variable.getToastTime()).show();
					}
					catch(Exception e)
					{
						ip_connect_dialog.show();
						Log.d("Exception",e.getMessage());
						Toast.makeText(getApplicationContext(), "IP주소와 Port번호를 확인해 주세요", variable.getToastTime()).show();
					
					}
				}
			});

		}
		catch(IOException e)
		{
			Log.d("IOException","발생");
		}
		finally
		{
			mProgress.dismiss();
		}
	}
	
	
	public void write_urine(Context context,byte status)
	{
		//파일 내용 바꾸기
		String FileName = "Urine_contatiner.txt";	//파일 이름		
		//소변통 관련 파일 생성
		try 
		{
			FileOutputStream fos = context.openFileOutput(FileName,Context.MODE_PRIVATE);	//File output 생성
			fos.write(status);	//파일에 쓰기
			fos.close();	//fileoutput Stream종료
		}
		catch(Exception e)
		{
			
		}
		variable.setUrineStatus((char)(status));	//Urine_status변수 초기화
	}
	public void read_urine(Context context)
	{
		byte[] status = new byte[1];
		try 
		{
			FileInputStream fis = context.openFileInput("Urine_contatiner.txt");	
			fis.read(status);
			fis.close();
		}
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			status[0] = 'f';
		}
		catch(Exception e1)
		{
		
		}
		variable.setUrineStatus((char)(status[0]));	//Urine_status변수 초기화
	}
	
	public void change_policy()
	{
		// 정책 우회 코드
				if (android.os.Build.VERSION.SDK_INT > 9) 
				{
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);
				}
	}
	
	public void show_activity(Class mClass)

	{
		Bundle extras = new Bundle();
		Intent intent = new Intent(EyeCanControl.this, mClass);
		extras.putSerializable("variable", variable);	//넘길 객체 설정
		intent.putExtras(extras);	//객체 넘기기
		try
		{
			startActivity(intent);
		}
		catch(Exception e)
		{
			e.getMessage();
		}
	}
	
	//인터넷에 연결돼 있나 확인
	public boolean check_internet()
	{
		ConnectivityManager connect = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		//NetworkInfo networkinfo = new NetworkInfo();
		if (

		connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == 
		NetworkInfo.State.CONNECTED ||
		connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() ==
		NetworkInfo.State.CONNECTED) {

			// 연결 돼있는 경우
			return true;
			// ...

		} else {

			// 연결 돼있지 않은 경우
			return false;
			// ...

		}
	}
	public static void setEmergencyMsg(String emergencyMsg)
	{
		EyeCanControl.emergencyMsg =emergencyMsg; 
	}
}