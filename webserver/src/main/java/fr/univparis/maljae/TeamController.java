
package fr.univparis.maljae;

import fr.univparis.maljae.Configuration;
import fr.univparis.maljae.Teams;
import io.javalin.*;
import io.javalin.plugin.rendering.FileRenderer;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinFreemarker;
import io.javalin.plugin.rendering.template.TemplateUtil;
import java.io.*;
import java.util.Arrays;
import java.util.Date;

/** API for team management. */
public class TeamController {

	// Update fix duplicated team name
	public static void installTeamCreate(Javalin app) {
		app.post("/team/create", ctx -> {
		if( Configuration.getOpeningDate().after(new Date())) {// no registration accepted before opening date
			
			ctx.redirect("/registration-refused.html");
			
		}else if (Configuration.getClosingDate().before(new Date())) {// no registration accepted after closing date
			
			ctx.redirect("/team-creation-refused.html");
			
		}else {
			
			String email = ctx.formParam("email");
			String nomTeam = ctx.formParam("teamname");
			Boolean[] emploiDuTemps = new Boolean[14];
			if ( ctx.formParam("lundiAM") != null) emploiDuTemps[0] = true; 
			if ( ctx.formParam("lundiPM") != null) emploiDuTemps[1] = true;
			if ( ctx.formParam("mardiAM") != null) emploiDuTemps[2] = true;
			if ( ctx.formParam("mardiPM") != null) emploiDuTemps[3] = true;
			if ( ctx.formParam("mercrediAM") != null) emploiDuTemps[4] = true;
			if ( ctx.formParam("mercrediPM") != null) emploiDuTemps[5] = true;
			if ( ctx.formParam("jeudiAM") != null) emploiDuTemps[6] = true;
			if ( ctx.formParam("jeudiPM") != null) emploiDuTemps[7] = true;
			if ( ctx.formParam("vendrediAM") != null) emploiDuTemps[8] = true;
			if ( ctx.formParam("vendrediPM") != null) emploiDuTemps[9] = true;
			if ( ctx.formParam("samediAM") != null) emploiDuTemps[10] = true;
			if ( ctx.formParam("samediPM") != null) emploiDuTemps[11] = true;
			if ( ctx.formParam("dimancheAM") != null) emploiDuTemps[12] = true;
			if ( ctx.formParam("dimanchePM") != null) emploiDuTemps[13] = true;
			/*
			 * boolean nameTaken=false; for (int i =0;i<Teams.getTeams().size();i++) { if
			 * (nomTeam.equals(Teams.getTeams().get(i).getTeamName() ) ) { nameTaken=true; }
			 * } if (nameTaken==true) {
			 */

			// get list of the teams : affice tout les teams dans le set
			Teams.getTeams().forEach((e) -> {
				System.out.println(e.getTeamName());
			});

			Team newteam = Teams.createTeam(email, nomTeam,emploiDuTemps);
			if (newteam == null) {
				ctx.redirect("/team-creation-fail.html");
			} else {
				Token newtoken = Token.createToken(newteam, email);
				String host = ctx.host();
				Notifier.sendTeamCreation(host, newtoken);
				ctx.redirect("/team-creation-done.html");
			}
			
			
		}

		});
	}
	// End of FixMe - Duplicated team name

	public static void installTeamEdit(Javalin app) {
		app.get("/team/edit/:token", ctx -> {
			try {
			System.out.println("!!!!! " + ctx.pathParam("token") + " " + Integer.parseInt(ctx.pathParam("token")));

			Token token = Token.getToken(ctx.pathParam("token"));
			//team non existe
			System.out.println(" >>> " + token.getTeam());
			try {
				String teamName = token.getTeam().getIdentifier();
				Teams.getTeam(teamName).getEmploiDuTemps();
				Team team = Teams.getTeam(teamName);
				String[] emploiDuTempsString= Teams.getTeam(teamName).getEmploiDuTemps() ;
				ctx.render("/public/edit-team.ftl",
						TemplateUtil.model("teamName", teamName, "secret", team.getSecret(), "students",
								team.studentsToString(), "preferences", team.preferencesToString(), "token",
								token.toString(),"edt0",emploiDuTempsString[0] ,"edt1",emploiDuTempsString[1]  
										,"edt2",emploiDuTempsString[2] ,"edt3",emploiDuTempsString[3]  
												,"edt4",emploiDuTempsString[4] ,"edt5",emploiDuTempsString[5]  
												,"edt6",emploiDuTempsString[6] ,"edt7",emploiDuTempsString[7]  
												,"edt8",emploiDuTempsString[8] ,"edt9",emploiDuTempsString[9]  
												,"edt10",emploiDuTempsString[10] ,"edt11",emploiDuTempsString[11]  
												,"edt12",emploiDuTempsString[12] ,"edt13",emploiDuTempsString[13]));
						  
								
			} catch (Exception e) {
				System.out.println(">>>>>>>>>>>>>"+e.getMessage());
				ctx.redirect("/team-dont-exist.html");
			}
			}// fin try
			catch (Exception e) {
				System.out.println("MAUVAIS TOKEN !!!!! " + ctx.pathParam("token") );	
			}
		});
	}

	// Modification Add : delete team function
	public static void TeamDelete(Javalin app) {
		app.get("/team/delete/:token", ctx -> {
			Token token = Token.getToken(ctx.pathParam("token"));
			String teamName = token.getTeam().getIdentifier();
			Team team = Teams.getTeam(teamName);
			ctx.render("/public/delete-team.ftl",
					TemplateUtil.model("teamName", teamName, "secret", team.getSecret(), "students",
							team.studentsToString(), "preferences", team.preferencesToString(), "token",
							token.toString()));
			Teams.deleteTeam(team); // Create new function for deleting team
			Token.deleteToken(token);// for deleting token
		});
	}
	// end of modification

	public static void installTeamUpdate(Javalin app) {
		// funciton of Javalin : manager les exceptions et les erreurs
		// In Javalin, we use try catch pour envoyer les informations : erreur

		app.post("/team/update/:token", ctx -> {
			Token token = Token.getToken(ctx.pathParam("token"));
			String who = token.getEmail();
			String teamName = token.getTeam().getIdentifier();
			Team team = Teams.getTeam(teamName);
			team.updateSecretFromString(ctx.formParam("secret"));
			team.updateStudentsFromString(who, ctx.formParam("students"));
			team.updatePreferencesFromString(ctx.formParam("preferences"));
			team.updateEdt(
					ctx.formParam("lundiAM"), 
					ctx.formParam("lundiPM"),
					ctx.formParam("mardiAM"),
					ctx.formParam("mardiPM"),
					ctx.formParam("mercrediAM"),
					ctx.formParam("mercrediPM"),
					ctx.formParam("jeudiAM"),
					ctx.formParam("jeudiPM"),
					ctx.formParam("vendrediAM"),
					ctx.formParam("vendrediPM"),
					ctx.formParam("samediAM"),
					ctx.formParam("samediPM"),
					ctx.formParam("dimancheAM"),
					ctx.formParam("dimanchePM")
					);
			Teams.saveTeam(team);
			ctx.redirect("/team-update-done.html");
			System.out.println(">>> " + team.getTeamMails());

			// Modification : add team members
			// Add loop on team mails to sendmail
			for (int i = 0; i < team.getTeamMails().size(); i++) {
				Notifier.sendEmail(team.getTeamMails().get(i), "[Maljae] Team " + teamName + " update",
						"\nYour team " + teamName + " has been updated.\n\nCurrent members :  " + team.getTeamMails());
			}
		});
	}

	//Update for Admin webpage : 3 Functions

	public static void adminTeamlist(Javalin app) {
		app.post("/team/adminteamlist", ctx -> {
			Teams.getTeamsList();
			ctx.redirect("/admin.html");
		});
	}

	public static void adminTasklist(Javalin app) {
		app.post("/team/admintasklist", ctx -> {
			Configuration.getTasksList();
			ctx.redirect("/admin.html");
		});
	}

	public static void adminAssign(Javalin app) {
		app.post("/team/assigntasktoteam", ctx -> {
			if (Configuration.getClosingDate().after(new Date())) {
				
				ctx.redirect("/assign_refused.html");

			}else {
				
				Assignment.finalAssign();
		        Assignment.saveTo(new File(Configuration.getDataDirectory()+"/Assignment.json"));
				System.out.println(Assignment.getTrace());
			    ctx.redirect("/admin.html");
			}
			
		});
	}

	public static void taskCreate(Javalin app) {
		app.post("/team/taskcreate", ctx -> {
			String taskid = ctx.formParam("taskid");
			String taskname = ctx.formParam("taskname");
			String taskurl = ctx.formParam("taskurl");
			String taskdesc = ctx.formParam("taskdesc");
			if ((taskid != null) && (taskname != null) && (taskurl != null) && (taskdesc != null)) {
				Configuration.addtask(taskid, taskname, taskurl, taskdesc);
				ctx.redirect("/task-creation-done.html");
			}
		});
		// write json
	}

	public static void install(Javalin app) {
		JavalinRenderer.register(JavalinFreemarker.INSTANCE, ".ftl");
		installTeamCreate(app);
		installTeamEdit(app);
		installTeamUpdate(app);
		// Update Admin webpage and Delete team
		adminTeamlist(app);
		adminTasklist(app);
		adminAssign(app);

		TeamDelete(app);
		taskCreate(app);

	}

}
