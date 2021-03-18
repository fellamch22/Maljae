package fr.univparis.maljae;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/** This class of objects represent task description. */
public class Task {

    private String identifier;
    private String title;
    private String url;
	private String description;

    Task (JSONObject o) {
	this.identifier = o.getString ("identifier");
	this.title = o.getString ("title");
	this.url = o.getString ("url");
	this.description = o.getString ("description");
    }

    //2nd task constructor to add tasks
    Task (String id, String name, String url, String desc) {
    	this.identifier = id;
    	this.title = name;
    	this.url = url;
    	this.description = desc;
    	//FIXME
    	//MUST ADD JSON SAVE HERE
        }

	Task(File f) throws IOException {
		JSONObject json = new JSONObject(FileUtils.readFileToString(f, "utf-8"));

		identifier = json.getString("identifier");
		if (!f.getName().equals(identifier + "-task.json")) {
			throw new RuntimeException("Inconsistency in the task data model: " + f.getName());
		}

		this.title = json.getString("title");
		this.url = json.getString("url");
		this.description = json.getString("description");
	}

	   public void saveTaskTo (File f) throws IOException {
			JSONObject json = new JSONObject ();
			json.put ("identifier", identifier);
			json.put ("title", title);
			json.put ("url", url);
			json.put ("description", description);
			FileWriter fw = new FileWriter (f);
			fw.write (json.toString (2));
			fw.close ();
		    }

    
    public String toString () {
	return
			"\n" +this.identifier + "\n" +
	    this.title + "\n" +
	    this.url + "\n" +
	    this.description+ "\n";
    }

    public static boolean isValidTaskFileName (String fname) {
    	Pattern p = Pattern.compile (".*-task.json");
    	Matcher m = p.matcher (fname);
    	return m.find ();
        }

    public String getIdentifier () {
	return identifier;
    }
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
