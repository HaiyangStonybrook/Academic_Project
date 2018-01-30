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

import java.util.Scanner;

public class MenuOperations {
	
	public static void printMenu()
	{
		System.out.println ("A)  Add Item " );
		System.out.println ("G)  Get Item " );
		System.out.println ("R)  Remove Item " );
		System.out.println ("P)  Print All Items " );
		System.out.println ("S)  Size" );
		System.out.println ("D)  Update description" );
		System.out.println ("C)  Update price" );
		System.out.println ("O)  Add to order" );
		System.out.println ("I)  Remove from order" );
		System.out.println ("V)  View order" );
		System.out.println ("Q)  Quit" );
	}
	
	public static void menuSelection()
	{
		System.out.println ("Select an operation: ");
	}
	
	public static void main(String[] args){
		Menu menu=new Menu();
		Menu order=new Menu();
		char c;
		Scanner reader= new Scanner(System.in);
		printMenu();
		while(true){
			menuSelection();
			c=reader.next().charAt(0);
			
			/**
			 * add the item to the menu
			 * catch wrong price, full list and null pointer Execption
			 */
			if(c=='A'||c=='a')
			{
				MenuItem item=new MenuItem();
					Scanner input=new Scanner (System.in);
					System.out.println("Enter the name: ");
					String nm=input.nextLine();
					System.out.println("Enter the description: ");
					String de=input.nextLine();
					System.out.println("Enter the Price: ");
					double pr=input.nextDouble();
					item.setName(nm);
					item.setDescription(de);
					try
					{
						item.setPrice(pr);
						System.out.println("Enter the position: ");
						int po=input.nextInt();
							menu.addItem(item, po);
							System.out.println("\n");
					}
					catch(IllegalArgumentException e)
						{
								System.out.println("Wrong Price ");
						}	 
					catch (CustomException e) 
						{
							System.out.println(" The menu is full !");
						}
					catch (NullPointerException e) 
					{	
						if(e instanceof NullPointerException)
						System.out.println(" It is invaild!, you have to put in order!");
					}
					
			}
			
			/**
			 * Print out the name, description, and price of the item at the specified position in the menu
			 * catch out of range Exception
			 */
			else if(c=='G'||c=='g')
			{
				System.out.println("Enter the position: ");
				Scanner input=new Scanner (System.in);
				int pos=input.nextInt();
				try
				{
					System.out.println(menu.getItem(pos).toString());
				}
				catch (IllegalArgumentException e)
				{
					System.out.println("Out of Range! \n");
				}
			}
			
			/**
			 * Remove the item with the given name in the menu
			 * catch IllegalArgumentException if can't find this item
			 */
			else if(c=='R'||c=='r')
			{
				try
				{
					System.out.println("Enter the name: ");
					Scanner input=new Scanner (System.in);
					String s=input.nextLine();
					menu.removeByName(s);
				}
				catch (IllegalArgumentException e)
				{
					System.out.println("Cannot find this item! \n");
				}
			}
			
			/**
			 * print the list of all items on the menu
			 */
			else if(c=='P'||c=='p')
			{
				menu.printAllItems();
			}
			
			/**
			 * print the number of items on the menu
			 */
			else if(c=='S'||c=='s')
			{
				System.out.println("The size of this menu is: "+menu.size());
			}
			
			/**
			 * update the description of the named item
			 * catch IllegalArgumentException if position is out of range
			 */
			else if(c=='D'||c=='d')
			{
				System.out.println("Enter the position: ");
				Scanner input=new Scanner (System.in);
				int pos=input.nextInt();
				System.out.println("Enter the new description: ");
				String newDes=input.next();
				try{
						menu.getItem(pos).setDescription(newDes);
						System.out.println("New description set as " + newDes + "\n");
					}
				catch (IllegalArgumentException e)
					{
						System.out.println("Out of Range ! \n");
					}
				
			}
			
			/**
			 * update the price of the named item
			 * catch IllegalArgumentException if price is wrong or can't find that item
			 */
			else if(c=='C'||c=='c')
			{
				System.out.println("Enter the name of the item: ");
				Scanner input=new Scanner (System.in);
				String nm=input.next();
				System.out.println("Enter the new price: ");
				int newPrice=input.nextInt();
				try
				{
					menu.getItemByName(nm).setPrice(newPrice);
					System.out.println("change the price of " + nm + " to " + newPrice);
				}
				catch (IllegalArgumentException e)
				{
					System.out.println("Wrong Price or cannot find that item! ");
				}
			}
			
			/**
			 * Add the item at the specified position in the menu to the order
			 * catch CustomException if the order is full
			 */
			else if(c=='o'||c=='O')
			{
				System.out.println("Enter the position of the itme in the menu you want to add: ");
				Scanner input=new Scanner (System.in);
				int pos=input.nextInt();
				System.out.println("Enter the position of the order: ");
				int pos_2=input.nextInt();
				
				try
				{
					order.addItem(menu.getItem(pos), pos_2);
					System.out.println("added the item " + menu.getItem(pos) + " at order " + order.size() );
				} catch (CustomException e){
					System.out.println("The order is full !");
				}
			}
			
			/**
			 * Remove the item at the specified position in the order
			 */
			else if(c=='i'||c=='I')
			{
				System.out.println("Enter the position of the itme in the menu you want to remove: ");
				Scanner input=new Scanner (System.in);
				int pos=input.nextInt();
				try
				{
					order.removeItem(pos);
				}
				catch(IllegalArgumentException e)
				{
					System.out.println("position is out of range!\n");
				}
			}
			
			/**
			 * print the items in the current order
			 */
			else if(c=='v'||c=='V')
			{
				order.printAllItems();
			}
			
			/**
			 * terminate the program gracefully
			 */
			else if(c=='q'||c=='Q')
			{
				System.out.println("Thank you for your using!");
				System.exit(0);
			}
		}
	}
}
	


