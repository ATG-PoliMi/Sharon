package behavior.simulator.tuning;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import utils.Constants;
import utils.CumulateHistogram;
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
	static CumulateHistogram hist = new CumulateHistogram();

	//Support ADL
	static ADL badl;
	static ADL finishingADL;

	public static void main(String[] args) {	

		hLADL		= 	ADLDB.addADL();
		lLADL 		= 	LLADLDB.addLLADL();
		matchADL 	= 	ADLMatcherDB.addADLMatch();
		badl 		= 	hLADL.get(Constants.SLEEP_ID); //Initial ADL: Sleeping
		finishingADL= 	hLADL.get(Constants.SLEEP_ID); //Initial ADL: Sleeping

		for (tick=0; tick <= 86400*300; tick++) {
			usedTime++;

			if (tick % 86400 == 0){
				newDay();
			}
			if (tick % 60 == 0) {				
				updateNeeds(1); //After each minute Needs are updated considering also the active ADL contribution				
				computeADLRank((int) tick % 86400);
				changedADL = checkBetterADL();
				//Logs(3); Hour histogram
				Logs(4);
			}
			if (changedADL == 1) {
				//Operations when a new ADL is selected
				changedADL=0;
				Logs(1);
				usedTime=0;
			}
		}
		hist.refineHistogram(300.0f);
		hist.printToFile("data/histResults.txt",1);		
	}	

	private static int checkBetterADL() {
		changedADL=0;
		//Check Better ADL
		for (ADL a : hLADL.values()) {

			if ((a != badl) && (a.getRank() > badl.getRank()) && (usedTime > 60 * badl.getMinTime())) {
				changedADL		= 1;
				badl.setActive(0);
				badl 			= a;
				keyBadl 		= a.getId();
			}
			badl.setActive(1);

		}
		if (changedADL>0){
			//Logs(1);
		}
		return changedADL;
	}

	private static void newDay() {

		dayInitADL();
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
			// TODO Auto-generated catch block
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
			active = (a.getActive() > 0) ? 1 : 0.5;
			for (int i=0; i<needs.length; i++) {
				r += needsEffort(a, i) * needs[i];					
			}

			r *= a.getExactTimeDescription(minute/60) * a.getExactDay(Day.getInstance().getWeekDay()%7) * 
					active * (0.80+Math.random()*(1-0.80));
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
				needed += a.getNeeds().contains("hunger") 	? 1 : 0;
				break;
			case 1: 
				needed += a.getNeeds().contains("stress") 	? 1 : 0;
				break;
			case 2: 
				needed += a.getNeeds().contains("sweat") 	? 1 : 0;
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
				needed += a.getNeeds().contains("asociality")? 1 : 0;
				break;
			case 7: 
				needed += a.getNeeds().contains("outofstock") 	? 1 : 0;
				break;
			case 8: 
				needed += a.getNeeds().contains("dirtiness")? 1 : 0;
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

			for (ADL a : hLADL.values())  {
				if (a.getRank()>0)
					System.out.printf ("%s : %.3f ",a.getName(),a.getRank());											
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
	 * Updates user's and house's needs
	 */
	private static void updateNeeds(int times) {

		for (int i=0; i<times; i++) {
			if (Needs.getInstance().getHunger() 	< 1.0) 
				Needs.getInstance().setHunger(Needs.getInstance().getHunger() 			+ Constants.HUNGER);
			if (Needs.getInstance().getStress() 	< 1.0) 
				Needs.getInstance().setStress(Needs.getInstance().getStress() 			+ Constants.STRESS);
			if (Needs.getInstance().getSweat() 	< 1.0) 
				Needs.getInstance().setSweat(Needs.getInstance().getSweat()				+ Constants.SWEAT);
			if (Needs.getInstance().getToileting() 	< 1.0) 
				Needs.getInstance().setToileting(Needs.getInstance().getToileting()		+ Constants.TOILETING);
			if (Needs.getInstance().getTirediness() 	< 1.0) 
				Needs.getInstance().setTirediness(Needs.getInstance().getTirediness()	+ Constants.TIREDINESS);
			if (Needs.getInstance().getBoredom() 		< 1.0) 
				Needs.getInstance().setBoredom(Needs.getInstance().getBoredom()			+ Constants.BOREDOM);
			if (Needs.getInstance().getAsociality() 	< 1.0) 
				Needs.getInstance().setAsociality(Needs.getInstance().getAsociality()	+ Constants.ASOCIALITY);
			if (Needs.getInstance().getDirtiness() 	< 1.0) 
				Needs.getInstance().setDirtiness(Needs.getInstance().getDirtiness()		+ Constants.DIRTINESS);
			if (Needs.getInstance().getOutOfStock() 		< 1.0) 
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
}