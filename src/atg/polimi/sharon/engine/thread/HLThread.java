package atg.polimi.sharon.engine.thread;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import atg.polimi.sharon.engine.LowLevelADL;
import atg.polimi.sharon.data.Day;
import atg.polimi.sharon.engine.ADL;
import atg.polimi.sharon.engine.ADLEffect;
import atg.polimi.sharon.engine.ADLMatcher;
import atg.polimi.sharon.engine.Needs;
import atg.polimi.sharon.configs.ADLDB;
import atg.polimi.sharon.configs.ADLMatcherDB;
import atg.polimi.sharon.configs.LLADLDB;
import atg.polimi.sharon.configs.Parameters;
import atg.polimi.sharon.utils.CumulateHistogram;
import atg.polimi.sharon.utils.Distributions;
import atg.polimi.sharon.utils.Time;


public class HLThread implements Runnable {

	//ADL QUEUE
	private BlockingQueue<ADLQueue> queue;

	//ADL Handling
	static Map<Integer, ADL> 			hLADL;
	static Map<Integer, LowLevelADL>  	lLADL;
	static Map<Integer, ADLMatcher> 	matchADL;

	//User actions
	static int agentStatus=1; //0:Idling 1:Extracting a new ADL 2:Walking 3:Acting

	//Utils
	static long 	usedTime 		= 0;
	static long 	tick 			= 0;
	static CumulateHistogram hist 	= new CumulateHistogram();
	static ADL badl;
	private int simulatedDays;
	private int printLog;

	private int mode;

	public HLThread(BlockingQueue<ADLQueue> q, int simulatedDays, int printLog, int mode){
		this.queue=q;
		this.simulatedDays=simulatedDays;
		this.printLog=printLog;
		this.mode=mode;

		hLADL		= 	ADLDB.addADL();
		lLADL 		= 	LLADLDB.addLLADL();
		matchADL 	= 	ADLMatcherDB.addADLMatch();
		badl 		= 	hLADL.get(Parameters.SLEEP_ID); //Initial ADL: Sleeping		
	}

	@Override
	public void run() {		
		for (tick=0; tick < 86400*simulatedDays; tick++){

			usedTime++;
			if (tick % 86400 == 0){
				newDay();
			}
			
			if (tick % 60 == 0) {
				updateNeeds(1); //After each minute Needs are updated considering also the active ADL contribution				
				computeADLRank((int) tick % 86400);
				ADLQueue ADLQ = changeADL();

				//Logs(3); Hour histogram
				Logs(4);			

				if (ADLQ != null) { 
					try {
						if (mode !=0)
							queue.put(ADLQ);
						System.out.println("TIME: "+tick+ ", ID: "+ADLQ.getADLId()+", T:"+ADLQ.getTime());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("Producer Thread ends");
		if (printLog == 1) {
			hist.refineHistogram(simulatedDays);	//normalized for days number
			//hist.normalizationTo1Histogram(); 	//normalized to 1

			hist.printToFile("data/OutputHistogram.txt",2);
			Distributions.loadDistributions("data/OutputHistogram.txt","data/t/norm1_7d.txt");
			//Distributions.loadDistributions("data/t/norm1_23d.txt","data/t/norm1_7d.txt");
		}
	}


	private static ADLQueue changeADL() {
		ADL cadl = hLADL.get(1);		

		if (usedTime > 60*badl.getMinTime()){

			//Check Better ADL
			for (ADL a : hLADL.values()) {			
				if ((a.getRank() >= cadl.getRank())) {
					cadl 			= a;
				}			
			}		

			if(cadl.getRank() > 0.01 && cadl.getName() != badl.getName()) {
				ADLQueue X = new ADLQueue(badl.getId(), usedTime);
				badl.setActive(0);
				badl = cadl;
				badl.setActive(1);				
				usedTime=0;
				Logs(1); //TODO: Log row
				return X;
			}
		}
		return null;
	}

	private static void newDay() {

		dayInitADL();
		Day.getInstance().nextDay();

		//eraseNeeds();
//		System.out.println("***NEW DAY***! ("+Day.getInstance().getWeekDay()+")");
//		System.out.print("Today is ");
//		switch (Day.getInstance().getWeekDay()%7) {
//		case 0: System.out.print("Monday"); break;
//		case 1: System.out.print("Tuesday"); break;
//		case 2: System.out.print("Wednsday"); break;
//		case 3: System.out.print("Thursday"); break;
//		case 4: System.out.print("Friday"); break;
//		case 5: System.out.print("Saturday"); break;
//		case 6: System.out.print("Sunday"); break;
//		default: System.out.print("Error");
//		}
//		System.out.print(" and the weather is ");
//		switch (Day.getInstance().getWeather()) {
//		case 1: System.out.print("Rainy\n"); break;
//		case 2: System.out.print("Cloudy\n"); break;
//		case 3: System.out.print("Sunny\n"); break;
//		default: System.out.print("Error\n");
//		}

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
	 * Updates user's and house needs
	 */
	private static void updateNeeds(int times) {
		Parameters.update(tick);
		for (int i=0; i<times; i++) {
			if (Needs.getInstance().getHunger() 	< 1.0) 
				Needs.getInstance().setHunger(Needs.getInstance().getHunger() 			+ Parameters.HUNGER);
			if (Needs.getInstance().getStress() 	< 1.0) 
				Needs.getInstance().setStress(Needs.getInstance().getStress() 			+ Parameters.STRESS);
			if (Needs.getInstance().getSweat() 		< 1.0) 
				Needs.getInstance().setSweat(Needs.getInstance().getSweat()				+ Parameters.SWEAT);
			if (Needs.getInstance().getToileting() 	< 1.0) 
				Needs.getInstance().setToileting(Needs.getInstance().getToileting()		+ Parameters.TOILETING);
			if (Needs.getInstance().getTirediness() < 1.0) 
				Needs.getInstance().setTirediness(Needs.getInstance().getTirediness()	+ Parameters.TIREDINESS);
			if (Needs.getInstance().getBoredom() 	< 1.0) 
				Needs.getInstance().setBoredom(Needs.getInstance().getBoredom()			+ Parameters.BOREDOM);
			if (Needs.getInstance().getAsociality() < 1.0) 
				Needs.getInstance().setAsociality(Needs.getInstance().getAsociality()	+ Parameters.ASOCIALITY);
			if (Needs.getInstance().getDirtiness() 	< 1.0) 
				Needs.getInstance().setDirtiness(Needs.getInstance().getDirtiness()		+ Parameters.DIRTINESS);
			if (Needs.getInstance().getOutOfStock() < 1.0) 
				Needs.getInstance().setOutOfStock(Needs.getInstance().getOutOfStock()	+ Parameters.OUTOFSTOCK);
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
