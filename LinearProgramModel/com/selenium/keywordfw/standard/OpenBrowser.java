package com.selenium.keywordfw.standard;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public class OpenBrowser {
	public static WebDriver driver;
	public static boolean OpenBrowser(String list[]) throws IOException{
		String ChromePath = "C:\\My Work Space\\Automation\\LinearProgramModel\\chromedriver.exe";
		String IEDriverServer= "C:\\My Work Space\\Automation\\LinearProgramModel\\IEDriverServer.exe";
		String browserType = list[2];
		String url = list[3];
		String profileName = list[4];

		switch(browserType){		
		case "Firefox":
			//if(profileName.equals("null")){
			if(profileName != null){
				ProfilesIni prof = new ProfilesIni();
				FirefoxProfile p = prof.getProfile(profileName);
				driver = new FirefoxDriver(p);
				driver.manage().window().maximize();
			    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			    driver.get(url);
			    
			}else{

				driver = new FirefoxDriver();		
			    driver.manage().window().maximize();
			    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			    driver.get(url);
				
			}
		    break;
		case "Chrome":
			System.setProperty("webdriver.chrome.driver", ChromePath);
			driver = new ChromeDriver();		
		    driver.manage().window().maximize();
		    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		    driver.get(url);
		    break;
		case "IE":
			System.setProperty("webdriver.ie.driver", IEDriverServer);
			driver = new InternetExplorerDriver();		
		    driver.manage().window().maximize();
		    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		    driver.get(url);
		    break;
		default:
			driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38);			
			driver.get(url);
		}
		return true;
	}

}
