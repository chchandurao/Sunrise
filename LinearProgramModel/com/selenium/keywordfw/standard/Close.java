package com.selenium.keywordfw.standard;
import org.openqa.selenium.WebDriver;

public class Close {
	public static WebDriver driver;
	
	public static boolean Close(String list[]){
		 driver = OpenBrowser.driver;
		driver.close();
	    return true;
	}
}
