package it.unisa.scam14.beans;

import java.util.ArrayList;

public class ClassArtifact {

	private String name;
	private ArrayList<Integer> loc = new ArrayList<Integer>();
	private ArrayList<Integer> lcom= new ArrayList<Integer>();
	private ArrayList<Integer> wmc= new ArrayList<Integer>();
	private ArrayList<Integer> rfc= new ArrayList<Integer>();
	private ArrayList<Integer> cbo= new ArrayList<Integer>();
	private ArrayList<Integer> nom= new ArrayList<Integer>();
	private ArrayList<Integer> noa= new ArrayList<Integer>();
	private ArrayList<Integer> nopa= new ArrayList<Integer>();
	private ArrayList<Integer> dit= new ArrayList<Integer>();
	private ArrayList<Integer> noc= new ArrayList<Integer>();
	private ArrayList<Boolean> isGodClass = new ArrayList<Boolean>();
	private ArrayList<Boolean> isComplexClass = new ArrayList<Boolean>();
	private ArrayList<Boolean> isSpaghettiCode = new ArrayList<Boolean>();
	private ArrayList<Boolean> isClassDataShouldBePrivate = new ArrayList<Boolean>();
	private ArrayList<Boolean> isFunctionalDecomposition = new ArrayList<Boolean>();
	
	private ArrayList<Long> authorTime = new ArrayList<Long>();
	private ArrayList<Long> committerTime = new ArrayList<Long>();
	private ArrayList<String> commitHash = new ArrayList<String>();
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Integer> getLoc() {
		return loc;
	}
	public void setLoc(ArrayList<Integer> loc) {
		this.loc = loc;
	}

	public void addLoc(Integer pMetric) {
		this.loc.add(pMetric);
	}
	
	public ArrayList<Integer> getLcom() {
		return lcom;
	}
	
	public void setLcom(ArrayList<Integer> lcom) {
		this.lcom = lcom;
	}
	
	public void addLcom(Integer pMetric) {
		this.lcom.add(pMetric);
	}
	
	public ArrayList<Integer> getWmc() {
		return wmc;
	}
	public void setWmc(ArrayList<Integer> wmc) {
		this.wmc = wmc;
	}
	
	public void addWmc(Integer pMetric) {
		this.wmc.add(pMetric);
	}
	
	public ArrayList<Integer> getRfc() {
		return rfc;
	}
	public void setRfc(ArrayList<Integer> rfc) {
		this.rfc = rfc;
	}
	
	public void addRfc(Integer pMetric) {
		this.rfc.add(pMetric);
	}
	
	public ArrayList<Integer> getCbo() {
		return cbo;
	}
	public void setCbo(ArrayList<Integer> cbo) {
		this.cbo = cbo;
	}
	
	public void addCbo(Integer pMetric) {
		this.cbo.add(pMetric);
	}
	
	public ArrayList<Integer> getNom() {
		return nom;
	}
	public void setNom(ArrayList<Integer> nom) {
		this.nom = nom;
	}
	
	public void addNom(Integer pMetric) {
		this.nom.add(pMetric);
	}
	
	public ArrayList<Integer> getNoa() {
		return noa;
	}
	
	public void addNoa(Integer pMetric) {
		this.noa.add(pMetric);
	}
	
	public void setNoa(ArrayList<Integer> noa) {
		this.noa = noa;
	}
	
	public ArrayList<Integer> getNopa() {
		return nopa;
	}
	
	public void setNopa(ArrayList<Integer> nopa) {
		this.nopa = nopa;
	}
	
	public void addNopa(Integer pMetric) {
		this.nopa.add(pMetric);
	}
	
	public ArrayList<Integer> getDit() {
		return dit;
	}
	
	public void setDit(ArrayList<Integer> dit) {
		this.dit = dit;
	}
	
	public void addDit(Integer pMetric) {
		this.dit.add(pMetric);
	}
	
	public ArrayList<Integer> getNoc() {
		return noc;
	}
	
	public void setNoc(ArrayList<Integer> noc) {
		this.noc = noc;
	}
	
	public void addNoc(Integer pMetric) {
		this.noc.add(pMetric);
	}
	
	public ArrayList<Boolean> getIsGodClass() {
		return isGodClass;
	}
	
	public void setIsGodClass(ArrayList<Boolean> isGodClass) {
		this.isGodClass = isGodClass;
	}
	
	public void addIsGodClass(Boolean pIsBadSmell) {
		this.isGodClass.add(pIsBadSmell);
	}
	
	public ArrayList<Boolean> getIsComplexClass() {
		return isComplexClass;
	}
	
	public void setIsComplexClass(ArrayList<Boolean> isComplexClass) {
		this.isComplexClass = isComplexClass;
	}
	
	public void addIsComplexClass(Boolean pIsBadSmell) {
		this.isComplexClass.add(pIsBadSmell);
	}
	
	public ArrayList<Boolean> getIsSpaghettiCode() {
		return isSpaghettiCode;
	}
	
	public void setIsSpaghettiCode(ArrayList<Boolean> isSpaghettiCode) {
		this.isSpaghettiCode = isSpaghettiCode;
	}
	
	public void addIsSpaghettiCode(Boolean pIsBadSmell) {
		this.isSpaghettiCode.add(pIsBadSmell);
	}
	
	public ArrayList<Boolean> getIsClassDataShouldBePrivate() {
		return isClassDataShouldBePrivate;
	}
	
	public void setIsClassDataShouldBePrivate(
			ArrayList<Boolean> isClassDataShouldBePrivate) {
		this.isClassDataShouldBePrivate = isClassDataShouldBePrivate;
	}
	
	public void addIsCDSBP(Boolean pIsBadSmell) {
		this.isClassDataShouldBePrivate.add(pIsBadSmell);
	}
	
	public ArrayList<Boolean> getIsFunctionalDecomposition() {
		return isFunctionalDecomposition;
	}
	
	public void setIsFunctionalDecomposition(
			ArrayList<Boolean> isFunctionalDecomposition) {
		this.isFunctionalDecomposition = isFunctionalDecomposition;
	}
	
	public void addIsFunctionalDecomposition(Boolean pIsBadSmell) {
		this.isFunctionalDecomposition.add(pIsBadSmell);
	}
	
	public ArrayList<Long> getAuthorTime() {
		return authorTime;
	}
	
	public void setAuthorTime(ArrayList<Long> authorTime) {
		this.authorTime = authorTime;
	}
	
	public void addAuthorTime(Long pTime) {
		this.authorTime.add(pTime);
	}
	
	public ArrayList<Long> getCommitterTime() {
		return committerTime;
	}
	
	public void setCommitterTime(ArrayList<Long> committerTime) {
		this.committerTime = committerTime;
	}
	
	public void addCommitterTime(Long pTime) {
		this.committerTime.add(pTime);
	}
	
	public ArrayList<String> getCommitHash() {
		return commitHash;
	}
	
	public void setCommitHash(ArrayList<String> commitHash) {
		this.commitHash = commitHash;
	}
	
	public void addCommitHash(String pHash) {
		this.commitHash.add(pHash);
	}
}
