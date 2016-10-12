package com.selenium.keywordfw.standard;

import org.openqa.selenium.By;

public class Template {

	public static boolean Dummy(String list[]){		
		String targetType=list[2];
		String targetTypeValue=list[3];
		String targetItemName=list[4];			
		String result="fail";		
		switch(targetType){
		case "xpath":
			if(IsElementPresent.isElementPresent(By.xpath(targetTypeValue))){
				try{
														    
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

}
