package it.unisa.scam14.main;

import it.unisa.scam14.beans.ClassArtifact;
import it.unisa.scam14.beans.MethodArtifact;
import it.unisa.scam14.beans.MetricOutput;
import it.unisa.scam14.beans.PackageBean;
import it.unisa.scam14.computation.Computation;
import it.unisa.scam14.utility.FolderToJavaProjectConverter;

import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;

public class Main {

	public static final String stopwordListPath = "/Users/fabiopalomba/Desktop/stopword";

	/**
	 * 
	 * Starting computation:
	 * 
	 * 		pFolderPath: the folder where the project is located;
	 * 		pOutputPath: the path where the csv file will be placed;
	 * 
	 * 
	 */
	public void computeRules(String pFolderPath, String pOutputPath, int pCommitNumber, long pAuthorTime, 
			long pCommitterTime, String pCommitHash, String pSystemType) {
		
		Computation computation = new Computation();
		
		try {
			Vector<PackageBean> packages = FolderToJavaProjectConverter.convert(pFolderPath);
			computation.computeClassLevelSmellRules(packages, pOutputPath, pCommitNumber, pAuthorTime, pCommitterTime, pCommitHash, pSystemType);
			//computation.computeMethodLevelSmellRules(packages, pOutputPath, pCommitNumber, pAuthorTime, pCommitterTime, pCommitHash);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public void computeRules(String pFolderPath, int pCommitNumber, long pAuthorTime, 
			long pCommitterTime, String pCommitHash, ArrayList<ClassArtifact> pClassArtifact,
			ArrayList<MethodArtifact> pMethodArtifacts, ArrayList<String> pClassPaths, 
			ArrayList<String> pMethodPaths, String pSystemType) {
		
		Computation computation = new Computation();
		
		try {
			Vector<PackageBean> packages = FolderToJavaProjectConverter.convert(pFolderPath);
			computation.computeClassLevelSmellRules(packages, pCommitNumber, pAuthorTime, pCommitterTime, pCommitHash, pClassArtifact, pClassPaths, pSystemType);
			//computation.computeMethodLevelSmellRules(packages, pCommitNumber, pAuthorTime, pCommitterTime, pCommitHash, pMethodArtifacts, pMethodPaths);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
}