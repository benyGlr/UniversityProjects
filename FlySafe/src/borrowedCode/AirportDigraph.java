package borrowedCode;

/**
 * An algorithm which form the directed graph
 * @author Daniel Brunton, Ben Giller, Matt Kaczmarek, Jiacheng Yang, Ray Winardi
 *
 */

import edu.princeton.cs.algs4.Bag;
import routeObjects.RouteADT;

public class AirportDigraph {
	private final int V;
	private int E;
	private Bag<Integer>[] adj;
	
	/**
	 * the constructor which takes an int as the source
	 * @param V the given vertice
	 */
	public AirportDigraph(int V){
		this.V = V;
		this.E = 0;
		//System.out.println("size" + " " + V);
		adj = (Bag<Integer>[]) new Bag[V];
		
		for (int v = 0; v < V; v++)
			adj[v] = new Bag<Integer>();
		
		
	}
	
	/**
	 *calculate how many vertices
	 * @return number of vertices
	 */
	public int V() { return V; }
	
	/**
	 * calculate how many edges
	 * @return number of vertices
	 */
	public int E() { return E; }
	
	/**
	 * add adge to the graph
	 * @param r the given routeADT
	 */
	public void addEdge(RouteADT r){
		//System.out.println(r.getSource().getAirportID() + " " + r.getDestination().getAirportID());
		adj[r.getSource().getAirportID()].add(r.getDestination().getAirportID());
		E++;
	}
	
	/**
	 * stores all the adjacent vertices to a certain vertice in a vertain list
	 * @param v he given vertice
	 * @return return the list
	 */
	public Iterable<Integer> adj(Integer v) { return adj[v]; }
}

