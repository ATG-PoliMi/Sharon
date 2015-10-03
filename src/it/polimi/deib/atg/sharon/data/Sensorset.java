package it.polimi.deib.atg.sharon.data;

import java.util.ArrayList;

public class Sensorset {
	private Integer idSensorset;
	private Integer minTime;
	private Integer maxTime;
	private ArrayList<Integer> activatedSensorsId;
	
	public Sensorset(Integer idSensorset, Integer minTime, Integer maxTime,
			ArrayList<Integer> activatedSensorsId) {
		super();
		this.idSensorset = idSensorset;
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.activatedSensorsId = activatedSensorsId;
	}
	
	public Sensorset(Integer idSensorset) {
		super();
		this.idSensorset = idSensorset;
		this.minTime = 0;
		this.maxTime = 0;
		this.activatedSensorsId = new ArrayList<Integer>();
	}
	
	public Integer getIdSensorset() {
		return idSensorset;
	}
	public void setIdSensorset(Integer idSensorset) {
		this.idSensorset = idSensorset;
	}
	public Integer getMinTime() {
		return minTime;
	}
	public void setMinTime(Integer minTime) {
		this.minTime = minTime;
	}
	public Integer getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(Integer maxTime) {
		this.maxTime = maxTime;
	}
	public ArrayList<Integer> getActivatedSensorsId() {
		return activatedSensorsId;
	}
	public void setActivatedSensorsId(ArrayList<Integer> activatedSensorsId) {
		this.activatedSensorsId = activatedSensorsId;
	}
	
	
}
