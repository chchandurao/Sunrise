package com.selenium.keywordfw.userdefined;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.selenium.keywordfw.standard.OpenBrowser;

public class Loginto {
	public static WebDriver driver;		
		
		
	public static boolean Loginto(String list[]){
	 driver = OpenBrowser.driver;
		
		String username=list[2];
		String password=list[3];		
		driver.findElement(By.id("uname")).clear();
		driver.findElement(By.id("uname")).sendKeys(username);
		driver.findElement(By.id("pword")).clear();
		driver.findElement(By.id("pword")).sendKeys(password);	    
		driver.findElement(By.id("submit")).click();
	    return true;
	}	
}