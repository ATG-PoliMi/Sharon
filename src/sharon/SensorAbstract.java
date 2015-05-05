package sharon;

public abstract class SensorAbstract {
	private int value,x,y;
	private String name;
	
	public SensorAbstract(String name, int value, int x, int y){
		this.name=name;
		this.value = value;
		this.x=x;
		this.y=y;
		
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

}
