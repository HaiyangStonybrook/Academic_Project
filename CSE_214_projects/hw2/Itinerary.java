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
 * Does stuff with an Itinerary of TripStopNode
 * @author Haiyang Liu
 *
 */
public class Itinerary implements Cloneable {
	/**
	 * The first node of the list
	 */
	private TripStopNode head;
	
	/**
	 * The last node of the list
	 */
	private TripStopNode tail;
	
	/**
	 * the current node of the list
	 */
	private TripStopNode cursor;
	
	/**
	 * the actual numbers of nodes of the list
	 */
	int size;
	
	/**
	 * the total distance of the itinerary
	 */
	int totalDistance;
	
	/**
	 * Constructor initializes an empty itinerary with no elements. Head, tail, and cursor are set to null.
	 */
	public Itinerary()
	{
		this.head=null;
		this.tail=null;
		this.cursor=null;	
		this.size=0;
		this.totalDistance=0;
	}
	
	/**
	 * get the reference of current cursor
	 * @return a TripStopNode value
	 */
	public TripStopNode getCursor()
	{
		return cursor;
	}
	
	/**
	 * get the actual numbers of trip stop
	 * @return an int value
	 */
	public int getStopsCount()
	{
		return size;
	}
	
	/**
	 * get the numbers of trip stop befor cursor
	 * @return an int value
	 */
	public int getCursorBeforeCount()
	{
		int before=0;
		TripStopNode nodeptr=head;
		while(nodeptr!=cursor)
		{
			before++;
			nodeptr=nodeptr.getNext();
		}
		return before;
	}
	
	/**
	 * get the numbers of trip stop after cursor 
	 * @return an int value
	 */
	public int getCursorAfterCount()
	{
		int after;
		after=size-this.getCursorBeforeCount()-1;
		return after;	
	}
	
	/**
	 * get the total distance of all trip stop
	 * @return an int value
	 */
	public int getTotalDistance()
	{
		return totalDistance;
	}
	
	/**
	 * get the information of cursor node
	 * @return a TripStop value
	 */
	public TripStop getCursorStop()
	{
		return cursor.getData();
	}
	
	/**
	 * reset the cursor to the head of the list
	 */
	public void resetCursorToHead()
	{
		if(head!=null)
			cursor=head;
		else
			{ 
				head=null;
				tail=null;
				cursor=null;
			}
	}
	
	/**
	 * move the cursor to the tail of the list
	 */
	public void moveToTail()
	{
		if(tail!=null)
			cursor=tail;
		else
			{ 
				head=null;
				tail=null;
				cursor=null;
			}
	}
	
	/**
	 * move the cursor to the next node
	 * @throws EndOfItineraryException if cursor is already at the tail position
	 */
	public void cursorForward() throws EndOfItineraryException
	{
		if(cursor==tail)
			throw new EndOfItineraryException();
		cursor=cursor.getNext();
	}
	
	/**
	 * Moves the cursor to select the previous TripStopNode in this list
	 * @throws EndOfItineraryException if cursor is at the head of the list
	 */
	public void cursorBackward() throws EndOfItineraryException
	{	
		if(cursor==head)
			throw new EndOfItineraryException();
		cursor=cursor.getPrev();	
	}
	
	/**
	 * Inserts the indicated TripStop before the cursor.
	 * when a trip stop was inserted, size increases by 1, total distance increases by this stop's distance 
	 * @param newStop a TripStop value
	 * @throws IllegalArgumentException if newStop is null
	 */
	public void insertBeforeCursor(TripStop newStop) throws IllegalArgumentException
	{
		TripStopNode newNode= new TripStopNode(newStop);
		if(newStop==null)
			throw new IllegalArgumentException();
		if(cursor==null)
			{
				head= newNode;
				tail= newNode;
				cursor=newNode;
			}
		else
			if(cursor==head)
			{
				newNode.setNext(head);
				cursor.setPrev(newNode);
				head=newNode;
			}
			else
			{
				newNode.setPrev(cursor.getPrev());
				newNode.setNext(cursor);
				cursor.setPrev(newNode);
				newNode.getPrev().setNext(newNode);
				if(cursor.getPrev()==null)
					head=cursor;
			}
		size++;
		totalDistance+=newStop.getDistance();
	}
	
	/**
	 * Inserts the indicated TripStop after the tail of the list.
	 * when a trip stop was inserted, size increases by 1, total distance increases by this stop's distance 
	 * @param newStop a TripStop value
	 * @throws IllegalArgumentException if newStop is null
	 */
	public void appendToTail(TripStop newStop) throws IllegalArgumentException
	{
		TripStopNode newNode= new TripStopNode(newStop);
		if(newStop==null)
			throw new IllegalArgumentException();
		if(tail!=null)
			{
				newNode.setPrev(tail);
				tail.setNext(newNode);
				tail=newNode;
			}
		else
		{
			head=newNode;
			tail=newNode;
			cursor=newNode;	
		}
		size++;
		totalDistance+=newStop.getDistance();		
	}
	
	/**
	 * Removes the TripStopNode referenced by cursor
	 * @return TripStop value. return the TripStop inside the node
	 * @throws EndOfListException if cursor is null
	 */
	public TripStop removeCursor() throws EndOfListException
	{	
		if(cursor==null)
			throw new EndOfListException();
		else{
			if(cursor.getPrev()==null&&cursor.getNext()==null)
			{
				size=0;
				totalDistance=0;
				TripStop temp=cursor.getData();
				head=null;
				tail=null;
				cursor=null;
				return temp;
			}
			else if(cursor.getPrev()==null)
			{	
				size--;
				totalDistance-=cursor.getData().getDistance();
				TripStop temp=cursor.getData();
				head=cursor.getNext();
				head.setPrev(null);
				cursor=cursor.getNext();
				return temp;
			}
			else if(cursor.getNext()==null)
			{	size--;
				totalDistance-=cursor.getData().getDistance();
				TripStop temp=cursor.getData();
				tail=cursor.getPrev();
				tail.setNext(null);
				cursor=tail;
				return temp;
			}
		
			else{
					size--;
					totalDistance-=cursor.getData().getDistance();
					TripStop temp=cursor.getData();
					cursor.getPrev().setNext(cursor.getNext());
					cursor.getNext().setPrev(cursor.getPrev());
					cursor=cursor.getNext();
					return temp;
				}
			}
	}
	
	/**
	 * Print the itinerary include every trip stop
	 */
	public void printItinerary()
	{
		if(size==0)
			System.out.println("This itinerary is empty");
		else{
		TripStopNode nodeptr=this.head;
		while(nodeptr!=null)
			{	
				if(nodeptr==cursor)
					System.out.println(String.format( ">"+nodeptr.getData().toString()+" miles\n"));
				else
					System.out.println( String.format(nodeptr.getData().toString()+" miles\n"));
				nodeptr=nodeptr.getNext();
			
			}
			System.out.println("This trip has "+this.getStopsCount()+" stops, totaling "+this.getTotalDistance()+" miles. There are "+this.getCursorBeforeCount()+" stops before the cursor and "+this.getCursorAfterCount()+" stops after the cursor.\n ");
		}
	}
	
	/**
	 * performs a deep copy of this itinerary. Doesn't modify original.
	 * @param source a Itinerary value
	 * @return a Itinerary value
	 */
	public static Itinerary listCopy( Itinerary source)
	{
		Itinerary newList= new Itinerary();
		TripStopNode nodeptr=source.head;
		while(nodeptr!=null)
		{
			newList.appendToTail((TripStop)nodeptr.getData().clone());
			nodeptr=nodeptr.getNext();
		}
		return newList;
	}
	
	
}
