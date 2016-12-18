package it.unisa.scam14.parser;

import java.util.Collection;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import it.unisa.scam14.beans.MethodBean;

public class InvocationVisitor extends ASTVisitor {
	
	private Collection<MethodBean> methodsInvoked;
	
	public InvocationVisitor(Collection<MethodBean> pInvocations) {
		methodsInvoked = pInvocations;
	}
	
	public boolean visit(MethodInvocation pInvocationNode) {
		MethodBean methodBean = new MethodBean();
		methodBean.setName(pInvocationNode.getName().toString());
		methodBean.setTextContent(pInvocationNode.toString());
		
		methodsInvoked.add(methodBean);
		
		return true;
	}
	
	public boolean visit(TypeDeclaration pClassNode) {
		return false;
	}
	
	@SuppressWarnings("unused")
	private boolean isLocalInvocation(MethodInvocation pInvocationNode) {
		
		// Get the important data of the invocation
		String invocation = pInvocationNode.toString();
		String invocationName = pInvocationNode.getName().toString();
		int index = invocation.indexOf(invocationName);
		
		// The invocation has not a qualifier
		if (index == 0)
			return true;
		
		// The invocation has this as qualifier
		else if (index >= 5 && invocation.substring(index-5, index-1).equals("this"))
			return true;
		
		// The invocation has some other qualifier
		else
			return false;
		
	}
	
}
