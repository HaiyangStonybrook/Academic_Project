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
import java.util.HashMap;

/**
 * this class hold information of buildings in the campus
 *
 */
public class Campus implements Serializable{
	private HashMap<String,Building> campus;
	
	/**
	 * Constructor without param
	 */
	public Campus(){
		campus=new HashMap<String,Building>();
	}
	
	/**
	 * This method adds a Building into the Campus using the specified building name as the key
	 * @param buildingName, a String value
	 * @param building, a Building value
	 * @throws IllegalArgumentException if the given buildingName = null or if the building name is already in the Campus.
	 */
	public void addBuilding(String buildingName,Building building) throws IllegalArgumentException{
		if (buildingName==null)
			throw new IllegalArgumentException();
		campus.put(buildingName, building);
	}
	
	/**
	 * Retrieves the Building from the table having the indicated building name
	 * @param buildingName, a String value
	 * @return a Building value
	 */
	public Building getBuilding(String buildingName){
		if(buildingName==null)
			return null;
		return campus.get(buildingName);
	}
	
	/**
	 * This method removes a Building from the Campus
	 * @param buildingName, a String value
	 * @throws IllegalArgumentException if the given buildingName = null or if the building name is not in the Campus.
	 */
	public void removeBuilding(String buildingName)throws IllegalArgumentException{
		if(buildingName==null)
			throw new IllegalArgumentException();
		campus.remove(buildingName);
			
	}
	
	/**
	 * check if the campus contains a certain building name
	 * @param buildingName, a String value
	 * @return a boolean value
	 */
	public boolean containsKey(String buildingName){
		return (campus.containsKey(buildingName));
	}
	
	/**
	 * print all buildings which have white board
	 */
	public void whiteboardBuilding(){
		for(String key:campus.keySet()){
			System.out.println("\t"+key+": "+campus.get(key).whiteboardClassroom());
		}
	}
	
	/**
	 * print all buildings which have black board
	 */
	public void blackboardBuilding(){
		for(String key:campus.keySet()){
			System.out.println("\t"+key+": "+campus.get(key).blackboardClassroom());
		}
	}
	
	/**
	 * print all buildings which have certain AV equipment
	 * @param equipment
	 */
	public void avBuilding(String equipment){
		for(String key:campus.keySet()){
			System.out.println("\t"+key+": "+campus.get(key).avClassroom(equipment));
		}
	}
	
	/**
	 * list all buildings' classroom number
	 */
	public void allBuildingRoom(){
		for(String key:campus.keySet()){
			System.out.println("\t"+key+": "+campus.get(key).allRoom());
		}
	}
	
}
