package com.selenium.keywordfw.userdefined;
import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.selenium.keywordfw.standard.IsElementPresent;
import com.selenium.keywordfw.standard.OpenBrowser;


public class SelectService {
	public static WebDriver driver; 
	 
	public static boolean SelectService(String list[]){
		 driver = OpenBrowser.driver;
		String serviceName=list[2];
		driver = OpenBrowser.driver;
		boolean Servicefound = false;
		driver.findElement(By.id("services_top")).click();
	    boolean flag = IsElementPresent.isElementPresent(By.xpath("//div[@id='permanentLinks']/ul/li[2]/a[@class='active']"));
	   if (flag == true)
	    {              
	        List<WebElement> columnElments = driver.findElements(By.xpath("//div[@id='contextLinks']/div/ul[@class='contextLinksSliderUl ui-sortable']/li"));	        
	        int i = columnElments.size();
	        for(int x = 1; x <i; x = x+1) {
	        	String sReturnText = driver.findElement(By.xpath("//div[@id='contextLinks']/div/ul/li["+x+"]")).getText();	        	
	        	if(sReturnText.equals(serviceName)){	        			        	
	        	driver.findElement(By.xpath("//img[@alt='"+serviceName+"']")).click();
	        	Servicefound = true;	
	        	return true;
	        	}
	          }      
	        try{	        		
	        	if(driver.findElement(By.xpath("//img[@alt='More']")).isDisplayed()){
				        
				        driver.findElement(By.xpath("//*[@id='moreBlockLi']/a/img")).click();
				        driver.findElement(By.xpath("//*[@id='moreBlockLi']/a/img")).click();   
				        
				        
				        i = i+1;
				        List<WebElement> columnElments1 = driver.findElements(By.xpath("//div[@id='contextLinks']/div/ul[@class='contextLinksSliderUl ui-sortable']/li[@id ='moreBlockLi']/a/div/ul/li"));
				        i = columnElments1.size();	        		

				        
				        
				        for(int x = 1; x <i; x = x+1) {
				        	String sReturnText = driver.findElement(By.xpath("//div[@id='contextLinks']/div/ul[@class='contextLinksSliderUl ui-sortable']/li[@id ='moreBlockLi']/a/div/ul/li["+x+"]")).getText();
				        	if(sReturnText.equals(serviceName)){				        		
					        	driver.findElement(By.xpath("//img[@alt='"+serviceName+"']")).click();
					        	Servicefound = true;
					        	return true;
				        	}
				          }
			      	}
		      	}catch (NoSuchElementException e){}
	    }
	    else{
	   
	    	Servicefound = false;
	    	}
	   if(Servicefound){
		return true;	   
		}
	   else{
			return false;
		}
	}
}
