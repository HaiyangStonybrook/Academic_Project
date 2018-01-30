package hw7;

/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 7
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 **/

import java.util.Comparator;

/**
 * This class is to compare two movies by year implementing Comparator
 *
 */
public class YearComparator implements Comparator<Movie>  {

	/**
	 * compare two movies by year
	 */
	public int compare(Movie o1, Movie o2) {
		if(o1.getYear()<o2.getYear())
			return -1;
		
		else if (o1.getYear()>o2.getYear())
			return 1;
		
			else
				return 0;
	}
	
}
