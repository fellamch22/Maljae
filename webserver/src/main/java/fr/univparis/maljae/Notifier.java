package fr.univparis.maljae;
import fr.univparis.maljae.Configuration;
import fr.univparis.maljae.Teams;
import java.io.*;
import java.util.Properties;



public class Notifier {

    /* !ALREADY FIXED! FIXME: Email sending has to be implemented. For the moment, we store message
       FIXME: in files */
    public static void sendEmail (String email, String subject, String message)
	throws IOException
    {
    //Debug
    System.out.println("START : " +email + subject + message);
	
    String filename = "LiFang"+message.hashCode () + subject.hashCode () + "-email.txt";
	//File f = new File (Configuration.getDataDirectory() + "/" + filename);
	File f = new File (Configuration.getDataDirectory() + "/" + filename);
	System.out.println("\nWriting file : "+Configuration.getDataDirectory() + "/" + filename);
	FileWriter fw = new FileWriter (f);
	fw.write ("email: " + email + "\n" +
		  "subject: " + subject + "\n" +
		  "message:\n" + message + "\n");
	fw.close ();
	
	//Debug
	System.out.println("END1 : "+Configuration.getDataDirectory());
	System.out.println("END2 : " +filename);
	
	//Update send mail
	SendEmailSSL.sendEmailSSL(email , subject , message);
	// End of modification
    }

    public static void sendTeamCreation (String host, Token token)
	throws IOException
    {
    System.out.println(host + "-" + token);
	String message =
	    "Hello!\n" +
	    "You have requested the creation of a team.\n" +
	    "Here are the links to perform actions on this team.\n" +
	    "- To edit your team information :\n" +
	    "  http://" + host + "/team/edit/" + token.toString () + "\n" +
	    "- To delete your team :\n" +
	    "  http://" + host + "/team/delete/" + token.toString () + "\n";
	System.out.println(token.getEmail()); //Testing
	
	sendEmail (token.getEmail (), "[maljae] Team Creation", message);
	
	System.out.println("message : " + message); //Testing
    }
}
