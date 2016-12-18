package it.unisa.scam14.filtering;

import it.unisa.scam14.utility.FileUtility;

import java.io.IOException;
import java.util.regex.Pattern;

public class FilteringLongMethod {

	public static void main(String args[]) {
		//FilteringLongMethod filtering = new FilteringLongMethod();
		//filtering.applyFilter("/Users/fabiopalomba/Desktop/longMethod_0.csv");
	}

	public static void applyFilter(String pPath) {
		Pattern newLine = Pattern.compile("\n");
		Pattern comma = Pattern.compile(";");
		String newContent ="methodName;isLongMethod;LOC;NOP;CC;CBO\n";
		try {
			String content = FileUtility.readFile(pPath);
			String[] lines = newLine.split(content);

			for(int i=1; i<lines.length;i++) {
				String fields[] = comma.split(lines[i]);

				if(Integer.parseInt(fields[2]) > 100) {
					if(Integer.parseInt(fields[3]) >= 2) {
						if(fields[1].contains("false")) {
							newContent+=lines[i].replace("false", "true") + "\n";	
						} else {
							newContent+=lines[i] + "\n";		
						}
					} else {
						newContent+=lines[i].replace("true", "false") + "\n";			
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
		String newContent ="";

		String fields[] = comma.split(pLine);

		if(pIndex == 0) {
			return "methodName;isLongMethod;LOC;NOP;CC;CBO\n";
		} else {
		
			if(Integer.parseInt(fields[2]) > 100) {
				if(Integer.parseInt(fields[3]) >= 2) {
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
			} else {
				newContent+=pLine.replace("true", "false") + "\n";
				return newContent;
			}
		}
	}
}
