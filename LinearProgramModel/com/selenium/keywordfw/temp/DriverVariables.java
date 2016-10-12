package com.selenium.keywordfw.temp;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class DriverVariables {
	
//	static{
		public static String Recovery_Arg02 = null;
		public static String Recovery_Arg01 = null;
		public static String list []= new String [20];
		
		public static String ts_Type ="";		// 	Teststep type
		public static String TC_Name=""; 		//	Testcase name
		public static String TS_Name=""; 		// 	Testset name
		public static String oldTS_Name=""; 	// 	OldTestset name
		public static String oldTC_Name=""; 	//	Old Testcase name	
		
		public static int TC_maxRow = 0;		
		public static int Roleback_Now = 0;
		public static int selectcase = 0;    	
		public static int DD_Startpoint=0;
		public static int DD_Endpoint=0;
		public static int DataDriven=0;
		public static int Data_Startpoint=0;
		public static int DD_TCmaxRow=0;
		public static int JumpLevel = 0;
		public static int JumpRowCount[] = new int[5];
		public static int Visited[] = new int[5];    
	    
		public static InputStream Testset_input = null;
		public static POIFSFileSystem ts;
		public static HSSFWorkbook tswb = null;
		public static HSSFSheet TS_sheet = null;
		public static HSSFSheet DD_sheet = null;
		public static Logger APPLICATION_LOGS=null; 
		public static int TS_maxRow =0;
		
		public static String KW_Name="";
		public static String flag = "";
		public static String CTestCaseName = "";
		
		public static String Arg01="";
		public static String Arg02="";
		public static String Arg03="";
		
		public static String fileName="";
		public static String JumpTestSetName[] = new String[5];
		public static String JumpTestCaseName[] = new String[5];
	    
		public static boolean result;
		public static boolean isFirstTestset = true;
		public static boolean isFirstTestcase = true;
		public static boolean TestsetResult = true;
		public static boolean TestcaseResult = true;
		
		public static Calendar cal;
		public static Properties prop=new Properties();
		public static Hashtable<String, String> table = new Hashtable<String, String>();;
//	}

}
