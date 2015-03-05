import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.remote.internal.*;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.firefox.*;

/*
 * +around 6:00PM +- a few minutes, say "brb dinner" or "brb food", comes back to chat 4 or 5 minutes later with "back"
 * at a random time in the afternoon/evening, "brb gonna shoot some hoops", comes back with "back" 5 or 6 minutes later
 * +someone says "lmao" reply with ";p;"
 * +randomly throughout the day, "brb gotta shit" then 2 or 3 minutes later "that was a good shit"
 * +everytime someone gets kicked reply with "i can smell a ban" "and it doesn't taste too good". that's gotta be on two lines
 * once an hour or so pull a line from that ultimate nas log (http://malwareup.org/logs/nas/the%20ultimate%20nas.txt)
 * +"ati" or "radeon" or "nvidia" reply with a line from http://malwareup.org/logs/nas/the%206770.txt
 * +"what's your specs" "what are your specs" or even just "specs" reply with "quad core 4gb of RAM, and 6770"
 * +if "is it down" or "is down" then "it was up and its down up and down today"
 * +balls triggers "my balls are stuck to my leg" "should i like put lotion down there?" "to make it silky>"
 * +sometimes, instead of brb gotta shit, "i think i gotta shit" "i blame my school for it" "and those damn hot pockets"
 * +sometimes after long inactivity "malwareup is as dead as my social life" or "malwareup has been as dead as a dead hat" or "malwareup is as dead a divorce"
 * +mentioning linux yields "i should install arch" "on my balls"
 */

public class Nasbot {
	static String username = "Nas49Razing";
	static String password = "***********";
	static String version = "2.2.6";
	static WebDriver driver;
	static int currentMessage = 0;
	static int messageDivSize = 0;
	static Random rnd = new Random();
	static int lastAction = 0;
	static int lastMessage = 0;
	static int lastHardwareMsg = 0;
	static final int hardwareCooldown = 7200;
	static int lastXPMsg = 0;
	static final int XPCooldown = 7200; 
	static final int coolDownTime = 15; // minutes
	static boolean textProtection = false;
	static boolean forceDropMessages = false;
	static String[] the6770 = {
		"but i would have bought it over the 6770",
		"and a 6770",
		"that is why i got a 6770",
		"me loves me new 6770",
		"is the 6770 on that list?",
		"so the 6770 is low end?",
		"quad core 4gb of RAM, and 6770",
		"6770",
		"my 6770 is more powerful then that",
		"you could get a 6770 now from them for 100",
		"i still like my 6770",
		"but i would have bought it over the 6770",
		"and a 6770",
		"that is why i got a 6770"
	};
	static String[] kickedMsgs = {
		"i can smell a ban`and it doesn't taste too good",
		"we have another case of a dumbass at malwareup",
		"that bitch is stupid",
		"sounds like a dumbass",
		"goddamn he is stupid as shit",
		"time to beat some ass",
		"my asshole has more IQ then that guy",
		"i think my ass is smarter then that guy",
		"they can go such a cock",
		"you get raped in the ass",
		"why is this so gay in here",
		"someone is about to get there ass whopped out",
		"i hope he never hits puberty"
	};
	static String[] linuxMsgs = {
		"i should install arch`on my balls",
		"inb4 my ISP shuts off my internet for downloading arch",
		"and i could run arch on a SD card>",
		"Linux cost me a ass and a leg",
		"how do i install arch without internet?",
		"imagine if i used linux`how many errors it would have"
	};
	static String[] ballsMsgs = {
		"my balls are stuck to my leg`should i like put lotion down there?`to make it silky>",
		"how does one have balls on there head?",
		"my balls burn`what did i do to them",
		"your balls are stuck in your ass"
	};
	static String[] jackOffMsgs = {
		"at least i ain't chacking off with super g;ie",
		"its fucking awesome to chack off with glue`shit sticks together",
		"i should try to jackoff with dry ice"
	};
	
	static final String[] loginInfo = {"Nas49Razing", "***********"};
	
	public static void orientSelf() {
		List<WebElement> chatdata = driver.findElement(By.id("chatList")).findElements(By.tagName("div"));
		messageDivSize = chatdata.size();
		for(WebElement we : chatdata) {
			if(Integer.parseInt(we.getAttribute("id").substring(11)) > currentMessage)
				currentMessage = Integer.parseInt(we.getAttribute("id").substring(11)); 
		}
		if(driver.findElement(By.id("audioButton")).getAttribute("class").equalsIgnoreCase("button"))
			driver.findElement(By.id("audioButton")).click();
	}
	
	public static void login() {
		driver.get("http://malwareup.org");
		driver.findElement(By.linkText("Login")).click();
		(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(By.id("username"))).sendKeys(username);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.name("login")).click();
		(new WebDriverWait(driver, 300)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("MalwareUp II Chat"))).click();
		(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(By.id("inputField")));
		orientSelf();
		lastMessage = (int)((new Date()).getTime() / 1000L);
	}
	
	public static boolean checkForWord(String word, String query) {
		if(query.toLowerCase().contains(word.toLowerCase()+" ") || query.toLowerCase().contains(" "+word.toLowerCase()) || query.equalsIgnoreCase(word.toLowerCase()))
			return true;
		else return false;
	}
	
	public static void sendMessage(String text) {
		sendMessage(text, true, false);
	}
	
	public static void sendMessage(String text, boolean checkProtection, boolean endProtection) {
		if(forceDropMessages) return;
		if(checkProtection) while(textProtection);
		textProtection = true;
		driver.findElement(By.id("inputField")).sendKeys(text);
		driver.findElement(By.id("submitButton")).click();
		try { Thread.sleep(500); } catch(Exception e) {}
		textProtection = endProtection;
	}
	
	public static void logout() {
		driver.findElement(By.id("logoutButton")).click();
	}
	
	public static Message waitForNewMessage() {
		int temp = currentMessage;
		
		if(messageDivSize >= 50) {
			while(textProtection);
			textProtection = true;
			driver.navigate().refresh();
			try {
				(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(By.id("inputField")));
			} catch(Exception e) {}
			orientSelf();
			System.out.println(currentMessage +" "+ messageDivSize);
			textProtection = false;
		}
		
		/*while(true) {
			currentMessage = temp+1;
			boolean found = false;
			for(; currentMessage-temp <= 200; currentMessage++) {
				try {
					if(currentMessage-temp >= 25) currentMessage+=4;
					driver.findElement(By.id("chatList")).findElement(By.id("ajaxChat_m_"+currentMessage));
					found = true;
					break;
				} catch(Exception e) { }
			}
			if(found) break;
			try { Thread.sleep(1000); } catch(Exception e) { }
		}*/
		
		System.out.println("Waiting for msg id greater than "+ currentMessage);
		
		while(true) {
			List<WebElement> chatdata = driver.findElement(By.id("chatList")).findElements(By.tagName("div"));
			boolean found = false;
			for(WebElement we : chatdata) {
				int nodeID = Integer.parseInt(we.getAttribute("id").substring(11));
				if(nodeID > currentMessage) {
					currentMessage = nodeID;
					found = true;
					break;
				}
			}
			if(found) break;
			try { Thread.sleep(1000); } catch(Exception e) { }
		}
		
		/*while(true) {
			int currentDivSize = driver.findElement(By.id("chatList")).findElements(By.tagName("div")).size();
			if(currentDivSize > messageDivSize) {
				currentMessage = temp+1;
				boolean found = false;
				for(;currentMessage-temp<=10;currentMessage++) {
					try {
						driver.findElement(By.id("chatList")).findElement(By.id("ajaxChat_m_"+currentMessage));
						found = true;
						break;
					} catch(Exception e) {}
				}
				if(found) break;
				else {
					System.out.println("a");
					messageDivSize = currentDivSize;
				}
			} else if(currentDivSize < messageDivSize) {
				//System.out.println("b");
				//messageDivSize = currentDivSize;
			}
			try { Thread.sleep(100); } catch(Exception e) {}
		}*/
		
		messageDivSize++;
		String msg = driver.findElement(By.id("ajaxChat_m_"+(currentMessage))).getText().substring(11);
		System.out.println(msg);
		return new Message(msg.substring(0,msg.indexOf(':')),msg.substring(msg.indexOf(':')+2));
	}
	
	public static void main(String[] args) {
		driver = new FirefoxDriver();
		
		//(new Thread(new Ghost())).start();
		
		login();
		/*sendMessage("NAS™ BOT v"+ version);
		sendMessage("© NNRACING 2003");*/
		
		(new Thread(new Autonomy())).start();
		
		while(true) {
			try {
				Message msg = waitForNewMessage();

				lastMessage = (int)((new Date()).getTime() / 1000L);
				
				if(!msg.name.equalsIgnoreCase(username.toLowerCase()) || forceDropMessages) {
					if(msg.name.equalsIgnoreCase("chatbot") && msg.message.toLowerCase().contains("kicked")) {
						String selmsg = kickedMsgs[rnd.nextInt(kickedMsgs.length)];
						String[] msgarr =  selmsg.split("`");
						if(msgarr.length == 1) {
							sendMessage(selmsg);
						} else if(msgarr.length == 2) {
							sendMessage(msgarr[0], true, true);
							sendMessage(msgarr[1], false, false);
						} else {
							sendMessage(msgarr[0], true, true);
							for(int i=1;i<msgarr.length-1;i++)
								sendMessage(msgarr[i], false, true);
							sendMessage(msgarr[msgarr.length-1], false, false);
						}
					}
					
					if(msg.name.equalsIgnoreCase("chatbot") && msg.message.toLowerCase().contains("ham62")) {
						sendMessage("he is what you call a \"vintage fag\"", true, true);
						sendMessage("aka legacy fag", false, false);
					}
					
					if(msg.name.equalsIgnoreCase("malloc(null);") && msg.message.toLowerCase().contains("fuck off nas")) {
						sendMessage("mission accomplished");
						break;
					}
					
					if(msg.message.toLowerCase().startsWith("(whispers)")) {
						if(msg.message.toLowerCase().contains("nnracing")) {
							lastAction = 0;
							sendMessage("/msg "+ msg.name +" mission accomplished");
						}
					}
					
					if((rnd.nextInt(100) == 50 && !msg.name.equalsIgnoreCase("chatbot")) || (msg.message.contains("\n") && rnd.nextInt(10)==5)) {
						List<String> mesg = Arrays.asList(msg.message.toLowerCase().replace(".", "").replace("?", "").replace("!", "").replace(",", "").split(" "));
						Collections.shuffle(mesg);
						
						StringBuilder b = new StringBuilder();
						for(String s : mesg)
							b.append(s +" ");
						
						sendMessage(b.toString());
					}
					
					int actionTime = (int)((new Date()).getTime() / 1000L);
					if(actionTime-lastAction >= coolDownTime*60) {
						boolean triggered = false;
						
						if(msg.message.toLowerCase().contains("lmao")) {
							sendMessage(";p;");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("i3")) {
							sendMessage("that processor is old as cock");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("gentoo") || msg.message.toLowerCase().contains("gen2")) {
							sendMessage("gentoo is way to much");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("xbox") || msg.message.toLowerCase().contains("ps2") || msg.message.toLowerCase().contains("ps3") || msg.message.toLowerCase().contains("ps4") || msg.message.toLowerCase().contains("nintendo") || msg.message.toLowerCase().contains("wii")) {
							sendMessage("if i had a damn 6990 i wouldn't play shit on a fucking console");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("waylt")) {
							sendMessage("[url]http://www.youtube.com/watch?v=Fj7Vklv5nDk[/url]");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().equalsIgnoreCase("wat")) {
							sendMessage("what the fuck does that mean");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("nsa") || msg.message.toLowerCase().contains("sopa")) {
							if(rnd.nextBoolean()) sendMessage("did you know about the windows TSA backdoor");
							else sendMessage("SOPA CAN SUCK MY LEFT ASS CHEAK");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("born in")) {
							sendMessage("i came from some place in ohio", true, true);
							sendMessage("and through some spirm", false, true);
							sendMessage("into a rectium", false, true);
							sendMessage("into a uterous", false, true);
							sendMessage("backout into a fagina", false, false);
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("mormon")) {
							sendMessage("who is that mormon");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("techbrent")) {
							sendMessage("a hillybilly that puts his dick inside processor pins", true, true);
							sendMessage("he is too supid to figure it out", false, true);
							sendMessage("and who builds a PC inside a old shitty compaq case", false, false);
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("install") && msg.message.toLowerCase().contains("window")) {
							if(rnd.nextBoolean()) {
								sendMessage("your better off removing windows", true, true);
								sendMessage("and going right to linxu", false, true);
								sendMessage("and saving a lot of cash", false, false);
							} else {
								sendMessage("i gotta try to get windows on that PC");
							}
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("mid-range") || msg.message.toLowerCase().contains("midrange")) {
							sendMessage("dem fucks don't know shit about power computing", true, true);
							sendMessage("inb4 they go try crysis on it", false, false);
							triggered = true;
						}
						
						else if(checkForWord("ati", msg.message) || msg.message.toLowerCase().contains("amd") || msg.message.toLowerCase().contains("radeon") || msg.message.toLowerCase().contains("nvidia") || checkForWord("intel", msg.message)) { 
							sendMessage(the6770[rnd.nextInt(the6770.length)]);
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("fuck off nas")) {
							sendMessage("inb4 i get murdered");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("hiv") || msg.message.toLowerCase().contains("aids")) {
							sendMessage("AIDS VS HIV", true, true);
							sendMessage("TOES VS FINGERS", false, false);
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().startsWith("where is my ")) {
							sendMessage("lost my "+ msg.message.substring("where is my ".length()) +" i think", true, true);
							sendMessage("it was shoved under my ass", false, false);
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("specs")) {
							sendMessage("quad core 4gb of RAM, and 6770");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("is") && msg.message.toLowerCase().contains("down")) {
							sendMessage("it was up and its down up and down today");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("tourette")) {
							sendMessage("who pissed on the couch, shit in my underwear and puked on the seat", true, true);
							sendMessage("i did", false, false);
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("ball")) {
							String selmsg = ballsMsgs[rnd.nextInt(ballsMsgs.length)];
							String[] msgarr =  selmsg.split("`");
							if(msgarr.length == 1) {
								sendMessage(selmsg);
							} else if(msgarr.length == 2) {
								sendMessage(msgarr[0], true, true);
								sendMessage(msgarr[1], false, false);
							} else {
								sendMessage(msgarr[0], true, true);
								for(int i=1;i<msgarr.length-1;i++)
									sendMessage(msgarr[i], false, true);
								sendMessage(msgarr[msgarr.length-1], false, false);
							}
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("chemistry") || msg.message.toLowerCase().contains("trigonometry")) {
							sendMessage("is mercury a base or a acid?", true, true);
							sendMessage("and what is its Pi?", false, false);
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("shower")) {
							sendMessage("i smell like fresh coockin");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("jack") && msg.message.toLowerCase().contains("off")) {
							String selmsg = jackOffMsgs[rnd.nextInt(jackOffMsgs.length)];
							String[] msgarr =  selmsg.split("`");
							if(msgarr.length == 1) {
								sendMessage(selmsg);
							} else if(msgarr.length == 2) {
								sendMessage(msgarr[0], true, true);
								sendMessage(msgarr[1], false, false);
							} else {
								sendMessage(msgarr[0], true, true);
								for(int i=1;i<msgarr.length-1;i++)
									sendMessage(msgarr[i], false, true);
								sendMessage(msgarr[msgarr.length-1], false, false);
							}
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("main pc")) {
							sendMessage("main PC", true, true);
							sendMessage("hurr durr", false, true);
							sendMessage("HURRFUCKINGDURRR", false, true);
							sendMessage("DURRRRRRR", false, false);
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("spanish")) {
							sendMessage("la compuador, facie in my ass.");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("ukraine") || msg.message.toLowerCase().contains("russia") || msg.message.toLowerCase().contains("georgia") || msg.message.toLowerCase().contains("poland")) {
							sendMessage("i've been keeping up with russiatoday", true, true);
							sendMessage("i know whats going on", false, false);
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("dog") || msg.message.toLowerCase().contains("dawg")) {
							sendMessage("why is there hot air coming out my dog's anus");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("weed") || msg.message.toLowerCase().contains("marijuana") || msg.message.toLowerCase().contains("pot")) {
							sendMessage("that guy must have smoked 50 pounts of pot");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("cow")) {
							sendMessage("<implying cows shit there pants");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("nas") && msg.message.toLowerCase().contains("log")) {
							sendMessage("i'd love to see the kind of bullshit i've said");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("linux") || msg.message.toLowerCase().contains("unix") || msg.message.toLowerCase().contains("fedora") || checkForWord("arch", msg.message) || msg.message.toLowerCase().contains("ubuntu")) {
							String selmsg = linuxMsgs[rnd.nextInt(linuxMsgs.length)];
							String[] msgarr =  selmsg.split("`");
							if(msgarr.length == 1) {
								sendMessage(selmsg);
							} else if(msgarr.length == 2) {
								sendMessage(msgarr[0], true, true);
								sendMessage(msgarr[1], false, false);
							} else {
								sendMessage(msgarr[0], true, true);
								for(int i=1;i<msgarr.length-1;i++)
									sendMessage(msgarr[i], false, true);
								sendMessage(msgarr[msgarr.length-1], false, false);
							}
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("you") && (msg.message.toLowerCase().contains("black") || msg.message.toLowerCase().contains("nigger"))) {
							sendMessage("no im a cauasion", true, true);
							sendMessage("or asian", false, false);
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("new") && (msg.message.toLowerCase().contains("hard drive") || msg.message.toLowerCase().contains("harddrive") || msg.message.toLowerCase().contains("hdd") || msg.message.toLowerCase().contains("hd"))) {
							sendMessage("HURRDUDURUR I GOT ME HARD DIRIVIEIRI IEIRIIFRIRFIoiG");
							triggered = true;
						}
						
						else if(msg.message.toLowerCase().contains("badass")) {
							sendMessage(msg.message.toLowerCase().replace(".", "").replace("?", "").replace("!", "").replace("badass", "nadfass"));
							triggered = true;
						}

						else if(checkForWord("xp", msg.message.toLowerCase())) {
							if(actionTime-lastXPMsg >= XPCooldown) {
								sendMessage("XP 64 bit sucks a cock", true, true);
								sendMessage("hey that rhymes", false, false);
								lastXPMsg = actionTime;
								triggered = true;
							}
						}
						
						else if(msg.message.toLowerCase().contains("hardware")) {
							if(actionTime-lastHardwareMsg >= hardwareCooldown) {
								sendMessage("harfware", true, true);
								sendMessage("harflwarfl", false, true);
								sendMessage("warflgarfl", false, false);
								lastHardwareMsg = actionTime;
								triggered = true;
							}
						}
						
						if(msg.message.toLowerCase().contains("im ") || msg.message.toLowerCase().contains("i'm ") || msg.message.toLowerCase().contains("i am ") && !triggered) {
							int age = 0;
							
							if(msg.message.toLowerCase().contains("i'm ")) {
								try {
									for(int i = 1;; i++) {
										try {
											int baseIndex = msg.message.toLowerCase().indexOf("i'm ")+"i'm ".length();
											age = Integer.parseInt(msg.message.substring(baseIndex,baseIndex+i));
										} catch(Exception ee) {
											break;
										}
									}
								} catch(Exception e) {}
							}

							if(msg.message.toLowerCase().contains("im ")) {
								try {
									for(int i = 1;; i++) {
										try {
											int baseIndex = msg.message.toLowerCase().indexOf("im ")+"im ".length();
											age = Integer.parseInt(msg.message.substring(baseIndex,baseIndex+i));
										} catch(Exception ee) {
											break;
										}
									}
								} catch(Exception e) {}
							}

							if(msg.message.toLowerCase().contains("am ")) {
								try {
									for(int i = 1;; i++) {
										try {
											int baseIndex = msg.message.toLowerCase().indexOf("am ")+"am ".length();
											age = Integer.parseInt(msg.message.substring(baseIndex,baseIndex+i));
										} catch(Exception ee) {
											break;
										}
									}
								} catch(Exception e) {}
							}

							if(age != 0) {
								sendMessage("he isn't "+ age, true, true);
								sendMessage("he is lying up the ass", false, false);
								triggered = true;
							}
						}
						
						
						if(triggered) lastAction = actionTime;
					}
				}
			} catch(Exception e) { 
				/*try {
					System.out.println("hi moma");
					driver.findElement(By.id("userNameField")).clear();
					driver.findElement(By.id("userNameField")).sendKeys(loginInfo[0]);
					driver.findElement(By.id("passwordField")).clear();
					driver.findElement(By.id("passwordField")).sendKeys(loginInfo[1]);
					driver.findElement(By.id("loginButton")).click();
					(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(By.id("inputField")));
					orientSelf();
				} catch(Exception ee) {
					try {
						System.out.println("hi momb");
						e.printStackTrace();
						driver.findElement(By.id("inputField"));
					} catch(Exception eee) {
						System.out.println("hi momc");
						driver.navigate().refresh();
					}
				}*/
			}
		}
		logout();
		driver.close();
	}
}
