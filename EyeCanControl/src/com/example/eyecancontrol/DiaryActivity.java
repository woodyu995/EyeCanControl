package com.example.eyecancontrol;

import java.util.Calendar;

import com.example.calendarui.CalendarMonthAdapter;
import com.example.calendarui.CalendarMonthView;
import com.example.calendarui.MonthItem;
import com.example.calendarui.OnDataSelectionListener;
import com.example.calendarui.WritingDiaryView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class DiaryActivity extends Activity
{

	CalendarMonthView monthView;	//CalendarMonthView 선언
	CalendarMonthAdapter monthViewAdapter;	//CalendarMonth 관련된 Adapter선언
	TextView monthText;	//TextView선언(Month관련된 화면 출력용)
	GestureDetector mGestureDetector;	//GestureDetector선언
	Intent WDVIntent;	//Intent변수 선언
	MonthItem curItem;	//MonthItem 변수 선언
	Calendar mCal;	//Calendar 변수 선언
	VariableSet variable;	//VariableSet변수 선언
	int curYear;	//현재 연도를 저장하기 위한 변수 선언
	int curMonth;	//현재 월을 저장하기 위한 변수 선언
	int curDay;		//현재 날짜를 저장하기 위한 변수
	int today;	// 오늘 날짜를 저장하기 위한 변수
	String curDateTxt;	//Date의 Text값을 저장하기 위한 문자열 변수
	String todayTxt;	//현재 날짜를 저장하기 위한 문자열 변수
	String dirPath;	//다이어리 파일 저장을 위한 Path 저장할 문자열 변수

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_diary);
        
        setDirPath();	//디렉토리 Path저장을 위한 메소드 호출
        Toast.makeText(DiaryActivity.this, dirPath, Toast.LENGTH_SHORT).show();	//Toast메세지 창 출력
        Log.v(null, "path :"+dirPath);	//log출력
        
        variable = (VariableSet)getIntent().getExtras().get("variable");	//이전 Activity의 VariableSet값 가져오기
        WDVIntent = new Intent(DiaryActivity.this, WritingDiaryView.class);	//Intent값 가져오기
        monthView = (CalendarMonthView) findViewById(R.id.monthView);	//monthView에 ID값 할당
        monthViewAdapter = new CalendarMonthAdapter(this, dirPath);	//mothviewAdapter 디렉토리 Path를 이용 하여 객체 생성
        monthView.setAdapter(monthViewAdapter);	//monthView에 Adapter 추가
        
        monthView.setOnDataSelectionListener(new OnDataSelectionListener() //monthview에 리스너 추가
        {
			public void onDataSelected(AdapterView parent, View v, int position, long id) 
			{

				curItem = (MonthItem) monthViewAdapter.getItem(position);	//monthViewAdapter의 getItem메소드 호출
				curDay = curItem.getDay();	//날짜 받아오기

				Log.d("CalendarMonthViewActivity", "Selected : " + curDay);	//LOG기록
				
				//putExtra : 다음 Activity에 Parameter로 입력한 객체 또는 변수 전달
				if(curDay != 0)
				{
					if(curMonth < 9) 
					{
						WDVIntent.putExtra("month", "0"+(curMonth+1));
						if(curDay < 10)
							WDVIntent.putExtra("date", "0"+curDay);
						else
							WDVIntent.putExtra("date", curDay+"");
					}
					else 
					{
						WDVIntent.putExtra("month", (curMonth+1)+"");
						if(curDay < 10)
							WDVIntent.putExtra("date", "0"+curDay);
						else
							WDVIntent.putExtra("date", curDay+"");
					}
					WDVIntent.putExtra("variable", variable);
					WDVIntent.putExtra("year", curYear+"");
					
					startActivity(WDVIntent);	//다음 Activity실행
				}
			}
		});
        
        //mGestureDetector객체 생성과 함께 리스너 구현
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener()
        {
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
            	if(velocityX < 0)
            	{
            		monthViewAdapter.setNextMonth();
            		monthViewAdapter.notifyDataSetChanged();

            		setMonthText();
                } 
            	else if(velocityX > 0)
            	{
            		monthViewAdapter.setPreviousMonth();
            		monthViewAdapter.notifyDataSetChanged();

            		setMonthText();
            	}
               return false;
            }
        });

        monthText = (TextView) findViewById(R.id.dateTxt);	//monthText ID값 등록
        setMonthText();	//setMonthText메소드 설정
        Button monthToday = (Button) findViewById(R.id.todayBtn);	// monthToday 변수 ID값 할당
        monthToday.setOnClickListener(new OnClickListener() 	//monthToday 리스너 추가
        {
			@Override
			public void onClick(View v) 
			{
				
				monthViewAdapter.setCurrentMonth();
				monthViewAdapter.notifyDataSetChanged();
				setMonthText();
			}
        });
        
        
        Button monthPrevious = (Button) findViewById(R.id.monthPrevious);	//monthPrevious변수에 ID값 할당
        monthPrevious.setOnClickListener(new OnClickListener() 	//monthPrevious 에 리스너 추가
        {
        	public void onClick(View v) {
        		monthViewAdapter.setPreviousMonth();
        		monthViewAdapter.notifyDataSetChanged();

        		setMonthText();	//setMonthText 메소드 실행
        	}
        });

        Button monthNext = (Button) findViewById(R.id.monthNext);	//monthNext변수에 ID값 할당
        monthNext.setOnClickListener(new OnClickListener() 	//monthNext변수에 리스너 추가
        {
        	public void onClick(View v) 
        	{
        		monthViewAdapter.setNextMonth();
        		monthViewAdapter.notifyDataSetChanged();

        		setMonthText();	//setMonthText 메소드 실행
        	}
        });

    }
    
    //해당 달에 해당하는 Text를 설정한다
    private void setMonthText() 
    {
    	curYear = monthViewAdapter.getCurYear();
        curMonth = monthViewAdapter.getCurMonth();
        if(curMonth<9)
        	monthText.setText(curYear + ".0" + (curMonth+1));
        else
        	monthText.setText(curYear + "." + (curMonth+1));
    }
    
    //현제 저장할 폴더의 절대 주소를 가져온다
    public String setDirPath() 
    {
    	return dirPath = getFilesDir().getAbsolutePath();
    }
    
    //버튼 click이벤트 발생을 위한 메소드
    public boolean dispatchTouchEvent(MotionEvent ev) 
    {
        if(mGestureDetector.onTouchEvent(ev))
        {
         return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    //옵션 메뉴 호출을 위한 메소드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
