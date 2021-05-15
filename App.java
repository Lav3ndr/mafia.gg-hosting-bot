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
	private static String welcomeString = ". I joined your room because you started your room name with '.helphost'. I can help you host any of the setups available here: https://github.com/Lav3ndr/mafia.gg-hosting-bot/blob/main/SETUPS.md. Pass host to me to begin.";
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
			return "'" +stp + "' is not a valid setup command, "+usr+"! Type '.help' to see available setup codes.";
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
			obj.setSetup( "", true );
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
				return;
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
			// show setup until game starts, unless special
			if ( !obj.specials.contains( currentSetup.name ) ){
				obj.setHidden("off");							
			}
			String host = "";
			if ( !autoHost ) {
				host = tempAuth.get( 0 );
			}
			obj.changeRoomName(currentSetup.name+" (hosted by "+ host +")");
			if ( !currentSetup.name.equals( "" ) ) {
				displayInfo( currentSetup );
			}
		} else if (command.startsWith( ".roles ") && authorized ) {
			String[] params = command.replace( ".roles ", "").split("\\s+",2);
			try {
				obj.adjRole( params[1], Integer.parseInt( params[0] ), true);
				//int amt = Integer.parseInt( params[0] );
				
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				obj.sendMessage(e.getMessage());
			}
		}
			
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
	
	public static void displayInfo( Setup cur ) {
		String msg = "This setup is called " + cur.name + ". Read about it here: " + cur.url;
		obj.sendMessage(msg);
		if ( !cur.notes.equals( "" ) ) {
			obj.sendMessage( cur.notes );
		}		
	}

 
	public static void main(String[] args) {
		boolean firstTime = true;
		
		//boolean gameRunning = false;
		while (true) {
			currentSetup = new Setup( "", "", "", 0, "", "", "", "", "", "", "", new String[] {}, "" );
			waiting = true;
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
				if ( !autoHost ) {
					if ( toldToLeave || obj.gameRunning() ) {
						if ( obj.pregamehosting() ) {
							obj.giveHost( summoner );							
						}
						obj.goHome();
						break;
						
						//|| toldToLeave ) || ( System.nanoTime() - losthosttime ) / 1000000000 > hostPatience ) )
					}	else if ( !obj.gameRunning() && !obj.pregamehosting() && ( System.nanoTime() - losthosttime ) / 1000000000 > hostPatience  && losthosttime != 0 ) {
						obj.sendMessage( "Hosting control lost for "+ Long.toString( hostPatience ) + " seconds. Please summon me again if you need me.");
						obj.goHome();
						break;
					}
				}
				
				// Update the command queue
				obj.updateChatSmart( chatQueue, lastInLine, welcomeString );
				
				// Check if not hosting, pause and unplayer if so
				if ( !obj.pregamehosting() ) {
					
					if (!waiting) {
						losthosttime = System.nanoTime();
						if ( autoHost ) {
							obj.sendMessage( "Someone else has host. Pausing execution. Return host to continue.");
						}
						
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
							if ( currentSetup.hidden.equals( "on" ) && !obj.specials.contains( currentSetup.name ) ){
								//obj.setHidden( "on");
								obj.sendMessage( "Rolling and hiding setup...");
								currentSetup = obj.setup( currentSetup.command, currentSetup );
								//appObj.execute( ".setup " + currentSetup.command, auth.get( 0 ));								
							}
							displayInfo( currentSetup );
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
		obj.sendMessage( "Hello " + summoner + welcomeString );
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