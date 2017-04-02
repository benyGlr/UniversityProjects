package borrowedCode;

/**
 * An DFS algorithm to process the data
 * @author Daniel Brunton, Ben Giller, Matt Kaczmarek, Jiacheng Yang, Ray Winardi
 *
 */
import java.util.ArrayList;

import database.fileToArrayList;
import edu.princeton.cs.algs4.Stack;
import routeObjects.AirportADT;

public class AirportDepthFirstDirectedPaths {
	private boolean[] marked;  // marked[v] = true if v is reachable from s
    private int[] edgeTo;      // edgeTo[v] = last edge on path from s to v
    private final AirportADT s;       // source vertex
    private AirportDigraph Gr;
    ArrayList<AirportADT[]> paths= new ArrayList<AirportADT[]>();
    
    /**
     * The constructor which begins the DFS
     * @param G the input graph
     * @param s the given start
     */
    public AirportDepthFirstDirectedPaths(AirportDigraph G, AirportADT s) {
    	this.Gr = G;
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        dfs(G, s.getAirportID());
    }
    
    /**
     * recursively do the search
     * @param G	the input graph
     * @param v the input start
     */
    private void dfs(AirportDigraph G, int v) { 
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
        }
    }
    
    /**
     * To see if a vertice has things connected to it or not
     * @param v the given vertice
     * @return boolean representing judgment
     */
    public boolean hasPathTo(AirportADT v) {
        return marked[v.getAirportID()];
    }
    
    /**
     * return a certain path
     * @param v the given vertice
     * @return the paths
     */
    public Iterable<AirportADT> pathTo(AirportADT v) {
        if (!hasPathTo(v)) return null;
        Stack<AirportADT> path = new Stack<AirportADT>();
        for (int x = v.getAirportID(); x != s.getAirportID(); x = edgeTo[x])
            path.push(fileToArrayList.getAirport(x));
        path.push(s);
        return path;
    }
    
    /**
     * return all the paths to a given dest
     * @param f the given dest
     * @param t given number of transfers
     * @return	the paths
     */
    public ArrayList<AirportADT[]> allPathsTo(AirportADT f, int t){
    	AirportADT[] cpath = new AirportADT[t + 1];
    	for(int i = 0; i < cpath.length; i++){
    		cpath[i] = fileToArrayList.nullAirport;
    	}
    	cpath[0] = s;
    	allPathsToR(s, f, 1, t + 1,cpath);
    	return paths;
    	
    }
    
    private void allPathsToR(AirportADT v, AirportADT f, int c, int t, AirportADT[] cpath){
    	for(Integer a : Gr.adj(v.getAirportID())){
    		//System.out.println(c);
    		if (c < t - 1 && fileToArrayList.getAirport(a) != f){	
    			cpath[c] = fileToArrayList.getAirport(a);
    			/*for(AirportADT test : cpath){
    				System.out.println(test.getAirportID());
    			}
    			System.out.println("");*/
    			allPathsToR(fileToArrayList.getAirport(a), f, c+1, t, cpath.clone());
    		}
    		else if (fileToArrayList.getAirport(a) == f ){
    			cpath[c] = fileToArrayList.getAirport(a);
    			//System.out.println(cpath[c-1].getAirportID());
    			paths.add(cpath.clone());
    			if(c != 0)
    				break;
    		}
       	}
    }
}
