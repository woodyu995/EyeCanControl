package com.example.calendarui;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import com.example.eyecancontrol.DiaryActivity;
import com.example.eyecancontrol.R;
import com.example.eyecancontrol.VariableSet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

public class CalendarMonthAdapter extends BaseAdapter {

	public static final String TAG = "CalendarMonthAdapter";
	
	Context mContext;
	
	public static int oddColor = Color.rgb(225, 225, 225);
	public static int headColor = Color.rgb(12, 32, 158);
	
	private int selectedPosition = -1;
	
	private MonthItem[] items;
	private int countColumn = 7;
	
	int mStartDay;
	int startDay;
	int curYear;
	int curMonth;
	int oriYear;
	int oriMonth;
	int oriDay;
	int oriDayOfWeek;
	int oriDayOfWeekInMonth;
	
	int firstDay;
	int lastDay;
	
	int[] condiNumb = new int[31];
	
	String dirPath;
	String content="", temp="";
	String tmpDate;
	
	Calendar mCalendar;
	boolean recreateItems = false;

	
	public CalendarMonthAdapter(Context context, String DAdirPath) 
	{
		super();
		mContext = context;
		dirPath = DAdirPath;
		init();
	}
	
	public CalendarMonthAdapter(Context context, AttributeSet attrs) 
	{
		super();
		mContext = context;
		init();
	}

	private void init() 
	{
		items = new MonthItem[7 * 6];
		
		mCalendar = Calendar.getInstance();
		
		oriYear = mCalendar.get(Calendar.YEAR);
		oriMonth = mCalendar.get(Calendar.MONTH);
		oriDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK)-1;
		oriDayOfWeekInMonth = mCalendar.get(Calendar.WEEK_OF_MONTH)-1;
		
		recalculate();
		resetDayNumbers();
		
	}
	
	public void recalculate() 
	{
		
		mCalendar.set(Calendar.DAY_OF_MONTH, 1);
		
		int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
		firstDay = getFirstDay(dayOfWeek);
		Log.d(TAG, "firstDay : " + firstDay);
		
		mStartDay = mCalendar.getFirstDayOfWeek();
		curYear = mCalendar.get(Calendar.YEAR);
		curMonth = mCalendar.get(Calendar.MONTH);

		
		lastDay = getMonthLastDay(curYear, curMonth);
		int diff = mStartDay - Calendar.SUNDAY - 1;
        startDay = getFirstDayOfWeek();
	}
	
	public void setPreviousMonth() 
	{
		mCalendar.add(Calendar.MONTH, -1);
        recalculate();
        resetDayNumbers();
        selectedPosition = -1;
	}
	
	public void setNextMonth() 
	{
		mCalendar.add(Calendar.MONTH, 1);
        recalculate();
        resetDayNumbers();
        selectedPosition = -1;
	}
	
	public void setCurrentMonth() 
	{
		init();
		recalculate();
		resetDayNumbers();
		selectedPosition = -1;
	}
	
	public void resetDayNumbers() 
	{
		for (int i = 0; i < 42; i++) 
		{
			int dayNumber = (i+1) - firstDay;
			if (dayNumber < 1 || dayNumber > lastDay) 
			{
				dayNumber = 0;
			}
				items[i] = new MonthItem(dayNumber);
		}
	}
	
	public int setDayBackgrounds(String curDate) 
	{
		//dirPath = getDir();
		File savedFile = new File(dirPath);
		if(!savedFile.exists()) 
		{
			savedFile.mkdirs();
			return 0;
		}
		else
		{
			if(savedFile.listFiles().length > 0) 
			{
				for(File f : savedFile.listFiles()) 
				{
					String str = f.getName();
					
					if(curDate.equals(str)) 
					{
						String loadPath = dirPath+"/"+str;
						try {
							FileInputStream fis = new FileInputStream(loadPath);
							BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
							
							Log.v(null, "path :"+loadPath);
							int i = 0;
							while( (temp = bufferReader.readLine()) != null ) {
								if(i == 2)
								{
									int tmpNumb = 0;
									
									tmpNumb=Integer.parseInt(temp);
									return (tmpNumb+1);
									
								}
								
								i++;
								
							}
						}
						catch (Exception e) { }
					}					
				}
			}
			else
				return 0;
			
		}
		return 0;
	}
	
	private int getFirstDay(int dayOfWeek) 
	{
		int result = 0;
		if (dayOfWeek == Calendar.SUNDAY) 
		{
			result = 0;
		} 
		else if (dayOfWeek == Calendar.MONDAY) 
		{
			result = 1;
		} 
		else if (dayOfWeek == Calendar.TUESDAY) 
		{
			result = 2;
		} 
		else if (dayOfWeek == Calendar.WEDNESDAY) 
		{
			result = 3;
		} 
		else if (dayOfWeek == Calendar.THURSDAY) 
		{
			result = 4;
		}
		else if (dayOfWeek == Calendar.FRIDAY) 
		{
			result = 5;
		} 
		else if (dayOfWeek == Calendar.SATURDAY) 
		{
			result = 6;
		}
		
		return result;
	}
	
	
	public int getCurYear() 
	{
		return curYear;
	}
	
	public int getCurMonth() 
	{
		return curMonth;
	}
	
	public int getOriDay() 
	{
		return oriDay;
	}
	
	
	public int getNumColumns() 
	{
		return 7;
	}

	public int getCount() 
	{
		return 7 * 6;
	}

	public Object getItem(int position) 
	{
		return items[position];
	}

	public long getItemId(int position) 
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) 
	{
		//Log.d(TAG, "getView(" + position + ") called.");
		
		MonthItemView itemView;
		
		
		if (convertView == null) 
		{
			itemView = new MonthItemView(mContext);
		}
		else 
		{
			itemView = (MonthItemView) convertView;
		}	
		GridView.LayoutParams params = new GridView.LayoutParams(
				GridView.LayoutParams.MATCH_PARENT,	179);
		
		
		
		int rowIndex = position / countColumn;
		int columnIndex = position % countColumn;
		int i = items[position].getDay();
		itemView.setItem(items[position]);
		itemView.setLayoutParams(params);
		itemView.setPadding(2, 2, 2, 2);
		itemView.setGravity(Gravity.LEFT);
		if (columnIndex == 0) 
		{
			itemView.setTextColor(Color.RED);
		} 
		else if(columnIndex == 6) 
		{
			itemView.setTextColor(Color.BLUE);
		}
		else
		{
			itemView.setTextColor(Color.BLACK);
		}
		
		if(curMonth < 9) 
		{
			if(i < 10) 
				tmpDate = curYear + "0" + (curMonth+1) + "0" + items[position].getDay() + ".txt";
			else
				tmpDate = curYear + "0" + (curMonth+1) + "" + items[position].getDay() + ".txt";			
		}
		else
		{
			if(i < 10) 
				tmpDate = curYear + "" + (curMonth+1) + "0" + items[position].getDay() + ".txt";
			else
				tmpDate = curYear + "" + (curMonth+1) + "" + items[position].getDay() + ".txt";
		}
		int flag = setDayBackgrounds(tmpDate);
		Log.v(null, tmpDate + " - flag :"+flag);
		//선택된 날짜에 대해
		if (position == getSelectedPosition()) 
		{
			if(i != 0) 
			{ 
				if(flag == 0) 
				{
					itemView.setTypeface(null, Typeface.BOLD);
					itemView.setBackgroundResource(R.drawable.selected_border);
				}
				else if(flag == 1)
				{
					itemView.setTypeface(null, Typeface.BOLD);
					itemView.setBackgroundResource(R.drawable.selected_calm);
				}
				else if(flag == 2)
				{
					itemView.setTypeface(null, Typeface.BOLD);
					itemView.setBackgroundResource(R.drawable.selected_normal);
				}
				else if(flag == 3)
				{
					itemView.setTypeface(null, Typeface.BOLD);
					itemView.setBackgroundResource(R.drawable.selected_depressed);
				}
				else if(flag == 4)
				{
					itemView.setTypeface(null, Typeface.BOLD);
					itemView.setBackgroundResource(R.drawable.selected_anxiety);
				}
				else
				{
					itemView.setTypeface(null, Typeface.BOLD);
					itemView.setBackgroundResource(R.drawable.selected_nervous);
				}
			}
			
        }
		else 
        {		//아닌 날짜에 대해
        	if(flag==0) 
        	{  // 데이터가 없을 때
	        	itemView.setTypeface(null, Typeface.NORMAL);
	        	itemView.setBackgroundColor(Color.WHITE);
	    		
	        	//선택되지않은 오늘이라면
	    		if (oriMonth == curMonth && oriYear == curYear && columnIndex == oriDayOfWeek
	    				&& rowIndex == oriDayOfWeekInMonth) {	
	    			itemView.setTypeface(null, Typeface.BOLD);
	    			itemView.setBackgroundResource(R.drawable.border);
	    			
	    		}
        	}
        	else if(flag == 1)
        	{  // 데이터가 있을 때
        		itemView.setTypeface(null, Typeface.NORMAL);
	        	itemView.setBackgroundResource(R.drawable.calm_sticker);
	    		
	    		if (oriMonth == curMonth && oriYear == curYear && columnIndex == oriDayOfWeek
	    				&& rowIndex == oriDayOfWeekInMonth) {	
	    			itemView.setTypeface(null, Typeface.BOLD);
	    			itemView.setBackgroundResource(R.drawable.selected_calm);
	    			
	    		}
        	}
        	else if(flag == 2)
        	{  // 데이터가 있을 때
        		itemView.setTypeface(null, Typeface.NORMAL);
	        	itemView.setBackgroundResource(R.drawable.normal_sticker);
	    		
	    		if (oriMonth == curMonth && oriYear == curYear && columnIndex == oriDayOfWeek
	    				&& rowIndex == oriDayOfWeekInMonth) {	
	    			itemView.setTypeface(null, Typeface.BOLD);
	    			itemView.setBackgroundResource(R.drawable.today_normal);
	    		}
        	}
        	else if(flag == 3)
        	{  // 데이터가 있을 때
        		itemView.setTypeface(null, Typeface.NORMAL);
	        	itemView.setBackgroundResource(R.drawable.depressed_sticker);
	    		
	    		if (oriMonth == curMonth && oriYear == curYear && columnIndex == oriDayOfWeek
	    				&& rowIndex == oriDayOfWeekInMonth) 
	    		{	
	    			itemView.setTypeface(null, Typeface.BOLD);
	    			itemView.setBackgroundResource(R.drawable.today_depressed);
	    		}
        	}
        	else if(flag == 4)
        	{  // 데이터가 있을 때
        		itemView.setTypeface(null, Typeface.NORMAL);
	        	itemView.setBackgroundResource(R.drawable.anxiety_sticker);
	    		
	    		if (oriMonth == curMonth && oriYear == curYear && columnIndex == oriDayOfWeek
	    				&& rowIndex == oriDayOfWeekInMonth) 
	    		{	
	    			itemView.setTypeface(null, Typeface.BOLD);
	    			itemView.setBackgroundResource(R.drawable.today_anxiety);
	    		}
        	}
        	else
        	{  // 데이터가 있을 때
        		itemView.setTypeface(null, Typeface.NORMAL);
	        	itemView.setBackgroundResource(R.drawable.nervous_sticker);
	    		
	    		if (oriMonth == curMonth && oriYear == curYear && columnIndex == oriDayOfWeek
	    				&& rowIndex == oriDayOfWeekInMonth) 
	    		{	
	    			itemView.setTypeface(null, Typeface.BOLD);
	    			itemView.setBackgroundResource(R.drawable.today_nervous);
	    		}
        	}
        }
		return itemView;
	}

    public static int getFirstDayOfWeek() 
    {
        int startDay = Calendar.getInstance().getFirstDayOfWeek();
        if (startDay == Calendar.SATURDAY) 
        {
            return Time.SATURDAY;
        }
        else if (startDay == Calendar.MONDAY) 
        {
            return Time.MONDAY;
        }
        else
        {
            return Time.SUNDAY;
        }
    }

    private int getMonthLastDay(int year, int month)
    {
    	switch (month) 
    	{
 	   		case 0:
      		case 2:
      		case 4:
      		case 6:
      		case 7:
      		case 9:
      		case 11:
      			return (31);

      		case 3:
      		case 5:
      		case 8:
      		case 10:
      			return (30);

      		default:
      			if(((year%4==0)&&(year%100!=0)) || (year%400==0) ) 
      			{
      				return (29);   
      			}
      			else
      			{ 
      				return (28);
      			}
 	   	}
 	}
    
	public void setSelectedPosition(int selectedPosition) 
	{
		this.selectedPosition = selectedPosition;
	}

	public int getSelectedPosition() 
	{
		return selectedPosition;
	}


}
