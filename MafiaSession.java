// this file contains the lower-level code which interacts with the browser
// forked by Lavender - original code and and lots of help comes from aRandomZy
package mafia.gg.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
//import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class MafiaSession {
	private static MafiaSession UniqueObject = null;
	final String URL = "https://mafia.gg";
	private String username, password;
	private WebDriver session;
	private Actions act;
	// private boolean autoChangeRoomName;
	private boolean unlistedMemory;
	// private String setInUse;
	// private List<WebElement> chat, usn;
	
	private List<Setup> setups;	
	
	/*
	public static final int TWO_THIRDS_MAJORITY = 66;
	public static final int TURNED_OFF = -1;
	public static final int SIMPLE_MAJORITY = 51;
	public static final int THREE_QUARTERS_MAJORITY = 75;
	public static final String SINGLE_PLAYER = "57a1";
	public static final String CONSIFOM = "19a1b5a1b61a1 ";
	public static final String CONPLAN_5 = "1547802152a1b18a1b31a1b40a1b52a1";
	public static final String DETHY = "75a1b7a1b23a1b33a1b37a1";
	public static final String LOVERS = "83a2b18a4";
	public static final String CONPLAN_6 = "18a1b1547802152a1b40a1b31a1b52a1b99a1";

	public static final String STC = "98a1b12a1b93a1b44a3";
	public static final String CAMERASHY = "4a3b1540004252a1b1580766301a1b5639273a1";

	public static final String CONSIGLIERE_8 = "28a1b61a1b7a1b75a1b31a1b45a2b57a1";
	public static final String CONSIGLIERE_10 = "28a1b61a1b7a1b1539915457a1b75a1b31a1b45a3b1596390851a1";
	public static final String DEF_MAF_12 = "28a1b19a1b8a1b1555727200a1b7a1b12a1b65a1b69a1b45a2b1572486115a1b1556390101a1";
	public static final String DEF_MAF_18 = "28a1b19a1b1555727200a1b7a1b12a1b22a1b324a1b69a1b24a1b1553337595a1b1556837816a1b1572486115a2b45a2b59a1b98a1b99a1";
	public static final String JANI = "7a1b12a1b73a1b72a1b75a1b32a1b36a1b57a3";
	public static final String GNH = "19a1b7a1b69a1b57a3b75a1";
	public static final String PIE7 = "7a1b75a1b57a3b12a1b69a1";
	public static final String PIE8 = "7a1b12a1b69a1b75a1b57a4";
	public static final String BOOTCAMP7 = "7a1b75a1b57a3b65a1b1565630670a1";
	public static final String BOOTCAMP8 = "7a1b75a1b57a4b65a1b1565630670a1";

	public static final String V_LTR_N = "1567786474a1b5639273a1b36a1b1541806737a1b57a5";
	public static final String GNIGHTLESS = "1596417444a7b86a2";
	public static final String SWEETDREAMS = "75a1b57a2b28a1b13a1b45a2b87a1";

	public static final String DNCT = "59a1b1553863028a1b45a3b1568391955a2b28a1b1596418309a1";
	public static final String TRIPLECAMP = "75a1b1565630670a1b324a1b65a1b1565223000a1b1565316812a1b45a5";
	public static final String HOW2MM = "65a1b75a1b86a1b7a1b32a1b57a4b5a1b31a1";
	public static final String ABC = "65a1b7a1b9a1b1540004252a1b83a1b1583467528a1b57a4";

	public static final String ASCENSION = "83a3b4a7"; // https://mafiagg.fandom.com/wiki/Ascension

	public static final String CONGRESS = "5639273a1b17a1b1540783473a1b24a1b72a1b25a7b84a1b201a1b106a3";
	public static final String ONESHOTCOPS = "1555727200a1b59a1b1577500425a2b1580766301a1b89a1b57a4b66a1";
	public static final String SWORDSHIELD = "66a1b1572486115a5b79a1b1588642540a5"; // not implemented
	public static final String MULTEMPLAR = "29a2b62a3b42a5b1573416236a2";
	public static final String UNCERTAINTY = "4a1b61a2b7a1b12a1b66a1b32a3b57a3";
	public static final String POWERVILLY = "28a1b7a1b75a2b1572486115a1b31a1b32a1b1552856661a1b45a3b1555727200a1b59a1";
	public static final String WHITEFLAG = "4a10b83a3";
	public static final String ALLSTARS13 = "75a3b31a1b38a1b34a1b57a7";
	public static final String CIRCUS = "57a5b36a1b5a1b1554252534a3b17a1b66a1b26a1b10a1b54a1";
	public static final String SOLOBOMBS = "4a1b59a1b73a1b31a1b1572568017a1b77a2b38a1b47a6b1558240320a1";
	public static final String ALLSTARS15 = "38a1b75a3b57a9b31a1b34a1";
	public static final String FATEDDUO = "5a1b54a1b57a2b1567786474a2b1540004252a1b31a1b84a1b45a4b1558930946a2b52a1";
	public static final String IBERN = "7a3b1547802152a1b65a1b1572486115a1b35a1b1552856661a1b38a1b1573568724a1b45a2b47a1b49a1b87a1b4a1";
	public static final String LIZARDRROFF = "75a2b38a1b34a1b57a7b1550785710a1b80a1b65a1b17a1b35a1";
	public static final String MASONRELAY = "69a1b62a1b57a6b14a1b29a2b35a1b1558930946a2b1572486115a1b5a1";
	public static final String POWERMILLERS = "31a1b1540004252a1b20a1b72a1b83a1b32a6b1565630670a1b45a1b1558240320a1b87a1b1577500425a1";
	public static final String SHOBOMBS = "7a1b75a1b31a1b32a1b4a1b65a1b77a1b42a1b87a1b57a6b47a1";
	public static final String VIP = "1567786474a1b7a1b1559319559a1b69a1b86a1b31a1b1581354568a1b1541480746a1b1580879842a1b57a6b1572486115a1";
	public static final String CHESS = "40a1b323a1b106a7b25a7"; // not implemented
	public static final String IBERNFREZE = "4a1b80a1b65a1b17a1b1572486115a1b35a1b1552856661a1b38a1b1573568724a1b45a2b47a1b87a1b57a5b32a1b1540004252a1"; // not
																																							// implemented
	public static final String BASIC20 = "28a1b5a1b1540004252a1b7a1b65a1b1538276587a1b69a1b75a1b1572486115a1b31a1b32a1b1552856661a1b45a2b1597815390a1b57a5";
	public static final String PIE25 = "57a10b80a1b75a2b36a1b14a1b69a1b9a1b1540004252a1b5a1b17a1b7a1b1540344996a1b86a1b49a1b65a1";

	public static final List<String> SUPERPOS = new ArrayList<String>() {
		{
			add("1599661616a1b37a1b1600386374a1b1556390101a1");
			add("1599661616a1b37a1b1600386374a1b1547802152a1");
			add("1599661616a1b37a1b1600386374a1b1573568724a1");
			add("1599661616a1b1600386374a1b33a1b1580928590a1");
			add("1596417444a1b37a1b1599661616a1b1547802152a1");
			add("1596417444a1b1538276587a1b1596418665a1b37a1");
			add("1596417444a1b1596418665a1b37a1b75a1");
			add("1596417444a1b23a1b69a1b1599661616a1");
		}
	};

	public static final List<String> CARBON14 = new ArrayList<String>() {
		{
			add("7a1b75a2b1565630670a1b57a3");
			add("7a1b1565630670a1b57a3b65a2");
		}
	};

	public static final List<String> MATRIX6 = new ArrayList<String>() {
		{
			add("75a1b57a4b66a1b1577500425a1");
			add("57a3b75a1b7a1b12a1b69a1");
			add("57a3b1603688758a1b75a1b1567786474a1b1540004252a1");
			add("1567786474a1b1577500425a1b75a1b57a3b69a1");
			add("57a4b75a1b7a1b1540004252a1");
			add("75a1b57a3b66a1b12a1b1603688758a1");
		}
	};

	public static final List<String> DOUBLENEG = new ArrayList<String>() {
		{
			add("1596337363a2b47a1b1588642540a2b57a1b7a1");
			add("1596337363a2b47a1b1588642540a2b57a1b23a1");
		}
	};

	public static final List<String> NEWD3 = new ArrayList<String>() {
		{
			add("57a5b69a1b1540004252a1b7a1b12a1");
			add("69a1b57a5b1540004252a1b35a1b52a1");
			add("57a5b69a1b1573416236a2b1540004252a1");
			add("57a5b52a1b1540004252a1b324a1b87a1");
			add("57a5b1540004252a1b324a1b87a1b35a1");
			add("57a5b1540004252a1b87a1b52a1b12a1");
			add("57a6b75a2b7a1");
			add("57a6b75a2b35a1");
			add("57a5b75a2b1573416236a2");
		}
	};

	public static final List<String> MAFIAJJANI = new ArrayList<String>() {
		{
			add("323a1b1602978335a1b12a1b73a1b36a1b57a5b1596417444a1");
			add("323a1b1602978335a1b73a1b36a1b57a5b1596417444a1b7a1");
			add("323a1b1602978335a1b73a1b57a5b1596417444a1b7a1b12a1");
			add("323a1b1602978335a1b73a1b57a4b1596417444a1b7a1b12a1b36a1");
			add("323a1b1602978335a1b73a1b57a4b1596417444a1b7a1b12a1b36a1");
		}
	};

	public static final List<String> SODIUM24 = new ArrayList<String>() {
		{
			add("5a1b7a1b75a3b1596417444a1b1565630670a1b57a5");
			add("5a1b7a1b75a2b1596417444a1b1565630670a1b57a5b65a1");
			add("5a1b7a1b75a1b1596417444a1b1565630670a1b57a5b65a2");
			add("5a1b7a1b1596417444a1b1565630670a1b57a5b65a3");
		}
	};

	public static final List<String> ALLSTARSSEMI = new ArrayList<String>() {
		{
			add("1555727200a5b31a1b34a1b1580879842a1b1595571568a1b1596420536a2b1596416287a1b54a1b1588642540a1b67a1b7a1");
			add("1555727200a5b31a1b34a1b1580879842a1b1595571568a1b1596420536a2b23a1b1596416287a1b54a1b1588642540a1b67a1");
			add("1555727200a6b31a1b34a1b1580879842a1b1595571568a1b1596416287a1b1588642540a1b67a1b5a1b7a1b1596420536a1");
			add("1555727200a6b31a1b34a1b1580879842a1b1595571568a1b1596416287a1b1588642540a1b67a1b5a1b1596420536a1b23a1");
			add("1555727200a5b31a1b34a1b1580879842a1b1595571568a1b1596416287a1b1588642540a1b7a1b1596420536a2b54a1b1567787886a1");
			add("1555727200a5b31a1b34a1b1580879842a1b1595571568a1b1596416287a1b1588642540a1b1596420536a2b54a1b1567787886a1b23a1");
			add("1555727200a6b31a1b34a1b1580879842a1b1595571568a1b1596416287a1b1588642540a1b7a1b1596420536a1b1567787886a1b5a1");
			add("1555727200a6b31a1b34a1b1580879842a1b1595571568a1b1596416287a1b1588642540a1b1596420536a1b1567787886a1b5a1b23a1");
			add("1555727200a6b31a1b34a1b1580879842a1b1595571568a1b1596420536a1b7a1b54a1b67a1b1572486115a1b79a1");
			add("1555727200a6b31a1b34a1b1580879842a1b1595571568a1b1596420536a1b54a1b67a1b1572486115a1b79a1b23a1");
			add("1555727200a7b31a1b34a1b1580879842a1b1595571568a1b7a1b67a1b1572486115a1b79a1b5a1");
			add("1555727200a7b31a1b34a1b1580879842a1b1595571568a1b67a1b1572486115a1b79a1b5a1b23a1");
			add("1555727200a6b31a1b34a1b1580879842a1b1595571568a1b7a1b1572486115a1b79a1b1596420536a1b54a1b1567787886a1");
			add("1555727200a6b31a1b34a1b1580879842a1b1595571568a1b1572486115a1b79a1b1596420536a1b54a1b1567787886a1b23a1");
			add("1555727200a7b31a1b34a1b1580879842a1b1595571568a1b7a1b1572486115a1b79a1b5a1b1567787886a1");
			add("1555727200a7b31a1b34a1b1580879842a1b1595571568a1b1572486115a1b79a1b5a1b1567787886a1b23a1");
		}
	};
	
	public static final List<String> REVERSEROULETTE7 = new ArrayList<String>() {
		{
			add("106a4b57a1b1596416878a1b56a1");
			add("57a4b1600386374a1b106a1b98a1");
		}
	};
	
	public static final List<String> REVERSEROULETTE11 = new ArrayList<String>() {
		{
			add("106a7b57a2b1596416878a1b56a1");
			add("106a2b57a7b98a1b1600386374a1");
		}
	};
	
	public static final List<String> REVERSEROULETTE15 = new ArrayList<String>() {
		{
			add("106a10b57a3b1596416878a1b56a1");
			add("106a3b57a10b98a1b1600386374a1");
		}
	}; */

	private MafiaSession() {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		List<String> creds = new ArrayList<String>();
		try {
			// file containing username and password of bot account
			// first line username, second line password
			File myObj = new File("credentials.txt");
			Scanner myReader = new Scanner(myObj);
			for (int i = 0; i < 2; i++) {
				String data = myReader.nextLine();
				// System.out.println( data );
				creds.add(data); // read username and password from file
			}
			// System.out.println( creds );
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		this.username = creds.get(0);
		this.password = creds.get(1);
		// this.autoChangeRoomName = false;
		this.unlistedMemory = false;
		// this.setInUse = "";
		// this.chat = new ArrayList<WebElement>();
		// this.usn = new ArrayList<WebElement>();
		
		this.setups = readSetups();
		
		session = new ChromeDriver();
		session.manage().window().maximize();
		session.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		session.get(URL);
		session.findElement(By.xpath("//a[text()=\"Log in\"]")).click();
		session.findElement(By.xpath("//input[@type='text']")).sendKeys(this.username);
		session.findElement(By.xpath("//input[@type='password']")).sendKeys(this.password);
		session.findElement(By.xpath("//button[@type='submit']")).click();
		act = new Actions(session);
		session.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}

	private List<Setup> readSetups() {
		List<Setup> setups = new ArrayList<Setup>();
		try {
			// open .md file containing setups
			File myObj = new File("src\\mafia\\gg\\bot\\SETUPS.md");
			Scanner myReader = new Scanner(myObj);
			String data = "";
			for (int i = 0; i < 5; i++) {
				data = myReader.nextLine();
			}
			// read open setups
			//String[] listdata;
			while ( data != "" ) {
				Setup curSetup = createSetup( data );
				//System.out.println( data );
				//listdata = data.split( "\\|" );
				//System.out.println( listdata.length );
				//int codeIndex = 11;
				//for ( int j = 0; j < listdata.length - 1; j++ ) {
					
				//	listdata[j] = listdata[j+1].strip();
				//	System.out.println( listdata[j] );
					
				//}
				
				//String[] curCodes;
				//curCodes = listdata[codeIndex].split(",");
				//System.out.println( listdata[3] );
				
				//Setup curSetup = new Setup( listdata[0], listdata[1], listdata[2], Integer.parseInt( listdata[3] ), listdata[4], listdata[5], listdata[6], listdata[7], listdata[8], listdata[9], listdata[10], curCodes, listdata[12] );
				setups.add( curSetup ); 
				//System.out.println( data );
				data = myReader.nextLine();
			}
			
			// skip space between tables
			for (int i = 0; i < 5; i++) {
				data = myReader.nextLine();
			}
			
			// read and create semi-open setups
			while ( true ) {
				Setup curSetup = createSetup( data );
				setups.add( curSetup );
				try {
					data = myReader.nextLine();
				} catch ( Exception e ){
					e.printStackTrace();
					break;
				}
			}
			
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Setup file not found. Check path.");
			e.printStackTrace();
		}
		return setups;
	}

	private Setup createSetup(String data) {
		//System.out.println( data );
		String[] listdata = data.split( "\\|" );
		//System.out.println( listdata.length );
		int codeIndex = 11;
		for ( int j = 0; j < listdata.length - 1; j++ ) {
			
			listdata[j] = listdata[j+1].strip();
			//System.out.println( listdata[j] );
			
		}
		
		String[] curCodes;
		curCodes = listdata[codeIndex].split(",");
		//System.out.println( listdata[3] );
		
		Setup setup = new Setup( listdata[0], listdata[1], listdata[2], Integer.parseInt( listdata[3] ), listdata[4], listdata[5], listdata[6], listdata[7], listdata[8], listdata[9], listdata[10], curCodes, listdata[12] );
		return setup;
	}

	public static MafiaSession newSession() {
		if (UniqueObject == null)
			UniqueObject = new MafiaSession();
		return UniqueObject;
	}

	public void hostUnlisted() {
		try {
			Thread.sleep(500);
			// System.out.println( "hi");
			if (!this.unlistedMemory) {
				session.findElement(By.xpath("//div[@class='checkbox-label']")).click();
				// session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
				session.findElement(By.xpath("//button[@type='submit']")).click();
			} else {
				// session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
				session.findElement(By.xpath("//button[@type='submit']")).click();
			}
			this.unlistedMemory = true;
		} catch (NoSuchElementException | InterruptedException e) {
			e.printStackTrace();

		}
	}

	/**
	 * public void hostUnlisted(String roomName) { if (!this.unlistedMemory) {
	 * session.findElement(By.xpath("//div[@class='checkbox']")).click();
	 * session.findElement(By.xpath("//div[@class='flex-1']/input"))
	 * .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
	 * session.findElement(By.xpath("//div[@class='flex-1']/input")).sendKeys(roomName);
	 * session.findElement(By.xpath("//span[text()=\"Create\"]")).click(); } else {
	 * session.findElement(By.xpath("//div[@class='flex-1']/input"))
	 * .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
	 * session.findElement(By.xpath("//div[@class='flex-1']/input")).sendKeys(roomName);
	 * session.findElement(By.xpath("//span[text()=\"Create\"]")).click(); }
	 * this.unlistedMemory = true; }
	 * 
	 * public void host() { try { if (!this.unlistedMemory) {
	 * session.findElement(By.xpath("//span[text()=\"Create\"]")).click(); } else {
	 * //System.out.println( "hello");
	 * session.findElement(By.xpath("//div[@class='checkbox']")).click();
	 * session.findElement(By.xpath("//span[text()=\"Create\"]")).click(); }
	 * this.unlistedMemory = false; } catch (Exception e) { e.printStackTrace(); } }
	 * 
	 * public void host(String roomName) { if (!this.unlistedMemory) {
	 * session.findElement(By.xpath("//div[@class='flex-1']/input"))
	 * .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
	 * session.findElement(By.xpath("//div[@class='flex-1']/input")).sendKeys(roomName);
	 * session.findElement(By.xpath("//span[text()=\"Create\"]")).click(); } else {
	 * session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
	 * session.findElement(By.xpath("//div[@class='flex-1']/input"))
	 * .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
	 * session.findElement(By.xpath("//div[@class='flex-1']/input")).sendKeys(roomName);
	 * session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
	 * 
	 * } this.unlistedMemory = false; }
	 **/

	public boolean tryHostNew() {
		try {
			session.findElement(By.xpath("//span[text()=\"Host new room\"]")).click();
			Thread.sleep(5000);
			session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
			Thread.sleep(5000);
			System.out.println("Hosting new room");
			return true;
		} catch (NoSuchElementException e) {
			// System.out.println( "Cannot host new until game ends. Caught exception in
			// tryHostNew().");
			// e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
			return false;
		}

	}

	public void save() {
		try {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act.moveToElement(session.findElement(By.xpath("//span[text()='Save']"))).click().perform();
		} catch (StaleElementReferenceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * public void defaultSettings() {
	 * 
	 * try { session.findElement(By.xpath("//span[text()='Edit options']")).click();
	 * session.findElement(By.xpath("//button[text()='Game']")).click();
	 * session.manage().window().maximize(); setDayTime(5); setNightTime(1);
	 * WebElement el = session.findElement(By.xpath(
	 * "/html/body/div[2]/div/div/div/div/div[2]/div/div[3]/div/div[3]/div/div[1]/div/div/div/div[3]/div/input"));
	 * if (!el.isSelected()) { el.click();// scale timer } el =
	 * session.findElement(By.xpath(
	 * "/html/body/div[2]/div/div/div/div/div[2]/div/div[3]/div/div[3]/div/div[2]/div/div/div/div[3]/div/input"));
	 * if (!el.isSelected()) { el.click();// idle players dont vote } // checkbox
	 * dont know wht to do setMajority(this.TWO_THIRDS_MAJORITY); //
	 * session.findElement(By.xpath("//span[text()='Save']")).click(); save(); }
	 * catch (Exception e) { e.printStackTrace(); } }
	 **/

	// only used by untested functions setDayTime and setNightTime
	public void editOptions() {
		try {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act.moveToElement(session.findElement(By.xpath("//span[text()='Edit options']"))).click().perform();
		} catch (NoSuchElementException e) {
			e.printStackTrace();

		}
	}

	// only used by untested functions setDayTime and setNightTime
	public void gameSettings() {
		try {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			session.findElement(By.xpath("//button[text()='Game']")).click();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}

	}

	public void setDayTime(int t) {
		try {
			WebElement element = session.findElement(By.xpath("//div[@class='row'][1]/button/div[2]/button"));
			int a = element.getLocation().getX() - 516;
			t = t - 3;
			int cont = Math.round((t * 550) / 17);
			cont = cont - a;
			act.dragAndDropBy(element, cont, 0).build().perform();
			session.findElement(By.xpath("//span[text()='Save']")).click();
			if (element.getLocation().getX() - 516 != Math.round((t * 550) / 17))
				setDayTime(t + 3);
		} catch (NoSuchElementException e) {
		}

	}

	/**
	 * public void setMajority(int code) { try { Thread.sleep(100);
	 * session.findElement(By.xpath("//option[@value=[code]]".replace("[code]", "" +
	 * code))).click(); } catch (NoSuchElementException | InterruptedException e) {
	 * 
	 * }
	 * 
	 * }
	 **/

	public void setNightTime(int t) {
		try {
			WebElement element = session.findElement(By.xpath("//div[@class='row'][2]/button/div[2]/button"));
			int a = element.getLocation().getX() - 516;
			t = t - 1;
			int cont = Math.round((t * 550) / 8);
			cont = cont - a;
			act.dragAndDropBy(element, cont, 0).build().perform();
			session.findElement(By.xpath("//span[text()='Save']")).click();
			if (element.getLocation().getX() - 516 != Math.round((t * 550) / 8))
				setDayTime(t + 1);
		} catch (NoSuchElementException e) {

		}
	}

	public void setSetup(String setupCode) {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);

			session.findElement(By.xpath("//input[@placeholder='Paste setup code…']"))
					.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));

			// System.out.println( "Deleted");
			session.findElement(By.xpath("//input[@placeholder='Paste setup code…']")).sendKeys(setupCode);
			// Thread.sleep(100);
			session.findElement(By.xpath("//span[text()='Save']")).click();
			// Thread.sleep(100);
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			System.out.println("Yo"); // Paste setup code.e..
			// setSetup(setupCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * public void setSetupOld(String setupCode) { try { try {
	 * 
	 * try { Thread.sleep(100); } catch (InterruptedException e) { }
	 * act.moveToElement(session.findElement(By.xpath("//span[text()='Edit
	 * options']"))).click().perform(); } catch (NoSuchElementException e) { }
	 * finally { session.findElement(By.xpath("//input[@placeholder='Paste setup
	 * codeâ€¦']")) .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
	 * session.findElement(By.xpath("//input[@placeholder='Paste setup
	 * codeâ€¦']")).sendKeys(setupCode); } try { save(); } catch
	 * (NoSuchElementException e) { save(); } catch
	 * (ElementClickInterceptedException e) { save(); } //
	 * session.findElement(By.xpath("//span[text()='Save']")).click(); // if
	 * (setupCode.equals(this.CONPLAN_5) && this.autoChangeRoomName) //
	 * this.changeRoomName("5 - Man ConPlan"); // if
	 * (setupCode.equals(this.CONSIGLIERE_10) && this.autoChangeRoomName) //
	 * this.changeRoomName("10 - Man Consigliere"); // if
	 * (setupCode.equals(this.CONSIGLIERE_8) && this.autoChangeRoomName) //
	 * this.changeRoomName("8 - Man Consigliere"); } catch (NoSuchElementException
	 * e) { e.printStackTrace(); //setSetup(setupCode); } }
	 **/

	public boolean setDeck(String deck) {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Deck']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input[@placeholder='Filter decks by name…']")).sendKeys(deck);
			// session.findElement(By.xpath("//div[text()[contains(.,'Use no deck')] and
			// @class='checkbox-label']")).click();
			Thread.sleep(500);
			session.findElement(
					By.xpath("//div[text()[contains(.,'" + deck + "')] and @class='game-options-editor-deck-title']"))
					.click();
			Thread.sleep(100);

			save();
			return true;
		} catch (Exception e) {
			// session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
			// Thread.sleep(100);
			e.printStackTrace();
			save();
			return false;
		}
	}

	/**
	 * public void setDeckOld (String name) {// not working yet
	 * session.findElement(By.xpath("//span[text()='Edit options']")).click();
	 * session.findElement(By.xpath("//button[text()='Deck']")).click(); if
	 * (name.equalsIgnoreCase("no deck")) { session.findElement(
	 * By.xpath("/html/body/div[2]/div/div/div/div/div[2]/div/div[2]/div[1]/div[3]/div[1]/input"));
	 * } else { session.findElement(By.xpath("//input[@placeholder='Filter decks by
	 * nameï¿½']")).sendKeys(name); try { Thread.sleep(1000); } catch
	 * (InterruptedException e) { e.printStackTrace(); }
	 * session.findElement(By.xpath(
	 * "/html/body/div[2]/div/div/div/div/div[2]/div/div[2]/div[1]/div[2]/div/div[3]/div[1]/div/input"))
	 * .click(); } session.findElement(By.xpath("//span[text()='Save']")).click();
	 * 
	 * }
	 **/

	// public void enableAutoChangeRoomName() {
	// this.autoChangeRoomName = true;
	// }

	// public void disableAutoChangeRoomName() {
	// this.autoChangeRoomName = false;
	// }

	public void changeRoomName(String roomName) {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Room']")).click();
			Thread.sleep(100);
			session.findElement(
					By.xpath("//input[contains( @id, 'id')]")).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
			Thread.sleep(100);
			session.findElement(
					By.xpath("//input[contains( @id, 'id')]")).sendKeys(roomName);
					//By.xpath("/html/body/div[2]/div/div/div/div/div[2]/div/div[2]/div/div[3]/div/div[1]/input"))
					//.sendKeys(roomName);
			Thread.sleep(100);
			session.findElement(By.xpath("//span[text()='Save']")).click();
			Thread.sleep(100);
			;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void listToggle() {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Room']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//div[@class='checkbox']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	public void hideSetupToggle() {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//div[text()[contains(.,'Hide setup')] and @class='checkbox-label']"))
					.click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/

	/*public void rroffToggle() {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			session.findElement(
					By.xpath("//div[text()[contains(.,'Hide roles upon death')] and @class='checkbox-label']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	public void setRR( String rr ) {
		assert rr.equals( "off" ) || rr.equals( "on" ) || rr.equals( "align" );
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//label[text()[contains(.,'Reveal roles upon death')]]")).click();
			Thread.sleep(100);
			if ( rr.equals( "off" ) ) {
				session.findElement(By.xpath("//option[@value='noReveal']")).click();
			}
			else if ( rr.equals( "on" ) ) {
				session.findElement(By.xpath("//option[@value='allReveal']")).click();
			}
			else if ( rr.equals( "align" ) ) {
				session.findElement(By.xpath("//option[@value='alignmentReveal']")).click();
			}			
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void setStart( String start ) {
		assert start.equals( "night" ) || start.equals( "informed day" ) || start.equals( "uninformed day" );
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//label[text()[contains(.,'Start with day')]]")).click();
			Thread.sleep(100);
			if ( start.equals( "night" ) ) {
				session.findElement(By.xpath("//option[@value='off']")).click();
			}
			else if ( start.equals( "informed day" ) ) {
				session.findElement(By.xpath("//option[@value='dawnStart']")).click();
			}
			else if ( start.equals( "uninformed day" ) ) {
				session.findElement(By.xpath("//option[@value='dayStart']")).click();
			}			
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
/*
	public void nightStart() {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//label[text()[contains(.,'Start with day')]]")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//option[@value='off']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			//session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dayStart() {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//label[text()[contains(.,'Start with day')]]")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//option[@value='dawnStart']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} */
	
	public void setMajority( String maj ) {
		assert maj.equals( "off") || maj.equals( "1/2") || maj.equals( "2/3") || maj.equals( "3/4");
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//label[text()[contains(.,'Finalize vote on majority')]]")).click();
			Thread.sleep(100);
			if (maj.equals( "off") ) {
				session.findElement(By.xpath("//option[@value='-1']")).click();
			}
			if (maj.equals( "1/2") ) {
				session.findElement(By.xpath("//option[@value='51']")).click();
			}
			if (maj.equals( "2/3") ) {
				session.findElement(By.xpath("//option[@value='66']")).click();
			}
			if (maj.equals( "3/4") ) {
				session.findElement(By.xpath("//option[@value='75']")).click();
			}
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void setDeadlock( String deadlock ) {
		assert deadlock.equals( "rand") || deadlock.equals( "init") || deadlock.equals( "resp") || deadlock.equals( "disable");
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//label[text()[contains(.,'Deadlock prevention')]]")).click();
			Thread.sleep(100);
			if (deadlock.equals( "rand") ) {
				session.findElement(By.xpath("//option[text()[contains(.,'Punish')] and @value='-1']")).click();
			}
			if (deadlock.equals( "init") ) {
				session.findElement(By.xpath("//option[text()[contains(.,'Punish')] and @value='5']")).click();
			}
			if (deadlock.equals( "resp") ) {
				session.findElement(By.xpath("//option[text()[contains(.,'Punish')] and @value='6']")).click();
			}
			if (deadlock.equals( "disable") ) {
				session.findElement(By.xpath("//option[text()[contains(.,'Disable')] and @value='-2']")).click();
			}
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void setForceVote( String force ) {
		assert force.equals( "off" ) || force.equals( "on" );
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			List<WebElement> checkboxes = session.findElements(By.xpath("//input[@type='checkbox']" ));
			List<WebElement> checkboxesClickable = session.findElements(By.xpath("//div[@class='checkbox-label']" ));
			WebElement forceVoteBox = checkboxes.get( 2 );
			WebElement forceVoteBoxClickable = checkboxesClickable.get( 2 );
			System.out.println( forceVoteBoxClickable.getAttribute( "innerHTML") );
			
			System.out.println("attribute check:" + forceVoteBox.getAttribute( "checked" ) );
			System.out.println("isSelected:" + forceVoteBox.isSelected() );
			//for ( int i = 0; i < checkboxes.size(); i++) {	
		//		System.out.println( checkboxes.get( i ).getAttribute( "innerHTML") );
			//	System.out.println("The checkbox is selection state is - " + checkboxes.get( i ).isSelected() );
			//	if ( checkboxes.get( i ).getAttribute( "innerHTML").contains( "Force vote" ) ){
					//System.out.println( checkboxes.get( i ).getAttribute( "innerHTML") );///)[contains(.,'Force vote')] and @class='checkbox-label']"));
			//		forceVoteBox = checkboxes.get( i );
					//System.out.println("The checkbox is selection state is - " + forceVoteBox.getAttribute( "checked" ) );
			//		System.out.println("The checkbox is selection state is - " + forceVoteBox.isSelected() );
		//		}
		//	}
			
			if ( force.equals( "off" ) && forceVoteBox.isSelected() ) {
				forceVoteBoxClickable.click();
			}
			else if ( force.equals( "on" ) && !forceVoteBox.isSelected() ) {
				forceVoteBoxClickable.click();
			}		
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void setHidden( String hiddenSetup ) {
		assert hiddenSetup.equals( "off" ) || hiddenSetup.equals( "on" );
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			List<WebElement> checkboxes = session.findElements(By.xpath("//input[@type='checkbox']" ));
			List<WebElement> checkboxesClickable = session.findElements(By.xpath("//div[@class='checkbox-label']" ));
			WebElement hideSetupBox = checkboxes.get( 4 );
			WebElement hideSetupBoxClickable = checkboxesClickable.get( 4 );
			//System.out.println( hideSetupBoxClickable.getAttribute( "innerHTML") );
			
			
			if ( hiddenSetup.equals( "off" ) && hideSetupBox.isSelected() ) {
				hideSetupBoxClickable.click();
			}
			else if ( hiddenSetup.equals( "on" ) && !hideSetupBox.isSelected() ) {
				hideSetupBoxClickable.click();
			}		
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}


	/* public void majOff() {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//label[text()[contains(.,'Finalize vote on majority')]]")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//option[@value='-1']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void majOn() {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//label[text()[contains(.,'Finalize vote on majority')]]")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//option[@value='51']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} */
	
	public void setkp( String kp ) {
		assert kp.equals( "off") || kp.equals( "always") || kp.equals( "2kp1") || kp.equals( "2kp2") || kp.equals( "2kp3") || kp.equals( "2kp4") || kp.equals( "2kp5") || kp.equals( "2kp5");
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//label[text()[contains(.,'Extra mafia kill power')]]")).click();
			Thread.sleep(100);
			if ( kp.equals( "off") ) {
				session.findElement(By.xpath("//option[@value='0']")).click();
			}
			if ( kp.equals( "always") ) {
				session.findElement(By.xpath("//option[@value='1']")).click();
			}
			if ( kp.equals( "2kp1") ) {
				session.findElement(By.xpath("//option[@value='2']")).click();
			}
			if ( kp.equals( "2kp2") ) {
				session.findElement(By.xpath("//option[@value='3']")).click();
			}
			if ( kp.equals( "2kp3") ) {
				session.findElement(By.xpath("//option[@value='4']")).click();
			}
			if ( kp.equals( "2kp4") ) {
				session.findElement(By.xpath("//option[@value='5']")).click();
			}
			if ( kp.equals( "2kp5") ) {
				session.findElement(By.xpath("//option[@value='6']")).click();
			}
			if ( kp.equals( "2kp6") ) {
				session.findElement(By.xpath("//option[@value='7']")).click();
			}			
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public void twokptwo() {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//label[text()[contains(.,'Extra mafia kill power')]]")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//option[@value='3']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void noextrakp() {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Game']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//label[text()[contains(.,'Extra mafia kill power')]]")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//option[@value='0']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public void randomDeck() {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Deck']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[@class='icon-shuffle']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void noDeck() {
		try {
			session.findElement(By.xpath("//span[text()='Edit options']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Deck']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//div[text()[contains(.,'Use no deck')] and @class='checkbox-label']"))
					.click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * //public void home() { //
	 * session.findElement(By.xpath("/html/body/div[1]/header/div/h1/a")); //}
	 * 
	 * public void autoPlayerUp(boolean choice) { if (choice == true) { while (true)
	 * { try { session.findElement(By.xpath("//span[text()='Become a
	 * player']")).click(); int p = 0; while (true) { try { autoJoinNewRoom();
	 * session.findElement(By.xpath("//span[text()='Become a player']")); p++; if (p
	 * >= 1000) break; } catch (NoSuchElementException t) { break; } } } catch
	 * (NoSuchElementException e) { continue; } catch (WebDriverException e) {
	 * session.quit(); break; } autoJoinNewRoom(); } } else return; }
	 * 
	 * public void autoJoinNewRoom() { try {
	 * session.findElement(By.xpath("//a[text()='Join the new room']")).click(); }
	 * catch (NoSuchElementException e) {
	 * 
	 * } }
	 **/

	public boolean soundOff() {
		try {
			Thread.sleep(100);
			// session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[1]/div[1]/button")).click();
			session.findElement(By.xpath("//div[@class='icon-settings']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//div[@class='checkbox-label']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			// session.findElement(By.xpath("//div[@class='checkbox-label']")).click();
			// Thread.sleep(100);
			// session.findElement(By.xpath("//div[@class='checkbox-label']")).click();
			// session.findElement(By.xpath("//div[text()='Play sounds')")).click();
			// Thread.sleep(100);
			// session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/button")).click();
			// session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/button")).click();
			// session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/button")).click();
			// session.findElement(By.xpath("//div[@class='icon-settings']")).click();
			// sendMessage( "" ); // reset
			// session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[1]/div[1]/button")).click();
			// Thread.sleep(100);
			return true;
			// session.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/section[1]/div[1]/input/label")).click();
			// Thread.sleep(100);
			// System.out.println( "what the hell");
			// session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[1]/div[1]/button")).click();
			// System.out.println( "what the hell");
			// return true;
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return false;
		} catch (ElementClickInterceptedException e) {
			e.printStackTrace();
			//act.moveToElement(session.findElement(By.xpath("//div[@class='game-checkbox-dropdown']/section[1]/div[1]")))
			//		.click().perform();
			//act.moveToElement(session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[1]/div[1]/button")))
			//		.click().perform();
			// System.out.println( "hmm");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// public List<String> getChat() {
	// try {
	// WebElement e =
	// session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]"));

	// usn = e.findElements(
	// By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[1]"));
	// chat = e.findElements(
	// By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[2]"));
	// System.out.println( "what about this");
	// List<String> arr = new ArrayList<String>();
	// for (int i = 0; i < chat.size(); i++) {
	// arr.add(chat.get(i).getText());
	// System.out.println( usn.get(i).getText() );
	// }
	// return arr;
	// } catch (NoSuchElementException e) {
	// return getChat();
	// }
	// catch (org.openqa.selenium.StaleElementReferenceException ex) {
	// System.out.println(ex.getMessage());
	// return getChat();
	// }

	// }

	// public List<String> getUsername() {
	// try {
	// Thread.sleep( 100 );
	// WebElement e =
	// session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]"));
	// usn = e.findElements(
	// By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[1]"));
	// chat = e.findElements(
	// By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[2]"));
	/// List<String> arr = new ArrayList<String>();
	// for (int i = 0; i < usn.size(); i++)
	// arr.add(usn.get(i).getText());
	// return arr;
	// } catch (NoSuchElementException e) {
	// return getUsername();
	// } catch ( Exception e ) {
	// return getUsername();
	// }
	// }

	public void updateChatSmart(List<List<String>> queue, List<String> last) {
		try {
			List<WebElement> rawChat = session.findElements(By.xpath("//div[@class='game-chronicle-chat']"));
			List<WebElement> names = session.findElements(By.xpath("//div[@class='game-chronicle-name']"));
			// List<String> times = new ArrayList<String>();
			List<List<String>> curchat = new ArrayList<List<String>>();
			for (int i = 0; i < rawChat.size(); i++) {
				List<String> toSplit = Arrays.asList(rawChat.get(i).getText().split("\n"));
				// String time = toSplit.get( 0 );
				int namelength = names.get(i).getText().length();
				List<String> cmd = new ArrayList<String>();
				cmd.add(toSplit.get(0));
				cmd.add(toSplit.get(1).substring(0, namelength));
				cmd.add(toSplit.get(1).substring(namelength));
				curchat.add(cmd);
			}
			boolean queueing = false;
			if ("".equals(last.get(0)) && "".equals(last.get(1)) && "".equals(last.get(2))) {
				queueing = true;
			}
			for (int i = 0; i < curchat.size(); i++) {
				// if ( curchat.get( i ).get( 0 ).compareTo( last.get( 0 ) ) < 0 ){
				// continue;
				// }
				if (curchat.get(i).get(0).equals(last.get(0)) && curchat.get(i).get(1).equals(last.get(1))
						&& curchat.get(i).get(2).equals(last.get(2))) {
					queueing = true;
					continue;
				}
				if (queueing) {
					queue.add(curchat.get(i));
				}
			}
			// System.out.println( curchat );
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			// shouldn't get here.
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * public int updateChat(List<String> outDatedChat, List<String>
	 * outDatedUsernames) { try { List<WebElement> fullChat =
	 * session.findElements(By.xpath("//div[@class='game-chronicle-chat']"));
	 * List<WebElement> names =
	 * session.findElements(By.xpath("//div[@class='game-chronicle-name']"));
	 * List<String> times = new ArrayList<String>(); List<List<String>> chat = new
	 * ArrayList<List<String>>(); for (int i = 0; i < fullChat.size(); i++) {
	 * List<String> toSplit = Arrays.asList( fullChat.get( i ).getText().split(
	 * "\n") ); String time = toSplit.get( 0 ); int namelength = names.get( i
	 * ).getText().length(); List<String> cmd = new ArrayList<String>(); cmd.add(
	 * toSplit.get( 0 )); cmd.add( toSplit.get( 1 ).substring( 0, namelength ) );
	 * cmd.add( toSplit.get( 1 ).substring( namelength ) ); chat.add( cmd ); }
	 * System.out.println( chat ); System.out.print( "hi ");
	 * 
	 * 
	 * 
	 * 
	 * List<String> arr = outDatedChat; List<String> usr = outDatedUsernames;
	 * WebElement e =
	 * session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]"));
	 * List<WebElement> grp = e.findElements(
	 * By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div"
	 * )); //List<WebElement> lastgrp = //System.out.println( grp.size()); //for
	 * (int i = 0; i < grp.size(); i++) { //System.out.println( grp.get(i).size() );
	 * // System.out.println( grp.get(i).getText() ); //} //usn = e.findElements( //
	 * By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[1]"));
	 * //chat = e.findElements( //
	 * By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[2]"));
	 * String user = grp.get( grp.size() - 2).getText(); String msg = grp.get(
	 * grp.size() - 1 ).getText();
	 * 
	 * //int c = chat.size(), a = arr.size(); //if (chat.size() > arr.size() ||
	 * chat.size() == 0) { // int diff = c - a; // System.out.println(diff + "|" + c
	 * + "|" + a); // for (int i = chat.size() - diff; i < chat.size(); i++) { //
	 * arr.add(chat.get(i).getText()); // usr.add(usn.get(i).getText()); // }
	 * //System.out.println( getUsername() ); // return diff; //} //if (chat.size()
	 * < arr.size()) { //arr = getChat(); // return updateChat(outDatedChat,
	 * outDatedUsernames);
	 * 
	 * //return arr.size(); //} int size = grp.size()/2; int c = size, a =
	 * arr.size(); if (size > arr.size() || size == 0) { int diff = c - a; //
	 * System.out.println(diff + "|" + c + "|" + a); for (int i = size - diff; i <
	 * size; i++) { arr.add(msg); usr.add(user); } //System.out.println(
	 * getUsername() ); return diff; } if (size < arr.size()) { //arr = getChat();
	 * // should you ever get here? return 1; //return updateChat(outDatedChat,
	 * outDatedUsernames);
	 * 
	 * //return arr.size(); } // should you ever get here? return 1; //return
	 * updateChat(outDatedChat, outDatedUsernames); // return 0; } catch
	 * (NoSuchElementException e) { e.printStackTrace(); // shouldn't get here.
	 * return 0;//updateChat(outDatedChat, outDatedUsernames); } catch (Exception e)
	 * { e.printStackTrace(); return 0; } }
	 **/

	// public int updateUsername(List<String> outDatedUsername) {
	// try {
	// List<String> arr = outDatedUsername;
	// WebElement e =
	// session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]"));
	// usn = e.findElements(
	// By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[1]"));
	// chat = e.findElements(
	// By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[2]"));
	// int c = usn.size(), a = arr.size();
	// int diff = c - a;
	// if (chat.size() > arr.size()) {
	// for (int i = 0; i < diff; i++)
	// arr.add(chat.get(c + i).getText());
	// }
	// return diff;
	// } catch (NoSuchElementException e) {
	// return updateUsername(outDatedUsername);
	// }
	// }

	public void sendMessage(String str) {
		session.findElement(By.xpath("//input[@placeholder='Chat with the group…']")).sendKeys(str);
		//session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
		//session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(str);
		session.findElement(By.xpath("//input")).sendKeys(Keys.ENTER);
		//session.findElement(By.xpath("//button[text()='Send']")).click();
	}

	public void playerUp() {
		try {
			session.findElement(By.xpath("//span[text()='Become a player']")).click();
			// int p = 0;
			// while (true) {
			// try {
			// session.findElement(By.xpath("//span[text()='Become a player']"));
			// p++;
			// if (p >= 1000)
			// break;
			// } catch (NoSuchElementException t) {
			// break;
			// }
			// }
		} catch (NoSuchElementException e) {
			System.out.println("Tried to become player and couldn't.");
			e.printStackTrace();

		}
	}

	public void playerDown() {
		try {
			session.findElement(By.xpath("//span[text()='Become a spectator']")).click();
			// int p = 0;
			// while (true) {
			// try {
			// session.findElement(By.xpath("//span[text()='Become a spectator']"));
			// p++;
			// if (p >= 1000)
			// break;
			// } catch (NoSuchElementException t) {
			// break;
			// }

		} catch (NoSuchElementException e) {
			e.printStackTrace();

		}
	}

	public int playerdUp() {
		
		return Integer.parseInt( session.findElement(By.xpath("//div[@class='game-top-player-count']")).getAttribute("innerHTML").split( "</span><span>")[1] );
		
		//return Integer.parseInt(
		//		session.findElement(By.xpath("//div[@class='game-top-player-count']")).getText() );
	}

	public int totalPlayers() {
		
		//System.out.println( session.findElement(By.xpath("//div[@class='game-top-player-count']")).getAttribute("innerHTML").split( "</span><span>")[2].split("</span>")[0] );
		
		return Integer.parseInt( session.findElement(By.xpath("//div[@class='game-top-player-count']")).getAttribute("innerHTML").split( "</span><span>")[2].split("</span>")[0]  );
		
		//System.out.print( session.findElement(By.xpath("//div[@class='game-top-player-count']")).getText() );
		
		//return Integer.parseInt(
		//		session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[1]/div[2]/span[3]")).getText());
	}

	public void afkCheck() {
		session.findElement(
				By.xpath("//span[@class='button-contents' and text()='Force all to spectate']"))
				.click();
	}

	public void startGame() {
		session.findElement(By.xpath("//span[text()='Start game' and @class='button-contents']")).click();// [contains(.,'"+deck+"')]
		// session.findElement(By.xpath("//button[text()[contains(.,'Start
		// game')]]")).click(); // and @disabled = '' and @class='button'
	}

	public boolean kick(String player) {
		try {
			session.findElement(By.xpath("//div[text()='" + player + "' and @class='game-player-list-user-username']"))
					.click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Kick user' and @type='button']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//span[text()='Kick' and @class='button-contents']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			return true;
		} catch (Exception e) {
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			e.printStackTrace();
			return false;
		}
	}

	public void ban(String player) {
		try {

			// considering that there is only one tab opened in that point.
			String oldTab = session.getWindowHandle();
			session.findElement(By.xpath("//button[@class='common-header-user']")).click();
			Thread.sleep(100);
			Actions action = new Actions(session);
			action.keyDown(Keys.CONTROL).build().perform();
			session.findElement(By.xpath("//span[@class='icon-account']")).click();
			action.keyUp(Keys.CONTROL).build().perform();
			ArrayList<String> newTab = new ArrayList<String>(session.getWindowHandles());
			newTab.remove(oldTab);
			// change focus to new tab
			session.switchTo().window(newTab.get(0));

			// Do what you want here, you are in the new tab
			Thread.sleep(100);
			session.findElement(By.xpath("//textarea")).sendKeys("\n"+player); //@id='id-1611894914681-22'
			Thread.sleep(100);
			session.findElement(By.xpath("//span[text()='Save' and @class='button-contents']")).click();
			Thread.sleep(1000);
			session.close();
			// change focus back to old tab
			session.switchTo().window(oldTab);


			// session.findElement(By.xpath("//button[@class='common-header-user']")).click();
			// Thread.sleep(100);
			// Actions action=new Actions(session);
			// action.keyDown(Keys.CONTROL).build().perform();
			// session.findElement(By.xpath("//span[@class='icon-account']")).click();
			// driver.findElement(By.xpath(".//*[@id='selectable']/li[3]")).click();
			// action.keyUp(Keys.CONTROL).build().perform();
			// Robot robot = new Robot();
			// Press key Ctrl+Shift+i
			// robot.keyPress(KeyEvent.VK_ALT);
			// robot.keyPress(KeyEvent.VK_D);
			// robot.keyPress(KeyEvent.VK_ENTER);

			// Release key Ctrl+Shift+i
			// robot.keyRelease(KeyEvent.VK_ENTER);
			// robot.keyRelease(KeyEvent.VK_D);
			// robot.keyRelease(KeyEvent.VK_ALT);

			// WebElement l = session.findElement(By.id("gsc-i-id1"));
			// String selectLinkOpeninNewTab = Keys.chord(Keys.ALT,"d",Keys.ENTER);
			// session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(selectLinkOpeninNewTab);
			// l.sendKeys(selectLinkOpeninNewTab);
		} catch (Exception e) {
			session.close();
			//session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
			e.printStackTrace();
		}
	}
	
	public boolean giveHost(String player) {
		try {
			session.findElement(By.xpath("//div[text()='" + player + "' and @class='game-player-list-user-username']"))
					.click();
			Thread.sleep(100);
			session.findElement(By.xpath("//button[text()='Transfer host' and @type='button']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//span[text()='Transfer' and @class='button-contents']")).click();
			Thread.sleep(100);
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			Thread.sleep(100);
			return true;
		} catch (Exception e) {
			session.findElement(By.xpath("//input")).sendKeys(Keys.ESCAPE);
			e.printStackTrace();
			return false;
		}
	}

	public boolean pregamehosting() {
		try {
			session.findElement(By.xpath("//span[text()='Start game' and @class='button-contents']"));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public boolean gameRunning() {
		try {
			session.findElement(By.xpath("//span[text()='Become a player' and @class='button-contents']"));
			return false;
		} catch (NoSuchElementException e) {
			try {
				session.findElement(By.xpath("//span[text()='Become a spectator' and @class='button-contents']"));
				return false;
			} catch (NoSuchElementException f) {
				return true;
			}
		}

	}
	
	public void refresh() {
		try {
			session.navigate().refresh();
			Thread.sleep(100);
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void goHome() {
		try {
			session.get(URL);
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}

	public String summoned(String summonPhrase) {
		try {
			List<WebElement> rooms = session.findElements(By.xpath("//a[contains( @href, '/game/')]") ); //text()[contains(.,'Hosted')] and 
			for ( int i = 0; i < rooms.size(); i++ ) {
				String roomStuff = rooms.get( i ).getAttribute( "innerHTML" );				
				//System.out.println( rooms.get( i ).getAttribute( "innerHTML" ) );
				if ( rooms.get( i ).getAttribute( "innerHTML" ).startsWith( "<strong>"+summonPhrase) ) {
					System.out.print( "Found room: " );
					String host = roomStuff.split("Hosted by <strong>")[1].split("</strong>")[0];
					System.out.println( rooms.get( i ).getAttribute( "innerHTML" ) );
					rooms.get( i ).click();
					return host;
				}
			}			
			return "";
		} catch (NoSuchElementException e ) {
			e.printStackTrace();
			return "";
		}
	}

	public Setup setup(String command, Setup curSetup) {
		System.out.println( command );
		//String[] empty = new String[] {};
		Setup choice = curSetup;
		boolean newSetup = false;
		for (int i = 0; i < setups.size(); i++ )
		{
			//System.out.println( setups.get( i ).command );
			//System.out.println( command );
			if ( setups.get( i ).command.equals( command ) && !setups.get( i ).command.equals( curSetup.command ) ) {
				//System.out.println( "match");
				choice = setups.get( i );
				newSetup = true;
			}
		}
		if ( !newSetup ) {
				return choice;
		}
		
		//System.out.println( "hi");
		
		System.out.println( choice );
		
		// set start settings		
		setStart( choice.start );
		
		// set role reveal
		setRR( choice.rr );
		
		// set majority vote
		setMajority( choice.majvote );
		
		// set kp
		setkp( choice.extrakp );
		
		// set deadlock
		setDeadlock( choice.deadlock );
		
		// set forcevote
		setForceVote( choice.forcevote );
		
		// set hidden setup off ( hidden prior to game start )
		setHidden( "off" );
		
		// set setup code
		Random rand = new Random();
		setSetup( choice.codes[ rand.nextInt( choice.codes.length )] );
		
		//System.out.println( choice.command );
		
		return choice;		
	}

	// public void unexpectedClose() {
	// try {
	// session.findElement(By.xpath("//button[@class='dialog-close']")).click();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}

//1066-max, 516-min 550 pixels