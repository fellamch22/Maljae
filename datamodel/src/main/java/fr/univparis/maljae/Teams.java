package fr.univparis.maljae;

import java.io.*;
import java.util.*;
//import org.json.*;

/** This module collects teams. */
public class Teams {

	/* FIXME: This may be not the right data structure... */
	private static final Set<Team> teams = new TreeSet<Team>();
	
	public static Set<Team> getTeams() {
		return teams;
	}

	public static void loadFrom(File d) throws IOException {
		for (File f : d.listFiles()) {
			if (Team.isValidTeamFileName(f.getName()))
				teams.add(new Team(f));
		}
	}

	/* FIXME: Shouldn't we throw an exception when the team is not found? */
	public static Team getTeam(String identifier) {
		for (Team team : teams) {
			if (team.getIdentifier().equals(identifier))
				return team;
		}
		return null;
	}

	public static void removeFromExistingTeam(String email) {
		for (Team team : teams) {
			team.removeStudent(email);
		}
	}

	//Update : modified constructor : added one String
	public static Team createTeam(String email, String teamnam, Boolean[] emploiDuTemps) throws IOException {
		System.out.println("Create team : " + email);
		removeFromExistingTeam(email);
		Team newTeam = new Team(new Student(email, true), teamnam,emploiDuTemps); // add one String for the team name
		// end of modify

		// Modi-Fella 20 11 2019
		if (!teams.add(newTeam)) {
			return null;
		} else {
			String filename = Configuration.getDataDirectory() + "/" + newTeam.getIdentifier() + "-team.json";
			newTeam.saveTo(new File(filename));
			System.out.println("End Create team : " + email + " - " + newTeam);
			System.out.println("End Create team reel ");
			return newTeam;
		}
	}

	//Update Add : Delete team
	public static void deleteTeam(Team t) {
		String filename = Configuration.getDataDirectory() + "/" + t.getIdentifier() + "-team.json";
		File f = new File(filename);
		f.delete(); // have to delete the txt(json) in same time.
		teams.remove(t); // delete the team from ArrayList
	}
	// end of Modification

	public static void saveTeam(Team team) throws IOException {
		String filename = Configuration.getDataDirectory() + "/" + team.getIdentifier() + "-team.json";
		team.saveTo(new File(filename));
	}

	public static void getTeamsList() {
		System.out.println(teams);
	}

    

}
