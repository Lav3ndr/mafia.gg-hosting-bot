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
	
	private static Map<String, ArrayList<String>> votes = new HashMap<String, ArrayList<String>>();
	
//	private static long ms = 0;
	private static boolean afk = false;
	
	private static boolean listed = false;
	private static boolean rroff = true;
	//private static boolean daystart = false;
	private static boolean hiddenSetup = false;
	private static String currentSetup = "conplan";
	private static String autoHostText = " (Lavenbot)";//" (autohosted)";
	private static boolean waiting = false;
	private static int votethreshold = 3;
	private static long timerstart = System.nanoTime();
	private static long timeToStart = 90;
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
        
        votes.put("superposition", new ArrayList<String>());
        votes.put("carbon14", new ArrayList<String>());
        votes.put("notnot", new ArrayList<String>());
        votes.put("mafiajjani", new ArrayList<String>());
        votes.put("sodium24", new ArrayList<String>());  
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
			return "'" +stp + "' is not a valid setup code.";
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
 
	public void queue(String command, String user) throws Exception {
		boolean authorized = isAuthorized( user );
		if (command.equals(".intro") && authorized)
			intro();
		else if (command.equals(".exit") && authorized) {
			obj.nightStart();
			obj.majOff();
			if ( hiddenSetup ) {
				obj.hideSetupToggle();
				hiddenSetup = false;
			}
			if ( !rroff ) {
				obj.rroffToggle();
				rroff = true;
			}
			obj.setSetup( "" );
			try {
				obj.playerDown();
				//System.out.println( "hi" );
				//appObj.queue(".playerDown");
			} catch (Exception e ) {
				return;
			}
			obj.afkCheck();
			obj.startGame();
			obj.sendMessage("Thanks for playing everyone! Signing off...");
			throw new Exception( "Terminating execution.");
		}
		else if (command.equals(".impatient")) {
			int onlyneed = obj.totalPlayers() - obj.playerdUp();
			obj.sendMessage( Integer.toString( onlyneed ) + " more" );
		}
		else if (command.equalsIgnoreCase(".info")) {
			displayInfo( currentSetup );
		}
		else if (command.startsWith(".info ")) {
			command = command.replace(".info ", "");
			if (!votes.containsKey( command )) {
				obj.sendMessage( "'" + command + "' is not a valid setup code.");
			}
			displayInfo( command );
		}
		else if (command.equalsIgnoreCase(".credits")) {
			obj.sendMessage( "This bot was programmed in Java by Lavender (Lavender#8704 on Discord), though the original code was written and given to him by someone else. It uses a library called Selenium (https://www.selenium.dev/) to open a browser and interact with it by referencing the source html.");			
		}
		else if (command.equalsIgnoreCase(".garbageman")) {
			obj.sendMessage("gotta figure out how to get off my butt and work on my real side projects");
		}
		else if (command.equalsIgnoreCase(".xinde")) {
			obj.sendMessage(".deck Love Live");
		}
		else if (command.equalsIgnoreCase(".auryxx")) {
			obj.sendMessage(".deck Love Live");
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
			obj.randomDeck();
			obj.sendMessage( user + " chose a random deck.");
			incChanges();
			
		}
		else if (command.equalsIgnoreCase(".nodeck") && deckChanges - changes > 0 ) {
			obj.noDeck();
			obj.sendMessage( user + " chose to use no deck.");
			incChanges();
		}
		else if (command.startsWith(".deck ") && deckChanges - changes > 0 ) {
			command = command.replace(".deck ", "");
			if ( obj.setDeck( command ) )
			{
				obj.sendMessage( user + " has successfully set the deck.");
				incChanges();
			}
			else {
				obj.sendMessage( "Unable to set deck. Deck selection is case-sensitive and must contain the exact string specified." );
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
 
		} else if (command.startsWith(".vote ")) {
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
				queue( ".setup "+ newSetup, auth.get( 0 ));
			}
		} else if (command.startsWith(".votethresh ") && authorized ) {
			command = command.replace(".votethresh ", "");
			try {
				int n = Integer.parseInt(command);
				votethreshold = n;
				obj.sendMessage( "Vote threshold has been adjusted to "+Integer.toString( n));
				queue( ".vote ", auth.get( 0 ));
			} catch (NumberFormatException e) {
				obj.sendMessage(e.getMessage());
			}
			
		} else if (command.equalsIgnoreCase(".resetvotes") && authorized ) {
			obj.sendMessage( user + " has reset the votes.");
			resetVotes();			
		} else if (command.equalsIgnoreCase(".showvotes")) {
			obj.sendMessage( displayVotes() );
		} else if (command.startsWith(".setup ") && authorized) {
			command = command.replace(".setup ", "");
			if (command.equalsIgnoreCase("single")) {
				currentSetup = "single";
				obj.nightStart();
				obj.majOff();
				obj.changeRoomName("Autohost testing");
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( !rroff ) {
					obj.rroffToggle();
					rroff = true;
				}
				obj.setSetup(MafiaSession.SINGLE_PLAYER);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("consifom")) {
				currentSetup = "consifom";
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Consifom"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.nightStart();
				obj.majOff();
				obj.changeRoomName("5 Man Conplan"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Dethy"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				obj.setSetup(MafiaSession.DETHY);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("conplan+")) {
				currentSetup = "conplan+";
				obj.nightStart();
				obj.majOff();
				obj.changeRoomName("5 Man Conplan + Lover"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( !rroff ) {
					obj.rroffToggle();
					rroff = true;
				}
				obj.setSetup(MafiaSession.CONPLAN_6);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("lovers")) {
				currentSetup = "lovers";
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Lovers"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}
				obj.setSetup(MafiaSession.LOVERS);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("gnh")) {
				currentSetup = "gnh";
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Guns n Hookers"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Pie E7"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Bootcamp"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Pie E7"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Bootcamp"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Dreams Never Come True"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("Janitorial"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.nightStart();
				obj.majOn();
				obj.changeRoomName("ABC"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("How To Play Marksman"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Triplecamp"+autoHostText);
				if ( hiddenSetup ) {
					obj.hideSetupToggle();
					hiddenSetup = false;
				}
				if ( rroff ) {
					obj.rroffToggle();
					rroff = false;
				}				
				obj.setSetup(MafiaSession.TRIPLECAMP);
				displayInfo( currentSetup );
			} else if (command.equalsIgnoreCase("cons8")) {
				obj.setSetup(MafiaSession.CONSIGLIERE_8);
			} else if (command.equalsIgnoreCase("cons10")) {
				obj.setSetup(MafiaSession.CONSIGLIERE_10);
			} else if (command.equalsIgnoreCase("12m")) {
				obj.setSetup(MafiaSession.DEF_MAF_12);
			} else if (command.equalsIgnoreCase("18m")) {
				obj.setSetup(MafiaSession.DEF_MAF_18);
			}  else if (command.equalsIgnoreCase( "superposition" )) {
				currentSetup = "superposition";
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Superposition (semi-open)"+autoHostText);
				if ( !hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Double negative (semi-open)"+autoHostText);
				if ( !hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Carbon-14 (semi-open)"+autoHostText);
				// https://mafiagg.fandom.com/wiki/MafiaJJanitorial
				if ( !hiddenSetup ) {
					obj.hideSetupToggle();
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
			} else if (command.equalsIgnoreCase( "mafiajjani" )) {
				currentSetup = "mafiajjani";
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("MafiaJJanitorial (semi-open)"+autoHostText);
				// https://mafiagg.fandom.com/wiki/MafiaJJanitorial
				if ( !hiddenSetup ) {
					obj.hideSetupToggle();
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
				obj.dayStart();
				obj.majOn();
				obj.changeRoomName("Sodium-24 (semi-open)"+autoHostText);
				if ( !hiddenSetup ) {
					obj.hideSetupToggle();
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
			}
		} else if (command.equalsIgnoreCase(".help")) {
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
		else {
			obj.sendMessage("'"+command+"' is not a recognized command.");
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		
	}
 
	public void intro() {
		obj.sendMessage(
				"This is a hosting bot. I will do an afk check when the room fills and start after the afk check. I will become a player to boost but spectate before starting. Type '.help' to see available setups and other commands." );
		help();//try {
		//	Thread.sleep(15000);
		//} catch (InterruptedException e) {
		//}
	}
 
	public void help() {
//		obj.sendMessage(
//				"Available Commands : .playerUp | .playerDown | .setup <Setup Code> | .setDayTime [time in minutes 3-13] | .setNightTime [time in minutes(1-9)] | .roomName [new name for the room] .setMajority [Majority Code]");
//		obj.sendMessage(
//				"Majority Codes - nm= Turned off, sm = Simple Majority, 2 = Two-Thirds Majority and 3 = Three-Quarters Majority");
		//obj.sendMessage("Available Commands : .intro | .setDayTime [3-13] | .feedback [feedback] | .afkCheck");
		obj.sendMessage("Use this syntax to vote for a setup: '.vote [setup code]' (e.g. '.vote pie7')   /\\/\\/\\   Open setup codes : consifom | dethy | conplan | conplan+ | lovers | pie7 | bootcamp | gnh | pie7+ | bootcamp+ | dnct | jani | abc | how2mm | triplecamp   /\\/\\/\\   Semi-open/closed setup codes : superposition | carbon14 | notnot | mafiajjani | sodium24   /\\/\\/\\   Try these commands, too! : .help | .info | .info [setup code] | .showvotes | .deck [deck] | .randomdeck | .nodeck | .impatient | .time | .credits");
	}
	
	public void displayInfo( String cur ) {
		String msg = "This setup is called ";
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
			msg = msg + "Dreams never come true. Read about it here: https://mafiagg.fandom.com/wiki/Dreams_Never_Come_True";
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
		
		else if (cur.equals( "superposition") ) {
			msg = msg + "Superposition. It is a semi-open setup with 8 possibilities. Read about the possibilities here: https://mafiagg.fandom.com/wiki/Superposition";
		}
		else if (cur.equals( "carbon14") ) {
			msg = msg + "Carbon-14. It is a semi-open setup with 2 possibilities. Read about the possibilities here: https://wiki.mafiascum.net/index.php?title=Carbon-14";
		}
		else if (cur.equals( "notnot") ) {
			msg = msg + "Double negative. It is a semi-open setup with 2 possibilities. See this link: https://imgur.com/a/Z9s71QU";
		}
		else if (cur.equals( "mafiajjani") ) {
			msg = msg + "MafiaJJanitorial. It is a semi-open setup with 4 possibilities similar to Janitorial. Read about it here: https://mafiagg.fandom.com/wiki/MafiaJJanitorial";
		}
		else if (cur.equals( "sodium24") ) {
			msg = msg + "Sodium-24. It is a semi-open setup with 4 possibilities. Read about it here: https://mafiagg.fandom.com/wiki/Sodium-24";
		}
		obj.sendMessage(msg);
	}
 
	public static void main(String[] args) {
		//App appObj = new App();
		//String roonName = "hostbot";
		//String roomName = "hostbot";
		boolean firstTime = true;
		boolean gameRunning = false;
		while (true) {
			
			afk = false;
			App appObj = new App();
			if ( firstTime) {
				obj.hostUnlisted();
				obj.soundOff();
				
			}
			
			List<List<String>> chatQueue = new ArrayList<List<String>>();
			List<String> lastInLine = new ArrayList<String>();
			lastInLine.add( "" );
			lastInLine.add( "" );
			lastInLine.add( "" );
			
			
			
			//List<String> chat = new ArrayList<String>();//App.obj.getChat();
			//List<String> users = new ArrayList<String>();//App.obj.getUsername();
			//if ( !firstTime ) {
			//	System.out.println( "we made it farther");
			//}
			appObj.intro();
			appObj.resetVotes();
			appObj.hardResetTimer();
			appObj.resetDeckChanges();
			
			//System.out.println( currentSetup );
			try {
				appObj.queue(".setup " + currentSetup, auth.get( 0 ) );
				appObj.queue(".time",  auth.get( 0 ) );
			} catch (Exception e ) {
				return;
			}
			//try {
			//	obj.playerUp();
			//} catch (Exception e ) {
				
			//}
			// appObj.displayInfo( currentSetup );
			//appObj.queue(".setDayTime 5");
			//System.out.println("Welcome"); 
			//appObj.queue(".setNightTime 1");		
			//appObj.queue(".setMajority nm");
			//appObj.queue(".roomName Automated Hosting");
			// uncomment next line to always make games listed
			if ( listed ){
				obj.listToggle(); // always makes listed?
			}
			
			long starttime = 0;
			int patience = 15;
			gameRunning = false;
			while (true) {
				if ( obj.tryHostNew() ) {
					firstTime = false;
					break;
				}
				if (gameRunning) {
					continue;
				}
				long curtime = System.nanoTime();
				
				//if (waiting) {
				//	continue;
				//}
				
				//Prioritize updating the command queue if there is still time on the clock.
				//obj.updateChatSmart( chatQueue, lastRead );
				//System.out.println( chatQueue );
				//if ( chatQueue.size() > 0 && appObj.toStart() > 0 ) {
				//	try {
				//		lastRead = appObj.queueChat( chatQueue );
				//	} catch ( Exception e ) {
				//		e.printStackTrace();
				//		System.out.println( "Failed successfully.");
				//		return;
				//	}					
				//}
				
				//Prioritize updating the command queue if there is still time on the clock.
				//int n = obj.updateChat(chat, users);
				//if (n > 0 && appObj.toStart() > 0) {
				//	try {
				//		appObj.queueChat(chat, users, n);
				//	} catch ( Exception e ) {
				//		e.printStackTrace();
				//		System.out.println( "Failed successfully.");
				//		return;
				//	}
				//}


				// Prioritize starting the game if there is no time on the clock.
				// Do an afk check if the lobby is full.
				if (obj.playerdUp() >= obj.totalPlayers() && !afk) {
					try {
						obj.playerDown();
						//appObj.queue(".playerDown");
					} catch (Exception e ) {
						e.printStackTrace();
						return;
					}
					obj.sendMessage("AFK check 3...2...1..." + Integer.toString( patience ) + " seconds to rejoin.");
					obj.afkCheck();
					appObj.displayInfo( currentSetup );
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
							appObj.queue( ".time", auth.get( 0 ) );
						}
						else {
							obj.sendMessage("GLHF! DON'T AFK!");
							obj.startGame();
							gameRunning = true;
						}						
					} catch (Exception e) {
						obj.unexpectedClose();
						e.printStackTrace();
						obj.sendMessage("Nevermind, let's try again...");
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
				
				
				// Update the command queue
				obj.updateChatSmart( chatQueue, lastInLine );
				//System.out.println( chatQueue );
				if ( chatQueue.size() > 0 ) {
					try {
						lastInLine = appObj.popOne( chatQueue );
					} catch ( Exception e ) {
						e.printStackTrace();
						System.out.println( "Failed successfully.");
						return;
					}					
				}
				
				// Update the command queue
				//n = obj.updateChat(chat, users);
				//if (n > 0 && appObj.toStart() == 0) {
				//	try {
				//		appObj.queueChat(chat, users, n);
				//	} catch ( Exception e ) {
				//		e.printStackTrace();
				//		System.out.println( "Failed successfully.");
				//		return;
				//	}
				//}
				//System.out.println( "In main loop");
			}
		}
	}
	
	public List<String> popOne( List<List<String>> chat ) throws Exception {
		int size = chat.size();
		List<String> lastInLine = new ArrayList<String>();
		//if ( size == 1 ) {
		//	lastInLine.add( "" );
		//	lastInLine.add( "" );
		//	lastInLine.add( "" );
		//	return lastInLine;
		//}
		//String time = "";
		//String user = "";
		//String msg = "";
		
		//System.out.println( size );
		//System.out.println(i + "|" + chat.get(i));
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
				//String user = obj.getUsername().get( obj.getUsername().size() - 1 );
				//String user = chat.get(i).get( 1 );
				
				if ( !waiting ) {
					queue(firstmsg, firstuser);
					System.out.println( "Command registered: user="+firstuser+ ", command="+ firstmsg);
					//chat = new ArrayList<List<String>>();
					System.out.println( lastInLine );
					return lastInLine;
				} else {
					if ( firstmsg.equals(".pause") ) {
						queue(firstmsg, firstuser);
						System.out.println( "Command registered: user="+firstuser+ ", command="+ firstmsg);
						//chat = new ArrayList<List<String>>();
						System.out.println( lastInLine );
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
 
	public void queueChatOld(List<String> chat, List<String> users, int n) throws Exception {
		int size = chat.size();
		for (int i = size - n; i < size; i++) {
			//System.out.println(i + "|" + chat.get(i));
			if (chat.get(i).startsWith(".") ) {
				try {
					//String user = obj.getUsername().get( obj.getUsername().size() - 1 );
					String user = users.get(i);
					
					if ( !waiting ) {
						queue(chat.get(i), user);
						System.out.println( "Command registered: user="+user+ ", command="+ chat.get(i));
					} else {
						if ( chat.get(i).equals(".pause") ) {
							queue(chat.get(i), user);
							System.out.println( "Command registered: user="+user+ ", command="+ chat.get(i));
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
		}
	} 
}