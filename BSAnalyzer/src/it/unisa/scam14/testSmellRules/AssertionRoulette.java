package it.unisa.scam14.testSmellRules;

import java.util.ArrayList;

import org.omg.CORBA.PUBLIC_MEMBER;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.beans.TestClassBean;

public class AssertionRoulette {

	public boolean isAssertionRoulette(ClassBean pClassBean) {
		boolean assertionRoulette = false;
		
		
		
		for (MethodBean mb : pClassBean.getMethods()) {
			if (!assertionRoulette) {
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

					if (!substring.contains("\"")) {
						assertionRoulette = true;
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

					if (!substring.contains("\"")) {
						assertionRoulette = true;
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

					if (!substring.contains("\"")) {
						assertionRoulette = true;
					}

					methodBody = methodBody.replaceFirst(
							"fail \\(", "");
				}
			}
		}
		
		return assertionRoulette;
	}
	
	/**
	 * Simplify version of the isAssertion rules.
	 * <p>
	 * the searchKeywork and replaceKeywork should match
	 * the assertion result is ArrayList<String>
	 * If needed, a result bean class will make this more
	 * @author Haoran Lu
	 * @see isAssertionRoulette
	 * @param pTestClassBean contain one test class and related production classes
	 * @return object contain an array of assertions found in pClassBean,if no assertion found return null
	 */
	public Object getAssertion(TestClassBean pTestClassBean) {
		ClassBean pClassBean = pTestClassBean.getTestCase();
		Object rtObject = null;
		boolean assertionRoulette = false;
		ArrayList<String> assertionFound = new ArrayList<String>(1);
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

						if (!substring.contains("\"")) {
							assertionRoulette = true;
							assertionFound.add(substring);//find one and add
						}

						methodBody = methodBody.replaceFirst(
								rkeyword, "");
					}
				}
		}
		if (assertionRoulette) {
			return rtObject = assertionFound;
		}
		return rtObject;
	}
}
