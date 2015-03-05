import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.remote.internal.*;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.firefox.*;

public class Autonomy implements Runnable {
	public int lastInactiveMessage = 0; 
	public String[] deadMessages = {
			"malwareup is as dead as my social life",
			"malwareup has been as dead as a dead hat",
			"malwareup is as dead a divorce"	
	};
	
	public int dinnerMessageTime = 0;
	public int dinnerWaitTime = 0;
	public boolean waitingForDinner = false;
	public String[] dinnerMessages = {
			"brb dinner",
			"brb food"
	};
	
	public int shitTime = 0;
	public int shitWaitTime = 0;
	public boolean takingAShit = false;
	public String[] shitLeaveMessages = {
		"i had to shit all day through school`i almost shit my pants several times",
		"brb gotta showwer`and shit",
		"i swear i have colen problems or something",
		"i gotta shit like a horse",
		"i have to shit like a horse again",
		"i can barely walk`i feel like shit`no idea"
	};
	public String[] shitBackMessages = {
		"fuck i hate pissing",
		"i just shit`it felt great",
		"i have assburn`that makes my ass burn more",
		"okay, i think i ended the shitting`i hope you guys never have to fart with assburn",
		"i sneezed i hurt myself",
		"i just took a shit`seemed to solve the problem",
		"shitting is a great feeling`your stomach like moves",
		"i shit a disc out every nighty"
	};
	
	public void run() {
		Date tmp = new Date();
		
		tmp.setHours(17);
		tmp.setMinutes(0);
		dinnerMessageTime = (int)(tmp.getTime()/1000L) + ((Nasbot.rnd.nextBoolean())?1:-1)*Nasbot.rnd.nextInt(600);
		if((new Date()).getHours() >= 17)
			dinnerMessageTime += 86400;
		
		shitTime = (int)((new Date()).getTime()/1000L) + 14400 + ((Nasbot.rnd.nextBoolean())?1:-1)*Nasbot.rnd.nextInt(1800);
		
		while(true) {
			try {
				int timeNow = (int)((new Date()).getTime() / 1000L);
				
				if(timeNow-Nasbot.lastMessage < 3600 && timeNow-lastInactiveMessage <= 10800)
					lastInactiveMessage = 0;
				
				else if(timeNow-Nasbot.lastMessage >= 3600 && timeNow-lastInactiveMessage >= 10800) {
					Nasbot.sendMessage(deadMessages[Nasbot.rnd.nextInt(deadMessages.length)]);
					lastInactiveMessage = timeNow;
				}
				
				else if(timeNow >= dinnerMessageTime && !waitingForDinner) {
					if(!Nasbot.forceDropMessages) {
						Nasbot.sendMessage(dinnerMessages[Nasbot.rnd.nextInt(dinnerMessages.length)]);
						Nasbot.forceDropMessages = true;
						dinnerWaitTime = (int)((new Date()).getTime() / 1000L) + (270 + ((Nasbot.rnd.nextBoolean())?1:-1)*Nasbot.rnd.nextInt(30));
						
						waitingForDinner = true;
					}
				}
				
				else if(timeNow >= dinnerWaitTime && waitingForDinner) {
					waitingForDinner = false;
					Nasbot.forceDropMessages = false;
					
					Nasbot.sendMessage("back");
					
					tmp = new Date();
					tmp.setHours(17);
					tmp.setMinutes(0);
					dinnerMessageTime = (int)(tmp.getTime()/1000L) + ((Nasbot.rnd.nextBoolean())?1:-1)*Nasbot.rnd.nextInt(600) + 86400;
				}
	
				else if(timeNow >= shitTime && !takingAShit) {
					String[] msgs = shitLeaveMessages[Nasbot.rnd.nextInt(shitLeaveMessages.length)].split("`");
					for(String s : msgs)
						Nasbot.sendMessage(s);
					Nasbot.forceDropMessages = true;
					shitWaitTime = (int)((new Date()).getTime() / 1000L) + (150 + ((Nasbot.rnd.nextBoolean())?1:-1)*Nasbot.rnd.nextInt(30));
					
					takingAShit = true;
				}
	
				else if(timeNow >= shitWaitTime && takingAShit) {
					takingAShit = false;
					Nasbot.forceDropMessages = false;

					String[] msgs = shitBackMessages[Nasbot.rnd.nextInt(shitBackMessages.length)].split("`");
					for(String s : msgs)
						Nasbot.sendMessage(s);
					
					shitTime = (int)((new Date()).getTime()/1000L) + 14400 + ((Nasbot.rnd.nextBoolean())?1:-1)*Nasbot.rnd.nextInt(1800);
					
				}
			} catch(Exception e) { System.out.println("crashed in autonomous"); }
		}
	}
}
