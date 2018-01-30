package hw4;
/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 4
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 */

import java.util.Scanner;

/**
 * this is a driver class, and run the download schedule, and show every timestep
 * @author ASUS
 *
 */
public class DownloadManager {

	public static void main(String[] args) {
		System.out.println("Hello and welcome to the Download Scheduler");
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter a number of servers: ");
		int server=input.nextInt();
		System.out.println("Please enter a download speed: ");
		int speed= input.nextInt();
		System.out.println("Please enter a length of time: ");
		int length=input.nextInt();
		System.out.println("Please enter a probability of new premium job per timestep: ");
		double premiumProb=input.nextDouble();
		System.out.println("Please enter a probability of new regular job per timestep: ");
		double regularProb=input.nextDouble();
		DownloadRandomizer random = new DownloadRandomizer(premiumProb,regularProb);
		DownloadScheduler scheduler = new DownloadScheduler();
		String str="";
		try {
			System.out.println("------------------------------Simulation Starting--------------------------------");
			str=scheduler.simulate(server,speed,length,premiumProb, regularProb);
			System.out.println(str);
			System.out.println("------------------------------Thank You For Running The Simulator---------------------------");
		} catch (EmptyQueueException e){}
	}

}
