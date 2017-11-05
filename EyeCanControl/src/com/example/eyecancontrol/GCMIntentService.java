package com.example.eyecancontrol;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService 
{

	private static void generateNotification(Context context, String message) 
	{

		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		Handler mHandler = EyeCanControl.mHandler;
		//소변통 관련 파일 생성
		if(message.charAt(message.length() -1 ) == '1')	//if message is urine
		{
			try 
			{
				Message msg = Message.obtain(mHandler, Integer.parseInt("42"));	//set urine status for set message
				mHandler.sendMessage(msg);	//handler send message
					
			}
			catch(Exception e)
			{
			
			}
		}
		else
		{
			try
			{
				Message msg = Message.obtain(mHandler, Integer.parseInt("51"));	//set emergency status for set message
				mHandler.sendMessage(msg);	//handler send message
				EyeCanControl.emergencyMsg = message.substring(0,message.length() -1 );	//send message for insert Emergency Table(DB)
			}
			catch(Exception e)
			{
				e.getMessage();
			}

		}
		message = message.substring(0,message.length() - 1);	//output message set
		//notification 관련
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		//푸시 알림
		Notification notification = new Notification(icon, message, when);

		//title값 저장
		String title = context.getString(R.string.app_name);

		Intent notificationIntent = new Intent(context, EyeCanControl.class);

		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, title, message, intent);

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		//진동 & 소리 설정
		notification.vibrate = new long[] {100, 100, 200, 200, 300, 300, 400, 400, 500, 500};
		notification.defaults |= Notification.DEFAULT_SOUND;



		notificationManager.notify(0, notification);

	}

	@Override
	protected void onError(Context arg0, String arg1) {

	}

	@Override
	protected void onMessage(Context context, Intent intent) {

		String msg = intent.getStringExtra("msg");
		Log.e("getmessage", "getmessage:" + msg);
		generateNotification(context, msg);

	}
	//기기 등록 관련LOG생성
	@Override
	protected void onRegistered(Context context, String reg_id) {
		Log.e("키를 등록합니다.(GCM INTENTSERVICE)", reg_id);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.e("키를 제거합니다.(GCM INTENTSERVICE)", "제거되었습니다.");
	}

}
