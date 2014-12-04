package repastsimphony.common;

public class Map {

	private int map [][] = new int [Constants.mapSizeW][Constants.mapSizeH];

	private static Map instance;
	@SuppressWarnings("unused")
	private Map(){		
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
			
			map[3][10]=2;
			map[18][2]=2;
			map[25][2]=2;
			map[28][10]=2;
			map[28][12]=2;
			map[28][14]=2;			
			map[42][2]=2;
			map[44][2]=2;
			map[48][2]=2;
			map[15][18]=2;
			map[23][18]=2;
			map[33][18]=2;
			map[38][20]=2;
			
			map[10][28]=2;
			map[15][28]=2;
			map[55][28]=2;
			
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

	public static synchronized Map getInstance() {

		if(instance==null) {
			instance=new Map();
		}
		return instance;
	}
	public int[][] getWorldMap() {
		return map;
	}

}
