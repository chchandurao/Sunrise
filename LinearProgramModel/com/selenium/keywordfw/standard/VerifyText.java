package com.selenium.keywordfw.standard;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VerifyText {
	public static WebDriver driver;
	public static boolean verifyText(String Arg01, String Arg02, String Arg03){
		 driver = OpenBrowser.driver;
		String result="fail";
		switch(Arg01){
		case "xpath":			
			WebElement we = driver.findElement(By.xpath(Arg02));
			if(we.getAttribute("title").equals(Arg03)){
				result="pass";
			}
			break;
		default:
		}
		if(result.equals("pass")){return true;}else{return false;}
	}

}
