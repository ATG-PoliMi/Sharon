package sharon.extractor.thread;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import sharon.data.Coordinate;
import sharon.data.HomeMap;
import sharon.data.Sensor;
import sharon.extractor.ADL;
import sharon.extractor.Needs;
import sharon.planner.ADLMatcher;
import sharon.planner.LowLevelADL;
import sharon.xml.ADLMatcherDB;
import sharon.xml.LLADLDB;
import utils.Constants;
import utils.CumulateHistogram;
import utils.RandomGaussian;
import utils.Time;
import utils.dijsktra.DijkstraEngine;

public class LLThread implements Runnable{

	private ContinuousSpace<Object> space;
	private Coordinate actor = new Coordinate (10,10);
	private GridPoint Target = new GridPoint(15, 25);
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
	RandomGaussian gaussian = new RandomGaussian();
	static long tick = 0;
	static int keyBadl;
	static long usedTime = 0;

	//TARGETS
	private Integer llADLIndex; 
	private ArrayList<Integer> tTime = new ArrayList<Integer>();
	private static int stationCounter = 0; //Station Counter

	//Support ADL
	static ADL badl;
	static CumulateHistogram hist = new CumulateHistogram();

	private BlockingQueue<ADLQueue> queue;
	private int simulatedDays;

	public LLThread(BlockingQueue<ADLQueue> q, int simulatedDays){
		this.queue=q;
		this.simulatedDays = simulatedDays;

		lLADL 		= 	LLADLDB.addLLADL();
		matchADL 	= 	ADLMatcherDB.addADLMatch();
	}

	@Override
	public void run() {
		int emptyN=0;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			System.out.println("Sleep Error");
			e1.printStackTrace();

		}

		for (tick=0; tick < 86400*simulatedDays; tick++) {

			//if (tick % 60 == 0) {System.out.println("LL MIN: "+(int)tick/60);}
			idling++;

			switch (agentStatus) {
			case 1: //Extracting + computing
				ADLQueue CADL;
				try {
					if (queue.isEmpty()) {
						//CADL = new ADLQueue((int)((Math.random() * 10) + 1), 500);
						//Fake ADLS for demo: start demo:		//queue.put(new ADLQueue(8, 300));queue.put(new ADLQueue(6, 300));queue.put(new ADLQueue(3, 666));queue.put(new ADLQueue(2, 300));queue.put(new ADLQueue(2, 300));queue.put(new ADLQueue(2, 300));				//end demo
						//System.out.println("***** A: EMPTY queue *****");
						tick--;
						emptyN++;

					} else {
						CADL = queue.take();
						System.out.println("A: NOT EMPTY taken: "+ CADL.getADLId()+" lasting "+CADL.getTime());

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
					System.out.println("ERROR!");
				}
				break;

			case 2:	//Walking+Acting
				if (!tTime.isEmpty()) {
					if (idling < tTime.get(0)) {

						if (path.isEmpty()) {
							newTarget(lLADL.get(llADLIndex).getStations().get(stationCounter).getId());
						}
						if (path.size() > 0) {
							String x = path.get(0);
							path.remove(0);
							String[] tokens = x.split(delims);

							Coordinate target = new Coordinate(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1]));
							moveTowards(target);
						}
					} else {
						idling=0;
						stationCounter++;
						tTime.remove(0);					
					}

				} else {
					agentStatus = 1;
					//completeADL(keyBadl);
					Logs(2);
					//updateNeeds((int) usedTime/3600);
					stationCounter=0;
				}
				break;
			}
		}
		System.out.println("Empty Seconds: "+emptyN);
		System.out.println("Consumer Thread ends");
	}

	public void moveTowards(Coordinate trgt) {

			if (!trgt.equals(actor)) {
				actor.setX(trgt.getX());
				actor.setY(trgt.getY());
			}
	}


	public void newTarget(int indexSensor) {
		DE = new DijkstraEngine();
		worldMapMatrix = HomeMap.getInstance().getWorldMap();
		DE.buildAdjacencyMatrix(worldMapMatrix);

		Sensor [] s= HomeMap.getInstance().getS();

		Target = new GridPoint(s[indexSensor].getX(), s[indexSensor].getY());

		// Start point:
		DE.setInitial(actor.getX() + ","+ actor.getY());

		// End point:
		path = DE.computePath(Target.getX()+","+Target.getY());
		//System.out.println("PATH:"+path);

	}

	private static void Logs(int logType) {
		Time t = new Time(tick % 86400);
		switch (logType) {
		case 1: 
			System.out.printf ("newNeeds: Hu:%.2f", Needs.getInstance().getHunger());
			System.out.printf (", St:%.2f", 	Needs.getInstance().getStress());
			System.out.printf (", Sw:%.2f", 	Needs.getInstance().getSweat());
			System.out.printf (", To:%.2f", 	Needs.getInstance().getToileting());
			System.out.printf (", Ti:%.2f", 	Needs.getInstance().getTirediness());
			System.out.printf (", Bo:%.2f", 	Needs.getInstance().getBoredom());
			System.out.printf (", As:%.2f",	 	Needs.getInstance().getAsociality());
			System.out.printf (", OS:%.2f", 	Needs.getInstance().getOutOfStock());
			System.out.printf (", Di:%.2f", 	Needs.getInstance().getDirtiness());		
			System.out.println();

			double cBest=0;
			for (ADL a : hLADL.values())  {

				//				if (a.getRank()>0)
				//					System.out.printf ("%s : %.3f ",a.getName(),a.getRank());
				if (a.getRank()>cBest){
					System.out.printf ("%s : %.3f ",a.getName(),a.getRank());
					cBest = a.getRank();
				}
			}
			System.out.println();
			System.out.print ("ADL SELECTED: "+ badl.getName() +" with rank ");
			System.out.printf ("%.3f", badl.getRank());
			System.out.println(" day hour: " +t.getHour() +":"+t.getMinute());

			System.out.println();				
			break;

		case 2:
			System.out.print (t.getHour() +":"+t.getMinute()+" ");
			System.out.printf ("newNeeds: Hu:%.2f", Needs.getInstance().getHunger());
			System.out.printf (", St:%.2f", 	Needs.getInstance().getStress());
			System.out.printf (", Sw:%.2f", 	Needs.getInstance().getSweat());
			System.out.printf (", To:%.2f", 	Needs.getInstance().getToileting());
			System.out.printf (", Ti:%.2f", 	Needs.getInstance().getTirediness());
			System.out.printf (", Bo:%.2f", 	Needs.getInstance().getBoredom());
			System.out.printf (", As:%.2f", 	Needs.getInstance().getAsociality());
			System.out.printf (", OS:%.2f", 	Needs.getInstance().getOutOfStock());
			System.out.printf (", Di:%.2f", 	Needs.getInstance().getDirtiness());	
			System.out.println();
			break;

		case 3:
			hist.updateHistogramH(tick, badl.getId());
			break;
		case 4:
			hist.updateHistogramM(tick, badl.getId());
			break;
		}
	}
	/**
	 * printActiveSensors computes the values for each sensor of the house and returns a String in the following format:
	 * "tick, home area, ADL id, UserX, UserY, sensors 0-k"
	 * @return
	 */
	public String printActiveSensors () {

		String activeSensors = "";

		Sensor[] sensorsArray = HomeMap.getInstance().getS();
		NdPoint printPoint = space.getLocation(this);
		activeSensors += tick;
		activeSensors += ", ";
		activeSensors += HomeMap.getInstance().getHouseArea(printPoint.getX(), printPoint.getY());
		activeSensors += ", ";
		activeSensors += badl.getId();
		activeSensors += ", ";
		activeSensors += (int)printPoint.getX();
		activeSensors += ", ";
		activeSensors += (int)printPoint.getY();
		activeSensors += ", ";
		for (int i=0; i < Constants.SENSORSNUMBER; i++) {
			if (((sensorsArray[i].getX() == printPoint.getX())&&
					(sensorsArray[i].getY() == printPoint.getY()))) {
				activeSensors += "1, ";

			} else {
				activeSensors += "0, ";				
			}
		}
		activeSensors = activeSensors.substring(0, activeSensors.length()-2);

		return activeSensors;
	}
}
