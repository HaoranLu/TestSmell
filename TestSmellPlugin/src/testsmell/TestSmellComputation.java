package testsmell;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.beans.TestClassBean;
import it.unisa.scam14.metrics.CKMetrics;
import it.unisa.scam14.metrics.TestSmellMetrics;
import it.unisa.scam14.testSmellRules.AssertionRoulette;
import it.unisa.scam14.testSmellRules.EagerTest;
import it.unisa.scam14.testSmellRules.GeneralFixture;
import it.unisa.scam14.testSmellRules.LazyTest;
import it.unisa.scam14.testSmellRules.MysteryGuest;
import it.unisa.scam14.testSmellRules.SensitiveEquality;

/**
 * This is a class copied from the BSAnalyzer computation. Just to gather all the TestSmell Rules.
 * @author Haoran Lu
 *
 */
public class TestSmellComputation {

	public AssertionRoulette assertionRoulette;
	public EagerTest eagerTest;
	public GeneralFixture generalFixture;
	public LazyTest lazyTest;
	public MysteryGuest mysteryGuest;
	public SensitiveEquality sensitiveEquality;
	
	public TestSmellComputation() {
		this.assertionRoulette = new AssertionRoulette();
		this.eagerTest = new EagerTest();
		this.generalFixture = new GeneralFixture();
		this.lazyTest = new LazyTest();
		this.mysteryGuest = new MysteryGuest();
		this.sensitiveEquality = new SensitiveEquality();
	}
	
	public void computeTestSmellRules(TestClassBean pTestClassBean) {

		ArrayList<ClassBean> productionClasses = pTestClassBean.getProductionClasses();
		ClassBean testClassBean = pTestClassBean.getTestCase();

		if((productionClasses != null) && (productionClasses.size() > 0)) {

			int numberOfAssert = TestSmellMetrics.getNumberOfAsserts(testClassBean);
			int sizeOfTextFixture = TestSmellMetrics.getSizeOfTextFixture(testClassBean);

			System.out.println("Is AssertionRoulette: " + this.assertionRoulette.isAssertionRoulette(testClassBean));
			ArrayList<String> ars = (ArrayList<String>)this.assertionRoulette.getAssertion(pTestClassBean);
			HashMap<MethodBean, HashMap<ClassBean, ArrayList<MethodBean>>> totalStat =
					(HashMap<MethodBean, HashMap<ClassBean, ArrayList<MethodBean>>>)this.eagerTest.getEagerTest(pTestClassBean);
			boolean isEagerTest = false;
			for(ClassBean productionClass: productionClasses) {
				isEagerTest = this.eagerTest.isEagerTest(testClassBean, productionClass);
				System.out.println("Is eager test: "+ isEagerTest);
				if(isEagerTest)
					break;
			}
			/*boolean isGeneralFixture = this.generalFixture.isGeneralFixture(testClassBean);
			boolean isLazyTest = false;
			for(ClassBean productionClass: productionClasses) {
				isLazyTest = this.lazyTest.isLazyTest(testClassBean, productionClass);

				if(isLazyTest)
					break;
			}
			boolean isMysteryGuest = this.mysteryGuest.isMysteryGuest(testClassBean);
			boolean isSensitiveEquality = this.sensitiveEquality.isSensitiveEquality(testClassBean);*/
		}
	}
}
