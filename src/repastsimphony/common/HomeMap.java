package repastsimphony.common;

import repastsimphony.agent.Sensor;

import utils.Constants;
public class HomeMap {

	private int map [][] = new int [Constants.mapSizeW][Constants.mapSizeH];
	Sensor[] s;

	private static HomeMap instance;
	@SuppressWarnings("unused")
	private HomeMap(){		
		for (int i=0; i<Constants.mapSizeW; i++) {
			for (int j=0; j<Constants.mapSizeH; j++) {
				map [i][j] = 0;
			}
		}

		if (Constants.mapID == 1) {
			//External borders
			for (int i=0; i<Constants.mapSizeW;i++) {
				map[i][0]=1;
				map[i][Constants.mapSizeH-1]=1;				
			}
			for (int i=0; i<Constants.mapSizeH;i++) {
				map[0][i]=1;
				map[Constants.mapSizeW-1][i]=1;				
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

			s = new Sensor [Constants.SENSORSNUMBER];
			
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

		} else if (Constants.mapID == 2) {
			for (int i=0; i<Constants.mapSizeW;i++) {
				map[i][0]=1;
				map[i][Constants.mapSizeH-1]=1;				
			}
			for (int i=0; i<Constants.mapSizeH;i++) {
				map[0][i]=1;
				map[Constants.mapSizeW-1][i]=1;				
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

	public static synchronized HomeMap getInstance() {

		if(instance==null) {
			instance=new HomeMap();
		}
		return instance;
	}
	public int[][] getWorldMap() {
		return map;
	}

}
