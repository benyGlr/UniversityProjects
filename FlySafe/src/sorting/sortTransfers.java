package sorting;

import java.util.ArrayList;

import borrowedCode.AirportDepthFirstDirectedPaths;
import borrowedCode.AirportDigraph;
import database.fileToArrayList;
import edu.princeton.cs.algs4.Stack;
import routeObjects.AirportADT;
import routeObjects.RouteADT;
 /**
  * 
  * @author Ben Giller
  *
  */
public class sortTransfers {
	/*
	 * How:
	 * the allPathsTo method for depthfirstdirectedpaths
	 * with return a ArrayList of paths that are in the form of 
	 * an iterable of airports, go through each airport
	 * and for each airport : i and the airport after it : i + 1 
	 * find the ALL routes that make that trip 
	 * 
	 * make an arrayList of arrayLists and put each route in the spot 
	 * with every other route that makes that trip then put each permutation
	 * of routes you can take into an arrayList of arrayLists of FINAL paths
	 * (god bless)
	 */
	
	private static ArrayList<RouteADT> routes;
	private static AirportDigraph transfers;
	
	private static void makeGraph(){
		for (int i = 0; i < routes.size(); i++){
			transfers.addEdge(routes.get(i));
		}
	}
	
	public static void graph_init(){
		routes = fileToArrayList.getRoutes();
		transfers = new AirportDigraph(routes.size());
	}
	
	public static ArrayList<RouteADT> getPath(AirportADT s, AirportADT d){
		makeGraph();
		AirportDepthFirstDirectedPaths p = new AirportDepthFirstDirectedPaths(transfers, s);
		Iterable<AirportADT> AirportP =  p.pathTo(d);
		ArrayList<AirportADT> AirportL = new ArrayList<AirportADT>();
		ArrayList<RouteADT> RouteP = new ArrayList<RouteADT>();
		for (AirportADT i : AirportP){
			AirportL.add(i);
		}
		for (int i = 0; i < AirportL.size() - 1; i++){
			RouteP.add(fileToArrayList.getRoute(AirportL.get(i), AirportL.get(i+1)));
		}
		
		return RouteP;
	}
	
	public static ArrayList<AirportADT[]> getPaths(AirportADT s, AirportADT d, int t){
		makeGraph();
		AirportDepthFirstDirectedPaths p = new AirportDepthFirstDirectedPaths(transfers, s);
		ArrayList<AirportADT[]> paths = p.allPathsTo(d, t);
		
		/*for(int i : transfers.adj(1)){
			System.out.println(i);
		}*/
		
		for(int i = 0; i < paths.size() - 1; i++){
			if (paths.get(i).equals(paths.get(i+1))){
				paths.remove(i);
				i--;
			}
    	}
		
		for(int i = 0; i < paths.size() - 1; i++){
			for (int y = i + 1; y < paths.size() - 1; y++){
				if (paths.get(i).equals(paths.get(y))){
					paths.remove(y);
					i--;
				}
			}
    	}
		
		
		
		return paths;
	}
	
	/*public static void main(String[] args){
		ArrayList<RouteADT> p = getPath(fileToArrayList.getAirport(1), fileToArrayList.getAirport(2));
		for (RouteADT r : p){
			System.out.print(r.getSource().getName() + " " + r.getDestination().getName() + ", ");
			if (r.getDestination().getName() == "DNE")
				break;
		}
		graph_init();
		for(AirportADT[] i : getPaths(fileToArrayList.getAirport(1), fileToArrayList.getAirport(2), 2)){
    		for (AirportADT a : i)
    			System.out.println(a.getAirportID());
    		System.out.println("");
    	}
		
	}*/

}
