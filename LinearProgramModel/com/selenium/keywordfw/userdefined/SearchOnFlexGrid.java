package com.selenium.keywordfw.userdefined;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.selenium.keywordfw.standard.OpenBrowser;

public class SearchOnFlexGrid {
	public static WebDriver driver=OpenBrowser.driver;
	static String list[];
	static String columnName;
	static String columnValue;	
	
	public static boolean SearchOnFlexGrid(String list[]){
	columnName=list[2];
	columnValue=list[3];
	boolean status = false;


	List<WebElement>Columns=driver.findElements(By.xpath("//*[@id='fb106009545_ctr']/div[1]/table/tbody/tr/th"));
	// Get column Id to search cell value on the Grid  
	int colId = checkColumnName(Columns);
	
	// Column name not equal to -1 (Column displayed on the Grid)
	if(colId != -1)
	{
		status=true;
		// Check cell value on colId on the Grid
		if(!checkColumnValue(colId))	
			status=false;
		}
	return status;
	}
	

	private static boolean checkColumnValue(int colId) {
		boolean found = false;		
		do {
			//Check no of records displayed on the Flexi Grid
			List<WebElement>NoOfRecords=driver.findElements(By.xpath("//*[@id='fb106009545_ctr']/div[1]/table/tbody/tr"));
			for (int i = 2; i <= NoOfRecords.size(); i++) {
				List<WebElement>Columns=driver.findElements(By.xpath("//*[@id='fb106009545_ctr']/div[1]/table/tbody/tr["+i+"]/td"));				
					WebElement iCol = Columns.get(colId);
					if(columnValue.equals(iCol.getText())){
						found = true;
						driver.findElement(By.xpath("//*[@id='fb106009545_ctr']/div[1]/table/tbody/tr["+i+"]/td")).click();
						break;
					}		
			}
			
			// Value not found on the page (check pagination control)
			if(!found){
		        if(driver.findElement(By.xpath("//*[@id='fb106009545n']")).isDisplayed()){
		        	// Navigate to next page
		        	driver.findElement(By.xpath("//*[@id='fb106009545n']")).click();
		        } else {
		        	break;
		        }
			}
		} while(!found);		
		return found;
	}	
	

	private static int checkColumnName(List<WebElement> Columns) {
		int colId = -1;
		for(int c = 0; c < Columns.size(); c++)
		{
			WebElement iCol = Columns.get(c);
			if(columnName.equals(iCol.getText()))
			{
				colId = c;
				break;				
			}
		}
		return colId;
	}
}