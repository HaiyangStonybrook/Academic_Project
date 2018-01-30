package hw7;

/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 7
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 **/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import big.data.DataSource;

/**
 * This class holds information of movie, including title, year and actor 
 *
 */
public class Movie {
	private String title;
	private int year;
	private ArrayList<Actor> actor;
	
	/**
	 * The constructor with two parameter
	 * @param title, a String value
	 * @param year, a int value
	 */
	public Movie(String title,int year){
		this.title=title;
		this.year=year;
		actor=new ArrayList<Actor>();
	}

	/**
	 * The constructor with one parameter
	 * @param url, a String value
	 */
	public Movie(String url){
		actor=new ArrayList<Actor>();
		//try{
			if(url.length()>0){
				DataSource ds=DataSource.connectXML(url);
				ds.load();
				setTitle(ds.fetchString("movie/title"));
				setYear(ds.fetchInt("movie/year"));
				String actors=ds.fetchString("movie/actors");
				String [] list=actors.split(",");
				for(int i=0; i<list.length; i++){
					Actor a=new Actor(list[i].trim());
					actor.add(a);
				}
				setActor(actor);
			}
		}
	
	/**
	 * The getter method of title
	 * @return a String value
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * The setter method of title
	 * @param title, a String value
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * The getter method of year
	 * @return a int value
	 */
	public int getYear() {
		return year;
	}

	/**
	 * The setter method of year
	 * @param year, an int value
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * The getter method of actor
	 * @return an ArrayList<Actor> value
	 */
	public ArrayList<Actor> getActor() {
		return actor;
	}

	/**
	 * The setter method of actor
	 * @param actor , an ArrayList<Actor> value
	 */
	public void setActor(ArrayList<Actor> actor) {
		this.actor = actor;
	}
	
	/**
	 * Print the detailed information of the movie 
	 * @return a String value
	 */
	public String moviePrint(){
		String name="";
		String title=this.getTitle();
		if(title.length()>28){
			title=title.substring(0, 27);
			title+="...";
		}
		for(Actor a:actor){
			name+=a.getName()+", ";
		}
		if(name.length()>30){
			name=name.substring(0, 30);
			name+="...";
		}
		return String.format(("%-30s%-10s%s"), title, this.getYear(), name);
	}
	
}
