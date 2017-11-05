package com.example.eyecancontrol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_Open_Emergency extends SQLiteOpenHelper 
{

	public DB_Open_Emergency(Context context) 
	{
		super(context, "Emergency", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE Emergency(DATE DATETIME primary key, Message VARCHAR);");
		//Table 생성 SQL문
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS Emergency");
		//Table 삭제 SQL문
		onCreate(db);
	}

}
