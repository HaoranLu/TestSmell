package it.unisa.scam14.badSmellRules;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.beans.PackageBean;
import it.unisa.scam14.metrics.CosineSimilarity;

import java.io.IOException;

public class MispacedClassRule {

	public boolean isMispacedClass(ClassBean pClass, PackageBean pBelongingPackage, PackageBean pCandidateEnvyPackage) {
		CosineSimilarity cosineSimilarity= new CosineSimilarity();
		
		int dependenciesWithCandidateEnvyPackage = computeDependencies(pClass, pCandidateEnvyPackage);
		int dependenciesWithBelongingPackage = computeDependencies(pClass, pBelongingPackage);
		double structuralDiff = dependenciesWithCandidateEnvyPackage - dependenciesWithBelongingPackage;
		
		String[] classInfo=new String[2];
		classInfo[0] = pClass.getName();
		classInfo[1] = pClass.getTextContent();

		String[] belongingPackageInfo=new String[2];
		belongingPackageInfo[0] = pBelongingPackage.getName();
		belongingPackageInfo[1] = pBelongingPackage.getTextContent();

		String[] candidateEnvyPackageInfo=new String[2];
		candidateEnvyPackageInfo[0] = pCandidateEnvyPackage.getName();
		candidateEnvyPackageInfo[1] = pCandidateEnvyPackage.getTextContent();

		try {
			double similarityClassBelongingPackage = cosineSimilarity.computeSimilarity(classInfo, belongingPackageInfo);
			double similarityClassCandidateEnvyPackageInfo = cosineSimilarity.computeSimilarity(classInfo, candidateEnvyPackageInfo);
			double textualDiff = similarityClassCandidateEnvyPackageInfo - similarityClassBelongingPackage;
			
			if(structuralDiff > 3 || textualDiff > 0.1) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	private int computeDependencies(ClassBean pClass, PackageBean pPackage) {
		int dependencies = 0;

		for(MethodBean methodBean: pClass.getMethods()) {
			
			for(MethodBean calledMethod: methodBean.getMethodCalls()) {
				
				for(ClassBean classBean: pPackage.getClasses()) {
					for(MethodBean classMb: classBean.getMethods()) {
						for(MethodBean calledClassMb: classMb.getMethodCalls()) {
							
							if(calledMethod.getName().equals(calledClassMb.getName()))
								dependencies++;
						}
					}
				}
				
			}
		}

		return dependencies;
	}
}
