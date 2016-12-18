package it.unisa.scam14.beans;

import java.util.ArrayList;

public class TestClassBean {
	private ClassBean testCase;
	private ArrayList<ClassBean> productionClasses;
	
	public TestClassBean(){
		
	}
	
	public TestClassBean(ClassBean pTestClass, ArrayList<ClassBean> pProductionClasses){
		this.testCase = pTestClass;
		this.productionClasses = pProductionClasses;
	}
	
	public ClassBean getTestCase() {
		return testCase;
	}
	
	public void setTestCase(ClassBean testCase) {
		this.testCase = testCase;
	}
	
	public ArrayList<ClassBean> getProductionClasses() {
		return productionClasses;
	}
	
	public void setProductionClasse(ArrayList<ClassBean> pProductionClasses) {
		this.productionClasses = pProductionClasses;
	}

}