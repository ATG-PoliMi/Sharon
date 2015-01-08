package behavior.simulator.tuning;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math3.distribution.BetaDistribution;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.GridPoint;
import utils.Constants;
import utils.Time;
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

public class Main {

	//ADL Handling
	static Map<Integer, ADL> hLADL;
	static Map<Integer, LowLevelADL>  lLADL;
	static Map<Integer, ADLMatcher> matchADL;

	//User actions
	static int agentStatus=1; //0:Idling 1:Extracting a new ADL 2:Walking 3:Acting
	static int idling;

	//Utils
	static RandomGaussian gaussian = new RandomGaussian();
	static long tick;
	static int keyBadl;
	static long usedTime;

	//BADL
	static ADL badl;

	public static void main(String[] args) {		
		
		idling++;
		
		hLADL		= 	ADLDB.addADL();
		lLADL 		= 	LLADLDB.addLLADL();
		matchADL 	= 	ADLMatcherDB.addADLMatch();
		badl = hLADL.get(Constants.SLEEP_ID); //Initial ADL: Sleeping

		for (tick=0; tick >=0; tick++) {
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
				usedTime = tick + (int) gaussian.getGaussian (badl.getTmean(), badl.getTvariability());
				badl.setRank(badl.getRank() + 2);
				idling = 0;
				Logs(1);
				agentStatus = 2;
				break;

			case 2:	//Computing new targets	
				agentStatus=3;
				break;


			case 3:	//Walking+Acting
				
				if (tick >= usedTime) {
					agentStatus = 1;
					completeADL(keyBadl);
					Logs(2);
					updateNeeds((int) (usedTime-tick)/3600);
					
				}
				break;
			}
		}
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
				Needs.getInstance().setHunger(Needs.getInstance().getHunger() + badl.getEffect());
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
					" minutes required: "+ (int) (usedTime-tick)/60);
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
