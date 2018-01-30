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
 * This class schedule the Download Job. New Job is enqueued to the regular
 * queue or premium queue, and is dequeued from queue to the array to be
 * downloaded
 * 
 * @author ASUS
 *
 */
public class DownloadScheduler {
	private DownloadQueue regularQ;
	private DownloadQueue premiumQ;
	private int currentTime;
	private int simulationEndTime;
	private DownloadRandomizer random;
	private DownloadJob[] CurrentJobs;
	private int downloadSpeed;
	private double p, r;

	/**
	 * the Constructor of DownloadScheduler whit no param
	 */
	public DownloadScheduler() {
		downloadSpeed = 0;
		simulationEndTime = 0;
		currentTime = 0;
	}

	/**
	 * This method do the download schedule. show the detail of each time step
	 * and the summary when it is done
	 * 
	 * @param numberOfServer
	 *            a int value, the number of server
	 * @param downloadSpeed
	 *            a int value, the speed of download
	 * @param simulationEndTime
	 *            a int value, the time range of download
	 * @param p
	 *            a double value, the probability of premium job
	 * @param r
	 *            a double value, the probability of premium job
	 * @return a string value
	 * @throws EmptyQueueException
	 *             if the queue is empty
	 */
	public String simulate(int numberOfServer, int downloadSpeed, int simulationEndTime, double p, double r)
			throws EmptyQueueException {
		int id = 1;
		int space = 0;
		int servedCounter = 0, servedPreNumber = 0, servedRegNumber = 0, totalData = 0, totalPreData = 0,
				totalRegData = 0, averagePre = 0, averageReg = 0, totalPreWaitTime = 0, totalRegWaitTime = 0;
		String s = "";
		String s2 = "";
		String s3 = "";
		String s4 = "";
		String s5 = "";
		String s6 = "";
		String s8 = "";
		CurrentJobs = new DownloadJob[numberOfServer + 1];//to make server from 1 to 3
		regularQ = new DownloadQueue();
		premiumQ = new DownloadQueue();
		random = new DownloadRandomizer(p, r);
		for (currentTime = 0; currentTime < simulationEndTime; currentTime++) {// the out for loop, which is time step
			String s1 = "\nTimestep " + currentTime + ":\n";
			String s7 = "";
			int time = currentTime;
			DownloadJob regJob = new DownloadJob();
			regJob.setDownloadSize(random.getRegular());
			if (regJob.getDownloadSize() != -1) {// check if the regular job appears
				regJob.setPremium(false);
				regJob.setId(id);
				regJob.setTimeRequested(currentTime);
				regJob.setDownloadSizeRemaining(regJob.getDownloadSize());
				regularQ.enqueue(regJob);
				id++;
				String regTyep = regJob.type();
				s2 = "New Regular Job: Job#" + regJob.getId() + ": Size: " + regJob.getDownloadSize() + "Mb\n";
				s1 += s2;
			} else
				s1 += "New Regular Job: n/a\n";
			DownloadJob preJob = new DownloadJob();
			preJob.setDownloadSize(random.getPremium());
			if (preJob.getDownloadSize() != -1) {// check if the regular job appears 
				preJob.setPremium(true);
				preJob.setId(id);
				preJob.setTimeRequested(currentTime);
				preJob.setDownloadSizeRemaining(preJob.getDownloadSize());
				premiumQ.enqueue(preJob);
				id++;
				String preTyep = preJob.type();
				s3 = "New Premium Job: Job#" + preJob.getId() + ": Size: " + preJob.getDownloadSize() + "Mb\n";
				s1 += s3;
			} else
				s1 += "New Premium Job: n/a\n";
			if (!regularQ.isEmpty()) { //check if the regular queue is empty
				s4 = regularQ.toString();
				s1 += "RegularQueue: " + s4 + "\n";
			} else
				s1 += "RegularQueue: empty\n";
			if (!premiumQ.isEmpty()) {//check if the premium queue is empty
				s5 = premiumQ.toString();
				s1 += "PremiumQueue:" + s5 + "\n";
			} else
				s1 += "PremiumQueue: empty\n";
			for (space = 1; space < CurrentJobs.length; space++) {// the inner for loop, which is the work of each server

				if (CurrentJobs[space] == null) {//check if the server is empty
					if (!premiumQ.isEmpty()) {
						try {
							CurrentJobs[space] = premiumQ.dequeue();
						} catch (EmptyQueueException e) {
							System.out.println("PremiumQ is Empty");
						}
						s6 = "Server " + space + ":[#" + CurrentJobs[space].getId() + ": "
								+ CurrentJobs[space].getDownloadSize() + "Mb total, "
								+ CurrentJobs[space].getDownloadSizeRemaining() + "Mb remaining, " + "Request Time: "
								+ CurrentJobs[space].getTimeRequested() + " " + CurrentJobs[space].type() + "]\n";
						s1 += s6;
					} // try catch
					else if (!regularQ.isEmpty()) {
						try {
							CurrentJobs[space] = regularQ.dequeue();
						} catch (EmptyQueueException e) {
							System.out.println("RegularQ is Empty");
						}
						s6 = "Server " + space + ":[#" + CurrentJobs[space].getId() + ": "
								+ CurrentJobs[space].getDownloadSize() + "Mb total, "
								+ CurrentJobs[space].getDownloadSizeRemaining() + "Mb remaining, " + "Request Time: "
								+ CurrentJobs[space].getTimeRequested() + " " + CurrentJobs[space].type() + "]\n";
						s1 += s6;
					} else {
						s6 = "Server " + space + ": idle\n";
						s1 += s6;
					}
				} else {
					CurrentJobs[space]
							.setDownloadSizeRemaining(CurrentJobs[space].getDownloadSizeRemaining() - downloadSpeed);//calculate the remaining size
					s6 = "Server " + space + ":[#" + CurrentJobs[space].getId() + ": "
							+ CurrentJobs[space].getDownloadSize() + "Mb total, "
							+ CurrentJobs[space].getDownloadSizeRemaining() + "Mb remaining, " + "Request Time: "
							+ CurrentJobs[space].getTimeRequested() + " " + CurrentJobs[space].type() + "]\n";
					if (CurrentJobs[space].getDownloadSizeRemaining() <= 0) {// the job is finished, and remove from the array
						DownloadJob temp = CurrentJobs[space];
						servedCounter++;
						totalData += temp.getDownloadSize();
						if (temp.isPremium()) {//check the finished job is premium or regular
							servedPreNumber++;
							totalPreData += temp.getDownloadSize();
							totalPreWaitTime += currentTime - temp.getTimeRequested();
						} else {
							servedRegNumber++;
							totalRegData += temp.getDownloadSize();
							totalRegWaitTime += currentTime - temp.getTimeRequested();
						}
						s7 += "Job#" + temp.getId() + " finished, " + temp.type() + " job. " + temp.getDownloadSize()
								+ "Mb served, Total wait time: " + (currentTime - temp.getTimeRequested()) + "\n";
						if (!premiumQ.isEmpty()) {//if the premium queue is not empty, add the premium job to the server
							CurrentJobs[space] = premiumQ.dequeue();
							s6 = "Server " + space + ":[#" + CurrentJobs[space].getId() + ": "
									+ CurrentJobs[space].getDownloadSize() + "Mb total, "
									+ CurrentJobs[space].getDownloadSizeRemaining() + "Mb remaining, "
									+ "Request Time: " + CurrentJobs[space].getTimeRequested() + " "
									+ CurrentJobs[space].type() + "]\n";
						} else if (!regularQ.isEmpty()) {// else if premium queue is empty and regular queue is not empty, add the regular job to the server
							CurrentJobs[space] = regularQ.dequeue();
							s6 = "Server " + space + ":[#" + CurrentJobs[space].getId() + ": "
									+ CurrentJobs[space].getDownloadSize() + "Mb total, "
									+ CurrentJobs[space].getDownloadSizeRemaining() + "Mb remaining, "
									+ "Request Time: " + CurrentJobs[space].getTimeRequested() + " "
									+ CurrentJobs[space].type() + "]\n";
						} else {
							CurrentJobs[space] = null;
							s6 = "Server " + space + ": idle\n";
						}
					}
					s1 = s1 + s6;

				}
			}
			s1 += s7;
			s = s + s1;
			s += "-----------------------------------------------------------------------";
		}
		//calculate the total size, average wait time
		if (servedPreNumber != 0)
			averagePre = totalPreWaitTime / servedPreNumber;
		else
			averagePre = -1;
		if (servedRegNumber != 0)
			averageReg = totalRegWaitTime / servedRegNumber;
		else
			averageReg = -1;
		s8 = "\nSimulation Ended:\n\tTotal Jobs served: " + servedCounter + "\n\tTotal Premium Jobs served: "
				+ servedPreNumber + "\n\tTotal Regular Jobs served: " + servedRegNumber + "\n\tTotal Data Served: "
				+ totalData + "Mb\n\tTotal Premium Data Served: " + totalPreData + "Mb\n\tTotal Regular Data Served: "
				+ totalRegData + "Mb\n\tAverage Premium Wait Time: " + averagePre + "\n\tAverage Regular Wait Time: "
				+ averageReg;
		s += s8;
		return s;
	}
}
