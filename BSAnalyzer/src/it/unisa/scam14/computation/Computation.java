package it.unisa.scam14.computation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;

import it.unisa.scam14.badSmellRules.ClassDataShouldBePrivateRule;
import it.unisa.scam14.badSmellRules.ComplexClassRule;
import it.unisa.scam14.badSmellRules.FunctionalDecompositionRule;
import it.unisa.scam14.badSmellRules.GodClassRule;
import it.unisa.scam14.badSmellRules.LongMethodRule;
import it.unisa.scam14.badSmellRules.SpaghettiCodeRule;
import it.unisa.scam14.beans.ClassArtifact;
import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.MethodArtifact;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.beans.PackageBean;
import it.unisa.scam14.beans.TestClassBean;
import it.unisa.scam14.metrics.CKMetrics;
import it.unisa.scam14.metrics.TestSmellMetrics;
import it.unisa.scam14.testSmellRules.AssertionRoulette;
import it.unisa.scam14.testSmellRules.EagerTest;
import it.unisa.scam14.testSmellRules.GeneralFixture;
import it.unisa.scam14.testSmellRules.LazyTest;
import it.unisa.scam14.testSmellRules.MysteryGuest;
import it.unisa.scam14.testSmellRules.SensitiveEquality;
import it.unisa.scam14.utility.FileUtility;
import it.unisa.scam14.utility.FolderToJavaProjectConverter;

public class Computation {
	private GodClassRule godClassRule;
	private ComplexClassRule complexClassRule;
	private SpaghettiCodeRule spaghettiCodeRule;
	private ClassDataShouldBePrivateRule classDataShouldBePrivateRule;
	private FunctionalDecompositionRule functionalDecompositionRule;
	private LongMethodRule longMethodRule;

	private AssertionRoulette assertionRoulette;
	private EagerTest eagerTest;
	private GeneralFixture generalFixture;
	private LazyTest lazyTest;
	private MysteryGuest mysteryGuest;
	private SensitiveEquality sensitiveEquality;

	public Computation() {
		this.godClassRule = new GodClassRule();
		this.complexClassRule = new ComplexClassRule();
		this.spaghettiCodeRule = new SpaghettiCodeRule();
		this.classDataShouldBePrivateRule = new ClassDataShouldBePrivateRule();
		this.longMethodRule = new LongMethodRule();
		this.functionalDecompositionRule = new FunctionalDecompositionRule();

		this.assertionRoulette = new AssertionRoulette();
		this.eagerTest = new EagerTest();
		this.generalFixture = new GeneralFixture();
		this.lazyTest = new LazyTest();
		this.mysteryGuest = new MysteryGuest();
		this.sensitiveEquality = new SensitiveEquality();
	}

	public void computeMethodLevelSmellRules(Vector<PackageBean> pPackages, String pOutputPath, int pCommitNumber,
			long pAuthorTime, long pCommitterTime, String pCommitHash) {

		String longMethodFile="methodName;isLongMethod;LOC;NOP;CC;CBO;author-time;committer-time;commit-hash\n";
		//	String featureEnvyFile="methodName;targetClass;isFeatureEnvy;METHOD-LOC;METHOD-NOP;METHOD-CC;METHOD-CBO;CLASS-LOC;CLASS-LCOM;CLASS-WMC;CLASS-RFC;CLASS-CBO;CLASS-NOM;CLASS-NOA;CLASS-DIT;CLASS-NOC;DEP-MBC;DEP-MTC;SIM-MBC;SIM-MTC\n";

		Vector<ClassBean> system = new Vector<ClassBean>();

		for(PackageBean packageBean: pPackages) {
			for(ClassBean classBean: packageBean.getClasses()) {
				system.add(classBean);
			}
		}

		for(PackageBean packageBean: pPackages) {
			for(ClassBean classBean: packageBean.getClasses()) {
				for(MethodBean methodBean: classBean.getMethods()) {
					int loc = methodBean.getTextContent().split("\n").length;
					int nop = methodBean.getParameters().size();
					int cc = CKMetrics.getMcCabeCycloComplexity(methodBean);
					int cbo = methodBean.getMethodCalls().size();
					boolean isLongMethod = this.longMethodRule.isLongMethod(methodBean);

					longMethodFile+=packageBean.getName()+"."+classBean.getName()+"."+methodBean.getName()+";"+isLongMethod+";"+loc+";"+nop+";"+cc+";"+cbo+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash+"\n";

					/*for(PackageBean targetPackage: pPackages) {
						for(ClassBean targetClass: targetPackage.getClasses()) {
							boolean isFeatureEnvy=this.featureEnvyRule.isFeatureEnvy(methodBean, classBean, targetClass);
							int dependenciesWithCandidateEnvyClass = computeDependencies(methodBean, targetClass);
							int dependenciesWithBelongingClass = computeDependencies(methodBean, classBean);

							double similarityWithCandidateEnvyPackage = computeSimilarity(methodBean, targetClass);
							double similarityWithBelongingPackage = computeSimilarity(methodBean, classBean);

							int classLoc = CKMetrics.getLOC(classBean);
							int lcom = CKMetrics.getLCOM(classBean);
							int wmc = CKMetrics.getWMC(classBean);
							int rfc = CKMetrics.getRFC(classBean);
							int classCbo = CKMetrics.getCBO(classBean);
							int nom = CKMetrics.getNOM(classBean);
							int noa = CKMetrics.getNOA(classBean);
							int dit = CKMetrics.getDIT(classBean, system, 0);
							int noc = CKMetrics.getNOC(classBean, system);

							featureEnvyFile+=packageBean.getName()+"."+classBean.getName()+"."+methodBean.getName()+";"+
									targetPackage.getName()+"."+targetClass.getName()+";"+isFeatureEnvy+";"+loc+";"+nop+";"+cc+";"+cbo+";"+
									classLoc+";"+lcom+";"+wmc+";"+rfc+";"+classCbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+
									dependenciesWithBelongingClass+";"+dependenciesWithCandidateEnvyClass+";"+
									similarityWithBelongingPackage+";"+similarityWithCandidateEnvyPackage+"\n";
						}
					}*/
				}

			}
		}

		FileUtility.writeFile(longMethodFile, pOutputPath+"/longMethod_"+pCommitNumber+".csv");
		//FileUtility.writeFile(featureEnvyFile, pOutputPath+"/featureEnvy_"+pCommitNumber+".csv");

	}

	public void computeMethodLevelSmellRules(Vector<PackageBean> pPackages, int pCommitNumber,
			long pAuthorTime, long pCommitterTime, String pCommitHash, ArrayList<MethodArtifact> pArtifacts,
			ArrayList<String> pPaths) {

		Vector<ClassBean> system = new Vector<ClassBean>();

		for(PackageBean packageBean: pPackages) {
			for(ClassBean classBean: packageBean.getClasses()) {
				system.add(classBean);
			}
		}

		for(PackageBean packageBean: pPackages) {
			for(ClassBean classBean: packageBean.getClasses()) {
				for(MethodBean methodBean: classBean.getMethods()) {
					int loc = methodBean.getTextContent().split("\n").length;
					int nop = methodBean.getParameters().size();
					int cc = CKMetrics.getMcCabeCycloComplexity(methodBean);
					int cbo = methodBean.getMethodCalls().size();
					boolean isLongMethod = this.longMethodRule.isLongMethod(methodBean);

					int index = Collections.binarySearch(pPaths,methodBean.getName());

					if(index >= 0) {
						MethodArtifact artifact = pArtifacts.get(index);

						artifact.addCbo(cbo);
						artifact.addCc(cc);
						artifact.addLoc(loc);
						artifact.addNop(nop);
						artifact.addIsLongMethod(isLongMethod);

						artifact.addCommitHash(pCommitHash);
						artifact.addAuthorTime(pAuthorTime);
						artifact.addCommitterTime(pCommitterTime);

					} else {

						MethodArtifact artifact = new MethodArtifact();

						artifact.setName(packageBean.getName()+"."+classBean.getName()+"."+methodBean.getName());
						artifact.addCbo(cbo);
						artifact.addCc(cc);
						artifact.addLoc(loc);
						artifact.addNop(nop);
						artifact.addIsLongMethod(isLongMethod);

						artifact.addCommitHash(pCommitHash);
						artifact.addAuthorTime(pAuthorTime);
						artifact.addCommitterTime(pCommitterTime);

						pArtifacts.add((-index)-1, artifact);
						pPaths.add((-index)-1, artifact.getName());

					}
				}

			}
		}
	}

	public void computeTestSmellRules(TestClassBean pTestClassBean, String pOutputPath,
			long pAuthorTime, long pCommitterTime, String pCommitHash, String pSystemType) {

		ArrayList<ClassBean> productionClasses = pTestClassBean.getProductionClasses();
		ClassBean testClassBean = pTestClassBean.getTestCase();

		if((productionClasses != null) && (productionClasses.size() > 0)) {

			File assertionRoulette = new File(pOutputPath+"/"+testClassBean.getFilePath() + "_assertionRoulette_trend.csv");
			File eagerTest = new File(pOutputPath+"/"+testClassBean.getFilePath()+"_eagerTest_trend.csv");
			File generalFixture = new File(pOutputPath+"/"+testClassBean.getFilePath()+"_generalFixture_trend.csv");
			File lazyTest = new File(pOutputPath+"/"+testClassBean.getFilePath() + "_lazyTest_trend.csv");
			File mysteryGuest = new File(pOutputPath+"/"+testClassBean.getFilePath() + "_mysteryGuest_trend.csv");
			File sensitiveEquality = new File(pOutputPath+"/"+testClassBean.getFilePath() + "_sensitiveEquality_trend.csv");
			
			int numberOfAssert = TestSmellMetrics.getNumberOfAsserts(testClassBean);
			int sizeOfTextFixture = TestSmellMetrics.getSizeOfTextFixture(testClassBean);

			int testCaseLoc = CKMetrics.getLOC(testClassBean);
			int testCaseLCOM = CKMetrics.getLCOM(testClassBean);
			int testCaseWMC = CKMetrics.getWMC(testClassBean);
			int testCaseRFC = CKMetrics.getRFC(testClassBean);
			int testCaseCBO = CKMetrics.getCBO(testClassBean);
			int testCaseNOM = CKMetrics.getNOM(testClassBean);
			int testCaseNOA = CKMetrics.getNOA(testClassBean);
			int testCaseNOPA = CKMetrics.getNOPA(testClassBean);

			boolean isAssertionRoulette = this.assertionRoulette.isAssertionRoulette(testClassBean);

			boolean isEagerTest = false;

			for(ClassBean productionClass: productionClasses) {
				isEagerTest = this.eagerTest.isEagerTest(testClassBean, productionClass);

				if(isEagerTest)
					break;
			}

			boolean isGeneralFixture = this.generalFixture.isGeneralFixture(testClassBean);

			boolean isLazyTest = false;

			for(ClassBean productionClass: productionClasses) {
				isLazyTest = this.lazyTest.isLazyTest(testClassBean, productionClass);

				if(isLazyTest)
					break;
			}

			boolean isMysteryGuest = this.mysteryGuest.isMysteryGuest(testClassBean);
			boolean isSensitiveEquality = this.sensitiveEquality.isSensitiveEquality(testClassBean);

			writeFile(assertionRoulette, testClassBean.getBelongingPackage()+"."+testClassBean.getName()+";"+isAssertionRoulette+";"+numberOfAssert+";"+sizeOfTextFixture+";"+testCaseLoc+";"+testCaseLCOM+";"+testCaseWMC+";"+testCaseRFC+";"+testCaseCBO+";"+testCaseNOM+";"+testCaseNOA+";"+testCaseNOPA+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
			writeFile(eagerTest, testClassBean.getBelongingPackage()+"."+testClassBean.getName()+";"+isEagerTest+";"+numberOfAssert+";"+sizeOfTextFixture+";"+testCaseLCOM+";"+testCaseWMC+";"+testCaseRFC+";"+testCaseCBO+";"+testCaseNOM+";"+testCaseNOA+";"+testCaseNOPA+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
			writeFile(generalFixture, testClassBean.getBelongingPackage()+"."+testClassBean.getName()+";"+isGeneralFixture+";"+numberOfAssert+";"+sizeOfTextFixture+";"+testCaseLCOM+";"+testCaseWMC+";"+testCaseRFC+";"+testCaseCBO+";"+testCaseNOM+";"+testCaseNOA+";"+testCaseNOPA+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
			writeFile(lazyTest, testClassBean.getBelongingPackage()+"."+testClassBean.getName()+";"+isLazyTest+";"+numberOfAssert+";"+sizeOfTextFixture+";"+testCaseLCOM+";"+testCaseWMC+";"+testCaseRFC+";"+testCaseCBO+";"+testCaseNOM+";"+testCaseNOA+";"+testCaseNOPA+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
			writeFile(mysteryGuest, testClassBean.getBelongingPackage()+"."+testClassBean.getName()+";"+isMysteryGuest+";"+numberOfAssert+";"+sizeOfTextFixture+";"+testCaseLCOM+";"+testCaseWMC+";"+testCaseRFC+";"+testCaseCBO+";"+testCaseNOM+";"+testCaseNOA+";"+testCaseNOPA+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
			writeFile(sensitiveEquality, testClassBean.getBelongingPackage()+"."+testClassBean.getName()+";"+isSensitiveEquality+";"+numberOfAssert+";"+sizeOfTextFixture+";"+testCaseLCOM+";"+testCaseWMC+";"+testCaseRFC+";"+testCaseCBO+";"+testCaseNOM+";"+testCaseNOA+";"+testCaseNOPA+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);

		}
	}

	/*public void computeTestSmellRules(Vector<PackageBean> pPackages, String pOutputPath, int pCommitNumber,
			long pAuthorTime, long pCommitterTime, String pCommitHash, String pSystemType, Collection<Commit> pHistory) {

		Vector<ClassBean> system = new Vector<ClassBean>();

		for(PackageBean packageBean: pPackages) {
			for(ClassBean classBean: packageBean.getClasses()) {
				system.add(classBean);
			}
		}

		for(PackageBean packageBean: pPackages) {
			for(ClassBean testClassBean: packageBean.getClasses()) {
				if(TestSmellUtilities.isTestCase(testClassBean)) {
					ClassBean productionClass = TestSmellUtilities.findProductionClass(testClassBean, system);

					if(productionClass!=null) {

						File assertionRoulette = new File(pOutputPath+"/"+packageBean.getName() + "." + testClassBean.getName()+"_assertionRoulette_trend.csv");
						File eagerTest = new File(pOutputPath+"/"+packageBean.getName() + "." + testClassBean.getName()+"_eagerTest_trend.csv");
						File generalFixture = new File(pOutputPath+"/"+packageBean.getName() + "." + testClassBean.getName()+"_generalFixture_trend.csv");
						File lazyTest = new File(pOutputPath+"/"+packageBean.getName() + "." + testClassBean.getName()+"_lazyTest_trend.csv");
						File mysteryGuest = new File(pOutputPath+"/"+packageBean.getName() + "." + testClassBean.getName()+"_mysteryGuest_trend.csv");
						File sensitiveEquality = new File(pOutputPath+"/"+packageBean.getName() + "." + testClassBean.getName()+"_sensitiveEquality_trend.csv");

						int numberOfAssert = TestSmellMetrics.getNumberOfAsserts(testClassBean);
						int sizeOfTextFixture = TestSmellMetrics.getSizeOfTextFixture(testClassBean);
						int numberOfChanges = TestSmellMetrics.getNumberOfChanges(testClassBean, pCommitterTime, pHistory);
						int numberOfRefactorings = TestSmellMetrics.getNumberOfRefactoring(testClassBean, pCommitterTime, pHistory);
						int numberOfBugFixings = TestSmellMetrics.getNumberOfBugFixing(testClassBean, pCommitterTime, pHistory);

						int testCaseLoc = CKMetrics.getLOC(testClassBean);
						int testCaseLCOM = CKMetrics.getLCOM(testClassBean);
						int testCaseWMC = CKMetrics.getWMC(testClassBean);
						int testCaseRFC = CKMetrics.getRFC(testClassBean);
						int testCaseCBO = CKMetrics.getCBO(testClassBean);
						int testCaseNOM = CKMetrics.getNOM(testClassBean);
						int testCaseNOA = CKMetrics.getNOA(testClassBean);
						int testCaseNOPA = CKMetrics.getNOPA(testClassBean);

						int loc = CKMetrics.getLOC(productionClass);
						int lcom = CKMetrics.getLCOM(productionClass);
						int wmc = CKMetrics.getWMC(productionClass);
						int rfc = CKMetrics.getRFC(productionClass);
						int cbo = CKMetrics.getCBO(productionClass);
						int nom = CKMetrics.getNOM(productionClass);
						int noa = CKMetrics.getNOA(productionClass);
						int nopa = CKMetrics.getNOPA(productionClass);
						int dit = CKMetrics.getDIT(productionClass, system, 0);
						int noc = CKMetrics.getNOC(productionClass, system);

						boolean isAssertionRoulette = this.assertionRoulette.isAssertionRoulette(testClassBean);
						boolean isEagerTest = this.eagerTest.isEagerTest(testClassBean, system);
						boolean isGeneralFixture = this.generalFixture.isGeneralFixture(testClassBean);
						boolean isLazyTest = this.lazyTest.isLazyTest(testClassBean, system);
						boolean isMysteryGuest = this.mysteryGuest.isMysteryGuest(testClassBean);
						boolean isSensitiveEquality = this.sensitiveEquality.isSensitiveEquality(testClassBean);

						boolean isGodClass = this.godClassRule.isGodClass(productionClass, pSystemType);
						boolean isComplexClass = this.complexClassRule.isComplexClass(productionClass, pSystemType);
						boolean isSpaghettiCode = this.spaghettiCodeRule.isSpaghettiCode(productionClass, system, pSystemType);
						boolean isClassDataShouldBePrivate = this.classDataShouldBePrivateRule.isClassDataShouldBePrivate(productionClass, pSystemType);
						boolean isFunctionalDecomposition = this.functionalDecompositionRule.isFunctionalDecomposition(productionClass, pSystemType);

						writeFile(assertionRoulette, packageBean.getName()+"."+testClassBean.getName()+";"+isAssertionRoulette+";"+numberOfAssert+";"+sizeOfTextFixture+";"+numberOfChanges+";"+numberOfRefactorings+";"+numberOfBugFixings+";"+testCaseLoc+";"+testCaseLCOM+";"+testCaseWMC+";"+testCaseRFC+";"+testCaseCBO+";"+testCaseNOM+";"+testCaseNOA+";"+testCaseNOPA+";"+productionClass.getBelongingPackage()+"."+productionClass.getName()+";"+isGodClass+";"+isComplexClass+";"+isClassDataShouldBePrivate+";"+isFunctionalDecomposition+";"+isSpaghettiCode+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
						writeFile(eagerTest, packageBean.getName()+"."+testClassBean.getName()+";"+isEagerTest+";"+numberOfAssert+";"+sizeOfTextFixture+";"+numberOfChanges+";"+numberOfRefactorings+";"+numberOfBugFixings+";"+testCaseLCOM+";"+testCaseWMC+";"+testCaseRFC+";"+testCaseCBO+";"+testCaseNOM+";"+testCaseNOA+";"+testCaseNOPA+";"+productionClass.getBelongingPackage()+"."+productionClass.getName()+";"+isGodClass+";"+isComplexClass+";"+isClassDataShouldBePrivate+";"+isFunctionalDecomposition+";"+isSpaghettiCode+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
						writeFile(generalFixture, packageBean.getName()+"."+testClassBean.getName()+";"+isGeneralFixture+";"+numberOfAssert+";"+sizeOfTextFixture+";"+numberOfChanges+";"+numberOfRefactorings+";"+numberOfBugFixings+";"+testCaseLCOM+";"+testCaseWMC+";"+testCaseRFC+";"+testCaseCBO+";"+testCaseNOM+";"+testCaseNOA+";"+testCaseNOPA+";"+productionClass.getBelongingPackage()+"."+productionClass.getName()+";"+isGodClass+";"+isComplexClass+";"+isClassDataShouldBePrivate+";"+isFunctionalDecomposition+";"+isSpaghettiCode+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
						writeFile(lazyTest, packageBean.getName()+"."+testClassBean.getName()+";"+isLazyTest+";"+numberOfAssert+";"+sizeOfTextFixture+";"+numberOfChanges+";"+numberOfRefactorings+";"+numberOfBugFixings+";"+testCaseLCOM+";"+testCaseWMC+";"+testCaseRFC+";"+testCaseCBO+";"+testCaseNOM+";"+testCaseNOA+";"+testCaseNOPA+";"+productionClass.getBelongingPackage()+"."+productionClass.getName()+";"+isGodClass+";"+isComplexClass+";"+isClassDataShouldBePrivate+";"+isFunctionalDecomposition+";"+isSpaghettiCode+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+nopa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
						writeFile(mysteryGuest, packageBean.getName()+"."+testClassBean.getName()+";"+isMysteryGuest+";"+numberOfAssert+";"+sizeOfTextFixture+";"+numberOfChanges+";"+numberOfRefactorings+";"+numberOfBugFixings+";"+testCaseLCOM+";"+testCaseWMC+";"+testCaseRFC+";"+testCaseCBO+";"+testCaseNOM+";"+testCaseNOA+";"+testCaseNOPA+";"+productionClass.getBelongingPackage()+"."+productionClass.getName()+";"+isGodClass+";"+isComplexClass+";"+isClassDataShouldBePrivate+";"+isFunctionalDecomposition+";"+isSpaghettiCode+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
						writeFile(sensitiveEquality, packageBean.getName()+"."+testClassBean.getName()+";"+isSensitiveEquality+";"+numberOfAssert+";"+sizeOfTextFixture+";"+numberOfChanges+";"+numberOfRefactorings+";"+numberOfBugFixings+";"+testCaseLCOM+";"+testCaseWMC+";"+testCaseRFC+";"+testCaseCBO+";"+testCaseNOM+";"+testCaseNOA+";"+testCaseNOPA+";"+productionClass.getBelongingPackage()+"."+productionClass.getName()+";"+isGodClass+";"+isComplexClass+";"+isClassDataShouldBePrivate+";"+isFunctionalDecomposition+";"+isSpaghettiCode+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);

					}
				}
			}
		}
	}*/

	/**
	 * This method does NOT consider inheritance metrics, such as NOC and DIT.
	 */
	public void computeClassLevelSmellRules(ArrayList<ClassBean> pClassesToAnalyze, String pOutputPath, 
			int pCommitNumber,long pAuthorTime, long pCommitterTime, String pCommitHash, String pSystemType) {

		for(ClassBean classBean: pClassesToAnalyze) {
			File blob = new File(pOutputPath+"/"+classBean.getFilePath()+"_blob_trend.csv");
			File cdsbp = new File(pOutputPath+"/"+classBean.getFilePath()+"_cdsbp_trend.csv");
			File cc = new File(pOutputPath+"/"+classBean.getFilePath()+"_complexClass_trend.csv");
			File fd = new File(pOutputPath+"/"+classBean.getFilePath()+"_fd_trend.csv");
			File sc = new File(pOutputPath+"/"+classBean.getFilePath()+"_spaghettiCode_trend.csv");

			int loc = CKMetrics.getLOC(classBean);
			int lcom = CKMetrics.getLCOM(classBean);
			int wmc = CKMetrics.getWMC(classBean);
			int rfc = CKMetrics.getRFC(classBean);
			int cbo = CKMetrics.getCBO(classBean);
			int nom = CKMetrics.getNOM(classBean);
			int noa = CKMetrics.getNOA(classBean);
			int nopa = CKMetrics.getNOPA(classBean);
			
			boolean isGodClass = this.godClassRule.isGodClass(classBean, pSystemType);
			boolean isComplexClass = this.complexClassRule.isComplexClass(classBean, pSystemType);
			boolean isSpaghettiCode = this.spaghettiCodeRule.isSpaghettiCode(classBean, pSystemType);
			boolean isClassDataShouldBePrivate = this.classDataShouldBePrivateRule.isClassDataShouldBePrivate(classBean, pSystemType);
			boolean isFunctionalDecomposition = this.functionalDecompositionRule.isFunctionalDecomposition(classBean, pSystemType);

			writeFile(blob, classBean.getFilePath()+";"+isGodClass+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
			writeFile(cc, classBean.getFilePath()+";"+isComplexClass+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
			writeFile(sc, classBean.getFilePath()+";"+isSpaghettiCode+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
			writeFile(cdsbp, classBean.getFilePath()+";"+isClassDataShouldBePrivate+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+nopa+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
			writeFile(fd, classBean.getFilePath()+";"+isFunctionalDecomposition+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);

		}
	}
	
	/**
	 * This method considers all the quality metrics. It retrieves the packages of the system by parsing
	 * the directory given as first parameter. 
	 */
	public void computeClassLevelSmellRules(File pDirectory, Set<ClassBean> pClassesToAnalyze, String pOutputPath, long pAuthorTime, long pCommitterTime, String pCommitHash, String pSystemType) {

		Vector<ClassBean> system = new Vector<ClassBean>(); 
		Vector<PackageBean> packages;
		try {
			
			packages = FolderToJavaProjectConverter.convert(pDirectory.getAbsolutePath());
			
			for(PackageBean packageBean: packages) {
				for(ClassBean classBean: packageBean.getClasses()) {
					system.add(classBean);
				}
			}
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
		for(ClassBean classBean: pClassesToAnalyze) {
			File blob = new File(pOutputPath+"/"+classBean.getFilePath()+"_blob_trend.csv");
			File cdsbp = new File(pOutputPath+"/"+classBean.getFilePath()+"_cdsbp_trend.csv");
			File cc = new File(pOutputPath+"/"+classBean.getFilePath()+"_complexClass_trend.csv");
			File fd = new File(pOutputPath+"/"+classBean.getFilePath()+"_fd_trend.csv");
			File sc = new File(pOutputPath+"/"+classBean.getFilePath()+"_spaghettiCode_trend.csv");

			int loc = CKMetrics.getLOC(classBean);
			int lcom = CKMetrics.getLCOM(classBean);
			int wmc = CKMetrics.getWMC(classBean);
			int rfc = CKMetrics.getRFC(classBean);
			int cbo = CKMetrics.getCBO(classBean);
			int nom = CKMetrics.getNOM(classBean);
			int noa = CKMetrics.getNOA(classBean);
			int nopa = CKMetrics.getNOPA(classBean);
			int dit = CKMetrics.getDIT(classBean, system, 0);
			int noc = CKMetrics.getNOC(classBean, system);

			boolean isGodClass = this.godClassRule.isGodClass(classBean, pSystemType);
			boolean isComplexClass = this.complexClassRule.isComplexClass(classBean, pSystemType);
			boolean isSpaghettiCode = this.spaghettiCodeRule.isSpaghettiCode(classBean, pSystemType);
			boolean isClassDataShouldBePrivate = this.classDataShouldBePrivateRule.isClassDataShouldBePrivate(classBean, pSystemType);
			boolean isFunctionalDecomposition = this.functionalDecompositionRule.isFunctionalDecomposition(classBean, pSystemType);

			writeFile(blob, classBean.getFilePath()+";"+isGodClass+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
			writeFile(cc, classBean.getFilePath()+";"+isComplexClass+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
			writeFile(sc, classBean.getFilePath()+";"+isSpaghettiCode+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
			writeFile(cdsbp, classBean.getFilePath()+";"+isClassDataShouldBePrivate+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+nopa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
			writeFile(fd, classBean.getFilePath()+";"+isFunctionalDecomposition+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);

		}
	}

	/**
	 * Old method for running class-level smells rules. 
	 */
	public void computeClassLevelSmellRules(Vector<PackageBean> pPackages, String pOutputPath, int pCommitNumber,
			long pAuthorTime, long pCommitterTime, String pCommitHash, String pSystemType) {

		Vector<ClassBean> system = new Vector<ClassBean>();

		for(PackageBean packageBean: pPackages) {
			for(ClassBean classBean: packageBean.getClasses()) {
				system.add(classBean);
			}
		}

		for(PackageBean packageBean: pPackages) {
			for(ClassBean classBean: packageBean.getClasses()) {
				File blob = new File(pOutputPath+"/"+classBean.getFilePath()+"_blob_trend.csv");
				File cdsbp = new File(pOutputPath+"/"+classBean.getFilePath()+"_cdsbp_trend.csv");
				File cc = new File(pOutputPath+"/"+classBean.getFilePath()+"_complexClass_trend.csv");
				File fd = new File(pOutputPath+"/"+classBean.getFilePath()+"_fd_trend.csv");
				File sc = new File(pOutputPath+"/"+classBean.getFilePath()+"_spaghettiCode_trend.csv");

				int loc = CKMetrics.getLOC(classBean);
				int lcom = CKMetrics.getLCOM(classBean);
				int wmc = CKMetrics.getWMC(classBean);
				int rfc = CKMetrics.getRFC(classBean);
				int cbo = CKMetrics.getCBO(classBean);
				int nom = CKMetrics.getNOM(classBean);
				int noa = CKMetrics.getNOA(classBean);
				int nopa = CKMetrics.getNOPA(classBean);
				int dit = CKMetrics.getDIT(classBean, system, 0);
				int noc = CKMetrics.getNOC(classBean, system);

				boolean isGodClass = this.godClassRule.isGodClass(classBean, pSystemType);
				boolean isComplexClass = this.complexClassRule.isComplexClass(classBean, pSystemType);
				boolean isSpaghettiCode = this.spaghettiCodeRule.isSpaghettiCode(classBean, pSystemType);
				boolean isClassDataShouldBePrivate = this.classDataShouldBePrivateRule.isClassDataShouldBePrivate(classBean, pSystemType);
				boolean isFunctionalDecomposition = this.functionalDecompositionRule.isFunctionalDecomposition(classBean, pSystemType);

				writeFile(blob, classBean.getFilePath()+";"+isGodClass+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
				writeFile(cc, classBean.getFilePath()+";"+isComplexClass+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
				writeFile(sc, classBean.getFilePath()+";"+isSpaghettiCode+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
				writeFile(cdsbp, classBean.getFilePath()+";"+isClassDataShouldBePrivate+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+nopa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);
				writeFile(fd, classBean.getFilePath()+";"+isFunctionalDecomposition+";"+loc+";"+lcom+";"+wmc+";"+rfc+";"+cbo+";"+nom+";"+noa+";"+dit+";"+noc+";"+pAuthorTime+";"+pCommitterTime+";"+pCommitHash);

			}
		}
	}

	public void computeClassLevelSmellRules(Vector<PackageBean> pPackages, int pCommitNumber,
			long pAuthorTime, long pCommitterTime, String pCommitHash, ArrayList<ClassArtifact> pArtifacts,
			ArrayList<String> pPaths, String pSystemType) {

		Vector<ClassBean> system = new Vector<ClassBean>();

		for(PackageBean packageBean: pPackages) {
			for(ClassBean classBean: packageBean.getClasses()) {
				system.add(classBean);
			}
		}

		for(PackageBean packageBean: pPackages) {
			for(ClassBean classBean: packageBean.getClasses()) {
				int loc = CKMetrics.getLOC(classBean);
				int lcom = CKMetrics.getLCOM(classBean);
				int wmc = CKMetrics.getWMC(classBean);
				int rfc = CKMetrics.getRFC(classBean);
				int cbo = CKMetrics.getCBO(classBean);
				int nom = CKMetrics.getNOM(classBean);
				int noa = CKMetrics.getNOA(classBean);
				int nopa = CKMetrics.getNOPA(classBean);
				int dit = CKMetrics.getDIT(classBean, system, 0);
				int noc = CKMetrics.getNOC(classBean, system);
				boolean isGodClass = this.godClassRule.isGodClass(classBean, pSystemType);
				boolean isComplexClass = this.complexClassRule.isComplexClass(classBean, pSystemType);
				boolean isSpaghettiCode = this.spaghettiCodeRule.isSpaghettiCode(classBean, pSystemType);
				boolean isClassDataShouldBePrivate = this.classDataShouldBePrivateRule.isClassDataShouldBePrivate(classBean, pSystemType);
				boolean isFunctionalDecomposition = this.functionalDecompositionRule.isFunctionalDecomposition(classBean, pSystemType);

				int index = Collections.binarySearch(pPaths,classBean.getName());

				if(index >= 0) {
					ClassArtifact artifact = pArtifacts.get(index);

					artifact.addCbo(cbo);
					artifact.addDit(dit);
					artifact.addLcom(lcom);
					artifact.addLoc(loc);
					artifact.addNoa(noa);
					artifact.addNoc(noc);
					artifact.addNom(nom);
					artifact.addNopa(nopa);
					artifact.addRfc(rfc);
					artifact.addWmc(wmc);

					artifact.addIsCDSBP(isClassDataShouldBePrivate);
					artifact.addIsComplexClass(isComplexClass);
					artifact.addIsGodClass(isGodClass);
					artifact.addIsSpaghettiCode(isSpaghettiCode);
					artifact.addIsFunctionalDecomposition(isFunctionalDecomposition);

					artifact.addCommitHash(pCommitHash);
					artifact.addAuthorTime(pAuthorTime);
					artifact.addCommitterTime(pCommitterTime);


				} else {
					ClassArtifact artifact = new ClassArtifact();

					artifact.setName(packageBean.getName()+"."+classBean.getName());
					artifact.addCbo(cbo);
					artifact.addDit(dit);
					artifact.addLcom(lcom);
					artifact.addLoc(loc);
					artifact.addNoa(noa);
					artifact.addNoc(noc);
					artifact.addNom(nom);
					artifact.addNopa(nopa);
					artifact.addRfc(rfc);
					artifact.addWmc(wmc);

					artifact.addIsCDSBP(isClassDataShouldBePrivate);
					artifact.addIsComplexClass(isComplexClass);
					artifact.addIsGodClass(isGodClass);
					artifact.addIsSpaghettiCode(isSpaghettiCode);
					artifact.addIsFunctionalDecomposition(isFunctionalDecomposition);

					artifact.addCommitHash(pCommitHash);
					artifact.addAuthorTime(pAuthorTime);
					artifact.addCommitterTime(pCommitterTime);

					pArtifacts.add((-index)-1, artifact);
					pPaths.add((-index)-1, artifact.getName());

				}
			}
		}

	}
	/*
	private int computeDependencies(ClassBean pClass, PackageBean pPackage) {
		int dependencies = 0;

		for(MethodBean methodBean: pClass.getMethods()) {

			for(MethodBean calledMethod: methodBean.getMethodCalls()) {

				for(ClassBean classBean: pPackage.getClasses()) {
					for(MethodBean classMb: classBean.getMethods()) {
						for(MethodBean calledClassMb: classMb.getMethodCalls()) {

							if(calledMethod.getName().equals(calledClassMb.getName()))
								dependencies++;
						}
					}
				}

			}
		}

		return dependencies;
	}

	private int computeDependencies(MethodBean pMethod, ClassBean pClass) {
		int dependencies = 0;

		for(MethodBean calledMethod : pMethod.getMethodCalls()) {

			for(MethodBean classMethod: pClass.getMethods()) {
				if(calledMethod.getName().equals(classMethod.getName())) 
					dependencies++;
			}

		}

		return dependencies;
	}

	private double computeSimilarity(MethodBean pMethod, ClassBean pClass) {
		CosineSimilarity cosineSimilarity= new CosineSimilarity();

		String[] methodInfo=new String[2];
		methodInfo[0] = pMethod.getName();
		methodInfo[1] = pMethod.getTextContent();

		String[] belongingClassInfo=new String[2];
		belongingClassInfo[0] = pClass.getName();
		belongingClassInfo[1] = pClass.getTextContent();

		try {
			return cosineSimilarity.computeSimilarity(methodInfo, belongingClassInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0.0;
	}

	private double computeSimilarity(ClassBean pClass, PackageBean pPackage) {
		CosineSimilarity cosineSimilarity= new CosineSimilarity();

		String[] classInfo=new String[2];
		classInfo[0] = pClass.getName();
		classInfo[1] = pClass.getTextContent();

		String[] belongingPackageInfo=new String[2];
		belongingPackageInfo[0] = pPackage.getName();
		belongingPackageInfo[1] = pPackage.getTextContent();

		try {
			return cosineSimilarity.computeSimilarity(classInfo, belongingPackageInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0.0;
	}
	 */
	private void writeFile(File pFile, String pContent) {
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(pFile, true);
			PrintWriter writer = new PrintWriter(fileWriter);

			writer.println(pContent);
			fileWriter.close();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}