package extractor;

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

	static int day, minute;
	static int usedTime;
	static int positionBadl;
	static Day d;


	public static void main (String[] args) {

		System.out.println("Simulation start!");
		addADL();	//Adds some ADLs examples

		RandomGaussian gaussian = new RandomGaussian();

		for (day=0; day>-1; day++) {
			newDay();
			dayInitADL();
			for (minute=0; minute <=1440; minute ++) {
				minute += minute == 0 ? wake() : 0; 
				computeADLValue(minute*60);
				badl.setRank(-1000);
				for (ADL a:adl) {

					if ((a.getDoneToday()==0) && 
							(a.getRank() > badl.getRank()) &&
							(Day.getInstance().getWeather() >= a.getWeather()) &&
							(a.getDays().contains(Day.getInstance().getWeekDay())))
					{						
						badl=a;
						positionBadl = adl.indexOf(a);
					}
				}				
				completeADL(positionBadl);
				usedTime = (int) gaussian.getGaussian (badl.getTmean(), badl.getTvariability());
				System.out.println ("ADL: "+ badl.getName() +" with rank "+ badl.getRank() + " day hour: " +(int) minute/60 +" time: "+ (int)usedTime/60);
				System.out.printf ("Needs: Hu:%.2f", Needs.getInstance().getHunger());
				System.out.printf (", C:%.2f", Needs.getInstance().getComfort());
				System.out.printf (", Hy:%.2f", Needs.getInstance().getHygiene());
				System.out.printf (", B:%.2f", Needs.getInstance().getBladder());
				System.out.printf (", E:%.2f", Needs.getInstance().getEnergy());
				System.out.printf (", F:%.2f", Needs.getInstance().getFun());
				System.out.println();
				updateNeeds((int) usedTime/3600);
				minute+= (int) usedTime/60;
			}			
			System.out.println("ENDDAY!");
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Updates the needs of the user 
	 */
	private static void updateNeeds(int times) {

		for (int i=0; i<=times; i++) {
			if (Needs.getInstance().getHunger() < 1.0) 
				Needs.getInstance().setHunger(Needs.getInstance().getHunger() 	+ 0.1);
			if (Needs.getInstance().getComfort() < 1.0) 
				Needs.getInstance().setComfort(Needs.getInstance().getComfort() + 0.1);
			if (Needs.getInstance().getHygiene() < 1.0) 
				Needs.getInstance().setHygiene(Needs.getInstance().getHygiene()	+ 0.1);
			if (Needs.getInstance().getBladder() < 1.0) 
				Needs.getInstance().setBladder(Needs.getInstance().getBladder()	+ 0.1);
			if (Needs.getInstance().getEnergy() < 1.0) 
				Needs.getInstance().setEnergy(Needs.getInstance().getEnergy()	+ 0.1);
			if (Needs.getInstance().getFun() < 1.0) 
				Needs.getInstance().setFun(Needs.getInstance().getFun()			+ 0.1);
		}
	}


	/**
	 * Actions to perform when the ADL has been completed
	 * @param ADLindex: Index of the ADL just executed
	 */
	private static void completeADL(int ADLindex) {
		Iterator<ADLEffect> x = adl.get(positionBadl).getEffects().iterator();
		while (x.hasNext()) {
			ADLEffect badl;
			badl = x.next();
			if (badl.getName().equals("hunger")) 
				Needs.getInstance().setHunger(Needs.getInstance().getHunger()+badl.getEffect());
			if (badl.getName().equals("comfort")) 
				Needs.getInstance().setComfort(Needs.getInstance().getComfort()+badl.getEffect());
			if (badl.getName().equals("hygiene")) 
				Needs.getInstance().setHygiene(Needs.getInstance().getHygiene()+badl.getEffect());
			if (badl.getName().equals("bladder")) 
				Needs.getInstance().setBladder(Needs.getInstance().getBladder()+badl.getEffect());
			if (badl.getName().equals("energy")) 
				Needs.getInstance().setEnergy(Needs.getInstance().getEnergy()+badl.getEffect());
			if (badl.getName().equals("fun")) 
				Needs.getInstance().setFun(Needs.getInstance().getFun()+badl.getEffect());
		}
		adl.get(positionBadl).setDoneToday(1);
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
		RandomGaussian gaussian = new RandomGaussian();
		return (int) gaussian.getGaussian (7*60, 60);
	}


	/**
	 * Operations applied to all the ADL each day
	 */
	private static void dayInitADL() {
		int n,d;
		for (ADL a : adl)  {
			a.setDoneToday(0); //In this way I avoid duplication of activities
			n = a.getCyclicalityN();
			d = a.getCyclicalityD();
			if (n<d)
				a.setCyclicalityN(n++);			
		}
	}


	private static void computeADLValue(int minute) {

		for (ADL a : adl) {
			a.setRank((double) ((a.getCyclicalityN() / a.getCyclicalityD()) + 
					a.isMandatory() + 
					needsEffort(a) +
					timeDependence(a, minute)));
		}
	}

	private static double timeDependence(ADL a, int minute) {
		BetaDistribution b = new BetaDistribution(a.getActivationShapeA(), a.getActivationShapeB());
		double test = b.density((0.5 + (minute - a.getBestTime()) / (2*a.getTvariability())));

		return test;
	}


	private static double needsEffort(ADL a) {
		double ADLeffort = 0.0;
		if (a.getNeeds() != null) {
			if (a.getNeeds().contains("hunger")) {
				ADLeffort += Needs.getInstance().getHunger();
			}
			if (a.getNeeds().contains("comfort")) {
				ADLeffort += Needs.getInstance().getComfort();
			}
			if (a.getNeeds().contains("hygiene")) {
				ADLeffort += Needs.getInstance().getHygiene();
			} 
			if (a.getNeeds().contains("bladder")) {
				ADLeffort += Needs.getInstance().getBladder();
			} 
			if (a.getNeeds().contains("energy")) {
				ADLeffort += Needs.getInstance().getEnergy();
			} 
			if (a.getNeeds().contains("fun")) {
				ADLeffort += Needs.getInstance().getFun();
			} 
		}
		return ADLeffort;
	}


	private static void addADL() {
		ArrayList <Integer> days = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7));
		ArrayList <Integer> marketDays = new ArrayList<Integer>(Arrays.asList(4,6));

		//ID, NAME, DAYS, WEATHER, TMEAN, TVARIABILITY, MANDATORY, BESTTIME, TYPE, CYCLICALITY, NEEDS, EFFECTS, BETADISTRA, BETADISTRB
		//Morning
		adl.add(new ADL (101, "Breakfast", days, 0, 10*60, 5*60, 1, 8*3600, 0, 1,  
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.3), new ADLEffect("bladder", +0.3))), 2.0,5.0));
		adl.add(new ADL (102, "MorningSitcom1", days, 0, 60*60, 10*60, 1, 9*3600, 0, 1, 
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.2), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (103, "MorningSitcom2", days, 0, 30*60, 10*60, 1, 10*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.2), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (104, "Shopping", days, 0, 120*60, 60*60, 1, 9*3600, 0, 1,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.add(new ADL (105, "Garden", days, 3, 180*60, 60*60, 1, 10*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.3), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.add(new ADL (106, "Shower", days, 0, 20*60, 10*60, 1, 11*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("hygiene")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hygiene", -0.5))), 2.0,5.0));
		adl.add(new ADL (107, "TakeAWalk", days, 3, 60*60, 15*60, 1, 10*3600, 0, 1,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.3), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.add(new ADL (108, "MARKET", marketDays, 3, 60*60, 15*60, 1, 9*3600, 0, 1,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.1), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.1))), 2.0,5.0));
		adl.add(new ADL (108, "Launder", days, 3, 60*60, 15*60, 1, 9*3600, 0, 7,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.2))), 2.0,5.0));


		adl.add(new ADL (100, "Lunch", days, 0, 40*60, 10*60, 1, 12*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.5), new ADLEffect("energy", +0.1))), 2.0,5.0));
		//Afternoon		

		adl.add(new ADL (201, "Sitcom1", days, 0, 60*60, 10*60, 0, 16*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.2), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (202, "Sitcom2", days, 0, 60*60, 10*60, 0, 14*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.2), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (203, "Tea", days, 0, 20*60, 5*60, 1, 17*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("hunger", "energy")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.2), new ADLEffect("energy", -0.1))), 2.0,5.0));
		adl.add(new ADL (204, "Garden", days, 3, 120*60, 60*60, 1, 16*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.3), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.add(new ADL (205, "ReadBook", days, 0, 30*60, 20*60, 1, 15*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.3), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (206, "Shower", days, 0, 20*60, 10*60, 1, 17*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("hygiene")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hygiene", -0.5))), 2.0,5.0));
		adl.add(new ADL (207, "WashingMachine", days, 1, 10*60, 5*60, 1, 15*3600, 0, 1,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.2))), 2.0,5.0));
		adl.add(new ADL (208, "TakeAWalk", days, 3, 60*60, 15*60, 1, 16*3600, 0, 1,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.3), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.3))), 2.0,5.0));

		adl.add(new ADL (200, "Dinner", days, 0, 50*60, 10*60, 1, 19*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.5), new ADLEffect("energy", +0.1))), 2.0,5.0));
		//Evening

		adl.add(new ADL (301, "Cinema", days, 0, 180*60, 60*60, 1, 20*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.5), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (302, "EveningTV", days, 0, 180*60, 30*60, 1, 21*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.5), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (303, "ReadBook", days, 0, 180*60, 60*60, 1, 20*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.5), new ADLEffect("fun", -0.3))), 2.0,5.0));

		adl.add(new ADL (300, "Sleep", days, 0, 480*60, 120*60, 1, 21*3600, 0, 1,
				new ArrayList<String>(Arrays.asList("energy")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", -1.0), new ADLEffect("comfort", -1.0))), 2.0,5.0));
	}
}
