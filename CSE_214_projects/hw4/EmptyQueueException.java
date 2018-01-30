package hw4;
/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 4
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 */
/**
 * this class is a customized exception
 * throw it when queue is empty
 * @author ASUS
 *
 */
public class EmptyQueueException extends Exception {
public EmptyQueueException(){}
	
	public EmptyQueueException (String message)
	{
		super(message);
	}
}
