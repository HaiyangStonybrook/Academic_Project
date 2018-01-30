package hw1;

/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 1
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: 
 * @author 
 *
 */

public class Menu {
	final int MAX_ITEMS=50;
	private MenuItem[] list;
	private int index;
	
	/**
	 * Construct an instance of the Menu class with no MenuItem objects in it
	 */
	public Menu()
	{
		list=new MenuItem[MAX_ITEMS];
		index=0;
		
	}
	
	/**
	 * Generate a copy of this Menu and return a copy of this Menu, and return the copy 
	 * The copy will not affect the original
	 */
	public Object clone() 
	{
		Menu clone=new Menu();
		clone.index=this.index;
		for(int i=0; i<index; i++)
		{
			clone.list[i]=(MenuItem)(this.list[i].clone());
		}
		return clone;
	}
		
	
	/**
	 * Compare this Menu to another object for equality.
     * Parameters: obj - an object to which this Menu is compared
     * Returns: A return value of true indicates that obj refers to a Menu object with the same Menu. 
     * Otherwise, the return value is false.
	 */
	public boolean equals (Object obj)
	{
		if(!(obj instanceof Menu))
				return false;
		Menu temp= (Menu) obj;// check index
		boolean compare=true;
		if (this.size()!=temp.size())
			return false;
		for(int i=0; i<this.list.length; i++)
		{
			if(!this.list[i].getName().equals(temp.list[i].getName()))
				compare=false;
			if(!this.list[i].getDescription().equals(temp.list[i].getDescription()))
				compare=false;
			if(this.list[i].getPrice()!=temp.list[i].getPrice())
				compare=false;
		}
    	return compare;
	}
	
	/**
	 * Return the number of items currently in this Menu.
	 * Preconditions:This Menu object has been instantiated.
	 * Returns: The number of MenuItems in this Menu.
	 */
	public int size()
	{
		return index;
	}
	
	
	/**
	 * item - the new MenuItem object to add to this Menu 
	 * position - the position in the Menu where item will be inserted 
	 * Preconditions: This Menu object has been instantiated and 1 < position < items_currently_in_list + 1. 
	 * 				  The number of MenuItem objects in this Menu is less than MAX_ITEMS. 
	 * Postcondition: The new MenuItem is now stored at the desired position in the Menu. 
	 * 				  All MenuItems that were originally in positions greater than or equal to position are moved back one position. 
	 */
	public void addItem(MenuItem item, int position) throws CustomException,NullPointerException
	{
		if(index>=50)
			throw new CustomException();
		if(position > this.size()+1)
			throw new NullPointerException();
		for(int i=position-1; i<this.size(); i++)
			{
				list[i+1]=list[i];
			}
			list[position-1]=item;
			index++;
			System.out.println("Added  " +" ' "+ item.getName() + " : "+ item.getDescription() + " ' for $"+ item.getPrice() + " at position " + position );	
	}
		
	/**
	 * search the item's name that user wants to remove
	 * param n: the name that user inputs to remove
	 *  
	 */
	public void removeByName(String n) throws IllegalArgumentException
	{	
		for(int i=0; i<this.size(); i++)
		{	
			if(list[i].getName().equals(n))
				{
					list[i]=null;
					list[i]=list[i+1];
					System.out.println(" remove		" + n);
				}
			else 
				throw new IllegalArgumentException();
		}
		index--;
	}
	
	/**
	 * Param position:the position in the Menu where the MenuItem will be removed from. 
	 * Preconditions: This Menu object has been instantiated and 1 < position < items_currently_in_list. 
	 * Postcondition: The MenuItem at the desired position in the Menu has been removed. 
	 * 				  All MenuItems that were originally in positions greater than or equal to position are moved forward one position. 
	 * Throws: IllegalArgumentException
			   Indicates that position is not within the valid range.
	 */
	public void removeItem (int position) throws IllegalArgumentException
	{
		if(position>size())
			throw new IllegalArgumentException();
		for(int i=position-1; i<this.size()-1; i++)
		{
			list[i]=list[i+1];
		}
		index--;
		System.out.println("remove" +" ' "+list[position].getName() + " ' " + "\n");
	}

	/**
	 * Get the MenuItem at the given position in this Menu object.
	 * Parameters:		position - position of the MenuItem to retrieve 
	 * Preconditions: 	This Menu object has been instantiated and 1 < position < items_currently_in_list.
	 * Returns: 		The MenuItem at the specified position in this Menu object.
	 * Throws: IllegalArgumentException.  
	 * 					Indicates that position is not within the valid range.
	 */
	public MenuItem getItem (int position) throws IllegalArgumentException
	{
		if(position>size())
			throw new IllegalArgumentException();
		return this.list[position-1];
		//exception
	}
	
	/**
	 * Get the MenuItem at with the given name in this Menu object.
	 * Parameters:		name - name of the MenuItem
	 * Preconditions: 	This Menu object has been instantiated and 1 < position < items_currently_in_list.
	 * Returns: 		The MenuItem with the given name in this Menu.
	 */
	public MenuItem getItemByName(String name) throws IllegalArgumentException
	{
		int i;
		for(i=0; i<this.size(); i++)
		{
			if (list[i].getName().equals(name))
				return list[i];
			else
				throw new IllegalArgumentException();
		}
		MenuItem m=list[i];
		return m;	
	}
	
	/**
	 * Prints a neatly formatted table of each item in the Menu on its own line with its position number as shown in the sample output.
	 * Preconditions:	This Menu object has been instantiated.
	 * Postcondition:	A neatly formatted table of each MenuItem in the Menu on its own line with its position number has been displayed to the user.
	 */
	public void printAllItems()
	{
		if(this.size()==0)
			System.out.println("This menu is empty! ");
		System.out.println("Name		 "+"Description		"+"Price		");
		System.out.println("------------------------------------------------");
		for(int i=0; i<this.size(); i++)
		{
			MenuItem a=list[i];
			System.out.println( (i+1)+" "+a.toString()+"\n");
		}	
	}		
}
	
	

