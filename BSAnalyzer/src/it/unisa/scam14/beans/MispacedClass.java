package it.unisa.scam14.beans;

//LOC	LCOM	WMC	RFC	CBO	NOM	NOA	DIT	NOC	IntraConn	InterConn	DEP-CBP	DEP-CTP	SIM-CBP	SIM-CTP
public class MispacedClass {

	private String name;
	private String targetPackageName;
	private boolean isMisplacedClass;
	private int loc;
	private int lcom;
	private int wmc;
	private int rfc;
	private int cbo;
	private int nom;
	private int noa;
	private int dit;
	private int noc;
	private double intraConnectivity;
	private double interConnectivity;
	private int dependenciesWithBelongingPackage;
	private int dependenciesWithTargetPackage;
	private double similarityWithBelongingPackage;
	private double similarityWithTargetPackage;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isMisplacedClass() {
		return isMisplacedClass;
	}
	public void setMisplacedClass(boolean isMisplacedClass) {
		this.isMisplacedClass = isMisplacedClass;
	}
	public String getTargetPackageName() {
		return targetPackageName;
	}
	public void setTargetPackageName(String targetClassName) {
		this.targetPackageName = targetClassName;
	}
	public int getLoc() {
		return loc;
	}
	public void setLoc(int loc) {
		this.loc = loc;
	}
	public int getLcom() {
		return lcom;
	}
	public void setLcom(int lcom) {
		this.lcom = lcom;
	}
	public int getWmc() {
		return wmc;
	}
	public void setWmc(int wmc) {
		this.wmc = wmc;
	}
	public int getRfc() {
		return rfc;
	}
	public void setRfc(int rfc) {
		this.rfc = rfc;
	}
	public int getCbo() {
		return cbo;
	}
	public void setCbo(int cbo) {
		this.cbo = cbo;
	}
	public int getNom() {
		return nom;
	}
	public void setNom(int nom) {
		this.nom = nom;
	}
	public int getNoa() {
		return noa;
	}
	public void setNoa(int noa) {
		this.noa = noa;
	}
	public int getDit() {
		return dit;
	}
	public void setDit(int dit) {
		this.dit = dit;
	}
	public int getNoc() {
		return noc;
	}
	public void setNoc(int noc) {
		this.noc = noc;
	}
	public double getIntraConnectivity() {
		return intraConnectivity;
	}
	public void setIntraConnectivity(double intraConnectivity) {
		this.intraConnectivity = intraConnectivity;
	}
	public double getInterConnectivity() {
		return interConnectivity;
	}
	public void setInterConnectivity(double interConnectivity) {
		this.interConnectivity = interConnectivity;
	}
	public int getDependenciesWithBelongingPackage() {
		return dependenciesWithBelongingPackage;
	}
	public void setDependenciesWithBelongingPackage(int dependenciesWithBelongingPackage) {
		this.dependenciesWithBelongingPackage = dependenciesWithBelongingPackage;
	}
	public int getDependenciesWithTargetPackage() {
		return dependenciesWithTargetPackage;
	}
	public void setDependenciesWithTargetPackage(int dependenciesWithTargetPackage) {
		this.dependenciesWithTargetPackage = dependenciesWithTargetPackage;
	}
	public double getSimilarityWithBelongingPackage() {
		return similarityWithBelongingPackage;
	}
	public void setSimilarityWithBelongingPackage(double similarityWithBelongingPackage) {
		this.similarityWithBelongingPackage = similarityWithBelongingPackage;
	}
	public double getSimilarityWithTargetPackage() {
		return similarityWithTargetPackage;
	}
	public void setSimilarityWithTargetPackage(double similarityWithTargetPackage) {
		this.similarityWithTargetPackage = similarityWithTargetPackage;
	}
}
