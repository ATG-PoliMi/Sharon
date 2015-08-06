package it.polimi.deib.atg.sharon.utils;

public class Time {
	public int hour;
	public int minute;
	
	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}	

	public Time (double tick) {	
		hour = (int) tick / 3600;	    
	    minute = ((int) tick - hour * 3600) / 60;
	}
}
