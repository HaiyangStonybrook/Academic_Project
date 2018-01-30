package hw5;

/**
 * IllegalFileException, throw it if file is Illegal
 */
public class IllegalFileException extends Exception {
public IllegalFileException(){}
	
	public IllegalFileException (String message)
	{
		super(message);
	}
}

