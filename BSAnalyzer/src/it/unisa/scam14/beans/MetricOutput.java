package it.unisa.scam14.beans;

import java.util.ArrayList;
import java.util.List;

public class MetricOutput {

	private List<String> blob;
	private List<String> cdsbp;
	private List<String> complexClass;
	private List<String> longMethod;
	private List<String> spaghettiCode;
	
	public MetricOutput() {
		this.blob=new ArrayList<String>();
		this.cdsbp=new ArrayList<String>();
		this.complexClass=new ArrayList<String>();
		this.longMethod=new ArrayList<String>();
		this.spaghettiCode=new ArrayList<String>();
	}
	
	public List<String> getBlob() {
		return blob;
	}
	public void setBlob(List<String> blob) {
		this.blob = blob;
	}
	public List<String> getCdsbp() {
		return cdsbp;
	}
	public void setCdsbp(List<String> cdsbp) {
		this.cdsbp = cdsbp;
	}
	public List<String> getComplexClass() {
		return complexClass;
	}
	public void setComplexClass(List<String> complexClass) {
		this.complexClass = complexClass;
	}
	public List<String> getLongMethod() {
		return longMethod;
	}
	public void setLongMethod(List<String> longMethod) {
		this.longMethod = longMethod;
	}
	public List<String> getSpaghettiCode() {
		return spaghettiCode;
	}
	public void setSpaghettiCode(List<String> spaghettiCode) {
		this.spaghettiCode = spaghettiCode;
	}
	public void addBlob(String pLine) {
		this.blob.add(pLine);
	}
	public void addCdsbp(String pLine) {
		this.cdsbp.add(pLine);
	}
	public void addComplexClass(String pLine) {
		this.complexClass.add(pLine);
	}
	public void addLongMethod(String pLine) {
		this.longMethod.add(pLine);
	}
	public void addSpaghettiCode(String pLine) {
		this.spaghettiCode.add(pLine);
	}
}
