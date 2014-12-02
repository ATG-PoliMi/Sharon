package repastsimphony.common;

public class Map {

	private int map [][] = new int [Constants.mapSizeH][Constants.mapSizeW];

	private static Map instance;
	private Map(){		
		for (int i=0; i<Constants.mapSizeH; i++) {
			for (int j=0; j<Constants.mapSizeW; j++) {
				map [i][j] = 0;
			}
		}

		if (Constants.mapID == 1) {
			//External borders
			for (int i=0; i<Constants.mapSizeW;i++) {
				map[0][i]=1;
				map[Constants.mapSizeH-1][i]=1;
				map[(int)Constants.mapSizeH/2][i]=1;
			}
			for (int i=0; i<Constants.mapSizeH;i++) {
				map[i][0]=1;
				map[i][Constants.mapSizeW-1]=1;				
			}
			
		}		
	}
	private int width;
	private int height;

	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = Constants.mapSizeH;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = Constants.mapSizeW;
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
