package hw6;
/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 6
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * this is a drive class which runs the program
 *
 */
public class RoomLookup {
	private static Scanner scan = new Scanner(System.in);
	private static Scanner scan2= new Scanner(System.in);
	public static void printMainMenu(){
		System.out.println("A) Add a building");
		System.out.println("D) Delete a building");
		System.out.println("E) Edit a building");
		System.out.println("F) Find a room");
		System.out.println("S) Search for rooms");
		System.out.println("C) List all buildings on Campus");
		System.out.println("L) List building details");
		System.out.println("Q) Quit");
	}
	
	public static void selection(){
		System.out.println("Please select an option: ");
	}
	
	public static void main(String[] args) throws Exception{	
		System.out.println("Welcome to SBGetARoom, Stony Brook's premium room lookup system");
		printMainMenu();
		Campus campus=new Campus();
		try{
			FileInputStream file = new FileInputStream("storage.obj");
			ObjectInputStream inStream = new ObjectInputStream(file);
			campus=(Campus)inStream.readObject(); 
			inStream.close();
			System.out.println("file loaded.");
		}
		catch (ClassNotFoundException e1) {System.out.println("Class not found");}
		catch(FileNotFoundException e2){System.out.println("No save file found. Creating an empty campus.");}
		catch(IOException e3){System.out.println("Trouble in I/O");}
		boolean running = true;

		while(running){
			selection();
			String in=scan.nextLine();
			in=in.toLowerCase();
		
			switch(in){
				//add building
				case "a":
					Building building=new Building();
					System.out.println("Please enter a building name: ");
						String bName1=scan.nextLine();
					if(!campus.containsKey(bName1)){
						campus.addBuilding(bName1, building);
						System.out.println("Building "+bName1+" added.");
					}
					else
						System.out.println("Building "+bName1+" already exists.");
					break;
				//delete building
				case "d":
					System.out.println("Please enter a building name you want to remove: ");
					String bName2=scan.nextLine();
					if(!campus.containsKey(bName2))
						System.out.println("Building not found.");
					else{
						campus.removeBuilding(bName2);
						System.out.println("Building "+bName2+" deleted.");
					}	
					break;
				//edit building by name
				case "e":
					//Scanner scan2=new Scanner(System.in);
					System.out.println("Please enter a building name: ");
					String bName3=scan.nextLine();
					if(!campus.containsKey(bName3))
						System.out.println("Building not found.");
					else{
						//campus.getBuilding(bName3);
						System.out.println("A) Add room");
						System.out.println("D) Delete room");
						System.out.println("E) Edit room");
						System.out.println("Please select an option: ");
						String in2=scan.nextLine();
						in2=in2.toLowerCase();
						switch(in2){
						//add room
						case "a":
							//classroom number
							System.out.println("Please enter room number: ");
							Classroom classroom=new Classroom();
							int rNumber1=scan2.nextInt();
							if(!campus.getBuilding(bName3).containsKey(rNumber1)){
								//classroom seats
								System.out.println("Please enter number of seats: ");
								int rSeats1=scan2.nextInt();	
								try{
									classroom.setNumSeats(rSeats1);
								}catch(IllegalArgumentException e){System.out.println("Negative seat number");}
								//classroom AV
								System.out.println("Please enter AV Equipment (separated by commas): projector,computer,desktops ");
								//Scanner scan3=new Scanner(System.in);
								String rAV1=scan.nextLine();	//classroom AV
								List<String> list=new ArrayList<String>(Arrays.asList(rAV1.split(",")));
								classroom.setAVEquipmentList(list);
								//classroom white board
								System.out.println("Does it have a whiteboard?(Y/n): ");
								String rWhite1=scan.nextLine();
								rWhite1=rWhite1.toLowerCase();
								if(rWhite1.equals("y"))			
									classroom.setHasWhiteboard(true);
								if(rWhite1.equals("n"))
									classroom.setHasWhiteboard(false);
								//classroom chalk board
								System.out.println("Does it have a chalkboard?(Y/n): ");
								String rChalk1=scan.nextLine();
								rChalk1=rChalk1.toLowerCase();
								if(rChalk1.equals("y"))			
									classroom.setHasChalkboard(true);
								if(rChalk1.equals("n"))
									classroom.setHasChalkboard(false);
								campus.getBuilding(bName3).addClassroom(rNumber1, classroom);	//add classroom to building
								System.out.println("Room "+ bName3 +" "+ rNumber1+" Added." );
							}
							else
								System.out.println("Classroom "+rNumber1+" already exists.");
							
							break;
						//delete room	
						case "d":
							System.out.println("Please enter room number:");
							int rNumber2=scan2.nextInt();
							if(!campus.getBuilding(bName3).containsKey(rNumber2))
								System.out.println("classroom not found.");
							else{
								campus.getBuilding(bName3).removeClassroom(rNumber2);
								System.out.println("room "+rNumber2+" deleted.");
							}
							break;
						//edit room	
						case "e":
							System.out.println("Please enter room number:");
							int rNumber3=scan2.nextInt();
							if(!campus.getBuilding(bName3).containsKey(rNumber3))
								System.out.println("classroom not found.");
							else{
								System.out.println("Old number of seats: "+campus.getBuilding(bName3).getClassroom(rNumber3).getNumSeats());
								//set room seat
								System.out.println("Please enter number of seats or press enter to skip:");
								String rSeat2=scan.nextLine();
								if(!rSeat2.equals("")){
									Integer rrSeat2=Integer.parseInt(rSeat2);
									try{
										campus.getBuilding(bName3).getClassroom(rNumber3).setNumSeats(rrSeat2);
									}catch (IllegalArgumentException e){System.out.println("Negative seat number");}
									}
								//set AVEquipment
								System.out.println("Old AV Equipment: "+campus.getBuilding(bName3).getClassroom(rNumber3).getAVEquipmentList().toString() );
								System.out.println("Please enter newAV Equipment (separated by commas) or press enter to skip: projector,computer,smart tablet");
								String rAV2=scan.nextLine();
								if(!rAV2.equals("")){
									List<String> list2=new ArrayList<String>(Arrays.asList(rAV2.split(",")));
									campus.getBuilding(bName3).getClassroom(rNumber3).setAVEquipmentList(list2);//Arrays.asList(rAV2.split(","))
								}
								//set white board
								System.out.println("Does it have a whiteboard?(Y/n) or press enter to skip: ");
								String rWhite2=scan.nextLine().toLowerCase();
								//rWhite2=rWhite2.toLowerCase();
								if(!rWhite2.equals("")){
									if(rWhite2.equals("y"))
										campus.getBuilding(bName3).getClassroom(rNumber3).setHasWhiteboard(true);
									if(rWhite2.equals("n"))	
										campus.getBuilding(bName3).getClassroom(rNumber3).setHasWhiteboard(false);
								}
								//set chalk board
								System.out.println("Does it have a chalkboard?(Y/n) or press enter to skip: ");
								String rChalk2=scan.nextLine().toLowerCase();
								//rChalk2=rChalk2.toLowerCase();
								if(!rChalk2.equals("")){
									if(rChalk2.equals("y"))
										campus.getBuilding(bName3).getClassroom(rNumber3).setHasChalkboard(true);
									if(rChalk2.equals("n"))
										campus.getBuilding(bName3).getClassroom(rNumber3).setHasChalkboard(false);
								}
								System.out.println(bName3+rNumber3+" updated.");	
								}
								break;	
								
						default:
							 System.out.println(in2 + " not is not an option");		
						}	
					}
					break;
				//find room	
				case "f":
					System.out.println("please enter a room name: Building [space] Roomnumber ");
					String find=scan.nextLine();
					//ArrayList<String> findRoom=new ArrayList<String>(Arrays.asList(find.split(" ")));
					String[] findRoom=find.split(" ");
					String	name=findRoom[0];
					String	number=findRoom[1];
					Integer roomNum=Integer.parseInt(number);
					String detail=campus.getBuilding(name).getClassroom(roomNum).printDetail();
					System.out.println(detail);
					break;
				//search	
				case"s":
					System.out.println("B) Blackboard");
					System.out.println("W) Whiteboard");
					System.out.println("A) AV Equipment");
					System.out.println("Please select an option: ");
					String in3=scan.nextLine();
					in3=in3.toLowerCase();
					switch(in3){
						//print all classrooms with white board
						case "w":
							campus.whiteboardBuilding();
						break;
						//print all classrooms with chalk board
						case "b":
							campus.blackboardBuilding();
						break;
						//print all classrooms with specific AV equipment
						case "a":
							System.out.println("Please enter the AV equipment name: ");
							String av=scan.nextLine();
							campus.avBuilding(av);
						break;
						
						default:
							 System.out.println(in3 + " not is not an option");
					}
				break;
				//list buildings on campus and their rooms.
				case "c":
					campus.allBuildingRoom();
				break;
				//list summary of building by name
				case "l":
					System.out.println("Please enter a building name: ");
					String bName4=scan.nextLine();
					String summary="";
					if(!campus.containsKey(bName4))
						System.out.println("Building not found.");
					else{
						summary=campus.getBuilding(bName4).buildingList();
						System.out.println(summary);
					}
				break;
				
				case "q":
					System.out.println("S - Save");
					System.out.println("D - Don't Save");
					System.out.println("Please select an option: ");
					String in4=scan.nextLine();
					in3=in4.toLowerCase();
					switch(in4){
					//close without saving
					case"d":
						System.out.println("Closing without saving...");
						scan.close();
						scan2.close();
						running = false;
		            break;
		            //save and close
					case"s":
						try{
							FileOutputStream file=new FileOutputStream("storage.obj");
							ObjectOutputStream outStream=new ObjectOutputStream(file);
							outStream.writeObject(campus);
							outStream.close();
							//file.close();
						}
						catch(FileNotFoundException e){System.out.println("File not found.");}
						catch(IOException e){System.out.println("troble in I/O");}
						System.out.println("Closing with saving!");
						scan.close();
						scan2.close();
						running = false;	
					break;
					}
				break;
				
				default:
					System.out.println(in + " not is not an option");
			}
		}
	}
}
