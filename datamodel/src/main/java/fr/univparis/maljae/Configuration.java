package fr.univparis.maljae;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

import org.apache.commons.io.FileUtils;
import org.json.*;

/**
 * This module gives access to the configuration of the maljae instance.
 *
 *
 */
public class Configuration {
	/** Version. We following semantic versioning conventions. */
	public static final String version = "0.1";
	
	

	/** Data directory. This is the place where we will put data files. */
	/* !ALREADY FIXE! FIXME: This should be configurable! */
	private static String dataDirectory = "./maljae-data";

	public static String getDataDirectory() {
		return dataDirectory;
	}

	// modification Start
	// setter to update the data directory dynamically
	public static void setDataDirectory(String s) {
		if (s != null) {
			dataDirectory = s;
		}
	}
	// modification End

	/** Dates are supposed to be written with the following format. */
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	/** Opening date. Before this date, nobody can create a team. */
	private static Date openingDate = null;

	public static Date getOpeningDate() {
		return openingDate;
	}
	
	/** Closing date. After this date, nobody can change team-related data. */
	private static Date closingDate = null;

	public static Date getClosingDate() {
		return closingDate;
	}
	/** Display the date range */
	public static String showDateRange() {
		return df.format(openingDate) + " - " + df.format(closingDate);
	}

	/** Minimal number of users per team. */
	private static int minNbUsersPerTeam = -1;

	public static int getMinNbUsersPerTeam() {
		return minNbUsersPerTeam;
	}

	/** Maximal number of users per team. Must be greater than minNbUsersPerTeam. */
	private static int maxNbUsersPerTeam = -1;

	public static int getMaxNbUsersPerTeam() {
		return maxNbUsersPerTeam;
	}

	/** Default number of teams per subject. */
	private static int defaultNbTeamsPerSubject = -1;

	public static int getDefaultNbTeamsPerSubject() {
		return defaultNbTeamsPerSubject;
	}

	/** Task descriptions. */
	private static ArrayList<Task> tasks=new ArrayList<Task>();

	//LiFang Dynamic Addtask
	public static void addtask(String id, String name, String url, String desc ) throws IOException {
	Task t = new Task(id,name,url,desc);
	tasks.add(t);
	String filename = Configuration.getDataDirectory() + "/" + t.getIdentifier() + "-task.json";
	t.saveTaskTo(new File(filename));
	
	}
	//
	
	public static Task getTask(String identifier) {
		for (int i = 0; i < tasks.size(); i++) {
			String tid = tasks.get(i).getIdentifier();
			if (tid.equals(identifier))
				return tasks.get(i);
		}
		return null;
	}

	/*public static void getTasks() {
		System.out.println(tasks);
	}*/
	
	public static ArrayList<Task> getTasks() {
		return tasks;
	}

	public static void ReadDates (JSONObject json) throws Exception {
		openingDate = df.parse(json.getString("opening_date"));
		closingDate = df.parse(json.getString("closing_date"));

	}
	
	public static void ReadNbUsersPerTeam ( JSONObject json ) {
		
		JSONObject rangeNbUsersPerTeam = json.getJSONObject("nb_users_per_team");
		minNbUsersPerTeam = rangeNbUsersPerTeam.getInt("min");
		maxNbUsersPerTeam = rangeNbUsersPerTeam.getInt("max");
	}
	
	public static void ReadNbTeamsPerSubject (JSONObject json ) {
		
		JSONObject rangeNbTeamsPerSubject = json.getJSONObject("nb_teams_per_subject");
		defaultNbTeamsPerSubject = rangeNbTeamsPerSubject.getInt("default");
	}
	
	public static void ReadTasks (JSONObject json ) {
		
		JSONArray json_tasks = json.getJSONArray("tasks");
		for (int index = 0; index < json_tasks.length(); index++) {
			try {
			tasks.add(new Task(json_tasks.getJSONObject(index)));
			}
			catch(Exception e) {
				
			}
		}
		
	}
	
	/** Load configuration file in memory. */
	/* FIXED */

	public static void loadFrom(File f) throws Exception {
		JSONObject json = new JSONObject(FileUtils.readFileToString(f, "utf-8"));
		ReadDates(json);
		ReadNbUsersPerTeam(json);
		ReadNbTeamsPerSubject(json);
		ReadTasks( json );
		System.out.println("Config File : " + f);
		}
	
	public static void loadTaskFrom(File d) throws IOException {
		for (File f : d.listFiles()) {
			if (Task.isValidTaskFileName(f.getName()))
				tasks.add(new Task(f));
		}
	}
	
	//FIXME to test after task creation , to store infos into the JSON
	public void saveTo (File f) throws IOException {
		JSONObject json = new JSONObject ();
		JSONArray task_json = new JSONArray ();
		for (int i = 0; i < tasks.size (); i++) {
		    if (tasks.get (i) != null) {
		    	task_json.put (tasks.get(i).getIdentifier ());
		    	task_json.put (tasks.get(i).getTitle ());
		    	task_json.put (tasks.get(i).getUrl ());
		    	task_json.put (tasks.get(i).getDescription ());
			    }
		}
		FileWriter fw = new FileWriter (f);
		fw.write (json.toString (2));
		fw.close ();
	    }
	  
	// LiFang update for admin webpage
	public static void getTasksList() {
			System.out.println(tasks);
	}
	// End of Update
}
