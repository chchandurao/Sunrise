package com.selenium.keywordfw.standard;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SelectListItem {
	public static WebDriver driver;
	public static boolean selectListItem(String list[]){
		 driver = OpenBrowser.driver;
		String targetType=list[2];
		String targetTypeValue=list[3];
		String targetItemName=list[4];			
		String result="fail";		
		switch(targetType){
		case "xpath":
			if(IsElementPresent.isElementPresent(By.xpath(targetTypeValue))){
				try{
					List<WebElement> listItems = driver.findElements(By.xpath(targetTypeValue));
					if(listItems.size()>=1){					
				    for(int x = 1; x <= listItems.size(); x = x+1) {	    	
				    	String SourceItemName = driver.findElement(By.xpath(targetTypeValue+"["+x+"]")).getText();
				    	if(targetItemName.equalsIgnoreCase(SourceItemName)){	    	
				    	    driver.findElement(By.xpath(targetTypeValue+"["+x+"]")).click();
				    	    result = "pass";
				    	   }	    	
				    	}
					}
					else{
						System.out.println("			No elements found in the List.");
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
