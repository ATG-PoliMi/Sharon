package repastsimphony.agent;



import repast.simphony.engine.schedule.ScheduledMethod;

import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;


public class User {
	
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private GridPoint Target = new GridPoint(11, 11);
	
	
	public User (ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space=space;
		this.grid=grid;	
	}

	@ScheduledMethod(start=1, interval=1, priority = 0)
	public void step () {
		// get the grid location of the agent
		//GridPoint pt = grid.getLocation (this);	
		moveTowards(Target);		
	}

	public double getX() {
		return space.getLocation(this).getX();
	}
	public double getY() {
		return space.getLocation(this).getY();
	}
	
	public void moveTowards(GridPoint pt) {
		// only move if we are not already in this grid location
		if (!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint,
					otherPoint);
			space.moveByVector(this, 1, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
		}
	}
}
