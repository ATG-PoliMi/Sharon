package repastsimphony.agent;

import java.util.ArrayList;
import java.util.List;

import dijsktra.Dijkstra;
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
	private Dijkstra DE;
	int[][] worldMapMatrix;
	
	
	public Actor (ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space=space;
		this.grid=grid;	
	}

	@ScheduledMethod(start = 1, interval = 1, priority=0)
	public void step() {	
		GridPoint pt = grid.getLocation(this);		
	}
	
	@ScheduledMethod(start = 1, interval = 50, priority=1)
	public void newTarget() {
		//DijkstraEngine DE = new DijkstraEngine();
		
		worldMapMatrix = Map.getInstance().getWorldMap();
		//DE.buildAdjacencyMatrix(worldMapMatrix);
//		System.out.println("INVIO!!"+(int)grid.getLocation(this).getX() + ","+ (int)grid.getLocation(this).getY());
		
		// Start point:
		//DE.setInitial((int)grid.getLocation(this).getX() + ","+ (int)grid.getLocation(this).getY());
		//DE.setInitial("5,5");
		// End point:
		//ArrayList<String> path = DE.computePath(Target.getX()+","+Target.getY());
		//System.out.println("PATH:"+path);
		
	}

	
	
}