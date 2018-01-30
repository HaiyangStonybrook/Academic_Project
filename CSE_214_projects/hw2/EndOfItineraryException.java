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
 *  customize the EndOfItineraryException to throw an exception if the cursor is at the end of the list
 * @author Haiyang Liu
 *
 */
public class EndOfItineraryException extends Exception {
	public EndOfItineraryException(){}
	
	public EndOfItineraryException (String message)
	{
		super(message);
	}
}
