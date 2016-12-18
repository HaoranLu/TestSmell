package it.unisa.scam14.beans;

public class FeatureEnvy {
	private String name;
	private String targetClassName;
	private boolean isFeatureEnvy;
	private int methodLoc;
	private int methodNop;
	private int methodCc;
	private int methodCbo;
	private int classLoc;
	private int classLcom;
	private int classWmc;
	private int classRfc;
	private int classCbo;
	private int classNom;
	private int classNoa;
	private int classDit;
	private int classNoc;
	private int dependenciesWithBelongingClass;
	private int dependenciesWithTargetClass;
	private double similarityWithBelongingClass;
	private double similarityWithTargetClass;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTargetClassName() {
		return targetClassName;
	}
	public void setTargetClassName(String targetClassName) {
		this.targetClassName = targetClassName;
	}
	public boolean isFeatureEnvy() {
		return isFeatureEnvy;
	}
	public void setFeatureEnvy(boolean isFeatureEnvy) {
		this.isFeatureEnvy = isFeatureEnvy;
	}
	public int getMethodLoc() {
		return methodLoc;
	}
	public void setMethodLoc(int methodLoc) {
		this.methodLoc = methodLoc;
	}
	public int getMethodNop() {
		return methodNop;
	}
	public void setMethodNop(int methodNop) {
		this.methodNop = methodNop;
	}
	public int getMethodCc() {
		return methodCc;
	}
	public void setMethodCc(int methodCc) {
		this.methodCc = methodCc;
	}
	public int getMethodCbo() {
		return methodCbo;
	}
	public void setMethodCbo(int methodCbo) {
		this.methodCbo = methodCbo;
	}
	public int getClassLoc() {
		return classLoc;
	}
	public void setClassLoc(int classLoc) {
		this.classLoc = classLoc;
	}
	public int getClassLcom() {
		return classLcom;
	}
	public void setClassLcom(int classLcom) {
		this.classLcom = classLcom;
	}
	public int getClassWmc() {
		return classWmc;
	}
	public void setClassWmc(int classWmc) {
		this.classWmc = classWmc;
	}
	public int getClassRfc() {
		return classRfc;
	}
	public void setClassRfc(int classRfc) {
		this.classRfc = classRfc;
	}
	public int getClassCbo() {
		return classCbo;
	}
	public void setClassCbo(int classCbo) {
		this.classCbo = classCbo;
	}
	public int getClassNom() {
		return classNom;
	}
	public void setClassNom(int classNom) {
		this.classNom = classNom;
	}
	public int getClassNoa() {
		return classNoa;
	}
	public void setClassNoa(int classNoa) {
		this.classNoa = classNoa;
	}
	public int getClassDit() {
		return classDit;
	}
	public void setClassDit(int classDit) {
		this.classDit = classDit;
	}
	public int getClassNoc() {
		return classNoc;
	}
	public void setClassNoc(int classNoc) {
		this.classNoc = classNoc;
	}
	public int getDependenciesWithBelongingClass() {
		return dependenciesWithBelongingClass;
	}
	public void setDependenciesWithBelongingClass(int dependenciesWithBelongingClass) {
		this.dependenciesWithBelongingClass = dependenciesWithBelongingClass;
	}
	public int getDependenciesWithTargetClass() {
		return dependenciesWithTargetClass;
	}
	public void setDependenciesWithTargetClass(int dependenciesWithTargetClass) {
		this.dependenciesWithTargetClass = dependenciesWithTargetClass;
	}
	public double getSimilarityWithBelongingClass() {
		return similarityWithBelongingClass;
	}
	public void setSimilarityWithBelongingClass(double similarityWithBelongingClass) {
		this.similarityWithBelongingClass = similarityWithBelongingClass;
	}
	public double getSimilarityWithTargetClass() {
		return similarityWithTargetClass;
	}
	public void setSimilarityWithTargetClass(double similarityWithTargetClass) {
		this.similarityWithTargetClass = similarityWithTargetClass;
	}
}
