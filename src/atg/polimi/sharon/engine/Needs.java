package atg.polimi.sharon.engine;


public class Needs {
	private static Needs instance;
	
	//Actor Needs
	private double hunger;
	private double stress;
	private double sweat;
	private double toileting;
	private double tirediness;
	private double boredom;
	private double asociality;
	
	//House Needs
	private double outOfStock;
	private double dirtiness;


	private Needs(){
		this.hunger			=	0.2;
		this.stress			=	1.0;
		this.sweat			=	0.0;
		this.toileting		=	0.0;
		this.tirediness		=	1.0;
		this.boredom		=	0.0;
		this.outOfStock		= 	0.0;
		this.dirtiness		= 	0.0;
		this.asociality		= 	0.0;
	}
	
	public double getHunger() {
		return hunger;
	}

	public void setHunger(double hunger) {
		this.hunger = hunger;
	}

	public double getStress() {
		return stress;
	}

	public void setStress(double stress) {
		this.stress = stress;
	}

	public double getSweat() {
		return sweat;
	}

	public void setSweat(double sweat) {
		this.sweat = sweat;
	}

	public double getToileting() {
		return toileting;
	}

	public void setToileting(double toileting) {
		this.toileting = toileting;
	}

	public double getTirediness() {
		return tirediness;
	}

	public void setTirediness(double tirediness) {
		this.tirediness = tirediness;
	}

	public double getBoredom() {
		return boredom;
	}

	public void setBoredom(double boredom) {
		this.boredom = boredom;
	}

	public double getAsociality() {
		return asociality;
	}

	public void setAsociality(double asociality) {
		this.asociality = asociality;
	}

	public double getOutOfStock() {
		return outOfStock;
	}

	public void setOutOfStock(double outOfStock) {
		this.outOfStock = outOfStock;
	}

	public double getDirtiness() {
		return dirtiness;
	}

	public void setDirtiness(double dirtiness) {
		this.dirtiness = dirtiness;
	}

	public double []loadNeeds () {
		
		double needs [] = {hunger, stress, sweat, toileting, tirediness, boredom, asociality, outOfStock, dirtiness};
		return needs;
	}
	
	public void setNeeds(double [] needs){
		this.hunger		=	needs[0];
		this.stress	=	needs[1];
		this.sweat	=	needs[2];
		this.toileting	=	needs[3];
		this.tirediness		=	needs[4];
		this.boredom		=	needs[5];
		this.asociality	=	needs[6];
		this.outOfStock		= 	needs[7];
		this.dirtiness	= 	needs[8];
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