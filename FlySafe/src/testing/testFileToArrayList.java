package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import database.fileToArrayList;
import routeObjects.AirportADT;
import routeObjects.RouteADT;
import sorting.Pathfinder;
import sorting.sortTransfers;

public class testFileToArrayList {
	
	@Before
	public void setUp() throws Exception 
	{
		fileToArrayList.init();
		sortTransfers.graph_init();
	}
	
	@Test
	public void test1() {
		ArrayList<RouteADT> r = fileToArrayList.getRoutes();
		ArrayList<AirportADT[]> airportRoutes = sortTransfers.getPaths(fileToArrayList.getAirport(1), fileToArrayList.getAirport(2), 2);
		for (AirportADT[] e : airportRoutes){
			ArrayList<ArrayList<RouteADT>> a = Pathfinder.listPossibleRoutes(e);
			for(ArrayList<RouteADT> b : a)
				for(RouteADT c : b){
					assertTrue(r.contains(c));
				}
		}
		
	}
	
	@Test
	public void test2() {
		ArrayList<RouteADT> r = fileToArrayList.getRoutes();
		ArrayList<AirportADT[]> airportRoutes = sortTransfers.getPaths(fileToArrayList.getAirport("\"DME\""), fileToArrayList.getAirport("\"SVX\""), 3);
		for (AirportADT[] e : airportRoutes){
			ArrayList<ArrayList<RouteADT>> a = Pathfinder.listPossibleRoutes(e);
			for(ArrayList<RouteADT> b : a)
				for(RouteADT c : b){
					assertTrue(r.contains(c));
				}
		}
		
	}

}
