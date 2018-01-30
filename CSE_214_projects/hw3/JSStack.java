package hw3;

/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 3
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 */

import java.util.EmptyStackException;
import java.util.LinkedList;

/**
 * This class construct the stack class implemented by LinkedList .
 * @author Haiyang Liu
 *
 */
public class JSStack <T> {
	/**
	 * 	the stack using LinkedList
	 */
	private LinkedList<T> stack;
	
	/**
	 * Constructor with no param, and initialize the stack to empty
	 */
	public JSStack()
	{
		stack= new LinkedList <T>();
	}
	
	/**
	 * return true is stack is empty
	 * @return a boolean value
	 */
	public boolean isEmpty()
	{
		if (stack.size()==0) 
			return true;
		return false;
	}
	
	/**
	 * return the size of the stack
	 * @return a int value
	 */
	public int size()
	{
		return stack.size();
	}
	
	/**
	 * add an object to the top of the stack
	 * @param obj a <T> value
	 */
	public void  push(T obj)
	{
		stack.addFirst(obj);
	}
	
	/**
	 * remove an object from the top of the stack and return it
	 * @return a <T> value
	 * @throws EmptyStackException if stack is empty
	 */
	public T pop() throws EmptyStackException
	{
		T temp;
		if(stack.isEmpty())
			throw new EmptyStackException();
		else
			temp =stack.getFirst();
			stack.removeFirst();
			return temp;
	}
	
	/**
	 * return the element at the top of stack 
	 * @return a <T> value
	 * @throws EmptyStackException if stack if empty
	 */
	public T peek() throws EmptyStackException
	{
		if(stack.isEmpty())
			throw new EmptyStackException();
		else
			return stack.getFirst();
	}
	
}
