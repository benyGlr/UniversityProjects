package sorting;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import database.fileToArrayList;
import routeObjects.AirportADT;
import routeObjects.PathADT;

public class getPaths {

	private static ArrayList<PathADT> listRoutes(AirportADT s, AirportADT d, int t){
		
		ArrayList<AirportADT[]> airportRoutes = sortTransfers.getPaths(s, d, t);
		ArrayList<PathADT> ret = new ArrayList<PathADT>();
		
		for(AirportADT[] e : airportRoutes){
			ret.addAll(Pathfinder.findPossiblePaths(Pathfinder.listPossibleRoutes(e)));
		}
		ret = sortSafetyRank.sortPaths(ret);
		
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
	//DME SVX 3
	public static void main(String[] args){
		fileToArrayList.init();
		sortTransfers.graph_init();
		Scanner scanner = new Scanner(System.in);
		System.out.println("input source");
		String source = scanner.next();
		System.out.println("input dest");
		String dest = scanner.next();
		System.out.println("input transfers");
		int tran = Integer.parseInt(scanner.next());
		writeToFile(displayRoutes(listRoutes(fileToArrayList.getAirport("\"" + source + "\""), fileToArrayList.getAirport("\"" + dest + "\""), tran)));
		System.out.println("Done");
	}
}
