package sorting;

import java.util.ArrayList;

import routeObjects.AirportADT;
import routeObjects.PathADT;
import routeObjects.RouteADT;
import database.fileToArrayList;

/**
 * Pathfinder class 
 * @author Matt Kaczmarek
 * Finds paths from one airport to another
 */
public class Pathfinder {
	// 1. given an array of airports, find all routes between every airport
	// 	e.g. if we have airports A, B, C
	// 	and if we have possible routes 1 and 2 between A and B
	// 	The arraylist will have an array of [1, 2] as element 0
	// 	So it is an arraylist of arrays of routes
	
	// 2. Find every permutation of possible paths
	
	// 3. Rank them based on safety ranking
	
	/**
	 * Returns all routes between two airports
	 * @param start starting airport
	 * @param end destination airport
	 * @return ArrayList of route objects between those two airports
	 */
	private static ArrayList<RouteADT> findRoutes(AirportADT start, AirportADT end) {
		ArrayList<RouteADT> allRoutes = fileToArrayList.getRoutes();
		ArrayList<RouteADT> routes = new ArrayList<RouteADT>();
		
		// Find all routes with the right source and destination airports
		for (RouteADT route : allRoutes) {
			if (route.getSource() == start & route.getDestination() == end)
				routes.add(route);
		}
		
		return routes;
	}
	
	/**
	 * Returns a List of every route between the airports in the List "stops"
	 * @param stops List of airports to find paths 
	 * @return
	 */
	public static ArrayList<ArrayList<RouteADT>> listPossibleRoutes(AirportADT[] stops) {
		ArrayList<ArrayList<RouteADT>> possibleRoutes = new ArrayList<ArrayList<RouteADT>>();
		
		// Add each list of routes to the list
		for (int i = 0; i < stops.length - 1; i++) {
			if(stops[i+1].getAirportID() != 0)
				possibleRoutes.add(findRoutes(stops[i], stops[i+1]));
			else
				break;
		}
		
		return possibleRoutes;
	}
	
	/**
	 * returns the List of all possible paths to get from one airport to another
	 * @param possibleRoutes List of possible legs of the path
	 * @return list of possible paths
	 */
	public static ArrayList<PathADT> findPossiblePaths(ArrayList<ArrayList<RouteADT>> possibleRoutes) {
		ArrayList<PathADT> possiblePaths = new ArrayList<PathADT>();
		
		// Find all possible combinations
		generatePermutations(possibleRoutes, possiblePaths, 0, new ArrayList<RouteADT>());
		
		return possiblePaths;
	}
	
	/**
	 * Generates all possible paths from a list of lists of routes
	 * @param lists the list of lists of routes that will be converted
	 * @param result the final result, which is added onto throughout the process
	 * @param depth the current position of the function in the list of lists
	 * @param current the permutation the function is building (not complete until it is added to the result arraylist)
	 */
	private static void generatePermutations(ArrayList<ArrayList<RouteADT>> lists, ArrayList<PathADT> result, int depth, ArrayList<RouteADT> current) {
		if (depth == lists.size()) {
			result.add(new PathADT(current.toArray(new RouteADT[current.size()])));
			return;
		}
		for (int i = 0; i < lists.get(depth).size(); i++) {
			ArrayList<RouteADT> temp = new ArrayList<RouteADT>(current);
			temp.add(lists.get(depth).get(i));
			generatePermutations(lists, result, depth + 1, temp);
		}
	}
}
