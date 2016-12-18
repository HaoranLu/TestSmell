package it.unisa.scam14.parser;

import java.util.Collection;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassVisitor extends ASTVisitor {
	
	private Collection<TypeDeclaration> classNodes;
	
	public ClassVisitor(Collection<TypeDeclaration> pClassNodes) {
		classNodes = pClassNodes;
	}
	
	public boolean visit(TypeDeclaration pClassNode) {
		classNodes.add(pClassNode);
		return true;
	}
	
}
