package repastsimphony.agent;

import java.util.ArrayList;
import java.util.List;
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

public class Actor {
	
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	
	
	public Actor (ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space=space;
		this.grid=grid;	
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
	
		GridPoint pt = grid.getLocation(this);		
	}

	
	
}