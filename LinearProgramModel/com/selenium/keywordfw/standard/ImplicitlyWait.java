package com.selenium.keywordfw.standard;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

public class ImplicitlyWait {
	public static WebDriver driver;
	public static boolean implicitlyWait(String list[])
	{
		 driver = OpenBrowser.driver;
		String waitTime=list[2];
		String result="fail";	
		try { 
			// Note: below line cannot convert string into long int.
	        long l = Long.valueOf(waitTime.replaceAll(".", "").toString());
	        driver.manage().timeouts().implicitlyWait(l, TimeUnit.SECONDS);
	        result="pass";
	     } catch (NumberFormatException nfe) {
	        System.out.println("NumberFormatException: " + nfe.getMessage());        
	     }	
		if(result.equals("pass")){return true;}else{return false;}
	}

}
