package it.unisa.scam14.badSmellRules;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.metrics.CKMetrics;

/**
 * 
 * Implementation of DECOR's God Class Rule Card (reported in the following)
 * 
 * RULE_CARD: Blob {
	RULE: Blob {ASSOC: associated FROM: mainClass ONE
		TO: DataClass MANY}
	RULE: mainClass {UNION LargeClassLowCohesion ControllerClass}
	RULE: LargeClassLowCohesion {UNION LargeClass LowCohesion} 
	RULE: LargeClass {(METRIC: NMD + NAD, VERY_HIGH, 20)} 
	RULE: LowCohesion {(METRIC: LCOM5, VERY_HIGH, 20)}
	RULE: ControllerClass {UNION (SEMANTIC: METHODNAME,
		{Process, Control, Command, Manage, Drive, System}), (SEMANTIC: CLASSNAME, {Process, Control, Command, Manage, Drive, System}}
	RULE: DataClass {(STRUCT: METHOD_ACCESSOR, 90)} };
 * 
 * @author fabiopalomba
 *
 */
public class GodClassRule {

	public boolean isGodClass(ClassBean pClass, String pSystemType) {

		if(isControllerClass(pClass) || isLargeClassLowCohesion(pClass, pSystemType)) {
			return true;
		}

		return false;
	}

	private static boolean isLargeClassLowCohesion(ClassBean pClass, String pSystemType) {
		int featureSum = CKMetrics.getNOM(pClass) + CKMetrics.getNOA(pClass);

		if(pSystemType.equals("android")) {
			if( (CKMetrics.getLCOM(pClass) > 175) || (featureSum > 10)) {
				if(CKMetrics.getLOC(pClass) > 500)  
					return true;
			}
		} else {
			if( (CKMetrics.getLCOM(pClass) > 350) || (featureSum > 20)) {
				if(CKMetrics.getLOC(pClass) > 500)  
					return true;
			}
		}
		return false;
	}

	private static boolean isControllerClass(ClassBean pClass) {
		String pClassName = pClass.getName().toLowerCase();

		if( (pClassName.contains("process")) || (pClassName.contains("control") || pClassName.contains("command") 
				|| pClassName.contains("manage") || pClassName.contains("drive") || pClassName.contains("system"))) {
			int featureSum = CKMetrics.getNOM(pClass) + CKMetrics.getNOA(pClass);

			if( (CKMetrics.getLCOM(pClass) > 350) || (featureSum > 20)) {
				if(CKMetrics.getLOC(pClass) > 500)  
					return true;
			}
		}

		return false;
	}
}