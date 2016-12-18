package it.unisa.scam14.utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.beans.PackageBean;
import it.unisa.scam14.parser.ClassParser;
import it.unisa.scam14.parser.CodeParser;
import it.unisa.scam14.parser.MethodParser;
import jdk.management.resource.internal.inst.FileChannelImplRMHooks;

public class TestSmellUtilities {

	public static boolean hasTestClasses(File pWorkingDirectory) {
		Vector<File> javaFiles = TestSmellUtilities.listJavaFiles(pWorkingDirectory);

		for(File javaFile: javaFiles) {
			try {
				String content = FileUtility.readFile(javaFile.getAbsolutePath());

				if(content.contains(" extends TestCase")) {
					return true;
				} else {
					return false;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}			
		}

		return false;
	}
/**Modified Origin method to make sure single file will be processed correctly.
 * <p>
 * The file name must end with ".java" since we compare the file to identify different file
 * @author Haoran Lu
 * @param pWorkingDirectory can be a single file.
 * 
 */
	public static List<ClassBean> extractTestCases(File pWorkingDirectory) {
		boolean isSingleFile = !pWorkingDirectory.isDirectory();// In case the pWorkingDirectory is a single file.
		String SingleFile = null;
		if (isSingleFile) {
			SingleFile = pWorkingDirectory.getName();
			pWorkingDirectory = pWorkingDirectory.getParentFile();
		}
		Vector<File> javaFiles = TestSmellUtilities.listJavaFiles(pWorkingDirectory);
		List<ClassBean> testCases = new ArrayList<ClassBean>();

		for(File javaFile: javaFiles) {
			try {
				String content = FileUtility.readFile(javaFile.getAbsolutePath());

				if(content.contains(" extends TestCase")) {
					CodeParser codeParser = new CodeParser();
					CompilationUnit cu = codeParser.createParser(content);
					TypeDeclaration typeDeclaration = (TypeDeclaration)cu.types().get(0);

					//Get file Path
					String filePath = pWorkingDirectory.toURI().relativize(javaFile.toURI()).getPath();
					filePath = filePath.substring(0, filePath.lastIndexOf('.'));

					Vector<String> imports = new Vector<String>();
					for(Object importedResource: cu.imports()){
						imports.add(importedResource.toString());
					}

					ClassBean testCase = ClassParser.parse(typeDeclaration, cu.getPackage().getName().getFullyQualifiedName(), imports, filePath);
					testCases.add(testCase);
				} 

			} catch (Exception e) {
				System.out.println("Error parsing the CompilationUnit");
				e.printStackTrace();
				break;
			}			
		}
		if (isSingleFile) {
			List<ClassBean> SingleFileTestCase = new ArrayList<ClassBean>();
			for (ClassBean classBean : testCases) {
				if (classBean.getName().equalsIgnoreCase(SingleFile.substring(0, SingleFile.length()-5))) {
					SingleFileTestCase.add(classBean);
					return SingleFileTestCase;
				}
			}
			return SingleFileTestCase;
		}
		return testCases;
	}

	public static boolean isTestCase(ClassBean pClassBean) {
		if(pClassBean.getTextContent().contains(" extends TestCase")) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 1. Identificare la prima PC candidata con le naming convention X
	   2. Check nome della classe e' nel TC X
	   3. Lista dei metodi chiamati dal TC X
	   4. Check metodi di TC nella PC candidata X
	   5. Check dei metodi restanti nelle classi import X
	   6. Check dei metodi restanti in tutte le altre classi (con doppio check del nome della classe nel TC)
	 * 
	 */
	public static Collection<ClassBean> findProductionClassUsingDependencies(ClassBean pTestClass, Vector<ClassBean> pSystem) {
		Collection<ClassBean> productionClasses = new ArrayList<ClassBean>();
		ArrayList<String> imported = new ArrayList<String>();
		ClassBean principalCandidateProductionClass = TestSmellUtilities.findProductionClassUsingNamingConventions(pTestClass, pSystem);

		for(String imp: pTestClass.getImports()) {
			imported.add(imp.substring(imp.indexOf(" ")+1, imp.length()-2));
		}

		for(MethodBean testMethod: pTestClass.getMethods()) {
			if( (!testMethod.getName().equals("setUp")) && (!testMethod.getName().equals("tearDown"))) {
				Collection<MethodBean> called = testMethod.getMethodCalls();

				for(MethodBean call: called) {

					// Step 1: Check in the production class respecting the naming conventions.
					// Step 2: Check that the production class name is in the test class
					if(principalCandidateProductionClass != null) {
						if(pTestClass.getTextContent().contains(principalCandidateProductionClass.getName())) { 

							for(MethodBean methodBean: principalCandidateProductionClass.getMethods()) {
								// Step 4: Check that a method called by the test case is in the production class
								if(methodBean.getName().equals(call.getName())) {
									call.setBelongingClass(methodBean.getBelongingClass());

									if(! isAlreadyIn(productionClasses, call.getBelongingClass()))
										productionClasses.add(call.getBelongingClass());	
								}

							}
						}
					} else {
						// Step 5: Check in the production classes imported by the test class.
						for(ClassBean classBean : pSystem) {
							if(imported.contains(classBean.getBelongingPackage()+"."+classBean.getName())) {
								for(MethodBean methodBean : classBean.getMethods()) {
									if(methodBean.getName().equals(call.getName())) {
										call.setBelongingClass(methodBean.getBelongingClass());
										if(! isAlreadyIn(productionClasses, call.getBelongingClass()))
											productionClasses.add(call.getBelongingClass());	
									}
								}
							}
						}

						// Step 6: If the method call has not cover yet, then I check all the other classes in the system.
						if(call.getBelongingClass()==null) {
							for(ClassBean classBean : pSystem) {
								if(pTestClass.getTextContent().contains(classBean.getName())) {
									for(MethodBean methodBean : classBean.getMethods()) {
										if(methodBean.getName().equals(call.getName())) {

											int commas = 0;
											for(int i = 0; i < call.getTextContent().length(); i++) {
												if(call.getTextContent().charAt(i) == ',') commas++;
											}

											if((commas+1) == methodBean.getParameters().size()) {
												call.setBelongingClass(methodBean.getBelongingClass());
												if(! isAlreadyIn(productionClasses, call.getBelongingClass())) {
													productionClasses.add(call.getBelongingClass());
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return productionClasses;
	}

	private static boolean isAlreadyIn(Collection<ClassBean> pCollections, ClassBean pClassBean) {

		for(ClassBean cb: pCollections) {
			if(cb.getFilePath().equals(pClassBean.getFilePath())) {
				return true;
			}
		}

		return false;
	}

	public static ClassBean findProductionClassUsingNamingConventions(ClassBean pTestClass, Vector<ClassBean> pSystem) {

		for(ClassBean classBean: pSystem) {
			String candidateTestName=classBean.getName()+"Test";
			String candidateTestName2="Test"+classBean.getName();

			String candidateTestName3="Tests"+classBean.getName();
			String candidateTestName4=classBean.getName()+"Tests";

			String candidateTestName5="TC_"+classBean.getName();
			String candidateTestName6=classBean.getName()+"_TC";

			String candidateTestName7=classBean.getName().replaceAll("_", "")+"Test";
			String candidateTestName8="Test"+classBean.getName().replaceAll("_", "");

			String candidateTestName9=classBean.getName()+"TestCase";
			String candidateTestName10="TestCase"+classBean.getName();

			if(candidateTestName.toLowerCase().equals(pTestClass.getName().toLowerCase())) {
				return classBean;
			} else if(candidateTestName2.toLowerCase().equals(pTestClass.getName().toLowerCase())) {
				return classBean;
			} else if(candidateTestName3.toLowerCase().equals(pTestClass.getName().toLowerCase())) {
				return classBean;
			} else if(candidateTestName4.toLowerCase().equals(pTestClass.getName().toLowerCase())) {
				return classBean;
			} else if(candidateTestName5.toLowerCase().equals(pTestClass.getName().toLowerCase())) {
				return classBean;
			} else if(candidateTestName6.toLowerCase().equals(pTestClass.getName().toLowerCase())) {
				return classBean;
			} else if(candidateTestName7.toLowerCase().equals(pTestClass.getName().toLowerCase())) {
				return classBean;
			} else if(candidateTestName8.toLowerCase().equals(pTestClass.getName().toLowerCase())) {
				return classBean;
			} else if(candidateTestName9.toLowerCase().equals(pTestClass.getName().toLowerCase())) {
				return classBean;
			} else if(candidateTestName10.toLowerCase().equals(pTestClass.getName().toLowerCase())) {
				return classBean;
			}

		}

		return null;
	}

	private static Vector<File> listJavaFiles(File pDirectory) {
		Vector<File> javaFiles=new Vector<File>(); 
		File[] fList = pDirectory.listFiles();

		if(fList != null) {
			for (File file : fList) {
				if (file.isFile()) {
					if(file.getName().contains(".java")) {
						javaFiles.add(file);
					}
				} else if (file.isDirectory()) {
					File directory = new File(file.getAbsolutePath());
					javaFiles.addAll(listJavaFiles(directory));
				}
			}
		}
		return javaFiles;
	}

	public static MethodBean getProductionMethodBy(MethodBean pTestMethod, Vector<ClassBean> pSystem) {
		for(ClassBean classBean : pSystem) {
			for(MethodBean methodBean : classBean.getMethods()) {
				if(methodBean.getName().equals(pTestMethod.getName())) {
					if(classBean.getFilePath().equals(pTestMethod.getBelongingClass().getFilePath())) {
						return methodBean;
					}
				}
			}
		}

		return null;
	}
}