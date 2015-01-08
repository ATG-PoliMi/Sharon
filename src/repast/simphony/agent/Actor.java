package repast.simphony.agent;

import utils.Time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import utils.Constants;

import org.apache.commons.math3.distribution.BetaDistribution;

import javolution.context.Context;
import behavior.simulator.extractor.ADL;
import behavior.simulator.extractor.ADLEffect;
import behavior.simulator.extractor.Day;
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
import utils.Constants;

public class Actor {


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
	static int agentStatus=1; //0:Idling 1:Extracting a new ADL 2:Walking 3:Acting
	static int idling;

	//Utils
	RandomGaussian gaussian = new RandomGaussian();
	static double tick;
	static int keyBadl;
	static int usedTime;

	//BADL
	static ADL badl;

	//TARGETS
	private Integer llADLIndex; 
	private ArrayList<Integer> tTime = new ArrayList<Integer>();
	private static int stationCounter = 0; //Station Counter


	public Actor (ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space=space;
		this.grid=grid;
		importADL();
		badl = hLADL.get(Constants.SLEEP_ID); //Initial ADL: Sleeping
	}

	private void importADL () {
		hLADL		= 	ADLDB.addADL();
		lLADL 		= 	LLADLDB.addLLADL();
		matchADL 	= 	ADLMatcherDB.addADLMatch();	
	}


	@ScheduledMethod(start = 0, interval = 1, priority=0)
	public void step() {
		tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		idling++;

		if (tick % 86400 == 0) {
			System.out.println("***NEW DAY***!");
			newDay();
			dayInitADL();
		}

		switch (agentStatus) {
		case 0: //Idle
			idling++;
			break;

		case 1: //Extracting a new ADL
			computeADLRank((int) tick % 86400);
			for (ADL a : hLADL.values()) {
				if ((a.getDoneToday()==0) && 
						(a.getRank() > badl.getRank()) &&
						(Day.getInstance().getWeather() >= a.getWeather()) &&
						(a.getDays().contains(Day.getInstance().getWeekDay())))	{						
					badl = a;
					keyBadl = a.getId();	
				}
			}
			usedTime = (int) gaussian.getGaussian (badl.getTmean(), badl.getTvariability());
			badl.setRank(badl.getRank() + 2);
			idling = 0;
			Logs(1);
			agentStatus = 2;
			break;

		case 2:	//Computing new targets			
			llADLIndex = matchADL.get(badl.getId()).getLLadl().get(0); //TODO: 	Implement probabilities in LLADL extraction!!!		
			tTime.clear();

			for (int i=0; i<lLADL.get(llADLIndex).getStations().size(); i++) {
				tTime.add((int) (usedTime * lLADL.get(llADLIndex).getStations().get(i).getTimePercentage())); 
			}
			agentStatus=3;
			break;


		case 3:	//Walking+Acting
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
				completeADL(keyBadl);
				Logs(2);
				updateNeeds((int) usedTime/3600);
				stationCounter=0;
			}
			break;
		}


		//if (idling <= 0) {	agentStatus=1;	}


		//Movement instructions
		/*
		GridPoint pt = grid.getLocation(this);

		if (path.size() > 0) {
			String x = path.get(0);
			path.remove(0);
			String[] tokens = x.split(delims);

			GridPoint pt2 = new GridPoint(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1]));
			moveTowards(pt2);
		}
		 */

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

	private static void newDay() {
		Day.getInstance().nextDay();

		System.out.print("Today is ");
		switch (Day.getInstance().getWeekDay()) {
		case 1: System.out.print("Monday"); break;
		case 2: System.out.print("Tuesday"); break;
		case 3: System.out.print("Wednsday"); break;
		case 4: System.out.print("Thursday"); break;
		case 5: System.out.print("Friday"); break;
		case 6: System.out.print("Saturday"); break;
		case 7: System.out.print("Sunday"); break;
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

	private static int wake() {
		Needs.getInstance().setEnergy(0);
		Needs.getInstance().setComfort(0);
		RandomGaussian gaussian = new RandomGaussian();
		return (int) gaussian.getGaussian (8*60, 10);
	}

	/**
	 * Operations applied to all the ADL each day
	 */
	private static void dayInitADL() {
		double n,d;
		for (ADL a : hLADL.values())  {
			a.setDoneToday(0); //In this way I avoid duplication of activities
			n = a.getCyclicalityN();
			d = a.getCyclicalityD();
			if (n < d)
				a.setCyclicalityN(n + 1.0);			
		}
	}

	/**
	 * Computation of the ADL rank
	 * @param minute
	 */
	private static void computeADLRank(int minute) {

		for (ADL a : hLADL.values()) {
			if (timeDependence(a, minute) > 0) {
				a.setRank((double) ((a.getCyclicalityN() / a.getCyclicalityD()) + 
						a.isMandatory() + 
						needsEffort(a) +
						timeDependence(a, minute)));
			}
			else
				a.setRank(0);
		}

	}

	private static double timeDependence(ADL a, int minute) {
		BetaDistribution b = new BetaDistribution(a.getActivationShapeA(), a.getActivationShapeB());
		double test = b.density((0.5 + (minute - a.getBestTime()) / (2 * a.getRangeTime())));

		return test;
	}


	private static double needsEffort(ADL a) {
		double ADLeffort = 0.0;
		int activations = 0;
		if (a.getNeeds() != null) {
			if (a.getNeeds().contains("hunger")) {
				ADLeffort += Needs.getInstance().getHunger();
				activations++;
			}
			if (a.getNeeds().contains("comfort")) {
				ADLeffort += Needs.getInstance().getComfort();
				activations++;
			}
			if (a.getNeeds().contains("hygiene")) {
				ADLeffort += Needs.getInstance().getHygiene();
				activations++;
			} 
			if (a.getNeeds().contains("bladder")) {
				ADLeffort += Needs.getInstance().getBladder();
				activations++;
			} 
			if (a.getNeeds().contains("energy")) {
				ADLeffort += Needs.getInstance().getEnergy();
				activations++;
			} 
			if (a.getNeeds().contains("fun")) {
				ADLeffort += Needs.getInstance().getFun();
				activations++;
			} 
			return ADLeffort/activations;
		}
		return 0;
	}

	/**
	 * Actions to perform when the ADL has been completed
	 * @param ADLindex: Index of the ADL just executed
	 */
	private static void completeADL(int ADLindex) {
		Iterator<ADLEffect> x = hLADL.get(ADLindex).getEffects().iterator();
		while (x.hasNext()) {
			ADLEffect badl;
			badl = x.next();
			if (badl.getName().equals("hunger")) {
				Needs.getInstance().setHunger(Needs.getInstance().getHunger()+badl.getEffect());
				if (Needs.getInstance().getHunger() < 0)
					Needs.getInstance().setHunger(0);
				if (Needs.getInstance().getHunger() > 1)
					Needs.getInstance().setHunger(1);				
			}
			if (badl.getName().equals("comfort")) {
				Needs.getInstance().setComfort(Needs.getInstance().getComfort()+badl.getEffect());
				if (Needs.getInstance().getComfort() < 0)
					Needs.getInstance().setComfort(0);
				if (Needs.getInstance().getComfort() > 1)
					Needs.getInstance().setComfort(1);	
			}
			if (badl.getName().equals("hygiene")) {
				Needs.getInstance().setHygiene(Needs.getInstance().getHygiene()+badl.getEffect());
				if (Needs.getInstance().getHygiene() < 0)
					Needs.getInstance().setHygiene(0);
				if (Needs.getInstance().getHygiene() > 1)
					Needs.getInstance().setHygiene(1);
			}
			if (badl.getName().equals("bladder")) {
				Needs.getInstance().setBladder(Needs.getInstance().getBladder()+badl.getEffect());
				if (Needs.getInstance().getBladder() < 0)
					Needs.getInstance().setBladder(0);
				if (Needs.getInstance().getBladder() > 1)
					Needs.getInstance().setBladder(1);				
			}
			if (badl.getName().equals("energy")) {
				Needs.getInstance().setEnergy(Needs.getInstance().getEnergy()+badl.getEffect());
				if (Needs.getInstance().getEnergy() < 0)
					Needs.getInstance().setEnergy(0);
				if (Needs.getInstance().getEnergy() > 1)
					Needs.getInstance().setEnergy(1);	
			}
			if (badl.getName().equals("fun")) {
				if (Needs.getInstance().getFun() < 0)
					Needs.getInstance().setFun(0);
				if (Needs.getInstance().getFun() > 1)
					Needs.getInstance().setFun(1);	
				Needs.getInstance().setFun(Needs.getInstance().getFun()+badl.getEffect());
			}
		}
		hLADL.get(keyBadl).setDoneToday(1);
		hLADL.get(keyBadl).setCyclicalityN(0);
	}

	private static void Logs(int logType) {
		Time t = new Time(tick % 86400);
		switch (logType) {
		case 1: 
			System.out.println ("ADL: "+ badl.getName() +
					" with rank "+ badl.getRank() + 
					" day hour: " +t.getHour() +":"+t.getMinute()+
					" minutes required: "+ (int) usedTime/60);
			System.out.printf ("oldNeeds: Hu:%.2f", Needs.getInstance().getHunger());
			System.out.printf (", C:%.2f", Needs.getInstance().getComfort());
			System.out.printf (", Hy:%.2f", Needs.getInstance().getHygiene());
			System.out.printf (", B:%.2f", Needs.getInstance().getBladder());
			System.out.printf (", E:%.2f", Needs.getInstance().getEnergy());
			System.out.printf (", F:%.2f", Needs.getInstance().getFun());
			System.out.print (", Cycl: "+ badl.getCyclicalityN()+":"+badl.getCyclicalityD());
			System.out.println();				
			break;

		case 2:
			System.out.printf ("newNeeds: Hu:%.2f", Needs.getInstance().getHunger());
			System.out.printf (", C:%.2f", Needs.getInstance().getComfort());
			System.out.printf (", Hy:%.2f", Needs.getInstance().getHygiene());
			System.out.printf (", B:%.2f", Needs.getInstance().getBladder());
			System.out.printf (", E:%.2f", Needs.getInstance().getEnergy());
			System.out.printf (", F:%.2f", Needs.getInstance().getFun());
			System.out.print (", Cycl: "+ badl.getCyclicalityN()+":"+badl.getCyclicalityD());
			System.out.println();
			break;
		}

	}

	/**
	 * Updates the needs of the user 
	 */
	private static void updateNeeds(int times) {

		for (int i=0; i<=times; i++) {
			if (Needs.getInstance().getHunger() < 1.0) 
				Needs.getInstance().setHunger(Needs.getInstance().getHunger() 	+ Constants.HUNGER);
			if (Needs.getInstance().getComfort() < 1.0) 
				Needs.getInstance().setComfort(Needs.getInstance().getComfort() + Constants.COMFORT);
			if (Needs.getInstance().getHygiene() < 1.0) 
				Needs.getInstance().setHygiene(Needs.getInstance().getHygiene()	+ Constants.HYGIENE);
			if (Needs.getInstance().getBladder() < 1.0) 
				Needs.getInstance().setBladder(Needs.getInstance().getBladder()	+ Constants.BLADDER);
			if (Needs.getInstance().getEnergy() < 1.0) 
				Needs.getInstance().setEnergy(Needs.getInstance().getEnergy()	+ Constants.ENERGY);
			if (Needs.getInstance().getFun() < 1.0) 
				Needs.getInstance().setFun(Needs.getInstance().getFun()			+ Constants.FUN);
		}
		
	}

}