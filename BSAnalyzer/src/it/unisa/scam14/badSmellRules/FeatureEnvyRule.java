package it.unisa.scam14.badSmellRules;

import java.io.IOException;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.metrics.CosineSimilarity;

/**
 * 
 * Implementation of MethodBook's Feature Envy detection strategy (reported in the following)
 * 
 * @author fabiopalomba
 *
 */
public class FeatureEnvyRule {

	public boolean isFeatureEnvy(MethodBean pMethod, ClassBean pBelongingClass, ClassBean pCandidateEnvyClass) {
		CosineSimilarity cosineSimilarity= new CosineSimilarity();
		
		int dependenciesWithCandidateEnvyClass = computeDependencies(pMethod, pCandidateEnvyClass);
		int dependenciesWithBelongingClass = computeDependencies(pMethod, pBelongingClass);
		double structuralDiff = dependenciesWithCandidateEnvyClass - dependenciesWithBelongingClass;
		
		String[] methodInfo=new String[2];
		methodInfo[0] = pMethod.getName();
		methodInfo[1] = pMethod.getTextContent();

		String[] belongingClassInfo=new String[2];
		belongingClassInfo[0] = pBelongingClass.getName();
		belongingClassInfo[1] = pBelongingClass.getTextContent();

		String[] candidateEnvyClassInfo=new String[2];
		candidateEnvyClassInfo[0] = pCandidateEnvyClass.getName();
		candidateEnvyClassInfo[1] = pCandidateEnvyClass.getTextContent();

		try {
			double similarityMethodBelongingClass = cosineSimilarity.computeSimilarity(methodInfo, belongingClassInfo);
			double similarityMethodCandidateEnvyClassInfo = cosineSimilarity.computeSimilarity(methodInfo, candidateEnvyClassInfo);
			double textualDiff = similarityMethodCandidateEnvyClassInfo - similarityMethodBelongingClass;
			
			if(structuralDiff > 3 || textualDiff > 0.1) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	private int computeDependencies(MethodBean pMethod, ClassBean pClass) {
		int dependencies = 0;

		for(MethodBean calledMethod : pMethod.getMethodCalls()) {

			for(MethodBean classMethod: pClass.getMethods()) {
				if(calledMethod.getName().equals(classMethod.getName())) 
					dependencies++;
			}

		}

		return dependencies;
	}
}