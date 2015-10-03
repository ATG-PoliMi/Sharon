package it.polimi.deib.atg.sharon.configs;

import it.polimi.deib.atg.sharon.data.Sensorset;

import java.util.ArrayList;

public class SensorsetManager {
	private static SensorsetManager instance;
	private ArrayList<Sensorset> sensorsets;
	
	private SensorsetManager() {
		super();
		sensorsets=new ArrayList<Sensorset>();
	}
	
	public static SensorsetManager getInstance(){
		if(instance==null){
			instance=new SensorsetManager();
		}
		return instance;
	}
	
	public Sensorset getSensorsetByID(Integer idSensorset){
		for(Sensorset ss:sensorsets){
			if(ss.getIdSensorset()==idSensorset){
				return ss;
			}
		}
		return new Sensorset(idSensorset);
	}
	
	public void addSensorset(Integer idSensorset, Integer minTime, Integer maxTime, ArrayList<Integer> activatedSensorsId){
		Sensorset ss=this.getSensorsetByID(idSensorset);
		ss.setMinTime(minTime);
		ss.setMaxTime(maxTime);
		ss.setActivatedSensorsId(activatedSensorsId);
		sensorsets.add(ss);
	}
	
	
}
