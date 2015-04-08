package repast.simphony.agent;

import utils.CumulateHistogram;
import utils.Time;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import utils.Constants;

import org.apache.commons.math3.distribution.BetaDistribution;

import javolution.context.Context;
import behavior.simulator.extractor.ADL;
import behavior.simulator.extractor.ADLEffect;
import behavior.simulator.extractor.ADLQueue;
import behavior.simulator.extractor.Day;
import behavior.simulator.extractor.HighLevelADLProducer;
import behavior.simulator.extractor.Needs;
import behavior.simulator.extractor.RandomGaussian;
import behavior.simulator.planner.ADLMatcher;
import behavior.simulator.planner.LowLevelADL;
import behavior.simulator.xml.ADLDB;
import behavior.simulator.xml.ADLMatcherDB;
import behavior.simulator.xml.LLADLDB;
import dijsktra.DijkstraEngine;
import repast.simphony.common.HomeMap;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;

public class Actor implements Runnable{

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
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
	static int agentStatus	=	1; //0:Idling 1:Extracting a new ADL 2:Walking 3:Acting
	static int idling 		= 	0;	

	//Utils
	RandomGaussian gaussian = new RandomGaussian();
	static long tick;
	static int keyBadl;
	static long usedTime = 0;

	//TARGETS
	private Integer llADLIndex; 
	private ArrayList<Integer> tTime = new ArrayList<Integer>();
	private static int stationCounter = 0; //Station Counter

	//Support ADL
	static ADL badl;
	static CumulateHistogram hist = new CumulateHistogram();

	//ID
	static int init=0;

	//Thread
	private BlockingQueue<ADLQueue> queue1;	//run
	private BlockingQueue<ADLQueue> queue2;	//step
	private HighLevelADLProducer producer;
	private Actor consumer;
	ADLQueue ADLQ;

	public Actor (ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space=space;
		this.grid=grid;
		importADL();
		badl = hLADL.get(Constants.SLEEP_ID); //Initial ADL: Sleeping //TODO: replicata con r90
	}

	public Actor(BlockingQueue<ADLQueue> q) {
		this.queue1=q;
	}

	private void importADL () {
		hLADL		= 	ADLDB.addADL();
		lLADL 		= 	LLADLDB.addLLADL();
		matchADL 	= 	ADLMatcherDB.addADLMatch();	
		badl 		= 	hLADL.get(Constants.SLEEP_ID); //Initial ADL: Sleeping //TODO: replicata con r83
	}


	public void threadStart () {
		queue1 = new ArrayBlockingQueue<>(10);
		producer = new HighLevelADLProducer(queue1);
		consumer = new Actor(queue1);

		//starting producer to produce messages in queue
		new Thread(producer).start();
		//starting consumer to consume messages from queue
		new Thread(consumer).start();
		System.out.println("Producer and Consumer has been started");
	}

	@ScheduledMethod(start = 0, interval = 1, priority=0)
	public void step() {
		tick = (long) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		idling++;
		switch (agentStatus) {
		case 1: //Extracting + computing
			ADLQueue CADL;
			try {
				if (tick == 0.0) {
					CADL = new ADLQueue(Constants.SLEEP_ID, 500);
				} else {
					CADL = queue2.take();
				}
				if (CADL != null) {
					llADLIndex = matchADL.get(CADL.getADLId()).getLLadl().get(0); //TODO: 	Implement probabilities in LLADL extraction!!!		
					tTime.clear();				
	
					for (int i=0; i<lLADL.get(llADLIndex).getStations().size(); i++) {
						tTime.add((int) (CADL.getTime() * lLADL.get(llADLIndex).getStations().get(i).getTimePercentage())); 
					}
					agentStatus=2;
				}				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		break;

		case 2:	//Walking+Acting
			if (!tTime.isEmpty()) {
				if (idling < tTime.get(0)) {

					GridPoint pt = grid.getLocation(this);
					if (path.isEmpty()) {
						newTarget(lLADL.get(llADLIndex).getStations().get(stationCounter).getId());
					}
					if (path.size() > 0) {
						String x = path.get(0);
						path.remove(0);
						String[] tokens = x.split(delims);

						GridPoint pt2 = new GridPoint(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1]));
						moveTowards(pt2);
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


	public void moveTowards(GridPoint pt) {
		// only move if we are not already in this grid location
		if (!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint,
					otherPoint);
			space.moveByVector(this, 1, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
		}
	}


	public void newTarget(int indexSensor) {
		DE = new DijkstraEngine();
		worldMapMatrix = HomeMap.getInstance().getWorldMap();
		DE.buildAdjacencyMatrix(worldMapMatrix);

		Sensor [] s= HomeMap.getInstance().getS();

		Target = new GridPoint(s[indexSensor].getX(), s[indexSensor].getY());

		//System.out.println("INVIO!!"+(int)grid.getLocation(this).getX() + ","+ (int)grid.getLocation(this).getY());
		// Start point:
		DE.setInitial((int)grid.getLocation(this).getX() + ","+ (int)grid.getLocation(this).getY());

		// End point:
		path = DE.computePath(Target.getX()+","+Target.getY());
		//System.out.println("PATH:"+path);
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


	/*
	 * 
	 * NEW CODE:
	 * 
	 */


	private static void checkBetterADL() {
		ADL cadl = hLADL.get(1);		

		if (usedTime > 60*badl.getMinTime()){

			//Check Better ADL
			for (ADL a : hLADL.values()) {			
				if ((a.getRank() >= cadl.getRank())) {
					cadl 			= a;
				}			
			}		

			if(cadl.getRank() > 0.01 && cadl.getName() != badl.getName()) {
				badl.setActive(0);
				badl = cadl;
				badl.setActive(1);
				usedTime=0;
				Logs(1);		
			}
		}
	}

	private static void newDay() {

		Day.getInstance().nextDay();

		//eraseNeeds();
		System.out.println("***NEW DAY***! ("+Day.getInstance().getWeekDay()+")");
		System.out.print("Today is ");
		switch (Day.getInstance().getWeekDay()%7) {
		case 0: System.out.print("Monday"); break;
		case 1: System.out.print("Tuesday"); break;
		case 2: System.out.print("Wednsday"); break;
		case 3: System.out.print("Thursday"); break;
		case 4: System.out.print("Friday"); break;
		case 5: System.out.print("Saturday"); break;
		case 6: System.out.print("Sunday"); break;
		default: System.out.print("Error");
		}
		System.out.print(" and the weather is ");
		switch (Day.getInstance().getWeather()) {
		case 1: System.out.print("Rainy\n"); break;
		case 2: System.out.print("Cloudy\n"); break;
		case 3: System.out.print("Sunny\n"); break;
		default: System.out.print("Error\n");
		}

	}

	private static void eraseNeeds() {
		try {
			System.in.read();
		} catch (IOException e) {

			e.printStackTrace();
		}
		Needs.getInstance().setToileting(0);
		Needs.getInstance().setStress(0);
		Needs.getInstance().setDirtiness(0);
		Needs.getInstance().setTirediness(0);
		Needs.getInstance().setBoredom(0);
		Needs.getInstance().setHunger(0);
		Needs.getInstance().setSweat(0);
		Needs.getInstance().setAsociality(0);
		Needs.getInstance().setOutOfStock(0);		
	}

	/**
	 * Computation of the ADL rank
	 * @param minute
	 */
	private static void computeADLRank(int minute) {
		double r, active;

		double needs[] = Needs.getInstance().loadNeeds();
		for (ADL a : hLADL.values()) {
			r = 0;			
			active = (a.getActive() > 0) ? 1 : 0.7;
			for (int i=0; i<needs.length; i++) {
				r += needsEffort(a, i) * needs[i];					
			}

			r *= (Math.random() < a.getExactTimeDescription(minute/60)) ? 1 : a.getExactTimeDescription(minute/60) * a.getExactDay(Day.getInstance().getWeekDay()%7) * 
					active * (0.80 + Math.random()*(1-0.80));
			a.setRank(r);
		}
	}

	/**
	 * 
	 */
	private static double needsEffort(ADL a, int i) {
		double ADLeffort = 0.0;
		int needed = 0;
		if (a.getNeeds() != null) {
			switch (i) {
			case 0: 
				needed += a.getNeeds().contains("hunger") 		? 1 : 0;
				break;
			case 1: 
				needed += a.getNeeds().contains("stress") 		? 1 : 0;
				break;
			case 2: 
				needed += a.getNeeds().contains("sweat") 		? 1 : 0;
				break;
			case 3: 
				needed += a.getNeeds().contains("toileting") 	? 1 : 0;
				break;
			case 4: 
				needed += a.getNeeds().contains("tirediness")	? 1 : 0;
				break;
			case 5: 
				needed += a.getNeeds().contains("boredom") 		? 1 : 0;
				break;
			case 6: 
				needed += a.getNeeds().contains("asociality")	? 1 : 0;
				break;
			case 7: 
				needed += a.getNeeds().contains("outofstock") 	? 1 : 0;
				break;
			case 8: 
				needed += a.getNeeds().contains("dirtiness")	? 1 : 0;
				break;
			}
		}

		ADLeffort = ((a.getNeeds().size()>0) && (needed>0)) ? ((double)1/a.getNeeds().size()) : 0.0;
		return ADLeffort;
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
	 * Updates user's and house needs
	 */
	private static void updateNeeds(int times) {

		for (int i=0; i<times; i++) {
			if (Needs.getInstance().getHunger() 	< 1.0) 
				Needs.getInstance().setHunger(Needs.getInstance().getHunger() 			+ Constants.HUNGER);
			if (Needs.getInstance().getStress() 	< 1.0) 
				Needs.getInstance().setStress(Needs.getInstance().getStress() 			+ Constants.STRESS);
			if (Needs.getInstance().getSweat() 		< 1.0) 
				Needs.getInstance().setSweat(Needs.getInstance().getSweat()				+ Constants.SWEAT);
			if (Needs.getInstance().getToileting() 	< 1.0) 
				Needs.getInstance().setToileting(Needs.getInstance().getToileting()		+ Constants.TOILETING);
			if (Needs.getInstance().getTirediness() < 1.0) 
				Needs.getInstance().setTirediness(Needs.getInstance().getTirediness()	+ Constants.TIREDINESS);
			if (Needs.getInstance().getBoredom() 	< 1.0) 
				Needs.getInstance().setBoredom(Needs.getInstance().getBoredom()			+ Constants.BOREDOM);
			if (Needs.getInstance().getAsociality() < 1.0) 
				Needs.getInstance().setAsociality(Needs.getInstance().getAsociality()	+ Constants.ASOCIALITY);
			if (Needs.getInstance().getDirtiness() 	< 1.0) 
				Needs.getInstance().setDirtiness(Needs.getInstance().getDirtiness()		+ Constants.DIRTINESS);
			if (Needs.getInstance().getOutOfStock() < 1.0) 
				Needs.getInstance().setOutOfStock(Needs.getInstance().getOutOfStock()	+ Constants.OUTOFSTOCK);
			updateADLNeeds(badl);
		}		
	}

	/**
	 * Actions to perform when the ADL has been completed
	 * @param ADLindex: Index of the ADL just executed
	 */
	private static void updateADLNeeds(ADL finishingADL) {
		Iterator<ADLEffect> x = hLADL.get(finishingADL.getId()).getEffects().iterator();
		while (x.hasNext()) {
			ADLEffect effects;
			effects = x.next();
			if (effects.getName().equals("hunger")) {
				Needs.getInstance().setHunger(Needs.getInstance().getHunger() + effects.getEffect());
				if (Needs.getInstance().getHunger() < 0)
					Needs.getInstance().setHunger(0);
				if (Needs.getInstance().getHunger() > 1)
					Needs.getInstance().setHunger(1);				
			}
			if (effects.getName().equals("stress")) {
				Needs.getInstance().setStress(Needs.getInstance().getStress()+effects.getEffect());
				if (Needs.getInstance().getStress() < 0)
					Needs.getInstance().setStress(0);
				if (Needs.getInstance().getStress() > 1)
					Needs.getInstance().setStress(1);	
			}
			if (effects.getName().equals("sweat")) {
				Needs.getInstance().setSweat(Needs.getInstance().getSweat()+effects.getEffect());
				if (Needs.getInstance().getSweat() < 0)
					Needs.getInstance().setSweat(0);
				if (Needs.getInstance().getSweat() > 1)
					Needs.getInstance().setSweat(1);
			}
			if (effects.getName().equals("toileting")) {
				Needs.getInstance().setToileting(Needs.getInstance().getToileting()+effects.getEffect());
				if (Needs.getInstance().getToileting() < 0)
					Needs.getInstance().setToileting(0);
				if (Needs.getInstance().getToileting() > 1)
					Needs.getInstance().setToileting(1);				
			}
			if (effects.getName().equals("tirediness")) {
				Needs.getInstance().setTirediness(Needs.getInstance().getTirediness()+effects.getEffect());
				if (Needs.getInstance().getTirediness() < 0)
					Needs.getInstance().setTirediness(0);
				if (Needs.getInstance().getTirediness() > 1)
					Needs.getInstance().setTirediness(1);	
			}
			if (effects.getName().equals("boredom")) {
				Needs.getInstance().setBoredom(Needs.getInstance().getBoredom()+effects.getEffect());
				if (Needs.getInstance().getBoredom() < 0)
					Needs.getInstance().setBoredom(0);
				if (Needs.getInstance().getBoredom() > 1)
					Needs.getInstance().setBoredom(1);				
			}
			if (effects.getName().equals("asociality")) {
				Needs.getInstance().setAsociality(Needs.getInstance().getAsociality()+effects.getEffect());
				if (Needs.getInstance().getAsociality() < 0)
					Needs.getInstance().setAsociality(0);
				if (Needs.getInstance().getAsociality() > 1)
					Needs.getInstance().setAsociality(1);				
			}
			if (effects.getName().equals("outofstock")) {
				Needs.getInstance().setOutOfStock(Needs.getInstance().getOutOfStock()+effects.getEffect());
				if (Needs.getInstance().getOutOfStock() < 0)
					Needs.getInstance().setOutOfStock(0);
				if (Needs.getInstance().getOutOfStock() > 1)
					Needs.getInstance().setOutOfStock(1);				
			}
			if (effects.getName().equals("dirtiness")) {
				Needs.getInstance().setDirtiness(Needs.getInstance().getDirtiness()+effects.getEffect());
				if (Needs.getInstance().getDirtiness() < 0)
					Needs.getInstance().setDirtiness(0);
				if (Needs.getInstance().getDirtiness() > 1)
					Needs.getInstance().setDirtiness(1);				
			}
		}
	}

	@Override
	public void run() {
		try{
			ADLQueue temp1 = queue1.take();
			if (temp1 != null)
				queue2.put(temp1);
			Thread.sleep(10);

		}catch(InterruptedException e) {
			e.printStackTrace();
		}

	}
}