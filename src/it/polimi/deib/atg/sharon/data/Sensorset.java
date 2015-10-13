package it.polimi.deib.atg.sharon.data;

import java.util.ArrayList;
import java.util.Random;

public class Sensorset {
	public ArrayList<Integer> getDurationDistribution() {
		return durationDistribution;
	}

	public void setDurationDistribution(ArrayList<Integer> durationDistribution) {
		this.durationDistribution = durationDistribution;
	}
	private Integer idSensorset;
	private ArrayList<Integer> durationDistribution;
	private ArrayList<Integer> activatedSensorsId;
	
	public Sensorset(Integer idSensorset, ArrayList<Integer> durDistr,
			ArrayList<Integer> activatedSensorsId) {
		super();
		this.idSensorset = idSensorset;
		this.durationDistribution = durDistr;
		this.activatedSensorsId = activatedSensorsId;
	}
	
	public Sensorset(Integer idSensorset) {
		super();
		this.idSensorset = idSensorset;
		this.durationDistribution = new ArrayList<Integer>();
		this.activatedSensorsId = new ArrayList<Integer>();
	}
	
	public Integer getIdSensorset() {
		return idSensorset;
	}
	public void setIdSensorset(Integer idSensorset) {
		this.idSensorset = idSensorset;
	}

	public ArrayList<Integer> getActivatedSensorsId() {
		return activatedSensorsId;
	}
	public void setActivatedSensorsId(ArrayList<Integer> activatedSensorsId) {
		this.activatedSensorsId = activatedSensorsId;
	}

	public Integer getDurationUsingDistribution(Random rndD) {
		Integer pos=rndD.nextInt(100);
		return this.durationDistribution.get(pos);
	}
	
	
}
