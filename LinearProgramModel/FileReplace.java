import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

class FileReplace {
	String line = "";
	String temp = null;

	public void doIt(String string) {
		try {
			File f1 = new File("C:/Users/cchinthalapudi/Desktop/v1/083510112015/index.html");
			FileReader fr = new FileReader(f1);
			BufferedReader br = new BufferedReader(fr);
			while ((temp = br.readLine()) != null) {
				if (temp.contains("<!-- replace-text -->")) {
					line = line.concat("<h2>"+string+"</h2>").concat("\n")
							.concat(temp.concat("\n"));
				} else {
					line = line.concat(temp).concat("\n");
				}
			}
			br.close();
			FileWriter fw = new FileWriter(f1);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(line);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String args[]) {
		FileReplace fr = new FileReplace();
		fr.doIt("<tr><td></td><td></td><td></td><td></td><td></td><td><b><i>Not Executed</b></td></tr>	");
	}
}