package behavior.simulator.extractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import org.apache.commons.math3.distribution.BetaDistribution;
public class Main {

	static ArrayList <ADL> adl= new ArrayList<ADL>();
	//TODO: correct the definition of badl
	static ADL badl = new ADL (0, "Foo", new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7)), 
			0, 0, 0, 0, 0, 0, 1, 
			new ArrayList<String>(Arrays.asList("hunger", "energy")),
			new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.3), new ADLEffect("energy", -0.3))), 1.0, 1.0);
	static ADL badl2;
	static int usedTime2;

	static int day, minute;
	static int usedTime;
	static int positionBadl;
	static Day d;
	static int preemptionCheck = 0;


	public static void main (String[] args) {

		System.out.println("Simulation start!");


		RandomGaussian gaussian = new RandomGaussian();

		for (day=0; day > -1; day++) {
			newDay();
			dayInitADL();
			usedTime=0;
			preemptionCheck=0;
			for (minute=0; minute <=1440; minute ++) {
				if (usedTime <=0) {
					minute += minute == 0 ? wake() : 0; 

					computeADLRank(minute*60);
					//badl.setRank(-1000);
					for (ADL a:adl) {
						if ((a.getDoneToday()==0) && 
								(a.getRank() > badl.getRank()) &&
								(Day.getInstance().getWeather() >= a.getWeather()) &&
								(a.getDays().contains(Day.getInstance().getWeekDay())))	{						
							badl=a;
							positionBadl = adl.indexOf(a);						
						}
					}
					usedTime = (int) gaussian.getGaussian (badl.getTmean(), badl.getTvariability());
					badl.setRank(badl.getRank() + 2);
					Logs(1);
				} 
				
				if (minute - preemptionCheck > 15) { //PREEMPTION CHECK
					//TODO: Insert Bladder ADL index (10) into the Constant File
					preemptionCheck = minute;
					if (Needs.getInstance().getBladder() > 0.90) {
						badl2 = badl;
						usedTime2 = usedTime;
						badl = adl.get(10);
						usedTime = (int) gaussian.getGaussian (badl.getTmean(), badl.getTvariability());
							System.out.println("START WC Preemption");
						Logs(1);
						completeADL(10);
						adl.get(10).setDoneToday(0);
						badl = badl2;
						usedTime = usedTime2;
						Logs(2);
							System.out.println("END WC Preemption");
					} else {}
				}
				
				minute += Math.min((int) usedTime/60, 10);
				usedTime -= 10*60;
				if (usedTime <= 0) {
					completeADL(positionBadl);
					Logs(2);				
					updateNeeds((int) usedTime/3600);
				}
			}			
			System.out.println("END DAY!");
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void Logs(int logType) {
		switch (logType) {
		case 1: 
			System.out.println ("ADL: "+ badl.getName() +" with rank "+ badl.getRank() + " day hour: " +(int) minute/60 +" minutes required: "+ (int)usedTime/60);
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
				Needs.getInstance().setHunger(Needs.getInstance().getHunger() 	+ 0.05);
			if (Needs.getInstance().getComfort() < 1.0) 
				Needs.getInstance().setComfort(Needs.getInstance().getComfort() + 0.03);
			if (Needs.getInstance().getHygiene() < 1.0) 
				Needs.getInstance().setHygiene(Needs.getInstance().getHygiene()	+ 0.03);
			if (Needs.getInstance().getBladder() < 1.0) 
				Needs.getInstance().setBladder(Needs.getInstance().getBladder()	+ 0.05);
			if (Needs.getInstance().getEnergy() < 1.0) 
				Needs.getInstance().setEnergy(Needs.getInstance().getEnergy()	+ 0.03);
			if (Needs.getInstance().getFun() < 1.0) 
				Needs.getInstance().setFun(Needs.getInstance().getFun()			+ 0.03);
		}
	}

	/**
	 * Actions to perform when the ADL has been completed
	 * @param ADLindex: Index of the ADL just executed
	 */
	private static void completeADL(int ADLindex) {
		Iterator<ADLEffect> x = adl.get(ADLindex).getEffects().iterator();
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
		adl.get(positionBadl).setDoneToday(1);
		adl.get(positionBadl).setCyclicalityN(0);
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
		for (ADL a : adl)  {
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

		for (ADL a : adl) {
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
}
