package it.unisa.scam14.parser;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.InstanceVariableBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.utility.TestSmellUtilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;

import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodParser {

	public static MethodBean parse(MethodDeclaration pMethodNode, Collection<InstanceVariableBean> pClassInstanceVariableBeans) {

		// Instantiate the bean
		MethodBean methodBean = new MethodBean();

		// Set the name
		methodBean.setName(pMethodNode.getName().toString());

		methodBean.setParameters(pMethodNode.parameters());
		methodBean.setReturnType(pMethodNode.getReturnType2());

		// Set the textual content
		methodBean.setTextContent(pMethodNode.toString());

		// Get the names in the method
		Collection<String> names = new HashSet<String>();
		pMethodNode.accept(new NameVisitor(names));

		// Verify the correspondence between names and instance variables 
		Collection<InstanceVariableBean> usedInstanceVariableBeans = getUsedInstanceVariable(names, pClassInstanceVariableBeans);

		// Set the used instance variables
		methodBean.setUsedInstanceVariables(usedInstanceVariableBeans);

		// Get the invocation names
		Collection<MethodBean> invocations = new HashSet<MethodBean>();
		pMethodNode.accept(new InvocationVisitor(invocations));

		// Get the invocation beans from the invocation names
		Collection<MethodBean> invocationBeans = new Vector<MethodBean>();
		for (MethodBean invocation : invocations){
			invocationBeans.add(invocation);
		}

		// Set the invocations
		methodBean.setMethodCalls(invocationBeans);

		// Return the bean
		return methodBean;

	}

	private static Collection<InstanceVariableBean> getUsedInstanceVariable(Collection<String> pNames, Collection<InstanceVariableBean> pClassInstanceVariableBeans) {

		// Instantiate the collection to return
		Collection<InstanceVariableBean> usedInstanceVariableBeans = new Vector<InstanceVariableBean>();

		// Iterate over the instance variables defined in the class
		for (InstanceVariableBean classInstanceVariableBean : pClassInstanceVariableBeans)

			// If there is a correspondence, add to the returned collection
			if (pNames.remove(classInstanceVariableBean.getName()))
				usedInstanceVariableBeans.add(classInstanceVariableBean);

		// Return the collection
		return usedInstanceVariableBeans;
	}
	
	public static void setOtherInfo(MethodBean pInvocation, ClassBean pTestClass, Vector<ClassBean> pSystem) {
		Collection<MethodBean> toCover = pTestClass.getMethods();
		
		// step 1: finding production class using naming conventions
		ClassBean candidateProductionClass = TestSmellUtilities.findProductionClassUsingNamingConventions(pTestClass, pSystem);
		
		if(candidateProductionClass != null) {
			
			for(MethodBean methodBean: toCover) {
				if(methodBean.getName().equals(pInvocation.getName())) {
					pInvocation.setBelongingClass(methodBean.getBelongingClass());
				}
			}
			
		}
		
		ArrayList<String> imported = new ArrayList<String>();

		
		for(String imp: pTestClass.getImports()) {
			imported.add(imp.substring(imp.indexOf(" ")+1, imp.length()-2));
		}

		for(ClassBean classBean : pSystem) {
			if(imported.contains(classBean.getBelongingPackage()+"."+classBean.getName())) {
				for(MethodBean methodBean : classBean.getMethods()) {
					if(methodBean.getName().equals(pInvocation.getName())) {
						pInvocation.setBelongingClass(methodBean.getBelongingClass());
					}
				}
			}
		}

		if(pInvocation.getBelongingClass()==null) {
			for(ClassBean classBean : pSystem) {
				if(pTestClass.getTextContent().contains(classBean.getName())) {
					for(MethodBean methodBean : classBean.getMethods()) {
						if(methodBean.getName().equals(pInvocation.getName())) {

							int commas = 0;
							for(int i = 0; i < pInvocation.getTextContent().length(); i++) {
								if(pInvocation.getTextContent().charAt(i) == ',') commas++;
							}

							if((commas+1) == methodBean.getParameters().size()) {
								pInvocation.setBelongingClass(methodBean.getBelongingClass());
							}

							pInvocation.setBelongingClass(methodBean.getBelongingClass());
						}
					}
				}
			}
		}
	}
}
