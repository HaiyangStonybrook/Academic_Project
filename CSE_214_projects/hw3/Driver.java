package hw3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Driver {

	public static void main(String[] args) 
	{
		Formatter f=new Formatter();
		StringBuilder sb= new StringBuilder();
		System.out.println("Welcome to the File Formatter.");
		System.out.println("Please Enter a filename: ");
		
		BufferedReader br = null;
		long length=0;
		String input;
		//Scanner input =new Scanner(System.in);
		//String filename=input.nextLine();
		
		try
		{
			String cLine;
			br=new BufferedReader(new FileReader("E:\\Javaworkspace\\CSE214\\src\\hw3\\1.txt"));
			cLine=br.readLine();
			while(cLine!=null)
			{
				cLine=cLine.replaceAll("\t", "");
				cLine=cLine.replaceAll("\n", "");
				cLine=cLine.replaceAll(" ", "");
				sb.append(cLine);
				cLine=br.readLine();
			}
			input=sb.toString()+" ";
			System.out.println("Your Scource file:");
            System.out.println(input);
            System.out.println("\n");
			String s=f.format(input);
			System.out.println("-------------------Properly Formatted Program------------------- \n" +s);
		}
		catch (IOException e){ System.out.println("Trouble formatting"); }
	
		System.out.println("--------------Thank you for making code readable!--------------- \n");
	}

}
