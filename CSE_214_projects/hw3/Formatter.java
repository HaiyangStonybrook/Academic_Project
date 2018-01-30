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

/**
 * Format a string
 * @author Haiyang Liu
 *
 */
public class Formatter {
	private JSStack<BlockType> stack;
	private int indentLevel=0;

	/**
	 * Constructor initializes an empty stack with no elements
	 */
	public Formatter()
	{
		stack=new JSStack<BlockType>();
		indentLevel=0;
	}
	
	/**
	 * return a string after formatting
	 * @param input a string value
	 * @return a string value
	 * @throws EmptyStackException if stack is empty
	 */
	public String format(String input) throws EmptyStackException
	{		
			String str="";
			char ch;
			int len=input.length();
			
			for (int i=0; i<len; i++)//read char by char from given string and append to a new string
			{
				ch=input.charAt(i);
				str+=ch;
				//--------------------------------------------------------------------------------------------
				// if char == '{' 
				if(ch==BlockType.LBRACE.getValue())
				{	
					stack.push(BlockType.LBRACE);
					str=str+'\n';
					indentLevel++;
					for(int j=0; j<indentLevel;j++)
					{
						str=str+"  ";
					}
				}
				//--------------------------------------------------------------------------------------------
				//	if char == '('
					else if(ch==BlockType.LPAREN.getValue())
					{
						stack.push(BlockType.LPAREN);	
					}
				//--------------------------------------------------------------------------------------------
				//	if char == ';'
							else if(ch==';')
							{	
								if(stack.isEmpty())
									str+='\n';
								else 
								{
									if(stack.peek()==BlockType.LBRACE)
										str+='\n';
									else		
									{	
											str=str+'\n';
									}
								}
								if(input.charAt(i+1)=='}')
								{	
									indentLevel--;
									for(int j=indentLevel;j>0; j--)
									{
										str=str+"  ";
									}	
								}
								else 
								{
									for(int j=indentLevel;j>0; j--)
									{
										str=str+"  ";
									}
								}
							}
				//--------------------------------------------------------------------------------------------
				//	if char == ')'
								else if(ch==')')
								{		
									if(input.charAt(i+1)==BlockType.LBRACE.getValue())
									{
										indentLevel++;
										str=str+'\n';
										for(int j=0; j<indentLevel; j++)
										{
											str=str+"  ";
										}
									}
									if(input.charAt(i+1)=='}')
									{
										str=str+'\n';
										indentLevel-=1;
										
										for(int j=indentLevel; j>0; j--)
										{
											str=str+"  ";
										}
										indentLevel--;
									}
									 if(stack.isEmpty()){
										System.out.println("Extra closed parenthese");break;}
									 try
									 {
										 if(!stack.isEmpty())
											{
												BlockType p1=stack.peek();
												if(p1==BlockType.LPAREN)
												{
													stack.pop();
												}
												else
													if(p1==BlockType.LBRACE)
													{
														stack.pop();
														System.out.println("Extra parenthese!\n");
														break;
													}
											}
									 }catch (EmptyStackException e){System.out.println("stack is empty! ");}
								}
				//--------------------------------------------------------------------------------------------
				//	if char == '}'
									else if(ch=='}')
									{		
											str+='\n';
											if(input.charAt(i+1)=='}'){
												indentLevel--;
												for(int j=indentLevel;j>0; j--)
												{
													str=str+"  ";
												}
												
											}
											
											if(stack.isEmpty()){
												System.out.println("extra brace!\n ");break;}
											try
											{
												if(!stack.isEmpty()) 
												{
													BlockType p2=stack.peek();
													if(p2==BlockType.LBRACE)
														{
															stack.pop();
														}
													else
													if(p2==BlockType.LPAREN)	
															stack.pop();
												}
											}catch (EmptyStackException e){System.out.println("stack is empty! ");}
									}
				
			}
			
			if(!stack.isEmpty() && stack.peek()==BlockType.LBRACE)// check the missing brace
				System.out.println("missing close brack!\n");
			if(!stack.isEmpty() && stack.peek()==BlockType.LPAREN)// check the missing parentheses
				System.out.println("missing close parenthese!\n");
			return str;	
	}
}
