package sharon;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import sharon.extractor.thread.ADLQueue;
import sharon.extractor.thread.HLThread;
import sharon.extractor.thread.LLThread;

public class MainHLLL {

	//Thread
	private static HLThread producer;
	private static LLThread consumer;
	ADLQueue ADLQ;
	private static BlockingQueue<ADLQueue> queue = new ArrayBlockingQueue<>(100); //ADL QUEUE

	
	public static void main(String[] args) { 

		producer = new HLThread(queue); //High 	Level 	simulation
		consumer = new LLThread(queue);	//Low 	Level 	simulation

		new Thread(producer).start();
		new Thread(consumer).start();
		
		System.out.println("Producer and Consumer has been started");
	}
}