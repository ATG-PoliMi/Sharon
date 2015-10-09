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
import java.util.Map;
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

		private static Boolean printConsoleActPatternSS=false;
		private static Integer fileHumanReadable=3; // 1- ARAS Format, 2-Human readable format, 3-Standard datasetCollector format
		private static Boolean shortPrint=false;
		int[][] worldMapMatrix;

		// ADL Handling
		Map<Integer, ADL> hLADL;
		LowLevelADLDB lLADL;
		// Map<Integer, ADLMatch> matchADL;
		HouseMap houseMap;

		// Sensorset Handling
		SensorsetManager ssManager;
		PatternSS currentPattern;
		Integer initialSS;
		Sensorset currentSS;
		Sensorset previousSS;
		Integer newSSId;
		Integer currentTimePattern;
		Integer currentTimeSS;
		Integer totalTimePattern;

		// User actions
		static int agentStatus = 1; // 1: extracting; 2: acting;
		static int idling = 0;

		// Utils
		static long timeInstant = 0;
		static long usedTime = 0;

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
			lLADL = LowLevelADLDB.getInstance();
			this.ssManager = SensorsetManager.getInstance();
			if((fileHumanReadable<1)||(fileHumanReadable>3)) fileHumanReadable=1;
		}

		@Override
		public void run() {
			Integer notScheduledSeconds=0;
			PrintWriter out;
			try {
				Thread.sleep(1000);
				out = new PrintWriter(new FileWriter(simulationOutputPrefix
						+ "0.txt"));
				
				//initialization phase
				action=3;//sleep
				currentPattern=lLADL.getPatternSS(lLADL.getMatch(action).getPatternID());
				initialSS = currentPattern.getInitialSSIdAPriori();
				currentSS=SensorsetManager.getInstance().getSensorsetByID(initialSS);
				previousSS=currentSS;
				
				//loop for every second
				for (timeInstant = 0; timeInstant < (86400 * simulatedDays) ; timeInstant++) {
					
					idling++;
					switch (agentStatus) {
					case 1: // Extracting + computing
						ADLQueue CADL;
						try {
							if (queue.isEmpty()){
								//System.out.println("***** A: EMPTY queue SensorsetSimulation *****");
								notScheduledSeconds++;
								//timeInstant--;
								//throw new Exception("***** A: EMPTY queue SensorsetSimulation *****");
							} else {
								CADL = queue.take();
								action = CADL.getADLId();
								totalTimePattern = (int) (long) CADL.getTime();
								
								//choose the pattern according to the last sensorset and the probability
								llADLIndex = lLADL.getPatternIDSS(currentSS,action);
								
								// chosen pattern for the activity
								currentPattern = lLADL.getPatternSS(llADLIndex);

								// chosen ss to start the pattern
								initialSS = currentPattern.getInitialSSIdAPrioriAndTransitionMatrix(currentSS.getIdSensorset());
								currentSS = SensorsetManager.getInstance().getSensorsetByID(initialSS);

								// time counters set to zero
								currentTimeSS = 0;
								currentTimePattern = 0;
								
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
							if(currentTimeSS>=currentSS.getMinTime()){
								//if the actual duration of this ss is >= then its minimum is possible to change
								if(currentTimeSS>=currentSS.getMaxTime()){
									//force to change SS
									newSSId=currentPattern.getDifferentSS(currentSS.getIdSensorset());
								}else{
									//compute using probability the next SS (can be the same)
									newSSId=currentPattern.getNextSS(currentSS.getIdSensorset());
								}
								if(!newSSId.equals(currentSS.getIdSensorset())){
									currentTimeSS=0;
									currentSS=SensorsetManager.getInstance().getSensorsetByID(newSSId);
								}
							}
						}
						break;
					}

					if ((timeInstant % 86400 == 0) && (timeInstant > 0)) {
						out.close();
						out = new PrintWriter(new FileWriter(simulationOutputPrefix
								+ (int) timeInstant / 86400 + ".txt"));
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
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Not scheduled seconds:"+notScheduledSeconds.toString());
		}
		
		public String printActiveSensorsStandard(int action,Sensorset currentSS){
			String activeSensors = "";

			activeSensors += timeInstant % 86400;
			activeSensors += ", ";
			
			activeSensors += currentSS.getIdSensorset();
			activeSensors += ", ";
			
			activeSensors += action;
			return activeSensors;
		}


		public String printActiveSensors(int action,Sensorset currentSS) {
			//print in ARAS format
			String activeSensors = "";
			Sensor[] sensorsArray = HouseMap.getS();

			int sId=0;
			   for (@SuppressWarnings("unused") Sensor aSensorsArray : sensorsArray) {
				sId++;
				if(currentSS.getActivatedSensorsId().contains(sId)){
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
			
			/*int sId=0;
			for (Sensor aSensorsArray : sensorsArray) {
				sId++;
				if(currentSS.getActivatedSensorsId().contains(sId)){
					//activeSensors +="1, ";
					activeSensors +=this.houseMap.getSensorById(sId).getName()+", ";
				}
			}*/
			
			for(Integer i:currentSS.getActivatedSensorsId()){
				activeSensors +=this.houseMap.getSensorById(i).getName()+", ";
			}
			

			// and ground truth
			activeSensors += "-----   "+pattName;
			return activeSensors;
		}
	}
