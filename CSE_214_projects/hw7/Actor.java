package hw7;
/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 7
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 **/

/**
 * This class hold information of Actor, including name and count
 *
 */
public class Actor {
	private String name;
	private int count;
	
	/**
	 * The constructor with one parameter 
	 * @param name, a String value
	 */
	public Actor(String name){
		this.name=name;
		this.count=1;
	}
	
	/**
	 * the getter method of name
	 * @return a String value
	 */
	public String getName() {
		return name;
	}

	/**
	 * The setter method of name
	 * @param name, a String value
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The getter method of count
	 * @return an int value
	 */
	public int getCount() {
		return count;
	}

	/**
	 * The setter method of count
	 * @param count, an int value
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * print the detailed information of actor 
	 * @return a String value
	 */
	public String actorPrint(){
		return String.format(("%-30s%-10s"), this.getName(), this.getCount());
	}
	
	
}
