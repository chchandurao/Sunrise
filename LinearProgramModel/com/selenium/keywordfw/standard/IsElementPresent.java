package com.selenium.keywordfw.standard;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
public class IsElementPresent {
	public static WebDriver driver;
	
	public static boolean isElementPresent(By by) {
		 driver = OpenBrowser.driver;
		try {
			long end = System.currentTimeMillis() + 3000;
		       while (System.currentTimeMillis() < end) {		    	   
		        if(driver.findElement(by).isDisplayed()){		        	
		        	break;        		
		           }		    	   
		         else{
		          	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);		          	
		            }		    	   
		        }		       
		       return true;
		      
		    } catch (NoSuchElementException e) {
		    	System.out.println("Exception");
		      return false;
		    	}
	 }
	

	
	
	public static boolean isElementPresent(String list[]) throws IOException
	{
		 driver = OpenBrowser.driver;
		String result="fail";
		String targetType=list[2];
		String targetTypeValue=list[3];
		switch(targetType){
		case "xpath":
			if(isElementPresent(By.xpath(targetTypeValue)))
				result="pass";				
			break;
			
		case "id":
			if(isElementPresent(By.id(targetTypeValue)))
				result="pass";
			break;
		default:
			
		} // end of switch
		
		if(result.equals("pass"))
			return true;
		else
			return false;
		}
	}
