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

package it.polimi.deib.atg.sharon.data;


public class Day {
	private static Day instance;
	
	//TODO: Change distribution of conditions
	private Day(){
		this.weather = 1;
		this.weekDay = 1;		
	}

	private int weather;
	private int weekDay;
	
	
	public int getWeather() {
		return weather;
	}
	public int getWeekDay() {
		return weekDay;
	}
	public void nextDay() {
		//TODO: insert a more complex weather model
		weather = weather == 1 ? 3 			: 1;	
		weekDay++;
	}
	
	public static synchronized Day getInstance() {

		if(instance==null) {
			instance=new Day();
		}
		return instance;
	}
}