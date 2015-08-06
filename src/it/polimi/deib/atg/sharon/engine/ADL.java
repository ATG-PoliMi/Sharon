package it.polimi.deib.atg.sharon.engine;

import java.util.ArrayList;

public class ADL {

	private int id;
	private String name;
	private double[] days;
	private double[] weather; //[0]: sun probability,[1]: cloud probability, [2]: rain probability
	private Float[] timeDescription;
	private int minTime;
	private double active;
	
	private double rank;
	private int doneToday;
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
		this.active = 0;
		
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
	public int getDoneToday() {
		return doneToday;
	}
	public void setDoneToday(int doneToday) {
		this.doneToday = doneToday;
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
	public double getActive() {
		return active;
	}
	public void setActive(double active) {
		this.active = active;
	}	
}
