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
import java.util.HashMap;
import java.util.List;

/**
 * this class hold classroom information of a building using hashMap
 *
 */
public class Building implements Serializable{
	private HashMap<Integer,Classroom> building;
	
	/**
	 * Constructor without param
	 */
	public Building(){
		building=new HashMap<Integer,Classroom>();
	}
	
	/**
	 * This method adds a Classroom into the Building using the specified room number as the key
	 * @param roomNumber, an Integer value
	 * @param classroom, a Classroom value
	 * @throws IllegalArgumentException if roomNumber is null
	 */
	public void addClassroom(Integer roomNumber,Classroom classroom) throws IllegalArgumentException{
		if (roomNumber==null)
			throw new IllegalArgumentException();
		building.put(roomNumber, classroom);
	}
	
	/**
	 * Retrieves the Classroom from the table having the indicated room number.
	 * @param roomNumber, an Integer value
	 * @return a Classroom value
	 */
	public Classroom getClassroom(Integer roomNumber){
		if(building.get(roomNumber)==null)
			return null;
		return building.get(roomNumber);
	}
	
	/**
	 * This method removes a classroom from the Building
	 * @param roomNumber, an Integer value
	 * @throws IllegalArgumentException if the given roomNumber = null or if it doesn't exist in the building
	 */
	public void removeClassroom(Integer roomNumber)throws IllegalArgumentException{
		if(roomNumber==null)
			throw new IllegalArgumentException();
		building.remove(roomNumber);
			
	}
	
	/**
	 * check if the building contains a certain classroom number
	 * @param roomNumber, an Integer value
	 * @return a boolean value
	 */
	public boolean containsKey(Integer roomNumber){
		return (building.containsKey(roomNumber));
	}
	
	/**
	 * return all classrooms which have white board 
	 * @return a String value
	 */
	public String whiteboardClassroom(){
		String str="";
		for(Integer key:building.keySet()){
			if(building.get(key).isHasWhiteboard())
				str+=key.toString()+"  ";
		}
		return str;
	}
	
	/**
	 * return all classrooms which have Black board 
	 * @return a String value
	 */
	public String blackboardClassroom(){
		String str="";
		for(Integer key:building.keySet()){
			if(building.get(key).isHasChalkboard())
				str+=key.toString()+" ";
		}
		return str;
	}
	
	/**
	 * return all classrooms which have certain AV equipment
	 * @param equipment ,a String value 
	 * @return a String value
	 */
	public String avClassroom(String equipment){
		String str="";
		for(Integer key: building.keySet()){
			if(building.get(key).getAVEquipmentList().contains(equipment))
				str+=key.toString()+" ";
		}
		return str;
	}
	
	/**
	 * List all classroom number
	 * @return a String value
	 */
	public String allRoom(){
		String str="";
		for(Integer key: building.keySet()){
			str+=key.toString()+" ";
		}
		return str;
	}
	
	/**
	 * List the building summary
	 * @return a String value
	 */
	public String buildingList(){
		String total="Details: \n";
		String room="\tRooms: ";
		String roomNum="";
		String avEquipment="";
		int seat=0;
		int hasW=0;
		int hasB=0;
		List<String> av=new ArrayList<String>();
		for(Integer key:building.keySet()){
			String liststr="";
			roomNum=roomNum+key+" ";
			seat+=building.get(key).getNumSeats();
			if(building.get(key).isHasWhiteboard()){
				hasW++;
			}
			if(building.get(key).isHasChalkboard()){
				hasB++;
			}
			for(String s:building.get(key).getAVEquipmentList()){
				if(!av.contains(s)){
					av.add(s);
					avEquipment+=s+" ";	
				}
			}
		}
		room+=roomNum;
		int size=building.size();
		double wPersent=(double)hasW/(double)building.size()*100.0;
		double bPersent=(double)hasB/(double)building.size()*100.0;
		total=room+"\n"+"\tTotal seats: "+seat+"\n"
			 +"\t"+wPersent+"% of rooms have whiteboards"+"\n"
			 +"\t"+bPersent+"% of rooms have blackboards"+"\n"
			 +"\tAV Equipment present: "+avEquipment;
		return total;
	}
}
