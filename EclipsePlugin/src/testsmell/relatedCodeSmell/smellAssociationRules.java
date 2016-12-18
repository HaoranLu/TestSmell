/**
 * 
 */
package testsmell.relatedCodeSmell;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import testsmell.PluginCandidate;

/**
 * provide rule to the related code smell detection.
 * <p>
 * Rule are read from properties file inside resource floder.
 * Default rules will be load if properties file not found or error
 * @author Haoran Lu
 *
 */
public enum smellAssociationRules {
	INSTANCE;
	private HashMap<String, String[]> Rules;
	/**
	 * this indicate the properties file name in the resource folder
	 */
	private String filePath = "AssociationRules.properties";
	/**
	 * Read the properties file to initialize Rule at constructing
	 */
	private smellAssociationRules() {

		Rules = new HashMap<String, String[]>();
		Properties prop = new Properties();
		String propFileName = filePath;
		
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		
		if (inputStream != null) {
			try {
				prop.load(inputStream);
				SetRulesUsingProperty(prop);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				SetDefault();
			}
		}else{
			System.out.println("Test Smell Plugin: property file " + filePath + "not found");
			SetDefault();
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
   	
    }
	/**
	 * @return the smell association rules
	 */
	public HashMap<String, String[]> getRules() {
		return Rules;
	}
	/**
	 * set the smell association rules using given properties.
	 * <p>
	 * Be careful, make sure all the properties appears in the properties file
	 * @param prop
	 */
	public void SetRulesUsingProperty(Properties prop){
		Rules = new HashMap<String, String[]>();
		String AssertionRoulette = prop.getProperty("AssertionRoulette");
		String EagerTest = prop.getProperty("EagerTest");
		String MysteryGuest = prop.getProperty("MysteryGuest");
		String GeneralFixture = prop.getProperty("GeneralFixture");
	   	String SensitiveEquality = prop.getProperty("SensitiveEquality");
	   	Rules.put("AssertionRoulette", getArrayfromString(AssertionRoulette));
	   	Rules.put("EagerTest", getArrayfromString(EagerTest));
	   	Rules.put("MyteryGuest", getArrayfromString(MysteryGuest));
	   	Rules.put("GeneralFiture", getArrayfromString(GeneralFixture));
	   	Rules.put("Sensitive", getArrayfromString(SensitiveEquality));
	}
	/**
	 * set default rules.
	 */
	public void SetDefault(){
		Rules = new HashMap<String, String[]>();
	   	String[] AssertionRoulette ={ "CDSBP", "BC"};
	   	String[] EagerTest = {"CDSBP","SC", "BC"};
	   	String[] MysteryGuest = {};
	   	String[] GeneralFixture = {};
	   	String[] SensitiveEquality = {};
	   	Rules.put("AssertionRoulette", AssertionRoulette);
	   	Rules.put("EagerTest", EagerTest);
	   	Rules.put("MyteryGuest", MysteryGuest);
	   	Rules.put("GeneralFiture", GeneralFixture);
	   	Rules.put("Sensitive", SensitiveEquality);
	}
	/**
	 * use regex to get Array from a comma split string
	 * @param str
	 * @return String[] . For example, ["SC","BC"]
	 */
	public String[] getArrayfromString(String str){
		List<String> items = Arrays.asList(str.split("\\s*,\\s*"));
		return (String [])items.toArray();
	}
	
}
