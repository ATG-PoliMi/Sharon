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

package it.polimi.deib.atg.sharon.engine;

import com.sun.org.apache.xpath.internal.functions.FuncFalse;

import java.util.ArrayList;

public class ADL {

	private int id;
	private String name;
	private double[] days;
	private double[] weather; //[0]: sun probability,[1]: cloud probability, [2]: rain probability
	private Float[] timeDescription;
	private int minTime;
	private boolean active;
	
	private double rank;
	private ArrayList<String> needs = new ArrayList <String>();
	private ArrayList<ADLEffect> effects = new ArrayList <ADLEffect>();
	
	public ADL(int id, String name, double [] days, double [] weather, Float[] timeDescription, int minTime,
			ArrayList<String> needs, ArrayList<ADLEffect> effects) {
		super();
		this.id = id;
		this.name = name;
		this.days = days;
		this.weather = weather;
		this.timeDescription = timeDescription;
		this.minTime = minTime;
		this.active = false;
		
		this.needs = needs;
		this.effects = effects;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double[] getDays() {
		return days;
	}
	public double getExactDay(int i) {
		if ((i>=0)&&(i<7))
			return days[i];
		else
			return 0;
	}
	public void setDays(double[] days) {
		this.days = days;
	}
	public double[] getWeather() {
		return weather;
	}
	public void setWeather(double[] weather) {
		this.weather = weather;
	}
	public Float[] getTimeDescription() {
		return timeDescription;
	}
	public double getExactTimeDescription(int i) {
		if ((i>=0)&&(i<1440))
			return timeDescription[i];
		else
			return 0;
	}
	public void setTimeDescription(Float[] timeDescription) {
		this.timeDescription = timeDescription;
	}
	public int getMinTime() {
		return minTime;
	}
	public void setMinTime(int minTime) {
		this.minTime = minTime;
	}
	public double getRank() {
		return rank;
	}
	public void setRank(double rank) {
		this.rank = rank;
	}
	public ArrayList<String> getNeeds() {
		return needs;
	}
	public void setNeeds(ArrayList<String> needs) {
		this.needs = needs;
	}
	public ArrayList<ADLEffect> getEffects() {
		return effects;
	}
	public void setEffects(ArrayList<ADLEffect> effects) {
		this.effects = effects;
	}
	public boolean getActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}	
}
