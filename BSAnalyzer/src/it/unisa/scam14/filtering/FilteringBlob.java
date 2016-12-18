package it.unisa.scam14.filtering;

import it.unisa.scam14.utility.FileUtility;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class FilteringBlob {

	public static void main(String args[]) {
		FilteringBlob filtering = new FilteringBlob();
		//filtering.applyFilter("/Users/fabiopalomba/Desktop/blob_10007.csv");
	}

	public static void applyFilter(String pPath) {
		Pattern newLine = Pattern.compile("\n");
		Pattern comma = Pattern.compile(";");
		String newContent ="className;isBlob;LOC;LCOM;WMC;RFC;CBO;NOM;NOA;DIT;NOC\n";
		try {
			String content = FileUtility.readFile(pPath);
			String[] lines = newLine.split(content);

			for(int i=1; i<lines.length;i++) {
				String fields[] = comma.split(lines[i]);

				if( (fields[0].contains("process")) || (fields[0].contains("control") || fields[0].contains("command") 
						|| fields[0].contains("manage") || fields[0].contains("drive") || fields[0].contains("system"))) {

					if(fields[1].equals("false")) {
						newContent+=lines[i].replace("false", "true") + "\n";	
					} else {
						newContent+=lines[i] + "\n";	
					}
				} else if(Integer.parseInt(fields[3]) > 40) {
					if((Integer.parseInt(fields[7]) + Integer.parseInt(fields[8])) > 20 ) {
						if((Integer.parseInt(fields[2])) > 300) {
							if(fields[1].equals("false")) {
								newContent+=lines[i].replace("false", "true") + "\n";	
							} else if(fields[1].equals("true")) {
								newContent+=lines[i]+"\n";	
							}
						} else {
							String sub = lines[i].replace("true", "false");
							newContent+=sub + "\n";
						}
					} else {
						String sub = lines[i].replace("true", "false");
						newContent+=sub + "\n";
					}
				} else {
					String sub = lines[i].replace("true", "false");
					newContent+=sub + "\n";
				}
			}
			FileUtility.writeFile(newContent, pPath);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String applyFilter(String line, int pIndex) {
		Pattern comma = Pattern.compile(";");
		String newContent ="";
		
			if(pIndex == 0) {
				return "className;isBlob;LOC;LCOM;WMC;RFC;CBO;NOM;NOA;DIT;NOC\n";
			} else {
				String fields[] = comma.split(line);

				if( (fields[0].contains("process")) || (fields[0].contains("control") || fields[0].contains("command") 
						|| fields[0].contains("manage") || fields[0].contains("drive") || fields[0].contains("system"))) {

					if(fields[1].equals("false")) {
						newContent+=line.replace("false", "true") + "\n";	
						return newContent;
					} else {
						newContent+=line + "\n";	
						return newContent;
					}
				} else if(Integer.parseInt(fields[3]) > 40) {
					if((Integer.parseInt(fields[7]) + Integer.parseInt(fields[8])) > 20 ) {
						if((Integer.parseInt(fields[2])) > 300) {
							if(fields[1].equals("false")) {
								newContent+=line.replace("false", "true") + "\n";	
								return newContent;
							} else if(fields[1].equals("true")) {
								newContent+=line+"\n";	
								return newContent;
							}
						} else {
							String sub = line.replace("true", "false");
							newContent+=sub + "\n";
							return newContent;
						}
					} else {
						String sub = line.replace("true", "false");
						newContent+=sub + "\n";
						return newContent;
					}
				} else {
					String sub = line.replace("true", "false");
					newContent+=sub + "\n";
					return newContent;
				}
			}
			return newContent;
	}
}
