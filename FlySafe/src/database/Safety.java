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
				allLines.add(input.nextLine());
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
			String caseFreeName = airline.getName().toUpperCase();
			for (int l = 0; l < fileLength; l++) {
				int startIndex = allLines.get(l).indexOf(caseFreeName);
				while (startIndex != -1) {
					incidents++;
					startIndex = allLines.get(l).indexOf(caseFreeName, startIndex + caseFreeName.length());
				}
			}
			if (incidents > maxIncidents) {
				maxIncidents = incidents;
			}
		}
		
		// check every line in the input file for every airline
		for (AirlineADT airline : outputAirlines) {
			int incidents = 0;
			String caseFreeName = airline.getName().toUpperCase();
			for (int l = 0; l < fileLength; l++) {
				int startIndex = allLines.get(l).indexOf(caseFreeName);
				
				while (startIndex != -1) {
					System.out.println(startIndex);
					incidents++;
					startIndex = allLines.get(l).indexOf(caseFreeName, startIndex + caseFreeName.length());
				}
			}
			int safetyRank = (int)(((double)(incidents) / maxIncidents) * 100);
			airline.setSafetyRank(safetyRank);
		}
		return outputAirlines;
	}
}
