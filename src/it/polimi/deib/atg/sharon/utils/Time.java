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

	@Override
	public String toString() {
		return this.getHour() + ":" + this.getMinute();
	}

	public Time (double timeInstant) {
		hour = (int) timeInstant / 3600;
	    minute = ((int) timeInstant - hour * 3600) / 60;
	}
}
