package mafia.gg.bot;

import java.util.Arrays;

//import java.util.List;

public class Setup {
	String name;
	String command;
	String url; //url for more info about the setup
	int playerCount;
	String start; //night,informed day, or uninformed day
	String rr; //on,off,or alignment
	String forcevote; //on,off
	String majvote; //off,1/2,2/3,3/4
	String extrakp; //off,always,2kp1,2kp2,etc
	String deadlock; //rand,init,resp,disable
	String hidden; //on,off
	String[] codes; //list of setup codes
	String notes; //additional rules/other comments
		
	Setup( String name, String command, String url, int playerCount, String start, String rr, String forcevote, String majvote, String extrakp, String deadlock, String hidden, String[] codes, String notes )
	   {
	      this.name = name;
	      this.command = command;
	  	  this.url = url; //url for more info about the setup
	  	  this.playerCount = playerCount;
	  	  this.start = start; //night,informed day, or uninformed day
	  	  this.rr = rr; //on,off,or alignment
	  	  this.forcevote = forcevote; //on,off
	  	  this.majvote = majvote; //off,1/2,2/3,3/4
	  	  this.extrakp = extrakp; //off,2kpa,2kp1,2kp2,etc
	  	  this.deadlock = deadlock; //rand,init,resp,disable
	  	  this.hidden = hidden; //on,off
	  	  this.codes = codes; //list of setup codes
	  	  this.notes = notes; //additional rules/other comments
	   }

	@Override
	public String toString() {
		return "Setup [name=" + name + ", command=" + command + ", url=" + url + ", playerCount=" + playerCount
				+ ", start=" + start + ", rr=" + rr + ", forcevote=" + forcevote + ", majvote=" + majvote + ", extrakp="
				+ extrakp + ", deadlock=" + deadlock + ", hidden=" + hidden + ", codes=" + Arrays.toString(codes)
				+ ", notes=" + notes + "]";
	}
	
	
}
