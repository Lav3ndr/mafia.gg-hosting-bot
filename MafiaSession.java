// this file contains the lower-level code which interacts with the browser
// forked by Lavender - original code and and lots of help comes from aRandomZy
package mafia.gg.bot;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
 
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
    private boolean autoChangeRoomName;
    private boolean unlistedMemory;
    private String setInUse;
    private List<WebElement> chat, usn;
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
    public static final String DNCT = "59a1b1553863028a1b45a3b1568391955a2b28a1b1596418309a1";
    public static final String TRIPLECAMP = "75a1b1565630670a1b324a1b65a1b1565223000a1b1565316812a1b45a5";
    public static final String HOW2MM = "65a1b75a1b86a1b7a1b32a1b57a4b5a1b31a1";
    public static final String ABC = "65a1b7a1b9a1b1540004252a1b83a1b1583467528a1b57a4";
    
    public static final List<String> CARBON14 = new ArrayList<String>() {{
	    add( "7a1b75a2b1565630670a1b57a3" );
	    add( "7a1b1565630670a1b57a3b65a2" );
	}};   
    
    public static final List<String> DOUBLENEG = new ArrayList<String>() {{
	    add( "1596337363a2b47a1b1588642540a2b57a1b7a1" );
	    add( "1596337363a2b47a1b1588642540a2b57a1b23a1" );
	}};    
    
    public static final List<String> SUPERPOS = new ArrayList<String>() {{
	    add( "1599661616a1b37a1b1600386374a1b1556390101a1" );
	    add( "1599661616a1b37a1b1600386374a1b1547802152a1" );
	    add( "1599661616a1b37a1b1600386374a1b1573568724a1" );
	    add( "1599661616a1b1600386374a1b33a1b1580928590a1" );
	    add( "1596417444a1b37a1b1599661616a1b1547802152a1" );
	    add( "1596417444a1b1538276587a1b1596418665a1b37a1" );
	    add( "1596417444a1b1596418665a1b37a1b75a1" );
	    add( "1596417444a1b23a1b69a1b1599661616a1" );
	}};
	
	public static final List<String> MAFIAJJANI = new ArrayList<String>() {{
	    add( "323a1b1602978335a1b12a1b73a1b36a1b57a5b1596417444a1" );
	    add( "323a1b1602978335a1b73a1b36a1b57a5b1596417444a1b7a1" );
	    add( "323a1b1602978335a1b73a1b57a5b1596417444a1b7a1b12a1" );
	    add( "323a1b1602978335a1b73a1b57a4b1596417444a1b7a1b12a1b36a1" );
	    add( "323a1b1602978335a1b73a1b57a4b1596417444a1b7a1b12a1b36a1" );
	}};
	
	public static final List<String> SODIUM24 = new ArrayList<String>() {{
	    add( "5a1b7a1b75a3b1596417444a1b1565630670a1b57a5" );
	    add( "5a1b7a1b75a2b1596417444a1b1565630670a1b57a5b65a1" );
	    add( "5a1b7a1b75a1b1596417444a1b1565630670a1b57a5b65a2" );
	    add( "5a1b7a1b1596417444a1b1565630670a1b57a5b65a3" );
	}};
 
    private MafiaSession() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        List<String >creds = new ArrayList<String>();
        try {
        	//file containing username and password of bot account
        	//first line username, second line password
            File myObj = new File("credentials.txt"); 
            Scanner myReader = new Scanner(myObj);
            for (int i = 0; i < 2; i++) {
              String data = myReader.nextLine();
              //System.out.println( data );
              creds.add( data ); // read username and password from file
            }
            //System.out.println( creds );
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        this.username = creds.get( 0 );
        this.password = creds.get( 1 );
        this.autoChangeRoomName = false;
        this.unlistedMemory = false;
        this.setInUse = "";
        this.chat = new ArrayList<WebElement>();
        this.usn = new ArrayList<WebElement>();
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
 
    public static MafiaSession newSession() {
        if (UniqueObject == null)
            UniqueObject = new MafiaSession();
        return UniqueObject;
    }
 
    public void hostUnlisted() {
        try {
            Thread.sleep(500);
            //System.out.println( "hi");
            if (!this.unlistedMemory) {            	
                session.findElement(By.xpath("//div[@class='checkbox-label']")).click();
                //session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
                session.findElement(By.xpath("//button[@type='submit']")).click();
            } else {
                //session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
                session.findElement(By.xpath("//button[@type='submit']")).click();
            }
            this.unlistedMemory = true;
        } catch (NoSuchElementException | InterruptedException e) {
        	e.printStackTrace();
 
        }
    }
 
    public void hostUnlisted(String roomName) {
        if (!this.unlistedMemory) {
            session.findElement(By.xpath("//div[@class='checkbox']")).click();
            session.findElement(By.xpath("//div[@class='flex-1']/input"))
                    .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
            session.findElement(By.xpath("//div[@class='flex-1']/input")).sendKeys(roomName);
            session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
        } else {
            session.findElement(By.xpath("//div[@class='flex-1']/input"))
                    .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
            session.findElement(By.xpath("//div[@class='flex-1']/input")).sendKeys(roomName);
            session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
        }
        this.unlistedMemory = true;
    }
 
    public void host() {
        try {
            if (!this.unlistedMemory) {
                session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
            } else {
            	//System.out.println( "hello");
                session.findElement(By.xpath("//div[@class='checkbox']")).click();
                session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
            }
            this.unlistedMemory = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public void host(String roomName) {
        if (!this.unlistedMemory) {
            session.findElement(By.xpath("//div[@class='flex-1']/input"))
                    .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
            session.findElement(By.xpath("//div[@class='flex-1']/input")).sendKeys(roomName);
            session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
        } else {
            session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
            session.findElement(By.xpath("//div[@class='flex-1']/input"))
                    .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
            session.findElement(By.xpath("//div[@class='flex-1']/input")).sendKeys(roomName);
            session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
 
        }
        this.unlistedMemory = false;
    }
    
    public boolean tryHostNew() {
    	try {
    		session.findElement(By.xpath("//span[text()=\"Host new room\"]")).click();
    		Thread.sleep( 5000 );
    		session.findElement(By.xpath("//span[text()=\"Create\"]")).click();
    		Thread.sleep( 5000 );
    		System.out.println( "Hosting new room");
    		return true;
    	} catch (NoSuchElementException e) {
    		//System.out.println( "Cannot host new until game ends. Caught exception in tryHostNew().");
    		//e.printStackTrace();
    		return false;
    	} catch (Exception e) {
    		e.printStackTrace();
            //System.out.println(e.getMessage());
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
 
    public void defaultSettings() {
 
        try {
            session.findElement(By.xpath("//span[text()='Edit options']")).click();
            session.findElement(By.xpath("//button[text()='Game']")).click();
            session.manage().window().maximize();
            setDayTime(5);
            setNightTime(1);
            WebElement el = session.findElement(By.xpath(
                    "/html/body/div[2]/div/div/div/div/div[2]/div/div[3]/div/div[3]/div/div[1]/div/div/div/div[3]/div/input"));
            if (!el.isSelected()) {
                el.click();// scale timer
            }
            el = session.findElement(By.xpath(
                    "/html/body/div[2]/div/div/div/div/div[2]/div/div[3]/div/div[3]/div/div[2]/div/div/div/div[3]/div/input"));
            if (!el.isSelected()) {
                el.click();// idle players dont vote
            } // checkbox dont know wht to do
            setMajority(this.TWO_THIRDS_MAJORITY);
            // session.findElement(By.xpath("//span[text()='Save']")).click();
            save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
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
 
    public void setMajority(int code) {
        try {
            Thread.sleep(100);
            session.findElement(By.xpath("//option[@value=[code]]".replace("[code]", "" + code))).click();
        } catch (NoSuchElementException | InterruptedException e) {
 
        }
 
    }
 
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
        	
               
            session.findElement(By.xpath("//input[@placeholder='Paste setup code�']"))
                        .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
            
            //System.out.println( "Deleted");
            session.findElement(By.xpath("//input[@placeholder='Paste setup code�']")).sendKeys(setupCode);
            //Thread.sleep(100);
            session.findElement(By.xpath("//span[text()='Save']")).click();
            //Thread.sleep(100);
        } catch (NoSuchElementException e) {
        	e.printStackTrace();
        	System.out.println( "Yo"); //Paste setup code.e..
            //setSetup(setupCode);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
 
    public void setSetupOld(String setupCode) {
        try {
            try {
 
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                act.moveToElement(session.findElement(By.xpath("//span[text()='Edit options']"))).click().perform();
            } catch (NoSuchElementException e) {
            } finally {
                session.findElement(By.xpath("//input[@placeholder='Paste setup code…']"))
                        .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
                session.findElement(By.xpath("//input[@placeholder='Paste setup code…']")).sendKeys(setupCode);
            }
            try {
                save();
            } catch (NoSuchElementException e) {
                save();
            } catch (ElementClickInterceptedException e) {
                save();
            }
//          session.findElement(By.xpath("//span[text()='Save']")).click();
//      if (setupCode.equals(this.CONPLAN_5) && this.autoChangeRoomName)
//          this.changeRoomName("5 - Man ConPlan");
//      if (setupCode.equals(this.CONSIGLIERE_10) && this.autoChangeRoomName)
//          this.changeRoomName("10 - Man Consigliere");
//      if (setupCode.equals(this.CONSIGLIERE_8) && this.autoChangeRoomName)
//          this.changeRoomName("8 - Man Consigliere");
        } catch (NoSuchElementException e) {
        	e.printStackTrace();
            //setSetup(setupCode);
        }
    }
    
    public boolean setDeck( String deck ) {
    	try {
    		session.findElement(By.xpath("//span[text()='Edit options']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//button[text()='Deck']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//input[@placeholder='Filter decks by name�']")).sendKeys(deck);
            //session.findElement(By.xpath("//div[text()[contains(.,'Use no deck')] and @class='checkbox-label']")).click();
            Thread.sleep(500);
            session.findElement(By.xpath("//div[text()[contains(.,'"+deck+"')] and @class='game-options-editor-deck-title']")).click();
            Thread.sleep(100);
            
            save();
            return true;
    	} catch (Exception e) {
    		//session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
            //Thread.sleep(100);
    		e.printStackTrace();
    		save();
    		return false;
        }
    }
 
    public void setDeckOld (String name) {// not working yet
        session.findElement(By.xpath("//span[text()='Edit options']")).click();
        session.findElement(By.xpath("//button[text()='Deck']")).click();
        if (name.equalsIgnoreCase("no deck")) {
            session.findElement(
                    By.xpath("/html/body/div[2]/div/div/div/div/div[2]/div/div[2]/div[1]/div[3]/div[1]/input"));
        } else {
            session.findElement(By.xpath("//input[@placeholder='Filter decks by name�']")).sendKeys(name);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            session.findElement(By.xpath(
                    "/html/body/div[2]/div/div/div/div/div[2]/div/div[2]/div[1]/div[2]/div/div[3]/div[1]/div/input"))
                    .click();
        }
        session.findElement(By.xpath("//span[text()='Save']")).click();
 
    }
 
    public void enableAutoChangeRoomName() {
        this.autoChangeRoomName = true;
    }
 
    public void disableAutoChangeRoomName() {
        this.autoChangeRoomName = false;
    }
 
    public void changeRoomName(String roomName) {
        try {
            session.findElement(By.xpath("//span[text()='Edit options']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//button[text()='Room']")).click();
            Thread.sleep(100);
            session.findElement(
                    By.xpath("/html/body/div[2]/div/div/div/div/div[2]/div/div[2]/div/div[3]/div/div[1]/input"))
                    .sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
            Thread.sleep(100);
            session.findElement(
                    By.xpath("/html/body/div[2]/div/div/div/div/div[2]/div/div[2]/div/div[3]/div/div[1]/input"))
                    .sendKeys(roomName);
            Thread.sleep(100);
            session.findElement(By.xpath("//span[text()='Save']")).click();
            Thread.sleep(100);
            ;
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }    
    
    public void listToggle( ) {
    	try {
    		session.findElement(By.xpath("//span[text()='Edit options']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//button[text()='Room']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//div[@class='checkbox']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
            Thread.sleep(100);
            save();            
    	} catch (Exception e) {
    		e.printStackTrace();
        }
    }
    
    public void hideSetupToggle() {
    	try {
    		session.findElement(By.xpath("//span[text()='Edit options']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//button[text()='Game']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//div[text()[contains(.,'Hide setup')] and @class='checkbox-label']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
            Thread.sleep(100);
            save();
    	} catch (Exception e) {
    		e.printStackTrace();
        }
    }
    
    public void rroffToggle() {
    	try {
    		session.findElement(By.xpath("//span[text()='Edit options']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//button[text()='Game']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//div[text()[contains(.,'Hide roles upon death')] and @class='checkbox-label']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
            Thread.sleep(100);
            save();
    	} catch (Exception e) {
    		e.printStackTrace();
        }
    }
    
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
            session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
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
            session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
            Thread.sleep(100);
            save();
    	} catch (Exception e) {
    		e.printStackTrace();
        }
    }
    
    public void majOff() {
    	try {
    		session.findElement(By.xpath("//span[text()='Edit options']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//button[text()='Game']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//label[text()[contains(.,'Finalize vote on majority')]]")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//option[@value='-1']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
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
            session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
            Thread.sleep(100);
            save();
    	} catch (Exception e) {
    		e.printStackTrace();
        }
    }
    
    public void randomDeck() {
    	try {
    		session.findElement(By.xpath("//span[text()='Edit options']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//button[text()='Deck']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//button[@class='icon-shuffle']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
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
            session.findElement(By.xpath("//div[text()[contains(.,'Use no deck')] and @class='checkbox-label']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
            Thread.sleep(100);
            save();
    	} catch (Exception e) {
    		e.printStackTrace();
        }
    }
     
    public void home() {
        session.findElement(By.xpath("/html/body/div[1]/header/div/h1/a"));
    }
 
    public void autoPlayerUp(boolean choice) {
        if (choice == true) {
            while (true) {
                try {
                    session.findElement(By.xpath("//span[text()='Become a player']")).click();
                    int p = 0;
                    while (true) {
                        try {
                            autoJoinNewRoom();
                            session.findElement(By.xpath("//span[text()='Become a player']"));
                            p++;
                            if (p >= 1000)
                                break;
                        } catch (NoSuchElementException t) {
                            break;
                        }
                    }
                } catch (NoSuchElementException e) {
                    continue;
                } catch (WebDriverException e) {
                    session.quit();
                    break;
                }
                autoJoinNewRoom();
            }
        } else
            return;
    }
 
    public void autoJoinNewRoom() {
        try {
            session.findElement(By.xpath("//a[text()='Join the new room']")).click();
        } catch (NoSuchElementException e) {
 
        }
    }
 
    public boolean soundOff() {
        try {
        	Thread.sleep(100);
            //session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[1]/div[1]/button")).click();
            session.findElement(By.xpath("//div[@class='icon-settings']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("//div[@class='checkbox-label']")).click();
            Thread.sleep(100);
            session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(Keys.ESCAPE);
            //session.findElement(By.xpath("//div[@class='checkbox-label']")).click();
            //Thread.sleep(100);
            //session.findElement(By.xpath("//div[@class='checkbox-label']")).click();
            //session.findElement(By.xpath("//div[text()='Play sounds')")).click();
            //Thread.sleep(100);
            //session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/button")).click();
            //session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/button")).click();
            //session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/button")).click();
            //session.findElement(By.xpath("//div[@class='icon-settings']")).click();
            //sendMessage( "" ); // reset
            //session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[1]/div[1]/button")).click();
            //Thread.sleep(100);
            return true;
            //session.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/section[1]/div[1]/input/label")).click();
            //Thread.sleep(100);
            //System.out.println( "what the hell");
            //session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[1]/div[1]/button")).click();
            //System.out.println( "what the hell");
            //return true;
        } catch (NoSuchElementException e) {
        	e.printStackTrace();
            return false;
        } catch (ElementClickInterceptedException e) {
        	e.printStackTrace();
            act.moveToElement(session.findElement(By.xpath("//div[@class='game-checkbox-dropdown']/section[1]/div[1]")))
                    .click().perform();
            act.moveToElement(session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[1]/div[1]/button")))
                    .click().perform();
            //System.out.println( "hmm");
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        }
    }
 
    //public List<String> getChat() {
    //    try {
    //        WebElement e = session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]"));
            
    //        usn = e.findElements(
   //                 By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[1]"));
    //        chat = e.findElements(
    //                By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[2]"));
    //        System.out.println( "what about this");
    //        List<String> arr = new ArrayList<String>();
    //        for (int i = 0; i < chat.size(); i++) {
    //            arr.add(chat.get(i).getText());
    //        	System.out.println( usn.get(i).getText() );
    //        }
     //       return arr;
    //    } catch (NoSuchElementException e) {
    //        return getChat();
    //    }
    //    catch (org.openqa.selenium.StaleElementReferenceException ex) {
    //    	System.out.println(ex.getMessage());
    //    	return getChat();
   //     }
        
   // }
 
   // public List<String> getUsername() {
    //    try {
    //    	Thread.sleep( 100 );
    //        WebElement e = session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]"));
     //       usn = e.findElements(
    //                By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[1]"));
    //        chat = e.findElements(
    //                By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[2]"));
     ///       List<String> arr = new ArrayList<String>();
     //       for (int i = 0; i < usn.size(); i++)
     //           arr.add(usn.get(i).getText());
     //       return arr;
     //   } catch (NoSuchElementException e) {
    //        return getUsername();
    //    } catch ( Exception e ) {
    //    	return getUsername();
    //    }
   // }
    
    public void updateChatSmart(List<List<String>> queue, List<String> last ) {
        try {
        	List<WebElement> rawChat = session.findElements(By.xpath("//div[@class='game-chronicle-chat']"));
        	List<WebElement> names = session.findElements(By.xpath("//div[@class='game-chronicle-name']"));
        	List<String> times = new ArrayList<String>();
        	List<List<String>> curchat = new ArrayList<List<String>>();
        	for (int i = 0; i < rawChat.size(); i++) {
        		List<String> toSplit = Arrays.asList( rawChat.get( i ).getText().split( "\n") );
        		String time = toSplit.get( 0 );
        		int namelength = names.get( i ).getText().length();
        		List<String> cmd = new ArrayList<String>();
        		cmd.add( toSplit.get( 0 ));
        		cmd.add( toSplit.get( 1 ).substring( 0, namelength ) );
        		cmd.add( toSplit.get( 1 ).substring( namelength ) );
        		curchat.add( cmd );     
            }
        	boolean queueing = false;
        	if ( "".equals( last.get( 0 ) ) &&
    				"".equals( last.get( 1 ) ) &&
    				"".equals( last.get( 2 ) ) ) {
    			queueing = true;
    		}
        	for (int i = 0; i < curchat.size(); i++) {
        		//if ( curchat.get( i ).get( 0 ).compareTo( last.get( 0 ) ) < 0 ){
        		//	continue;
        		//}
        		if ( curchat.get( i ).get( 0 ).equals( last.get( 0 ) ) &&
        				curchat.get( i ).get( 1 ).equals( last.get( 1 ) ) &&
        				curchat.get( i ).get( 2 ).equals( last.get( 2 ) ) ) {
        			queueing = true;
        			continue;
        		}
        		if (queueing ) {
        			queue.add( curchat.get( i ) ); 			
        		}
        	}
        	//System.out.println( curchat );        	
        } catch (NoSuchElementException e) {
        	e.printStackTrace();
        	// shouldn't get here.
            return;
        } catch (Exception e) {
        	e.printStackTrace();
        	return;
        }
    }
 
    public int updateChat(List<String> outDatedChat, List<String> outDatedUsernames) {
        try {
        	List<WebElement> fullChat = session.findElements(By.xpath("//div[@class='game-chronicle-chat']"));
        	List<WebElement> names = session.findElements(By.xpath("//div[@class='game-chronicle-name']"));
        	List<String> times = new ArrayList<String>();
        	List<List<String>> chat = new ArrayList<List<String>>();
        	for (int i = 0; i < fullChat.size(); i++) {
        		List<String> toSplit = Arrays.asList( fullChat.get( i ).getText().split( "\n") );
        		String time = toSplit.get( 0 );
        		int namelength = names.get( i ).getText().length();
        		List<String> cmd = new ArrayList<String>();
        		cmd.add( toSplit.get( 0 ));
        		cmd.add( toSplit.get( 1 ).substring( 0, namelength ) );
        		cmd.add( toSplit.get( 1 ).substring( namelength ) );
        		chat.add( cmd );     
            }
        	System.out.println( chat );
        	System.out.print( "hi ");
        	
        	
        	
        	
            List<String> arr = outDatedChat;
            List<String> usr = outDatedUsernames;
            WebElement e = session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]"));
            List<WebElement> grp = e.findElements(
                    By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div" ));
            //List<WebElement> lastgrp = 
            //System.out.println( grp.size());
            //for (int i = 0; i < grp.size(); i++) {
            	//System.out.println( grp.get(i).size() );
            //	System.out.println( grp.get(i).getText() );
            //}
            //usn = e.findElements(
            //        By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[1]"));
            //chat = e.findElements(
            //        By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[2]"));
            String user = grp.get( grp.size() - 2).getText();
            String msg = grp.get( grp.size() - 1 ).getText();
            
            //int c = chat.size(), a = arr.size();
            //if (chat.size() > arr.size() || chat.size() == 0) {
            //    int diff = c - a;
//              System.out.println(diff + "|" + c + "|" + a);
            //    for (int i = chat.size() - diff; i < chat.size(); i++) {
            //        arr.add(chat.get(i).getText());
            //    	usr.add(usn.get(i).getText());
            //    }
                //System.out.println( getUsername() ); 
            //    return diff;
            //}
            //if (chat.size() < arr.size()) {
                //arr = getChat();
            //    return updateChat(outDatedChat, outDatedUsernames);
                
                //return arr.size();
            //}
            int size = grp.size()/2;            
            int c = size, a = arr.size();
            if (size > arr.size() || size == 0) {
                int diff = c - a;
//              System.out.println(diff + "|" + c + "|" + a);
                for (int i = size - diff; i < size; i++) {
                    arr.add(msg);
                	usr.add(user);
                }
                //System.out.println( getUsername() ); 
                return diff;
            }
            if (size < arr.size()) {
                //arr = getChat();
            	// should you ever get here?
            	return 1;
                //return updateChat(outDatedChat, outDatedUsernames);
                
                //return arr.size();
            }
            // should you ever get here?
            return 1;
            //return updateChat(outDatedChat, outDatedUsernames);
            // return 0;
        } catch (NoSuchElementException e) {
        	e.printStackTrace();
        	// shouldn't get here.
            return 0;//updateChat(outDatedChat, outDatedUsernames);
        } catch (Exception e) {
        	e.printStackTrace();
        	return 0;
        }
    }
 
    //public int updateUsername(List<String> outDatedUsername) {
     //   try {
      //      List<String> arr = outDatedUsername;
      //      WebElement e = session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]"));
       //     usn = e.findElements(
        //            By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[1]"));
         //   chat = e.findElements(
       //             By.xpath("/html/body/div[1]/main/div/div[2]/div[2]/div/div[3]/div/div/div/div/div[2]"));
      //      int c = usn.size(), a = arr.size();
      //      int diff = c - a;
      //      if (chat.size() > arr.size()) {
      //          for (int i = 0; i < diff; i++)
      //              arr.add(chat.get(c + i).getText());
      //      }
    //        return diff;
    //    } catch (NoSuchElementException e) {
    //        return updateUsername(outDatedUsername);
    //    }
   // }
 
    public void sendMessage(String str) {
        session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/input")).sendKeys(str);
        session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[3]/form/button")).click();
    }
 
    public void playerUp() {
        try {
            session.findElement(By.xpath("//span[text()='Become a player']")).click();
            //int p = 0;
            //while (true) {
            //    try {
            //        session.findElement(By.xpath("//span[text()='Become a player']"));
            //        p++;
            //        if (p >= 1000)
            //            break;
            //    } catch (NoSuchElementException t) {
            //        break;
            //    }
           // }
        } catch (NoSuchElementException e) {
        	System.out.println( "Tried to become player and couldn't.");
        	e.printStackTrace();
 
        }
    }
 
    public void playerDown() {
        try {
            session.findElement(By.xpath("//span[text()='Become a spectator']")).click();
            //int p = 0;
            //while (true) {
             //   try {
             //       session.findElement(By.xpath("//span[text()='Become a spectator']"));
             //       p++;
             //       if (p >= 1000)
             //           break;
             //   } catch (NoSuchElementException t) {
             //       break;
             //   }
           
        } catch (NoSuchElementException e) {
        	e.printStackTrace();
 
        }
    }
 
    public int playerdUp() {
        return Integer.parseInt(
                session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[1]/div[2]/span[2]")).getText());
    }
 
    public int totalPlayers() {
        return Integer.parseInt(
                session.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div[1]/div[2]/span[3]")).getText());
    }
 
    public void afkCheck() {
        session.findElement(
                By.xpath("/html/body/div[1]/main/div/div[1]/div[2]/div/div[3]/div/div[2]/div/div/div/button[1]"))
                .click();
    }
 
    public void startGame() {
    	try {
    		//Thread.sleep(2000);
    		session.findElement(
                    By.xpath("/html/body/div[1]/main/div/div[1]/div[2]/div/div[3]/div/div[2]/div/div/div/button[3]"))
                    .click();
    	} catch (Exception e) {
    		e.printStackTrace();
        	return;
        }
        
    }
 
    public void unexpectedClose() {
        try {
            session.findElement(By.xpath("//button[@class='dialog-close']")).click();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}
 
//1066-max, 516-min 550 pixels