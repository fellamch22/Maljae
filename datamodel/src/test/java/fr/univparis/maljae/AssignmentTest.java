package fr.univparis.maljae;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.Test;

public class AssignmentTest {

	@Test
	public void ordreInitialTest() throws Exception {
		
		Configuration.loadFrom(new File("../datamodel/src/test/resources/config.json"));
		Teams.loadFrom(new File("../webserver/maljae-data"));
		ArrayList<Team> t = Assignment.ordreInitial();
		
	}

	@Test
	public void finalAssignTest() throws Exception {

		Configuration.loadFrom(new File("../datamodel/src/test/resources/config.json"));
		Teams.loadFrom(new File("../webserver/maljae-data"));
         Entry<Team,Task> e ;
        Assignment.finalAssign();
        Assignment.saveTo(new File("../webserver/maljae-data/Assignment.json"));
		System.out.println(Assignment.team_tasks.size());
		Iterator <Entry<Team,Task>> it = Assignment.team_tasks.entrySet().iterator() ;
		
	    System.out.println( "\n T    R    A    C   E  \n");
		System.out.println(Assignment.getTrace());
	}
}
