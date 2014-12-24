package behavior.simulator.planner;

public class Station {

	private int id;
	private double timePercentage;
	
	public Station(int id, double timePercentage) {
		super();
		this.id = id;
		this.timePercentage = timePercentage;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getTimePercentage() {
		return timePercentage;
	}
	public void setTimePercentage(double timePercentage) {
		this.timePercentage = timePercentage;
	}	

}
