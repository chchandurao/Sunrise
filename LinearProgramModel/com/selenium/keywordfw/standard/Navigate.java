package com.selenium.keywordfw.standard;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Navigate {
	public static WebDriver driver;
	public static boolean Navigate(String list[])throws IOException			
	{
		 driver = OpenBrowser.driver;
		String targetType=list[2];
		String targetTypeValue=list[3];
		String targetValue=list[4];
		String result="fail";
		switch(targetType){
		case "xpath":
			switch(targetValue){
				case "click":
					if(IsElementPresent.isElementPresent(By.xpath(targetTypeValue))){
						try{
							result="pass";
							WebElement we = driver.findElement(By.xpath(targetTypeValue));
						    if (driver instanceof JavascriptExecutor) {
						    	((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", we);
						    }
						    we.click();
						}catch(Exception e){
							System.out.println("			Below step, element is not currently visible and so may not be interacted with.");
							result="fail";
							}					
					}				
					break;				
				default:		
				}
			break;
		case "id":
			switch(targetValue){
			case "click":
				if(IsElementPresent.isElementPresent(By.id(targetTypeValue))){						
					driver.findElement(By.id(targetTypeValue)).click();
					result="pass";
				}				
				break;				
			default:			
				}
			break;
		default:
		}
		if(result.equals("pass")){return true;}else{return false;}		
	}


}
