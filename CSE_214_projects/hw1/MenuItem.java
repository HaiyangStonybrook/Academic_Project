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

public class MenuItem {
    private String name;
    private String description;
    private double price;
 
    /** 
     * 	MenuItem Constructor without param
     */
    public MenuItem(){}
    
    /**
     *  MenuItem Constructor with para 
     *  param name: item's Name 
     *  param description: item's description
     *  param price: item's price 
     */
    public MenuItem(String name, String description, double price)
    {
		this.name=name;
    	this.description=description;
    	this.price=price;
    }
    
    /**
     *  To get itme's name
     */
    public String getName()
    {
		return name;
	}
    
    /**
     *  To get item's description
     */
	public String getDescription() 
	{
		return description;
	}
	
	/**
	 * To get item's price
	 */
	public double getPrice() 
	{
		return price;
	}
	
	/**
	 *  Set item's name with String n
	 */
	public String setName(String n)
	{
    	name=n;
    	return name;
    }
	
    /**
     *  Set item's decription with String d
     */
    public String setDescription(String d)
    {
    	description=d;
    	return description;
    }
    
    /**
     *  Set item's price with double p
     *  throw an Exception if price is nonpositive
     */
    public double setPrice(double p) throws IllegalArgumentException 
    {
    	price=p;
    	if(price<0)
    	{
    		throw new IllegalArgumentException();
    	}
    	else
    	{
    		return price;
    	}	
    }
    
    /**
     *  Gets the String representation of this MenuItem 
     */
    public String toString()
    {
		return this.getName()+"			"+ this.getDescription() + "		$ "+ this.getPrice();
    }
    
    /**
     * Compare this MenuItem to another object for equality.
     * Parameters: item - an object to which this MenuItem is compared
     * Returns: A return value of true indicates that item refers to a MenuItem object with the same MenuItems. 
     * Otherwise, the return value is false.
     */
    public boolean equals(MenuItem item)
    {
    	boolean compare=true;
    	if(!this.getName().equals(item.getName()))
    		compare=false;
    	if(!this.getDescription().equals(item.getDescription()))
    		compare=false;
    	if(this.getPrice()!=item.getPrice())
    		compare=false;
    	return compare;
    }
    
    /**
     * Generate a copy of this MenuItem and return a copy of this MenuItem, the copy will not affect the original
     */
    public Object clone()
    {
    	MenuItem clone=new MenuItem();
    	clone.setName(this.getName());
    	clone.setDescription(this.getDescription());
    	try {
			clone.setPrice(this.getPrice());
			} 
    	catch (CustomException e) {}
    	return clone;
    }   
}
