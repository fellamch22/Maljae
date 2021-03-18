package fr.univparis.maljae;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class TeamsTest {

	@Test
	public void unicityIdentifiersTest() throws IOException {
	   System.out.println("Trying to add teams with same identifiers ");
		assertEquals(Teams.getTeams().add(new Team("../webserver/maljae-data/maljae1405004696-team.json")),true);
		assertEquals(Teams.getTeams().add(new Team("../webserver/maljae-data/maljae1405004696-team.json")),false);
		
	}

	
}
