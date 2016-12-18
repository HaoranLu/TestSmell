package it.unisa.scam14.beans;

import java.util.ArrayList;

public class MethodArtifact {

	private String name;
	private ArrayList<Integer> loc = new ArrayList<Integer>();
	private ArrayList<Integer> nop = new ArrayList<Integer>();
	private ArrayList<Integer> cc = new ArrayList<Integer>();
	private ArrayList<Integer> cbo = new ArrayList<Integer>();
	private ArrayList<Boolean> isLongMethod = new ArrayList<Boolean>();
	
	private ArrayList<Long> authorTime = new ArrayList<Long>();
	private ArrayList<Long> committerTime = new ArrayList<Long>();
	private ArrayList<String> commitHash = new ArrayList<String>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addLoc(Integer pMetric) {
		this.loc.add(pMetric);
	}
	public ArrayList<Integer> getLoc() {
		return loc;
	}
	public void setLoc(ArrayList<Integer> loc) {
		this.loc = loc;
	}
	public ArrayList<Integer> getNop() {
		return nop;
	}
	public void setNop(ArrayList<Integer> nop) {
		this.nop = nop;
	}
	public void addNop(Integer pMetric) {
		this.nop.add(pMetric);
	}
	public ArrayList<Integer> getCc() {
		return cc;
	}
	public void setCc(ArrayList<Integer> cc) {
		this.cc = cc;
	}
	public void addCc(Integer pMetric) {
		this.cc.add(pMetric);
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
	public ArrayList<Boolean> getIsLongMethod() {
		return isLongMethod;
	}
	public void setIsLongMethod(ArrayList<Boolean> isLongMethod) {
		this.isLongMethod = isLongMethod;
	}
	public void addIsLongMethod(Boolean pIsBadSmell) {
		this.isLongMethod.add(pIsBadSmell);
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
