// this file contains the main loop and upper level functionality
// forked by Lavender - original code and and lots of help comes from aRandomZy
package mafia.gg.bot;
 
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.NoSuchElementException;

public class App {
	//System.out.println( "hi" );
 
	private static MafiaSession obj = MafiaSession.newSession();
	private static List<String> auth = new ArrayList<String>() {{
		add( "Lavender" );
		add( "zbruh" );
	}};
	private static List<String> tempAuth = new ArrayList<String>();
	
	private static Map<String, ArrayList<String>> votes = new HashMap<String, ArrayList<String>>();
	
//	private static long ms = 0;
	private static boolean afk = false;
	private static boolean autoHost = false; // wait to be summoned (false), or host rooms automatically (true)?
	private static String summonPhrase = ".helphost";
	private static String summoner = "";
	//private static boolean standingBy = true; // used in helphost mode when waiting for host
	private static boolean toldToLeave = false;
	private static long hostPatience = 60;
	private static boolean lockdeck = false;
	private static boolean votemode = false; // can players change setup?
	private static boolean listed = false; //check/uncheck to host listed or unlisted rooms by default
	private static boolean rroff = true; // default conplan setting
	//private static boolean daystart = false;
	private static boolean hiddenSetup = false; // default conplan setting
	
	//String[] empty = new String[] {};
	private static Setup currentSetup = new Setup( "", "", "", 0, "", "", "", "", "", "", "", new String[] {}, "" );
	//private static String currentSetup = "conplan";
	//private static int setupSize = 0;
	private static String autoHostText = "";//" (autohosted)";
	private static boolean waiting = false;
	private static int votethreshold = 3;
	private static long timerstart = System.nanoTime();
	private static long timeToStart = 0;
	private static long timeResets = 0;
	public static int deckChanges;
	public static int changes;
	
	
	public void resetDeckChanges() {
		Random rand = new Random();
		deckChanges = 15 + rand.nextInt( 5 );
		changes = 0;
	}
	
	public void incChanges() {
		changes = changes + 1;
		if ( deckChanges - changes == 0 ) {
			obj.sendMessage( "Okay you animals, deck has been changed " + Integer.toString( deckChanges ) + " times and will not be changed again this round.");
		}
	}
	
	public void resetTimer() {
		timerstart = System.nanoTime();
		timeResets = timeResets + 1;
	}
	
	public void hardResetTimer() {
		timerstart = System.nanoTime();
		timeResets = 0;
	}
	
	
	//public void dectimer( long nano ) {
	//	timer = timer - nano;
	//}
	
	public int toStart() {
		long elapsed = ( System.nanoTime() - timerstart ) / 1000000000;
		int toStart = (int) (timeToStart/(Math.pow(2,timeResets)) - elapsed);
		if (toStart < 0 ) {
			toStart = 0;
		}
		return toStart;
	}
	
	public void resetVotes() {
		votes = new HashMap<String, ArrayList<String>>();		
		votes.put("consifom", new ArrayList<String>());
	    votes.put("dethy", new ArrayList<String>());
        votes.put("conplan", new ArrayList<String>());
        votes.put("conplan+", new ArrayList<String>());
        votes.put("lovers", new ArrayList<String>());
        votes.put("pie7", new ArrayList<String>());
        votes.put("bootcamp", new ArrayList<String>());
        votes.put("gnh", new ArrayList<String>());
        votes.put("pie7+", new ArrayList<String>());
        votes.put("bootcamp+", new ArrayList<String>());
        votes.put("dnct", new ArrayList<String>());
        votes.put("jani", new ArrayList<String>());
        votes.put("abc", new ArrayList<String>());
        votes.put("how2mm", new ArrayList<String>());
        votes.put("triplecamp", new ArrayList<String>());		
		
        votes.put("stc", new ArrayList<String>());
        votes.put("camerashy", new ArrayList<String>());
		
        votes.put("congress", new ArrayList<String>());
        votes.put("ascension", new ArrayList<String>());
        votes.put("sweetdreams", new ArrayList<String>());
        votes.put("gnightless", new ArrayList<String>());
        votes.put("voltronmicro", new ArrayList<String>());
        votes.put("oneshotcops", new ArrayList<String>());
        votes.put("multemplar", new ArrayList<String>());
        votes.put("uncertainty", new ArrayList<String>());
        votes.put("powervilly13", new ArrayList<String>());
        votes.put("whiteflag", new ArrayList<String>());
        votes.put("allstars13", new ArrayList<String>());
        votes.put("circus", new ArrayList<String>());
        votes.put("allstars15", new ArrayList<String>());
        votes.put("solobombs", new ArrayList<String>());
        votes.put("fatedduo", new ArrayList<String>());
        votes.put("ibern", new ArrayList<String>());
        votes.put("lizardrroff", new ArrayList<String>());
        votes.put("masonrelay", new ArrayList<String>());
        votes.put("powermillers", new ArrayList<String>());
        votes.put("shobombs", new ArrayList<String>());
        votes.put("vip", new ArrayList<String>());
        votes.put("basic20", new ArrayList<String>());
        votes.put("pie25", new ArrayList<String>());
        
        votes.put("superposition", new ArrayList<String>());
        votes.put("carbon14", new ArrayList<String>());
        votes.put("matrix6", new ArrayList<String>());
        votes.put("notnot", new ArrayList<String>());
        votes.put("newd3", new ArrayList<String>());
        votes.put("mafiajjani", new ArrayList<String>());
        votes.put("sodium24", new ArrayList<String>());
        votes.put("allstarssemi", new ArrayList<String>());
        votes.put("revrol7", new ArrayList<String>());
        votes.put("revrol11", new ArrayList<String>());
        votes.put("revrol15", new ArrayList<String>());
	}
	
	public String displayVotes() {
		String str = "Current votes ("+Integer.toString( votethreshold )+ " votes needed to change setup) ";
		for (String setup : votes.keySet()) {
			int vote = votes.get( setup ).size();
			if ( vote != 0 ) {
				str = str +  " | " + setup + ": " + Integer.toString( vote );
			}
		}
		if ( str.length() <= 50 ) {
			str = "No votes have been cast.";
		}
		return str;	     
	}
	
	public String castVote( String stp, String usr) {
		stp = stp.toLowerCase();
		if (!votes.containsKey( stp )) {
			return "'" +stp + "' is not a valid setup code, "+usr+"! Type '.help' to see available setup codes.";
		}
		else if (votes.get( stp ).contains(usr) ) {
			return "You have already voted for "+ stp + ", "+ usr +"!";
		}
		else {
			votes.get( stp ).add( usr );
			return usr + " has voted for setup: "+ stp;			
		}		
	}
	
	public String checkVoteNew() {
		for (String setup : votes.keySet()) {
			int vote = votes.get( setup ).size();
			if ( vote == votethreshold ) {
				return setup;
			}
		}
		return "";
	}
	
	public boolean isAuthorized( String user ) {
		//boolean authorized = false;
		//if ( !chat.get(i).equals( ".exit" ) && !chat.get(i).equals( ".wait" ) && !chat.get(i).equals( ".continue" ) && !chat.get(i).equals( ".continue" )&& !chat.get(i).equals( ".listtoggle" )) {
		//	authorized = true;
		//}
		return auth.contains( user );
		//for (int j = 0; j < auth.size(); j++) {
		//	if ( user.equals( auth.get( j ) ) ){
		//	//System.out.println( "hmmm");
		//		return true;
		//	}
		//}
		//return false;
	}
	
	public boolean isTempAuthorized( String user ) {
		//boolean authorized = false;
		//if ( !chat.get(i).equals( ".exit" ) && !chat.get(i).equals( ".wait" ) && !chat.get(i).equals( ".continue" ) && !chat.get(i).equals( ".continue" )&& !chat.get(i).equals( ".listtoggle" )) {
		//	authorized = true;
		//}
		return tempAuth.contains( user ) || isAuthorized( user );
		//for (int j = 0; j < auth.size(); j++) {
		//	if ( user.equals( auth.get( j ) ) ){
		//	//System.out.println( "hmmm");
		//		return true;
		//	}
		//}
		//return false;
	}
	
	
 
	public void execute(String command, String user) throws Exception {
		boolean authorized = isAuthorized( user );
		boolean tempAuthorized = isTempAuthorized( user );
		if (command.equals(".intro") && authorized)
			intro();
		else if ( ( command.equals(".exit") && authorized ) || ( command.equals(".nukeroom") && tempAuthorized ) ) {
			//obj.setStart( "night" );
			//obj.setMajority( "off" );
			//obj.setHidden("off");
			//obj.setRR("off");
			obj.setSetup( "" );
			try {
				obj.playerDown();
				//System.out.println( "hi" );
				//appObj.queue(".playerDown");
			} catch (Exception e ) {
				return;
			}
			obj.afkCheck();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}			
			obj.startGame();
			obj.sendMessage("Room terminated.");
			if ( command.equals(".exit") ) {
				throw new Exception( "Terminating execution.");
			}
			else {
				toldToLeave = true;
				obj.goHome();				
			}
		}
		else if ( command.equalsIgnoreCase(".host") && ( authorized || ( !autoHost && tempAuthorized ) ) ){
			obj.giveHost(user);
			obj.sendMessage( "Hosting duty passed to " + user + ". Pass host back for additional help selecting the setup." );
		}
		else if ( command.equalsIgnoreCase( ".kickbot" ) && !autoHost && tempAuthorized ) {
			toldToLeave = true;
		}
		else if (command.equalsIgnoreCase(".democracy") && authorized) {
			votemode = !votemode;
			if ( votemode ) {
				obj.sendMessage( "Democracy enabled." );
				help();
			}
			else {
				obj.sendMessage( "Democracy disabled." );
			}
		}
		else if (command.startsWith(".kick ") && tempAuthorized) {
			command = command.replace(".kick ", "");
			//obj.sendMessage( "Kicking user: " + command);
			if ( obj.kick( command ) ) {
				obj.sendMessage( command + " has been kicked.");
			}
			else {
				obj.sendMessage( "Couldn't kick " + command + "." );
			}
		}
		else if (command.startsWith(".ban ") && authorized) {
			command = command.replace(".ban ", "");
			obj.ban( command );
			obj.sendMessage( command + " has been banned.");
			execute( ".kick "+command, user);
		}
		else if (command.equals(".impatient")) {
			int onlyneed = obj.totalPlayers() - obj.playerdUp();
			obj.sendMessage( Integer.toString( onlyneed ) + " more" );
		}
		else if (command.equalsIgnoreCase(".info")) {
			if ( currentSetup.name.equals( "" ) ) {
				obj.sendMessage( "The bot must change the setup at least once in order to use this command." );
			}
			displayInfo( currentSetup );
		}
		//else if (command.startsWith(".info ") && votemode ) {
		//	command = command.replace(".info ", "");
		//	if (!votes.containsKey( command )) {
		//		obj.sendMessage( "'" + command + "' is not a valid setup code.");
		//	}
			//displayInfo( command );
		//}
		else if (command.equalsIgnoreCase(".credits")) {
			obj.sendMessage( "This bot was programmed in Java by Lavender (Lavender#8704 on Discord), though the original code was written and given to him by someone else. It uses a library called Selenium (https://www.selenium.dev/) to open a browser and interact with it by referencing the source html. Poorly-documented source code available here: https://github.com/Lav3ndr/mafia.gg-hosting-bot");			
		}
		else if (command.equalsIgnoreCase(".garbageman")) {
			obj.sendMessage("gotta figure out how to get off my butt and work on my real side projects");
		}
		else if (command.equalsIgnoreCase(".xinde")) {
			obj.sendMessage(".deck Love Live");
		}
		else if (command.equalsIgnoreCase(".auryxx")) {
			obj.sendMessage(".xinde");
		}
		else if (command.equalsIgnoreCase(".hockeyfan123")) {
			obj.sendMessage("doesn't even like hockey");
		}
		else if (command.equalsIgnoreCase(".samthewhale")) {
			obj.sendMessage("not actually a whale?");
		}
		else if (command.equalsIgnoreCase(".lavender")) {
			obj.sendMessage("hath no more brain than stone");
		}
		else if (command.equalsIgnoreCase(".1612")) {
			obj.sendMessage("wholesome <3");
		}
		else if (command.equalsIgnoreCase(".khs131")) {
			obj.sendMessage("good player");
		}
		else if (command.equalsIgnoreCase(".stimilant")) {
			obj.sendMessage("plays conplan with the wrong settings but i forgive him");
		}
		else if (command.equalsIgnoreCase(".ericmw")) {
			obj.sendMessage("went out for milk and never came back");
		}
		else if (command.equalsIgnoreCase(".reshoe7777777")) {
			obj.sendMessage("kangaroo emoji");
		}
		else if (command.equalsIgnoreCase(".hitoshisuki")) {
			obj.sendMessage("always town");
		}
		else if (command.equalsIgnoreCase(".chiccpea")) {
			obj.sendMessage("chick emoji can emoji");
		}
		else if (command.equalsIgnoreCase(".wangalang")) {
			obj.sendMessage("wang emoji");
		}
		else if (command.equalsIgnoreCase(".getdropkicked")) {
			obj.sendMessage("gdk pog");
		}
		else if (command.equalsIgnoreCase(".bruhmoments")) {
			obj.sendMessage("taught me everything i know");
		}
		else if (command.equalsIgnoreCase(".michael")) {
			obj.sendMessage("i miss michael");
		}
		else if (command.equalsIgnoreCase(".akiak")) {
			obj.sendMessage("i miss akiak");
		}
		else if (command.equalsIgnoreCase(".iruncursebidoof")) {
			obj.sendMessage("legend");
		}
		else if (command.equalsIgnoreCase(".meteornate")) {
			obj.sendMessage("meteor emoji nate emoji");
		}
		else if (command.equalsIgnoreCase(".froggo43")) {
			obj.sendMessage("dm me froggo memes i can't think of any");
		}
		
		else if (command.equals(".time")) {
			int toStart = toStart();
			if ( toStart != 0 ){
				obj.sendMessage( "Game will not start for at least "+ Integer.toString( toStart() ) +" seconds.");
			}
			else {
				obj.sendMessage( "Game will start as soon as we have enough players!");
			}
		}
		else if (command.equals(".resettimer") && authorized) {
			obj.sendMessage( user + " has reset the timer.");
			hardResetTimer();
		}
		else if (command.startsWith(".defaulttimer") && authorized) {
			command = command.replace(".defaulttimer ", "");
			try {
				int n = Integer.parseInt(command);
				timeToStart = n;
				obj.sendMessage( user + " has changed the default timer to " + Integer.toString( n ) + "seconds." );
			} catch (NumberFormatException e) {
				obj.sendMessage(e.getMessage());
			}
		}
		else if (command.equalsIgnoreCase(".pause") && authorized) {
			waiting = !waiting;
			if (waiting ) {
				obj.sendMessage( user + " has paused the bot.");
			}
			else {
				obj.sendMessage( user + " has unpaused the bot.");
			}
		}
			//obj.sendMessage( "Waiting...game will not begin until an authorized user types '.continue'."  );
		//}
		//else if (command.equals(".wait") && authorized) {
		//	waiting = true;
		//	obj.sendMessage( "Waiting...game will not begin until an authorized user types '.continue'."  );
		//}
		//else if (command.equals(".continue") && authorized) {
		//	waiting = false;
		//	obj.sendMessage( "Resuming normal execution."  );
		//}
		else if (command.equalsIgnoreCase(".listtoggle") && authorized) {
			obj.sendMessage( user + " toggled listed/unlisted.");
			obj.listToggle();
		}
		else if (command.equalsIgnoreCase(".randomdeck") && deckChanges - changes > 0 ) {
			if ( lockdeck ) {
				obj.sendMessage( "Deck is locked." );
				return;
			}
			obj.randomDeck();
			obj.sendMessage( user + " chose a random deck.");
			incChanges();
			
		}
		else if (command.equalsIgnoreCase(".nodeck") && deckChanges - changes > 0 ) {
			if ( lockdeck ) {
				obj.sendMessage( "Deck is locked." );
				return;
			}
			obj.noDeck();
			obj.sendMessage( user + " chose to use no deck.");
			incChanges();
		}
		else if (command.startsWith(".deck ") && deckChanges - changes > 0 ) {
			if ( lockdeck ) {
				obj.sendMessage( "Deck is locked." );
				return;
			}
			command = command.replace(".deck ", "");
			if ( obj.setDeck( command ) )
			{
				obj.sendMessage( user + " has successfully set the deck.");
				incChanges();
			}
			else {
				obj.sendMessage( "Unable to process deck request from "+user+". Deck selection is case-sensitive and must contain the exact string specified." );
			}			
		}
		else if ( command.equalsIgnoreCase(".locknodeck") && tempAuthorized ) {
			obj.noDeck();
			obj.sendMessage( user + " has successfully set the deck, and the deck has been locked.");
			lockdeck = true;
		}
		else if ( command.startsWith(".lockdeck ") && tempAuthorized ) {
			command = command.replace(".lockdeck ", "");
			if ( obj.setDeck( command ) )
			{
				obj.sendMessage( user + " has successfully set the deck, and the deck has been locked.");
				lockdeck = true;
			}
			else {
				obj.sendMessage( "Unable to process deck request from "+user+". Deck selection is case-sensitive and must contain the exact string specified." );
			}			
		}
		//else if (command.equals(".playerUp"))
			// obj.playerUp();
		//	obj.sendMessage("This command has been temporarily restricted.");
		//else if (command.equals(".playerDown"))
		//	obj.playerDown();
		//else if (command.startsWith(".setup ")) {
		//	command = command.replace(".setup ", "");
		//	System.out.println( command );
			//obj.setSetup(command);
		else if (command.startsWith(".setDayTime ") && authorized) {
			command = command.replace(".setDayTime ", "");
			try {
				int n = Integer.parseInt(command);
				obj.editOptions();
				obj.gameSettings();
				obj.setDayTime(n);
				obj.save();
			} catch (NumberFormatException e) {
				obj.sendMessage(e.getMessage());
			}
 
		} else if (command.startsWith(".setNightTime ") && authorized) {
			command = command.replace(".setNightTime ", "");
			try {
				int n = Integer.parseInt(command);
				obj.editOptions();
				obj.gameSettings();
				obj.setNightTime(n);
				obj.save();
			} catch (NumberFormatException e) {
				obj.sendMessage(e.getMessage());
			}
 
		} else if (command.startsWith(".vote ") && votemode ) {
			command = command.replace(".vote ", "");
			String castString = castVote( command, user );
			String newSetup = checkVoteNew();
			if (newSetup == "") {
				obj.sendMessage( castString + " /\\/\\/\\ " + displayVotes() );
			}
			else {
				obj.sendMessage( "Players have voted to play setup: " + newSetup );
				resetVotes();
				resetTimer();
				execute( ".setup "+ newSetup, auth.get( 0 ));
			}
		} else if (command.startsWith(".votethresh ") && authorized ) {
			command = command.replace(".votethresh ", "");
			try {
				int n = Integer.parseInt(command);
				votethreshold = n;
				obj.sendMessage( "Vote threshold has been adjusted to "+Integer.toString( n));
				execute( ".vote ", auth.get( 0 ));
			} catch (NumberFormatException e) {
				obj.sendMessage(e.getMessage());
			}
			
		} else if (command.equalsIgnoreCase(".resetvotes") && authorized ) {
			obj.sendMessage( user + " has reset the votes.");
			resetVotes();			
		} else if (command.equalsIgnoreCase(".showvotes") && votemode ) {
			obj.sendMessage( displayVotes() );
		} else if (command.startsWith(".setup ") && tempAuthorized) {
			command = command.replace(".setup ", "");
			currentSetup = obj.setup( command, currentSetup );
			obj.changeRoomName(currentSetup.name+autoHostText);
			displayInfo( currentSetup );
		}
			/*if (command.equalsIgnoreCase("single")) {
				currentSetup = "single";
				obj.noextrakp();
				obj.nightStart();
				obj.majOff();
				obj.changeRoomName("Autohost testing");
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( !rroff ) {
					obj.rroffToggle();
					rroff = true;
				}
				obj.setSetup(MafiaSession.SINGLE_PLAYER);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("singlehidden")) {
				currentSetup = "singlehidden";
				obj.noextrakp();
				obj.nightStart();
				obj.majOff();
				obj.changeRoomName("Autohost testing");
				if ( !hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = true;
				}
				if ( !rroff ) {
					obj.rroffToggle();
					rroff = true;
				}
				obj.setSetup(MafiaSession.SINGLE_PLAYER);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("consifom")) {
				currentSetup = "consifom";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Consifom"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( !rroff ) {
					obj.rroffToggle();
					rroff = true;
				}
				obj.setSetup(MafiaSession.CONSIFOM);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("conplan")) {
				currentSetup = "conplan";
				obj.noextrakp();
				obj.nightStart();
				obj.majOff();
				obj.changeRoomName("5 Man Conplan"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( !rroff ) {
					obj.rroffToggle();
					rroff = true;
				}
				obj.setSetup(MafiaSession.CONPLAN_5);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("dethy")) {
				currentSetup = "dethy";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Dethy"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				obj.setSetup(MafiaSession.DETHY);
				//setupSize = 5;
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("conplan+")) {
				currentSetup = "conplan+";
				obj.noextrakp();
				obj.nightStart();
				obj.majOff();
				obj.changeRoomName("5 Man Conplan + Lover"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( !rroff ) {
					obj.rroffToggle();
					rroff = true;
				}
				obj.setSetup(MafiaSession.CONPLAN_6);
				//setupSize=6;
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("lovers")) {
				currentSetup = "lovers";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Lovers"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				obj.setSetup(MafiaSession.LOVERS);
				//setupSize = 6;
				displayInfo( currentSetup );
				
			} else if (command.equalsIgnoreCase("stc")) {
				currentSetup = "stc";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Shrink the Cult"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				obj.setSetup(MafiaSession.STC);
				//setupSize = 6;
				displayInfo( currentSetup );
				
			} else if (command.equalsIgnoreCase("camerashy")) {
				currentSetup = "camerashy";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Camera Shy"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( !rroff ) {
					obj.rroffToggle();
					rroff = true;
				}
				obj.setSetup(MafiaSession.CAMERASHY);
				// need to set punish randomly according to the site
				//setupSize = 6;
				displayInfo( currentSetup );
				
			} else if (command.equalsIgnoreCase("gnh")) {
				currentSetup = "gnh";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Guns n Hookers"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				obj.setSetup(MafiaSession.GNH);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("pie7")) {
				currentSetup = "pie7";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Pie E7"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				obj.setSetup(MafiaSession.PIE7);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("bootcamp")) {
				currentSetup = "bootcamp";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Bootcamp"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				obj.setSetup(MafiaSession.BOOTCAMP7);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("pie7+")) {
				currentSetup = "pie7+";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Pie E7"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				obj.setSetup(MafiaSession.PIE8);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("bootcamp+")) {
				currentSetup = "bootcamp+";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Bootcamp"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				obj.setSetup(MafiaSession.BOOTCAMP8);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("dnct")) {
				currentSetup = "dnct";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Dreams Never Come True"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				obj.setSetup(MafiaSession.DNCT);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("jani")) {
				currentSetup = "jani";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Janitorial"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.JANI);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("abc")) {
				currentSetup = "abc";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("ABC"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.ABC);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("how2mm")) {
				currentSetup = "how2mm";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("How To Play Marksman"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.HOW2MM);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("triplecamp")) {
				currentSetup = "triplecamp";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Triplecamp"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.TRIPLECAMP);
				displayInfo( currentSetup );
			
			} else if (command.equalsIgnoreCase("congress")) {
				currentSetup = "congress";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Congress"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.CONGRESS);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("ascension")) {
				currentSetup = "ascension";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Ascension"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.ASCENSION);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("sweetdreams")) {
				currentSetup = "sweetdreams";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Sweet Dreams"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.SWEETDREAMS);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("gnightless")) {
				currentSetup = "gnightless";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Gnightless"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.GNIGHTLESS);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("voltronmicro")) {
				currentSetup = "voltronmicro";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("V_ltr_n μ"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.V_LTR_N);
				displayInfo( currentSetup );	
			} else if (command.equalsIgnoreCase("oneshotcops")) {
				currentSetup = "oneshotcops";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("One Shot Cops"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.ONESHOTCOPS);
				displayInfo( currentSetup );	
			} else if (command.equalsIgnoreCase("multemplar")) {
				currentSetup = "multemplar";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Multemplar"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.MULTEMPLAR);
				displayInfo( currentSetup );	
			} else if (command.equalsIgnoreCase("uncertainty")) {
				currentSetup = "uncertainty";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Uncertainty"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.UNCERTAINTY);
				displayInfo( currentSetup );	
			} else if (command.equalsIgnoreCase("powervilly13")) {
				currentSetup = "powervilly13";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Power Villy 13"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.POWERVILLY);
				displayInfo( currentSetup );	
			} else if (command.equalsIgnoreCase("whiteflag")) {
				currentSetup = "whiteflag";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("White Flag"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.WHITEFLAG);
				displayInfo( currentSetup );	
			} else if (command.equalsIgnoreCase("circus")) {
				currentSetup = "circus";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Circus"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.CIRCUS);
				displayInfo( currentSetup );	
			} else if (command.equalsIgnoreCase("allstars13")) {
				currentSetup = "allstars13";
				obj.twokptwo();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("All Stars"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( !rroff ) {
					obj.rroffToggle();
					rroff = true;
				}				
				obj.setSetup(MafiaSession.ALLSTARS13);
				displayInfo( currentSetup );	
			} else if (command.equalsIgnoreCase("allstars15")) {
				currentSetup = "allstars15";
				obj.twokptwo();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("All Stars"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( !rroff ) {
					obj.rroffToggle();
					rroff = true;
				}				
				obj.setSetup(MafiaSession.ALLSTARS15);
				displayInfo( currentSetup );	
			} else if (command.equalsIgnoreCase("solobombs")) {
				currentSetup = "solobombs";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Solobombs"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.SOLOBOMBS);
				displayInfo( currentSetup );				
			} else if (command.equalsIgnoreCase("fatedduo")) {
				currentSetup = "fatedduo";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Fated Duo"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.FATEDDUO);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("ibern")) {
				currentSetup = "ibern";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("iBern"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.IBERN);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("lizardrroff")) {
				currentSetup = "lizardrroff";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Lizard RR Off"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( !rroff ) {
					obj.rroffToggle();
					rroff = true;
				}				
				obj.setSetup(MafiaSession.LIZARDRROFF);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("masonrelay")) {
				currentSetup = "masonrelay";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Mason Relay"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.MASONRELAY);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("powermillers")) {
				currentSetup = "powermillers";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Power Millers"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.POWERMILLERS);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("shobombs")) {
				currentSetup = "shobombs";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Shobombs"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.SHOBOMBS);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("vip")) {
				currentSetup = "vip";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("VIP"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.VIP);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("basic20")) {
				currentSetup = "basic20";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Basic 20"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.BASIC20);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("pie25")) {
				currentSetup = "triplecamp";
				obj.noextrakp();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Pie25"+autoHostText);
				if ( hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.PIE25);
				displayInfo( currentSetup );
			//} //else if (command.equalsIgnoreCase("cons8")) {
			//	obj.setSetup(MafiaSession.CONSIGLIERE_8);
			//} //else if (command.equalsIgnoreCase("cons10")) {
			//	obj.setSetup(MafiaSession.CONSIGLIERE_10);
			//} //else if (command.equalsIgnoreCase("12m")) {
			//	obj.setSetup(MafiaSession.DEF_MAF_12);
			//} //else if (command.equalsIgnoreCase("18m")) {
			//	obj.setSetup(MafiaSession.DEF_MAF_18);
			}  else if (command.equalsIgnoreCase( "superposition" )) {
				currentSetup = "superposition";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Superposition (semi-open)"+autoHostText);
				if ( !hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = true;
				}
				if ( !rroff ) {
					obj.rroffToggle();
					rroff = true;
				}
				Random rand = new Random();
			    String randomElement = MafiaSession.SUPERPOS.get(rand.nextInt(MafiaSession.SUPERPOS.size()));
				obj.setSetup( randomElement );
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase( "notnot" )) {
				currentSetup = "notnot";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Double negative (semi-open)"+autoHostText);
				if ( !hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = true;
				}
				if ( !rroff ) {
					obj.rroffToggle();
					rroff = true;
				}
				Random rand = new Random();
			    String randomElement = MafiaSession.DOUBLENEG.get(rand.nextInt(MafiaSession.DOUBLENEG.size()));
				obj.setSetup( randomElement );
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase( "carbon14" )) {
				currentSetup = "carbon14";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Carbon-14 (semi-open)"+autoHostText);
				// https://mafiagg.fandom.com/wiki/MafiaJJanitorial
				if ( !hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = true;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				Random rand = new Random();
			    String randomElement = MafiaSession.CARBON14.get(rand.nextInt(MafiaSession.CARBON14.size()));
				obj.setSetup( randomElement );
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase( "matrix6" )) {
				currentSetup = "matrix6";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Matrix6.gg (semi-open)"+autoHostText);
				// https://mafiagg.fandom.com/wiki/MafiaJJanitorial
				if ( !hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = true;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				Random rand = new Random();
			    String randomElement = MafiaSession.MATRIX6.get(rand.nextInt(MafiaSession.MATRIX6.size()));
				obj.setSetup( randomElement );
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase( "newd3" )) {
				currentSetup = "newd3";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("NewD3 M (semi-open)"+autoHostText);
				// https://mafiagg.fandom.com/wiki/MafiaJJanitorial
				if ( !hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = true;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				Random rand = new Random();
			    String randomElement = MafiaSession.NEWD3.get(rand.nextInt(MafiaSession.NEWD3.size()));
				obj.setSetup( randomElement );
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase( "mafiajjani" )) {
				currentSetup = "mafiajjani";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("MafiaJJanitorial (semi-open)"+autoHostText);
				// https://mafiagg.fandom.com/wiki/MafiaJJanitorial
				if ( !hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = true;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				Random rand = new Random();
			    String randomElement = MafiaSession.MAFIAJJANI.get(rand.nextInt(MafiaSession.MAFIAJJANI.size()));
				obj.setSetup( randomElement );
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase( "sodium24" )) {
				currentSetup = "sodium24";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Sodium-24 (semi-open)"+autoHostText);
				if ( !hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = true;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				Random rand = new Random();
			    String randomElement = MafiaSession.SODIUM24.get(rand.nextInt(MafiaSession.SODIUM24.size()));
				obj.setSetup( randomElement );
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase( "allstarssemi" )) {
				currentSetup = "allstarssemi";
				obj.twokptwo();
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("All Stars But (semi-open)"+autoHostText);
				if ( !hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = true;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				Random rand = new Random();
			    String randomElement = MafiaSession.ALLSTARSSEMI.get(rand.nextInt(MafiaSession.ALLSTARSSEMI.size()));
				obj.setSetup( randomElement );
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase( "revrol7" )) {
				currentSetup = "revrol7";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Reverse Roulette (semi-open)"+autoHostText);
				if ( !hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = true;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				Random rand = new Random();
			    String randomElement = MafiaSession.REVERSEROULETTE7.get(rand.nextInt(MafiaSession.REVERSEROULETTE7.size()));
				obj.setSetup( randomElement );
				displayInfo( currentSetup );
			} 
			else if (command.equalsIgnoreCase( "revrol11" )) {
				currentSetup = "revrol11";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Reverse Roulette (semi-open)"+autoHostText);
				if ( !hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = true;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				Random rand = new Random();
			    String randomElement = MafiaSession.REVERSEROULETTE11.get(rand.nextInt(MafiaSession.REVERSEROULETTE11.size()));
				obj.setSetup( randomElement );
				displayInfo( currentSetup );
			}	
			else if (command.equalsIgnoreCase( "revrol15" )) {
				currentSetup = "revrol15";
				obj.noextrakp();
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Reverse Roulette (semi-open)"+autoHostText);
				if ( !hiddenSetup ) {
					//obj.hideSetupToggle();
					hiddenSetup = true;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				Random rand = new Random();
			    String randomElement = MafiaSession.REVERSEROULETTE15.get(rand.nextInt(MafiaSession.REVERSEROULETTE15.size()));
				obj.setSetup( randomElement );
				displayInfo( currentSetup );
			}*/	
		else if (command.equalsIgnoreCase(".help")) {
			help();
		} else if (command.startsWith(".roomName ") && authorized) {
			command = command.replace(".roomName ", "");
			obj.changeRoomName(command);
//			obj.sendMessage("Command restricted.");
		} //else if (command.startsWith(".setMajority ")) {
		//	command = command.replace(".setMajority ", "");
		//	try {
		//		obj.editOptions();
		//		obj.gameSettings();
		//		if (command.equals("sm"))
		//			obj.setMajority(MafiaSession.SIMPLE_MAJORITY);
		//		if (command.equals("2"))
		//			obj.setMajority(MafiaSession.TWO_THIRDS_MAJORITY);
		//		if (command.equals("3"))
		//			obj.setMajority(MafiaSession.THREE_QUARTERS_MAJORITY);
		//		if (command.equals("nm"))
		//			obj.setMajority(MafiaSession.TURNED_OFF);
		//		obj.save();
		//	} catch (Exception e) {
		//		obj.sendMessage(e.getMessage());
		//	}
		//} else if (command.startsWith(".feedback ")) {
		//	obj.sendMessage("Your feedback has been recorded.");
		//} else if (command.equals(".pdt"))
		//	obj.sendMessage(obj.playerdUp() + "" + ((int) Math.random() * 10000 + 9999));
		else if (command.equalsIgnoreCase(".afkCheck") && authorized) {
			obj.afkCheck();
		}
		else if (votemode){
			obj.sendMessage("'"+command+"' is not a recognized command, or you are not authorized to use it.");
		}
		//else {
		//	
		//}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		
	}
 
	public void intro() {
		obj.sendMessage(
				"This is a hosting bot. I will do an afk check when the room fills and start after the afk check. I will become a player to boost but spectate before starting. Type '.help' to see available setups and other commands." );
		
		help();
		//try {
		//	Thread.sleep(1000);
		//} catch (InterruptedException e) {
		//}
	}
 
	public void help() {
//		obj.sendMessage(
//				"Available Commands : .playerUp | .playerDown | .setup <Setup Code> | .setDayTime [time in minutes 3-13] | .setNightTime [time in minutes(1-9)] | .roomName [new name for the room] .setMajority [Majority Code]");
//		obj.sendMessage(
//				"Majority Codes - nm= Turned off, sm = Simple Majority, 2 = Two-Thirds Majority and 3 = Three-Quarters Majority");
		//obj.sendMessage("Available Commands : .intro | .setDayTime [3-13] | .feedback [feedback] | .afkCheck");
		obj.sendMessage( "See https://github.com/Lav3ndr/mafia.gg-hosting-bot/blob/main/SETUPS.md for available setups.");
		if ( votemode ) {
			obj.sendMessage( "Use this syntax to vote for a setup: '.vote [SETUP COMMAND]' (e.g. '.vote pie7')."  );
			obj.sendMessage( "Try these commands, too! : .help | .info | .info [SETUP COMMAND] | .showvotes | .deck [DECK (case sensitive)] | .randomdeck | .nodeck | .impatient | .time | .credits");
			//obj.sendMessage( "Open setup codes : consifom | dethy | conplan | conplan+ | lovers | stc | camerashy | pie7 | bootcamp | gnh | pie7+ | bootcamp+ | sweetdreams | gnightless | voltronmicro | dnct | jani | abc | ascension | how2mm | oneshotcops | triplecamp | multemplar | uncertainty | powervilly13 | whiteflag | allstars13 | circus | allstars15 | solobombs | fatedduo | ibern | lizardrroff | masonrelay | powermillers | shobombs | vip | congress | basic20 | pie25" );
			//obj.sendMessage( "Semi-open/closed setup codes : superposition | carbon14 | revrol7 | matrix6 | notnot | newd3 | revrol11 | mafiajjani | sodium24 | revrol15 | allstarssemi  /\\/\\/\\   Try these commands, too! : .help | .info | .info [setup code] | .showvotes | .deck [deck] | .randomdeck | .nodeck | .impatient | .time | .credits");
		} else if ( autoHost ){
			obj.sendMessage( "Other available commands : .help | .info | .info [SETUP COMMAND] | .deck [DECK (case sensitive)] | .randomdeck | .nodeck | .impatient | .time | .credits" );
		} else if ( !autoHost ){
			obj.sendMessage( "Other available commands (any user): .help | .info | .deck [DECK (case sensitive)] | .randomdeck | .nodeck | .impatient | .credits" );
			obj.sendMessage( "Other available commands (original host only): .host | .lockdeck [DECK (case sensitive)] | .locknodeck | .kick [PLAYER] | .kickbot | .nukeroom" );
		}
	}
	
	public void displayInfo( Setup cur ) {
		String msg = "This setup is called " + cur.name + ". Read about it here: " + cur.url;
		if ( !cur.notes.equals( "" ) ) {
			msg = msg + ". " + cur.notes;
		}
		obj.sendMessage(msg);
	}
	/*
	public void displayInfoOld( Setup cur ) {

		if (cur.equals( "single") ) {
			msg = msg + "DEBUGGING";
		}
		if (cur.equals( "consifom") ) {
			msg = msg + "Consifom. Read about it here: https://mafiagg.fandom.com/wiki/Consifom";
		}
		else if (cur.equals( "conplan") ) {
			msg = msg + "5 Man Conplan. Read about it here: https://mafiagg.fandom.com/wiki/5_Man_Conplan";
		}
		else if (cur.equals( "dethy") ) {
			msg = msg + "Dethy. Read about it here: https://mafiagg.fandom.com/wiki/Dethy";
		}		
		else if (cur.equals( "lovers") ) {
			msg = msg + "Lovers. Read about it here: https://mafiagg.fandom.com/wiki/Lovers_Mafia";
		}
		else if (cur.equals( "conplan+") ) {
			msg = msg + "5 Man Conplan +. Read about it here: https://mafiagg.fandom.com/wiki/5_Man_Conplan";
		}
		else if (cur.equals( "stc") ) {
			msg = msg + "Shrink the Cult. Read about it here: https://mafiagg.fandom.com/wiki/Shrink_the_Cult";
		}
		else if (cur.equals( "camerashy") ) {
			msg = msg + "Camera Shy. Read about it here: https://mafiagg.fandom.com/wiki/Camera_Shy";
		}
		
		
		else if (cur.equals( "gnh") ) {
			msg = msg + "Guns n Hookers. Read about it here: https://mafiagg.fandom.com/wiki/Guns_%26_Hookers";
		}
		else if (cur.equals( "pie7") ) {
			msg = msg + "Pie E7. Read about it here: https://mafiagg.fandom.com/wiki/Pie_E7";
		}
		else if (cur.equals( "bootcamp") ) {
			msg = msg + "Bootcamp. Read about it here: https://mafiagg.fandom.com/wiki/Bootcamp";
		}
		else if (cur.equals( "pie7+") ) {
			msg = msg + "Pie E7 (nightstart - adjusted for 8p). Read about it here: https://mafiagg.fandom.com/wiki/Pie_E7";
		}
		else if (cur.equals( "bootcamp+") ) {
			msg = msg + "Bootcamp (nightstart - adjusted for 8p). Read about it here: https://mafiagg.fandom.com/wiki/Bootcamp";
		}
		else if (cur.equals( "dnct")) {
			msg = msg + "Dreams Never Come True. Read about it here: https://mafiagg.fandom.com/wiki/Dreams_Never_Come_True";
		}
		else if (cur.equals( "jani") ) {
			msg = msg + "Janitorial. Read about it here: https://mafiagg.fandom.com/wiki/Janitorial";
		}
		else if (cur.equals( "abc") ) {
			msg = msg + "ABC. Read about it here: https://mafiagg.fandom.com/wiki/ABC";
		}
		else if (cur.equals( "how2mm" )) {
			msg = msg + "How to play Marksman. Read about it here: https://mafiagg.fandom.com/wiki/How_To_Play_Marksman";
		}
		else if (cur.equals( "triplecamp" )) {
			msg = msg + "Triplecamp. Read about it here: https://mafiagg.fandom.com/wiki/Triplecamp";
		}
		
		else if (cur.equals( "congress") ) {
			msg = msg + "Congress. I'm supposed to warn you that reading the link is especially important for this one. Read about it here: https://mafiagg.fandom.com/wiki/Congress";
		}
		else if (cur.equals( "ascension") ) {
			msg = msg + "Ascension. SPECIAL RULES: Jekylls must protect each other in a circle at night, if possible, and the first team to lose, wins. Read about it here: https://mafiagg.fandom.com/wiki/Ascension";
		}
		else if (cur.equals( "sweetdreams") ) {
			msg = msg + "Sweet Dreams. Read about it here: https://mafiagg.fandom.com/wiki/Sweet_Dreams";
		}
		else if (cur.equals( "gnightless") ) {
			msg = msg + "Gnightless. Read about it here: https://mafiagg.fandom.com/wiki/Gnightless";
		}
		else if (cur.equals( "voltronmicro") ) {
			msg = msg + "V_ltr_n μ. Read about it here: https://mafiagg.fandom.com/wiki/V_ltr_n_%CE%BC";
		}
		else if (cur.equals( "oneshotcops") ) {
			msg = msg + "One Shot Cops. Read about it here: https://mafiagg.fandom.com/wiki/One-Shot_Cops";
		}
		else if (cur.equals( "multemplar") ) {
			msg = msg + "Multemplar. Read about it here: https://mafiagg.fandom.com/wiki/Multemplar";
		}
		else if (cur.equals( "uncertainty") ) {
			msg = msg + "Uncertainty. Read about it here: https://mafiagg.fandom.com/wiki/Uncertainty";
		}
		else if (cur.equals( "powervilly13") ) {
			msg = msg + "Power Villy 13. Read about it here: https://mafiagg.fandom.com/wiki/Power_Villy_13";
		}
		else if (cur.equals( "whiteflag") ) {
			msg = msg + "White Flag. Read about it here: https://mafiagg.fandom.com/wiki/White_Flag_(Setup)";
		}
		else if (cur.equals( "allstars13") ) {
			msg = msg + "All Stars (13p). It is two mafia kill power until two are left. Special rules: Only parity cop is allowed to do night action night 1. MAFIA IS NOT ALLOWED TO KILL N1.";
		}
		else if (cur.equals( "circus") ) {
			msg = msg + "Circus. Read about it here: https://mafiagg.fandom.com/wiki/Circus";
		}
		else if (cur.equals( "allstars15") ) {
			msg = msg + "All Stars (15p). It is two mafia kill power until two are left.";
		}
		else if (cur.equals( "solobombs") ) {
			msg = msg + "Solobombs. Read about it here: https://mafiagg.fandom.com/wiki/Solobombs";
		}
		else if (cur.equals( "fatedduo") ) {
			msg = msg + "Fated Duo. Read about it here: https://mafiagg.fandom.com/wiki/Fated_Duo";
		}
		else if (cur.equals( "ibern") ) {
			msg = msg + "iBern. Read about it here: https://mafiagg.fandom.com/wiki/IBern";
		}
		else if (cur.equals( "lizardrroff") ) {
			msg = msg + "Lizard RR Off. Read about it here: https://mafiagg.fandom.com/wiki/Lizard_RR_off";
		}
		else if (cur.equals( "masonrelay") ) {
			msg = msg + "Mason Relay. Read about it here: https://mafiagg.fandom.com/wiki/Mason_Relay";
		}
		else if (cur.equals( "powermillers") ) {
			msg = msg + "Power Millers. Read about it here: https://mafiagg.fandom.com/wiki/Power_Millers";
		}
		else if (cur.equals( "shobombs") ) {
			msg = msg + "Shobombs. Read about it here: https://mafiagg.fandom.com/wiki/Shobombs";
		}
		else if (cur.equals( "vip") ) {
			msg = msg + "VIP. Read about it here: https://mafiagg.fandom.com/wiki/VIP";
		}
		else if (cur.equals( "basic20") ) {
			msg = msg + "Basic 20. Read about it here: https://mafiagg.fandom.com/wiki/Basic20";
		}
		else if (cur.equals( "pie25") ) {
			msg = msg + "Pie 25. I have no hints for you. Coast until f3? idk";
		}
		
		else if (cur.equals( "superposition") ) {
			msg = msg + "Superposition. It is a semi-open setup with 8 possibilities. The currently shown setup is only one of the possibilities. Read about the possibilities here: https://mafiagg.fandom.com/wiki/Superposition";
		}
		else if (cur.equals( "carbon14") ) {
			msg = msg + "Carbon-14. It is a semi-open setup with 2 possibilities: cop+polygraph+3villy+2gf OR cop+polygraph+3villy+2mafia. The currently shown setup is only one of the possibilities. Read about the possibilities here: https://wiki.mafiascum.net/index.php?title=Carbon-14";
		}
		else if (cur.equals( "matrix6") ) {
			msg = msg + "Matrix6.gg. It is a semi-open setup with 6 possibilities. The currently shown setup is only one of the possibilities. Read about the possibilities here: https://mafiagg.fandom.com/wiki/Matrix6.gg";
		}
		else if (cur.equals( "notnot") ) {
			msg = msg + "Double negative. It is a semi-open setup with 2 possibilities. The currently shown setup is only one of the possibilities. See this link: https://imgur.com/a/Z9s71QU";
		}
		else if (cur.equals( "newd3") ) {
			msg = msg + "NewD3 M. It is a semi-open setup with 9 possibilities. The currently shown setup is only one of the possibilities. Read about the possibilities here: https://mafiagg.fandom.com/wiki/NewD3_M";
		}
		else if (cur.equals( "mafiajjani") ) {
			msg = msg + "MafiaJJanitorial. It is a semi-open setup with 4 possibilities similar to Janitorial. The currently shown setup is only one of the possibilities. Read about it here: https://mafiagg.fandom.com/wiki/MafiaJJanitorial";
		}
		else if (cur.equals( "sodium24") ) {
			msg = msg + "Sodium-24. It is a semi-open setup with 4 possibilities. The currently shown setup is only one of the possibilities. Read about it here: https://mafiagg.fandom.com/wiki/Sodium-24";
		}
		else if (cur.equals( "allstarssemi") ) {
			msg = msg + "All Stars But. It is a semi-open setup with 16 possibilities which is two kill power until two mafia are alive, and is effectively role reveal off due to bozoman. Bozoman exists to give hints about the setup to town. The currently shown setup is only one of the possibilities. See the possibilities here: https://imgur.com/EVN42r8";
		}
		else if (cur.equals( "revrol7") ||  cur.equals( "revrol11") || cur.equals( "revrol15") ) {
			msg = msg + "Reverse Roulette. It is a semi-open setup with 2 possibilities. The currently shown setup is only one of the possibilities. SPECIAL RULES: 1) TRAITOR WINS WITH KILLER 2) TOWN WINS ALONE IF MERLIN LIVES UNTIL FINAL 2. More info here: https://mafiagg.fandom.com/wiki/Reverse_roulette";
		}	
		obj.sendMessage(msg);
	} */
 
	public static void main(String[] args) {
		boolean firstTime = true;
		
		//boolean gameRunning = false;
		while (true) {
			currentSetup = new Setup( "", "", "", 0, "", "", "", "", "", "", "", new String[] {}, "" );
			waiting = false;
			//standingBy = true;
			summoner = "";
			tempAuth = new ArrayList<String>();
			toldToLeave = false;
			lockdeck = false;
			afk = false;
			App appObj = new App();
			if ( firstTime && autoHost ) {
				obj.hostUnlisted();
				obj.soundOff();
			}
			else if ( !autoHost ) {
				// wait on main page until summoned
				while (true ) {
					summoner = obj.summoned( summonPhrase );
					if ( summoner != "" ){
						if ( !tempAuth.contains( summoner ) ){
							tempAuth.add( summoner );
						}						
						break;
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
					obj.refresh();
				}
			}
			
			// unhide the setup until game start
			obj.setHidden("off");

			List<List<String>> chatQueue = new ArrayList<List<String>>();
			List<String> lastInLine = new ArrayList<String>();
			lastInLine.add( "" );
			lastInLine.add( "" );
			lastInLine.add( "" );
			
			if ( autoHost ) {
				appObj.intro();
			}
			else if ( !autoHost && !obj.gameRunning() ) {
				appObj.greet( summoner );
			}
			if (!obj.gameRunning() ) {
				appObj.resetVotes();
				appObj.hardResetTimer();
				appObj.resetDeckChanges();
			}
			
			
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			if ( autoHost ) {
				try {
					appObj.execute(".setup " + currentSetup, auth.get( 0 ) );
					appObj.execute(".time",  auth.get( 0 ) );
				} catch (Exception e ) {
					return;
				}
				if ( listed ){
					obj.listToggle(); // always makes listed?
				}
			}
			
			long starttime = 0;
			long losthosttime = System.nanoTime();
			int patience = 15;
			//gameRunning = false;
			while (true) {
				// If autoHost, try to host a new room if possible
				if ( autoHost && obj.tryHostNew() ) {
					firstTime = false;
					break;
				}
				// If autoHost, continue if the game is running 
				if ( autoHost && obj.gameRunning() ) {
					continue;
				}
				// If !autoHost and game running or told to leave, or not host for a while, quit to main lobby
				if ( !autoHost && ( ( obj.gameRunning() || toldToLeave ) || ( System.nanoTime() - losthosttime ) / 1000000000 > hostPatience ) ){
					if ( ( System.nanoTime() - losthosttime ) / 1000000000 > hostPatience ) {
						obj.sendMessage( "Hosting control lost for "+ Long.toString( hostPatience ) + " seconds. Please summon me again if you need me.");
					}
					if ( !obj.gameRunning() && obj.pregamehosting() ) {
						obj.giveHost( summoner );
					}
					obj.goHome();	
					break;
				}
				
				// Update the command queue
				obj.updateChatSmart( chatQueue, lastInLine );
				
				// Check if not hosting, pause and unplayer if so
				if ( !obj.pregamehosting() ) {
					if (!waiting) {
						if ( autoHost ) {
							obj.sendMessage( "Someone else has host. Pausing execution. Return host to continue.");
						}
						losthosttime = System.nanoTime();
					}
					try {
						obj.playerDown();
					} catch (Exception e ) {
						e.printStackTrace();
						//return;
					}
					waiting = true;
					continue;
				}
				else {
					if ( waiting ) {
						if ( autoHost ) {
							obj.sendMessage( "Thank you for giving host back. Resuming execution.");
						}
						else {
							appObj.hostHelp2( summoner );					
						}
					}
					waiting = false;
				}
				
				// execute the next command
				if ( chatQueue.size() > 0 ) {
					try {
						lastInLine = appObj.popOne( chatQueue );
					} catch ( Exception e ) {
						e.printStackTrace();
						System.out.println( "Failed successfully.");
						return;
					}					
				}
				
				if ( toldToLeave ) {
					break;
				}
				
				// grab system time
				long curtime = System.nanoTime();
				
				// Do an afk check if the lobby is full.
				if (obj.playerdUp() >= obj.totalPlayers() && !afk) {
					try {
						obj.playerDown();
					} catch (Exception e ) {
						e.printStackTrace();
						//return;
					}
					obj.sendMessage("AFK check 3...2...1..." + Integer.toString( patience ) + " seconds to rejoin.");
					obj.afkCheck();
					if ( !currentSetup.name.equals( "" ) ){
						appObj.displayInfo( currentSetup );
					}					
					afk = !afk;
					starttime = System.nanoTime();				
				}
				// Reset afk if more than patience seconds have passed.
				else if ( afk && ( curtime - starttime ) / 1000000000 > patience ) {
					obj.sendMessage("We will need to do another AFK check.");
					afk = false;
				}
				// Otherwise try to start the game.
				else if (afk && obj.playerdUp() == obj.totalPlayers() ) {
					try {
						if ( appObj.toStart() > 0 && obj.playerdUp() != 0 ) {
							appObj.execute( ".time", auth.get( 0 ) );
						}
						else {
							if ( currentSetup.hidden.equals( "on" ) ) {
								obj.setHidden("on");
								obj.sendMessage( "Rolling and hiding setup...");
								appObj.execute( ".setup " + currentSetup, auth.get( 0 ));								
							}
							obj.sendMessage("GLHF! DON'T AFK!");							
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
							
							} 
							obj.startGame();
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
							}
							if ( !obj.gameRunning() ) {
								obj.sendMessage("Someone unplayered. Let's try again...");
								obj.setHidden( "off" );
							}
							//gameRunning = true;
						}						
					} catch (NoSuchElementException e) {
					
						e.printStackTrace();
					} catch (Exception e) {
						obj.sendMessage("Unexpected error. Let's try again...");
						e.printStackTrace();
					}
				}
				// Boost if more than patience seconds have passed and no one is playered up.
				if ( ( curtime - starttime ) / 1000000000 > patience && obj.playerdUp() == 0 ) {
					try {
						obj.playerUp();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}				
			}
		}
	}

	private void greet(String summoner) {
		obj.sendMessage( "Hello " + summoner + ". I joined your room because you started your room name with '.helphost'. I can help you host any of the setups available here: https://github.com/Lav3ndr/mafia.gg-hosting-bot/blob/main/SETUPS.md. Pass host to me to begin." );
	}
	
	private void hostHelp2( String summoner ) {
		obj.sendMessage( "Thank you, " + summoner + "! Type '.setup [SETUP COMMAND]' (e.g. '.setup jani') to change the setup.");
		obj.sendMessage( "The setup command for each setup can be found in the second column of the setup table. When you have settled on a setup, you may either type '.host' to reclaim host from me, or you may leave me as host. I will automatically do an afk check and host the game for you when the lobby fills. Type '.help' to see additional commands." );
		// TODO Auto-generated method stub		
	}

	public List<String> popOne( List<List<String>> chat ) throws Exception {
		int size = chat.size();
		List<String> lastInLine = new ArrayList<String>();

		String firstuser = chat.get( 0 ).get( 1 );
		String firstmsg = chat.get( 0 ).get( 2 );
		String lasttime = chat.get( size - 1 ).get( 0 );
		String lastuser = chat.get( size - 1 ).get( 1 );
		String lastmsg = chat.get( size - 1 ).get( 2 );
		lastInLine.add(lasttime);
		lastInLine.add(lastuser);
		lastInLine.add(lastmsg);
		chat.remove( 0 );
		if (firstmsg.startsWith(".") ) {
			try {

				
				if ( !waiting ) {
					execute(firstmsg, firstuser);
					System.out.println( "Command registered: user="+firstuser+ ", command="+ firstmsg);
					//System.out.println( lastInLine );
					return lastInLine;
				} else {
					if ( firstmsg.equals(".pause") ) {
						execute(firstmsg, firstuser);
						System.out.println( "Command registered: user="+firstuser+ ", command="+ firstmsg);
						//System.out.println( lastInLine );
						return lastInLine;
						}
					}
					
				} catch (NoSuchElementException e) {
					e.printStackTrace();
 
				} catch (Exception e) {
					System.out.println( e.getMessage() );
					if ( e.getMessage().equals( "Terminating execution.")  ){
					throw e;
				}
				System.out.println( "slow down there");
				e.printStackTrace();
			}
		}
		else if (size - 1  == 0 ){
			return lastInLine;
		}
		else {
			return popOne( chat );
		}
		System.out.println( "You shouldn't be here!");
		return lastInLine;	//shouldn't get here?
		
	} 
 
	//public void queueChatOld(List<String> chat, List<String> users, int n) throws Exception {
	//	int size = chat.size();
	//	for (int i = size - n; i < size; i++) {
	//		//System.out.println(i + "|" + chat.get(i));
	//		if (chat.get(i).startsWith(".") ) {
	//			try {
					//String user = obj.getUsername().get( obj.getUsername().size() - 1 );
	//				String user = users.get(i);
					
	//				if ( !waiting ) {
	//					queue(chat.get(i), user);
	//					System.out.println( "Command registered: user="+user+ ", command="+ chat.get(i));
	//				} else {
	//					if ( chat.get(i).equals(".pause") ) {
	//						queue(chat.get(i), user);
	//						System.out.println( "Command registered: user="+user+ ", command="+ chat.get(i));
	//					}
	//				}
					
	//			} catch (NoSuchElementException e) {
	//				e.printStackTrace();
 
	//			} catch (Exception e) {
	//				System.out.println( e.getMessage() );
	//				if ( e.getMessage().equals( "Terminating execution.")  ){
	//					throw e;
	//				}
	//				System.out.println( "slow down there");
	//				e.printStackTrace();
	//			}
	//		}
	//	}
	//} 
}