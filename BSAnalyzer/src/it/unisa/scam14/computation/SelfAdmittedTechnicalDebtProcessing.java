package it.unisa.scam14.computation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.beans.SelfAdmittedTechnicalDebt;

public class SelfAdmittedTechnicalDebtProcessing {

	public static void main(String args[]) {
		ClassBean cb = new ClassBean();
		MethodBean mb = new MethodBean();
		
		mb.setTextContent(""
				+ "/**"
				+ "kludge ciao 2io4y3r@###,.a,oemci gbh9128pynxtuvknmwjb26154876749031997565134765rgbygbknwm.krjnxlerjglkrq]q[[[øπ„€øπoqporohegwbfyuw[[¡¡¡''''???rx2gfvc juknt7r4jòmojrvefgwdjvbyu41ywefvnxnmò29txg847nf91vyu"
				+ "*/"
				+ ""
				+ "public Collection<SelfAdmittedTechnicalDebt> extractSelfAdmittedTechnicalDebts(ClassBean pTestClass) {\n"
				+ "Collection<SelfAdmittedTechnicalDebt> selfAdmittedTechnicalDebts = new ArrayList<SelfAdmittedTechnicalDebt>();"
				+ "//kludge ciao 2io4y3r@###,.a,oemci gbh9128pynxtuvknmwjb26154876749031997565134765rgbygbknwm.krjnxlerjglkrq]q[[[øπ„€øπoqporohegwbfyuw[[¡¡¡''''???rx2gfvc juknt7r4jòmojrvefgwdjvbyu41ywefvnxnmò29txg847nf91vyu"
				+ "for(MethodBean testMethod: pTestClass.getMethods()) {"
				+ "if(this.containsPattern(testMethod.getTextContent())) {"
				+ "SelfAdmittedTechnicalDebt selfAdmittedTechnicalDebt = new SelfAdmittedTechnicalDebt();"
				+ "selfAdmittedTechnicalDebt.setCorrespondingMethod(testMethod);\n"
				+ "//todo\n"
				+ "Matcher matcher = pattern.matcher(testMethod.getTextContent());"
				+ "selfAdmittedTechnicalDebt.setAdmission(matcher.group(0)) ");
		
		cb.addMethod(mb);
		
		SelfAdmittedTechnicalDebtProcessing sp = new SelfAdmittedTechnicalDebtProcessing();
		Collection<SelfAdmittedTechnicalDebt> td = sp.extractSelfAdmittedTechnicalDebts(cb);
				
		System.out.println(td.size());
		
		for(SelfAdmittedTechnicalDebt satd: td) {
			System.out.println("Admission: " + satd.getAdmission());
		}
	}
	
	public Collection<SelfAdmittedTechnicalDebt> extractSelfAdmittedTechnicalDebts(ClassBean pTestClass) {
		Collection<SelfAdmittedTechnicalDebt> selfAdmittedTechnicalDebts = new ArrayList<SelfAdmittedTechnicalDebt>();

		for(MethodBean testMethod: pTestClass.getMethods()) {
			
			Pattern multilinePattern = Pattern.compile("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)");
			Matcher multilineMatcher = multilinePattern.matcher(testMethod.getTextContent());
			
			Pattern inlineComment = Pattern.compile("//(.*)");
			Matcher inlineMatcher = inlineComment.matcher(testMethod.getTextContent());
			
			String comments = "";
			
			while(multilineMatcher.find()) {
				comments+=multilineMatcher.group();
			}
			
			while(inlineMatcher.find()) {
				comments+=" " + inlineMatcher.group();
			}
			
			if(this.containsPattern(comments)) {
				
				SelfAdmittedTechnicalDebt selfAdmittedTechnicalDebt = new SelfAdmittedTechnicalDebt();
				selfAdmittedTechnicalDebt.setCorrespondingMethod(testMethod);
				
				selfAdmittedTechnicalDebt.setAdmission(comments.replaceAll("\n", " "));
				selfAdmittedTechnicalDebts.add(selfAdmittedTechnicalDebt);
			}
		}

		return selfAdmittedTechnicalDebts;
	}

	private boolean containsPattern(String pTestMethodContent) {

		if( (pTestMethodContent.toLowerCase().contains("todo")) || (pTestMethodContent.toLowerCase().contains("temporary code")) ||
			(pTestMethodContent.toLowerCase().contains("temporary fix")) || (pTestMethodContent.toLowerCase().contains("temporary patch")) ||
			(pTestMethodContent.toLowerCase().contains("temp fix")) || (pTestMethodContent.toLowerCase().contains("temp patch")) || 
			(pTestMethodContent.toLowerCase().contains("be improved")) || (pTestMethodContent.toLowerCase().contains("to refactor")) ||
			(pTestMethodContent.toLowerCase().contains("to improve")) || (pTestMethodContent.toLowerCase().contains("hack")) ||
			(pTestMethodContent.toLowerCase().contains("retarded")) || (pTestMethodContent.toLowerCase().contains("at a loss")) ||
			(pTestMethodContent.toLowerCase().contains("stupid")) || (pTestMethodContent.toLowerCase().contains("remove this code")) ||
			(pTestMethodContent.toLowerCase().contains("ugly")) || (pTestMethodContent.toLowerCase().contains("take care")) ||
			(pTestMethodContent.toLowerCase().contains("something\'s gone wrong")) || (pTestMethodContent.toLowerCase().contains("something is gone wrong")) ||
			(pTestMethodContent.toLowerCase().contains("nuke")) || (pTestMethodContent.toLowerCase().contains("is problematic")) ||
			(pTestMethodContent.toLowerCase().contains("may cause problem")) || (pTestMethodContent.toLowerCase().contains("hacky")) ||
			(pTestMethodContent.toLowerCase().contains("unknown why we ever experience this")) || (pTestMethodContent.toLowerCase().contains("treat this as a soft error")) ||
			(pTestMethodContent.toLowerCase().contains("silly")) || (pTestMethodContent.toLowerCase().contains("workaround for bug")) ||
			(pTestMethodContent.toLowerCase().contains("kludge")) || (pTestMethodContent.toLowerCase().contains("fixme")) ||
			(pTestMethodContent.toLowerCase().contains("this isn\'t quite right")) || (pTestMethodContent.toLowerCase().contains("trial and error")) ||
			(pTestMethodContent.toLowerCase().contains("give up")) || (pTestMethodContent.toLowerCase().contains("this is wrong")) ||
			(pTestMethodContent.toLowerCase().contains("hang our heads in shame")) || (pTestMethodContent.toLowerCase().contains("temporary solution")) ||
			(pTestMethodContent.toLowerCase().contains("causes issue")) || (pTestMethodContent.toLowerCase().contains("something bad is going on")) ||
			(pTestMethodContent.toLowerCase().contains("cause for issue")) || (pTestMethodContent.toLowerCase().contains("this doesn't look right")) ||
			(pTestMethodContent.toLowerCase().contains("is this next line safe")) || (pTestMethodContent.toLowerCase().contains("this indicates a more fundamental problem")) ||
			(pTestMethodContent.toLowerCase().contains("temporary crutch")) || (pTestMethodContent.toLowerCase().contains("this can be a mess")) ||
			(pTestMethodContent.toLowerCase().contains("this isn't very solid")) || (pTestMethodContent.toLowerCase().contains("this is temporary and will go away")) ||
			(pTestMethodContent.toLowerCase().contains("is this line really safe")) || (pTestMethodContent.toLowerCase().contains("there is a problem")) ||
			(pTestMethodContent.toLowerCase().contains("some fatal error")) || (pTestMethodContent.toLowerCase().contains("something serious is wrong")) ||
			(pTestMethodContent.toLowerCase().contains("don't use this")) || (pTestMethodContent.toLowerCase().contains("get rid of this")) ||
			(pTestMethodContent.toLowerCase().contains("doubt that this would work")) || (pTestMethodContent.toLowerCase().contains("this is bs")) ||
			(pTestMethodContent.toLowerCase().contains("give up and go away")) || (pTestMethodContent.toLowerCase().contains("risk of this blowing up")) ||
			(pTestMethodContent.toLowerCase().contains("just abandon it")) || (pTestMethodContent.toLowerCase().contains("prolly a bug")) ||
			(pTestMethodContent.toLowerCase().contains("probably a bug")) || (pTestMethodContent.toLowerCase().contains("hope everything will work")) ||
			(pTestMethodContent.toLowerCase().contains("toss it")) || (pTestMethodContent.toLowerCase().contains("barf")) ||
			(pTestMethodContent.toLowerCase().contains("something bad happened")) || (pTestMethodContent.toLowerCase().contains("barf")) ||
			(pTestMethodContent.toLowerCase().contains("yuck")) || (pTestMethodContent.toLowerCase().contains("fix this crap")) ||
			(pTestMethodContent.toLowerCase().contains("certainly buggy")) || (pTestMethodContent.toLowerCase().contains("remove me before production")) ||
			(pTestMethodContent.toLowerCase().contains("you can be unhappy now")) || (pTestMethodContent.toLowerCase().contains("this is uncool")) ||
			(pTestMethodContent.toLowerCase().contains("bail out")) || (pTestMethodContent.toLowerCase().contains("it doesn\'t work yet")) ||
			(pTestMethodContent.toLowerCase().contains("crap")) || (pTestMethodContent.toLowerCase().contains("inconsistency")) ||
			(pTestMethodContent.toLowerCase().contains("abandon all hope")) || (pTestMethodContent.toLowerCase().contains("kaboom"))) {
				return true;
		}
		
		return false;
	}

}