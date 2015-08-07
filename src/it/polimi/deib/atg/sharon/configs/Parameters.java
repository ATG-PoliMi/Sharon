package it.polimi.deib.atg.sharon.configs;

import java.util.ArrayList;
import java.util.Iterator;

import it.polimi.deib.atg.sharon.Main;
import it.polimi.deib.atg.sharon.engine.Needs;

/**
 * This class contains all the parameters of the simulation such as:
 * <ul>
 * <li>Needs growth rate
 * <li>Drifts to apply to the growth rates
 * </ul>
 * <p>
 * It's a singleton class, its instance is contained in its member @see {@link #instance}instance, 
 * accessible only with methods @see {@link #getInstance()} and @see {@link #setInstance(Parameters)}.
 * <p>
 * It contains an array of Floats called @see #NeedsParameters, that contains the needs growth rate 
 * in the same order of @see Needs, accessible only with methods @see {@link #getNeedsParameters()}, 
 * @see {@link #setNeedsParameters(Double[])}, 
 * @see {@link #GetNeedsParameter(int)} and @see {@link #setNeedParameter(int, double)}. This array is used in the class 
 * @see HLTread in the method @see ActivitySimulationThread#updateNeeds to update the status of the needs in the simulation accorting
 * to the constant growth of the need during the time.
 * <p>
 * It contains an ArrayList of @see NeedsDrift, approachable only with methods @see {@link #getDrifts()} and 
 * @see {@link #setDrifts(ArrayList)},	 * it is instanced by @see {@link NeedDrift#loadNeedDrift } in @see Main.
 * This ArrayList is used in method @see {@link #update(long)} to apply the needs drift.
 * <p>
 * It also contains the unused methods @see {@link #MinToSec(long)}, @see {@link #MinToSec(long)}, 
 * @see {@link #DayToSec(int)}.
 * @author alessandro
 *
 */
public class Parameters {
	
	private static Parameters instance;

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
	Double[] NeedsParameters;
	
	private ArrayList<NeedsDrift> Drifts;

	/**
	 * This constructor just instance an array made of one arbitrary value to allow the method @see {@link #getInstance()}
	 * to build the class and, afterwards, to call the method @see {@link #setNeedsParameters(Double[])} in @see Needs 
	 * to set the real value.
	 */
	private Parameters(){
		/*
		HUNGER = 0.0033;
		STRESS = 0.003;
		SWEAT = 0.0006;	
		TOILETING = 0.009;//0.0056
		TIREDINESS = 0.0010; //0.0025
		BOREDOM = 0.006;
		ASOCIALITY = 0.001;
		DIRTINESS = 0.0001;
		OUTOFSTOCK = 0.0001;
		*/
		super();
		this.NeedsParameters = new Double[1];
		NeedsParameters[0] = 0.0010;
		/*
		try {
			Drifts = NeedsDrift.loadNeedDrift();
		} catch (NotDirectoryException e) {
			e.printStackTrace();
		}
		*/
	}
	
	/**
	 * Checks if there are drifts to apply at the needs growth rate
	 * @param time	The current time of the simulation
	 */
	public void update(long time){

		if (Main.USE_DRIFTS) {
			Iterator<NeedsDrift> ItrDrifts = Drifts.iterator();
			//	boolean[] done = new boolean[Drifts.size()];
			//	int i = 0;
			while (ItrDrifts.hasNext()) {
				NeedsDrift CurrentDrift = ItrDrifts.next();
				if (time == CurrentDrift.getTime()) {
					setNeedParameter(Needs.getInstance().searchIndex(CurrentDrift.getId()), CurrentDrift.getNewValue());
					//	done[i] = true;
				}
				//i++;
			}
	/*	for(i = 0; i < done.length; i++){
			if(done[i]){
				Drifts.remove(i);
			}
		}*/
		}
	}

	
	/**
	 * Change the value of @see {@link #instance}
	 * @param instance	The new value of instance
	 */
	public static void setInstance(Parameters instance) {
		Parameters.instance = instance;
	}
	
	/**
	 * Return the value of @see {@link #instance}, but if its value equals the null, it would first call the constructor @see {@link #Parameters()}.
	 * @return	The value of the Parameters' instance
	 */
	public static synchronized Parameters getInstance(){
		if(instance==null) {
			instance = new Parameters();
		}
		return instance;
	}
	
	/**
	 * Converts minutes to seconds
	 * @param min	The number of minutes to convert
	 * @return		The value of the input converted in seconds
	 */
	private static long MinToSec(long min){
		return min*60;
	}
	
	/**
	 * Converts hours to seconds
	 * @param min	The number of hours to convert
	 * @return		The value of the input converted in seconds
	 */
	private static long HourToSec(int hour){
		return MinToSec(hour*60);
	}
	
	/**
	 * Converts days to seconds
	 * @param min	The number of days to convert
	 * @return		The value of the input converted in seconds
	 */
	@SuppressWarnings("unused")
	private static long DayToSec(int day){
		return HourToSec(day*24);
	}
	
	/**
	 * Get the parameter placed at the specified index in @see #NeedsParameters
	 * @param index	The index of the desired value
	 * @return		The value contained at the index in @see #NeedsParameters 
	 */
	public double GetNeedsParameter(int index){
		try{
			return NeedsParameters[index];
		} catch(Exception e){
			e.printStackTrace();
		}
		return (Double) null;
	}
	
	/**
	 * Returns @see #NeedsParameters 
	 * @return
	 */
	public Double[] getNeedsParameters() {
		return NeedsParameters;
	}
	
	/**
	 * Sets a value placed at the index in @see #NeedsParameters with a new value.
	 * @param index		The index of the value
	 * @param newParam	The new value
	 */
	public void setNeedParameter(int index, double newParam) {
		NeedsParameters[index] = newParam;
	}
	
	/**
	 * Sets a new array as @see #NeedsParameters 
	 * @param needsParameters	New array
	 */
	public void setNeedsParameters(Double[] needsParameters) {
		NeedsParameters = needsParameters;
	}
	
	/**
	 * Returns the @see #Drifts
	 * @return The ArrayList of drifts
	 */
	public ArrayList<NeedsDrift> getDrifts() {
		return Drifts;
	}
	
	/**
	 * Sets a new array as @see #Drifts 
	 * @param drifts	New ArrayList
	 */
	public void setDrifts(ArrayList<NeedsDrift> drifts) {
		Drifts = drifts;
	}
}
