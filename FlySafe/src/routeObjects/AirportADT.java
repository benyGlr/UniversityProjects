package routeObjects;

/**
 * An airportADT that holds the data for an airport
 * @author Daniel Brunton, Ben Giller, Matt Kaczmarek, Jiacheng Yang, Ray Winardi
 *
 */
public class AirportADT implements Comparable<AirportADT>{

	int airportID;
	String airportCode;
	String name;
	String city;
	String country;
	float longitude;
	float latitude;
	
	/**
	 * The airportADT constructor
	 * @param airportID a 3 letter airport code
	 * @param city a city name for the airport
	 * @param country a country for the airport
	 */
	public AirportADT(int airportID, String name, String city, String country, float longitude, float latitude,String ac){
		this.airportID = airportID;
		this.airportCode = ac;
		this.name = name;
		this.city = city;
		this.country = country;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	/**
	 * Return the airport's code
	 * @return return the airport's code
	 */
	public int getAirportID() {
		return this.airportID;
	}
	
	/**
	 * Set a new airport code for the airport
	 * @param airportID the new airport code
	 */
	public void setAirportID(int airportID) {
		this.airportID = airportID;
	}
	
	public void setAirportCode(String ac){
		this.airportCode = ac;
	}
	
	public String getAirportCode(){
		return this.airportCode;
	}
	
	/**
	 * Return the airport name
	 * @return airport name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Update the name of the airport
	 * @param name The updated name for the airport
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Return the airport's city
	 * @return return the airport's city
	 */
	public String getCity() {
		return this.city;
	}
	
	/**
	 * Set a new city for the airport
	 * @param city the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * Return the airport's country
	 * @return return the airport's country
	 */
	public String getCountry() {
		return this.city;
	}
	
	/**
	 * Set a new country for the airport
	 * @param country the new country
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
	 * Return the longitude of the airport
	 * @return return the longitude
	 */
	public float getLongitude() {
		return this.longitude;
	}
	
	/**
	 * Set a new longitude for the airport
	 * @param longitude the new longitude
	 */
	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * Return the latitude of the airport
	 * @return return the latitude
	 */
	public float getLatitude() {
		return this.latitude;
	}
	
	/**
	 * Set a new latitude for the airport
	 * @param latitude the new latitude
	 */
	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}
	
	public int compareTo(AirportADT a) {
		if (this.getAirportID() > a.getAirportID()) return 1;
		if (this.getAirportID() == a.getAirportID()) return 0;
		return -1;
	}


	
}
