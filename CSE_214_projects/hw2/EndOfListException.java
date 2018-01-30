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
 * customize the EndOfListException to throw an exception if the cursor is null
 * @author Haiyang Liu
 *
 */
public class EndOfListException extends Exception {
public EndOfListException(){}
	
	public EndOfListException (String message)
	{
		super(message);
	}
}
