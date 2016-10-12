

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

public class Repository{
	static String Repository_Location="c://Selenium Reports//";
	static String Repository_Name="";
	static String RepositoryPath = Repository_Location+Repository_Name;
	static int tc_Size=0;
	static int ts_Size=0;
	static boolean Repository_Result=true;
	
	public static void createfolder(){
		Logger APPLICATION_LOGS = Logger.getLogger("devpinoyLogger");
		Date myDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		Repository_Name = sdf.format(myDate);
		System.out.println(Repository_Name);

		File dir = new File(Repository_Location+Repository_Name);
		dir.mkdir();
		String source = System.getProperty("user.dir")+"\\myDB.mdb";
		String target = Repository_Location+Repository_Name+"//";		
		File sourceFile = new File(source);
        String name = sourceFile.getName();
      
        File targetFile = new File(target+name);
        System.out.println("Copying file : " + sourceFile.getName() +" from Java Program");
        try {        	 
        	FileUtils.copyFile(sourceFile, targetFile);        	
			String content = "Automation Results"; 
			File file = new File(target+"//DriverScriptResult.log");
 			if (!file.exists()) {file.createNewFile();}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			} catch (IOException e) {
				APPLICATION_LOGS.error("Error: Cannot create Database in the Repository"+"\n"+e);
				Repository_Result=false;
			}
		}
	
	public static boolean CreateRepository(){
		Logger APPLICATION_LOGS = Logger.getLogger("devpinoyLogger");		
		createfolder();		
		int TS_maxRow =0;
		int TC_maxRow = 0;
		String tc_Type ="";	
		String TC_Name="";
		String KW_Name="";
		String flag = "";
		String CTestCaseName = "";
		String Arg01="";
		String Arg02="";
		String Arg03="";
		boolean result;
		
		int Roleback_Now = 0;
    	int selectcase = 0;    	
    	int DD_Startpoint=0;
    	int DD_Endpoint=0;
    	int DataDriven=0;
    	int Data_Startpoint=0;
    	int DD_TCmaxRow=0;
    	int JumpLevel = 0;
        String JumpTestSetName[] = new String[5];
        String JumpTestCaseName[] = new String[5];
        int JumpRowCount[] = new int[5];
        int Visited[] = new int[5];
        Calendar cal = Calendar.getInstance();
    	cal.getTime();

    	boolean isFirstTestset = true;
    	boolean isFirstTestcase = true;
    	boolean TestsetResult = true;
    	boolean TestcaseResult = true;
    	boolean Tag_Search=false;
    	String oldTS_Name="";
    	String oldTC_Name="";
    	
        Hashtable<String, String> table = new Hashtable<String, String>();
        Properties prop = new Properties();
   		FileInputStream ip = null;
		try {
			ip = new FileInputStream(System.getProperty("user.dir")+"\\Application.properties");
		} catch (FileNotFoundException e) {
			APPLICATION_LOGS.error("Error in Repository: Application.Properties file not found"+"\n"+e);
			return false;		
		}
   		
		try {
			prop.load(ip);
		} catch (IOException e) {
			APPLICATION_LOGS.error("Error in Repository: loading Application.Properties file"+"\n"+e);			
			return false;
		}
   			
        InputStream Testset_input = null;
		try {
			Testset_input = new BufferedInputStream(new FileInputStream("Automation.xls"));
		} catch (FileNotFoundException e) {
			APPLICATION_LOGS.error("Error in Repository: Automation.xls spread sheet not found"+"\n"+e);			
			return false;
		}
			
        POIFSFileSystem ts = null;
		try {
			ts = new POIFSFileSystem( Testset_input );
		} catch (IOException e) {
			APPLICATION_LOGS.error("Error in Repository: org.apache.poi.poifs.filesystem.POIFSFileSystem"+"\n"+e);			
			return false;
		}
            
        HSSFWorkbook tswb = null;
		try {
			tswb = new HSSFWorkbook(ts);
		} catch (IOException e) {
			APPLICATION_LOGS.error("Error in Repository: org.apache.poi.hssf.usermodel.HSSFWorkbook"+"\n"+e);
			return false;			
		}
		
		HSSFSheet TS_sheet = null;
		try{
			TS_sheet = tswb.getSheet("ControlSheet");
			TS_maxRow = TS_sheet.getLastRowNum();        // Read total no of test sets (module) in the spread sheet			
		} catch (Exception e) {			
			APPLICATION_LOGS.error("Error in Repository: Control sheet not found"+"\n"+e);
			return false;
		}
			
        HSSFSheet DD_sheet = tswb.getSheet("Datasheet");
        TS_maxRow = TS_sheet.getLastRowNum(); // Read total No of Test sets in the Control Sheet
        System.out.println("Total No of Testsets in the Control Sheet = "+TS_maxRow);
            
        for (int ts_i=1;ts_i<=TS_maxRow;ts_i++){ //For each test set in the control sheet
           	Cell TS_SNo = TS_sheet.getRow(ts_i).getCell(0);
           	Cell ts_Name = TS_sheet.getRow(ts_i).getCell(1);
           	String TS_Name = ts_Name.getStringCellValue();
            Cell Execute = TS_sheet.getRow(ts_i).getCell(2);
            String TS_Execute = Execute.getStringCellValue();
            Cell Tag = TS_sheet.getRow(ts_i).getCell(3);
            String TS_Tag = Tag.getStringCellValue();
            Tag_Search=false;
            
             // Initialize / reset all below values for every new test set execution  
             JumpTestSetName[0] = "";
             JumpTestCaseName[0] = "";
             JumpRowCount[0] = 0;
             Visited[0] = 0;
             JumpLevel = 0;                
                
             if(! isFirstTestset){
                if(! isFirstTestcase){
                   System.out.println("	Testcase "+oldTC_Name+"        Size = "+tc_Size);
                   System.out.println("	------------------------------------------------------------");
                   tc_Size=0;
                   }
                	System.out.println("Testset "+oldTS_Name);       
                	System.out.println("--------------------------------------------------------------------");
                	} 
                if ((TS_Execute).equals("Yes")){ 							// Test set is set to execute
                	
                	
                	oldTS_Name = TS_Name;
                	isFirstTestset = false;   
                	isFirstTestcase = true;
                	System.out.println("Testset "+TS_Name);
                	HSSFSheet TC_sheet = tswb.getSheet(TS_Name);
                	
                    String TS_Tag_words[] = TS_Tag.split(":");
                    
        			if(TS_Tag_words.length>=2){
        				Tag_Search=true;       				
        			}
        			
                	
                	if(TC_sheet!=null){
                		TC_maxRow = TC_sheet.getLastRowNum(); 				// Read test case spread sheet size [No of test steps]
                		String [ ] list = new String [20];
                		
                		for (int tc_i=1;tc_i<=TC_maxRow;tc_i++){ 			// For each test step in the test case spread sheet
	                    	Roleback_Now = 0;
	                    	selectcase = 0;	                    	
	                    	Iterator cells = TC_sheet.getRow(tc_i).cellIterator();	                    		                        	                        
	                        while( cells.hasNext() ) {
	                        	HSSFCell cell = (HSSFCell) cells.next();
	                        	list[cell.getColumnIndex()]=cell.getStringCellValue();	                        	
	                        }	                        
	                        TC_Name=list[0];
	                        KW_Name=list[1];
	                        Arg01=list[2];
	                        
	                        
	                    	
	                    	Cell tc_Name = TC_sheet.getRow(tc_i).getCell(0); 					// Read test case name	                        
	                    	if(tc_Name!=null){TC_Name = tc_Name.toString();}else{TC_Name="";}
	                    	Cell kw_Name = TC_sheet.getRow(tc_i).getCell(1);					// Read Keyword name
	                    	if(kw_Name!=null){KW_Name = kw_Name.toString();}else{KW_Name="";}
	                    	Cell arg01 = TC_sheet.getRow(tc_i).getCell(2);						// Read argument-1 
	                    	if(arg01!=null){Arg01 = arg01.toString();}else{Arg01="";}
	                    	
	                    	
	                        Cell arg02 = TC_sheet.getRow(tc_i).getCell(3);						// Read argument-2
	                    	if(arg02!=null){
	                    		
	                    		Arg02 = arg02.toString();
	                    		if(Arg02.length()>=3 && !Arg02.substring(0, 2).equals("//") && KW_Name.equals("Navigate") && DataDriven==0){
	                    			if(prop.getProperty(Arg02)!= null){
	                    				Arg02 = prop.getProperty(Arg02);
	                    			}
								}	                    		
	                    		if(Arg02.length()>=3 && !Arg02.substring(0, 2).equals("//") && KW_Name.equals("Navigate") && DataDriven==1){
									if(Arg02.length()==9 && Arg02.substring(0, 8).equals("Argument")){										
										Arg02=table.get(Arg02.substring(0, 9));	}
									if(Arg02.length()==10 && Arg02.substring(0, 8).equals("Argument")){										
										Arg02=table.get(Arg02.substring(0, 10));	}
	                    			if(prop.getProperty(Arg02)!= null){
	                    				Arg02 = prop.getProperty(Arg02);
	                    			}
								}	                    		
	                    		if(Arg02.length()>=3 && !Arg02.substring(0, 2).equals("//") && KW_Name.equals("isElementPresent") && DataDriven==0){
	                    			if(prop.getProperty(Arg02)!= null){
	                    				Arg02 = prop.getProperty(Arg02);
	                    			}
								}
	                    		if(Arg02.length()>=3 && !Arg02.substring(0, 2).equals("//") && KW_Name.equals("isElementPresent") && DataDriven==1){	                    																
									if(Arg02.length()==9 && Arg02.substring(0, 8).equals("Argument")){										
										Arg02=table.get(Arg02.substring(0, 9));	}
									if(Arg02.length()==10 && Arg02.substring(0, 8).equals("Argument")){										
										Arg02=table.get(Arg02.substring(0, 10));	}
	                    			if(prop.getProperty(Arg02)!= null){
	                    				Arg02 = prop.getProperty(Arg02);
	                    			}
								}
	                    	}else{
	                    		Arg02="";
	                    	}
	                    	
	                    	Cell arg03 = TC_sheet.getRow(tc_i).getCell(4);						// Read argument-2 
	                    	if(arg03!=null){
	                    		Arg03 = arg03.toString();

	                    		Arg03 = Arg03.toString();
	                    		if(Arg03.length()>=3 && !Arg03.substring(0, 2).equals("//") && KW_Name.equals("Navigate") && DataDriven==0){
	                    			if(prop.getProperty(Arg03)!= null){
	                    				Arg03 = prop.getProperty(Arg03);
	                    			}
								}	                    		
	                    		if(Arg03.length()>=3 && !Arg03.substring(0, 2).equals("//") && KW_Name.equals("Navigate") && DataDriven==1){
									if(Arg03.length()==9 && Arg03.substring(0, 8).equals("Argument")){										
										Arg03=table.get(Arg03.substring(0, 9));	}									
									if(Arg03.length()==10 && Arg03.substring(0, 8).equals("Argument")){										
										Arg03=table.get(Arg03.substring(0, 10));	}
	                    			if(prop.getProperty(Arg03)!= null){
	                    				Arg03 = prop.getProperty(Arg03);
	                    			}
								}	                    		
	                    		if(Arg03.length()>=3 && !Arg03.substring(0, 2).equals("//") && KW_Name.equals("isElementPresent") && DataDriven==0){
	                    			if(prop.getProperty(Arg03)!= null){
	                    				Arg03 = prop.getProperty(Arg03);
	                    			}
								}
	                    		if(Arg03.length()>=3 && !Arg03.substring(0, 2).equals("//") && KW_Name.equals("isElementPresent") && DataDriven==1){	                    																
									if(Arg03.length()==9 && Arg03.substring(0, 8).equals("Argument")){										
										Arg03=table.get(Arg03.substring(0, 9));	}	
									if(Arg03.length()==10 && Arg03.substring(0, 8).equals("Argument")){										
										Arg03=table.get(Arg03.substring(0, 10));	}
	                    			if(prop.getProperty(Arg03)!= null){
	                    				Arg03 = prop.getProperty(Arg02);
	                    			}
								}
	                    	
	                    	}else{Arg03="";}	
							if(!(TC_Name).equals("") && !(KW_Name).equals("") ) {
			           		 	tc_Type = "Type1";
			           		 	CTestCaseName = TC_Name;
			               	}else if(!(TC_Name).equals("") && (KW_Name).equals("") ) {
				          		tc_Type = "Type2";   
				          		CTestCaseName = TC_Name;
				           	}else if((TC_Name).equals("") && !(KW_Name).equals("") ) {
				          		 tc_Type = "Type3";                    		  
				           	}
						
							//Define the position of test step in the test case spreadsheet 
							if(tc_i==TC_maxRow){						//Is last line
								if ((Visited[JumpLevel])==0){
									flag = "LLPM";}
								else{
									flag = "LLCM";
									Roleback_Now=1;}
								}
							else{										//Is NOT - last line
								if ((Visited[JumpLevel])==0){flag = "NLLPM";}else{flag = "NLLCM";}
							}

							// Start Logic for datadriven							
							if(KW_Name.equalsIgnoreCase("StartDataDriven")){
								//System.out.println("		DataDiven - Started");
								if(DD_sheet!=null){
									DD_TCmaxRow = DD_sheet.getLastRowNum();									
									for (int i=1;i<=DD_TCmaxRow;i++){
			                			Cell dd_TCName = DD_sheet.getRow(i).getCell(0); 
			                			String DD_TCName;
										if(dd_TCName!=null)
			                			{
			                				DD_TCName = dd_TCName.toString();
			                				}else
			                				{
			                					DD_TCName="";
			                					}
			                			if(Arg01.equals(DD_TCName)){
			                				DataDriven = 1;
			                				DD_Startpoint=tc_i;
			                				Data_Startpoint=i;			                				
			                				tc_Type="DataDriven";							
											break;
		                					}		// End Of if(Arg01.equals(DD_TCName)){                			
										} // End of For
									if(DataDriven==0){
										System.out.println("				No Data found for DataDriven [0] --- FAIL");
										APPLICATION_LOGS.error("Error in Repository: No Data found for DataDriven [0]"+"\n");			
										return false;										
									}																		
								} // If(DD_Sheet != null)
								else{				//Scenario: Testcase not found in the Datasheet]
									System.out.println("				Datasheet for Datadriven not found --- FAIL");
									APPLICATION_LOGS.error("Error in Repository: Datasheet for Datadriven not found"+"\n");			
									return false;									
								} // Else - Datasheet not found					
							}	// end of if(KW_Name = "StartDataDriven")
							
							if(KW_Name.equalsIgnoreCase("Repeat")){
								DD_Endpoint=tc_i;
								tc_i=DD_Startpoint;									
								tc_Type="Repeat";
							}							

							if(DataDriven==1){								
								if(Arg01.length()>=8 && Arg01.substring(0, 8).equals("Argument")){										
									Arg01=table.get(Arg01.substring(0, 9));	}													
								if(Arg02.length()>=8 && Arg02.substring(0, 8).equals("Argument")){										
									Arg02=table.get(Arg02.substring(0, 9));	}
								if(Arg03.length()>=8 && Arg03.substring(0, 8).equals("Argument")){										
									Arg03=table.get(Arg03.substring(0, 9));	}
							}							
							
							tc_Type = tc_Type+"_"+flag;		
							//System.out.println("Type = "+tc_Type);
							switch (tc_Type){							
								case "DataDriven_NLLPM":
									if(Data_Startpoint<DD_TCmaxRow ){
										Data_Startpoint++;
										String DD_KW_Name;
										Cell dd_kw_Name = DD_sheet.getRow(Data_Startpoint).getCell(0);
										if(dd_kw_Name!=null){DD_KW_Name = dd_kw_Name.toString();}else{DD_KW_Name="";}												
										
										if(DD_KW_Name.equals("")){
											for(int i=1; i<=18; i++){
											Cell dd_Arg01 = DD_sheet.getRow(Data_Startpoint).getCell(i);
											if(dd_Arg01!=null){table.put("Argument"+i, dd_Arg01.toString());}																					
											}											
											}
										else{		//Scenario: TestcaseA is NOT last line and has No data [Create a new testcase below this testcase]
											DataDriven = 0;
											System.out.println("				No Data found for DataDriven [1] --- FAIL");
											APPLICATION_LOGS.error("Error in Repository: No Data found for DataDriven [1]"+"\n");			
											return false;											
											}
									}else{			//Scenario: Testcase is last line, and no data]
										DataDriven = 0;										
										System.out.println("				No Data found for DataDriven [2] --- FAIL");
										APPLICATION_LOGS.error("Error in Repository: No Data found for DataDriven [2]"+"\n");			
										return false;
									}
									break;
								case "Repeat_LLPM": case "Repeat_NLLPM":
									if(Data_Startpoint<DD_TCmaxRow ){
										Data_Startpoint++;
										String DD_KW_Name;
										Cell dd_kw_Name = DD_sheet.getRow(Data_Startpoint).getCell(0);
										if(dd_kw_Name!=null){DD_KW_Name = dd_kw_Name.toString();}else{DD_KW_Name="";}
										if(DD_KW_Name.equals("")){
											for(int i=1; i<=18; i++){
												Cell dd_Arg01 = DD_sheet.getRow(Data_Startpoint).getCell(i);
												if(dd_Arg01!=null){table.put("Argument"+i, dd_Arg01.toString());}																					
												}											
											}
										else{
											DataDriven = 0;
											//System.out.println("		DataDriven - Finish");
											tc_i=DD_Endpoint;
												}
											}
									else{
										DataDriven = 0;
										//System.out.println("		DataDriven - Finish");
										tc_i=DD_Endpoint;
										}
									break;	
								case "DataDriven_NLLCM":
									if(Data_Startpoint<DD_TCmaxRow ){
										Data_Startpoint++;
										String DD_KW_Name;
										Cell dd_kw_Name = DD_sheet.getRow(Data_Startpoint).getCell(0);
										if(dd_kw_Name!=null){DD_KW_Name = dd_kw_Name.toString();}else{DD_KW_Name="";}
										if(DD_KW_Name.equals("")){
											for(int i=1; i<=18; i++){
												Cell dd_Arg01 = DD_sheet.getRow(Data_Startpoint).getCell(i);
												if(dd_Arg01!=null){table.put("Argument"+i, dd_Arg01.toString());}																					
												}											
											}
										else{		
											DataDriven = 0;
											System.out.println("				No Data found for DataDriven [3] --- FAIL");
											APPLICATION_LOGS.error("Error in Repository: No Data found for DataDriven [3]"+"\n");			
											return false;											
											}
									}else{			
										DataDriven = 0;										
										System.out.println("				No Data found for DataDriven [4] --- FAIL");
										APPLICATION_LOGS.error("Error in Repository: No Data found for DataDriven [4]"+"\n");			
										return false;
									}
									break;
								case "Repeat_LLCM":
									if(Data_Startpoint<DD_TCmaxRow ){
										Data_Startpoint++;
										String DD_KW_Name;
										Cell dd_kw_Name = DD_sheet.getRow(Data_Startpoint).getCell(0);
											if(dd_kw_Name!=null){DD_KW_Name = dd_kw_Name.toString();}else{DD_KW_Name="";}
											if(DD_KW_Name.equals("")){
												for(int i=1; i<=18; i++){
													Cell dd_Arg01 = DD_sheet.getRow(Data_Startpoint).getCell(i);
													if(dd_Arg01!=null){table.put("Argument"+i, dd_Arg01.toString());}																					
													}												
												}
											else{
												DataDriven = 0;
												tc_i=DD_Endpoint+2;
												//System.out.println("		DataDriven - Finish-2A");
												}
									}else{
										DataDriven = 0;
										//System.out.println("		DataDriven - Finish-2");
										Roleback_Now = 1;										
									}
									break;
								case "Repeat_NLLCM":
									if(Data_Startpoint<DD_TCmaxRow ){
										Data_Startpoint++;
										String DD_KW_Name;
										Cell dd_kw_Name = DD_sheet.getRow(Data_Startpoint).getCell(0);
										if(dd_kw_Name!=null){DD_KW_Name = dd_kw_Name.toString();}else{DD_KW_Name="";}		
											if(DD_KW_Name.equals("")){
												for(int i=1; i<=18; i++){
													Cell dd_Arg01 = DD_sheet.getRow(Data_Startpoint).getCell(i);
													if(dd_Arg01!=null){table.put("Argument"+i, dd_Arg01.toString());}																					
													}												
											}else{
												DataDriven = 0;
												tc_i=DD_Endpoint;
												//System.out.println("		DataDriven - Finish-3A");
											}
										}else{
											DataDriven = 0;
											tc_i=DD_Endpoint;
											//System.out.println("		DataDriven - Finish-3");										
										}
									break;
								
								case "Type1_LLPM":  case "Type1_NLLPM":

									
					            	if(! isFirstTestcase){
					            		System.out.println("	Testcase "+oldTC_Name+"        Size = "+tc_Size);
					            		tc_Size=0;
					            		tc_Size=0;
					            		}  											
										System.out.println("	Testcase "+TC_Name);
										oldTC_Name=TC_Name;
										isFirstTestcase=false;
										break;				                
					            case "Type2_LLPM":  case "Type2_NLLPM":
									if(Tag_Search=true){
										
					                    String TC_Tag_words[] = Arg01.split(":");
					                    if(TC_Tag_words.length>=2){
					                    	System.out.println("Do the tagsearch logic here=");	 
					                    	/* if pass then execute the testcase
					                    	 * else skip this testcase
					                    	 */
					        			}else{
					        				System.out.println("Write logic to skip this testcase");
					        				selectcase=1;
					        				KW_Name="Skiptestcase";
					        				break;
					        						
					        			}
										
									}
					            	if(! isFirstTestcase){
					                	System.out.println("	Testcase "+oldTC_Name+"        Size = "+tc_Size);
					                	System.out.println("	------------------------------------------------------------");
					                	tc_Size=0;
					                	}					            	
					            	System.out.println("	Testcase "+TC_Name);					              	
					            	oldTC_Name=TC_Name;
					            	isFirstTestcase=false;					            	
					            	CTestCaseName = TC_Name;				        
					                break;				                
					            case "Type3_NLLPM":  case "Type3_LLPM":
					            	switch(KW_Name){
					            		case "Jump": case "GoTo": case "Exit":
					            			selectcase = 1;
					            			break;
					            		default:									
					            			result = compile(KW_Name, Arg01, Arg02, Arg03);					            			
					            		}
					            	break;
 	
					            case "Type1_LLCM":  case "Type1_NLLCM":
					            	Roleback_Now = 1;
					            	break;													
								case "Type2_LLCM": case"Type2_NLLCM":
									Roleback_Now = 1;
									break;													
								case "Type3_LLCM":
									switch(KW_Name){
										case "Jump": case "GoTo": case "Exit":
											selectcase = 1;
											break;
										default:
											result = compile(KW_Name, Arg01, Arg02, Arg03);											
										}
									break;
					            case "Type3_NLLCM":
					            	switch(KW_Name){
					            		case "Jump": case "GoTo": case "Exit":
					            			selectcase = 1;
											break;
										default:
											result = compile(KW_Name, Arg01, Arg02, Arg03);											
						            	}
									break;				                
							} // End of Switch tc_Type		
							
						if(selectcase==1){
							switch(KW_Name){
								case "Skiptestcase":
									for ( tc_i=tc_i+1;tc_i<=TC_maxRow;tc_i++){										
				                    	tc_Name = TC_sheet.getRow(tc_i).getCell(0);
				                    	if(tc_Name!=null){
				                    		TC_Name = tc_Name.toString();break;
				                    		}
				                    	else{
				                    		TC_Name="";
				                    		}
									}
									break;
																
									
								case "Jump":
					            	JumpLevel++;
					            	JumpTestSetName[JumpLevel]=TS_Name;
					            	JumpTestCaseName[JumpLevel]=CTestCaseName;
					            	JumpRowCount[JumpLevel]=tc_i;
					            	Visited[JumpLevel]=1;
					            	TS_Name=Arg01;
					            	CTestCaseName=Arg01;
					            	try{
					            	TC_sheet = tswb.getSheet(Arg01);
					            	TC_maxRow = TC_sheet.getLastRowNum();
					            	}
					            	catch(Exception e){
					            		APPLICATION_LOGS.error("Error in Repository: Spreadsheet not found - Jump"+"\n");			
										return false;
					            	}
					            	
				                    String TC_Found = "NotFound";
				                    for (tc_i=1;tc_i<=TC_maxRow;tc_i++){
				                    	tc_Name = TC_sheet.getRow(tc_i).getCell(0);
				                    	if(tc_Name!=null){TC_Name = tc_Name.toString();}else{TC_Name="";}
				                    	if(TC_Name.equals(Arg02)){
				                    		TC_Found = "Found";				                    		
				                    		kw_Name = TC_sheet.getRow(tc_i).getCell(1);
					                    	if(kw_Name!=null){KW_Name = kw_Name.toString();}else{KW_Name="";}
					                    	if(!KW_Name.equals("")){
				                    		//This command will not compile when called using JUMP keyword.
					                    	}
					                    	if(tc_i==TC_maxRow){
					                    		Roleback_Now=1;
					                    	}			                    		
					                    	break;			                    		
				                    	}			                    	
				                    }
				                    if(TC_Found.equals("NotFound")){
				                    	APPLICATION_LOGS.error("Error in Repository: Testcase not found"+"\n");			
										return false;
				                    }
			                    break;
			            	case "GoTo":
			            	}							
						}
						
						if (Roleback_Now==1){
							TS_Name=JumpTestSetName[JumpLevel];
							CTestCaseName = JumpTestCaseName[JumpLevel];
							TC_sheet = tswb.getSheet(TS_Name);
		                    TC_maxRow = TC_sheet.getLastRowNum(); // Read Testcase rowsize 
							
		                    tc_i=JumpRowCount[JumpLevel];
		            		Visited[JumpLevel]=0;
		            		JumpTestSetName[JumpLevel]="null";
		            		JumpTestCaseName[JumpLevel]="null";
		            		JumpRowCount[JumpLevel]=0;
		            		JumpLevel--;			                    
							}
                		} 	// end Of For Loop (Testcase)
                	} 		// end of if(TC_sheet!=null)
                else{
                    System.out.println("		Testcase spread sheet not found");
                    APPLICATION_LOGS.error("Error in Repository: Testcase Spreadsheet not found"+"\n");
        			return false;
                }
                if(! isFirstTestset){
                   	if(! isFirstTestcase){
                    	System.out.println("	Testcase "+oldTC_Name+"        Size = "+tc_Size);
                       	System.out.println("	------------------------------------------------------------");
                      	}
                   		System.out.println("Testset "+oldTS_Name+"        Size = "+ts_Size);       
                    	System.out.println("********************************************************************");
                    }
                
                } 		// end of If condition - if((TS_Execute).equals("Yes"))
                ts_Size=0;
                tc_Size=0;
                isFirstTestset = true;
            	isFirstTestcase = true;
            	TestsetResult=true;
            } 			// end of For loop (Testset)
		return Repository_Result;
	}
	public static boolean compile(String KW_Name, String Arg01, String Arg02, String Arg03){
		
    	System.out.println("			"+KW_Name+" -- "+Arg01+" -- "+Arg02+"--"+Arg03);
    	tc_Size++;
    	ts_Size++;
    	return true;
        
	} // end of public static boolean compile	 
}