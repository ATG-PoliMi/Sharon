package behavior.simulator.tuning;

import java.util.Iterator;
import java.util.Map;

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

public class HighLevelDaySimulator {

	//ADL Handling
	static Map<Integer, ADL> 			hLADL;
	static Map<Integer, LowLevelADL>  	lLADL;
	static Map<Integer, ADLMatcher> 	matchADL;

	//User actions
	static int agentStatus=1; //0:Idling 1:Extracting a new ADL 2:Walking 3:Acting

	//Utils
	static RandomGaussian gaussian = new RandomGaussian();
	static long 	tick;
	static int 		keyBadl;
	static long 	usedTime = 0;
	static int 		changedADL = 0;

	//Support ADL
	static ADL badl;
	static ADL finishingADL;

	public static void main(String[] args) {	

		hLADL		= 	ADLDB.addADL();
		lLADL 		= 	LLADLDB.addLLADL();
		matchADL 	= 	ADLMatcherDB.addADLMatch();
		badl 		= 	hLADL.get(Constants.SLEEP_ID); //Initial ADL: Sleeping
		finishingADL= 	hLADL.get(Constants.SLEEP_ID); //Initial ADL: Sleeping

		for (tick=0; tick >=0; tick++) {

			if (tick % 86400 == 0)
				newDay();			

			if (tick % 60 == 0) {
				updateNeeds(1); //After each minute Needs are updated considering also the active ADL contribution
				computeADLRank((int) tick % 86400);
				changedADL = checkBetterADL();
			}
			if (changedADL == 1) {
				//Operations when a new ADL is selected
				changedADL=0;
				Logs(2);
				usedTime=0;
			}
		}
	}	
	
	private static int checkBetterADL() {
		changedADL=0;
		//Check Better ADL
		for (ADL a : hLADL.values()) {
			if ((a != badl) && (a.getRank() > badl.getRank()) && (usedTime > 60 * a.getMinTime())) {
				finishingADL	= changedADL == 0 ? badl : finishingADL;
				changedADL		= 1;
				badl.setActive(0);
				badl 			= a;
				keyBadl 		= a.getId();
				badl.setActive(0);
			}
			badl.setActive(1);
			Logs(1);
		}
		return changedADL;
	}

	private static void newDay() {
		dayInitADL();
		Day.getInstance().nextDay();

		System.out.println("***NEW DAY***!");
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

	/**
	 * Operations applied to all the ADL each day
	 */
	private static void dayInitADL() {
		//TODO: Con nuova f peso da rimuovere?
		for (ADL a : hLADL.values())  {
			a.setDoneToday(0); //In this way I avoid duplication of activities			
		}
	}

	/**
	 * Computation of the ADL rank
	 * @param minute
	 */
	private static void computeADLRank(int minute) {
		double r;
		double active;

		double needs[] = Needs.getInstance().loadNeeds();
		for (ADL a : hLADL.values()) {
			r = 0;			
			active = (a.getActive() > 0) ? 1 : 0.8;
			for (int i=0; i<needs.length; i++) {
				r += needsEffort(a, i) * needs[i] * a.getExactTimeDescription(minute) * 
						a.getExactDay(Day.getInstance().getWeekDay()) * active;						
			}
			a.setRank(r);
		}
	}

	private static double needsEffort(ADL a, int i) {
		double ADLeffort = 0.0;
		int needed = 0;
		if (a.getNeeds() != null) {
			switch (i) {
			case 0: 
				needed += a.getNeeds().contains("hunger") ? 1 : 0;
				break;
			case 1: 
				needed += a.getNeeds().contains("comfort") ? 1 : 0;
				break;
			case 2: 
				needed += a.getNeeds().contains("hygiene") ? 1 : 0;
				break;
			case 3: 
				needed += a.getNeeds().contains("bladder") ? 1 : 0;
				break;
			case 4: 
				needed += a.getNeeds().contains("energy") ? 1 : 0;
				break;
			case 5: 
				needed += a.getNeeds().contains("fun") ? 1 : 0;
				break;
			case 6: 
				needed += a.getNeeds().contains("stock") ? 1 : 0;
				break;
			case 7: 
				needed += a.getNeeds().contains("dirtiness") ? 1 : 0;
				break;
			}
		}
		ADLeffort = ((a.getNeeds().size()>0) && (needed>0)) ? (1/a.getNeeds().size()) : 0;
		return ADLeffort;
	}

	
	private static void Logs(int logType) {
		Time t = new Time(tick % 86400);
		switch (logType) {
		case 1: 
			System.out.println ("ADL: "+ badl.getName() +
					" with rank "+ badl.getRank() + 
					" day hour: " +t.getHour() +":"+t.getMinute()+
					" minutes required: "+ (int) (usedTime-tick)/60); //TODO: Used time l'ho usato per altro! 
			//			System.out.printf ("oldNeeds: Hu:%.2f", Needs.getInstance().getHunger());
			//			System.out.printf (", C:%.2f", Needs.getInstance().getComfort());
			//			System.out.printf (", Hy:%.2f", Needs.getInstance().getHygiene());
			//			System.out.printf (", B:%.2f", Needs.getInstance().getBladder());
			//			System.out.printf (", E:%.2f", Needs.getInstance().getEnergy());
			//			System.out.printf (", F:%.2f", Needs.getInstance().getFun());
			//			System.out.print (", Cycl: "+ badl.getCyclicalityN()+":"+badl.getCyclicalityD());
			//			System.out.println();				
			break;

		case 2:
			//			System.out.printf ("newNeeds: Hu:%.2f", Needs.getInstance().getHunger());
			//			System.out.printf (", C:%.2f", Needs.getInstance().getComfort());
			//			System.out.printf (", Hy:%.2f", Needs.getInstance().getHygiene());
			//			System.out.printf (", B:%.2f", Needs.getInstance().getBladder());
			//			System.out.printf (", E:%.2f", Needs.getInstance().getEnergy());
			//			System.out.printf (", F:%.2f", Needs.getInstance().getFun());
			//			System.out.print (", Cycl: "+ badl.getCyclicalityN()+":"+badl.getCyclicalityD());
			//			System.out.println();
			break;
		}

	}

	/**
	 * Updates user's and house's needs
	 */
	private static void updateNeeds(int times) {

		for (int i=0; i<=times; i++) {
			if (Needs.getInstance().getHunger() < 1.0) 
				Needs.getInstance().setHunger(Needs.getInstance().getHunger() 		+ Constants.HUNGER/60);
			if (Needs.getInstance().getComfort() < 1.0) 
				Needs.getInstance().setComfort(Needs.getInstance().getComfort() 	+ Constants.COMFORT/60);
			if (Needs.getInstance().getHygiene() < 1.0) 
				Needs.getInstance().setHygiene(Needs.getInstance().getHygiene()		+ Constants.HYGIENE/60);
			if (Needs.getInstance().getBladder() < 1.0) 
				Needs.getInstance().setBladder(Needs.getInstance().getBladder()		+ Constants.BLADDER/60);
			if (Needs.getInstance().getEnergy() < 1.0) 
				Needs.getInstance().setEnergy(Needs.getInstance().getEnergy()		+ Constants.ENERGY/60);
			if (Needs.getInstance().getFun() < 1.0) 
				Needs.getInstance().setFun(Needs.getInstance().getFun()				+ Constants.FUN/60);
			if (Needs.getInstance().getDirtiness() < 1.0) 
				Needs.getInstance().setDirtiness(Needs.getInstance().getDirtiness()	+ Constants.DIRTINESS/60);
			if (Needs.getInstance().getStock() < 1.0) 
				Needs.getInstance().setStock(Needs.getInstance().getStock()			+ Constants.STOCK/60);
			updateADLNeeds(finishingADL);
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
			if (effects.getName().equals("comfort")) {
				Needs.getInstance().setComfort(Needs.getInstance().getComfort()+effects.getEffect());
				if (Needs.getInstance().getComfort() < 0)
					Needs.getInstance().setComfort(0);
				if (Needs.getInstance().getComfort() > 1)
					Needs.getInstance().setComfort(1);	
			}
			if (effects.getName().equals("hygiene")) {
				Needs.getInstance().setHygiene(Needs.getInstance().getHygiene()+effects.getEffect());
				if (Needs.getInstance().getHygiene() < 0)
					Needs.getInstance().setHygiene(0);
				if (Needs.getInstance().getHygiene() > 1)
					Needs.getInstance().setHygiene(1);
			}
			if (effects.getName().equals("bladder")) {
				Needs.getInstance().setBladder(Needs.getInstance().getBladder()+effects.getEffect());
				if (Needs.getInstance().getBladder() < 0)
					Needs.getInstance().setBladder(0);
				if (Needs.getInstance().getBladder() > 1)
					Needs.getInstance().setBladder(1);				
			}
			if (effects.getName().equals("energy")) {
				Needs.getInstance().setEnergy(Needs.getInstance().getEnergy()+effects.getEffect());
				if (Needs.getInstance().getEnergy() < 0)
					Needs.getInstance().setEnergy(0);
				if (Needs.getInstance().getEnergy() > 1)
					Needs.getInstance().setEnergy(1);	
			}
			if (effects.getName().equals("fun")) {
				Needs.getInstance().setFun(Needs.getInstance().getFun()+effects.getEffect());
				if (Needs.getInstance().getFun() < 0)
					Needs.getInstance().setFun(0);
				if (Needs.getInstance().getFun() > 1)
					Needs.getInstance().setFun(1);				
			}
			if (effects.getName().equals("stock")) {
				Needs.getInstance().setStock(Needs.getInstance().getStock()+effects.getEffect());
				if (Needs.getInstance().getStock() < 0)
					Needs.getInstance().setStock(0);
				if (Needs.getInstance().getStock() > 1)
					Needs.getInstance().setStock(1);				
			}
			if (effects.getName().equals("dirtiness")) {
				Needs.getInstance().setDirtiness(Needs.getInstance().getDirtiness()+effects.getEffect());
				if (Needs.getInstance().getDirtiness() < 0)
					Needs.getInstance().setDirtiness(0);
				if (Needs.getInstance().getDirtiness() > 1)
					Needs.getInstance().setDirtiness(1);				
			}
		}
		//hLADL.get(keyBadl).setDoneToday(1);		
	}

}
