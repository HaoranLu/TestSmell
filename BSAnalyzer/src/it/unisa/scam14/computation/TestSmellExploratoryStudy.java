package it.unisa.scam14.computation;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.PackageBean;
import it.unisa.scam14.utility.FolderToJavaProjectConverter;
import it.unisa.scam14.utility.TestSmellUtilities;

import java.util.Vector;

import org.eclipse.core.runtime.CoreException;

public class TestSmellExploratoryStudy {
	
	private int total = 0;
	private int withProductionClass = 0;
	private int withoutProductionClass = 0;
	private String nameClassesWithoutProductionClass = "";
	
	public void runAnalysis(String pFolderPath){
		Vector<ClassBean> system = new Vector<ClassBean>();
		Vector<PackageBean> packages = null;
		
		try {
			//Convert the folder in a Java Project
			packages = FolderToJavaProjectConverter.convert(pFolderPath);	
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		//Create vector of all the classes in the system
		for(PackageBean packageBean: packages) {
			for(ClassBean classBean: packageBean.getClasses()) {
				system.add(classBean);
			}
		}
		
		//Find test cases and the relative production class
		for(PackageBean packageBean: packages) {
			for(ClassBean classBean: packageBean.getClasses()) {
				if(TestSmellUtilities.isTestCase(classBean)){
					total++;
					ClassBean pc = TestSmellUtilities.findProductionClassUsingNamingConventions(classBean, system);
					
					if(pc == null){
						withoutProductionClass++;
						nameClassesWithoutProductionClass += classBean.getName()+"\n";
					} else {
						withProductionClass++;
					}
				}
			}
		}
		
		
	}
	
	
	public String getNameClassesWithoutProductionClass() {
		return nameClassesWithoutProductionClass;
	}

	public void setNameClassesWithoutProductionClass(
			String nameClassesWithoutProductionClass) {
		this.nameClassesWithoutProductionClass = nameClassesWithoutProductionClass;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getWithProductionClass() {
		return withProductionClass;
	}

	public void setWithProductionClass(int withProductionClass) {
		this.withProductionClass = withProductionClass;
	}

	public int getWithoutProductionClass() {
		return withoutProductionClass;
	}

	public void setWithoutProductionClass(int withoutProductionClass) {
		this.withoutProductionClass = withoutProductionClass;
	}

}
