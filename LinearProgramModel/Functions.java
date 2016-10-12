

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import com.selenium.keywordfw.standard.IsElementPresent;

public class Functions{
	static WebDriver driver;

	static String Recovery_Arg02 = null;
	static String Recovery_Arg01 = null;
	static String [ ] list = new String [20];
	
	public static boolean AssertText(String list[]){		
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
	

public static boolean FluentWait(String list[]){
		String fluentWaitDetails=list[2];
		String targetType=list[3];
		String targetTypeValue=list[4];
		
		String result="fail";
		String[] array = fluentWaitDetails.split(":",-1);		
		int timeOut = Integer.parseInt(array[1]);
		int pollingTime = Integer.parseInt(array[2]);
		
		if(array[0].equals("visibility"))
		{			
			switch(targetType){
			case "xpath":
				new FluentWait<WebDriver>(driver)
				.withTimeout(timeOut, TimeUnit.SECONDS)// Wait dynamically for an object to show in max of 30 sec
				.pollingEvery(pollingTime, TimeUnit.SECONDS) //poll for every 5 seconds
				.ignoring(NoSuchElementException.class)// Above line will Ignore exceptions, even if element is not found in 30 sec
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(targetTypeValue)));				
				result="pass";
				break;
			case "id":
				new FluentWait<WebDriver>(driver)
				.withTimeout(timeOut, TimeUnit.SECONDS)// Wait dynamically for an object to show in max of 30 sec
				.pollingEvery(pollingTime, TimeUnit.SECONDS) //poll for every 5 seconds
				.ignoring(NoSuchElementException.class)// Above line will Ignore exceptions, even if element is not found in 30 sec
				.until(ExpectedConditions.visibilityOfElementLocated(By.id(targetTypeValue)));				
				result="pass";
				break;
			default:
			}									
		}		
		if(array[0].equals("invisibility"))
		{
			switch(targetType){
			case "xpath":
				new FluentWait<WebDriver>(driver)
				.withTimeout(timeOut, TimeUnit.SECONDS)// Wait dynamically for an object to show in max of 30 sec
				.pollingEvery(pollingTime, TimeUnit.SECONDS) //poll for every 5 seconds
				.ignoring(NoSuchElementException.class)// Above line will Ignore exceptions, even if element is not found in 30 sec
				.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(targetTypeValue)));				
				result="pass";
				break;
			case "id":
				new FluentWait<WebDriver>(driver)
				.withTimeout(timeOut, TimeUnit.SECONDS)// Wait dynamically for an object to show in max of 30 sec
				.pollingEvery(pollingTime, TimeUnit.SECONDS) //poll for every 5 seconds
				.ignoring(NoSuchElementException.class)// Above line will Ignore exceptions, even if element is not found in 30 sec
				.until(ExpectedConditions.invisibilityOfElementLocated(By.id(targetTypeValue)));
				result="pass";
				break;
			default:
			}
		}
		if(result.equals("pass")){return true;}else{return false;}
	}
	
	private static boolean newisElementPresent(By by) {
		try {
			long end = System.currentTimeMillis() + 3000;
			boolean found = false;
		       while (System.currentTimeMillis() < end) {	        	        	
		        if(driver.findElement(by).isDisplayed()){
		        	found = true;
		        	break;        		
		           }		        	
		         else{
		          	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		            }    
		        }  	    
		       
		      return found;
		       
		       
		    } catch (NoSuchElementException e) {
		      return false;
		    	}
	 }
	
	
	public static boolean Proxy(String Browsertype, String url, String Arg03){
		Proxy myproxy = new Proxy();
		myproxy.setProxyAutoconfigUrl("http://Freeproxyserver.net/");
		DesiredCapabilities capabilities = new DesiredCapabilities();   		 
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "firefox");
		capabilities.setCapability(CapabilityType.PROXY, myproxy);
		driver = new FirefoxDriver(capabilities);   	
		return true;
	}
	
	
	public static boolean verifyAttributeValue(String Arg01, String Arg02, String Arg03){
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


	public static boolean Quit(String list[]){
		driver.quit();
	    return true;
	}
		
		
}
