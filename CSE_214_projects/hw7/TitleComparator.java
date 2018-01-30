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
 * This class is to compare two movies by title implementing Comparator
 *
 */
public class TitleComparator implements Comparator<Movie>{

	/**
	 * compare two movie by title
	 */
	public int compare(Movie o1, Movie o2) {
		return o1.getTitle().compareToIgnoreCase(o2.getTitle());
	}
}
