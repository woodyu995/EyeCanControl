package com.example.calendarui;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.example.eyecancontrol.DB_Open_Emergency;
import com.example.eyecancontrol.DB_Open_Urine;
import com.example.eyecancontrol.DiaryActivity;
import com.example.eyecancontrol.EyeCanControl;
import com.example.eyecancontrol.R;
import com.example.eyecancontrol.TCPclient;
import com.example.eyecancontrol.VariableSet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class WritingDiaryView extends Activity
{
	
	ListView emergency_list;
	ListView urine_list;
	ArrayAdapter<String> emergency_adapter;
	ArrayAdapter<String> urine_adapter;
	Button backBtn;
	Button saveBtn;
	String year;
	String month;
	String date;
	String dateTxt;
	String dateWithTxt;
	TextView dateTxtView;
	EditText editMemoTxt;
	EditText weightTxtView;
	String content="", temp="";
	Spinner weatherSpinner;
	Spinner conditionSpinner;
	ArrayAdapter adWeatherSpin;
	ArrayAdapter adConditionSpin;
	Handler mHandler;
	String flag = "0";
	VariableSet variable;
	Intent SNIntent;
	protected void onCreate(Bundle savedInstanceState) 	
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_writingdiary);
		
		// 유저가 선택한 날짜를 각 변수에 저장
		date = getIntent().getStringExtra("date");
		month = getIntent().getStringExtra("month");
		year = getIntent().getStringExtra("year");
			
		// 선택된 날짜 저장
		EyeCanControl.selectDate = year + "-" + month + "-" + date;
			
		// 선택된 날짜를 년월일로 액티비티 화면에 설정
		dateTxtView = (TextView) findViewById(R.id.selectedDateTxt);
		weightTxtView = (EditText)findViewById(R.id.weightTxtView);
		dateTxtView.setText(year + "년" + month + "월" + date + "일");
		
		// 저장과 로드를 위해 txt파일 변수 설정
		dateTxt = year+month+date;
		dateWithTxt = dateTxt + ".txt";
		
		
		mHandler = EyeCanControl.mHandler;
		Message msg = Message.obtain(mHandler, Integer.parseInt("61"));	//diary db select
		mHandler.sendMessage(msg);	//handler send message
		variable = (VariableSet)getIntent().getExtras().get("variable");	//get variable
		SNIntent = new Intent(WritingDiaryView.this, DiaryActivity.class);
		
		// 메모한 정보를 얻기 위한 EditText 설정 
		editMemoTxt = (EditText) findViewById(R.id.editMemoTxt);
		
		// 저장과 로드를 위한 경로 변수 저장
		final String absDirPath = getFilesDir().getAbsolutePath();
		
		// 파일 저장을 위한 파일 생성
		File savedFile = new File(absDirPath);
		
		// 어댑터 객체 생성
		emergency_adapter = new ArrayAdapter<String>(this, R.layout.listview_layout, EyeCanControl.emergency_date);

		//리스트뷰 객체 생성 & 어댑터 설정
		emergency_list = (ListView)findViewById(R.id.emergency_listView);
		emergency_list.setAdapter(emergency_adapter);
		urine_adapter = new ArrayAdapter<String>(this, R.layout.listview_layout, EyeCanControl.urine_date);

		//리스트뷰 객체 생성 & 어댑터 설정
		urine_list = (ListView)findViewById(R.id.urine_listView);
		urine_list.setAdapter(urine_adapter);
		urine_list.setDividerHeight(0);

		// 선택된 날의 날씨 선택을 위한 스피너 선언
		weatherSpinner = (Spinner) findViewById(R.id.weatherSpinner);
		weatherSpinner.setPrompt("select one");
		
		// 날씨스피너의 어댑터 설정
		adWeatherSpin = ArrayAdapter.createFromResource(this, R.array.weather, android.R.layout.simple_spinner_item);
		weatherSpinner.setAdapter(adWeatherSpin);
		
		// 환자의 상태를 선택하기 위한 스피너 선언
		conditionSpinner = (Spinner) findViewById(R.id.conditionSpinner);
		conditionSpinner.setPrompt("select one");
		
		// 환자상태스피너의 어댑터 설정
		adConditionSpin = ArrayAdapter.createFromResource(this, R.array.condition, android.R.layout.simple_spinner_item);
		conditionSpinner.setAdapter(adConditionSpin);
		
		// 유저의 이전 입력사항을 로드 
		if(!savedFile.exists()) 	// 저장된 파일이 없으면 새로 폴더를 생성
		{
			savedFile.mkdirs();
		}
		else												// 저장된 파일이 있을 경우
		{
			if(savedFile.listFiles().length > 0) 			// 폴더 안에 파일이 하나라도 있을 경우
			{	
				for(File f : savedFile.listFiles())			// 파일의 처음부터 끝까지 for문 실행 
				{
					String str = f.getName();
					int i = 0;
					
					if(str.equals(dateWithTxt)) 			// 파일의 이름이 위에 선택된 날짜와 같을 경우
					{
						String loadPath = absDirPath+"/"+str;
						try 
						{
															// 파일 입력 스트림과 파일 내용을 읽기위한
															// 버퍼리더 변수 생성
							FileInputStream fis = new FileInputStream(loadPath);
							BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
															// 파일의 내용을 처음부터 끝까지 검색							
							while( (temp = bufferReader.readLine()) != null ) 
							{
								
								if(i == 0)					// 입력한 무게 값 로드
								{
									weightTxtView.setText(temp);
								}
								else if(i == 1)				// 입력한 날씨 로드
								{
									int tmpNumb = 0;
									tmpNumb=Integer.parseInt(temp);
									weatherSpinner.setSelection(tmpNumb);
								}
								else if(i == 2)				// 입력한 환자상태 로드
								{
									int tmpNumb = 0;
									tmpNumb=Integer.parseInt(temp);
									conditionSpinner.setSelection(tmpNumb);
								}
								else						// 입력한 메모 content에 임시저장
								{
									content += temp+"\n";
								}
								i++;
							}
						}
						catch (Exception e) { }
					}
				}
				// 임시저장된 메모 다시 세팅
				editMemoTxt.setText(content);
			}
		}
		
		// 취소버튼 정의
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() 
		{
			// 버튼 클릭시 액티비티 종료
			public void onClick(View v) 
			{
				finish();				
			}			
		});

		// 저장버튼 정의
		saveBtn = (Button) findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(new OnClickListener() 
		{
			// 버튼 클릭시 유저가 입력한 내용 저장
			public void onClick(View v) {
				int weatherNumb, conditionNumb;
				String dirPath = getFilesDir().getAbsolutePath();
				
				//무게,날씨,환자상태,메모 값 각 변수 선언 & 세팅
				String weightStr = weightTxtView.getText().toString()+"\n";
				weatherNumb = changeWeatherToNumb((String)weatherSpinner.getSelectedItem());
				String weatherStr = weatherNumb+"\n";
				conditionNumb = changeConditionToNumb((String)conditionSpinner.getSelectedItem());
				
				// 환자 상태  DiaryActivity에 넘겨주기위한 intent 
				SNIntent.putExtra("condition", conditionNumb);
				String conditionStr = conditionNumb+"\n";
				String memoStr = editMemoTxt.getText().toString();
				String pathStr = "/" + dateTxt + ".txt";
				// 저장할 파일 선언, 파일명은 해당 날짜로 저장
				File saveFile = new File(dirPath+pathStr);
				try 
				{	
					// 무게, 날씨, 환자상태, 메모 순서로 저장
					FileOutputStream fos = new FileOutputStream(saveFile);
					fos.write(weightStr.getBytes());
					fos.write(weatherStr.getBytes());
					fos.write(conditionStr.getBytes());
					fos.write(memoStr.getBytes());
					fos.close();
					
					// 환자상태 DiaryActivity로 전달
					startActivity(SNIntent);
					flag="1";
					
				}
				catch (Exception e) 
				{
					Toast.makeText(WritingDiaryView.this, "fail!!!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	
		// 선택된 날씨 리스너
		weatherSpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{ }
			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{ }
			
		});
		
		// 선택된 환자상태 리스너
		conditionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) 
			{ }
			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{ }
		});
	}
	
	
	public void sendData(String data)
	{
		TCPclient tp;
		tp = new TCPclient(data, EyeCanControl.socket, variable.getServerIP(),
				variable.getServerPort()); // TCPclient생성(11초기화)
		tp.run(); // run 메소드 호출

	}
	
	// 선택된 날씨의 상태를 각 숫자로 저장하기 위한 함수
	public int changeWeatherToNumb(String str)
	{
		if(str.equals("맑음"))
			return 0;
		else if(str.equals("흐림"))
			return 1;
		else if(str.equals("눈"))
			return 2;
		else if(str.equals("비"))
			return 3;
		else if(str.equals("안개"))
			return 4;
		return 0;
	}
	
	// 선택된 환자의 상태를 각 숫자로 저장하기 위한 함수
	public int changeConditionToNumb(String str)
	{
		if(str.equals("진정"))
			return 0;
		else if(str.equals("보통"))
			return 1;
		else if(str.equals("우울"))
			return 2;
		else if(str.equals("화남"))
			return 3;
		else if(str.equals("짜증"))
			return 4;
		return 0;
	}
}
