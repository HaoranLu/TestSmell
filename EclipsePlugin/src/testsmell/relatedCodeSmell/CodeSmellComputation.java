package testsmell.relatedCodeSmell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.unisa.scam14.badSmellRules.ClassDataShouldBePrivateRule;
import it.unisa.scam14.badSmellRules.ComplexClassRule;
import it.unisa.scam14.badSmellRules.FunctionalDecompositionRule;
import it.unisa.scam14.badSmellRules.GodClassRule;
import it.unisa.scam14.badSmellRules.LongMethodRule;
import it.unisa.scam14.badSmellRules.SpaghettiCodeRule;
import it.unisa.scam14.beans.ClassBean;
/**
 * Use Rules from BSAnalyzer and the Association rules to detect code smell.
 * <p>
 * Please be careful of the Association rules String. "SC","CDSBP", "BC"
 * @author Haoran Lu
 *
 */
public class CodeSmellComputation {

	public GodClassRule godClassRule;
	public ComplexClassRule complexClassRule;
	public SpaghettiCodeRule spaghettiCodeRule;
	public ClassDataShouldBePrivateRule classDataShouldBePrivateRule;
	public FunctionalDecompositionRule functionalDecompositionRule;
	public LongMethodRule longMethodRule;
	public String pSystemType;
	
	public CodeSmellComputation(){
		this.godClassRule = new GodClassRule();
		this.complexClassRule = new ComplexClassRule();
		this.spaghettiCodeRule = new SpaghettiCodeRule();
		this.classDataShouldBePrivateRule = new ClassDataShouldBePrivateRule();
		this.functionalDecompositionRule = new FunctionalDecompositionRule();
		this.longMethodRule = new LongMethodRule();
		this.pSystemType = "unknown";
	}
	/**
	 * Detect Code Smell in the list of Production classes
	 * @param productionClasses
	 * @param rule can get rules from smellSssociationRules.INSTANCE 
	 * @return null for no smell found and the key of the Map is code smell Type
	 */
	public Map<String, ClassBean> detectUseRule(ArrayList<ClassBean> productionClasses, String[] rule){
		Map<String, ClassBean> rtObject = new HashMap<>();
		int counter = rule.length;
		if(counter > 0){
			for (int i = 0; i < counter; i++) {
				String target = rule[i];
				for (ClassBean classBean : productionClasses) {
					if (target.equalsIgnoreCase("SC")) {
						if (this.spaghettiCodeRule.isSpaghettiCode(classBean, pSystemType)) {
							rtObject.put("SpaghettiCode", classBean);
						}
					}else if(target.equalsIgnoreCase("CDSBP")){
						if (this.classDataShouldBePrivateRule.isClassDataShouldBePrivate(classBean, pSystemType)) {
							rtObject.put("ClassDataShouldBePrivate", classBean);
						}
					}else if(target.equalsIgnoreCase("BC")){
						if (this.godClassRule.isGodClass(classBean, pSystemType)) {
							rtObject.put("Blob Class", classBean);
						}
					}
				}
			}
		}
		if (rtObject.size()>0) {
			return rtObject;
		}
		return null;
	}

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//CodeSmellComputation pp = new CodeSmellComputation();
		//boolean CDSP1 = pp.classDataShouldBePrivateRule.isClassDataShouldBePrivate(pClass, pSystemType)

	}

}
