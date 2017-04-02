package routeObjects;

import java.util.Comparator;

/**
 * A Path ADT which stores many routes
 * @author Daniel Brunton, Ben Giller, Matt Kaczmarek, Jiacheng Yang, Ray Winardi
 *
 */

public class PathADT implements Comparable<PathADT> {
	private RouteADT[] list;
	
	/**
	 * This constructor stores an array into the variables list;
	 * @param a the input array you want to use
	 */
	public PathADT(RouteADT[] a){
		list = a;
	}
	
	/**
	 * This method return the routeADT at a given index in the array
	 * @param index the given index
	 * @return return the routeADT you want
	 */
	public RouteADT getVal(int index){
		return list[index];
	}
	
	public int size() {
		return list.length;
	}
	
	/**
	 * Gets a safety rank for the path (equivalent to that of the least safe route in the path)
	 * @return returns the safety rank for the path
	 */
	public int getSafetyRank() {
		int worst = 0;
		for (int i = 0; i < this.size(); i++) {
			if (this.getVal(i).getAirline().getSafetyRank() > worst) {
				worst = this.getVal(i).getAirline().getSafetyRank();
			}
		}
		return worst;
	}
	
	/**
	 * Override the toString method to reasonably return the string
	 * which describes what you have in the array of routeADT
	 */
	public String toString(){
		String x = "";
		for (int i = 0; i < list.length; i ++){
			String y ="the source of the " + i + " route is" + list[i].getSource().getName()
					+" and The destination is" + list[i].getDestination().getName() + 
					" and the airline is " + list[i].getAirline().getName() + "\n";
			x += y;
		}
		return x;
	}
	
	/**
	 * CompareTo for Path objects
	 * @param p Path being compared against
	 * @return 1 if p1 > p2, 0 if p1 = p2, -1 if p1 < p2
	 */
	public int compareTo(PathADT p) {
		int worst1 = 0;
		int worst2 = 0;
		for (int i = 0; i < this.size(); i++) {
			if (this.getVal(i).getAirline().getSafetyRank() > worst1) {
				worst1 = this.getVal(i).getAirline().getSafetyRank();
			}
		}
		for (int i = 0; i < p.size(); i++) {
			if(p.getVal(i).getAirline().getSafetyRank() > worst2) {
				worst2 = p.getVal(i).getAirline().getSafetyRank();
			}
		}
		if (worst1 > worst2) return 1;
		if (worst1 == worst2) return 0;
		return -1;
	}
}
