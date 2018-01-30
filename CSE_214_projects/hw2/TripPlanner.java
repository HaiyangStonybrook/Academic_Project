package hw2;

/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 2
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 */

import java.util.Scanner;

/**
 * This class is to print the operation menu
 * @author Haiyang lIu
 *
 */
public class TripPlanner {

	public static void printMenu()
	{
		System.out.println ("F- Cursor forward " );
		System.out.println ("B- Cursor backward " );
		System.out.println ("I- Insert before cursor " );
		System.out.println ("A- Append to tail " );
		System.out.println ("D- Delete and move cursor forward " );
		System.out.println ("H- Cursor to Head " );
		System.out.println ("T- Cursor to Tail " );
		System.out.println ("E- Edit cursor " );
		System.out.println ("S- Switch itinerary " );
		System.out.println ("O- Insert cursor from Other itinerary before cursor to this itinerary" );
		System.out.println ("R- Replace this itinerary with a copy of the other itinerary " );
		System.out.println ("P- Print current itinerary, including summary " );
		System.out.println ("C- Clear current itinerary " );
		System.out.println ("Q- Quit" );
		System.out.println ("Now you are in the Itinerary 1 as default!\n" );
	}
	
	/**
	 * print the selection operation to input a selection
	 */
	public static void operationSelection()
	{
		System.out.println ("Select an operation: ");
	}
	
	public static void main(String[] args) {
		System.out.println ("Welcome to TripPlanner ! " );
		Itinerary trip =new Itinerary();
		Itinerary trip_1 =trip;
		Itinerary trip_2 = new Itinerary();
		char c;
		Scanner reader= new Scanner(System.in);
		printMenu();
		
		while(true)
		{
			operationSelection();
			c=reader.next().charAt(0);
			
			/**
			 * runs the operation to move cursor forward
			 */
			if(c=='F'||c=='f')
			{	
				try
				{
					trip.cursorForward();
					System.out.println(" Cursor moved forward, now is at " + trip.getCursorStop().getLocation());
				}
				catch (EndOfItineraryException e)
				{
					System.out.println("Cursor already at end of list\n");
				}
			}
			
			/**
			 * runs the operation to move cursor backward
			 */
			if(c=='B'||c=='b')
			{
				try
				{	
					trip.cursorBackward();
					System.out.println(" Cursor moved back, now is at " + trip.getCursorStop().getLocation());
				}	
				catch (EndOfItineraryException e)
				{
					System.out.println("Cursor already at beginning of list\n");
				}
			}
			
			/**
			 *runs the operation to insert a TripStop element before cursor
			 */
			if(c=='I'||c=='i')
			{
				TripStop stop = new TripStop();
				Scanner input=new Scanner (System.in);
				System.out.println("Enter the Location: ");
				String loc=input.nextLine();
				System.out.println("Enter the Activity: ");
				String act=input.nextLine();
				System.out.println("Enter the Distance: ");
				int dis=input.nextInt();
				stop.setLocation(loc);
				stop.setActivity(act);
				try{stop.setDistance(dis);}
				catch(IllegalArgumentException e){System.out.println("This distance is invalid, it must be non-negetive! \n");}
				try
				{
					trip.insertBeforeCursor(stop);
					System.out.println("Inserted\n");
				}
				catch(IllegalArgumentException e){System.out.println("This stop is null, please re-enter! \n");}
			}
			
			/**
			 * runs the operation to append a TripStop element after the tail
			 */
			if(c=='A'||c=='a')
			{
				TripStop stop = new TripStop();
				Scanner input=new Scanner (System.in);
				System.out.println("Enter the Location: ");
				String loc=input.nextLine();
				System.out.println("Enter the Activity: ");
				String act=input.nextLine();
				System.out.println("Enter the Distance: ");
				int dis=input.nextInt();
				stop.setLocation(loc);
				stop.setActivity(act);
				try{stop.setDistance(dis);}
				catch(IllegalArgumentException e){System.out.println("This distance is invalid, it must be non-negetive! \n");}
				try
				{
					trip.appendToTail(stop);;
					System.out.println(" Appended to tail\n");
				}
				catch(IllegalArgumentException e){System.out.println("This stop is null, please re-enter! \n");}
			}
			
			/**
			 * runs the operation to remove the cursor node and move cursor to the next node
			 */
			if(c=='d'||c=='D')
			{
				try
				{
					trip.removeCursor();
					System.out.println(" Deleted ");
				}
				catch(EndOfListException e){System.out.println(" This cursor is empty!\n ");}
			}
			
			/**
			 * runs the operation to reset cursor to the start of the list
			 */
			if(c=='H'||c=='h')
			{
				trip.resetCursorToHead();
				System.out.println(" Cursor now moved to head ! \n");
			}
			
			/**
			 * runs the operation to move the cursor to the tail
			 */
			if(c=='T'||c=='t')
			{
				trip.moveToTail();
				System.out.println(" Cursor now moved to tail ! \n");
			}
			
			/**
			 * runs the operation to update the location, activity, and distance information of the cursor
			 */
			if(c=='E'||c=='e')
			{
				TripStop stop = trip.getCursorStop();
				Scanner input=new Scanner (System.in);
				System.out.println("Enter the Location, or press enter without typing anything to keep: ");
				String loc=input.nextLine();
					if(!loc.equals(""))
						stop.setLocation(loc);	
				System.out.println("Enter the Activity, or press enter without typing anything to keep: ");
				String act=input.nextLine();
					if(!act.equals(""))
						stop.setActivity(act);
				System.out.println("Enter the Distance: ");
				int dis=input.nextInt();
				try
				{
					stop.setDistance(dis);
					System.out.println(" Edits applied \n");
				}
				catch(IllegalArgumentException e){System.out.println("This distance is invalid, it must be non-negetive! \n");}
					
			}
			
			/**
			 * runs the operation to switch itinerary 
			 */
			if(c=='S'||c=='s')
			{
				System.out.println("Please enter the itinerary 1 or itinerary 2 by typing 1 or 2: ");
				Scanner input=new Scanner (System.in);
				int ipt= input.nextInt();
				switch(ipt)
				{
					case 1:	
						trip=trip_1;
						System.out.println("switch to itinerary 1\n");
						break;
					case 2:
						trip=trip_2;
						System.out.println("switch to itinerary 2\n");
						break;		
				}
			}
			
			/**
			 * runs the operation to Insert cursor from other itinerary before cursor from this itinerary
			 */
			if(c=='O'||c=='o')
			{
				System.out.println("which itinerary tripstop you want to pick, typing 1 or 2 for itinerary: ");
				Scanner input=new Scanner (System.in);
				int ipt= input.nextInt();
				if(ipt==1)
				{
					trip_2.insertBeforeCursor(trip_1.getCursorStop());
					System.out.println("Inserted in itinerary 2\n");
				}
				if(ipt==2)
				{
					trip_1.insertBeforeCursor(trip_2.getCursorStop());
					System.out.println("Inserted in itinerary 1\n");
				}
			}
			
			/**
			 * runs the operation to Replace this itinerary with a copy of the other itinerary
			 */
			if(c=='R'||c=='r')
			{
				System.out.println("which itinerary you want to use as source, typing 1 or 2: ");
				Scanner input=new Scanner (System.in);
				int ipt= input.nextInt();
				if(ipt==1)
				{
					trip_2=Itinerary.listCopy(trip_1);
					System.out.println("copied\n");
				}
				if(ipt==2)
				{
					trip_1=Itinerary.listCopy(trip_2);
					System.out.println("copied\n");
				}
			}
			
			/**
			 * runs the operation to Print current itinerary including the summary
			 */
			if(c=='P'||c=='p')
			{
				System.out.println("chose your current itinerary, typing 1 or 2 ");
				Scanner input=new Scanner (System.in);
				int ipt= input.nextInt();
				if(ipt==1)
				{
					trip_1.printItinerary();
				}
				if(ipt==2)
				{
					trip_2.printItinerary();
				}
			}
			
			/**
			 * runs the operation to clear current itinerary
			 */
			if(c=='C'||c=='c')
			{
				System.out.println("chose the itinerary you want to clear, typing 1 or 2 ");
				Scanner input=new Scanner (System.in);
				int ipt= input.nextInt();
				if(ipt==1)
				{
					trip_1=new Itinerary();
					System.out.println("Cleared\n");
				}
				if(ipt==2)
				{
					trip_2=new Itinerary();
					System.out.println("Cleared\n");
				}
			}
			
			/**
			 * runs the operation to quit
			 */
			if(c=='Q'||c=='q')
			{
				System.out.println("Thank you for your using!");
				System.exit(0);
			}
		}
	}

}
