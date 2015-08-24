/*
 *
 * SHARON - Human Activities Simulator
 * Author: ATG Group (http://atg.deib.polimi.it/)
 *
 * Copyright (C) 2015, Politecnico di Milano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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

    public int getMinuteS() {
        return (minute+(hour*60));
    }

	@Override
	public String toString() {
		return this.getHour() + ":" + this.getMinute();
	}

	public Time (double timeInstant) {
		hour = (int) timeInstant / 3600;
	    minute = ((int) timeInstant - hour * 3600) / 60;
	}

    public Time (String timeString){
        String[] tkns = timeString.split(":");
        hour = Integer.parseInt(tkns[0]);
        minute = Integer.parseInt(tkns[1]);
    }
}
