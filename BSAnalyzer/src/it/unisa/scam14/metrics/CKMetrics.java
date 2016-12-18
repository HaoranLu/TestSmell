package it.unisa.scam14.metrics;

import it.unisa.scam14.beans.*;

import java.util.Collection;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CKMetrics {

	public static int getLOC(ClassBean cb){
		return cb.getLOC();
	}

	public static int getWMC(ClassBean cb) {

		int WMC = 0;

		Vector<MethodBean> methods = (Vector<MethodBean>) cb.getMethods();
		for(MethodBean m: methods){
			WMC+=getMcCabeCycloComplexity(m);
		}

		return WMC;

	}


	public static int getDIT(ClassBean cb, Vector<ClassBean> System, int inizialization){

		int DIT = inizialization;

		if(DIT == 3) 
			return DIT;
		else {
			if(cb.getSuperclass() != null) {
				DIT++;
				for(ClassBean cb2: System){
					if(cb2.getName().equals(cb.getSuperclass())){
						getDIT(cb2, System, DIT);
					}
				}
			} else {
				return DIT;
			}
		}
		return DIT;

	}


	public static int getNOC(ClassBean cb, Vector<ClassBean> System){

		int NOC = 0;


		for(ClassBean c: System){
			if(c.getSuperclass() != null && c.getSuperclass().equals(cb.getName())){
				NOC++;
			}
		}

		return NOC;

	}

	public static int getRFC(ClassBean cb){

		int RFC = 0;

		Vector<MethodBean> methods = (Vector<MethodBean>) cb.getMethods();
		for(MethodBean m: methods){
			RFC+=m.getMethodCalls().size();
		}

		return RFC;

	}


	public static int getCBO(ClassBean cb){

		Vector<String> imports = (Vector<String>) cb.getImports();

		return imports.size();

	}


	public static int getLCOM(ClassBean cb){

		int share = 0;
		int notShare = 0;

		Vector<MethodBean> methods = (Vector<MethodBean>) cb.getMethods();
		for(int i=0; i<methods.size(); i++){
			for (int j=i+1; j<methods.size(); j++){
				if(shareAnInstanceVariable(methods.elementAt(i), methods.elementAt(j))){
					share++;
				} else {
					notShare++;
				}
			}
		}

		if(share > notShare){
			return 0;
		} else {
			return (notShare-share);
		}
	}

	public static int getNOM(ClassBean cb){
		return cb.getMethods().size();
	}

	public static int getNOA(ClassBean cb){
		return cb.getInstanceVariables().size();
	}
	
	public static int getNOPA(ClassBean cb) {
		int publicVariable=0;

		Collection<InstanceVariableBean> variables = cb.getInstanceVariables();

		for(InstanceVariableBean variable: variables) {
			if(variable.getVisibility().equals("public"))
				publicVariable++;
		}

		return publicVariable;
	}
	
	public static int getNOPrivateA(ClassBean cb) {
		int privateVariable=0;

		Collection<InstanceVariableBean> variables = cb.getInstanceVariables();

		for(InstanceVariableBean variable: variables) {
			if(variable.getVisibility().equals("private"))
				privateVariable++;
		}

		return privateVariable;
	}

	//Number of operations added by a subclass
	public static int getNOA(ClassBean cb, Vector<ClassBean> System){

		int NOA = 0;

		for(ClassBean c: System){
			if(c.getName().equals(cb.getSuperclass())){
				Vector<MethodBean> subClassMethods = (Vector<MethodBean>) cb.getMethods();
				Vector<MethodBean> superClassMethods = (Vector<MethodBean>) c.getMethods();
				for(MethodBean m: subClassMethods){
					if(!superClassMethods.contains(m)){
						NOA++;
					}
				}
				break;
			}
		}

		return NOA;
	}


	//Number of operations overridden by a subclass
	public static int getNOO(ClassBean cb, Vector<ClassBean> System){

		int NOO = 0;

		if(cb.getSuperclass() != null){

			for(ClassBean c: System){
				if(c.getName().equals(cb.getSuperclass())){
					Vector<MethodBean> subClassMethods = (Vector<MethodBean>) cb.getMethods();
					Vector<MethodBean> superClassMethods = (Vector<MethodBean>) c.getMethods();
					for(MethodBean m: subClassMethods){
						if(superClassMethods.contains(m)){
							NOO++;
						}
					}
					break;
				}
			}
		}

		return NOO;
	}

	public static double computeMediumIntraConnectivity(PackageBean pPackage) {
		double packAllLinks = Math.pow(pPackage.getClasses().size(), 2);
		double packIntraConnectivity=0.0;

		for(ClassBean eClass: pPackage.getClasses()) {
			for(ClassBean current: pPackage.getClasses()) {
				if(eClass!=current) {
					if(existsDependence(eClass, current)) {
						packIntraConnectivity++;
					}
				}
			}
		}

		return packIntraConnectivity/packAllLinks;
	}

	public static double computeMediumInterConnectivity(PackageBean pPackage, Collection<PackageBean> pPackages) {
		double sumInterConnectivities=0.0;	

		for(ClassBean eClass: pPackage.getClasses()) {
			for(PackageBean currentPack: pPackages) {
				double packsInterConnectivity=0.0;
				double packsAllLinks = 2 * pPackage.getClasses().size() * currentPack.getClasses().size();

				if(pPackage!=currentPack) {
					for(ClassBean currentClass: currentPack.getClasses()) {
						if(existsDependence(eClass, currentClass)) {
							packsInterConnectivity++;
						}
					}
				}
				sumInterConnectivities+=((packsInterConnectivity)/packsAllLinks);
			}
		}

		return ((1.0/(pPackages.size()*(pPackages.size()-1))) * sumInterConnectivities);
	}

	public static double computeMediumIntraConnectivity(Collection<PackageBean> pClusters) { 
		double sumIntraConnectivities=0.0;	
		for(PackageBean pack: pClusters) {
			double packAllLinks = Math.pow(pack.getClasses().size(), 2);
			double packIntraConnectivity=0.0;
			for(ClassBean eClass: pack.getClasses()) {
				for(ClassBean current: pack.getClasses()) {
					if(eClass!=current) {
						if(existsDependence(eClass, current)) {
							packIntraConnectivity++;
						}
					}
				}
			}
			sumIntraConnectivities+=packIntraConnectivity/packAllLinks;
		}

		return ((1.0/pClusters.size()) * sumIntraConnectivities);
	}

	public static double computeMediumInterConnectivity(Collection<PackageBean> pClusters) {
		double sumInterConnectivities=0.0;	

		for(PackageBean pack: pClusters) {
			for(ClassBean eClass: pack.getClasses()) {
				for(PackageBean currentPack: pClusters) {
					double packsInterConnectivity=0.0;
					double packsAllLinks = 2 * pack.getClasses().size() * currentPack.getClasses().size();
					if(pack!=currentPack) {
						for(ClassBean currentClass: currentPack.getClasses()) {
							if(existsDependence(eClass, currentClass)) {
								packsInterConnectivity++;
							}
						}
					}
					sumInterConnectivities+=((packsInterConnectivity)/packsAllLinks);
				}
			}

		}

		return ((1.0/(pClusters.size()*(pClusters.size()-1))) * sumInterConnectivities);
	}


	public static int getMcCabeCycloComplexity(MethodBean mb){

		int mcCabe = 0;
		String code = mb.getTextContent();


		String regex = "return";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(code);


		if(matcher.find()){
			mcCabe++;
		}


		regex = "if";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);


		if(matcher.find()){
			mcCabe++;
		}

		regex = "else";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);


		if(matcher.find()){
			mcCabe++;
		}


		regex = "case";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);


		if(matcher.find()){
			mcCabe++;
		}

		regex = "for";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);


		if(matcher.find()){
			mcCabe++;
		}

		regex = "while";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);


		if(matcher.find()){
			mcCabe++;
		}

		regex = "break";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);


		if(matcher.find()){
			mcCabe++;
		}

		regex = "&&";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);


		if(matcher.find()){
			mcCabe++;
		}

		regex = "||";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);

		if(matcher.find()){
			mcCabe++;
		}

		regex = "catch";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);


		if(matcher.find()){
			mcCabe++;
		}

		regex = "throw";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);


		if(matcher.find()){
			mcCabe++;
		}


		return mcCabe;
	}

	private static boolean existsDependence(ClassBean pClass1, ClassBean pClass2) {
		for(MethodBean methodClass1: pClass1.getMethods()) {
			for(MethodBean call: methodClass1.getMethodCalls()) {

				for(MethodBean methodClass2: pClass2.getMethods()) {
					if(call.getName().equals(methodClass2.getName())) 
						return true;
				}
			}
		}

		for(MethodBean methodClass2: pClass2.getMethods()) {
			for(MethodBean call: methodClass2.getMethodCalls()) {

				for(MethodBean methodClass1: pClass1.getMethods()) {
					if(call.getName().equals(methodClass1.getName())) 
						return true;
				}
			}
		}

		return false;
	}

	private static boolean shareAnInstanceVariable(MethodBean m1, MethodBean m2){

		Vector<InstanceVariableBean> m1Variables = (Vector<InstanceVariableBean>) m1.getUsedInstanceVariables();
		Vector<InstanceVariableBean> m2Variables = (Vector<InstanceVariableBean>) m2.getUsedInstanceVariables();

		for(InstanceVariableBean i: m1Variables){
			if(m2Variables.contains(i)){
				return true;
			}
		}

		return false;

	}

}
