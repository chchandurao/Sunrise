package com.selenium.keywordfw.temp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.selenium.keywordfw.standard.IsElementPresent;


public class GridSelect {
	static WebDriver driver;
	static String columnName="Department Name";
	static String columnValue="IT";
    
	// static String columnName="Incident ID";
	// static String columnValue="INC000086";

	
	public static void main(String[] args) throws InterruptedException {
	    int colposition=0; 
	    int columnNamePosition=0;

	   // Open Firefox browser then login to application
		driver = new FirefoxDriver();
		driver.get("http://172.120.1.15:8080/Sostenuto/SUsers/");

	    driver.findElement(By.id("uname")).clear();
	    driver.findElement(By.id("uname")).sendKeys("Administrator");
	    driver.findElement(By.id("pword")).clear();
	    driver.findElement(By.id("pword")).sendKeys("sunrise");
	    driver.findElement(By.id("submit")).click();
	    
	    
	    // Click on Services_Top button to show all list of services.
	    driver.findElement(By.xpath("//*[@id='services_top']")).click();
		int wt = 60;
		WebDriverWait wait = new WebDriverWait(driver, wt);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		if(IsElementPresent.isElementPresent(By.xpath("//*[@id='101001108_a']/img"))){
			driver.findElement(By.xpath("//*[@id='101001108_a']/img")).click();	
		}		
		
		
		// Count total no of Columns on the Summary grid, then check if target column exist on the summary grid
	    // If Column Name found on the summary grid, then copy position on Grid. 
	    List<WebElement>NoOfColumns=driver.findElements(By.xpath("//div[@id='srr_container']/div/div[2]/div/table/thead/tr/th/div"));
	    for(WebElement trElement : NoOfColumns){
	    	if (trElement.getText().equals(columnName)){	    		
	    		columnNamePosition=colposition;
	    		break;
	    		}
	    	colposition++;
	    }
	
	
	    // Since column name is displayed on the Grid. Perform search on the search panel.
	    // On the search panel, select right column then enter text to search to show search results on the Grid.
	    if(IsElementPresent.isElementPresent(By.xpath("//*[@id='showSearch']"))){
	    	driver.findElement(By.xpath("//*[@id='showSearch']")).click();
	    }
	    
	    if(IsElementPresent.isElementPresent(By.xpath("//*[@id='srr_container']/div/div[5]/div/select"))){
	    	driver.findElement(By.xpath("//*[@id='srr_container']/div/div[5]/div/select")).click();	
	    }
	    
	    List<WebElement> columnElments = driver.findElements(By.xpath("//*[@id='srr_container']/div/div[5]/div/select/option"));
	    int position = 1;
	    columnName = columnName +"  ";
	    for(WebElement trElement : columnElments){
	    	if (trElement.getText().equals(columnName)){
	    	    if(IsElementPresent.isElementPresent(By.xpath(""))){
	    	    	driver.findElement(By.xpath("//*[@id='srr_container']/div/div[5]/div/select/option["+position+"]")).click();
		    		break;
		    		}
	    	    }
	    	position++; 
	    	}
	

	    
	    // Enter Text / Value to search on the above selected column then click SEARCH Button
	    driver.findElement(By.xpath("//*[@id='srr_container']/div/div[5]/div/input[1]")).sendKeys(columnValue);
		driver.findElement(By.xpath("//*[@id='srr_container']/div/div[5]/div/input[2]")).click();  
			    
	      
	    // Find total no of records displayed on the Grid		    
	    List<WebElement>totalNoOfRecords=driver.findElements(By.xpath("id('srr_container_grid')/tbody/tr"));
	    System.out.println("NUMBER OF Records IN THIS TABLE = "+totalNoOfRecords.size());	
	    int record = 1;
	    
	    //For each record on the Grid
	    for(WebElement iRow : totalNoOfRecords){	
	    	List<WebElement> totalNoOfColumns=iRow.findElements(By.xpath("td"));
	    	position=0;
	    	for(WebElement iCol : totalNoOfColumns)
	    	{	    		
	    		if (position==columnNamePosition){
	    			if (iCol.getText().equals(columnValue)){
	    				System.out.println(record);
	    				driver.findElement(By.xpath("//*[@id='srr_container_grid']/tbody/tr["+record+"]/td[1]/div/input")).click();
	    				break;
	    				}
	    			}
	    		position++;
	    		}
	    	record++;
	    	}
	    }
	
	private static boolean isElementPresent(By by) {
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
	}