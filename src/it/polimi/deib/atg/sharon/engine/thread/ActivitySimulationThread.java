/*
 *
 * SHARON - Human Activities Simulator
 * Author: ATG Group (http://atg.deib.polimi.it/)
 *
 * Copyright (C) 2015, Politecnico di Milano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package it.polimi.deib.atg.sharon.engine.thread;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import it.polimi.deib.atg.sharon.Main;
import it.polimi.deib.atg.sharon.configs.HighLevelADLDB;

import it.polimi.deib.atg.sharon.data.Day;
import it.polimi.deib.atg.sharon.engine.ADL;
import it.polimi.deib.atg.sharon.engine.ADLEffect;
import it.polimi.deib.atg.sharon.engine.Needs;
import it.polimi.deib.atg.sharon.configs.Parameters;
import it.polimi.deib.atg.sharon.utils.CumulateHistogram;
import it.polimi.deib.atg.sharon.utils.Distributions;
import it.polimi.deib.atg.sharon.utils.Time;


public class ActivitySimulationThread implements Runnable {

	//ADL QUEUE
	private BlockingQueue<ADLQueue> queue;

	//ADL Handling
	//static LowLevelADLDB lLADL;
	//static Map<Integer, ADLMatch> 	matchADL;

	//User actions
	static int agentStatus=1; //0:Idling 1:Extracting a new ADL 2:Walking 3:Acting

	//Utils
	static long elapsed_time = 0;
	static long timeInstant = 0;
	static CumulateHistogram hist 	= new CumulateHistogram();
	static ADL ongoingAdl;
	private int simulatedDays;

    private String outputFilePrefix;
    private PrintWriter outFile;

    public ActivitySimulationThread(BlockingQueue<ADLQueue> q, int simulatedDays, String outputFilePrefix){
		this.queue=q;
		this.simulatedDays=simulatedDays;
        this.outputFilePrefix = outputFilePrefix;

        this.outFile = null;

        //	HighLevelADLDB.getInstance().getAdlmap()		= 	HighLevelADLDB.getInstance().getAdlmap();
		//lLADL 		= 	LowLevelADLDB.getInstance();
		ongoingAdl = HighLevelADLDB.getInstance().defaultADL(); //Initial ADL: Sleeping
	}

	@Override
	public void run() {		
		for (timeInstant =0; timeInstant < 86400*simulatedDays; timeInstant++){

			elapsed_time++;

			if (timeInstant % 60 == 0) {
                if (timeInstant % 86400 == 0){
                    try {
                        newDay();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
				updateNeeds(1); //After each minute Needs are updated considering also the active ADL contribution				
				computeADLRank((int) timeInstant % 86400);
				ADLQueue ADLQ = changeADL();

				this.updateHistogramM();

				if (ADLQ != null) { 
					try {
						if (Main.ENABLE_SENSORS_ACTIVITY)
							queue.put(ADLQ);

						//TODO Andrea comment System.out.println("TIME: "+ timeInstant + ", ID: "+ADLQ.getADLId());
                        ADL adl = HighLevelADLDB.getInstance().getADLById(ADLQ.getADLId());
                        outFile.println(timeInstant+","+ ongoingAdl.getId()+","+ ongoingAdl.getName()+","+new Time(timeInstant % 86400) );

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("Producer Thread ends");
		if (Main.PRINT_LOG) {
			hist.refineHistogram(simulatedDays);	//normalized for days number
			//hist.normalizationTo1Histogram(); 	//normalized to 1

			hist.printToFile("data/OutputHistogram.txt",2);
			Distributions.loadDistributions("data/OutputHistogram.txt", "data/t/norm1_7d.txt");
			//Distributions.loadDistributions("data/t/norm1_23d.txt","data/t/norm1_7d.txt");
		}
	}


	private  ADLQueue changeADL() {
		ADL bestAdl = HighLevelADLDB.getInstance().defaultADL();

		if (elapsed_time > 60* ongoingAdl.getMinTime()){

			//Check Better ADL
			for (ADL a : HighLevelADLDB.getInstance().getAdlmap().values()) {
				if ((a.getRank() >= bestAdl.getRank())) {
					bestAdl = a;
				}			
			}		

//			TODO print this info with the logging system
//			System.out.println(bestAdl.getName() + " " + ongoingAdl.getName() + "\n");
//			System.out.println(Needs.getInstance().toString() + "\n");
			
			if(bestAdl.getRank() > 0.01 && !bestAdl.getName().equals(ongoingAdl.getName())) {
				ADLQueue X = new ADLQueue(ongoingAdl.getId(), elapsed_time);
				ongoingAdl.setActive(false);
				ongoingAdl = bestAdl;
				ongoingAdl.setActive(true);
				elapsed_time = 0;
                //this.printVerboseOnConsole();
				return X;
			}
		}
		return null;
	}

	private void newDay() throws IOException {
        if (outFile != null)
            outFile.close();

        outFile = new PrintWriter(new FileWriter(outputFilePrefix +(int) timeInstant /86400+".txt"));
        outFile.println("simulation_seconds,activity_id,activity_name,time_of_the_day");
        outFile.println(timeInstant+","+ ongoingAdl.getId()+","+ ongoingAdl.getName()+","+new Time(timeInstant % 86400) );
		Day.getInstance().nextDay();


	}


	/**
	 * Computation of the ADL rank
	 * @param minute
	 */
	private static void computeADLRank(int minute) {
		double r, active;
		Map<Integer, ADL> adlmap = HighLevelADLDB.getInstance().getAdlmap();

		double needs[] = Needs.getInstance().getStatus();
		for (ADL a : HighLevelADLDB.getInstance().getAdlmap().values()) {
			r = 0;
			active = a.getActive() ? 1 : 0.8;
			for (int i = 0; i<needs.length; i++) {
				r += needsContribution(a, i) * needs[i];
			}

            r *= ((Math.random() < a.getTimeDescription(minute / 60)) ? 1 : (a.getTimeDescription(minute / 60))) * (a.getDayWeight(Day.getInstance().getWeekDay() % 7) *
                    active);
			a.setRank(r);
			adlmap.put(a.getId(), a);
		}
	}

	private static double needsContribution(ADL a, int i) {
		double contribution = 0.0;
		int needed = 0;
		if (a.getNeeds() != null) {
			needed = a.getNeeds().contains(Needs.getInstance().getName()[i]) ? 1 : 0;
			contribution = ((a.getNeeds().size()>0) && (needed>0)) ? ((double)1/a.getNeeds().size()) : 0.0;
		}
		return contribution;
	}


    private  void printVerboseOnConsole() {
        Time t = new Time(timeInstant % 86400);

        System.out.printf (Needs.getInstance().toString());
        System.out.println();

        double cBest=0;
        for (ADL a : HighLevelADLDB.getInstance().getAdlmap().values())  {
            if (a.getRank()>0)
                System.out.printf ("%s : %.3f ",a.getName(),a.getRank());
            if (a.getRank()>cBest){
                cBest = a.getRank();
            }
        }
        System.out.println();
        System.out.print ("ADL SELECTED: "+ ongoingAdl.getName().substring(0, 1).toUpperCase()+ ongoingAdl.getName().substring(1)  +" with rank ");
        System.out.printf("%.3f", ongoingAdl.getRank());
        System.out.println(" day hour: " +t.getHour() +":"+t.getMinute());

        System.out.println();
    }


    private void updateHistogramM() {
        hist.updateHistogramM(timeInstant, ongoingAdl.getId());
    }

	/**
	 * Updates user's and house needs
	 */
	private static void updateNeeds(int times) {
		Parameters.getInstance().update(timeInstant);
		HighLevelADLDB.getInstance().update(timeInstant);
		for (int i=0; i<times; i++) {
			Iterator<Double> ItrStatus = Arrays.asList(Needs.getInstance().getStatusWrapped()).iterator();
			Iterator<Double> ItrParam = Arrays.asList(Parameters.getInstance().getNeedsParameters()).iterator();
			Double[] NewNeedsStatus = new Double[Needs.getInstance().getStatusWrapped().length];
			
			int j = 0;
			while(ItrStatus.hasNext()){	
				double CurrentStatus = ItrStatus.next();
                NewNeedsStatus[j] = CurrentStatus + ItrParam.next();
				if (NewNeedsStatus[j] > 1.0){
					NewNeedsStatus[j] = 1.0;
				}
				j++;
			}
			Needs.getInstance().setStatus(NewNeedsStatus);
			applyADLEffect(ongoingAdl);
		}		
	}

	/**
	 * Actions to perform when the ADL has been completed
	 * @param finishingADL: Index of the ADL just executed
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
