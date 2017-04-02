package sorting;

import routeObjects.RouteADT;

import java.util.ArrayList;

/**
 * this sorting algorithm comes from the ALgorithm 4the edition book which applies a merge sort to our data
 * according to the safety rank
 * @author Daniel Brunton, Ben Giller, Matt Kaczmarek, Jiacheng Yang, Ray Winardi
 *
 */
public class Mergesort {
    private static ArrayList<RouteADT>  aux;
    private static void merge(ArrayList<RouteADT> a, int lo, int mid, int hi){
        int i = lo, j = mid + 1;

        for (int k = lo; k < hi; k++) {
            aux.set(k,a.get(k));
        }

        for (int k = lo; k <= hi; k++){
            if (i > mid)  a.set(k, aux.get(j++));
            else if (j > hi)  a.set(k, aux.get(i++));
            else if (less(aux.get(j), aux.get(i)))  a.set(k,aux.get(j++));
            else a.set(k,aux.get(i++));
        }
    }

    public static void sort(ArrayList a){
        aux = new ArrayList<RouteADT>(a.size());
        sort(a, 0, a.size()-1);
    }

    private static void sort(ArrayList<RouteADT> a, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo)/2;
        sort(a, lo, mid);
        sort(a, mid+1, hi);
        merge(a, lo,mid,hi);
    }

    private static boolean less(RouteADT a, RouteADT b) {
        if (a.getAirline().getSafetyRank() >= b.getAirline().getSafetyRank()) {
            return false;
        }
        return true;
    }
}
