package fr.univparis.maljae;

import fr.univparis.maljae.Configuration;
import io.javalin.*;
import java.io.*;
import java.util.Calendar;

/**
 * A webserver
 */
public class App {
	public static void about() {
		System.out.println("maljae server version " + Configuration.version + " running.");
	}

	public static Javalin launch() {
		Javalin app = Javalin.create(config -> {
			config.addStaticFiles("public/");
			config.enableWebjars();
		}).start(8080);
		return app;
	}


	public static void loadConfiguration(String configFile) throws Exception {
		Configuration.loadFrom(new File(configFile));
	}

//modification Start 
	public static void loadDataDir(String datadir) throws Exception {
		Configuration.setDataDirectory(datadir);
	}
//modification End
	
	public static void initialize() throws IOException {
		System.out.println("DefaultNbTeamsPerSubject = " + Configuration.getDefaultNbTeamsPerSubject());
		new File(Configuration.getDataDirectory()).mkdirs();
		Teams.loadFrom(new File(Configuration.getDataDirectory()));
		Configuration.loadTaskFrom(new File(Configuration.getDataDirectory()));
		
	}
//modification Start
	public static void main(String[] args) throws Exception {
		about();
		loadConfiguration(args[0]);
		try {
		loadDataDir(args[1]); //dynamic data dir from args[1]
		}
		catch (Exception e ) {
			System.out.println("Please fill the data dir");
			System.exit(0);
		}
//modification End
		initialize();
		Javalin app = launch();
		TeamController.install(app);
		
		Teams.getTeams().forEach( (e) ->  { System.out.println(e.getTeamName()) ; } );
		

	} 
}
