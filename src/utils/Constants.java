package utils;

import behavior.simulator.extractor.Needs;

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

	//Histogram
	public static int ADLCOUNT = 17;
	//Behavior Simulator
	public static final int SLEEP_ID = 7;
	
	//Behavior Simulator NeedsParameters
	public static double HUNGER = 0.0033;
	public static double HYGIENE = 0.0006;
	public static double COMFORT = 0.003;
	public static double BLADDER = 0.005;//0.0056
	public static double ENERGY = 0.0025;
	public static double FUN = 0.004;
	public static double SOCIALITY = 0.001;
	public static double DIRTINESS = 0.0001;
	public static double STOCK = 0.0001;
	
}
