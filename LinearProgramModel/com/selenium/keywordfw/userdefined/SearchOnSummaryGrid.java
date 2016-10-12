package com.selenium.keywordfw.userdefined;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.selenium.keywordfw.standard.OpenBrowser;

public class SearchOnSummaryGrid {
	public static WebDriver driver=OpenBrowser.driver;
	static String list[];
	static String columnName;
	static String columnValue;	  
	
	public static boolean SearchOnSummaryGrid(String list[]){
		boolean doSearch ;
		columnName=list[2];
		columnValue=list[3];	  
			
		// Count total no of columns on the Summary grid, and then check if the target column exist on the summary grid.
	    // If Column Name found on the summary grid, then copy column position on Grid. 
	    List<WebElement>NoOfColumns=driver.findElements(By.xpath("//div[@id='srr_container']/div/div[2]/div/table/thead/tr/th/div"));
	    for(WebElement trElement : NoOfColumns){
	    	// execute doSearch() if column exists on the grid.
	    	if (trElement.getText().equals(columnName)){
	    		doSearch = doSearch();	    
	    	}
	    }
	    
	    
		if(doSearch=true)
			return true;
		else
			return false;

		}

	private static boolean doSearch() {
	    // Since column name is displayed on the Grid. Perform search on the search panel for column name.
	    // On the search panel, select right column then enter text to search to show search results on the Grid.	    
	    driver.findElement(By.xpath("//*[@id='showSearch']")).click();	    	    	    
	    List<WebElement> columnElments = driver.findElements(By.xpath("//*[@id='srr_container']/div/div[5]/div/select/option"));
	    
	    
	    //select column on the Search panel
	    columnName = columnName +"  ";
	    int position = 1;
	    for(WebElement trElement : columnElments){
	    	if (trElement.getText().equals(columnName)){	    		
	    		driver.findElement(By.xpath("//*[@id='srr_container']/div/div[5]/div/select/option["+position+"]")).click();	    		
	    		break;
	    		} 
	    	position++;
	    	}
	    
	    // Enter Text to search on the above selected column
	    driver.findElement(By.xpath("//*[@id='srr_container']/div/div[5]/div/input[1]")).sendKeys(columnValue);
	    
	    //Click search ICON button to show search results.
	    driver.findElement(By.xpath("//*[@id='srr_container']/div/div[5]/div/input[2]")).click();
		return true;
		}
	}
