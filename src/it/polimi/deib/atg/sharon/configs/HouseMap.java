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


package it.polimi.deib.atg.sharon.configs;

import it.polimi.deib.atg.sharon.data.Sensor;

import java.io.*;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * This class describes the structure of the used map (dimension, walls, sensors)
 *
 */
public class HouseMap {

    // TODO migrate these to a single ref in the main file?
    private static final String CONFIG_PATH="config/env";
    private static final String MAP_FILENAME="map.conf";
    private static final String SENSORS_FILENAME="sensors.conf";

	private int map [][];
    public static int scale = 1;
	private Sensor[] s;

	private static HouseMap instance;

	private HouseMap(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(
                    CONFIG_PATH,MAP_FILENAME)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = null;
        char[] asChars= null;
        int cols = 0;
        int rows = 0;
        ArrayList<Integer> asList = new ArrayList<Integer>();
        try {
            line = reader.readLine();
            while(line != null){
                String[] chunks = line.split(" ");
                if(cols == 0){
                    cols = chunks.length;
                }
                for( int k = 0; k < cols; k++){
                    Integer elem = Integer.parseInt(chunks[k]);
                    if (elem > 1) {
                        scale = elem;
                        rows--;
                        break;
                    }
                    asList.add(elem);
                }
                rows++;
                line = reader.readLine();
            }
            ListIterator<Integer> itAsList = asList.listIterator();
            // The map reproduced as seen has x on cols and y on rows
            map = new int[cols][rows];
            for(int x=0; x<cols; x++){
                for(int y=0; y<rows; y++){
                    map[x][y]=itAsList.next();
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            reader = new BufferedReader(new FileReader(new File(
                    CONFIG_PATH,SENSORS_FILENAME)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Sensor> sAsList = new ArrayList<Sensor>();

        try {
            line = reader.readLine();
            while(line != null) {
                String[] chunks = line.split(",");
                sAsList.add(new Sensor(chunks[0], 2, Integer.parseInt(chunks[1])/scale
                        , Integer.parseInt(chunks[2])/scale));
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        s = sAsList.toArray(new Sensor[sAsList.size()]);
    }

	public static synchronized HouseMap getInstance(){

		if(instance==null) {
			instance=new HouseMap();
		}
		return instance;
    }

    public static Sensor[] getS() {
        return getInstance().s;
    }

    public static int[][] getMap(){
        return getInstance().map;
    }


}
