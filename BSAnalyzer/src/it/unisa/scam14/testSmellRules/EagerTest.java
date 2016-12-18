package it.unisa.scam14.testSmellRules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.beans.TestClassBean;

public class EagerTest {

	public boolean isEagerTest(ClassBean pClassBean, ClassBean pProductionClass) {
		boolean eagerTest = false;
		
		for (MethodBean mb : pClassBean.getMethods()) {
			if (!mb.getName().equals(pClassBean.getName())
					&& !mb.getName().toLowerCase()
					.equals("setup")
					&& !mb.getName().toLowerCase()
					.equals("teardown") && !eagerTest) {
				Vector<MethodBean> calledMethods = (Vector<MethodBean>) mb
						.getMethodCalls();



				if (calledMethods.size() > 1){
					Vector<MethodBean> cbMethods = (Vector<MethodBean>) pProductionClass.getMethods();
					int count = 0;
					for(MethodBean cm : calledMethods){
						for (MethodBean cbMethod : cbMethods){
							if (cbMethod.getName().toLowerCase().equals(cm.getName().toLowerCase())){
								count++;
							}
						}
					}

					if(count > 1)
						eagerTest = true;
				}
			}
		}

		return eagerTest;
	}//depend on name is not accurate since different can has method with same name
	
	/**
	 * Multiple production methods in one test method or Multiple production classes in one test class 
	 * <p>
	 * the returning object is quiet messy but still easy to understand.
	 * the mapping is {TestMethod:{Production class:[production method,...],Production Class:[production method,...]}, TestMethod:{...}}
	 * Please refer to the Json structure if have any trouble understanding it.
	 * @author Haoran Lu
	 * @param pTestClassBeanp contain one test class and related production classes
	 * @return null if no eagerTest detected, and HashMap<MethodBean, HashMap<ClassBean, ArrayList<MethodBean>>> will returned for the mapping
	 */
	public Object getEagerTest(TestClassBean pTestClassBean) {
		Object rtObject = null;
		boolean eagerTest = false;
		ArrayList<ClassBean> pProductionClasses = pTestClassBean.getProductionClasses();
		ClassBean pClassBean = pTestClassBean.getTestCase();
		//totalStat store all the mappings between testclass method and invoked production classes
		HashMap<MethodBean, HashMap<ClassBean, ArrayList<MethodBean>>> totalStat = new HashMap<MethodBean, HashMap<ClassBean,ArrayList<MethodBean>>>();
		for (MethodBean mb : pClassBean.getMethods()) {
			boolean multiClass = false;
			boolean multiMethod = false;
			//if the method is not constructor||setup||teardown
			if (!mb.getName().equals(pClassBean.getName())
					&& !mb.getName().toLowerCase().equals("setup")
					&& !mb.getName().toLowerCase().equals("teardown")) {
				Vector<MethodBean> calledMethods = (Vector<MethodBean>) mb.getMethodCalls();
				if (calledMethods.size() > 1) {
					HashMap<ClassBean, ArrayList<MethodBean>> methodStat = new HashMap<ClassBean, ArrayList<MethodBean>>();
					for(ClassBean pProductionClass: pProductionClasses) {
						Vector<MethodBean> cbMethods = (Vector<MethodBean>) pProductionClass.getMethods();
						ArrayList<MethodBean> pdStat = new ArrayList<MethodBean>();
				
						if (!pProductionClass.getName().equals(pClassBean.getName())){//if not testClass itself
							for(MethodBean cm : calledMethods){
								for (MethodBean cbMethod : cbMethods){
									if (cbMethod.getName().toLowerCase().equals(cm.getName().toLowerCase())){
										pdStat.add(cbMethod);
									}
								}
							}
						}
						
						if (pdStat.size() > 1) {
							multiMethod = true;//check multimethod after check one production class
							eagerTest = true;
						}
						if (pdStat.size() > 0) {
							methodStat.put(pProductionClass, pdStat);
						}
						
					}
					if (methodStat.size() > 1) {
						multiClass = true;//check multiclass after all check all production class
						eagerTest = true;
					}
					if (multiMethod || multiClass) {
						totalStat.put(mb, methodStat);
					}
				}
			}
		}
		if (eagerTest) {
			return totalStat;
		}
		return rtObject;
	}
}
