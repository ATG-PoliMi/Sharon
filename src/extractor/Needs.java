package extractor;


public class Needs {
	private static Needs instance;
	
	private double hunger;
	private double comfort;
	private double hygiene;
	private double bladder;
	private double energy;
	private double fun;


	private Needs(){
		this.hunger	=	1.0;
		this.comfort=	1.0;
		this.hygiene=	1.0;
		this.bladder=	1.0;
		this.energy	=	1.0;
		this.fun	=	1.0;	
	}
	
	public double getHunger() {
		return hunger;
	}

	public void setHunger(double hunger) {
		this.hunger = hunger;
	}

	public double getComfort() {
		return comfort;
	}

	public void setComfort(double comfort) {
		this.comfort = comfort;
	}

	public double getHygiene() {
		return hygiene;
	}

	public void setHygiene(double hygiene) {
		this.hygiene = hygiene;
	}

	public double getBladder() {
		return bladder;
	}

	public void setBladder(double bladder) {
		this.bladder = bladder;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public double getFun() {
		return fun;
	}

	public void setFun(double fun) {
		this.fun = fun;
	}

	public static void setInstance(Needs instance) {
		Needs.instance = instance;
	}

	public static synchronized Needs getInstance() {

		if(instance==null) {
			instance = new Needs();
		}
		return instance;
	}
}