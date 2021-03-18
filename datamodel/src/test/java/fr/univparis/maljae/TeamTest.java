package fr.univparis.maljae;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class TeamTest {

	@Test
	public void plusPetiteAdrMailTest() throws Exception {
		Configuration.loadFrom(new File("../datamodel/src/test/resources/config.json"));
	Team t =  new Team(new File("../webserver/maljae-data/maljae1405004696-team.json"));
	   
		System.out.println(" Testing that the method returns the smallest "
						+ " email Adress in the team ");
	  assertEquals(t.plusPetiteAdr()
			    ,"hf_mechouar@esi.dz");
	}

}
