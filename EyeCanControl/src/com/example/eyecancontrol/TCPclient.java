package com.example.eyecancontrol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;

public class TCPclient implements Runnable 
{

	// private String return_msg;

	private String msg;	//전솔할 메세지
	private Socket socket;	//소켓 선언
	private String serverIP;	//연결할 IP
	private int serverPort;	//연결할 Port
	private String return_msg;	//서버로 부터 입력 받은 메세지
	
	//변수 초기화 생성자
	public TCPclient(String _msg,Socket socket,String serverIP, int serverPort) 
	{
		this.msg = _msg;
		this.socket = socket;
		this.serverIP = serverIP;
		this.serverPort = serverPort;
	}

	@Override
	public void run() 
	{
		
		try
		{
			Log.d("TCP", "C: Connecting...");	
			try 
			{
				Log.d("TCP", "C: Sending: '" + msg + "'"); 
				if(socket.isClosed())
					socket = new Socket(serverIP,serverPort);
				
				PrintWriter out = new PrintWriter(
						new BufferedWriter(new OutputStreamWriter(
								socket.getOutputStream())), true);
				out.println(msg); // msg 출력(서버로 보내기)
				Log.d("TCP", "C: Sent.");
				Log.d("TCP", "C: Done.");
				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				return_msg = in.readLine();// 서버에서 받기
				Log.d("TCP", "C: Server send to me this message -->"
						+ return_msg);
				
			}
			catch (Exception e) 
			{
				Log.e("TCP", "C: Error1", e);
			}
			finally 
			{
				socket.close();
			}
		}
		catch (Exception e) 
		{
			Log.e("TCP", "C: Error2", e);
		}
	}
	
	
	public void initial_run() 
	{
		
		try {
			
			Log.d("TCP", "C: Connecting...");
			
			try 
			{
				Log.d("TCP", "C: Sending: '" + msg + "'"); // msg = �Է°�
				if(socket.isClosed())
					socket = new Socket(serverIP,serverPort);
				PrintWriter out = new PrintWriter(
						new BufferedWriter(new OutputStreamWriter(
								socket.getOutputStream())), true);
				out.println("99"); // msg 출력(서버로 보내기)
				Log.d("TCP", "C: Sent.");
				Log.d("TCP", "C: Done.");
				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				return_msg = in.readLine();// 서버에서 받기

				Log.d("TCP", "C: Server send to me this message -->"
						+ return_msg);
				
			}
			catch (Exception e) 
			{
				Log.e("TCP", "C: Error1", e);
			}
			finally 
			{
				socket.close();
			}
		}
		catch (Exception e) 
		{
			Log.e("TCP", "C: Error2", e);
		}
	}
	
	//서버로 부터 받은 메세지 반환 메소드
	public String return_msg()
	{
		return return_msg;
	}
}

