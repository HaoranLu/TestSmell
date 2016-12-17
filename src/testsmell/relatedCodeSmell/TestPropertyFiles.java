package testsmell.relatedCodeSmell;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TestPropertyFiles {
	private String filePath = "AssociationRules.properties";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, String[]> rules = smellAssociationRules.INSTANCE.getRules();
		System.out.println(rules);
		for (Entry<String, String[]> ee : rules.entrySet()) {
			System.out.println(ee.getKey());
			String[] rrStrings = ee.getValue();
			for (int i = 0; i < rrStrings.length; i++) {
				System.out.println(rrStrings[i]);
			}
			
		}

	}

}
