package testsmell.relatedCodeSmell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import it.unisa.scam14.beans.ClassBean;

public class codeSmellResult {
	public ClassBean testClass;
	public String testSmellType;
	public Map<String, ClassBean> codeSmellResult;
		
		
	
	public codeSmellResult() {
		super();
		testClass = new ClassBean();
		testSmellType = "";
		codeSmellResult = null;
	}
	public codeSmellResult(ClassBean testClass, String testSmellType, Map<String, ClassBean> codeSmellResult) {
		super();
		this.testClass = testClass;
		this.testSmellType = testSmellType;
		this.codeSmellResult = codeSmellResult;
	}
	/**
	 * @return the testClass
	 */
	public ClassBean getTestClass() {
		return testClass;
	}
	/**
	 * @param testClass the testClass to set
	 */
	public void setTestClass(ClassBean testClass) {
		this.testClass = testClass;
	}
	/**
	 * @return the testSmellType
	 */
	public String getTestSmellType() {
		return testSmellType;
	}
	/**
	 * @param testSmellType the testSmellType to set
	 */
	public void setTestSmellType(String testSmellType) {
		this.testSmellType = testSmellType;
	}
	/**
	 * @return the codeSmellResult
	 */
	public Map<String, ClassBean> getCodeSmellResult() {
		return codeSmellResult;
	}
	/**
	 * @param codeSmellResult the codeSmellResult to set
	 */
	public void setCodeSmellResult(Map<String, ClassBean> codeSmellResult) {
		this.codeSmellResult = codeSmellResult;
	}
	
}
