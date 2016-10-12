package com.selenium.keywordfw.userdefined;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateReport {
	
	public static void main(String[] args) {
		String repositoryLocation="c://Selenium Reports//";		
		
		if(createRepository(repositoryLocation ))
			System.out.println("Repository created sucessfully");		
		else
			System.out.println("Error in creating Repository");
	}
	
	

	// Create Repository
	private static boolean createRepository(String repositoryLocation) {
		boolean status=false;
		
		// create repository folder then return repository name
		String repositoryName = createRepositoryfolder(repositoryLocation);
		String repositoryPath = repositoryLocation+repositoryName;
		if(createReportFile(repositoryPath))
			status = true;
		else
			System.out.println("Error in creating default report file");
		
		return status;		
	}


	// Create folder
	private static String createRepositoryfolder(String repository_Location) {
		Date myDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		String repositoryName = sdf.format(myDate);
		File dir = new File(repository_Location+repositoryName);
		dir.mkdir();
		return repositoryName;
	}
	
	// Create Report File
	private static boolean createReportFile(String repositoryPath) {
		File file = new File(repositoryPath+"\\Report.html");
		boolean status = false;
		
		try {
			file.createNewFile();
			writeReportStructure(file);
			status=true;
			} 
		catch (IOException e) {
				//e.printStackTrace();
				}
		
		return status;
		}

	
	// Write basic report structure
	private static void writeReportStructure(File file) throws IOException {
		String content = "This is the content to write into filezxd";
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");
		bw.write("    <head>\n");
		bw.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
		bw.write("        <title>Demo Expandable list</title>\n");
		bw.write("        <link rel=\"stylesheet\" href=\"../css/style.css\" type=\"../text/css\" media=\"screen, projection\">\n");
		bw.write("        <script type=\"text/javascript\" src=\"../js/jquery-1.4.2.min.js\">\n");
		bw.write("        </script>\n");
		bw.write("        <script type=\"text/javascript\" src=\"../js/scripts.js\">\n");
		bw.write("        </script>\n");
		bw.write("    </head>\n");
		bw.write("    <body>\n");
		bw.write("    </body>\n");
		bw.write("\n");
		
		bw.write("        <h1><b>Demo ExpandableList</b></h1>\n");
		bw.write("        <div id=\"listContainer\">\n");
		bw.write("            <div class=\"listControl\">\n");
		bw.write("                <a id=\"expandList\">Expand All</a>\n");
		bw.write("                <a id=\"collapseList\">Collapse All</a>\n");
		bw.write("            </div>\n");
		bw.write("            <ul id=\"expList\">\n");
		bw.write("				<li>\n");
		bw.write("                    Excel file Name\n");
		bw.write("                    <ul>\n");
		
		// Write Test Result column headings
		bw.write("                        <li>\n");
		bw.write("                            Sheet Name 01\n");
		bw.write("                            <ul>\n");
		bw.write("                          		<table border=\"1\" style=\"width:900px\">\n");
		bw.write("									<tr>\n");
		bw.write("									  <td>Testcase Name</td>	\n");
		bw.write("									  <td>Date Executed</td>\n");
		bw.write("									  <td>Start Timee</td>\n");
		bw.write("									  <td>Finish time</td>\n");
		bw.write("									  <td>Execute</td>\n");
		bw.write("									  <td><b><i>Result</b></td>\n");
		bw.write("									</tr>\n");
		bw.write("								</table>\n");
		bw.write("                            </ul>\n");
		bw.write("                        </li>\n");		
		bw.write("                        Insert test result");
 	
		
		bw.write("                     </ul>\n");
		bw.write("                </li>\n");
		bw.write("            </ul>\n");
		bw.write("        </div>\n");
		bw.write("    </body>\n");
		bw.write("</html>\n");		    
		
		bw.close();		
	}


	

}
