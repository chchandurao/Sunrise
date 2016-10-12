package com.selenium.keywordfw.standard;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SelectCell {
	public static WebDriver driver;
	public static boolean SelectCell(String list[]){
		 driver = OpenBrowser.driver;
		String targetType=list[2];
		String targetTypeValue=list[3];
		String targetCellName=list[4];			
		String result="fail";		
		switch(targetType){
		case "xpath":
			
			if(IsElementPresent.isElementPresent(By.xpath(targetTypeValue))){
				if(IsElementPresent.isElementPresent(By.xpath(targetTypeValue+"/th"))){
			
				try{
					String[] Cellvalue = targetCellName.split("=",-1);
				    String TargetColumName=Cellvalue[0];
				    String SourceCellValue=Cellvalue[1];
				    
				    
				    List<WebElement> col = driver.findElements(By.xpath(targetTypeValue+"/th"));
				    
					
				    List<WebElement> row = driver.findElements(By.xpath(targetTypeValue));
					
				    int y;
				    boolean ColNamefound = false;
				    boolean cellvaluefound = false;
				    System.out.println(col.size());
				    if(col.size()>=1){
				    for(y = 1; y < col.size(); y = y+1) {
				      	String SourceColumName = driver.findElement(By.xpath(targetTypeValue+"["+1+"]/th["+y+"]")).getText();   	
				      	if(TargetColumName.equals(SourceColumName)){    	
				      		ColNamefound=true;
				      		int i = row.size();
					    	for(int x = 2; x < i+1; x = x+1) {
					    		String TargetCellValue = driver.findElement(By.xpath(targetTypeValue+"["+x+"]/td["+y+"]")).getText();
					    	    if(SourceCellValue.equals(TargetCellValue)){
					    	    	cellvaluefound=true;	
					    	      	driver.findElement(By.xpath(targetTypeValue+"["+x+"]/td["+y+"]")).click();
					    	      	result = "pass";
					    	      	break;	      		
					    	      }
					    	    }
					    	 if(!cellvaluefound){
					    	    System.out.println("Cell value not found");					    	    
					    	 }
					    	 break;	      		
				      		}
				    	}	
				    }
				    else
				    {
				    	System.out.println(" No elements found in the table");
				    }
				    if(!ColNamefound){
				    System.out.println("Column Name not found");				    
				    }														    
					}catch(Exception e){
					System.out.println("			Below step, element is not currently visible and so may not be interacted with.");			
					}	
				}
				}	
			break;				
		case "id":
			System.out.println("			Logic not Implemented.");			
			break;
		default:
		}
		if(result.equals("pass")){return true;}else{return false;}	
	}
	

}
