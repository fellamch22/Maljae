package fr.univparis.maljae;

import java.util.regex.*;
import java.util.concurrent.ThreadLocalRandom;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import org.apache.commons.io.FileUtils;
import org.json.*;

/*** This module stores tasks assignment. */
public class Assignment {

    /** For each team, there must be exactly one task. */
    public static HashMap<Team, Task> team_tasks = new HashMap<Team, Task> ();

    /** We must provide a trace of the assignment algorithm. */
    /** trace restructured to an arrayList **/
    private static ArrayList<String> trace = new ArrayList<>();

	/** Returns a string representation for the trace. */ //-->return objet trace
    public static String getTrace () { 
    	String s = "";
    	for ( String e : trace) s += e ;
    	return s; 
    	}

	public static void addTraceStep (String s) { trace.add( s + "\n"); }

    /** initial Order : first step of assignment Algorithm 
     *  Returns ArrayList of student ordered by the smallest email 
     *  adress of their members **/
    
    public static ArrayList<Team> ordreInitial(){
    	Team t ;
    	int i = 1 ;
    	ArrayList <Team> l = new ArrayList <>() ;
    	l.addAll(Teams.getTeams());
    	
    	// Trier les equipe selon la plus petite adresse mail dans chaque equipe
    	Collections.sort(l , new Comparator<Team>() {

    		public int compare(Team s1, Team s2) {
    			return s1.plusPetiteAdr().compareTo(s2.plusPetiteAdr());
    		}
    		}
    			);
         

       return l ;
    }
    
    /** echange deux elements du arrayList de la position i et j **/
    
    public static void exchange (ArrayList<Team> l , int i , int j) {
    	    Team tmp ;
    	    
    	    if(i < l.size() && j < l.size()) {
    	    	tmp = l.get(i) ;
    	    	l.set(i, l.get(j));
    	    	l.set(j, tmp ) ;
    	    }else {
    	    	System.out.println(" l'index introduit depasse la taille de la liste ");
    	    }
    	    
    	
    }
    
    /** Retourne la somme de tous les codes secrets des equipes inscrites  */
     public static int secretSomme () {
    	 int s = 0 ;
    	 Iterator<Team > it = Teams.getTeams().iterator() ;
    	 
    	 while (it.hasNext()) {
    		     
    		 s += it.next().getSecret() ;
    	 }
    	 
    	 return s ;
     }
    
    
      /**
     * Ordre de depart : Second step of assignment Algorithm 
     *  Returns ArrayList of student after making a serie of permutations 
     *  between its elements  
     */
    
    public static ArrayList<Team> ordreDepart(){
    	Team t ;
    	int j = 1 ;
    	int secretSomme = secretSomme () ;
    	int nbTeams = Teams.getTeams().size() - 1 ; // car i commence de 0 donc de 0 a k-1
    	ArrayList <Team> l =  ordreInitial() ;
    	Iterator<Team> it = l.iterator() ;
    	addTraceStep (" -----   Ordre initial   ----- ");
    	while(it.hasNext()) {
    		t = it.next();
        	addTraceStep (t.getIdentifier()+" in position "+j
        			+" email adress "
        			+t.plusPetiteAdr());
        	j ++ ;
    	}
    	addTraceStep (" -----   Ordre De Depart   ----- ");
    	for (int i = 0  ; i < l.size() ; i++) {
    		addTraceStep ( "Team in position "+(i+1)+"  Switched with team in position "
    				+(i + ((secretSomme - l.get(i).getSecret())%(nbTeams - i + 1))+1));
    		exchange(l , i , i + ((secretSomme - l.get(i).getSecret())%(nbTeams - i + 1))) ;
    		
    	}
    	
		return l;
    	
    }
  
    
     /** Check if there is still available places for this task **/
    public static boolean taskAvailable( Task k ) {
    	
    	Iterator<Entry<Team, Task>> it = team_tasks.entrySet().iterator() ;
    	int somme = 0 ;
    	
    	while(it.hasNext()) {
    		if(it.next().getValue().getIdentifier().equals(k.getIdentifier())) {
    			somme ++ ;
    		}
    	}
    	
    	return Configuration.getDefaultNbTeamsPerSubject() - somme > 0 ;
    }
    
    /** returns  the final task respecting order of prefrences 
     *  to Assign it to the team 
     */
    
    public static Task rightTask ( Team t ) {
    	 int i = 0 ;
    	 while(! taskAvailable(t.getPreferences().get(i))) {
    		 i ++ ;
    	 }
    	   
		   return  t.getPreferences().get(i) ; 

    }
    /** Final attribution algorithm **/
    
    public static void finalAssign () {
    	
    	Team t ;
    	ArrayList <Team> l = ordreDepart() ;
    	Iterator<Team> it = l.iterator() ;
    	addTraceStep (" -----   Resultats finales    ----- ");
    	while ( it.hasNext() ) {
    		t = it.next();
    		assignTask(t, rightTask (t)) ;
    	
    	}
    		
    }
    
    
    public static void assignTask (Team team, Task task) {

	if (team_tasks.containsKey (team)) {
	    throw new RuntimeException ("Team " + team.getIdentifier ()
					+ " already has a task.");
	}
	team_tasks.put (team, task);
	addTraceStep (team.getIdentifier ()
		      + " is assigned task "
		      + task.getIdentifier ());
    }

	public static String show () {
	String description = "";
	description += team_tasks.toString () + "\n";
	description += getTrace();
	return description;
    }

	public static void loadFrom (File f) throws IOException {
	JSONObject json = new JSONObject (FileUtils.readFileToString (f, "utf-8"));
	// retructure trace object in json file
	JSONArray trace_json = json.getJSONArray("trace") ;
	for (int i = 0; i < trace_json.length (); i++) {
		trace.add(trace_json.getString(i)) ;
	}
	JSONArray assignment_json = json.getJSONArray ("assignment");
	for (int i = 0; i < assignment_json.length (); i++) {
	    JSONArray team_task = assignment_json.getJSONArray (i);
	    Team team = Teams.getTeam (team_task.getString (0));
	    Task task = Configuration.getTask (team_task.getString (1));
	    assignTask (team, task);
	}
    }

	public static void saveTo (File f) throws IOException {
	JSONObject json = new JSONObject ();
	// restructure trace as a JsonArray Object
	JSONArray trace_json = new JSONArray ();
	for (String tr : trace ) {
		trace_json.put(tr) ;
	}
	json.put("trace", trace_json );
	JSONArray assignment_json = new JSONArray ();
	for (HashMap.Entry<Team, Task> assignment : team_tasks.entrySet ()) {
	    JSONArray team_task = new JSONArray ();
	    team_task.put (assignment.getKey ().getIdentifier ());
	    team_task.put (assignment.getValue ().getIdentifier ());
	    // this line was missing tasks assigned to teams were not saved
	    assignment_json.put(team_task) ;
	}
	json.put ("assignment", assignment_json);
	FileWriter fw = new FileWriter (f);
	fw.write (json.toString (2));
	fw.close ();
    }

}
