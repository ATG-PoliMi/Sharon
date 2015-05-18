package sharon.engine.thread;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.io.IOException;












import sharon.data.Coordinate;
import sharon.data.Sensor;
import sharon.engine.ADL;
import sharon.engine.ADLMatcher;
import sharon.engine.LowLevelADL;
import sharon.xml.ADLMatcherDB;
import sharon.xml.HomeMap;
import sharon.xml.LLADLDB;
import utils.Constants;
import utils.CumulateHistogram;
import utils.dijsktra.DijkstraEngine;

public class LLThread implements Runnable{


	private Coordinate actor = new Coordinate (10,10);
	private Coordinate Target = new Coordinate(15, 25);
	private DijkstraEngine DE;
	int[][] worldMapMatrix;
	private ArrayList<String> path = new ArrayList<String>();
	private String delims = ",";

	//ADL Handling
	static Map<Integer, ADL> hLADL;
	static Map<Integer, LowLevelADL>  lLADL;
	static Map<Integer, ADLMatcher> matchADL;

	//User actions
	static int agentStatus	=	1; //1: extracting; 2: acting;
	static int idling 		= 	0;	

	//Utils
	static long tick = 0;
	static long usedTime = 0;

	//TARGETS
	private Integer llADLIndex; 
	private ArrayList<Integer> tTime = new ArrayList<Integer>();
	private static int stationCounter = 0; //Station Counter

	//Support ADL
	static CumulateHistogram hist = new CumulateHistogram();

	private BlockingQueue<ADLQueue> queue;
	private int simulatedDays;
	private int action;
	private int dijkstra;
	private String sensorsOutput;

	public LLThread(BlockingQueue<ADLQueue> q, int simulatedDays, int dijkstra, String sOutput){
		this.queue=q;
		this.simulatedDays = simulatedDays;
		this.dijkstra = dijkstra;
		this.sensorsOutput = sOutput;

		lLADL 		= 	LLADLDB.addLLADL();
		matchADL 	= 	ADLMatcherDB.addADLMatch();
	}

	@Override
	public void run() {

		int emptyN=0;
		PrintWriter out;
		try {
			Thread.sleep(1000);
			out = new PrintWriter(new FileWriter(sensorsOutput+"0.txt"));
			
			for (tick=0; tick < (86400*simulatedDays)-5000; tick++) {

				idling++;
				switch (agentStatus) {
				case 1: //Extracting + computing
					ADLQueue CADL;
					try {
						if (queue.isEmpty()) {
							//CADL = new ADLQueue((int)((Math.random() * 10) + 1), 500);
							//Fake ADLS for demo: start demo:		//queue.put(new ADLQueue(8, 300));queue.put(new ADLQueue(6, 300));queue.put(new ADLQueue(3, 666));queue.put(new ADLQueue(2, 300));queue.put(new ADLQueue(2, 300));queue.put(new ADLQueue(2, 300));				//end demo
							System.out.println("***** A: EMPTY queue *****");
							tick--;
							emptyN++;

						} else {
							CADL = queue.take();
							//System.out.println("A: NOT EMPTY taken: "+ CADL.getADLId()+" lasting "+CADL.getTime()); //TODO: Log row
							action=CADL.getADLId();
							if (CADL != null) {
								llADLIndex = matchADL.get(CADL.getADLId()).getLLadl().get(0); 
								tTime.clear();				

								for (int i=0; i<lLADL.get(llADLIndex).getStations().size(); i++) {
									tTime.add((int) (CADL.getTime() * lLADL.get(llADLIndex).getStations().get(i).getTimePercentage())); 
								}
								agentStatus=2;							
							}	
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("QUEUE ERROR!");
					}
					break;

				case 2:	//Walking+Acting
					if (!tTime.isEmpty()) {	//tTime contains timings for each station
						if (idling < tTime.get(0)) {
							if (dijkstra == 0 ) {

								Sensor [] s= HomeMap.getInstance().getS();
								Target = new Coordinate(s[lLADL.get(llADLIndex).getStations().get(stationCounter).getId()].getX(),
										s[lLADL.get(llADLIndex).getStations().get(stationCounter).getId()].getY());
								if (Target.getX()>actor.getX()) 
									actor.setX(actor.getX()+1);
								if (Target.getX()<actor.getX()) 
									actor.setX(actor.getX()-1);

								if (Target.getY()>actor.getY()) 
									actor.setY(actor.getY()-1);
								if (Target.getY()>actor.getY()) 
									actor.setY(actor.getY()+1);							

							} else {
								if (path.isEmpty()) {	//New station case
									newTarget(lLADL.get(llADLIndex).getStations().get(stationCounter).getId());
								}

								if (path.size() > 0) {	//With a target the target moves toward that direction
									String x = path.get(0);
									//System.out.println(x);	//TODO: row log 

									path.remove(0);
									String[] tokens = x.split(delims);
									Coordinate target = new Coordinate(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1]));
									if (!target.equals(actor)) {
										actor.setX(target.getX());
										actor.setY(target.getY());
									}
								}
							}

						} else {	//Time at the station ended
							idling=0;
							stationCounter++;
							tTime.remove(0);					
						}					
					} else {	//	ADL completed
						agentStatus = 1;
						stationCounter=0;
						action = 0; //(Walking)
					}
					break;
				}
				
				if ((tick%86400==0)&&(tick>0)) {
					out.close();
					out = new PrintWriter(new FileWriter(sensorsOutput+(int)tick/86400+".txt"));
				}
				out.println(printActiveSensors(action));	//TODO: Log row
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Empty ticks: "+emptyN);
		System.out.println("Consumer Thread ends");

	}


	public void newTarget(int indexSensor) {
		DE = new DijkstraEngine();
		worldMapMatrix = HomeMap.getInstance().getWorldMap();
		DE.buildAdjacencyMatrix(worldMapMatrix);

		Sensor [] s= HomeMap.getInstance().getS();

		Target = new Coordinate(s[indexSensor].getX(), s[indexSensor].getY());

		// Start point:
		DE.setInitial(actor.getX() + ","+ actor.getY());

		// End point:
		path = DE.computePath(Target.getX()+","+Target.getY());
		//System.out.println("PATH:"+path);

	}

	/**
	 * printActiveSensors computes the values for each sensor of the house and returns a String in the following format:
	 * "tick, home area, ADL id, UserX, UserY, sensors 0-k"
	 * @return
	 */
	public String printActiveSensors (int action) {

		String activeSensors = "";

		Sensor[] sensorsArray = HomeMap.getInstance().getS();

		activeSensors += tick;
		activeSensors += ", ";
		activeSensors += HomeMap.getInstance().getHouseArea(actor.getX(), actor.getY());
		activeSensors += ", ";
		activeSensors += action;
		activeSensors += ", ";
		activeSensors += (int)actor.getX();
		activeSensors += ", ";
		activeSensors += (int)actor.getY();
		activeSensors += ", ";
		for (int i=0; i < Constants.SENSORSNUMBER; i++) {
			if (((sensorsArray[i].getX() == actor.getX())&&
					(sensorsArray[i].getY() == actor.getY()))) {
				activeSensors += "1, ";

			} else {
				activeSensors += "0, ";				
			}
		}
		activeSensors = activeSensors.substring(0, activeSensors.length()-2);

		return activeSensors;
	}
}