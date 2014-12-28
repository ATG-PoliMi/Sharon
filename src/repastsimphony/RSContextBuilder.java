package repastsimphony;



import java.util.ArrayList;

import behavior.simulator.extractor.ADL;
import behavior.simulator.planner.ADLMatcher;
import behavior.simulator.planner.LowLevelADL;
import behavior.simulator.xml.ADLDB;
import behavior.simulator.xml.ADLMatcherDB;
import behavior.simulator.xml.LLADLDB;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.valueLayer.GridValueLayer;
import repastsimphony.agent.Actor;
import repastsimphony.agent.Wall;
import utils.Constants;
import repastsimphony.common.HomeMap;



public class RSContextBuilder implements ContextBuilder<Object> {

	//Repast Simphony
	private int worldMap[][];
	private ContinuousSpace<Object> space;
	private ContinuousSpaceFactory spaceFactory;
	private GridFactory gridFactory;
	private Grid<Object> grid;
	private GridValueLayer structureLayer;
	
	//Extractor
	ArrayList<ADL> adl;

	@SuppressWarnings("rawtypes")
	@Override
	public Context build(Context<Object> context) {
		context.setId("BehaviourSimulator");

		continuousSpaceDeclaration (context);
		gridDeclaration(context);
		cellsCreation(context);
		
		if (RunEnvironment.getInstance().isBatch()) {
			RunEnvironment.getInstance().endAt(20);
		}
		return context;
	}

	private void continuousSpaceDeclaration (Context<Object> context){
		spaceFactory = ContinuousSpaceFactoryFinder
				.createContinuousSpaceFactory(null);

		space = spaceFactory.createContinuousSpace(
				"space", context, new RandomCartesianAdder<Object>(),
				new repast.simphony.space.continuous.WrapAroundBorders(), Constants.mapSizeW,
				Constants.mapSizeH);
	}

	private void gridDeclaration (Context<Object> context) {
		//Grid declaration
		gridFactory = GridFactoryFinder.createGridFactory(null);
		grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(), true, Constants.mapSizeW, Constants.mapSizeH));
		context.add(new Actor(space, grid));
		
		// Create a background layer for the displayed grid
		structureLayer = new GridValueLayer(
				Constants.STRUCTURE_LAYER_ID, // Access layer through context
				true, // Densely populated
				new WrapAroundBorders(), // Toric world
				// Size of the grid (defined constants)
				Constants.mapSizeW, Constants.mapSizeH);
		context.addValueLayer(structureLayer);
	}	
	
	private void cellsCreation(Context<Object> context) {
		// Fill up the context with cells, and set the initial values for
		// the new layer. Also add them to the created grid.
		
		//Retrieving map
		worldMap = HomeMap.getInstance().getWorldMap();
		
		for (int i = 0; i < Constants.mapSizeW; i++) {
			for (int j = 0; j < Constants.mapSizeH; j++) {
				final Wall cell = new Wall(i, j);
				context.add(cell); // First add it to the context
				grid.moveTo(cell, i, j);
				structureLayer.set(worldMap[i][j], i, j);
			}
		}
		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
		}
	}	
}