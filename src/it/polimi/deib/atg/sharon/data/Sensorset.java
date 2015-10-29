package it.polimi.deib.atg.sharon.data;

import java.util.ArrayList;
import java.util.Random;

public class Sensorset {
	private Integer idSensorset;
	private ArrayList<Integer> activatedSensorsId;
	
	public Sensorset(Integer idSensorset,ArrayList<Integer> activatedSensorsId) {
		super();
		this.idSensorset = idSensorset;
		this.activatedSensorsId = activatedSensorsId;
	}
	
	public Sensorset(Integer idSensorset) {
		super();
		this.idSensorset = idSensorset;
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
	
	public Integer getDurationUsingDistribution(Float expValTimeDist,Integer scheduledTime){
		Random randomDistrTimeSS=new Random();
		float p_t=randomDistrTimeSS.nextFloat();
		int t=(int) Math.ceil(scheduledTime*(-expValTimeDist*Math.log(1-p_t)));
		return t;
	}
	
}
