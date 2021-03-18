package fr.univparis.maljae;

import java.util.StringTokenizer;
import java.util.regex.*;
import java.util.concurrent.ThreadLocalRandom;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import org.apache.commons.io.FileUtils;
import org.json.*;

/*** The team of students. */

public class Team  implements Comparable {


    private String    identifier;
    public String     getIdentifier () { return identifier; }

    private ArrayList<Task>    preferences;
    public ArrayList<Task>     getPreferences () { return preferences; }
    private Boolean[] emploiDuTemps = new Boolean[14]; //l'emploiDuTemps
    private ArrayList<Student> students;
    private Integer            secret;
    public Integer getSecret () { return secret; }
    public void updateSecretFromString (String s) {
	secret = Integer.parseInt (s);
    }

     
    Team (String id){
    	students = new ArrayList<Student> ();
    	this.identifier = id ;
    }
    
    //LiFang Update : add the name of team
    Team (Student creator,String teamnam,Boolean[] emploiDuTemps) {
    	this.emploiDuTemps=emploiDuTemps;
    	if (teamnam.equals("") ) {
    		identifier = generateRandomTeamIdentifier ();
    	}
    	else {
    		identifier=teamnam;
    		} 
    //end of modify
    preferences = new ArrayList<Task> (Configuration.getTasks ());
    students = new ArrayList<Student> (Configuration.getMaxNbUsersPerTeam ());
	students.add (creator);
	secret = ThreadLocalRandom.current().nextInt(10, 100);
    }
    @Override
	public boolean equals(Object obj) {
		
		return this.getIdentifier().equals(((Team)obj).getIdentifier());
	}
    
   
    
	// methode qui recupere l'identifiant de l'equipe a partir de l'objet json
    private void readIdentifier (File f , JSONObject json ) {
    	identifier = json.getString ("identifier");
    	if (! f.getName ().equals (identifier + "-team.json")) {
    	    throw new RuntimeException ("Inconsistency in the data model: " + f.getName ());
    	}
    }
    
    private void readSecret (JSONObject json ) {
    	secret     = json.getInt ("secret");
    }
    
    private void readPreferences (JSONObject json) {
    	
    	JSONArray preferences_json = json.getJSONArray ("preferences");
    	preferences = new ArrayList<Task> (preferences_json.length ());
    	for (int i = 0; i < preferences_json.length (); i++) {
    	    String pid = preferences_json.getString (i);
    	    preferences.add (i, Configuration.getTask (pid));
    	}
    }
       
    private void readStudents (JSONObject json) {

    	JSONArray students_json = json.getJSONArray ("students");
    	students = new ArrayList<Student> (students_json.length ());
    	for (int i = 0; i < students_json.length (); i++)
    	    students.add (i, new Student (students_json.getJSONObject (i)));
    	
    }
    
	private void readEDT (JSONObject json) {
    	JSONArray edt_json = json.getJSONArray ("emploiDuTemps");
    	for (int i = 0; i < 14; i++) {
    		emploiDuTemps[i] = null;
    		if ( ! edt_json.get(i).equals(null)) {
    			emploiDuTemps[i] = true ;
    		} 
    	}
    }
	
    /* FIXME: The following code is ugly! */
    /* FIXED: THE FOLLOWING CODE IS NO MORE UGLY */
    public Team (File f) throws IOException {
	JSONObject json = new JSONObject (FileUtils.readFileToString (f, "utf-8"));
	
	readIdentifier(f,json);
	readSecret ( json );
	readPreferences( json );
	readStudents( json );	
	readEDT ( json );
    }
    
    public void writeIdentifier(JSONObject json) {
    	
    	json.put ("identifier", identifier);
    	
    }
    
    public void writeSecret(JSONObject json) {
    	
    	json.put ("secret", secret);

    }
    public  void writePrferences(JSONObject json ,JSONArray preferences_json) {
    	
    	for (int i = 0; i < preferences.size (); i++) {
    	    if (preferences.get (i) != null) {
    		preferences_json.put (preferences.get (i).getIdentifier ());
    	    }
    	}
    	json.put ("preferences", preferences_json);
    	
    }
    
    public void writeStudents (JSONObject json , JSONArray students_json ) {
    	
    	for (int i = 0; i < students.size (); i++) {
    	    students_json.put (students.get (i).toJSON ());
    	}
    	json.put ("students", students_json);
    }
    
    public void writeEDT (JSONObject json , JSONArray edt_json ) {
    	
    	for (int i = 0; i < 14; i++) {
    		edt_json.put (emploiDuTemps[i]);
    	}
    	json.put ("emploiDuTemps", edt_json);
    }
    

    /* FIXED*/
  
    public void saveTo (File f) throws IOException {
	JSONObject json = new JSONObject ();
	
	writeIdentifier(json ) ;
	writeSecret( json ) ; 
	
	JSONArray preferences_json = new JSONArray ();
	writePrferences(json , preferences_json);
	
	JSONArray students_json = new JSONArray ();
	writeStudents( json , students_json ) ;

	JSONArray edt_json = new JSONArray ();
	writeEDT( json , edt_json ) ;

	FileWriter fw = new FileWriter (f);
	fw.write (json.toString (2));
	fw.close ();
	
    }

    public String preferencesToString () {
	String result = "";
	for (int i = 0; i < preferences.size (); i++) {
		//debug
		//System.out.println(">>> "+preferences.get(i).getIdentifier ());
	    result += preferences.get (i).getIdentifier () + ";";
	}
	return result;
    }

    public void updatePreferencesFromString (String s) {
	System.out.println ("Prefs: " + s);
	String[] fields = s.split (";");
	ArrayList<Task> newPreferences = new ArrayList<Task> ();
	for (int i = 0; i < fields.length; i++) {
	    newPreferences.add (Configuration.getTask (fields[i]));
	}
	
	// FIXME: We should check that newPreferences is a permutation
	// FIXME: of all task identifiers.
	this.preferences = newPreferences;
    }

    public String studentsToString () {
	String result = "";
	for (int i = 0; i < students.size (); i++) {
	    result += students.get (i).toString () + ";";
	}
	return result;
    }

    public void updateStudentsFromString (String who, String s) {
	System.out.println (who + " " + s);
	String[] fields = s.split (";");
	ArrayList<Student> newStudents = new ArrayList<Student> ();
	for (int i = 0; i < fields.length; i++) {
	    newStudents.add (Student.fromString (fields[i]));
	}
	// FIXME: We should check that [who] did not change the status of
	// FIXME: other team members.
	this.students = newStudents;
    }

    public String toString () {
	String description = "\n" +identifier + "\n";
	description += preferencesToString () + "\n";
	description += studentsToString () + "\n";
	description += "secret:" + secret+ "\n";
	description += "EDT : ";
	for(int i=0;i<14;i++) {
		description += emploiDuTemps[i] + " ";
	}
	return description;
    }
//Update
    public ArrayList<String> getTeamMails() {
    	   ArrayList<String> A=new ArrayList<String>();
    	   Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(studentsToString());
    	    while (m.find()) {
    	        System.out.println(m.group());
    	        A.add(m.group());
    	    }
    	    return A;
    }
// end of modification
    
    private static String generateRandomTeamIdentifier () {
	return "maljae" + ThreadLocalRandom.current().nextInt(10000, Integer.MAX_VALUE);
    }

    public static boolean isValidTeamFileName (String fname) {
	Pattern p = Pattern.compile (".*-team.json");
	Matcher m = p.matcher (fname);
	return m.find ();
    }

    //Adding get team name - required for duplicate team name check
    public String getTeamName() {
    	return this.identifier;
    }
    //End of FixMe - Duplicated team name check
    
    public void removeStudent (String email) {
	Student found = null;
	for (Student student : students) {
	    if (student.getEmail ().equals (email)) {
		found = student;
		break;
	    }
	}
	if (found != null)
	    students.remove (found);
    }
	
    /** return the smallest email adress between all team members adresses **/
    public String plusPetiteAdr() {
    	Iterator<Student> it = this.students.iterator();
    	String s = "";
    	Student m ;
    	if(this.students != null ) {
    		s = this.students.get(0).getEmail();  
    	}else {
    		return s;
    	}
    	
    	while( it.hasNext() ) {
    		m = it.next() ;
    		
    		if ( m.getEmail().compareTo(s) < 0) {
    			s = m.getEmail() ;
    		}
    	}
    	return s ;
      }
   
  
	@Override
	/** Comparing Teams according to their identifier **/
	public int compareTo(Object o) {
		
		return this.getIdentifier().compareTo(((Team)o).getIdentifier());
	}
	
	//l'emploi du temps
		public String[] getEmploiDuTemps() { 
			String emploiDuTempsString []= new String[14];
					for(int i=0;i<14;i++) {
						if(this.emploiDuTemps[i] != null) { 
							emploiDuTempsString[i] = "checked" ;
							} 
						else emploiDuTempsString[i] = "";
					}
			return emploiDuTempsString;
		}
		
		public void updateEdt(String lam,String lpm,String mam,String mpm,String meam,String mepm,String jam,String jpm,
				String vam,String vpm,String sam,String spm,String dam,String dpm) {
			if(lam == null) { emploiDuTemps[0]= null; } else {emploiDuTemps[0]= true;}
			if(lpm == null) { emploiDuTemps[1]= null; } else {emploiDuTemps[1]= true;}
			if(mam == null) { emploiDuTemps[2]= null; } else {emploiDuTemps[2]= true;}
			if(mpm == null) { emploiDuTemps[3]= null; } else {emploiDuTemps[3]= true;}
			if(meam == null) { emploiDuTemps[4]= null; } else {emploiDuTemps[4]= true;}
			if(mepm == null) { emploiDuTemps[5]= null; } else {emploiDuTemps[5]= true;}
			if(jam == null) { emploiDuTemps[6]= null; } else {emploiDuTemps[6]= true;}
			if(jpm == null) { emploiDuTemps[7]= null; } else {emploiDuTemps[7]= true;}
			if(vam == null) { emploiDuTemps[8]= null; } else {emploiDuTemps[8]= true;}
			if(vpm == null) { emploiDuTemps[9]= null; } else {emploiDuTemps[9]= true;}
			if(sam == null) { emploiDuTemps[10]= null; } else {emploiDuTemps[10]= true;}
			if(spm == null) { emploiDuTemps[11]= null; } else {emploiDuTemps[11]= true;}
			if(dam == null) { emploiDuTemps[12]= null; } else {emploiDuTemps[12]= true;}
			if(dpm == null) { emploiDuTemps[13]= null; } else {emploiDuTemps[13]= true;}	
		}


}
