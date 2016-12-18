package it.unisa.scam14.testSmellRules;

import java.util.ArrayList;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.beans.TestClassBean;

public class SensitiveEquality {

	public boolean isSensitiveEquality(ClassBean pClassBean) {
		boolean sensitiveEquality = false;

		for (MethodBean mb : pClassBean.getMethods()) {
			if (!sensitiveEquality) {
				String methodBody = mb.getTextContent();
				methodBody = methodBody.replace(mb.getName(),
						"");

				while (methodBody.contains("assert")) {
					int indexStart = methodBody
							.indexOf("assert");
					int indexEnd = indexStart;
					char c = methodBody.charAt(indexStart);
					String substring = c + "";
					while (c != ';') {
						indexEnd++;
						c = methodBody.charAt(indexEnd);
						substring += c + "";
					}

					if (substring.toLowerCase().contains(
							"tostring")) {
						sensitiveEquality = true;
					}

					methodBody = methodBody.replaceFirst(
							"assert", "");
				}

				while (methodBody.contains("fail(")) {
					int indexStart = methodBody
							.indexOf("fail(");
					int indexEnd = indexStart;
					char c = methodBody.charAt(indexStart);
					String substring = c + "";
					while (c != ';') {
						indexEnd++;
						c = methodBody.charAt(indexEnd);
						substring += c + "";
					}

					if (substring.toLowerCase().contains(
							"tostring")) {
						sensitiveEquality = true;
					}

					methodBody = methodBody.replaceFirst(
							"fail\\(", "");
				}

				while (methodBody.contains("fail (")) {
					int indexStart = methodBody
							.indexOf("fail (");
					int indexEnd = indexStart;
					char c = methodBody.charAt(indexStart);
					String substring = c + "";
					while (c != ';') {
						indexEnd++;
						c = methodBody.charAt(indexEnd);
						substring += c + "";
					}

					if (substring.toLowerCase().contains(
							"tostring")) {
						sensitiveEquality = true;
					}

					methodBody = methodBody.replaceFirst(
							"fail \\(", "");
				}
			}
		}

		return sensitiveEquality;
	}
	
	/**
	 * Basically same with getAssertion method.
	 * <p>
	 * the searchKeywork and replaceKeywork should match
	 * If needed, a result bean class will make this more
	 * @author Haoran Lu
	 * @param pTestClassBean
	 * @return ArrayList<String> EqualityFound and null for negative SensitiveEquality
	 */
	public Object getSensitiveEquality(TestClassBean pTestClassBean) {
		ClassBean pClassBean = pTestClassBean.getTestCase();
		Object rtObject = null;
		boolean SensitiveEquality = false;
		ArrayList<String> EqualityFound = new ArrayList<String>(1);
		String[] searchKeyword = {
			"assert", "fail(", "fail ("
		};
		String[] replaceKeyword = {
			"assert", "fail\\(", "fail \\("
		};
		for (MethodBean mb : pClassBean.getMethods()) {
				String methodBody = mb.getTextContent();
				methodBody = methodBody.replace(mb.getName(),"");
				//use different keyword to detect assertions without explanation
				for (int i = 0; i < searchKeyword.length; i++) {
					String skeyword = searchKeyword[i];
					String rkeyword = replaceKeyword[i];
					while (methodBody.contains(skeyword)) {
						int indexStart = methodBody
								.indexOf(skeyword);
						int indexEnd = indexStart;
						char c = methodBody.charAt(indexStart);
						String substring = c + "";
						while (c != ';') {
							indexEnd++;
							c = methodBody.charAt(indexEnd);
							substring += c + "";
						}

						if (substring.toLowerCase().contains(
								"tostring")) {
							SensitiveEquality = true;
							EqualityFound.add(substring);//find one and add
						}

						methodBody = methodBody.replaceFirst(
								rkeyword, "");
					}
				}
		}
		if (SensitiveEquality) {
			return rtObject = EqualityFound;
		}
		return rtObject;
	}
}
