package behavior.simulator.extractor;

import java.lang.reflect.Array;


public class Needs {
	private static Needs instance;
	
	//Actor Needs
	private double hunger;
	private double comfort;
	private double hygiene;
	private double bladder;
	private double energy;
	private double fun;
	
	//House Needs
	private double stock;
	private double dirtiness;


	private Needs(){
		this.hunger		=	0.2;
		this.comfort	=	1.0;
		this.hygiene	=	0.0;
		this.bladder	=	0.0;
		this.energy		=	1.0;
		this.fun		=	0.0;
		this.stock		= 	0.0;
		this.dirtiness	= 	0.0;
	}
	
	public double []loadNeeds () {
		
		double needs [] = {hunger, comfort, hygiene, bladder, energy, fun, stock, dirtiness };
		return needs;
	}
	
	public void setNeeds(double [] needs){
		this.hunger		=	needs[0];
		this.comfort	=	needs[1];
		this.hygiene	=	needs[2];
		this.bladder	=	needs[3];
		this.energy		=	needs[4];
		this.fun		=	needs[5];
		this.stock		= 	needs[6];
		this.dirtiness	= 	needs[7];
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
	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
	}

	public double getDirtiness() {
		return dirtiness;
	}

	public void setDirtiness(double dirtiness) {
		this.dirtiness = dirtiness;
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