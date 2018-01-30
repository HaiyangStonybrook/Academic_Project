package hw4;

/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 4
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 */

import java.util.LinkedList;

/**
 * 
 * This class construct the queue class implemented by LinkedList .
 * 
 * @author Haiyang Liu
 *
 */
public class DownloadQueue extends LinkedList<DownloadJob> {
	/**
	 * return true if the queue is empty
	 * 
	 * @return a boolean value
	 */
	public boolean isEmpty() {
		return (size() == 0);
	}

	/**
	 * add a node to the last of the queue
	 * 
	 * @param d
	 *            a DownloadJob value
	 */
	public void enqueue(DownloadJob d) {
		addLast(d);
	}

	/**
	 * return and remove the first node of the queue
	 * 
	 * @return a DownloadJob value
	 * @throws EmptyQueueException
	 *             if the queue is empty
	 */
	public DownloadJob dequeue() throws EmptyQueueException {
		if (isEmpty())
			throw new EmptyQueueException("The Queue is Empty");
		else {
			DownloadJob temp = getFirst();
			removeFirst();
			return temp;
		}
	}

	/**
	 * return the first not of the queue
	 * 
	 * @return a DownloadJob value
	 */
	public DownloadJob peek() {
		return getFirst();
	}

	/**
	 * @override override the toString(), print the nodes inside the queue
	 * @return a String value
	 */
	public String toString() {
		String str = "";
		for (int i = 0; i < this.size(); i++) {
			str += "[#" + this.get(i).getId() + ":" + this.get(i).getDownloadSize() + "Mb" + "]";
		}
		return str;
	}
}
