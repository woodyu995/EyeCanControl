package com.example.eyecancontrol;


import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class CreateSocket 
{	
	private SocketAddress address;	//Server의 IP와 Port를 저장할 변수 선언
	private Socket socket;	//Socket변수 선언
	public CreateSocket(String ip, int port)
	{
		try 
		{
			address = new InetSocketAddress(ip,port);	//SocketAddress 객체 생성
			socket = new Socket();		//Socket객체 생성
		}
		
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//각 객체의 get/set메소드
	public Socket getSocket()
	{
		return socket;
	}
	public SocketAddress getAddress()
	{
		return address;
	}
}
