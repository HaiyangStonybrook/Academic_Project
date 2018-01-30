package hw5;
/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 5
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * this class is a diver class which run the program
 */
public class DecisionTreeClassifier {
	private static Scanner reader = new Scanner(System.in);
	private static boolean running = true;

	/**
	 * print main menu
	 */
	public static void printMenu() {
		System.out.println("I) Import a tree from a file");
		System.out.println("E) Edit current tree ");
		System.out.println("C) Classify a Description ");
		System.out.println("P) Show decision path for a Description ");
		System.out.println("Q) Quit ");

	}

	/**
	 * print Main menu selection
	 */
	public static void selection() {
		System.out.println("Select an operation in Main Menu: ");
	}

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to the DecisionTree Classifier");
		TreeNavigator tree = new TreeNavigator();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		String input;
		printMenu();

		while (running) {
			selection();
			char c = reader.next().charAt(0);
			if (c == 'I' || c == 'i') { // Import file
				Scanner file = new Scanner(System.in);
				try {
					String cLine;
					System.out.println("Please Enter a filename: ");
					String filename = file.nextLine();
					br = new BufferedReader(new FileReader(filename)); // type the file address like:
																	   // "C:\\Users\\ASUS\\Desktop\\input.txt"
					cLine = br.readLine();
					while (cLine != null) {
						cLine = cLine.replaceAll("\t", "");
						cLine = cLine.replaceAll("\n", "");
						cLine = cLine.replaceAll(" ", "");
						sb.append(cLine);
						cLine = br.readLine();
					}
					input = sb.toString();
					System.out.println(input);
					tree = TreeNavigator.buildTree(input);
					System.out.println("file imported");
				} catch (IOException e) {System.out.println("Trouble importing");}
				  catch (IllegalFileException e) {System.out.println("file is invaild");}
			}
			if (c == 'E' || c == 'e') { // Edit current tree
				Scanner reader2 = new Scanner(System.in);
				char c2;
				tree.resetCursor();
				boolean run = true;
				System.out.println("Cursor is at root.");
				if (tree.getRoot().getKeywords() == null) {
					System.out.println("Current node keywords: tree is empty.");
				} else {
					System.out.println("Current node keywords: " + tree.getRoot().toString());
				}
				System.out.println("E)Edit Keywords");
				System.out.println("A)Add Children ");
				System.out.println("D)Delete Children, and Make Leaf ");
				System.out.println("N)Cursor to No Child ");
				System.out.println("Y)Cursor to Yes Child ");
				System.out.println("R)Cursor to Root ");
				System.out.println("P)Cursor to Parent ");
				System.out.println("M)Main Menu ");
				while (run) {
					System.out.println("Please select an option in Edit Menu: ");
					c2 = reader.next().charAt(0);
					
					if (c2 == 'e' || c2 == 'E') { // Edit keywords for this node
						System.out.println("Please enter keywords for this node, separated by a comma:smelly,dim ");
						String c3 = reader2.nextLine();
						tree.editCursor(c3);
						System.out.println(" Keywords updated to: " + c3);
					}
					
					if (c2 == 'A' || c2 == 'a') { // Add children for left and
													// right with no and yes
						System.out.println("Please enter terminal text for the no leaf: ");
						String s3 = reader2.nextLine();
						tree.editLeft(s3);
						System.out.println(" Please enter terminal text for the yes leaf: ");
						String s4 = reader2.nextLine();
						tree.editRight(s4);
						System.out.println(" Children are: yes - " + "'" + s4 + "'" + " and no -" + "'" + s3 + "'");
					}
					
					if (c2 == 'D' || c2 == 'd') { // Delete Children, and Make
													// Leaf
						System.out.println(" Please enter a terminal text for this node: ");
						String d = reader2.nextLine();
						tree.editCursor(d);
						tree.getCursor().setLeft(null);
						tree.getCursor().setRight(null);
						System.out.println(" Current node is leaf. Text is: " + tree.getCursor().toString());

					}
					
					if (c2 == 'Y' || c2 == 'y') { // Cursor to Yes Child
						try {
							tree.cursorRight();
							if (tree.getCursor().isLeaf())
								System.out.println(
										" Cursor moved. Cursor is at leaf, message is " + tree.getCursor().toString());
							else
								System.out.println(" Cursor moved. Cursor is at nonleaf, message is "
										+ tree.getCursor().toString());
						} catch (NullPointerException e) {
							System.out.println("right is null");
						}
					}

					if (c2 == 'N' || c2 == 'n') { // Cursor to No Child
						try {
							tree.cursorLeft();
							if (tree.getCursor().isLeaf())
								System.out.println(
										" Cursor moved. Cursor is at leaf, message is " + tree.getCursor().toString());
							else
								System.out.println(" Cursor moved. Cursor is at nonleaf, message is "
										+ tree.getCursor().toString());
						} catch (NullPointerException e) {
							System.out.println("left is null");
						}
					}
					
					if (c2 == 'R' || c2 == 'r') { // reset cursor
						tree.resetCursor();
						System.out.println("Cursor moved. Cursor is at root. ");
					}

					if (c2 == 'P' || c2 == 'p') { // find parent
						try {
							String str = tree.findCursorParent();
							System.out.println("Cursor parent is: " + str);
						} catch (NullPointerException e) {
							System.out.println("cursor is at root, no parent");
						}
					}

					if (c2 == 'M' || c2 == 'm') { // return to main menu
						break;
					}
				}
			}
			
			if (c == 'C' || c == 'c') { // Classify a Description
				Scanner in = new Scanner(System.in);
				System.out.println("Please enter some text: ");
				String cs = in.nextLine();
				String classify = tree.classify(cs);
				System.out.println("Your request is classified as: " + classify);
			}
			
			if (c == 'P' || c == 'p') { // Show decision path for a Description
				Scanner in = new Scanner(System.in);
				System.out.println("Please enter some text: ");
				String pa = in.nextLine();
				String cp = tree.getCursorPath(pa);
				String path = tree.getPath(cp);
				System.out.println("Dicision path: " + path);
			}
			
			if (c == 'Q' || c == 'q') { // Quit
				System.out.println("Goodbye!");
				running = false;
				break;
			}
		}
	}
}
