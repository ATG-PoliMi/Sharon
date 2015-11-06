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
import it.polimi.deib.atg.sharon.engine.thread.ADLQueue;
import it.polimi.deib.atg.sharon.engine.thread.ActivitySimulationThread;
import it.polimi.deib.atg.sharon.engine.thread.HMMSensorSimulationThread;
import it.polimi.deib.atg.sharon.engine.thread.SensorSimulationThread;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

	//*****SIMULATION PARAMETERS:*****
	private static int def_simulatedDays 	= 31; 	//Days to simulate
	//***** END PARAMETERS *****
	
	Parameters param = Parameters.getInstance();

	// Extra parameters - not to touch
    public static final boolean ENABLE_SENSORS_ACTIVITY = true;    //False: Only High Level, True: High Level + Low Level (Experimental!)
    public static final boolean PRINT_LOG = false;//False: no log
    public static final boolean DISABLE_DIJKSTRA = true;
    public static final boolean USE_DRIFTS = true;// activates drifts
    public static final boolean USE_HMM_LL = true;// activates LowLevel Based on HMM

	private static String sensorOutputPrefix = "data/SensorOutput/DAY_";	//this file is heavy. Open it from explorer.
	private static String activityOutputPrefix = "data/ActivityOutput/DAY_";	//this file is heavy. Open it from explorer.
	//

	//Thread
	private static ActivitySimulationThread activitySimulationThread;
    private static BlockingQueue<ADLQueue> queue = new ArrayBlockingQueue<>(100); //ADL QUEUE

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
		activitySimulationThread = new ActivitySimulationThread(queue, simulatedDays, activityOutputPrefix);
		new Thread(activitySimulationThread).start();
		System.out.println("Simulator correctly instantiated... Beginning the simulation");
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