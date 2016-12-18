package it.unisa.scam14.parser;

import it.unisa.scam14.beans.MethodBean;

public class InvocationParser {
	
	public static MethodBean parse(String pInvocationName) {
		MethodBean methodBean = new MethodBean();
		methodBean.setName(pInvocationName);
		return methodBean;
	}

}
