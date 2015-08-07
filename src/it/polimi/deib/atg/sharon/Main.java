package it.polimi.deib.atg.sharon;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import it.polimi.deib.atg.sharon.configs.ADLDB;
import it.polimi.deib.atg.sharon.configs.NeedsDrift;
import it.polimi.deib.atg.sharon.configs.Parameters;
import it.polimi.deib.atg.sharon.engine.Needs;
import it.polimi.deib.atg.sharon.engine.thread.ADLQueue;
import it.polimi.deib.atg.sharon.engine.thread.ActivitySimulationThread;
import it.polimi.deib.atg.sharon.engine.thread.SensorSimulationThread;

public class Main {

	//*****SIMULATION PARAMETERS:*****
	private static int def_simulatedDays 	= 3; 	//Days to simulate
	//***** END PARAMETERS *****
	
	Parameters param = Parameters.getInstance();

	// Extra parameters - not to touch
	public static final boolean ENABLE_SENSORS_ACTIVITY = false;	//0: only High Level, 1: High Level + Low Level (Experimental!)
	public static final boolean PRINT_LOG 				= false;	//0: no log print, 1: print (histograms...)
	private static int ENABLE_DIJKSTRA 			= 0;	//0: no ENABLE_DIJKSTRA, 1: ENABLE_DIJKSTRA (slower)
	public static final boolean USE_DRIFTS		= false;		// activates drifts

	private static String sensorOutputPrefix = "data/SensorOutput/DAY";	//this file is heavy. Open it from explorer.
	private static String activityOutputPrefix = "data/ActivityOutput/DAY";	//this file is heavy. Open it from explorer.
	//

	//Thread
	private static ActivitySimulationThread producer;
	private static SensorSimulationThread consumer;
	private static BlockingQueue<ADLQueue> queue = new ArrayBlockingQueue<>(100); //ADL QUEUE

	public static void main(String[] args) {

		int simulatedDays;
		try{
			Needs.getInstance();
			if (USE_DRIFTS)
				Parameters.getInstance().setDrifts(NeedsDrift.loadNeedDrift());
			ADLDB.getInstance();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		//HIGH LEVEL SIMULATION
		if (args.length > 0){
			try {
				simulatedDays = Integer.parseInt(args[0]);
			}catch (Exception e){
				System.out.println("Incoherent input, refer to README. Quitting.");
				return;
			}
		}else{
			simulatedDays = def_simulatedDays;
		}
		producer = new ActivitySimulationThread(queue, simulatedDays, activityOutputPrefix);
		new Thread(producer).start();
		System.out.println("Simulator correctly instantiated... Beginning the simulation");

		//LOW LEVEL SIMULATION
		if (ENABLE_SENSORS_ACTIVITY) {
			consumer = new SensorSimulationThread(queue, simulatedDays, ENABLE_DIJKSTRA, sensorOutputPrefix);
			new Thread(consumer).start();
			System.out.println("Consumer Starts");	
		}			
	}
}