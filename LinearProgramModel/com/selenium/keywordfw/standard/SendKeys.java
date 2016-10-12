package com.selenium.keywordfw.standard;


import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SendKeys{
	public static WebDriver driver;	
	static String [ ] list = new String [20];
	
public static boolean SendKeys(String list[]) throws IOException		
{			
	 driver = OpenBrowser.driver;
	String targetType=list[2];
	String targetTypeValue=list[3];
	String targetValue=list[4];			
	String result="fail";
	switch(targetType){
	case "xpath":
		IsElementPresent iep = new IsElementPresent();		
		if(iep.isElementPresent(By.xpath(targetTypeValue))){
			try{
				result="pass";
				driver.findElement(By.xpath(targetTypeValue)).sendKeys(targetValue);				    
				}catch(Exception e){
				System.out.println("			Below step, element is not currently visible and so may not be interacted with.");
				result="fail";
				}					
			}				
		break;				
	case "id":
		
		default:
	}
	if(result.equals("pass")){return true;}else{return false;}		
}

}