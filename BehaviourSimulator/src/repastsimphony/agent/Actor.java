package repastsimphony.agent;

import java.util.ArrayList;
import java.util.List;

import dijsktra.DijkstraEngine;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;
import repastsimphony.common.Map;

public class Actor {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private GridPoint Target = new GridPoint(15, 25);
	private DijkstraEngine DE;
	int[][] worldMapMatrix;
	ArrayList<String> path;
	String delims = ",";


	public Actor (ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space=space;
		this.grid=grid;	
	}
	
//TODO: Cleanup method step()
	@ScheduledMethod(start = 1, interval = 1, priority=0)
	public void step() {	
		GridPoint pt = grid.getLocation(this);

		if (path.size() > 0) {
			String x = path.get(0);
			path.remove(0);
			String[] tokens = x.split(delims);

			GridPoint pt2 = new GridPoint(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1]));
			moveTowards(pt2);
		}

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

	@ScheduledMethod(start = 1, interval = 60, priority=1)
	public void newTarget() {
		DE = new DijkstraEngine();		

		worldMapMatrix = Map.getInstance().getWorldMap();
		DE.buildAdjacencyMatrix(worldMapMatrix);

		System.out.println("INVIO!!"+(int)grid.getLocation(this).getX() + ","+ (int)grid.getLocation(this).getY());
		// Start point:
		DE.setInitial((int)grid.getLocation(this).getX() + ","+ (int)grid.getLocation(this).getY());

		// End point:
		path = DE.computePath(Target.getX()+","+Target.getY());
		System.out.println("PATH:"+path);
	}	
}