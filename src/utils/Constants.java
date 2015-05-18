package utils;

import sharon.engine.Needs;

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
	public static double STRESS = 0.003;
	public static double SWEAT = 0.0006;	
	public static double TOILETING = 0.009;//0.0056
	public static double TIREDINESS = 0.0010; //0.0025
	public static double BOREDOM = 0.006;
	public static double ASOCIALITY = 0.001;
	public static double DIRTINESS = 0.0001;
	public static double OUTOFSTOCK = 0.0001;
	
}
