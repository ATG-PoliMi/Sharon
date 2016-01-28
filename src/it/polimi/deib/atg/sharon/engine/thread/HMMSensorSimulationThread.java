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

import it.polimi.deib.atg.sharon.configs.ActivityViewer;
import it.polimi.deib.atg.sharon.configs.NeedsViewer;
import it.polimi.deib.atg.sharon.configs.ParamsManager;
import it.polimi.deib.atg.sharon.configs.HouseMap;
import it.polimi.deib.atg.sharon.configs.LowLevelADLDB;
import it.polimi.deib.atg.sharon.configs.SensorsetManager;
import it.polimi.deib.atg.sharon.data.PatternSS;
import it.polimi.deib.atg.sharon.data.Sensor;
import it.polimi.deib.atg.sharon.data.Sensorset;
import it.polimi.deib.atg.sharon.engine.ADL;
import it.polimi.deib.atg.sharon.utils.CumulateHistogram;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class HMMSensorSimulationThread implements Runnable {

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
		private static Boolean printStatistic=true;
		private static Boolean printConsoleActPatternSS=false;
		private static Integer fileHumanReadable=1; // 1- ARAS Format, 2-Human readable format, 3-simple format
		private static Boolean shortPrint=false;
		private static Boolean printAnalytics=true;
		
		//TODO move this in a configuration file
		private  List<Integer> analyzedActivities= new ArrayList<Integer>();
		
		int[][] worldMapMatrix;

		// ADL Handling
		Map<Integer, ADL> hLADL;
		LowLevelADLDB lLADL;
		// Map<Integer, ADLMatch> matchADL;
		HouseMap houseMap;
			
		// Sensorset Handling
		SensorsetManager ssManager;
		ParamsManager pManager;
		PatternSS currentPattern;
		Integer initialSS;
		Sensorset currentSS;
		Sensorset previousSS;
		Integer newSSId;
		Integer currentTimePattern;
		Integer currentTimeSS;
		Integer totalTimePattern;
		Integer plannedSSDuration;
		Random randomDistrSSInPattern;
		Random randomDistrTimeSS;
		// User actions
		static int agentStatus = 1; // 1: extracting; 2: acting;
		static int idling = 0;

		// Utils
		static long timeInstant = 0;
		static long usedTime = 0;
		static long deltaTime=0;

		// TARGETS
		private Integer llADLIndex;

		// Support ADL
		static CumulateHistogram hist = new CumulateHistogram();

		private BlockingQueue<ADLQueue> queue;
		private int simulatedDays;
		private int action;
		private String simulationOutputPrefix;

		public HMMSensorSimulationThread(BlockingQueue<ADLQueue> q,
				int simulatedDays, String sOutput) throws IOException {
			this.queue = q;
			this.simulatedDays = simulatedDays;
			this.simulationOutputPrefix = sOutput;
			houseMap = HouseMap.getInstance();
			this.pManager=ParamsManager.getInsatnce();
			lLADL = LowLevelADLDB.getInstance();
			this.ssManager = SensorsetManager.getInstance();
			if((fileHumanReadable<1)||(fileHumanReadable>3)) fileHumanReadable=1;
		}

		@Override
		public void run() {
			Integer notScheduledSeconds=0;
			PrintWriter out;
			PrintWriter outAnalyzer;
			try {
				Thread.sleep(1000);
				out = new PrintWriter(new FileWriter(simulationOutputPrefix
						+ "0.txt"));
				outAnalyzer=new PrintWriter(new FileWriter("data/outAnalyzer.txt"));
				//initialization phase
				action=1;//sleep
				currentPattern=lLADL.getPatternSS(lLADL.getMatch(action).getPatternID());
				initialSS = currentPattern.getInitialSSIdAPriori();
				currentSS=SensorsetManager.getInstance().getSensorsetByID(initialSS);
				previousSS=currentSS;
				
				//TODO move this in a configuration file
				if(printAnalytics){
					//TODO insert here the activities to be analyzed
					analyzedActivities.add(4);//LUNCH
					outAnalyzer.println("Analyzing activity: LUNCH");
					analyzedActivities.add(6);//SHOWER
					outAnalyzer.println("Analyzing activity: SHOWER");
					analyzedActivities.add(9);//CLEANING
					outAnalyzer.println("Analyzing activity: CLEANING");
					
					if(analyzedActivities.contains(action)){
						outAnalyzer.println("activity: "+currentPattern.getNameAct()+" pattern: "+currentPattern.getName());
						outAnalyzer.println("Time: 0 - Initial situation: "+printActiveSensorsAnalyzer(action,currentSS));
					}
				}			
						
				int previousAction=action;
				int durationAction=0;
				//loop for every second
				for (timeInstant = 0; timeInstant < (86400 * simulatedDays) ; timeInstant++) {
					
					idling++;
					switch (agentStatus) {
					case 1: // Extracting + computing
						ADLQueue CADL;
						try {
							if (queue.isEmpty()){
								notScheduledSeconds++;
							} else {
								CADL = queue.take();
								action = CADL.getADLId();
								totalTimePattern = (int) (long) CADL.getTime();			
								
								//choose the pattern according to the last sensorset and the probability
								llADLIndex = lLADL.getPatternIDSS(currentSS,action);
								
								// chosen pattern for the activity
								currentPattern = lLADL.getPatternSS(llADLIndex);
								
								// chosen ss to start the pattern
								//System.out.println("searching ss id "+currentSS.getIdSensorset());
								initialSS = currentPattern.getInitialSSIdAPrioriAndTransitionMatrix(currentSS.getIdSensorset());
								currentSS = SensorsetManager.getInstance().getSensorsetByID(initialSS);
								
								//TODO
								if(printAnalytics){
									if(analyzedActivities.contains(action)){
										deltaTime=timeInstant;
										outAnalyzer.println("activity: "+currentPattern.getNameAct()+" pattern: "+currentPattern.getName());
										outAnalyzer.println("Time: "+timeInstant+" - Initial situation: "+printActiveSensorsAnalyzer(action,currentSS));
									}
								}	
								
								
								//initializing distr prob for the pattern
								randomDistrSSInPattern=new Random();
								randomDistrTimeSS=new Random();
								
								// time counters set to zero
								currentTimeSS = 0;
								currentTimePattern = 0;
								Float expVfromPatt=currentPattern.getExpValue(currentSS.getIdSensorset());
								plannedSSDuration= currentSS.getDurationUsingDistribution(expVfromPatt,totalTimePattern);
								
								if(printConsoleActPatternSS){
									String sssids=" ids of SSs: ";
									for(Integer sss:currentPattern.getSsIds()){
										sssids+=sss.toString()+" , ";
									}
									System.out.println("Second: "+timeInstant+" actionId: "+action+" action: "+currentPattern.getNameAct()+" pattern: "+currentPattern.getName()+sssids);
								}
								agentStatus = 2;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
							System.out.println("QUEUE ERROR!");
						}
						break;

					case 2: // Walking+Acting
						
						//-- add here the algorithm to update the position
						
						currentTimeSS++;
						currentTimePattern++;
						if(currentTimePattern.equals(totalTimePattern)){
							//force to change the activity
							currentTimeSS=0;
							currentTimePattern=0;
							agentStatus=1;
						}else{
							if(currentTimeSS>=(plannedSSDuration+1)){
								//CHANGE SS
								Integer ti=(int) timeInstant;
								
								//next ss chosen using transition probability (given current ss) (no more rythm)
								newSSId=currentPattern.getNextSS(randomDistrSSInPattern,ti,currentPattern.getName(),currentSS.getIdSensorset(),action,currentTimePattern,totalTimePattern);
								
								if(!newSSId.equals(currentSS.getIdSensorset())){
									currentTimeSS=0;
									currentSS=SensorsetManager.getInstance().getSensorsetByID(newSSId);
									Float expVfromPatt=currentPattern.getExpValue(newSSId);
									plannedSSDuration= currentSS.getDurationUsingDistribution(expVfromPatt,totalTimePattern);
									//TODO
									if(printAnalytics){
										if(analyzedActivities.contains(action)){
											outAnalyzer.println("Time: "+timeInstant+" (difference to last: "+(timeInstant-deltaTime)+") - change situation: "+printActiveSensorsAnalyzer(action,currentSS));
											deltaTime=timeInstant;
										}
									}	
									
								}
							}
						}
						break;
					}
	
					if(action==previousAction){
						durationAction++;
					}else{
						ActivityViewer.getInstance().addActivity(previousAction, durationAction);
						durationAction=0;
						previousAction=action;
					}
					
					if ((timeInstant % 86400 == 0) && (timeInstant > 0)) {
						//System.out.println("Computed day "+(int) timeInstant / 86400);
						out.close();
						out = new PrintWriter(new FileWriter(simulationOutputPrefix+ (int) timeInstant / 86400 + ".txt"));
						ActivityViewer.getInstance().addActivity(action, durationAction);
						durationAction=0;
						previousAction=action;
						ActivityViewer.getInstance().initDay((int) timeInstant / 86400);
					}
					
					if((!shortPrint)||(!previousSS.equals(currentSS))){
						switch(fileHumanReadable){
						case 1:
							out.println(printActiveSensors(action,currentSS));
							break;
						case 2:
							out.println(printActiveSensors2(action,currentSS,currentPattern.getName(),currentPattern.getNameAct()));
							break;
						case 3:
							out.println(printActiveSensorsStandard(action,currentSS));
							break;
						}
					}
					previousSS=currentSS;
				}
				out.close();
				outAnalyzer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Not scheduled seconds:"+notScheduledSeconds.toString());
			if(printStatistic){
				NeedsViewer.getInstance().printFile();
				ActivityViewer.getInstance().printFile();
			}
		}
		
		public String printActiveSensorsStandard(int action,Sensorset currentSS){
			String activeSensors = "";

			activeSensors += (timeInstant % 86400)+1;
			activeSensors += ", ";
			
			activeSensors += currentSS.getIdSensorset();
			activeSensors += ", ";
			
			activeSensors += action;
			return activeSensors;
		}


		public String printActiveSensors(int action,Sensorset currentSS) {
			//print in ARAS format
			String activeSensors = "";
			int[] sid=ParamsManager.getInsatnce().getSid();
			for(int i=0;i<sid.length;i++){
				if(currentSS.getActivatedSensorsId().contains(sid[i])){
                    activeSensors += "1 ";
                }else{
                    activeSensors += "0 ";
                }
            }
            activeSensors += action + " 0 ";
			return activeSensors;
		}
		
		public String printActiveSensors2(int action,Sensorset currentSS,String pattName,String aname) {
			String activeSensors = "";

			activeSensors += timeInstant % 86400;
			activeSensors += ", "+aname+" ss: "+currentSS.getIdSensorset()+" nsens: "+currentSS.getActivatedSensorsId().size()+" -----";
			
			for(Integer i:currentSS.getActivatedSensorsId()){
				activeSensors +=this.houseMap.getSensorById(i).getName()+", ";
			}
			

			// and ground truth
			activeSensors += "-----   "+pattName;
			return activeSensors;
		}
		
		public String printActiveSensorsAnalyzer(int action,Sensorset currentSS) {
			String activeSensors = "";
			for(Integer i:currentSS.getActivatedSensorsId()){
				activeSensors +=this.houseMap.getSensorById(i).getName()+", ";
			}
			return activeSensors;
		}
	}
