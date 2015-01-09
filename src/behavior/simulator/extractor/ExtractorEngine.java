package behavior.simulator.extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.math3.distribution.BetaDistribution;

public class ExtractorEngine {


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
	
	public ExtractorEngine () {
		addADL();	//Adds ADLs examples
		RandomGaussian gaussian = new RandomGaussian();

	}
	
	private static void Logs(int logType) {
		switch (logType) {
		case 1: 
			System.out.println ("ADL: "+ badl.getName() +" with rank "+ badl.getRank() + " day hour: " +(int) minute/60 +" minutes required: "+ (int)usedTime/60);
			/*System.out.printf ("oldNeeds: Hu:%.2f", Needs.getInstance().getHunger());
			System.out.printf (", C:%.2f", Needs.getInstance().getComfort());
			System.out.printf (", Hy:%.2f", Needs.getInstance().getHygiene());
			System.out.printf (", B:%.2f", Needs.getInstance().getBladder());
			System.out.printf (", E:%.2f", Needs.getInstance().getEnergy());
			System.out.printf (", F:%.2f", Needs.getInstance().getFun());
			System.out.print (", Cycl: "+ badl.getCyclicalityN()+":"+badl.getCyclicalityD());
			System.out.println();
			 */
			break;

		case 3:
			System.out.printf ("newNeeds: Hu:%.2f", NeedsActor.getInstance().getHunger());
			System.out.printf (", C:%.2f", NeedsActor.getInstance().getComfort());
			System.out.printf (", Hy:%.2f", NeedsActor.getInstance().getHygiene());
			System.out.printf (", B:%.2f", NeedsActor.getInstance().getBladder());
			System.out.printf (", E:%.2f", NeedsActor.getInstance().getEnergy());
			System.out.printf (", F:%.2f", NeedsActor.getInstance().getFun());
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
			if (NeedsActor.getInstance().getHunger() < 1.0) 
				NeedsActor.getInstance().setHunger(NeedsActor.getInstance().getHunger() 	+ 0.05);
			if (NeedsActor.getInstance().getComfort() < 1.0) 
				NeedsActor.getInstance().setComfort(NeedsActor.getInstance().getComfort() + 0.03);
			if (NeedsActor.getInstance().getHygiene() < 1.0) 
				NeedsActor.getInstance().setHygiene(NeedsActor.getInstance().getHygiene()	+ 0.03);
			if (NeedsActor.getInstance().getBladder() < 1.0) 
				NeedsActor.getInstance().setBladder(NeedsActor.getInstance().getBladder()	+ 0.05);
			if (NeedsActor.getInstance().getEnergy() < 1.0) 
				NeedsActor.getInstance().setEnergy(NeedsActor.getInstance().getEnergy()	+ 0.03);
			if (NeedsActor.getInstance().getFun() < 1.0) 
				NeedsActor.getInstance().setFun(NeedsActor.getInstance().getFun()			+ 0.03);
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
			if (badl.getName().equals("hunger")) {
				NeedsActor.getInstance().setHunger(NeedsActor.getInstance().getHunger()+badl.getEffect());
				if (NeedsActor.getInstance().getHunger() < 0)
					NeedsActor.getInstance().setHunger(0);
				if (NeedsActor.getInstance().getHunger() > 1)
					NeedsActor.getInstance().setHunger(1);				
			}

			if (badl.getName().equals("comfort")) {
				NeedsActor.getInstance().setComfort(NeedsActor.getInstance().getComfort()+badl.getEffect());
				if (NeedsActor.getInstance().getComfort() < 0)
					NeedsActor.getInstance().setComfort(0);
				if (NeedsActor.getInstance().getComfort() > 1)
					NeedsActor.getInstance().setComfort(1);	
			}
			if (badl.getName().equals("hygiene")) {
				NeedsActor.getInstance().setHygiene(NeedsActor.getInstance().getHygiene()+badl.getEffect());
				if (NeedsActor.getInstance().getHygiene() < 0)
					NeedsActor.getInstance().setHygiene(0);
				if (NeedsActor.getInstance().getHygiene() > 1)
					NeedsActor.getInstance().setHygiene(1);
			}
			if (badl.getName().equals("bladder")) {
				NeedsActor.getInstance().setBladder(NeedsActor.getInstance().getBladder()+badl.getEffect());
				if (NeedsActor.getInstance().getBladder() < 0)
					NeedsActor.getInstance().setBladder(0);
				if (NeedsActor.getInstance().getBladder() > 1)
					NeedsActor.getInstance().setBladder(1);				
			}
			if (badl.getName().equals("energy")) {
				NeedsActor.getInstance().setEnergy(NeedsActor.getInstance().getEnergy()+badl.getEffect());
				if (NeedsActor.getInstance().getEnergy() < 0)
					NeedsActor.getInstance().setEnergy(0);
				if (NeedsActor.getInstance().getEnergy() > 1)
					NeedsActor.getInstance().setEnergy(1);	
			}
			if (badl.getName().equals("fun")) {
				if (NeedsActor.getInstance().getFun() < 0)
					NeedsActor.getInstance().setFun(0);
				if (NeedsActor.getInstance().getFun() > 1)
					NeedsActor.getInstance().setFun(1);	
				NeedsActor.getInstance().setFun(NeedsActor.getInstance().getFun()+badl.getEffect());
			}
		}
		adl.get(positionBadl).setDoneToday(1);
		adl.get(positionBadl).setCyclicalityN(0);
	}


	public static void newDay() {
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
		NeedsActor.getInstance().setEnergy(0);
		NeedsActor.getInstance().setComfort(0);
		RandomGaussian gaussian = new RandomGaussian();
		return (int) gaussian.getGaussian (8*60, 60);
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
	private static void computeADLsRank(int minute) {

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
				ADLeffort += NeedsActor.getInstance().getHunger();
				activations++;
			}
			if (a.getNeeds().contains("comfort")) {
				ADLeffort += NeedsActor.getInstance().getComfort();
				activations++;
			}
			if (a.getNeeds().contains("hygiene")) {
				ADLeffort += NeedsActor.getInstance().getHygiene();
				activations++;
			} 
			if (a.getNeeds().contains("bladder")) {
				ADLeffort += NeedsActor.getInstance().getBladder();
				activations++;
			} 
			if (a.getNeeds().contains("energy")) {
				ADLeffort += NeedsActor.getInstance().getEnergy();
				activations++;
			} 
			if (a.getNeeds().contains("fun")) {
				ADLeffort += NeedsActor.getInstance().getFun();
				activations++;
			} 
			return ADLeffort/activations;
		}
		return 0;
	}


	private static void addADL() {
		ArrayList <Integer> days = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7));
		ArrayList <Integer> marketDays = new ArrayList<Integer>(Arrays.asList(4,6));
		ArrayList <Integer> sitcomDays1 = new ArrayList<Integer>(Arrays.asList(1,3,5));
		ArrayList <Integer> sitcomDays2 = new ArrayList<Integer>(Arrays.asList(2,4));
		ArrayList <Integer> weekdays = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5));
		ArrayList <Integer> holidays = new ArrayList<Integer>(Arrays.asList(6,7));

		//ID, NAME, DAYS, WEATHER, TMEAN, TVARIABILITY, MANDATORY, BESTTIME, RANGETIME, CYCLICALITY, 
		//NEEDS, 
		//EFFECTS, BETADISTRA, BETADISTRB
		//Morning
		adl.add(new ADL (101, "Breakfast", days, 0, 10*60, 5*60, 1, 8*3600, 2*3600, 1,  
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.9), new ADLEffect("bladder", +0.3))), 2.0,5.0));
		adl.add(new ADL (102, "MorningSitcom1", weekdays, 0, 60*60, 10*60, 1, 9*3600, 2*3600, 3, 
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.2), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (103, "MorningSitcom2", sitcomDays2, 0, 30*60, 10*60, 1, 10*3600, 2*3600, 2,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.2), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (104, "Shopping", days, 0, 120*60, 40*60, 1, 10*3600, 2*3600, 4,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.2), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.add(new ADL (105, "Garden", days, 3, 180*60, 60*60, 1, 10*3600, 2*3600, 3,
				new ArrayList<String>(Arrays.asList("fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.3), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.add(new ADL (106, "Shower", days, 0, 40*60, 10*60, 1, 11*3600, 2*3600, 2,
				new ArrayList<String>(Arrays.asList("hygiene")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hygiene", -1.0))), 2.0,5.0));
		adl.add(new ADL (107, "TakeAWalk", days, 3, 60*60, 15*60, 1, 10*3600, 2*3600, 3,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.3), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.add(new ADL (108, "MARKET", marketDays, 3, 60*60, 15*60, 1, 9*3600, 2*3600, 3,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.1), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.1))), 2.0,5.0));
		adl.add(new ADL (108, "Launder", days, 3, 60*60, 15*60, 1, 9*3600, 2*3600, 7,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.2))), 2.0,5.0));
		adl.add(new ADL (109, "CleanUp", days, 0, 80*60, 20*60, 1, 10*3600, 2*3600, 5,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.2))), 2.0,5.0));
		adl.add(new ADL (110, "Toilet", days, 0, 5*60, 1*60, 1, 9*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("bladder")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("bladder", -1.0))), 2.0,5.0));
		adl.add(new ADL (110, "Toilet", days, 0, 5*60, 1*60, 1, 11*3600, 1*3600, 1,
				new ArrayList<String>(Arrays.asList("bladder")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("bladder", -1.0))), 2.0,5.0));
		adl.add(new ADL (111, "Phone", days, 0, 10*60, 5*60, 1, 10*3600, 2*3600, 1,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("fun", -0.1))), 2.0,5.0));

		adl.add(new ADL (100, "Lunch", days, 0, 50*60, 5*60, 3, 12*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.8), new ADLEffect("energy", +0.1))), 10.0,1.0));
		//Afternoon		

		adl.add(new ADL (201, "Sitcom1", holidays, 0, 60*60, 10*60, 0, 16*3600, 2*3600, 3,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.2), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (202, "Sitcom2", sitcomDays1, 0, 60*60, 10*60, 0, 14*3600, 2*3600, 2,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.2), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (203, "Tea", days, 0, 20*60, 5*60, 1, 17*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("hunger", "energy")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.2), new ADLEffect("energy", -0.1))), 2.0,5.0));
		adl.add(new ADL (204, "Garden", days, 3, 120*60, 60*60, 1, 16*3600, 2*3600, 3,
				new ArrayList<String>(Arrays.asList("fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.3), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.add(new ADL (205, "ReadBook", days, 0, 30*60, 20*60, 1, 15*3600, 2*3600, 2,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.3), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (206, "Shower", days, 0, 40*60, 10*60, 1, 17*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("hygiene")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hygiene", -1.0))), 2.0,5.0));
		adl.add(new ADL (207, "WashingMachine", days, 1, 10*60, 5*60, 1, 15*3600, 2*3600, 4,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.2))), 2.0,5.0));
		adl.add(new ADL (208, "TakeAWalk", days, 3, 60*60, 15*60, 1, 16*3600, 2*3600, 2,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", +0.3), new ADLEffect("energy", +0.3), new ADLEffect("hygiene", +0.3))), 2.0,5.0));
		adl.add(new ADL (209, "CleanUp", days, 0, 80*60, 20*60, 1, 15*3600, 2*3600, 5,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", +0.1),new ADLEffect("hygiene", +0.1))), 2.0,5.0));
		adl.add(new ADL (210, "Toilet", days, 0, 5*60, 1*60, 1, 18*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("bladder")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("bladder", -1.0))), 2.0,5.0));
		adl.add(new ADL (211, "Phone", days, 0, 5*60, 1*60, 1, 16*3600, 2*3600, 1,
				null,
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("fun", -0.1))), 2.0,5.0));
		adl.add(new ADL (212, "Nap", days, 0, 30*60, 10*60, 0, 15*3600, 1*3600, 1,
				new ArrayList<String>(Arrays.asList("energy")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", -0.4))), 1.5,1.5));

		adl.add(new ADL (200, "Dinner", days, 0, 50*60, 5*60, 1, 20*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("hunger")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("hunger", -0.8), new ADLEffect("energy", +0.1))), 10.0,1.0));

		//Evening

		adl.add(new ADL (301, "Cinema", days, 0, 180*60, 60*60, 1, 20*3600, 2*3600, 4,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.5), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (302, "EveningTV", days, 0, 180*60, 30*60, 1, 21*3600, 2*3600, 3,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.5), new ADLEffect("fun", -0.3))), 2.0,5.0));
		adl.add(new ADL (303, "ReadBook", days, 0, 180*60, 60*60, 1, 20*3600, 2*3600, 3,
				new ArrayList<String>(Arrays.asList("comfort", "fun")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("comfort", -0.5), new ADLEffect("fun", -0.3))), 2.0,5.0));

		adl.add(new ADL (300, "Sleep", days, 0, 480*60, 120*60, 1, 21*3600, 2*3600, 1,
				new ArrayList<String>(Arrays.asList("energy")),
				new ArrayList<ADLEffect>(Arrays.asList(new ADLEffect("energy", -2.0), new ADLEffect("comfort", -1.0))), 10.0,1.0));
	}
}
