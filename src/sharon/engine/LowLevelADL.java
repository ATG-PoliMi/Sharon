package sharon.engine;

import java.util.ArrayList;

import sharon.data.Station;

public class LowLevelADL {
	
	int id;
	String name;
	private ArrayList <Station> stations = new ArrayList<Station>();
	
	
	public LowLevelADL(int id, String name, ArrayList<Station> stations) {
		super();
		this.id 		= id;
		this.name 		= name;
		this.stations 	= stations;
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
	public ArrayList<Station> getStations() {
		return stations;
	}
	public void setStations(ArrayList<Station> stations) {
		this.stations = stations;
	}
	
}
