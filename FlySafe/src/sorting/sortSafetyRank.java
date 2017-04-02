package sorting;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.Merge;
import routeObjects.PathADT;

/**
 * Takes an arraylist of paths and returns an array of paths sorted by relative safety
 * @author Matt Kaczmarek
 *
 */
public class sortSafetyRank {
    public static ArrayList<PathADT> sortPaths(ArrayList<PathADT> paths) {
    	// Create an array copy of the arraylist parameter
    	PathADT[] result = paths.toArray(new PathADT[paths.size()]);
    	
    	// sorting
    	Merge.sort(result);
    	
    	ArrayList<PathADT> aResult = new ArrayList<PathADT>(Arrays.asList(result));
    	return aResult;
    }
}
