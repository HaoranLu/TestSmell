package it.unisa.scam14.beans;

public class SelfAdmittedTechnicalDebt {

	private MethodBean correspondingMethod;
	private String admission;
	
	public MethodBean getCorrespondingMethod() {
		return correspondingMethod;
	}
	public void setCorrespondingMethod(MethodBean correspondingMethod) {
		this.correspondingMethod = correspondingMethod;
	}
	public String getAdmission() {
		return admission;
	}
	public void setAdmission(String admission) {
		this.admission = admission;
	}
}