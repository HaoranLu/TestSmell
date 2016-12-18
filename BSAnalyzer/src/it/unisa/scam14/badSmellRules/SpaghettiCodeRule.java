package it.unisa.scam14.badSmellRules;

import java.util.Collection;
import java.util.Vector;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.metrics.CKMetrics;

/*
 * 
   RULE_CARD: SpaghettiCode {
	RULE: SpaghettiCode {INTER: NoInheritanceClassGlobalVariable LongMethodMethodNoParameter} 
	RULE: LongMethodMethodNoParameter {INTER LongMethod MethodNoParameter}
	RULE: LongMethod { (METRIC: METHOD_LOC, VERY_HIGH, 0) }
	RULE: MethodNoParameter { (STRUCT: METHOD_NO_PARAM) }
	RULE: NoInheritanceClassGlobalVariable {INTER NoInheritance ClassGlobalVariable}
	RULE: NoInheritance { (METRIC: DIT, INF_EQ, 2, 0) }
	RULE: ClassGlobalVariable {INTER ClassOneMethod FieldPrivate}
	RULE: ClassOneMethod { (STRUCT: GLOBAL_VARIABLE, 1) } };

 * 
 */
public class SpaghettiCodeRule {

	public boolean isSpaghettiCode(ClassBean pClass, String pSystemType) {
		Collection<MethodBean> methods = pClass.getMethods();

		if(pSystemType.equals("android")) {
			if(CKMetrics.getLOC(pClass) > 500) {
				if(hasLongMethodNoParameter(methods))
					return true;
			}
		} else {
			if(CKMetrics.getLOC(pClass) > 1000) {
				if(hasLongMethodNoParameter(methods))
					return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	private boolean hasNoInheritance(ClassBean pClass, Vector<ClassBean> pSystem) {
		int dit = CKMetrics.getDIT(pClass, pSystem, 0);

		if(dit == 0) 
			return true;

		return false;

	}

	private boolean hasLongMethodNoParameter(Collection<MethodBean> pMethods) {

		for(MethodBean methodBean: pMethods) {
			String[] tokenizedTextualContent = methodBean.getTextContent().split("\n");

			if( (tokenizedTextualContent.length > 100) || (methodBean.getParameters().size() == 0) )
				return true;
		}

		return false;
	}

}
