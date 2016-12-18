package it.unisa.scam14.badSmellRules;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.metrics.CKMetrics;

public class ClassDataShouldBePrivateRule {

	public boolean isClassDataShouldBePrivate(ClassBean pClass, String pSystemType) {
		
		if(CKMetrics.getNOPA(pClass) > 10)
			return true;
		
		return false;
	}
}
