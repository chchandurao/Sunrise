package com.selenium.keywordfw.standard;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

public class AssertText {
	public static WebDriver driver;
	public static boolean AssertText(String list[]){	
		 driver = OpenBrowser.driver;
		String targetType=list[2];
		String targetTypeValue=list[3];
		String targetItemName=list[4];			
		String result="fail";		
		switch(targetType){
		case "xpath":
			if(IsElementPresent.isElementPresent(By.xpath(targetTypeValue))){
				try{
					 new FluentWait<WebDriver>(driver)
						.withTimeout(60, TimeUnit.SECONDS)// Wait dynamically for an object to show in max of 30 sec
						.pollingEvery(5, TimeUnit.SECONDS) //poll for every 5 seconds
						.ignoring(NoSuchElementException.class)// Above line will Ignore exceptions, even if element is not found in 30 sec
						.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(targetTypeValue)));	
					    String Textvalue = driver.findElement(By.xpath(targetTypeValue)).getText();					    
					    if(targetItemName.equals(Textvalue)){
					    	result = "pass";
					    }	    														    
					}catch(Exception e){
					System.out.println("			Below step, element is not currently visible and so may not be interacted with.");
					result="fail";
					}					
				}	
			break;				
		case "id":
			System.out.println("			Logic not Implemented.");
			result = "FAIL";
			break;
		default:
		}
		if(result.equals("pass")){return true;}else{return false;}	
	}
	
}
