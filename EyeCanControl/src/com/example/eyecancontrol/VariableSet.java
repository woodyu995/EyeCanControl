package com.example.eyecancontrol;

import java.io.Serializable;

public class VariableSet implements Serializable
{
	private static final long serialVersionUID = 1L;
	private final int toastTime = 1000;
	private final int connectionTime = 2000;
	private int serverPort;
	private final byte urineT = 't';
	private final byte urineF = 'f';
	private char urineStatus;	//소변통 상태
	private String phoneIp = null;
	private String selectDate = null;
	private String initialStatus = null;	//초기값 저장 변수
	private String fanStatus = null; // fan상태 false
	private String serverIP = null;
	private Boolean cancel = false;
	private Boolean bedUp = false;
	private Boolean bedDown = false;
	private Boolean ledStatus = false; // led상태 false
	private Boolean windowStatus = false; 
	
	
	
	//각변수의 set/get메소드
	public String getSelectDate() {
		return selectDate;
	}
	public void setSelectDate(String selectDate) {
		this.selectDate = selectDate;
	}
	public boolean isBedUp() {
		return bedUp;
	}
	public void setBedUp(Boolean bedUp) {
		this.bedUp = bedUp;
	}
	public boolean isBedDown() {
		return bedDown;
	}
	public void setBedDown(Boolean bedDown) {
		this.bedDown = bedDown;
	}
	public String getInitial_status() {
		return initialStatus;
	}
	public void setInitialStatus(String initial_status) {
		this.initialStatus = initial_status;
	}
	public Boolean getLedStatus() {
		return ledStatus;
	}
	public void setLedStatus(Boolean ledStatus) {
		this.ledStatus = ledStatus;
	}
	public String getFanStatus() {
		return fanStatus;
	}
	public void setFanStatus(String fanStatus) {
		this.fanStatus = fanStatus;
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public Boolean getCancel() {
		return cancel;
	}
	public void setCancel(Boolean cancel) {
		this.cancel = cancel;
	}
	
	public char getUrineStatus() {
		return urineStatus;
	}
	public void setUrineStatus(char urine_status) {
		this.urineStatus = urine_status;
	}
	public String getPhoneIp() {
		return phoneIp;
	}
	public void setPhoneIp(String phoneIp) {
		this.phoneIp = phoneIp;
	}
	public int getToastTime() {
		return toastTime;
	}
	public int getConnectionTime() {
		return connectionTime;
	}
	public Boolean getWindowStatus() {
		return windowStatus;
	}
	public void setWindowStatus(Boolean window_status) {
		this.windowStatus = window_status;
	}
	public byte getUrineT() {
		return urineT;
	}
	public byte getUrineF() {
		return urineF;
	}
	
	
	
	
	
	
	
	
	
}
