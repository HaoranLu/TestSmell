package it.unisa.scam14.parser;

import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.PackageBean;

public class PackageParser {

	public static PackageBean parse(IPackageFragment pPackage) {
		PackageBean packageBean = new PackageBean();
		CodeParser codeParser = new CodeParser();
		String textualContent="";
		
		ArrayList<ClassBean> classes = new ArrayList<ClassBean>();

		packageBean.setName(pPackage.getElementName());

		try {
			for(ICompilationUnit cu: pPackage.getCompilationUnits()) {
				textualContent+=cu.getSource();
				
				CompilationUnit parsed = codeParser.createParser(cu.getSource());
				TypeDeclaration typeDeclaration = (TypeDeclaration)parsed.types().get(0);

				Vector<String> imported = new Vector<String>();

				for(IImportDeclaration importedResource: cu.getImports())
					imported.add(importedResource.getElementName());

				//TODO Add real filepath as last parameter of the call
				classes.add(ClassParser.parse(typeDeclaration, packageBean.getName(), imported, "TEST"));
			}
			
			packageBean.setTextContent(textualContent);
		
		} catch (JavaModelException e) {
			e.printStackTrace();
		}

		return packageBean;
	}
}