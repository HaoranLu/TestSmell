package testsmell;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.TestClassBean;
import it.unisa.scam14.testSmellRules.AssertionRoulette;
import it.unisa.scam14.utility.FolderToJavaProjectConverter;
import it.unisa.scam14.utility.TestSmellUtilities;

public class DetectionExample {
	File pWorkingDirectory = null;
	File pSelectedDirectory = null;
	List<ClassBean> testCasesClasses = null;
	Vector<ClassBean> system = null;
	List<TestClassBean> testsCases = null;
	HashMap<TestClassBean, Object> testSmellResult = null;
	public DetectionExample(File pWorkingDirectory, File pSelectedDirectory) {
		this.pWorkingDirectory = pWorkingDirectory;
		this.pSelectedDirectory = pSelectedDirectory;
		this.testCasesClasses = new ArrayList<>();
		this.testsCases = new ArrayList<TestClassBean>();
		this.system = new Vector<ClassBean>();
		this.testSmellResult = new HashMap<>();
	}
	public void prepareForDetection(IProgressMonitor monitor) {
		List<ClassBean> tcc = new ArrayList<>();
		Vector<ClassBean> sys = new Vector<>();
		List<TestClassBean> tcs = new ArrayList<>();
		if (monitor != null) {
			monitor.beginTask("Extracting Test Classes", pSelectedDirectory.listFiles().length);
			/*for (File ff : pSelectedDirectory.listFiles()) {
				tcc.addAll(TestSmellUtilities.extractTestCases(ff));
				monitor.worked(1);
			}*/
			tcc = TestSmellUtilities.extractTestCases(this.pSelectedDirectory);
			this.testCasesClasses.addAll(tcc);
			monitor.done();
		}
		if (monitor != null) {
			monitor.beginTask("Extracting all production classes, this may take a little bit longer..", pWorkingDirectory.listFiles().length);
			sys = FolderToJavaProjectConverter.extractClasses(pWorkingDirectory.getAbsolutePath());
			/*for (File ff : pWorkingDirectory.listFiles()) {
				if(monitor != null && monitor.isCanceled())
	    			throw new OperationCanceledException();
				System.out.println(ff.toString());
				sys.addAll(FolderToJavaProjectConverter.extractClasses(ff.getAbsolutePath()));
				monitor.worked(1);
			}*/
			this.system.addAll(sys);
			monitor.done();
		}
		if (monitor != null) {
			monitor.beginTask("Constructing Test Cases", tcc.size());
			for (ClassBean tc : tcc) {
				Collection<ClassBean> productionClasses = TestSmellUtilities.findProductionClassUsingDependencies(tc, sys);
				tcs.add(new TestClassBean(tc, new ArrayList<ClassBean>(productionClasses)));
				monitor.worked(1);
			}
			this.testsCases.addAll(tcs);
			monitor.done();
		}

	}
	public void getDetectionResult(IProgressMonitor monitor) {
		HashMap<TestClassBean, Object> result = new HashMap<>();
		TestSmellComputation metrics = new TestSmellComputation();
		if (monitor != null) {
			for (TestClassBean tClassBean : testsCases) {
				if (monitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				//metrics.computeTestSmellRules(tClassBean);
				HashMap<String, Object> tmp = new HashMap<>();
				Object AR = metrics.assertionRoulette.getAssertion(tClassBean);
				monitor.worked(1);
				Object ET = metrics.eagerTest.getEagerTest(tClassBean);
				monitor.worked(1);
				Object MG = metrics.mysteryGuest.getMysteryGuest(tClassBean);
				monitor.worked(1);
				Object GF = metrics.generalFixture.getGeneralFixture(tClassBean);
				monitor.worked(1);
				Object SE = metrics.sensitiveEquality.getSensitiveEquality(tClassBean);
				monitor.worked(1);
				if (AR != null) {
					tmp.put("AssertationRoulette",AR);
				}
				if (ET != null) {
					tmp.put("EagerTest", ET);
				}
				if (MG != null){
					tmp.put("MysteryGuest", MG);
				}
				if (GF != null) {
					tmp.put("GeneralFixture", GF);
				}
				if (SE != null) {
					tmp.put("SensitiveEquality", SE);
				}
				if (ET !=null || AR != null || MG !=null || GF != null || SE != null) {
					result.put(tClassBean, tmp);
				}
				System.out.println(result);
				monitor.worked(1);
			}
			this.testSmellResult.putAll(result);
			monitor.done();
		}
	}
	public void excuteDetectionExample() {
		//Extract all the test classes form a directory containing Java files, directory with granularity of package or project
		List<ClassBean> testCasesClasses = TestSmellUtilities.extractTestCases(this.pSelectedDirectory);
		//Extract all system classes, regardless of the granularity.
		System.out.println("Starting extract all classes");
		Vector<ClassBean> system = FolderToJavaProjectConverter.extractClasses(this.pWorkingDirectory.getAbsolutePath());
		System.out.println("finished");
		//Extract production classes,For each Test Class extracted, the code will find the list of associated production classes.
		//Next, it will embed these information (Test Class and Production Classes) into a new TestClassBean.
		List<TestClassBean> testsCases = new ArrayList<TestClassBean>();
		for(ClassBean tc : testCasesClasses){
		   Collection<ClassBean> productionClasses = TestSmellUtilities.findProductionClassUsingDependencies(tc, system);
		   testsCases.add(new TestClassBean(tc, new ArrayList<ClassBean>(productionClasses)));
		}
		//identify test smells
		TestSmellComputation metrics = new TestSmellComputation();
		for (TestClassBean tClassBean : testsCases) {
			metrics.computeTestSmellRules(tClassBean);
			System.out.println(metrics.assertionRoulette.getAssertion(tClassBean));
			System.out.println(metrics.eagerTest.getEagerTest(tClassBean));
		}

	}
	public static void main(String[] args) {
		File projectfile = new File("/Users/luhaoran/Prpjects/jackrabbit/jackrabbit-core");
		///Users/luhaoran/Prpjects/jackrabbit/jackrabbit-core/src/main/java/org/apache/jackrabbit/core/
		///Users/luhaoran/Prpjects/jackrabbit/jackrabbit-core/src/main/java/org/apache/jackrabbit/core/
		System.out.println(projectfile.exists());
		File testclass = new File("/Users/luhaoran/Prpjects/jackrabbit/jackrabbit-core/src/test/java/org/apache/jackrabbit/core/config/");
		System.out.println(testclass.exists());
		DetectionExample test = new DetectionExample(projectfile, testclass);
		test.excuteDetectionExample();
		
	}
	public HashMap<TestClassBean, Object> getTestSmellResult() {
		return testSmellResult;
	}
	public void setTestSmellResult(HashMap<TestClassBean, Object> testSmellResult) {
		this.testSmellResult = testSmellResult;
	}
	

}
