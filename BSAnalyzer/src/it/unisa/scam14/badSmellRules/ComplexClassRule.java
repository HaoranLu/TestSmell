package it.unisa.scam14.badSmellRules;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.metrics.CKMetrics;

public class ComplexClassRule {

	public boolean isComplexClass(ClassBean pClass, String pSystemType) {

		if(pSystemType.equals("android")) {
			if(CKMetrics.getWMC(pClass) > 100)
				return true;
		} else {
			if(CKMetrics.getWMC(pClass) > 200)
				return true;
		}

		return false;
	}
}