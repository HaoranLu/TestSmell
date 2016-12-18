package it.unisa.scam14.filtering;

import it.unisa.scam14.utility.FileUtility;

import java.io.IOException;
import java.util.regex.Pattern;

public class FilteringSpaghettiCode {

	public static void main(String args[]) {
		//FilteringSpaghettiCode filtering = new FilteringSpaghettiCode();
		//filtering.applyFilter("/Users/fabiopalomba/Desktop/spaghettiCode_0.csv");
	}

	public static void applyFilter(String pPath) {
		Pattern newLine = Pattern.compile("\n");
		Pattern comma = Pattern.compile(";");
		String newContent ="className;isSpaghettiCode;LOC;LCOM;WMC;RFC;CBO;NOM;NOA;DIT;NOC\n";
		try {
			String content = FileUtility.readFile(pPath);
			String[] lines = newLine.split(content);

			for(int i=1; i<lines.length;i++) {
				String fields[] = comma.split(lines[i]);

				if(Integer.parseInt(fields[2]) > 1000) {
					if(fields[1].contains("false")) {
						newContent+=lines[i].replace("false", "true") + "\n";	
					} else {
						newContent+=lines[i] + "\n";		
					}
				} else {
					newContent+=lines[i].replace("true", "false") + "\n";			
				}

			}
			FileUtility.writeFile(newContent, pPath);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String applyFilter(String pLine, int pIndex) {
		Pattern comma = Pattern.compile(";");
		String newContent = "";

		if(pIndex == 0) {
			return "className;isSpaghettiCode;LOC;LCOM;WMC;RFC;CBO;NOM;NOA;DIT;NOC\n";
		} else {
			String fields[] = comma.split(pLine);

			if(Integer.parseInt(fields[2]) > 1000) {
				if(fields[1].contains("false")) {
					newContent+=pLine.replace("false", "true") + "\n";	
					return newContent;
				} else {
					newContent+=pLine + "\n";
					return newContent;
				}
			} else {
				newContent+=pLine.replace("true", "false") + "\n";
				return newContent;
			}
		}

	}
}
