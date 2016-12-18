package it.unisa.scam14.testSmellRules;

import java.util.ArrayList;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.beans.TestClassBean;

public class MysteryGuest {

	public boolean isMysteryGuest(ClassBean pClassBean) {
		boolean mysteryGuest = false;

		for (MethodBean mb : pClassBean.getMethods()) {
			String body = mb.getTextContent();

			if (!mysteryGuest){
				if (body.contains(" File ") || body.contains(" File(") || body.contains("db")){
					mysteryGuest = true;
				}
			}
		}

		return mysteryGuest;
	}
	//follow instruction from http://xunitpatterns.com/Obscure%20Test.html#Mystery%20Guest
	
	/**
	 * Very arbitrary rules to detect external resources like file and database.
	 * <p>
	 * More robust and accurate rules needed
	 * @param pTestClassBean
	 * @return ArrayList<MethodBean> contains methods who are suspiciously using external resources like File and DB
	 */
	public Object getMysteryGuest(TestClassBean pTestClassBean) {
		Object rtObject = null;
		ClassBean pClassBean = pTestClassBean.getTestCase();
		boolean mysteryGuest = false;
		ArrayList<MethodBean> mgMethods = new ArrayList<MethodBean>();
		for (MethodBean mb : pClassBean.getMethods()) {
			String body = mb.getTextContent();
			if (body.contains(" File ") || body.contains(" File(") || body.contains("db")){
				mysteryGuest = true;
				mgMethods.add(mb);
			}
		}
		if (mysteryGuest) {
			rtObject = mgMethods;
		}
		return rtObject;
	}
}