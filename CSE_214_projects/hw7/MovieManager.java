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
import java.util.Collections;

/**
 * This class holds information of movie and actor
 *
 */
public class MovieManager {
	private ArrayList<Movie> movie;
	private ArrayList<Actor> actor;
	
	/**
	 * Constructor without parameter
	 */
	public MovieManager(){
		movie=new ArrayList<Movie>();
		actor=new ArrayList<Actor>();
	}
	
	/**
	 * Getter method of movie
	 * @return an ArrayList<Movie> value
	 */
	public ArrayList<Movie> getMovies(){
		return movie;
	}
	
	/**
	 * Getter method of actor
	 * @return an ArrayList<Actor> value
	 */
	public ArrayList<Actor> getActors(){
		return actor;
	}
	
	/**
	 * remove duplicated actors in ArrayList<Actor>
	 */
	public void removeDupli(){

		for(int i=0; i<getActors().size(); i++){
			for(int j=i+1; j<getActors().size();j++){
			String a=getActors().get(i).getName();
			String b=getActors().get(j).getName();
			if(a.equals(b)){
				getActors().remove(j);
				getActors().get(i).setCount(getActors().get(i).getCount()+1);
				//int k=getActors().get(i-1).getCount();
			}
			}
		}
	}
	
	/**
	 * get sorted movies by title
	 * @param comp, a TitleComparator value
	 * @return an ArrayList<Movie> value
	 */
	public ArrayList<Movie> getSortedMoviesByTitle(TitleComparator comp){
		Collections.sort(movie, comp);
		return movie;
	}
	
	/**
	 * get reversed sorted movies by title
	 * @param comp, a TitleComparator value
	 * @return an ArrayList<Movie> value
	 */
	public ArrayList<Movie> getReverseMoviesByTitle(TitleComparator comp){
		Collections.sort(movie, comp.reversed());
		return movie;
	}
	
	/**
	 * get sorted movies by year
	 * @param comp, a YearComparator value
	 * @return an ArrayList<Movie> value
	 */
	public ArrayList<Movie> getSortedMoviesByYear(YearComparator comp){
		Collections.sort(movie, comp);
		return movie;
	}
	
	/**
	 * get reversed sorted movies by year
	 * @param comp, a YearComparator value
	 * @return an ArrayList<Movie> value
	 */
	public ArrayList<Movie> getReverseMoviesByYear(YearComparator comp){
		Collections.sort(movie, comp.reversed());
		return movie;
	}
	
	/**
	 * get sorted actors by name
	 * @param comp, a NameComparator value
	 * @return an ArrayList<Actor> value
	 */
	public ArrayList<Actor> getSortedActorsByName(NameComparator comp){
		Collections.sort(actor, comp);
		return actor;
	}
	
	/**
	 * get reversed sorted actors by name
	 * @param comp, a NameComparator value
	 * @return an ArrayList<Actor> value
	 */
	public ArrayList<Actor> getReverseActorsByName(NameComparator comp){
		Collections.sort(actor, comp.reversed());
		return actor;
	}
	
	/**
	 * get sorted actors by count
	 * @param comp, a CountComparator value
	 * @return an ArrayList<Actor> value
	 */
	public ArrayList<Actor> getSortedActorsByCount(CountComparator comp){
		Collections.sort(actor, comp);
		return actor;
	}
	
	/**
	 * get reversed sorted actors by count
	 * @param comp, a CountComparator value
	 * @return an ArrayList<Actor> value
	 */
	public ArrayList<Actor> getReverseActorsByCount(CountComparator comp){
		Collections.sort(actor, comp.reversed());
		return actor;
	}
	
	/**
	 * Print the head title of movie
	 * @return a String value
	 */
	public String movieFommart(){
		return String.format(("%-30s%-10s%s"), "Title", "Year", "Actors");
	}
	
	/**
	 * Print  the head title of actor
	 * @return a String value
	 */
	public String actorFommart(){
		return String.format(("%-30s%-10s"), "Actor", "Number of Movies");
	}
	
	/**
	 * Print Sorted movies
	 * @return a String value
	 */
	public String movieSort(){
		String str="";
		for(Movie m: movie){
			String movie="";
			movie=m.moviePrint()+"\n";
			str+=movie;
		}
		return str;
	}
	
	/**
	 * Print Sorted actors
	 * @return a String value
	 */
	public String actorSort(){
		String str="";
		
			for(Actor a: actor){
				String actor="";
				actor=a.actorPrint()+"\n";
				str+=actor;
			}
			return str;
	}
	
}
