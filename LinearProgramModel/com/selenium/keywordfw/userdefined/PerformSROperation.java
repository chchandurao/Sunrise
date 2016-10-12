package com.selenium.keywordfw.userdefined;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.selenium.keywordfw.standard.OpenBrowser;

public class PerformSROperation {
	public static WebDriver driver=OpenBrowser.driver;
	
	static String list[];
    
	static String PerformOperation;
    static String OperationClass;
    static String OperationName;
    static String OperationsBarPath ="//div[@id='operationsBar']/ul/li";
    
    static boolean found=false;
    
    public static boolean PerformSROperation(String list[]) throws InterruptedException{
    	PerformOperation = list[2];
    	Thread.sleep(4000);
	    List<WebElement> Operations = driver.findElements(By.xpath(OperationsBarPath));	
		int TotalOperations = Operations.size();
		
		for(int x = 1; x <TotalOperations+1; x = x+1) {
			OperationsBarPath ="//div[@id='operationsBar']/ul/li["+x+"]";			
			OperationName = driver.findElement(By.xpath(OperationsBarPath)).getText();
			OperationClass = driver.findElement(By.xpath(OperationsBarPath)).getAttribute("class");
			
			switch (OperationClass) {
				case "menu ddownarrow":
					menuddownarrow(x);
					break;
				case "menu ddownarrow multiGroupOperationsMenu":
					menuddownarrowmultiGroupOperationsMenu(x);
					break;
				case "operation autoOperations":
					operationautoOperations(x);
					break;	
				default:
					defaultOperation(x);
					break;
					}
			
			if (found) 				
				break;
			} 
		
		if (found)
			return true;
		else
			return false;
		}
    
  

	  private static void defaultOperation(int x) {		
			if (OperationClass.equals(""))
				if (OperationName.equals(PerformOperation))	{						 
					 driver.findElement(By.xpath("//div[@id='operationsBar']/ul/li["+x+"]/a")).click();
					 found = true;
				}
			}
	  
	  
	  
	  private static  void operationautoOperations(int x) throws InterruptedException {
		  //Read all operations that belong to calls Class = autoOperations
		  if (OperationClass.equals("operation autoOperations")) {
			  OperationName =driver.findElement(By.xpath("//div[@id='operationsBar']/ul/li["+x+"]")).getText();
			  if (OperationName.equals(PerformOperation)){
				  driver.findElement(By.xpath("//div[@id='operationsBar']/ul/li["+x+"]/a")).click();
				  Thread.sleep(4000);
				  found = true;
				  }
			  }
		  
		  }
	  
	  
	  
	  private static void menuddownarrowmultiGroupOperationsMenu(int x) throws InterruptedException {
		  //Read all operations that belong to calls Class = ddownarrow autoOperations multiGroupOperationsMenu
		  if (OperationClass.equals("menu ddownarrow multiGroupOperationsMenu")) {
			  driver.findElement(By.xpath("//div[@id='operationsBar']/ul/li["+x+"]/a")).click();
				List<WebElement> DdownOperations = driver.findElements(By.xpath("//div[@id='operationsBar']/ul/li["+x+"]/ul/li/a"));
				int TotalDdownOperations = DdownOperations.size();
				for(int y = 1; y <TotalDdownOperations+1; y = y+1) {

					String DdownOperationName =driver.findElement(By.xpath("//div[@id='operationsBar']/ul/li["+x+"]/ul/li["+y+"]/a")).getText();
					if (DdownOperationName.equals(PerformOperation)){
						driver.findElement(By.xpath("//div[@id='operationsBar']/ul/li["+x+"]/ul/li["+y+"]/a")).click();
						Thread.sleep(4000);
						found = true;
						}					 
					}
				}
		  
			}
	  
	  
	  
	private static void menuddownarrow(int x) throws InterruptedException {
			//Read all operations that belong to calls Class = ddownarrow		  
			if (OperationClass.equals("menu ddownarrow")) {				
				driver.findElement(By.xpath(OperationsBarPath+"/a")).click();
				Thread.sleep(4000);
				List<WebElement> MoreOperations = driver.findElements(By.xpath("//*[@id='operationsBar']/ul/li[@class='menu ddownarrow'][1]/ul/li"));
				int TotalMoreOperations = MoreOperations.size();
				
				for(int z = 1; z <TotalMoreOperations+1; z = z+1) {
					String MoreOperationName =driver.findElement(By.xpath(OperationsBarPath+"/ul/li["+z+"]/a")).getText();
					if (MoreOperationName.equals(PerformOperation)){
						driver.findElement(By.xpath(OperationsBarPath+"/ul/li["+z+"]/a")).click();
						Thread.sleep(4000);
						found = true;
						}
					}
				}
			
			}
}