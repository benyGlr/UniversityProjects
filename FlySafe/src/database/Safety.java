package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import routeObjects.AirlineADT;
import routeObjects.RouteADT;
/**
 * this class calculates the safety rank of all the routes
 * @author Daniel Brunton, Ben Giller, Matt Kaczmarek, Jiacheng Yang, Ray Winardi
 *
 */
public class Safety {
	public static ArrayList<AirlineADT> calcAirlineSafety(ArrayList<AirlineADT> inputAirlines) {
		ArrayList<AirlineADT> outputAirlines = inputAirlines;
		ArrayList<String> allLines = new ArrayList<String>();
		int fileLength = 0;

		// turn file into arrayList of lines of text
		try {
			Scanner input = new Scanner(new File("data/AviationDataEnd2016UP.csv"));
			while (input.hasNextLine()) {
				String current = input.nextLine();
				current = current.split(",")[23];
				allLines.add(current);
				fileLength++;
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("safety File not found");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		// find the worst airline
		int maxIncidents = 0;
		for (AirlineADT airline : outputAirlines) {
			int incidents = 0;
			String caseFreeName = airline.getName().toUpperCase().trim();
			caseFreeName = caseFreeName.substring(1,caseFreeName.length() - 1);
			for (int l = 0; l < fileLength; l++) {
				if(allLines.get(l).toUpperCase().trim().contains(caseFreeName)) {
					incidents++;
					//System.out.println(incidents);
				}
			}
			if (incidents > maxIncidents) {
				maxIncidents = incidents;
			}
		}
		
		// check every line in the input file for every airline
		for (AirlineADT airline : outputAirlines) {
			int incidents = 0;
			String caseFreeName = airline.getName().toUpperCase().trim();
			caseFreeName = caseFreeName.substring(1,caseFreeName.length() - 1);
			for (int l = 0; l < fileLength; l++) {
				if((allLines.get(l).toUpperCase().trim().contains(caseFreeName)))
				{
					incidents++;
				}
			}
			
			int safetyRank = (int)(((double)(incidents) / maxIncidents) * 100);
			airline.setSafetyRank(safetyRank);
		}
		return outputAirlines;
	}
}
