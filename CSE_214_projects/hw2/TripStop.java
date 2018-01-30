package hw2;

/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 2
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 */

/**
 * This class holds some information including a location, activity, and distance
 * @author Haiyang Liu
 *
 */
public class TripStop implements Cloneable
{
	/**
	 * The location of the trip stop
	 */
	private String location;
	
	/**
	 * The distance of the trip stop
	 */
	private int distance;
	
	/**
	 * The activity of the trip stop
	 */
	private String activity;
	
	/**
	 * Constructor with no param
	 */
	public TripStop(){}
	
	/**
	 * Constructor to initialize object with values
	 * @param location  the string for the location value
	 * @param activity	the string for the activity value
	 * @param distance	the int for the distance value
	 */
	public TripStop(String location, String activity, int distance)
	{
		this.location=location;
		this.distance=distance;
		this.activity=activity;
	}
	
	/**
	 * returns the location
	 * @return an string value
	 */
	public String getLocation() 
	{
		return location;
	}
	
	/**
	 * sets the location
	 * @param newLocation  a string value
	 */
	public void setLocation(String newLocation) 
	{
		this.location = newLocation;
	}
	
	/**
	 * return the distance
	 * @return an int value
	 */
	public int getDistance() 
	{
		return distance;
	}
	
	/**
	 * sets the distance
	 * @param newDistance an int value
	 * @throws IllegalArgumentException if the distance is negative
	 */
	public void setDistance(int newDistance) throws IllegalArgumentException 
	{
		if(newDistance<0)
			throw new IllegalArgumentException();
		this.distance = newDistance;
	}
	
	/**
	 * return the activity
	 * @return a string value
	 */
	
	public String getActivity() 
	{
		return activity;
	}
	
	/**
	 * sets the activity
	 * @param newActivity a string value
	 */
	public void setActivity(String newActivity) 
	{
		this.activity = newActivity;
	}
	
	/**
	 * Returns a string with all of the values of the fields of the objects inside
     * @return a formatted string of all the values
	 */
	@Override
	public String toString()
    {
		return String.format("%-21s%-26s%19s",this.getLocation(),this.getActivity(),this.getDistance());
    }
	
	/**
	 * performs a deep copy of this Object. Doesn't modify original.
     * @return a new TripStop object with the same values
	 */
	@Override
	public Object clone()
	{
		TripStop clone=new TripStop();
		clone.setActivity(this.getActivity());
		clone.setLocation(this.getLocation());
		clone.setDistance(this.getDistance());
		return clone;	
	}
}
