package hw6;
/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 6
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * This class hold information of classroom, 
 * include hasWhiteboard, hasChalkboard, numSeats, AVEquipmentList
 *
 */
public class Classroom implements Serializable{
	private boolean hasWhiteboard;
	private boolean hasChalkboard;
	private int numSeats;
	private List<String> AVEquipmentList;
	
	/**
	 * Constructor without param
	 */
	public Classroom(){
		hasWhiteboard=false;
		hasChalkboard=false;
		numSeats=0;
		AVEquipmentList=null;	
	}
	
	/**
	 * return true if classroom has white board
	 * @return a boolean value
	 */
	public boolean isHasWhiteboard() {
		return hasWhiteboard;
	}
	
	/**
	 * set true/false of the hasWhiteboard value
	 * @param hasWhiteboard, a boolean value
	 */
	public void setHasWhiteboard(boolean hasWhiteboard) {
		this.hasWhiteboard = hasWhiteboard;
	}
	
	/**
	 * return true if classroom has chalk board
	 * @return a boolean value
	 */
	public boolean isHasChalkboard() {
		return hasChalkboard;
	}
	
	/**
	 * set true/false of the hasChalkboard value
	 * @param hasChalkboard, a boolean value
	 */
	public void setHasChalkboard(boolean hasChalkboard) {
		this.hasChalkboard = hasChalkboard;
	}
	
	/**
	 * return the number of the seats
	 * @return a int value
	 */
	public int getNumSeats() {
		return numSeats;
	}
	
	/**
	 * set the number of the seats
	 * @param numSeats, a int value
	 * @throws IllegalArgumentException if the numSeats is negative
	 */
	public void setNumSeats(int numSeats)throws IllegalArgumentException {
		if(numSeats<0)
			throw new IllegalArgumentException();
		this.numSeats = numSeats;
	}
	
	/**
	 * return the AV equipments of the class
	 * @return a List<String> value
	 */
	public List<String> getAVEquipmentList() {
		return AVEquipmentList;
	}

	/**
	 * set the AV equipments of the class
	 * @param aVEquipmentList, a List<String> value
	 */
	public void setAVEquipmentList(List<String> aVEquipmentList) {
		AVEquipmentList = aVEquipmentList;
	}
	
	/**
	 * print the detail information of the class
	 * @return a string value
	 */
	public String printDetail(){
		String str="Room Details: \n";
		String str1="";
		String str2="";
		String str3="";
		String str4="";
		str3="\tSeats: "+this.getNumSeats()+"\n";
		if(this.hasWhiteboard)
			str1="\tHas Whiteboard\n ";
		if(!this.hasWhiteboard)
			str1="\tDoesn't have Whiteboard\n";
		if(this.hasChalkboard)
			str2="\tHas Chalkboard\n ";
		if(!this.hasChalkboard)
			str2="\tDoen't have Chalkboard";
		str4="\tAV Amenities: "+this.getAVEquipmentList().toString()+"\n";
		str=str+str3+str1+str2+str4;
		return str;
	}
}
