package hw4;

/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 4
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 */

/**
 * This class holds DownloadJob data including a downloadSize,
 * downloadSizeRemaining, timeRequested, isPremium and id
 * 
 * @author ASUS
 *
 */
public class DownloadJob {
	/**
	 * the downloadSize of DownloadJob
	 */
	private int downloadSize;
	/**
	 * the downloadSizeRemaining of DownloadJob
	 */
	private int downloadSizeRemaining;
	/**
	 * the timeRequested of DownloadJob
	 */
	private int timeRequested;
	/**
	 * the isPremium of DownloadJob
	 */
	private boolean isPremium;
	/**
	 * the id of DownloadJob
	 */
	private int id;

	/**
	 * Constructor of DownloadJob with no param
	 */
	public DownloadJob() {
		id = 1;
		downloadSize = 0;
		downloadSizeRemaining = 0;
		timeRequested = 0;
		isPremium = true;
	}

	/**
	 * return downloadSize of DownloadJob
	 * 
	 * @return a int value
	 */
	public int getDownloadSize() {
		return downloadSize;
	}

	/**
	 * Sets the downloadSize private field to the one passed as a parameter
	 * 
	 * @param downloadSize
	 */
	public void setDownloadSize(int downloadSize) {
		this.downloadSize = downloadSize;
	}

	/**
	 * return downloadSizeRemaining of DownloadJob
	 * 
	 * @return a int value
	 */
	public int getDownloadSizeRemaining() {
		return downloadSizeRemaining;
	}

	/**
	 * Sets the downloadSizeRemaining private field to the one passed as a
	 * parameter
	 * 
	 * @param downloadSizeRemaining
	 */
	public void setDownloadSizeRemaining(int downloadSizeRemaining) {
		this.downloadSizeRemaining = downloadSizeRemaining;
	}

	/**
	 * return timeRequested of DownloadJob
	 * 
	 * @return int value
	 */
	public int getTimeRequested() {
		return timeRequested;
	}

	/**
	 * Sets the timeRequested private field to the one passed as a parameter
	 * 
	 * @param timeRequested
	 */
	public void setTimeRequested(int timeRequested) {
		this.timeRequested = timeRequested;
	}

	/**
	 * return isPremium of DownloadJob
	 * 
	 * @return boolean value
	 */
	public boolean isPremium() {
		return isPremium;
	}

	/**
	 * Sets the isPremium private field to the one passed as a parameter
	 * 
	 * @param isPremium
	 */
	public void setPremium(boolean isPremium) {
		this.isPremium = isPremium;
	}

	/**
	 * return the job is premium or regular
	 * 
	 * @return a String value
	 */
	public String type() {
		String str = "";
		if (isPremium)
			str = "Premium";
		else
			str = "Regular";
		return str;
	}

	/**
	 * return the id of DownloadJob
	 * 
	 * @return a int value
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id private field to the one passed as a parameter
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

}