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

package it.polimi.deib.atg.sharon;

import it.polimi.deib.atg.sharon.configs.HighLevelADLDB;
import it.polimi.deib.atg.sharon.configs.NeedsDrift;
import it.polimi.deib.atg.sharon.configs.Parameters;
import it.polimi.deib.atg.sharon.engine.Needs;
import it.polimi.deib.atg.sharon.engine.thread.*;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

	//*****SIMULATION PARAMETERS:*****
    private static int def_simulatedDays = 5;    //Days to simulate
    //***** END PARAMETERS *****

	Parameters param = Parameters.getInstance();

	// Extra parameters - not to touch
    public static final boolean ENABLE_SENSORS_ACTIVITY = true;    //False: Only High Level, True: High Level + Low Level (Experimental!)
    public static final boolean PRINT_LOG = false;//False: no log
    public static final boolean DISABLE_PATH = false;
    public static final boolean USE_DRIFTS = false;// activates drifts
    public static final boolean USE_HMM_LL = false;// activates LowLevel Based on HMM
    public static final boolean GENERATE_HL_SCHEDULING = false; //true to generate, false to import from file
    public static final boolean MIMIC_ARAS = true; // output same format as ARAS Dataset

    //***** TEMPORARY CONFIG - TO BE RELOCATED WHEN INTRODUCING AGENT ******
    public static final double WALK_SPEED = 1.2; // m/s
    //***** END TEMP CONF

	private static String sensorOutputPrefix = "data/SensorOutput/DAY_";	//this file is heavy. Open it from explorer.
	private static String activityOutputPrefix = "data/ActivityOutput/DAY_";	//this file is heavy. Open it from explorer.
	//

	//Thread
	private static ActivitySimulationThread activitySimulationThread;
    private static ActivityImportThread activityImportationThread;
	private static BlockingQueue<ADLQueue> queue = new ArrayBlockingQueue<>(2000); //ADL QUEUE enough for 90 days

	public static void main(String[] args) {

		int simulatedDays;
		try{
			Needs.getInstance();
			if (USE_DRIFTS)
				Parameters.getInstance().setDrifts(NeedsDrift.loadNeedDrift());
			HighLevelADLDB.getInstance();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		//HIGH LEVEL SIMULATION
		if (args.length > 0){
			try {
				simulatedDays = Integer.parseInt(args[0]);
			}catch (Exception e){
				System.out.println("Incoherent input, refer to README. Quitting.");
				return;
			}
		}else{
			simulatedDays = def_simulatedDays;
		}

		if(GENERATE_HL_SCHEDULING){
			//GENERATE ACTIVITY SCHEDULING
			activitySimulationThread = new ActivitySimulationThread(queue, simulatedDays, activityOutputPrefix);
			new Thread(activitySimulationThread).start();
			System.out.println("Simulator correctly instantiated... Beginning the simulation");
		}else{
			//IMPORT ACTIVITY SCHEDULING
            activityImportationThread = new ActivityImportThread(queue, "config/ActivityInput/");
            simulatedDays=activityImportationThread.numberOfDays();
			new Thread(activityImportationThread).start();
			System.out.println("Simulator correctly instantiated... Beginning to import activities");
		}
        Runnable sensorSimulationThread;
        //LOW LEVEL SIMULATION
		try {
			if (ENABLE_SENSORS_ACTIVITY) {
				if (USE_HMM_LL)
					sensorSimulationThread = new HMMSensorSimulationThread(queue, simulatedDays, sensorOutputPrefix);
				else
					sensorSimulationThread = new SensorSimulationThread(queue,simulatedDays, sensorOutputPrefix);
				
				new Thread(sensorSimulationThread).start();
				System.out.println("LowLevel Starts");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
