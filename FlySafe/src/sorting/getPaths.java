package sorting;

import java.io.FileWriter;
import java.util.ArrayList;

import database.fileToArrayList;
import routeObjects.AirportADT;
import routeObjects.PathADT;

public class getPaths {

	private static ArrayList<PathADT> listRoutes(AirportADT s, AirportADT d, int t){
		
		ArrayList<AirportADT[]> airportRoutes = sortTransfers.getPaths(s, d, t);
		ArrayList<PathADT> ret = new ArrayList<PathADT>();
		/*for(AirportADT e : airportRoutes.get(3)){
			System.out.println(e.getAirportID());
		}
		ret = Pathfinder.findPossiblePaths(Pathfinder.listPossibleRoutes(airportRoutes.get(3)));*/
		
		for(AirportADT[] e : airportRoutes){
			ret.addAll(sortSafetyRank.sortPaths(Pathfinder.findPossiblePaths(Pathfinder.listPossibleRoutes(e))));
		}
		
		return ret;
	}
	
	private static ArrayList<String> displayRoutes(ArrayList<PathADT> in){
		ArrayList<String> output = new ArrayList<String>();
		for (PathADT p : in){
			output.add(p.toString() + "and the safety rank is " + p.getSafetyRank() + "\n");
		}
		
		
		return output;
	}
	
	private static void writeToFile(ArrayList<String> output){
		try{
			FileWriter f = new FileWriter("data/results_out.txt");
			for(String out: output){
				f.write(out + "\n");
			}
			f.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	} 
	//MAG GKA 2
	public static void main(String[] args){
		fileToArrayList.init();
		sortTransfers.graph_init();
		writeToFile(displayRoutes(listRoutes(fileToArrayList.getAirport("\"DME\""), fileToArrayList.getAirport("\"SVX\""), 3)));
	}
}
