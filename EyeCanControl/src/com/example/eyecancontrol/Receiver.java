package com.example.eyecancontrol;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.R.integer;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
 
public class Receiver extends Thread 
{   
    String status;	//상태값 저장 변수
    VariableSet variable;	//공통으로 사용할 변수 선언
    Handler mHandler;	//Handler 선언
    public Receiver(VariableSet variable,Handler mHandler)//(Socket r_socket,String serverIP, int serverPort) 
    {
    	this.variable = variable;	//공통 변수 초기화
    	this.mHandler = mHandler;	//Handler초기화
    }
 
    @Override
    public void run() 
    {
    	Socket test_socket = null;
        while (true) 
        {
            try 
            {
            	
            	ServerSocket serversocket = new ServerSocket(8000);	//소켓 생성
            	test_socket = serversocket.accept();	//연결 대기
            	BufferedReader in = new BufferedReader(new InputStreamReader(test_socket.getInputStream()));
                status = in.readLine();	//통신값 읽기
                //Handler를 통해 메세지 전달 
                Message msg = Message.obtain(mHandler,Integer.parseInt(status));
                mHandler.sendMessage(msg);
                
                
            } 
            catch (IOException e) 
            {
            	Log.d("Exception",e.getMessage());
            }
            catch(Exception e1)
            {
            	Log.d("Exception",e1.getMessage());
            	
            }
            finally
            {
            	try 
            	{
					test_socket.close();	//소켓 닫기
				}
            	catch (IOException e) 
				{
					e.printStackTrace();
				}
            }
        }
    }
}