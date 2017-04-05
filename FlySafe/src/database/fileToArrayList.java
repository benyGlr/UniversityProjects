/**
 * airlines:
 * 		[0] - ID, [1] - Name
 * airports:
 * 		[0] - ID, [1] - Name, [2] - City, [3] - Country, [4] - longitude, [5] - latitude
 * routes:
 * 		[1] airline id, [3] source airport id, [5] destination airport id
 */
/**
 * 
 */
package database;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import routeObjects.AirlineADT;
import routeObjects.AirportADT;
import routeObjects.RouteADT;
/**
 * this class takes all the input, and store them into the array lists
 * @author Daniel Brunton, Ben Giller, Matt Kaczmarek, Jiacheng Yang, Ray Winardi
 *
 */
public class fileToArrayList {
	private static ArrayList<AirportADT> airports = new ArrayList<AirportADT>();
	private static ArrayList<AirlineADT> airlines = new ArrayList<AirlineADT>();
	private static ArrayList<RouteADT> routes = new ArrayList<RouteADT>();
	public static AirportADT nullAirport = new AirportADT(0,"DNE","DNE","DNE",0,0,"DNE");
	public static AirlineADT nullAirline = new AirlineADT(0,"DNE",-1);
	
	private static void readInputAirports(){
		try{
			Scanner input = new Scanner(new File("data/airports.txt"));
			Scanner input2 = new Scanner(new File("data/airlines.txt"));
			while (input.hasNextLine()){
				String current = input.nextLine();
				//System.out.println(current);
				String[] entry = current.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				AirportADT temp = new AirportADT(Integer.parseInt(entry[0]), entry[1], entry[2], entry[3], Float.parseFloat(entry[6]), Float.parseFloat(entry[7]), entry[4]);
				airports.add(temp);
				if (input2.hasNextLine()){
					String current2 = input2.nextLine();
					String[] entry2 = current2.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					AirlineADT temp2 = new AirlineADT(0, entry2[1], Integer.parseInt(entry2[0]));
					airlines.add(temp2);
				}
				
			}
			if (airlines.size() > 0)
				airlines = Safety.calcAirlineSafety(airlines);
			input.close();
			input2.close();
			Collections.sort(airports);
		}catch(FileNotFoundException e){
			System.out.println("airport File not found");
		}
	}
	
	private static void readInputRoutes(){
		try{
			System.out.println("here");
			Scanner input = new Scanner(new File("data/routes.txt"));
			while (input.hasNext()){
				String current = input.nextLine();
				String[] entry = current.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				AirlineADT airline = null;
				AirportADT source = null;
				AirportADT dest = null;
				RouteADT temp;
				int low = 0;
				int high = airlines.size() - 1;
				while(low <= high) {
					int mid = (low + high)/2;
					if (entry[1].substring(1).equals("N")) {
						airline = nullAirline;
						break;
					}
						
					else if (Integer.parseInt(entry[1]) == airlines.get(mid).getAirlineID()){
						airline = airlines.get(mid);
						break;
					}
					if (airlines.get(mid).getAirlineID() > Integer.parseInt(entry[1]))
						high = mid - 1;
					else if(airlines.get(mid).getAirlineID() < Integer.parseInt(entry[1]))
						low = mid + 1;
				}
				if (airline == null){
					System.out.println(Integer.parseInt(entry[1]));
				}
				low = 0;
				high = airports.size() - 1;
				while(low <= high) {
					int mid = (low + high)/2;
					if (entry[3].substring(1).equals("N")){ 
						source = nullAirport;
						break;
					}
					else if (Integer.parseInt(entry[3]) == airports.get(mid).getAirportID()){
						source = airports.get(mid);
						break;
					}
					if (airports.get(mid).getAirportID() > Integer.parseInt(entry[3]))
						high = mid - 1;
					else if(airports.get(mid).getAirportID() < Integer.parseInt(entry[3]))
						low = mid + 1;
				}
				if (source == null){
					source = nullAirport;
				}
				
				low = 0;
				high = airports.size() - 1;
				while(low <= high) {
					int mid = (low + high)/2;
					if (entry[5].substring(1).equals("N")){
						dest = nullAirport;
						break;
					}
					
					else if (Integer.parseInt(entry[5]) == airports.get(mid).getAirportID()){
						dest = airports.get(mid);
						break;
					} 
					if (airports.get(mid).getAirportID() > Integer.parseInt(entry[5]))
						high = mid - 1;
					else if(airports.get(mid).getAirportID() < Integer.parseInt(entry[5]))
						low = mid + 1;
				}
				if (dest == null){
					dest = nullAirport;
				}
				temp = new RouteADT(source,dest,airline,0,0);
				routes.add(temp);
				
			}
		input.close();
		}catch(FileNotFoundException e){
			System.out.println("route File not found");
		}
	}
	/**
	 * get the airport of a certain int id
	 * @param ID the certain id
	 * @return the aiport you want to get
	 */
	public static AirportADT getAirport(String code){
		for (AirportADT a : airports){
			if (a.getAirportCode().equals(code)){
				return a;
			}
		}
		return nullAirport;
	}
	
	public static AirportADT getAirport(int id){
		for (AirportADT a : airports){
			if (a.getAirportID() == id){
				return a;
			}
		}
		return nullAirport;
	}
	/**
	 * the method which calls all the local methods to read all the inputs
	 * @return the arraylist of RouteADT that stores all the information
	 */
	
	public static void init(){
		readInputAirports();
		readInputRoutes();
	}
	public static ArrayList<RouteADT> getRoutes(){
		//sortByTransfers();
		return routes;
	}
	
	// only call this function after calling getRoutes (to update the routes)
	public static RouteADT getRoute(AirportADT s, AirportADT d){
		for (RouteADT r : routes){
			if (r.getSource() == s && r.getDestination() == d)
				return r;
		}
		return new RouteADT(nullAirport,nullAirport,nullAirline, -1, -1);
	}

	public static void main(String[] args){
		init();
	}

	
}
