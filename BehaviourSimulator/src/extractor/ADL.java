package extractor;

import java.util.ArrayList;

public class ADL {
	private int id;
	private String name;
	private ArrayList <Integer> days = new ArrayList <Integer>();	
	private int weather; //0: Independent, 1: sunny, 2: cloudy, 3: rainy
	private int tmean;
	private int tvariability;
	private int mandatory;
	private int bestTime;
	private int type;
	private int cyclicalityN;
	private int cyclicalityD;
	
	private double rank;
	private int doneToday;
	
	
	
	public ADL(int id, String name, ArrayList<Integer> days, int weather, int tmean,
			int tvariability, int mandatory, int bestTime, int type,
			int cyclicalityD, ArrayList<String> needs, ArrayList<ADLEffect> effects) {
		super();
		this.id = id;
		this.name = name;
		this.days = days;
		this.weather = weather;
		this.tmean = tmean;
		this.tvariability = tvariability;
		this.mandatory = mandatory;
		this.bestTime = bestTime;
		this.type = type;
		this.cyclicalityD = cyclicalityD;
		this.cyclicalityN = 0;
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
	public ArrayList<Integer> getDays() {
		return days;
	}
	public void setDays(ArrayList <Integer> days) {
		this.days = days;
	}
	public int getWeather() {
		return weather;
	}
	public void setWeather(int weather) {
		this.weather = weather;
	}
	public int getTmean() {
		return tmean;
	}
	public void setTmean(int tmean) {
		this.tmean = tmean;
	}
	public int getTvariability() {
		return tvariability;
	}
	public void setTvariability(int tvariability) {
		this.tvariability = tvariability;
	}
	public int isMandatory() {
		return mandatory;
	}
	public void setMandatory(int mandatory) {
		this.mandatory = mandatory;
	}
	public int getBestTime() {
		return bestTime;
	}
	public void setBestTime(int bestTime) {
		this.bestTime = bestTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCyclicalityN() {
		return cyclicalityN;
	}
	public void setCyclicalityN(int cyclicalityN) {
		this.cyclicalityN = cyclicalityN;
	}
	public int getCyclicalityD() {
		return cyclicalityD;
	}
	public void setCyclicalityD(int cyclicalityD) {
		this.cyclicalityD = cyclicalityD;
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
		cyclicalityN = 0;
	}
}
