package hw7;

/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 7
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 **/

import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * This is a drive class which runs the program
 *
 */
public class ASMDB {
	private static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) {
		JOptionPane.showMessageDialog(null,"Welcome to A Smiley Movie Data Base, the happiest way to manage your DVDs.");
		MovieManager manager=new MovieManager();
		boolean running=true;
		
		while(running){
			String in=JOptionPane.showInputDialog("I) Import a Movie\n"
											+"D) Delete a Movie\n"
											+"A) Sort Actors\n"
											+"M) Sort Movies\n"
											+"Q) Quit\n"
											+"Please select an option: \n");
			in=in.toLowerCase();
			switch(in){
			
			//import a movie
			case "i":
					String prefix= "http://www.omdbapi.com/?t=";
					String postfix="&y=&plot=short&r=xml";
					String mName=JOptionPane.showInputDialog("Please enter a movie title: \n");
					String title=mName.replace(' ','+');
					String url=prefix+title+postfix;
					try{
						Movie movie=new Movie(url);
							manager.getMovies().add(movie);
							for(Actor a: movie.getActor()){
								manager.getActors().add(a);
							}
							System.out.println("movie imported.\n");
					}catch(big.data.DataInstantiationException e){System.out.println("movie not found. \n");}
					 catch(big.data.DataSourceException e1){System.out.println("movie not found.\n");}			
			break;
			//delete a movie
			case"d":
				String rName=JOptionPane.showInputDialog("Please enter the movie title to be deleted: \n");
				boolean find=false;
				for(int i=0; i<manager.getMovies().size(); i++){
					if(rName.equalsIgnoreCase(manager.getMovies().get(i).getTitle())){
						find=true;
						for(Actor a: manager.getMovies().get(i).getActor()){
							if(a.getCount()==1)
								manager.getActors().remove(a);
							if(a.getCount()>1)
								a.setCount(a.getCount()-1);
						}
						for(Actor b:manager.getActors()){
							if(b.getCount()>1)
								b.setCount(b.getCount()-1);
						}
						manager.getMovies().remove(i);
						System.out.println(rName+" deleted.\n");
					}
				}
				if (find==false)
					System.out.println("Movie not found.\n");	
			break;
			
			//sort actors
			case"a":
				String in1=JOptionPane.showInputDialog("AA) Alphabetically Ascending(A-Z)\n"
						+ "AD) Alphabetically Descending(Z-A)\n"
						+"NA) By Number of Movies They Are In Ascending\n"
						+"ND) By Number of Movies They Are In Descending\n"
						+"Please Select A Sort method:");
				in1=in1.toLowerCase();
				switch(in1){

					case "aa":
						NameComparator nameComp=new NameComparator();
						manager.getSortedActorsByName(nameComp);
						manager.removeDupli();
						manager.getSortedActorsByName(nameComp);
						System.out.println(manager.actorFommart());
						System.out.println("-----------------------------------------------------------------");
						System.out.println(manager.actorSort());
					break;
					
					case "ad":
						NameComparator nameCompReverse=new NameComparator();
						manager.getReverseActorsByName(nameCompReverse);
						manager.removeDupli();
						manager.getReverseActorsByName(nameCompReverse);
						System.out.println(manager.actorFommart());
						System.out.println("-----------------------------------------------------------------");
						System.out.println(manager.actorSort());
					break;
					
					case "na":
						CountComparator countComp=new CountComparator();
						manager.getSortedActorsByCount(countComp);
						manager.removeDupli();
						manager.getSortedActorsByCount(countComp);
						System.out.println(manager.actorFommart());
						System.out.println("-----------------------------------------------------------------");
						System.out.println(manager.actorSort());
					break;
					
					case "nd":
						CountComparator countCompReverse=new CountComparator();
						manager.getReverseActorsByCount(countCompReverse);
						manager.removeDupli();
						manager.getReverseActorsByCount(countCompReverse);
						System.out.println(manager.actorFommart());
						System.out.println("-----------------------------------------------------------------");
						System.out.println(manager.actorSort());
					break;
					
					default:
						System.out.println(in1+" is not an option");
				}	
			break;
			
			//sorts movies
			case "m":
				manager.removeDupli();
				String in2=JOptionPane.showInputDialog("TA) Title Ascending(A-Z)\n"
														+"TD) Title Descending(Z-A)\n"
														+"YA) Year Ascending\n"
														+"YD) Year Descending\n"
														+"Please Select M Sort method: \n");
				in2=in2.toLowerCase();
				switch(in2){
					case "ta":
						TitleComparator titleComp=new TitleComparator();
						manager.getSortedMoviesByTitle(titleComp);
						System.out.println(manager.movieFommart());
						System.out.println("-----------------------------------------------------------------");
						System.out.println(manager.movieSort());
					break;
					
					case "td":
						TitleComparator titleCompReverse=new TitleComparator();
						manager.getReverseMoviesByTitle(titleCompReverse);
						System.out.println(manager.movieFommart());
						System.out.println("-----------------------------------------------------------------");
						System.out.println(manager.movieSort());
					break;
					
					case "ya":
						YearComparator yearComp=new YearComparator();
						manager.getSortedMoviesByYear(yearComp);
						System.out.println(manager.movieFommart());
						System.out.println("-----------------------------------------------------------------");
						System.out.println(manager.movieSort());
					break;
					
					case "yd":
						YearComparator yearCompReverse=new YearComparator();
						manager.getReverseMoviesByYear(yearCompReverse);
						System.out.println(manager.movieFommart());
						System.out.println("-----------------------------------------------------------------");
						System.out.println(manager.movieSort());
					break;
					
					default:
						System.out.println(in2+" is not an option");
				}
			break;
			
			case"q":
				JOptionPane.showMessageDialog(null,"That's All Folks!");
				scan.close();
				running=false;
			break;
			
			default:
				JOptionPane.showMessageDialog(null,in+ " is not an option");
			}
		}
	}

}
