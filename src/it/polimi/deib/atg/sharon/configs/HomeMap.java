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

/**
 * This class describes the structure of the used map (dimension, walls, sensors)
 *
 */
public class HomeMap {

	private int map [][] = new int [Parameters.mapSizeW][Parameters.mapSizeH];
	Sensor[] s;

	private static HomeMap instance;
	@SuppressWarnings("unused")
	private HomeMap(){		
		for (int i=0; i<Parameters.mapSizeW; i++) {
			for (int j=0; j<Parameters.mapSizeH; j++) {
				map [i][j] = 0;
			}
		}

		if (Parameters.mapID == 1) {
			//External borders
			for (int i=0; i<Parameters.mapSizeW;i++) {
				map[i][0]=1;
				map[i][Parameters.mapSizeH-1]=1;				
			}
			for (int i=0; i<Parameters.mapSizeH;i++) {
				map[0][i]=1;
				map[Parameters.mapSizeW-1][i]=1;				
			}
			for (int i=0; i<19;i++) {
				map[20][i]=1;
				map[30][i]=1;
				map[40][i]=1;
			}
			for (int i=0; i<40;i++) {
				map[i][18]=1;			
			}

			map[14][18]=0;
			map[15][18]=0;
			map[16][18]=0;

			map[22][18]=0;
			map[23][18]=0;
			map[24][18]=0;

			map[32][18]=0;
			map[33][18]=0;
			map[34][18]=0;

			s = new Sensor [Parameters.SENSORSNUMBER];

			//(Name, Sensor type(2+), X, Y)
			s[0] = new Sensor ("BedroomBed",2,3,10);
			s[1] = new Sensor ("BedroomWardrobe",2,18,2);
			s[2] = new Sensor ("BedroomDoor",2,15,18);

			s[3] = new Sensor ("BathBathtub",2,25,2);
			s[4] = new Sensor ("BathWC",2,29,8);			
			s[5] = new Sensor ("BathSink",2,29,12);
			s[6] = new Sensor ("BathWashingMachine",2,29,16);
			s[7] = new Sensor ("BathDoor",2,23,18);
			
			s[8] = new Sensor ("ExitDoor",2,33,18);

			s[9] = new Sensor ("LivingroomChair1",2,45,8);
			s[10] = new Sensor ("LivingroomChair2",2,48,11);
			s[11] = new Sensor ("LivingroomSofa1",2,48,2);
			s[12] = new Sensor ("LivingroomSofa2",2,58,10);
			s[13] = new Sensor ("LivingroomLight",2,45,2);
			s[14] = new Sensor ("LivingroomTV",2,48,28);

			s[15] = new Sensor ("KitchenTermometer",2,18,28);
			s[16] = new Sensor ("KitchenSink",2,14,28);
			s[17] = new Sensor ("KitchenCooker",2,10,28);
			s[18] = new Sensor ("KitchenCupboard",2,10,28);
			s[19] = new Sensor ("KitchenFridge",2,2,25);



			for (int i=0; i<s.length;i++) {
				map[s[i].getX()][s[i].getY()]=2;
			}			

		} else if (Parameters.mapID == 2) {
			for (int i=0; i<Parameters.mapSizeW;i++) {
				map[i][0]=1;
				map[i][Parameters.mapSizeH-1]=1;				
			}
			for (int i=0; i<Parameters.mapSizeH;i++) {
				map[0][i]=1;
				map[Parameters.mapSizeW-1][i]=1;				
			}
			for (int i=10; i<31; i++) {
				map[20][i]=1;
				map[i][20]=1;
			}
		}
	}
	public Sensor[] getS() {
		return s;
	}

	public void setS(Sensor[] s) {
		this.s = s;
	}

	/**
	 * * @param x: User Coord X
	 * @param y: User Coord Y
	 * @return current user room
	 */
	public int getHouseArea (double x, double y) {
		if (x>40)
			return 5;
		else if (y>20) {
			return 4;
		} else if (x<18)
			return 1;
		else if (x<30)
			return 2;
		else
			return 3;
	}

	public static synchronized HomeMap getInstance() {

		if(instance==null) {
			instance=new HomeMap();
		}
		return instance;
	}
	public int[][] getWorldMap() {
		return map;
	}

	public void printMap () {
		int[][] x = transpose(map);
		//int[][] x = map;

		for (int i=0; i<Parameters.mapSizeH; i++) {			
			for (int j=0; j<Parameters.mapSizeW; j++) {
				System.out.print(x[j][i]);
			}
			System.out.println(); 
		}
	}


	static int[][] transpose(int[][] mat) {
		int [][] x = new int[60][30];

		for (int i=0; i<Parameters.mapSizeW; i++) {			
			for (int j=0; j<Parameters.mapSizeH; j++) {
				x[i][Parameters.mapSizeH-1-j]=mat[i][j];
			}
		}

		return x;
	}

}
