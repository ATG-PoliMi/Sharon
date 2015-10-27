package it.polimi.deib.atg.sharon.data;

import java.util.ArrayList;
import java.util.Random;

public class Sensorset {
	public int getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(int maxDuration) {
		this.maxDuration = maxDuration;
	}

	public float getExpValTimeDist() {
		return expValTimeDist;
	}

	public void setExpValTimeDist(float expValTimeDist) {
		this.expValTimeDist = expValTimeDist;
	}
	private Integer idSensorset;
	private int maxDuration;
	private float expValTimeDist;
	private ArrayList<Integer> activatedSensorsId;
	
	public Sensorset(Integer idSensorset, int maxdur, float expValTimeDist ,ArrayList<Integer> activatedSensorsId) {
		super();
		this.idSensorset = idSensorset;
		this.maxDuration=maxdur;
		this.expValTimeDist=expValTimeDist;
		this.activatedSensorsId = activatedSensorsId;
	}
	
	public Sensorset(Integer idSensorset) {
		super();
		this.idSensorset = idSensorset;
		this.maxDuration=0;
		this.expValTimeDist=(float) 0;
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
	
	public Integer getDurationUsingDistribution(){
		Random randomDistrTimeSS=new Random();
		float p_t=randomDistrTimeSS.nextFloat();
		int t=(int) Math.ceil(this.expValTimeDist*Math.log(1-p_t));
		return t;
	}
	
}
