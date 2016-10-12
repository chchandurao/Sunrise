package com.selenium.keywordfw.standard;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverDelay {

	public static WebDriver driver;
	
	public static boolean WebDriverWait(String list[]){
		 driver = OpenBrowser.driver;
		String webDriverWaitDetails=list[2];
		String targetType=list[3];
		String targetTypeValue=list[4];
		
		String result="fail";
		String[] array = webDriverWaitDetails.split(":",-1);
		String waitType = array[0];
		int waitTime = Integer.parseInt(array[1]);	
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		
		/* If this functionality stops working make sure that you uncomment below lines
		 * driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); 
		 */
		//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				
		if(waitType.equals("visibility"))
		{			
			switch(targetType){
			case "xpath":
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(targetTypeValue)));
				result="pass";
				break;
			case "id":
				result="pass";
				break;
			default:
			}									
		}		
		if(waitType.equals("invisibility"))
		{
			switch(targetType){
			case "xpath":
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(targetTypeValue)));
				result="pass";
				break;
			case "id":
				result="pass";
				break;
			default:
			}
		}	
		if(result.equals("pass")){return true;}else{return false;}
	}


}
