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
 * This class holds the doubly linked list information including a data, next, and prev.
 * @author Haiyang Liu
 *
 */
public class TripStopNode {
	/**
	 * the data of current position 
	 */
	private TripStop data;
	/**
	 * The reference of next node 
	 */
	private TripStopNode next;
	
	/**
	 * the reference of previous node
	 */
	private TripStopNode prev;
	
	/**
	 *  Constructor with no param
	 */
	public TripStopNode(){}
	
	/**
	 *  Constructor to initialize object with values
	 * @param initData the TripStop for the data value
	 * @throws IllegalArgumentException if the param is null
	 */
	public TripStopNode(TripStop initData) throws IllegalArgumentException
	{	
		if(initData==null)
			throw new IllegalArgumentException();
		 this.data=initData;
		 this.next=null;
		 this.prev=null;
	}
	
	/**
	 * Returns the reference to the data member variable of the list node
	 * @return a TripStop value
	 */
	public TripStop getData()
	{
		return data;
	}
	
	/**
	 * Sets the data private field to the one passed as a parameter
	 * @param newData a TripStop value
	 * @throws IllegalArgumentException if param is null.
	 */
	public void setData(TripStop newData) throws IllegalArgumentException
	{
		if(newData==null)
			throw new IllegalArgumentException();
		data=newData;
	}
	
	/**
	 *Returns the reference to the next member variable of the list node. Can be null, means there's no next TripStopNode.
	 * @return a TripStopNode value
	 */
	public TripStopNode getNext()
	{
		return next;
	}
	
	/**
	 * Updates the next member variable with a new TripStopNode reference.
	 * @param newNext a TripStopNode value
	 */
	public void setNext(TripStopNode newNext)
	{
		next=newNext;
	}
	
	/**
	 * Gets the reference to the prev member variable of the list node.
	 * @return a TripStopNode value
	 */
	public TripStopNode getPrev()
	{
		return prev;
	}
	
	/**
	 * Updates the prev member variable with a new TripStopNode reference.
	 * @param newPrev a TripStopNode value
	 */
	public void setPrev(TripStopNode newPrev)
	{
		prev=newPrev;
	}
	
	

}
