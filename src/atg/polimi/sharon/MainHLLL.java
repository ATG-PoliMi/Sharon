package atg.polimi.sharon;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import atg.polimi.sharon.engine.thread.ADLQueue;
import atg.polimi.sharon.engine.thread.HLThread;
import atg.polimi.sharon.engine.thread.LLThread;

public class MainHLLL {

	//*****SIMULATION PARAMETERS:*****
	private static int def_simulatedDays 	= 10; 	//Days to simulate
	//***** END PARAMETERS *****

	// Extra parameters - not to touch
	private static int mode 			= 0;	//0: only High Level, 1: High Level + Low Level (Experimental!)
	private static int printLog 		= 0;	//0: no log print, 1: print (histograms...)
	private static int dijkstra 		= 0;	//0: no dijkstra, 1: dijkstra (slower)
	private static String sOutput 		= "data/SensorOutput/DAY";	//this file is heavy. Open it from explorer.
	//

	//Thread
	private static HLThread producer;
	private static LLThread consumer;
	private static BlockingQueue<ADLQueue> queue = new ArrayBlockingQueue<>(100); //ADL QUEUE

	public static void main(String[] args) {

		int simulatedDays;
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
		producer = new HLThread(queue, simulatedDays, printLog, mode); 	
		new Thread(producer).start();
		System.out.println("Simulator correctly instantiated... Beginning the simulation");

		//LOW LEVEL SIMULATION
		if (mode == 1) {
			consumer = new LLThread(queue, simulatedDays, dijkstra, sOutput);
			new Thread(consumer).start();
			System.out.println("Consumer Starts");	
		}			
	}
}