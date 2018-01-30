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
 * This class is to compare two actors by count implementing Comparator
 *
 */
public class CountComparator implements Comparator<Actor> {

	/**
	 * compare two actors by count
	 */
	public int compare(Actor o1, Actor o2) {
		if(o1.getCount()<o2.getCount())
			return -1;
		else if(o1.getCount()>o2.getCount())
			return 1;
			else
				return 0;
	}

}
