package sharon;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import sharon.extractor.thread.ADLQueue;
import sharon.extractor.thread.HLThread;
import sharon.extractor.thread.LLThread;

public class MainHLLL {

	//*****SIMULATION PARAMETERS:*****
	private static int simulatedDays 	= 10; 	//Days to simulate
	private static int mode 			= 1;	//0: only High Level, 1: High Level + Low Level
	private static int printLog 		= 0;	//0: no log print, 1: print (histograms...)
	//***** END PARAMETERS *****
	
	
	
	//Thread
	private static HLThread producer;
	private static LLThread consumer;
	ADLQueue ADLQ;
	private static BlockingQueue<ADLQueue> queue = new ArrayBlockingQueue<>(10); //ADL QUEUE


	public static void main(String[] args) { 

		//HIGH LEVEL SIMULATION
		producer = new HLThread(queue, simulatedDays, printLog); 	
		new Thread(producer).start();
		System.out.println("Producer Starts");

		//LOW LEVEL SIMULATION
		if (mode == 1) {
			consumer = new LLThread(queue, simulatedDays);
			new Thread(consumer).start();
			System.out.println("Consumer Starts");	
		}			
	}
}