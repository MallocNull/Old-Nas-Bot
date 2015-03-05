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

public class Ghost implements Runnable {
	static WebDriver driver;
	static int currentMessage = 0;
	static int messageDivSize = 0;
	static Random rng = new Random();

	static String[] connectMessages = {
		"hello",
		"hi",
		"welcome",
		"ey",
		"woah it's",
		"shit it's",
		"oh fuck it's",
		"get out",
		"no one wants you here",
		"why were you put on this earth"
	};
	
	static String[] leaveMessages = {
		"what a kidder",
		"actually that kid wasn't that bad",
		"i could smell that kid from a mile away",
		"kid rots like a corpse",
		"that kid won't be back",
		"and to think i could've collected that kid's life insurance",
		"that kid didn't look on the bright side of life",
		"i hope that kid never comes back",
		"i hope that kid falls off a cliff",
		"i'm going to go to that kid's house with a knife",
		"hope that kid sleeps with one eye open"
	};
	
	public static void login() {
		driver.get("http://malwareup.org");
		driver.findElement(By.linkText("Login")).click();
		(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(By.id("username"))).sendKeys("Hamiltonian");
		driver.findElement(By.id("password")).sendKeys("********");
		driver.findElement(By.name("login")).click();
		(new WebDriverWait(driver, 300)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("MalwareUp II Chat"))).click();
		(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(By.id("inputField")));
		List<WebElement> chatdata = driver.findElement(By.id("chatList")).findElements(By.tagName("div"));
		messageDivSize = chatdata.size();
		for(WebElement we : chatdata) {
			if(Integer.parseInt(we.getAttribute("id").substring(11)) > currentMessage)
				currentMessage = Integer.parseInt(we.getAttribute("id").substring(11)); 
		}
	}

	
	public static void sendMessage(String text) {
		driver.findElement(By.id("inputField")).sendKeys(text);
		driver.findElement(By.id("submitButton")).click();
		try { Thread.sleep(500); } catch(Exception e) {}
	}
	
	public static void logout() {
		driver.findElement(By.id("logoutButton")).click();
	}
	
	public static Message waitForNewMessage() {
		int temp = currentMessage;
		
		if(messageDivSize >= 50) {
			driver.navigate().refresh();
			(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(By.id("inputField")));
			List<WebElement> chatdata = driver.findElement(By.id("chatList")).findElements(By.tagName("div"));
			messageDivSize = chatdata.size();
			for(WebElement we : chatdata) {
				if(Integer.parseInt(we.getAttribute("id").substring(11)) > currentMessage)
					currentMessage = Integer.parseInt(we.getAttribute("id").substring(11)); 
			}
		}

		while(true) {
			currentMessage = temp+1;
			boolean found = false;
			for(; currentMessage-temp <= 10; currentMessage++) {
				try {
					driver.findElement(By.id("chatList")).findElement(By.id("ajaxChat_m_"+currentMessage));
					found = true;
					break;
				} catch(Exception e) {}
			}
			if(found) break;
			try { Thread.sleep(1000); } catch(Exception e) {}
		}
		
		String msg = driver.findElement(By.id("ajaxChat_m_"+(currentMessage))).getText().substring(11);
		return new Message(msg.substring(0,msg.indexOf(':')),msg.substring(msg.indexOf(':')+2));
	}
	
	public void run() {
		driver = new FirefoxDriver();
		login();
		
		while(true) {
			try {
				Message msg = waitForNewMessage();
				if(msg.name.toLowerCase().equalsIgnoreCase("chatbot") && rng.nextInt(5) == 2) {
					if(msg.message.toLowerCase().contains("in")) {
						sendMessage(connectMessages[rng.nextInt(connectMessages.length-1)] +" "+ msg.message.substring(0, msg.message.indexOf(" logs into")));
					}
	
					if(msg.message.toLowerCase().contains("out")) {
						sendMessage(leaveMessages[rng.nextInt(leaveMessages.length-1)]);
					}
				}
				
				if(msg.message.toLowerCase().contains("get out of here stalker")) break;
			} catch(Exception e) {}
		}
		
		logout();
	}
}
