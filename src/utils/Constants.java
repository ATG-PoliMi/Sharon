package utils;

import behavior.simulator.extractor.NeedsActor;

public class Constants {	



	//Repast Simphony
	public static final int mapID = 1;
	public static final int mapSizeH = 30;
	public static final int mapSizeW = 60;
	public static final int SENSORSNUMBER = 20;

	/*public static final int mapID = 2;
		public static final int mapSizeH = 40;
		public static final int mapSizeW = 40;
		public static final int sensorsNumber = 0;
	 */

	public static final String STRUCTURE_LAYER_ID = "walls";

	//Behavior Simulator
	public static final int SLEEP_ID = 300;
	
	//Behavior Simulator NeedsParameters
	public static double HUNGER = 0.05;
	public static double HYGIENE = 0.03;
	public static double COMFORT = 0.03;
	public static double BLADDER = 0.05;
	public static double ENERGY = 0.03;
	public static double FUN = 0.05;
	
}
