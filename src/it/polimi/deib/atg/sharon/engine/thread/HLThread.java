package it.polimi.deib.atg.sharon.engine.thread;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import it.polimi.deib.atg.sharon.engine.LowLevelADL;
import it.polimi.deib.atg.sharon.data.Day;
import it.polimi.deib.atg.sharon.engine.ADL;
import it.polimi.deib.atg.sharon.engine.ADLEffect;
import it.polimi.deib.atg.sharon.engine.ADLMatcher;
import it.polimi.deib.atg.sharon.engine.Needs;
import it.polimi.deib.atg.sharon.configs.ADLDB;
import it.polimi.deib.atg.sharon.configs.ADLMatcherDB;
import it.polimi.deib.atg.sharon.configs.LLADLDB;
import it.polimi.deib.atg.sharon.configs.Parameters;
import it.polimi.deib.atg.sharon.utils.CumulateHistogram;
import it.polimi.deib.atg.sharon.utils.Distributions;
import it.polimi.deib.atg.sharon.utils.Time;


public class HLThread implements Runnable {

	//ADL QUEUE
	private BlockingQueue<ADLQueue> queue;

	//ADL Handling
	static Map<Integer, LowLevelADL>  	lLADL;
	static Map<Integer, ADLMatcher> 	matchADL;

	//User actions
	static int agentStatus=1; //0:Idling 1:Extracting a new ADL 2:Walking 3:Acting

	//Utils
	static long 	usedTime 		= 0;
	static long timeInstant = 0;
	static CumulateHistogram hist 	= new CumulateHistogram();
	static ADL onGoingAdl;
	private int simulatedDays;
	private int printLog;

	private int mode;

	public HLThread(BlockingQueue<ADLQueue> q, int simulatedDays, int printLog, int mode){
		this.queue=q;
		this.simulatedDays=simulatedDays;
		this.printLog=printLog;
		this.mode=mode;

	//	ADLDB.getInstance().getAdlmap()		= 	ADLDB.getInstance().getAdlmap();
		lLADL 		= 	LLADLDB.addLLADL();
		matchADL 	= 	ADLMatcherDB.addADLMatch();
		onGoingAdl = 	ADLDB.getInstance().defaultADL(); //Initial ADL: Sleeping
	}

	@Override
	public void run() {		
		for (timeInstant =0; timeInstant < 86400*simulatedDays; timeInstant++){

			usedTime++;
			if (timeInstant % 86400 == 0){
				newDay();
			}
			
			if (timeInstant % 60 == 0) {
				updateNeeds(1); //After each minute Needs are updated considering also the active ADL contribution				
				computeADLRank((int) timeInstant % 86400);
				ADLQueue ADLQ = changeADL();

				//printOnConsole(3); Hour histogram
				printOnConsole(4);

				if (ADLQ != null) { 
					try {
						if (mode !=0)
							queue.put(ADLQ);
						System.out.println("TIME: "+ timeInstant);
						System.out.println("TIME: "+ timeInstant + ", ID: "+ADLQ.getADLId());
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
			Distributions.loadDistributions("data/OutputHistogram.txt", "data/t/norm1_7d.txt");
			//Distributions.loadDistributions("data/t/norm1_23d.txt","data/t/norm1_7d.txt");
		}
	}


	private static ADLQueue changeADL() {
		//TODO:Starting Activity?
		ADL bestAdl = ADLDB.getInstance().defaultADL();

		if (usedTime > 60* onGoingAdl.getMinTime()){

			//Check Better ADL
			for (ADL a : ADLDB.getInstance().getAdlmap().values()) {			
				if ((a.getRank() >= bestAdl.getRank())) {
					bestAdl = a;
				}			
			}		

//			TODO print this info with the logging system
//			System.out.println(bestAdl.getName() + " " + onGoingAdl.getName() + "\n");
//			System.out.println(Needs.getInstance().toString() + "\n");
			
			if(bestAdl.getRank() > 0.01 && bestAdl.getName() != onGoingAdl.getName()) {
				ADLQueue X = new ADLQueue(onGoingAdl.getId(), usedTime);
				onGoingAdl.setActive(0);
				onGoingAdl = bestAdl;
				onGoingAdl.setActive(1);
				usedTime=0;
				printOnConsole(1); //TODO: Log row
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
		for (ADL a : ADLDB.getInstance().getAdlmap().values())  {
			a.setDoneToday(0); //In this way I avoid duplication of activities			
		}
	}

	/**
	 * Computation of the ADL rank
	 * @param minute
	 */
	private static void computeADLRank(int minute) {
		double r, active;
		Map<Integer, ADL> adlmap = ADLDB.getInstance().getAdlmap();

		double needs[] = Needs.getInstance().getStatus();
		for (ADL a : ADLDB.getInstance().getAdlmap().values()) {
			r = 0;
			active = (a.getActive() > 0) ? 1 : 0.7;
			for (int i=0; i<needs.length; i++) {
				r += needsContribution(a, i) * needs[i];
			}

			r *= (Math.random() < a.getExactTimeDescription(minute/60)) ? 1 : a.getExactTimeDescription(minute/60) * a.getExactDay(Day.getInstance().getWeekDay()%7) * 
					active * (0.80 + Math.random()*(1-0.80));
			a.setRank(r);
			adlmap.put(a.getId(), a);
		}
	}

	private static double needsContribution(ADL a, int i) {
		double contribution = 0.0;
		int needed = 0;
		if (a.getNeeds() != null) {
			needed = a.getNeeds().contains(Needs.getInstance().getName()[i]) ? 1 : 0;
			//TODO:What does this means?
			contribution = ((a.getNeeds().size()>0) && (needed>0)) ? ((double)1/a.getNeeds().size()) : 0.0;
		}
		return contribution;
	}


	private static void printOnConsole(int whatToPrint) {
		Time t = new Time(timeInstant % 86400);
		switch (whatToPrint) {
		//Metodo toString formattato (3 char per nome )
		case 1: 
			System.out.printf (Needs.getInstance().toString());
			System.out.println();

			double cBest=0;
			for (ADL a : ADLDB.getInstance().getAdlmap().values())  {

				//				if (a.getRank()>0)
				//					System.out.printf ("%s : %.3f ",a.getName(),a.getRank());
				if (a.getRank()>cBest){
					System.out.printf ("%s : %.3f ",a.getName(),a.getRank());
					cBest = a.getRank();
				}
			}
			System.out.println();
			System.out.print ("ADL SELECTED: "+ onGoingAdl.getName().substring(0, 1).toUpperCase()+ onGoingAdl.getName().substring(1)  +" with rank ");
			System.out.printf("%.3f", onGoingAdl.getRank());
			System.out.println(" day hour: " +t.getHour() +":"+t.getMinute());

			System.out.println();				
			break;

		case 2:
			System.out.print (t.getHour() +":"+t.getMinute()+" ");
			System.out.printf (Needs.getInstance().toString());	
			System.out.println();
			break;

		case 3:
			hist.updateHistogramH(timeInstant, onGoingAdl.getId());
			break;
		case 4:
			hist.updateHistogramM(timeInstant, onGoingAdl.getId());
			break;
		}
	}

	/**
	 * Updates user's and house needs
	 */
	private static void updateNeeds(int times) {
		Parameters.getInstance().update(timeInstant);
		ADLDB.getInstance().update(timeInstant);
		for (int i=0; i<times; i++) {
			Iterator<Double> ItrStatus = Arrays.asList(Needs.getInstance().getStatusWrapped()).iterator();
			Iterator<Double> ItrParam = Arrays.asList(Parameters.getInstance().getNeedsParameters()).iterator();
			Double[] NewNeedsStatus = new Double[Needs.getInstance().getStatusWrapped().length];
			
			int j = 0;
			while(ItrStatus.hasNext()){	
				double CurrentStatus = ItrStatus.next();
				if (CurrentStatus < 1.0) {
					NewNeedsStatus[j] = CurrentStatus + ItrParam.next();
				} else {
					NewNeedsStatus[j] = 1.0;
				}
				j++;
			}
			Needs.getInstance().setStatus(NewNeedsStatus);
			applyADLEffect(onGoingAdl);
		}		
	}

	/**
	 * Actions to perform when the ADL has been completed
	 * @param ADLindex: Index of the ADL just executed
	 */
	private static void applyADLEffect(ADL finishingADL) {
		Iterator<ADLEffect> x = finishingADL.getEffects().iterator();
		while (x.hasNext()) {
			ADLEffect effect = x.next();
			int index = Needs.getInstance().searchIndex(effect.getName());
			Double newStatus = Needs.getInstance().getStatus(index) + effect.getEffect();
			if(newStatus < 0){
				Needs.getInstance().setStatus(index, 0.0);
			} else if(newStatus > 1){
				Needs.getInstance().setStatus(index, 1.0);
			} else {
				Needs.getInstance().setStatus(index, newStatus);
			}
		}
	}
}
