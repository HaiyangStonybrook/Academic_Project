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

public class CustomException extends Exception {
	public CustomException(){}
	
	public CustomException(String message)
	{
		super(message);
	}
	
	public CustomException(Throwable cause)
	{
		super(cause);
	}
	
	public CustomException(String message,Throwable cause)
	{
		super(message,cause);
	}

}
