package it.unisa.scam14.testSmellRules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.InstanceVariableBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.beans.TestClassBean;

public class GeneralFixture {

	public boolean isGeneralFixture(ClassBean pClassBean) {

		for (MethodBean mb : pClassBean.getMethods()) {
			
			if (mb.getName().toLowerCase().equals("setup")) {
				Vector<InstanceVariableBean> instanceVariablesInsideSetUp = (Vector<InstanceVariableBean>) mb
						.getUsedInstanceVariables();

				if (instanceVariablesInsideSetUp.size() > 0) {
					for (MethodBean mbInside : pClassBean.getMethods()) {
						if (!mbInside.getName().equals(
								pClassBean.getName())
								&& !mbInside.getName()
								.toLowerCase()
								.equals("setup")
								&& !mbInside.getName()
								.toLowerCase()
								.equals("teardown")) {
							Vector<InstanceVariableBean> tmpUsed = (Vector<InstanceVariableBean>) mbInside
									.getUsedInstanceVariables();
							for (InstanceVariableBean ivb : instanceVariablesInsideSetUp) {
								if (!tmpUsed.contains(ivb)) {
									return true;
								}
							}
						} 
					}

				}
			}
		}
		
		return false;
	}
	
	/**
	 * The variables declared in the "setup" method but didn't used in every other method except teardown method and setup itself.
	 * @param pTestClassBean
	 * @return HashSet<InstanceVariableBean> unusedVariable will be returned and null for negative GeneralFixture
	 */
	public Object getGeneralFixture(TestClassBean pTestClassBean) {

		Object rtObject = null;
		HashSet<InstanceVariableBean> unusedVariable = new HashSet<InstanceVariableBean>();
		boolean isGeneralFixture = false;
		ClassBean pClassBean = pTestClassBean.getTestCase();
		for (MethodBean mb : pClassBean.getMethods()) {

			if (mb.getName().toLowerCase().equals("setup")) {
				Vector<InstanceVariableBean> instanceVariablesInsideSetUp = (Vector<InstanceVariableBean>) mb
						.getUsedInstanceVariables();

				if (instanceVariablesInsideSetUp.size() > 0) {
					for (MethodBean mbInside : pClassBean.getMethods()) {
						if (!mbInside.getName().equals(pClassBean.getName())
								&& !mbInside.getName().toLowerCase().equals("setup")
								&& !mbInside.getName().toLowerCase().equals("teardown")) {
							Vector<InstanceVariableBean> tmpUsed = (Vector<InstanceVariableBean>) mbInside
									.getUsedInstanceVariables();
							for (InstanceVariableBean ivb : instanceVariablesInsideSetUp) {
								if (!tmpUsed.contains(ivb)) {
									unusedVariable.add(ivb);
									isGeneralFixture = true;
								}
							}
						}
					}

				}
			}
		}
		if (isGeneralFixture) {
			rtObject = unusedVariable;
		}
		return rtObject;
	}
}
