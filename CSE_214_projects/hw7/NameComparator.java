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
 * This class is to compare two actors by name implementing Comparator
 *
 */
public class NameComparator implements Comparator<Actor> {

	/**
	 * compare two actors by name
	 */
	public int compare(Actor o1, Actor o2) {
		return o1.getName().compareToIgnoreCase(o2.getName());
	}
	
}
