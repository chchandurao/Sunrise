import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import com.selenium.keywordfw.temp.DriverVariables;

//This program will always read cell values in the spread sheet you specify. 
public class DriverScript {
	//private static int StartDataDrive;

	static String Recovery_Arg02 = null;
	static String Recovery_Arg01 = null;
	static String list []= new String [20];
	
	static String ts_Type ="";		// 	Teststep type
	static String TC_Name=""; 		//	Testcase name
	static String TS_Name=""; 		// 	Testset name
	static String oldTS_Name=""; 	// 	OldTestset name
	static String oldTC_Name=""; 	//	Old Testcase name	
	
	static int TC_maxRow = 0;		
	static int Roleback_Now = 0;
	static int selectcase = 0;    	
	static int DD_Startpoint=0;
	static int DD_Endpoint=0;
	static int DataDriven=0;
	static int Data_Startpoint=0;
	static int DD_TCmaxRow=0;
	static int JumpLevel = 0;
	static int JumpRowCount[] = new int[5];
	static int Visited[] = new int[5];    
    
	static InputStream Testset_input = null;
	static POIFSFileSystem ts;
	static HSSFWorkbook tswb = null;
	static HSSFSheet TS_sheet = null;
	static HSSFSheet DD_sheet = null;
	static Logger APPLICATION_LOGS=null; 
	static int TS_maxRow =0;
	
	static String KW_Name="";
	static String flag = "";
	static String CTestCaseName = "";
	
	static String Arg01="";
	static String Arg02="";
	static String Arg03="";
	
	static String fileName="";
	static String JumpTestSetName[] = new String[5];
	static String JumpTestCaseName[] = new String[5];
    
	static boolean result;
	static boolean isFirstTestset = true;
	static boolean isFirstTestcase = true;
	static boolean TestsetResult = true;
	static boolean TestcaseResult = true;
	
	static Calendar cal;
	static Properties prop=new Properties();
	static Hashtable<String, String> table = new Hashtable<String, String>();;
	
	public static void main( String [] args ){
		Logger APPLICATION_LOGS = Logger.getLogger("devpinoyLogger");
        FileInputStream ip = null;
        try {
        	ip = new FileInputStream(System.getProperty("user.dir")+"\\LinearProgramModel\\com\\selenium\\keywordfw\\settings\\Application.properties");
			prop.load(ip);
			}catch (IOException e) {
				e.printStackTrace();
				return;
				}
        
        // Directory path here
   		String path = (System.getProperty("user.dir")+"\\LinearProgramModel\\com\\selenium\\keywordfw\\spreadsheets");
   		String files;
   		File folder = new File(path);
   		File[] listOfFiles = folder.listFiles();  
  		for (int f = 0; f < listOfFiles.length; f++){
  			if (listOfFiles[f].isFile())
  			{
  				files = listOfFiles[f].getName();
  				if (files.endsWith(".xls") || files.endsWith(".XLS"))
  					fileName=files;
  				} 
  			
  				writeResults("***** Spread sheet Name = " +fileName+" *****" );  				
  				TS_maxRow = dovalidation(fileName);
  				writeResults("Total No of Testsets in the Control Sheet = "+TS_maxRow);
            
  				execute();
  				}
  		}	// End of static void man(String[] args)
	
	
	
	private static void execute(){
        for (int ts_i=1;ts_i<=TS_maxRow;ts_i++){ //For each test set in the control sheet            	
        	Cell TS_SNo = TS_sheet.getRow(ts_i).getCell(0);
        	Cell ts_Name = TS_sheet.getRow(ts_i).getCell(1);
        	TS_Name = ts_Name.getStringCellValue();

            Cell Execute = TS_sheet.getRow(ts_i).getCell(2);
            String TS_Execute = Execute.getStringCellValue();

			// Initialize Or Reset all below values before executing new testset.
            JumpTestSetName[0] = "";
            JumpTestCaseName[0] = "";
            JumpRowCount[0] = 0;
            Visited[0] = 0;
            JumpLevel = 0; 
            
			// Below logic is to print execution results
            // Below logic will not execute only system executing 1st test set.
            if(! isFirstTestset){
            	if(! isFirstTestcase){	                	
                	writeResults("			Finish Time: 	Result = "+TestcaseResult);
                	}                	
            	writeResults("			Finish Time: 	Result = "+TestcaseResult);                	
            	}    
			
            if ((TS_Execute).equals("Yes")){ 							// Test set is set to execute
            	oldTS_Name = TS_Name;
            	isFirstTestset = false;   
            	isFirstTestcase = true;
            	
            	writeResults("Testset "+TS_Name+"	Start Time: ");                			
            	HSSFSheet TC_sheet = tswb.getSheet(TS_Name);
            	if(TC_sheet!=null){
            		TC_maxRow = TC_sheet.getLastRowNum(); 				// Read test case spread sheet size [No of test steps]
            		for (int tc_i=1;tc_i<=TC_maxRow;tc_i++){ 			// For each test step in the test case spread sheet
            			Roleback_Now = 0;
                    	selectcase = 0; 		
                    	Iterator cells = TC_sheet.getRow(tc_i).cellIterator();
                    	for(int i=0; i<=18; i++){
                        	list[i]="";
                        		}
                    	while( cells.hasNext() ) {
                        	HSSFCell cell = (HSSFCell) cells.next();
                        	cell.setCellType(Cell.CELL_TYPE_STRING); // This line will convert Integer values into String values (Example: my password is 1234 with no characters)	                        	
                        	list[cell.getColumnIndex()]=cell.getStringCellValue();	                        	
                        }
                    	
                        TC_Name=list[0];
                        KW_Name=list[1];
                        Arg01=list[2];
                        
                        for(int i=1; i<=18; i++){
                        	if(list[i]!=null){		                    		
	                    		if(list[i].length()>=3 && !list[i].substring(0, 2).equals("//") && DataDriven==0){
	                    			if(prop.getProperty(list[i])!= null){
	                    				list[i] = prop.getProperty(list[i]);
	                    			}
								}	                    		
	                    		if(list[i].length()>=3 && !list[i].substring(0, 2).equals("//") && DataDriven==1){
									if(list[i].length()==9 && list[i].substring(0, 8).equals("Argument")){										
										list[i]=table.get(list[i].substring(0, 9));	}
									if(list[i].length()==10 && list[i].substring(0, 8).equals("Argument")){										
										list[i]=table.get(list[i].substring(0, 10));	}
	                    			if(prop.getProperty(list[i])!= null){
	                    				list[i] = prop.getProperty(list[i]);
	                    			}
								}	 	
                        		}
                        }
                        Cell tc_Name ;	                    	
                    	Cell kw_Name ;	                    	

            // Group Test STEP into one of the following types 
			// Type1, Type2 and Type3 help us to identify type of test step.
						if((list[0]!="") && (list[1]!="") )
						{
							ts_Type = "Type1";
							CTestCaseName = list[0];
							}
						else if((list[0]!="") && (list[1]=="") )
						{ 
							ts_Type = "Type2";
							CTestCaseName = list[0];
							}
						else if((list[0]=="") && (list[1]!="") )
							ts_Type = "Type3";
							
				  
			//	Check if the control is on the last TEST STEP then
			//	If control is on LAST LINE, then check if it is in Parent / Child Module
			//	Is NOT LAST LINE - then check if this is in Parent / Child module
						if(tc_i==TC_maxRow)		
							if ((Visited[JumpLevel])==0)								
								flag = "LLPM"; // Last line in parent module									
							else{
								flag = "LLCM"; // last line in child module
								Roleback_Now=1;
								}
						else										
			
							if ((Visited[JumpLevel])==0)
								flag = "NLLPM"; // Not last line in parent module	
							else
								flag = "NLLCM"; // Not last line  in child module
								
								
						
			//	 If Keyword in the test step is 'STARTDATADRIVEN' then
			// 	find data location in the data sheet for data driven							
			//	 If no data found for data driven then find next test case.
						
						if(list[1].equals("StartDataDriven"))
						{
							writeResults("		DataDiven - Started");	
						DD_TCmaxRow = DD_sheet.getLastRowNum();
						for (int i=1;i<=DD_TCmaxRow;i++){
							Cell dd_TCName = DD_sheet.getRow(i).getCell(0);
							String DD_TCName;
							
							if(dd_TCName!=null)
								DD_TCName = dd_TCName.toString();
							else
								DD_TCName="";
							
							if(Arg01.equals(DD_TCName)){
								DataDriven = 1;
								DD_Startpoint=tc_i;
								Data_Startpoint=i;
								ts_Type="DataDriven";
								break;
								}	
							
						} // End of For
						
						// When no data found for data driven, then find next test case location 
						if(DataDriven==0){
							writeResults("				No Data found for DataDriven [0] --- FAIL");
							for(int i=tc_i+1; i<=TC_maxRow; i++){
								tc_Name = TC_sheet.getRow(i).getCell(0);
								if(tc_Name!=null){
									tc_i=i-1;
									break;
									}
								else{
									tc_i=i; 
									ts_Type="donothing";
									}
								}
							}
						}	// end of if(list[1].equals("StartDataDriven"))
						
						
				// If Keyword  in Repeat
				// 							
				// 	
						if(list[1].equalsIgnoreCase("Repeat")){
							DD_Endpoint=tc_i;
							tc_i=DD_Startpoint;									
							ts_Type="Repeat";
						}
						
						// Since Keyword, Object Identification and Method in teststep can also be perametarised.
						// When Datadriven is ON, check 2nd, 3rd and 4th columns if these columns are using datadriven arguments (Argument01,Argument02...ect)
						// If so get Datadriven column number  into variables Arg01, Arg02, Arg03
						
						if(DataDriven==1){								
							if(Arg01.length()>=8 && Arg01.substring(0, 8).equals("Argument")){										
								Arg01=table.get(Arg01.substring(0, 9));	
								}													
							if(Arg02.length()>=8 && Arg02.substring(0, 8).equals("Argument")){										
								Arg02=table.get(Arg02.substring(0, 9));	
								}
							if(Arg03.length()>=8 && Arg03.substring(0, 8).equals("Argument")){										
								Arg03=table.get(Arg03.substring(0, 9));	
								}
						}													
						
						
						ts_Type = ts_Type+"_"+flag;		
						//System.out.println("Type = "+ts_Type);
						switch (ts_Type){								
							case "DataDriven_NLLPM":
								if(Data_Startpoint < DD_TCmaxRow ){
									Data_Startpoint++;
									String DD_KW_Name;
									Cell dd_kw_Name = DD_sheet.getRow(Data_Startpoint).getCell(0);
									
									if(dd_kw_Name!=null)
										DD_KW_Name = dd_kw_Name.toString();											
									else
										DD_KW_Name="";
										
									if(DD_KW_Name.equals("")){
										for(int i=1; i<=18; i++){
											Cell dd_Arg01 = DD_sheet.getRow(Data_Startpoint).getCell(i);
											if(dd_Arg01!=null){
												table.put("Argument"+i, dd_Arg01.toString());
												}
											}
										}
									else{		//Scenario: TestcaseA is NOT last line and has No data [Create a new testcase below this testcase]
										DataDriven = 0;
										writeResults("				No Data found for DataDriven [1] --- FAIL");
										for(int i=tc_i+1; i<=TC_maxRow; i++){
			            					tc_Name = TC_sheet.getRow(i).getCell(0);
			    	                    	if(tc_Name!=null){tc_i=i-1; break;}	else{	
		    	                    			tc_i=i; // No more test cases found, Set focus to next test set
		    	                    			ts_Type="donothing";
			    	                    		}				            					
			            					}
										}
								}else{			//Scenario: Testcase is last line, and no data]
									DataDriven = 0;										
									writeResults("				No Data found for DataDriven [2] --- FAIL");
									for(int i=tc_i+1; i<=TC_maxRow; i++){
		            					tc_Name = TC_sheet.getRow(i).getCell(0);
		    	                    	if(tc_Name!=null){tc_i=i-1; break;}	else{	
	    	                    			tc_i=i; // No more test cases found, Set focus to next test set
	    	                    			ts_Type="donothing";
		    	                    		}				            					
		            					}
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
										writeResults("		DataDriven - Finish");
										tc_i=DD_Endpoint;
										//System.out.println("No Data found / No more data found for datadriven - End of Datadriven");
											}
										}
								else{
									DataDriven = 0;
									writeResults("		DataDriven - Finish");
									tc_i=DD_Endpoint;
									}
								break;
							
								
							case "DataDriven_NLLCM":
								if(Data_Startpoint < DD_TCmaxRow ){
									Data_Startpoint++;
									String DD_KW_Name;
									
									Cell dd_kw_Name = DD_sheet.getRow(Data_Startpoint).getCell(0);
									if(dd_kw_Name!=null)
										DD_KW_Name = dd_kw_Name.toString();
									else
										DD_KW_Name="";
											
									if(DD_KW_Name.equals("")){
										for(int i=1; i<=18; i++){
											Cell dd_Arg01 = DD_sheet.getRow(Data_Startpoint).getCell(i);
											if(dd_Arg01!=null){
												table.put("Argument"+i, dd_Arg01.toString());
												}																					
											}
										}
									else{		//Scenario: TestcaseA is NOT last line and has No data [Create a new testcase below this testcase]
										DataDriven = 0;
										writeResults("				No Data found for DataDriven [1] --- FAIL");
										for(int i=tc_i+1; i<=TC_maxRow; i++){
											tc_Name = TC_sheet.getRow(i).getCell(0);
											if(tc_Name!=null){
												tc_i=i-1; 
			    	                    		break;
			    	                    		}
											else{	
		    	                    			tc_i=i; // No more test cases found, Set focus to next test set
		    	                    			ts_Type="donothing";
			    	                    		}				            					
			            					}
										}									
									}
								else{
									//Scenario: Testcase is last line, and no data]
									DataDriven = 0;
									writeResults("				No Data found for DataDriven [2] --- FAIL");
									for(int i=tc_i+1; i<=TC_maxRow; i++){
										tc_Name = TC_sheet.getRow(i).getCell(0);
										if(tc_Name!=null){
											tc_i=i-1;
											break;
											}
										else{
											tc_i=i; // No more test cases found, Set focus to next test set
	    	                    			ts_Type="donothing";
	    	                    			}
										}
									}
								break;

							case "Repeat_LLCM":
								if(Data_Startpoint<DD_TCmaxRow ){
									Data_Startpoint++;
									String DD_KW_Name;
									
									Cell dd_kw_Name = DD_sheet.getRow(Data_Startpoint).getCell(0);
									if(dd_kw_Name!=null)
										DD_KW_Name = dd_kw_Name.toString();
									else
										DD_KW_Name="";
									
									if(DD_KW_Name.equals("")){
										for(int i=1; i<=18; i++){
											Cell dd_Arg01 = DD_sheet.getRow(Data_Startpoint).getCell(i);
											if(dd_Arg01!=null)
												table.put("Argument"+i, dd_Arg01.toString());
											}
										}
									else{
										DataDriven = 0;
										tc_i=DD_Endpoint+2;
										writeResults("		DataDriven - Finish-2A");
										}
									}
								else{
									DataDriven = 0;
									Roleback_Now = 1;
									writeResults("		DataDriven - Finish-2");
																			
								}
								break;
								
							case "Repeat_NLLCM":
								if(Data_Startpoint<DD_TCmaxRow ){
									Data_Startpoint++;
									String DD_KW_Name;
									Cell dd_kw_Name = DD_sheet.getRow(Data_Startpoint).getCell(0);
									if(dd_kw_Name!=null){
										DD_KW_Name = dd_kw_Name.toString();
										}
									else{
										DD_KW_Name="";
										}	
									
									if(DD_KW_Name.equals("")){
										for(int i=1; i<=18; i++){
											Cell dd_Arg01 = DD_sheet.getRow(Data_Startpoint).getCell(i);
											if(dd_Arg01!=null){
												table.put("Argument"+i, dd_Arg01.toString());
												}
											}
										}else{
											DataDriven = 0;
											tc_i=DD_Endpoint;
											writeResults("		DataDriven - Finish-3A");
										}
									}
								else{
										DataDriven = 0;
										tc_i=DD_Endpoint;
										writeResults("		DataDriven - Finish-3");
										}
								break;
								
								
							//Parent - Test case spreadsheet scenarios  
								
							case "Type1_LLPM":  case "Type1_NLLPM":
								if(! isFirstTestcase)
									writeResults("			Finish Time: 	Result = "+TestcaseResult);
								writeResults("	Testcase "+TC_Name+"	Start Time: ");
								oldTC_Name=TC_Name;
								isFirstTestcase=false;
								if(!TestcaseResult){
									TestcaseResult = true;
									String words[] = list[1].split(":");
									if(words.length==3){
										list[1]=words[0];
										Recovery_Arg01=words[1];
										Recovery_Arg02=words[2];
										// DriverVariables.Recovery_Arg02=words[2];
										}
									selectcase=1;
									}
								break;	
										
				            case "Type2_LLPM":  case "Type2_NLLPM":	
				            	if(! isFirstTestcase){
				                	writeResults("			Finish Time: 	Result = "+TestcaseResult);
				                	TestcaseResult=true;
				                	}  
				            	writeResults("	Testcase "+TC_Name+"	Start Time: ");
				            	oldTC_Name=TC_Name;
				            	isFirstTestcase=false;
				            	CTestCaseName = TC_Name;					            	
				                break;		
				                
				                
				            case "Type3_NLLPM":  case "Type3_LLPM":
				            	switch(list[1]){
				            		case "Jump": case "GoTo": case "Exit":
				            			selectcase = 1;
				            			break;
				            		default:									
				            			result = compile(list[1], list);
				            			if(result!=true){
				            				TestcaseResult=false;
				            				TestsetResult=false;
				            				DataDriven=0;
				            				for(int i=tc_i+1; i<=TC_maxRow; i++){
				            					tc_Name = TC_sheet.getRow(i).getCell(0);
				            					if(tc_Name.getStringCellValue()!=""){
				    	                    		tc_i=i-1; 
				    	                    		break;
				    	                    	}else{
				    	                    		tc_i=i-1;
				    	                      }			
				            				}					            															
										}
				            		}
				            	break;
							//Child - Test case spreadsheet scenarios 	
				            case "Type1_LLCM":  case "Type1_NLLCM":
				            	Roleback_Now = 1;
				            	break;													
							case "Type2_LLCM": case"Type2_NLLCM":
								Roleback_Now = 1;
								break;													
							case "Type3_LLCM":
								switch(list[1]){
									case "Jump": case "GoTo": case "Exit":
										selectcase = 1;
										break;
									default:
										result = compile(list[1], list);
										if(result!=true){
											TestcaseResult=false;
											TestsetResult=false;
											DataDriven=0;
							            	TS_Name=JumpTestSetName[1];
							            	CTestCaseName=JumpTestCaseName[1];
							            	tc_i=JumpRowCount[1];
							            	TC_sheet = tswb.getSheet(TS_Name);
							            	TC_maxRow = TC_sheet.getLastRowNum(); 				
							            	for(int i=tc_i+1; i<=TC_maxRow; i++){
				            					tc_Name = TC_sheet.getRow(i).getCell(0);
				            					if(tc_Name.getStringCellValue()!=""){
				    	                    		tc_i=i-1; 
				    	                    		break;
				    	                    	}else{
				    	                    		tc_i=i;
				    	                      }								            					
				            				}		
											for(int jl=1; jl<=JumpLevel; jl++){
												JumpTestSetName[jl]="";
								            	JumpTestCaseName[jl]="";
								            	JumpRowCount[jl]=0;
								            	Visited[jl]=0;
											}
											JumpLevel=0;										
										}else{
										Roleback_Now = 1;
										}
									}
								break;
				            case "Type3_NLLCM":
				            	switch(list[1]){
				            		case "Jump": case "GoTo": case "Exit":
				            			selectcase = 1;
										break;
									default:
										result = compile(list[1], list);
										if(result!=true){
											TestcaseResult=false;
											TestsetResult=false;
											DataDriven=0;
							            	TS_Name=JumpTestSetName[1];
							            	CTestCaseName=JumpTestCaseName[1];
							            	tc_i=JumpRowCount[1];
							            	TC_sheet = tswb.getSheet(TS_Name);
							            	TC_maxRow = TC_sheet.getLastRowNum(); 				
							            	for(int i=tc_i+1; i<=TC_maxRow; i++){
				            					tc_Name = TC_sheet.getRow(i).getCell(0);					            					
				            					if(tc_Name.getStringCellValue()!=""){
				    	                    		tc_i=i-1; 
				    	                    		break;
				    	                    	}else{
				    	                    		tc_i=i;
				    	                      }								            					
				            				}		
											for(int jl=1; jl<=JumpLevel; jl++){
												JumpTestSetName[jl]="";
								            	JumpTestCaseName[jl]="";
								            	JumpRowCount[jl]=0;
								            	Visited[jl]=0;
											}
											JumpLevel=0;	
										}
					            	}
								break;				                
						} // End of Switch ts_Type		
						
					if(selectcase==1){
						switch(list[1]){
							case "Recovery":
								JumpLevel++;
				            	JumpTestSetName[JumpLevel]=TS_Name;
				            	JumpTestCaseName[JumpLevel]=CTestCaseName;
				            	JumpRowCount[JumpLevel]=tc_i;
				            	Visited[JumpLevel]=1;
				            	
				            	TS_Name=Recovery_Arg01;
				            	CTestCaseName=Recovery_Arg01;
				            	TC_sheet = tswb.getSheet(Recovery_Arg01);
			                    TC_maxRow = TC_sheet.getLastRowNum(); 
			                    
			                    String TC_Found = "NotFound";
			                    for (tc_i=1;tc_i<=TC_maxRow;tc_i++){
			                    	tc_Name = TC_sheet.getRow(tc_i).getCell(0);
			                    	if(tc_Name!=null){TC_Name = tc_Name.toString();}else{TC_Name="";}
			                    	if(TC_Name.equals(Recovery_Arg02)){
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
			                    	writeResults("			Recovery Scenario Testcase Not Found - FAIL");	
			                    	
			                    	TS_Name=JumpTestSetName[JumpLevel];
					            	CTestCaseName=JumpTestCaseName[JumpLevel];
					            	tc_i=JumpRowCount[JumpLevel];
					            	tc_i=tc_i+1;
					            	TC_sheet = tswb.getSheet(TS_Name);
					            	TC_maxRow = TC_sheet.getLastRowNum(); 				
					            	for(int i=tc_i; i<=TC_maxRow; i++){
		            					tc_Name = TC_sheet.getRow(i).getCell(0);
		    	                    	if(tc_Name!=null){tc_i=i-1; break;}					            					
		            				}		
									for(int jl=1; jl<=JumpLevel; jl++){
										JumpTestSetName[jl]="";
						            	JumpTestCaseName[jl]="";
						            	JumpRowCount[jl]=0;
						            	Visited[jl]=0;
									}
									JumpLevel=0;				                    			                    	
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
				            	TC_Found = "NotFound";
				            	try{
					            	TC_sheet = tswb.getSheet(Arg01);
					            	TC_maxRow = TC_sheet.getLastRowNum();						            	
				                    for (tc_i=1;tc_i<=TC_maxRow;tc_i++){
				                    	tc_Name = TC_sheet.getRow(tc_i).getCell(0);
				                    	if(tc_Name!=null){TC_Name = tc_Name.toString();}else{TC_Name="";}
				                    	if(TC_Name.equals(list[3])){
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
					            	}
					            	catch(Exception e){
					            		APPLICATION_LOGS.error("Error in Repository: Spreadsheet not found - Jump"+"\n");											
					            	}  
			                    if(TC_Found.equals("NotFound")){
			                    	writeResults("			Testcase / Testcase Spreadsheet  Not Found - FAIL");				                    	
			                    	TS_Name=JumpTestSetName[JumpLevel];
					            	CTestCaseName=JumpTestCaseName[JumpLevel];
					            	tc_i=JumpRowCount[JumpLevel];
					            	TC_sheet = tswb.getSheet(TS_Name);
					            	TC_maxRow = TC_sheet.getLastRowNum(); 				
					            	for(int i=tc_i+1; i<=TC_maxRow; i++){
		            					tc_Name = TC_sheet.getRow(i).getCell(0);
		    	                    	if(tc_Name!=null){tc_i=i-1; break;}					            					
		            				}		
									for(int jl=1; jl<=JumpLevel; jl++){
										JumpTestSetName[jl]="";
						            	JumpTestCaseName[jl]="";
						            	JumpRowCount[jl]=0;
						            	Visited[jl]=0;
									}
									JumpLevel=0;				                    			                    	
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
            	writeResults("		Testcase spread sheet not found");
            }	
                if(! isFirstTestset){
                	if(! isFirstTestcase){
                    	writeResults("			Finish Time: 	Result = "+TestcaseResult);
                    	}
                	writeResults("			Finish Time: 	Result = "+TestsetResult);
                	} 
           } 		// end of If condition - if((TS_Execute).equals("Yes"))
            isFirstTestset = true;
        	isFirstTestcase = true;
        	TestsetResult=true;
        } 			// end of For loop (Testset)
	}
	
	private static int dovalidation(String fileName) {
		try {
				Testset_input = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"\\LinearProgramModel\\com\\selenium\\keywordfw\\spreadsheets\\"+fileName));
				} catch (FileNotFoundException e) {
					APPLICATION_LOGS.error("Error:spread sheet not found"+"\n"+e);
					return 0;
					} 			
			try {
				ts = new POIFSFileSystem( Testset_input );
				} catch (IOException e) {
					APPLICATION_LOGS.error("Error: org.apache.poi.poifs.filesystem.POIFSFileSystem"+"\n"+e);
					return 0;
					} 			
			try {
				tswb = new HSSFWorkbook(ts);
				} catch (IOException e) {
					APPLICATION_LOGS.error("Error: org.apache.poi.hssf.usermodel.HSSFWorkbook"+"\n"+e);
					return 0;
					}			
			try{
				TS_sheet = tswb.getSheet("ControlSheet");
				} catch (Exception e) {
					APPLICATION_LOGS.error("Error: Control sheet not found"+"\n"+e);
					return 0;
					}				
			try{
				DD_sheet = tswb.getSheet("Datasheet");
				} catch (Exception e) {
					APPLICATION_LOGS.error("Error: Datasheet sheet not found"+"\n"+e);
					return 0;
					}
			TS_maxRow = TS_sheet.getLastRowNum(); // Read total No of Test sets in the Control Sheet;
			return TS_maxRow ;
	}




	// Below logic is used to write results into a file.
	// File location is hardcoded in the variable declaration.	
	public static void writeResults(String str){
		try {
			System.out.println(str);
			File file = new File(Repository.Repository_Location+Repository.Repository_Name+"//DriverScriptResult.log");				    
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);				
			bw.append('\n'); 
			bw.write(str);
			bw.close();
			} 
		catch (IOException e) {
			e.printStackTrace();
			}
		}
	
	
	// Below is the logic to execute a test step
	// System will find a class that matches the keyword.   
	// location for each class file is set in classnames.properties file 
	public static boolean compile(String KWName, String list[]){
		Logger APPLICATION_LOGS = Logger.getLogger("devpinoyLogger");
		boolean compileresult = false;		
		String Keyword=null;
		try {
			TreeMap<String, String> map = ReadClassName.getProperties(System.getProperty("user.dir")+"\\LinearProgramModel\\com\\selenium\\keywordfw\\settings\\ClassNames.properties");
			Keyword = map.get(KWName);
			if (Keyword == null)
				Keyword = "Functions";
			}catch (IOException e) {
				
			}
		try{
			Class classToCall = Class.forName(Keyword);
    		String[] args = {"one", "two"};
    		Method methodToExecute = classToCall.getDeclaredMethod(KWName, new Class[]{String[].class});
    		Object result = methodToExecute.invoke(classToCall.newInstance(), new Object[]{list});
    		compileresult = (boolean) result;
    		classToCall=null;
    		}catch (Exception ex){
    			//ex.printStackTrace();
    			APPLICATION_LOGS.debug(ex);
    			}
		
    	String str = "			"+list[1]+" -- "+list[2]+" -- "+list[3]+"--"+list[4]+"---"+compileresult;    	
    	writeResults(str);
		APPLICATION_LOGS.debug(str);		
    	if(compileresult)
    	   	return true;
        else
           	return false;
    	}		
} 							