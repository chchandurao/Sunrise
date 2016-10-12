package com.selenium.keywordfw.standard;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.WebDriver;

public class WindowHandler {
	public static WebDriver driver;	
	
	public static boolean WindowHandle(String list[])
	{
		 driver = OpenBrowser.driver;
		String windowParameter=list[2]; //Currenlty not in use
		String parameterValue=list[3];	
		String result="fail";
		Iterator<String> iter;
		Set<String> ids;
		ids = driver.getWindowHandles();
		iter = ids.iterator();
		for(int i=1; i<=ids.size(); i++){
			String title = iter.next();
			driver.switchTo().window(title);
			if(driver.getTitle().equals(parameterValue)){
				System.out.println("			Title match found on Browser  "+i);
				result="pass";
				break;				
			}						
		}
		if(result.equals("pass")){			
			return true;	
		}else{
			// If window not found, role back driver control to the default window [Parent window]
			ids = driver.getWindowHandles();
			iter = ids.iterator();
			for(int i=1; i<=1;){
				String title = iter.next();
				driver.switchTo().window(title);
					break;				
				}	
			return false;}
	}	
	

}
