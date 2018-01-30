package hw3;

/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 3
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 */


/**
 * This class holds enum data including a left brace, left parentheses and for
 * @author Haiyang Liu
 *
 */
public enum BlockType {
	LBRACE('{'), 
	LPAREN('('),
	FOR('f');
	private char value;
	
/**
 * Constructor of enum
 * @param symble the char for the enum data
 */
	private BlockType(char symble )
	{
		this.value=symble;
	}

/**
 * return the enum data value
 * @return a char value
 */
	public char getValue() {
		return value;
	}
	
}